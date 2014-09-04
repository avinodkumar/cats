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
package com.comcast.cats.recorder.response;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.comcast.cats.recorder.VideoRecorderTask;

/**
 * Yaml deserialization test.
 * 
 * <pre>
 * REQUEST : http://localhost:8080/video-recorder-service/rest/internal/recorder/task/list
 * RESPONSE TYPE: YAML
 * </pre>
 * 
 * @author ssugun00c
 * 
 */
public class CurrentTasksDeserializationTest
{
    private Logger logger = LoggerFactory.getLogger( getClass() );

    @SuppressWarnings( "unchecked" )
    @Test
    public void testRecordingHistoryYaml() throws Exception
    {
        final Yaml yaml = new Yaml();
        Reader reader = null;
        try
        {
            reader = new FileReader( "src/test/resources/current-tasks.yaml" );
            Object obj = yaml.load( reader );

            Assert.assertNotNull( obj );
            logger.info( "[OBJECT][" + obj + "]" );

            Map< Integer, VideoRecorderTask > vlcTasks = ( Map< Integer, VideoRecorderTask > ) obj;

            for ( int key : vlcTasks.keySet() )
            {
                logger.info( key + " = " + vlcTasks.get( key ) );
            }
        }
        catch ( final FileNotFoundException e )
        {
            logger.error( e.getMessage() );
        }
        finally
        {
            if ( null != reader )
            {
                try
                {
                    reader.close();
                }
                catch ( final IOException e )
                {
                    logger.error( e.getMessage() );
                }
            }
        }
    }
}
