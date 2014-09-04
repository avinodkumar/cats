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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderResponse;

/**
 * XML deserialization test.
 * 
 * <pre>
 * REQUEST : http://localhost:8080/video-recorder-service/rest/public/recorder/status?macId=E4:48:C7:A8:1A:56&videoServerIp=192.168.160.202&port=1
 * RESPONSE TYPE: XML
 * </pre>
 * 
 * @author ssugun00c
 * 
 */
public class RecordingStatusDeserializationTest
{
    private Logger logger = LoggerFactory.getLogger( getClass() );

    @Test
    public void testRecordingHistory() throws Exception
    {
        VideoRecorderResponse recorderResponse = null;
        InputStream inputStream = null;

        try
        {
            JAXBContext jc = JAXBContext.newInstance( VideoRecorderResponse.class );
            Unmarshaller um = jc.createUnmarshaller();

            inputStream = new FileInputStream( "src/test/resources/recorder-status.xml" );

            recorderResponse = ( VideoRecorderResponse ) um.unmarshal( inputStream );
        }
        catch ( JAXBException jaxbException )
        {
            jaxbException.printStackTrace();
        }
        catch ( FileNotFoundException fileNotFoundException )
        {
            fileNotFoundException.printStackTrace();
        }
        finally
        {
            if ( null != inputStream )
            {
                inputStream.close();
            }
        }

        logger.info( "[VideoRecorderResponse]" + recorderResponse );
    }
}
