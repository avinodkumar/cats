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
package com.comcast.cats.config.ui.recording;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.ManagedBean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;

/**
 * This is to abstract MediaINfo in video-recorder so that changes there wont
 * affect CATS-UI.
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
public class MediaInfoBean
{
    Date                  createdDate       = null;
    Date                  lastModifiedDate  = null;
    String                filePath          = null;
    String                directory         = null;
    public String         fileName          = DEFAULT_FILE_NAME;
    public int            fileSize          = DEFAULT_FILE_SIZE;
    int                   duration          = 0;
    static final String   DEFAULT_FILE_NAME = "";
    static final int     DEFAULT_FILE_SIZE = 0;
    int id;
    SettopRecordingBean recording;
    boolean playable = false;

    private static Logger logger            = LoggerFactory.getLogger( MediaInfoBean.class );

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate( Date createdDate )
    {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    public void setLastModifiedDate( Date lastModifiedDate )
    {
        this.lastModifiedDate = lastModifiedDate;
    }

    public SettopRecordingBean getRecording()
    {
        return recording;
    }

    public void setRecording( SettopRecordingBean recording )
    {
        this.recording = recording;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath( String filePath )
    {
        //FIXME: once video recorder service returns the actual path isntead of 127.0.0.1
        this.filePath = substituteFilePath(filePath);
        try
        {
            setFileName( this.filePath.substring( this.filePath.lastIndexOf( "/" ) + 1 ) );
            setDirectory( this.filePath.substring( 0, this.filePath.lastIndexOf( "/" ) ) );
        }
        catch ( StringIndexOutOfBoundsException e )
        {
            logger.debug( "Invalid Filename " + this.filePath + " meesage: " + e.getMessage() );
            // invalid Filename.
            setFileName( DEFAULT_FILE_NAME );
        }
    }

    private String substituteFilePath(String filePath){
        String retVal = filePath;
        try{
            URL filePathURL = new URL( filePath );
            String host = filePathURL.getHost();
            retVal = StringUtils.replaceOnce( filePath, host, AuthController.getHostAddress() );
        }catch(MalformedURLException e){
            logger.debug( "Provider doesnt know how to parse this syntax" );
        }
        return retVal;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public void setFileSize( int fileSize )
    {
        this.fileSize = fileSize;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory( String directory )
    {
        this.directory = directory;
    }

    public void setDuration( int duration )
    {
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public boolean isPlayable()
    {
        return playable;
    }

    public void setPlayable( boolean playable )
    {
        this.playable = playable;
    }
    
    @Override
    public boolean equals(Object object){
    	boolean retVal = false;
    	
    	if(object instanceof MediaInfoBean){
    		if(((MediaInfoBean) object).getId() == this.getId()){
    			retVal = true;
    		}
    	}
    	
    	return retVal;
    }
}
