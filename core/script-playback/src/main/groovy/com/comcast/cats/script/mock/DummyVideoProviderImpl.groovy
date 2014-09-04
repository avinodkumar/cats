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
package com.comcast.cats.script.mock

import java.awt.Dimension
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.Dimension;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.comcast.cats.Settop;
import com.comcast.cats.provider.VideoProvider;

class DummyVideoProviderImpl implements VideoProvider
{

    @Override
    public Object getParent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void connectVideoServer()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnectVideoServer()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public List< ? > getAvailableVideoSizes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getFrameRate()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BufferedImage getVideoImage()
    {
        File theFile = new File( "src/main/resources/sampleImage.jpg" );
        BufferedImage img = ImageIO.read( theFile );
        return img;
    }

	@Override
    public BufferedImage getVideoImage(Dimension dim)
    {
        File theFile = new File( "src/main/resources/sampleImage.jpg" );
        BufferedImage img = ImageIO.read( theFile );
        return img;
    }

    @Override
    public URI getVideoLocator()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dimension getVideoSize()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVideoURL()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isConnected()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStreaming()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setFrameRate( Integer arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVideoSize( Dimension arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String saveVideoImage()
    {
        String catsHome = "D:/CATSHOME"
        String fileSeperator =  System.getProperty("file.separator");
        
        String fileName = catsHome + fileSeperator + getFolderAndFileNames()        
        saveVideoImage(fileName);
    }
    
    
    private String getFolderAndFileNames (){
        String fileSeperator =  System.getProperty("file.separator");
        
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
        String dateString =  dateFormat.format( date );
        
        getCleanMac("00:00:00:AA:AA:AA") + fileSeperator + dateString + ".jpg";
    }    

    @Override
    public void saveVideoImage( String file )
    {
        if(null == file || file.isEmpty())
        {
           println( "The filePath cannot be null or empty" );
        }
        else{
            File outputfile = new File( file )
            if( outputfile.exists() && outputfile.isDirectory()){
                file = file + System.getProperty("file.separator") + getFolderAndFileNames()
                outputfile = new File( file )
            }
            try{
                FileUtils.forceMkdir( outputfile.getAbsoluteFile().getParentFile() );
                ImageIO.write( getVideoImage(), "jpg", outputfile )
                println "Saved image to file : ${outputfile.getAbsolutePath()}"
            }
            catch(Exception e){
                println "Error in creating file. ${e.getMessage()}"
            }
        }
    }

    private String getCleanMac(String mac)
    {
        return mac.trim().replace( ":", "" ).toUpperCase();
    }
}