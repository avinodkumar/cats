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
package com.comcast.cats.recorder.domain.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;

/**
 * Default implementation of {@link MediaMetaDataEntityService}.
 * 
 * @author SSugun00c
 * 
 */
@Stateless
@Remote( MediaMetaDataEntityService.class )
@TransactionAttribute( TransactionAttributeType.REQUIRED )
public class DefaultMediaMetaDataEntityService extends BaseEntityService implements MediaMetaDataEntityService
{
    @PersistenceContext( unitName = PERSISTENCE_UNIT )
    private EntityManager entityManager;

    @Override
    public int deleteByRecordingId( Integer recordingId )
    {
        String queryStr = "SELECT mediaMetaData FROM MediaMetaData mediaMetaData WHERE mediaMetaData.recordingEntity.id=:recordingId";
        Query query = entityManager.createQuery( queryStr );
        query.setParameter( "recordingId", recordingId );

        @SuppressWarnings( "unchecked" )
        List< MediaMetaData > mediaList = ( List< MediaMetaData > ) query.getResultList();

        List< String > fileList = new ArrayList< String >();

        for ( MediaMetaData mediaMetaData : mediaList )
        {
            fileList.add( mediaMetaData.getFilePath() );
            entityManager.refresh( mediaMetaData );
        }

        deleteFiles( fileList );

        return fileList.size();
    }

    private void deleteFiles( List< String > fileList )
    {
        int count = 0;

        for ( String filepath : fileList )
        {
            if ( deleteFile( filepath ) )
            {
                count++;
            }
        }

        LOGGER.info( count + " file(s) deleted" );
    }

    private boolean deleteFile( String filepath )
    {
        boolean deleted = false;

        try
        {
            File file = new File( filepath );

            LOGGER.info( filepath + " is being deleted" );

            if ( file.delete() )
            {
                deleted = true;
            }
        }
        catch ( Exception e )
        {

            e.printStackTrace();

        }

        return deleted;
    }

    @Override
    public int deleteById( Integer mediaMetaDataId ) throws RecorderNotFoundException
    {
        MediaMetaData mediaMetaData = ( MediaMetaData ) entityManager.find( MediaMetaData.class, mediaMetaDataId );

        if ( null == mediaMetaData )
        {
            throw new RecorderNotFoundException( "No media found with mediaMetaDataId [" + mediaMetaDataId + "]" );
        }
        else
        {
            Query query = entityManager.createNamedQuery( "MediaMetaData.deleteById" );
            query.setParameter( "mediaMetaDataId", mediaMetaDataId );
            return query.executeUpdate();
        }
    }
}
