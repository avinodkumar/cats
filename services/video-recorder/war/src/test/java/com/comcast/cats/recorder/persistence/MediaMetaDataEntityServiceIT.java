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

import java.util.List;

import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.cats.recorder.domain.MediaMetaData;
import com.comcast.cats.recorder.domain.service.DefaultMediaMetaDataEntityService;

/**
 * Integration tests for {@link DefaultMediaMetaDataEntityService}.
 * 
 * @author SSugun00c
 * 
 */
public class MediaMetaDataEntityServiceIT extends BaseEntityServiceIT
{
    @Test
    public void testQuery() throws Exception
    {
        int recordingId = 1;

        String queryStr = "SELECT mediaMetaData FROM MediaMetaData mediaMetaData WHERE mediaMetaData.recordingEntity.id=:recordingId";
        Query query = entityManager.createQuery( queryStr );
        query.setParameter( "recordingId", Integer.valueOf( recordingId ) );

        @SuppressWarnings( "unchecked" )
        List< MediaMetaData > mediaList = ( List< MediaMetaData > ) query.getResultList();

        Assert.assertNotNull( mediaList );

        for ( MediaMetaData mediaMetaData : mediaList )
        {
            logger.info( mediaMetaData.toString() );
        }
    }
}
