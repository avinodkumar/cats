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
package com.comcast.cats.recorder.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.recorder.domain.service.DefaultRecordingEntityService;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Integration test for {@link DefaultRecordingEntityService}.
 * 
 * @author SSugun00c
 * 
 */
public class RecordingEntityServiceIT extends BaseEntityServiceIT
{
    @Test
    public void testQuery() throws Exception
    {
        String macId = "54:D4:6F:96:D9:BC";

        String queryStr = "SELECT recording FROM Recording recording WHERE recording.stbMacAddress=:stbMacAddress";
        Query query = entityManager.createQuery( queryStr );

        query.setParameter( "stbMacAddress", macId );

        @SuppressWarnings( "unchecked" )
        List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        Assert.assertNotNull( recordingList );

        for ( Recording recording : recordingList )
        {
            logger.info( recording.toString() );
        }
    }

    @Test
    public void testDateQuery() throws Exception
    {
        String queryStr = "SELECT recording FROM Recording recording WHERE recording.createdTime <= :createdTime";
        Query query = entityManager.createQuery( queryStr );

        Date createdDate = new Date();

        query.setParameter( "createdTime", createdDate );

        @SuppressWarnings( "unchecked" )
        List< Recording > recordingList = ( List< Recording > ) query.getResultList();

        for ( Recording recording : recordingList )
        {
            logger.info( recording.toString() );
        }
    }

    @Test
    public void testNoResultQuery() throws Exception
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse();

        String macId = "24:76:7D:13:D3:54";
        final Query query = entityManager.createNamedQuery( "Recording.findAllRecordingByMacIdWithLatestFirst" );
        query.setParameter( "stbMacAddress", macId );

        try
        {
            final Recording recording = ( Recording ) query.setMaxResults( 1 ).getSingleResult();

            LOGGER.info( "Latest recording found for [" + macId + "] - " + recording );
            // videoRecorderResponse = checkCurrentStatus( recording );
        }
        catch ( NoResultException e )
        {
            LOGGER.info( "NO recording found for [" + macId + "] - " );
            videoRecorderResponse.setResult( WebServiceReturnEnum.FAILURE );
            videoRecorderResponse.setMessage( "No recording information found for [" + macId + "]" );
        }

        logger.info( videoRecorderResponse.toString() );

        // This line should produce a NPE.
        logger.info( "[EJB3][STATUS][" + macId + "][" + videoRecorderResponse.getResult() + "]["
                + videoRecorderResponse.getRecording().getRecordingStatus().getId() + "]["
                + videoRecorderResponse.getRecording().getRecordingStatus().getState() + "]" );

    }

}
