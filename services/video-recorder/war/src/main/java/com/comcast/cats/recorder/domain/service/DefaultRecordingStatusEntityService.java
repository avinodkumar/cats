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

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.comcast.cats.recorder.domain.RecordingStatus;
import com.comcast.cats.recorder.exception.RecorderNotFoundException;

/**
 * Default implementation of {@link RecordingStatusEntityService}.
 * 
 * @author SSugun00c
 * 
 */
@Stateless
@Remote( RecordingStatusEntityService.class )
@TransactionAttribute( TransactionAttributeType.REQUIRED )
public class DefaultRecordingStatusEntityService extends BaseEntityService implements RecordingStatusEntityService
{
    @PersistenceContext( unitName = PERSISTENCE_UNIT )
    private EntityManager entityManager;

    @Override
    public void deleteByRecordingId( Integer recordingId ) throws RecorderNotFoundException
    {
        // recordingId =recordingStatusId
        RecordingStatus recordingStatus = entityManager.find( RecordingStatus.class, recordingId );

        if ( null == recordingStatus )
        {
            throw new RecorderNotFoundException( "Cannot find RecordingStatus of Recording " + recordingId );
        }

        entityManager.remove( recordingStatus );
    }
}
