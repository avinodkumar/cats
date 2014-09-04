/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.reboot.service;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.reboot.RebootDetectionCategory;
import com.comcast.cats.reboot.RebootDetectionStatus;
import com.comcast.cats.reboot.RebootHostStatus;
import com.comcast.cats.reboot.model.Host;
import com.comcast.cats.reboot.model.DetectionInfo;

/**
 * This class manages the persistence operations surrounding reboot detection.
 * 
 * @author cfrede001
 * 
 */
@Stateless
public class RebootDetectionService
{
    public static int       MAX_REBOOT_DETECTION_RESULTS = 200;
    protected static Logger logger                       = LoggerFactory.getLogger( RebootDetectionService.class );

    @PersistenceContext( name = "reboot-detection-pu" )
    protected EntityManager em;

    public List< Host > getHosts( RebootHostStatus status )
    {
        List< Host > responseList = null;

        logger.trace( "getHosts()" );
        try
        {
            TypedQuery< Host > query = em.createQuery( "SELECT h FROM Host h WHERE h.status = :status", Host.class );

            query.setParameter( "status", status );
            responseList = query.getResultList();
        }
        catch ( NoResultException e )
        {
            logger.warn( "No Hosts Found: {}", e );
        }
        return responseList;
    }

    public List< Host > getEnabledHosts()
    {
        return getHosts( RebootHostStatus.ENABLED );
    }

    public List< DetectionInfo > getDetectionInfo( String mac )
    {
        return getDetectionInfo( mac, 0, MAX_REBOOT_DETECTION_RESULTS );
    }

    public List< DetectionInfo > getDetectionInfo( String mac, Integer offset, Integer count )
    {
        List< DetectionInfo > detections = null;
        try
        {
            TypedQuery< DetectionInfo > query = em
                    .createNamedQuery( "DetectionInfo.FindByHostMac", DetectionInfo.class );
            query.setParameter( "macAddress", mac );
            query.setFirstResult( offset );
            query.setMaxResults( count );
            detections = query.getResultList();
        }
        catch ( NoResultException e )
        {}
        return detections;
    }

    /**
     * Retrieve the current RebootDetectionCount for a given host.
     * 
     * @param mac
     * @return
     */
    public Integer getRebootDetectionCount( String mac )
    {
        try
        {
            Query query = em.createNamedQuery( "DetectionInfo.CountByHostMac" );
            query.setParameter( "macAddress", mac );
            query.setMaxResults( 1 );
            Long count = ( Long ) query.getSingleResult();
            return count.intValue();
        }
        catch ( NoResultException e )
        {}
        return -1;
    }

    public void delete( DetectionInfo detection )
    {
        em.remove( detection );
    }

    public DetectionInfo getLastDetectionInfo( String mac )
    {
        DetectionInfo detection = null;
        try
        {
            Query query = em.createNamedQuery( "DetectionInfo.FindByHostMac" );
            query.setParameter( "macAddress", mac );
            query.setMaxResults( 1 );
            detection = ( DetectionInfo ) query.getSingleResult();
        }
        catch ( NoResultException e )
        {}
        return detection;
    }

    public void processUptime( Host host, Long uptime, RebootDetectionStatus status )
    {
        DetectionInfo info = new DetectionInfo();
        info.setCategory( RebootDetectionCategory.SNMP );
        info.setHost( host );
        info.setExecutionDate( new Date() );
        // Problem with request, what is the status.
        Long lastUptime = host.getUptime();
        if ( uptime < 0 )
        {
            // Use incoming status that code reflect potential errors.
            info.setStatus( status );
            /*
             * Host uptime should not be set, since it must reflect the last
             * valid value.
             */
        }
        else if ( uptime <= lastUptime )
        {
            info.setStatus( RebootDetectionStatus.REBOOT_DETECTED );
            host.setLastModified( new Date() );
            host.setUptime( uptime );
        }
        else
        {
            /*
             * If uptime is > than previous uptime then it was a successful
             * request.
             */
            host.setLastModified( new Date() );
            host.setUptime( uptime );
            info.setStatus( RebootDetectionStatus.SUCCESSFUL_REQUEST );
        }
        info.setUpTime( uptime );
        logger.trace( "Updating the Host" );
        process( host, info );
    }

    public void process( Host host, DetectionInfo info )
    {
        logger.trace( "process({}, {})", host, info );
        info.setHost( host );
        // Reboot has been detected, save to database.
        if ( info.getStatus() == RebootDetectionStatus.REBOOT_DETECTED )
        {
            logger.trace( "Save Reboot[{}]", info.toString() );
        }
        else
        {
            // Find the most current RebootDetection Information.
            DetectionInfo last = getLastDetectionInfo( host.getMacAddress() );
            if ( last != null && last.getStatus().equals( info.getStatus() ) )
            {
                logger.trace( "Delete last[{}]", last.toString() );
                delete( last );
            }
            // Always save the most recent detection information.
            logger.trace( "Save Info[{}]", info.toString() );
        }
        update( host );
        em.persist( info );
    }

    public void delete( String mac )
    {
        Host h = getHostByMac( mac );
        if ( h != null )
        {
            em.remove( h );
        }
    }

    public Host update( Host h )
    {
        return em.merge( h );
    }

    /**
     * Handle host information based on the provided information.
     * 
     * @param mac
     * @param ip
     * @param ecm
     * @param status
     * @return
     */
    public Host saveOrUpdate( String mac, String ip, String ecm, RebootHostStatus status )
    {
        Host host = getHostByMac( mac );
        if ( host == null )
        {
            host = new Host( mac, ip, ecm, status );
            em.persist( host );
        }
        else
        {
            host.setIpAddress( ip );
            host.setEcm( ecm );
            host.setStatus( status );
            // Update are host entry.
            em.merge( host );
        }
        return host;
    }

    /**
     * Retrieve the host by a given mac address.
     * 
     * @param mac
     * @return
     */
    public Host getHostByMac( String mac )
    {
        Host host = null;
        try
        {
            Query query = em.createNamedQuery( "Host.FindByMac" );
            query.setParameter( "macAddress", mac );
            host = ( Host ) query.getSingleResult();
        }
        catch ( NoResultException e )
        {}
        return host;
    }
}
