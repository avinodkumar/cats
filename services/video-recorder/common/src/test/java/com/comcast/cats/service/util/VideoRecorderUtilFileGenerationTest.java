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
package com.comcast.cats.service.util;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;

/**
 * Unit tests for file name generation logic.
 * 
 * @author ssugun00c
 * 
 */
public class VideoRecorderUtilFileGenerationTest
{
    private Logger              logger                        = LoggerFactory.getLogger( getClass() );

    private String              macId                         = "E4:48:C7:A8:2A:F2";
    private Integer             port                          = 2;
    private String              videoServerIp                 = "192.168.160.201";
    private String              alias                         = "test my @ cat's 12345%1234$123#9011(){}[]1234";

    private static final String VIDEO_RECORDING_BASE_DIR      = "E:/temp/test/";
    private static final String VIDEO_RECORDING_HTTP_BASE_URL = "http://localhost/";

    @Before
    public void setup()
    {
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH,
                VIDEO_RECORDING_BASE_DIR );
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH,
                VIDEO_RECORDING_HTTP_BASE_URL );
    }

    @Test
    public void testFileGenerationTest() throws Exception
    {
        int fileCount = 1;

        String initialFilePath = VideoRecorderUtil.getFilePath( macId, videoServerIp, port, alias, fileCount );
        logger.info( "[initialFilePath] " + initialFilePath );

        String initialHttpPath = VideoRecorderUtil.getHttpPath( initialFilePath );
        logger.info( "[initialHttpPath] " + initialHttpPath );

        String folderPath = VideoRecorderUtil.getFolderPathFromFilePath( initialFilePath );
        logger.info( "[folderPath] " + folderPath );

        String initialFileName = VideoRecorderUtil.getFilePathFromFilePath( initialFilePath );
        logger.info( "[initialFileName] " + initialFileName );

        fileCount++;

        String subsequentFilePath = VideoRecorderUtil.getSubsequentFile( initialFilePath, fileCount ).getAbsolutePath()
                .trim();
        logger.info( "[subsequentFilePath] " + subsequentFilePath );

    }
}
