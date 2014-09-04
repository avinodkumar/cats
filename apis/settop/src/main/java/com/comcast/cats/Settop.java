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
package com.comcast.cats;

import org.slf4j.Logger;

import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.AudioProvider;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.OCRProvider;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.RFControlProvider;
import com.comcast.cats.provider.RecorderProvider;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.VideoSelectionProvider;
import com.comcast.cats.recorder.Recorder;

/**
 * High level interface (Controller) for all settop operations, which is mostly
 * an aggregation of various hardware providers like Remote, Power, Audio, etc.
 * This interface should NOT provide setters for settop attributes. This
 * interface should be created by the factory and then used by things like CATS
 * Vision, TestNG and other client interfaces. If we begin exposing the details
 * of this class it will become brittle over time.
 * 
 * @author cfrede001
 */
public interface Settop extends SettopInfo, PowerProvider, RemoteProvider, RecorderProvider, RFControlProvider
{

    /**
     * @return PowerProvider of the settop.
     */
    public PowerProvider getPower();

    /**
     * @return RemoteProvider of the settop.
     */
    public RemoteProvider getRemote();

    /**
     * @return AudioProvider of the settop.
     */
    public AudioProvider getAudio();

    /**
     * @return TraceProvider of the settop.
     */
    public TraceProvider getTrace();

    /**
     * @return VideoProvider of the settop.
     */
    public VideoProvider getVideo();

    /**
     * @return VideoSelectionProvider of the settop.
     */
    public VideoSelectionProvider getVideoSelection();

    /**
     * @return details of the settop.
     */
    public SettopInfo getSettopInfo();

    /**
     * @return ImageCompareProvider of the settop.
     */
    public ImageCompareProvider getImageCompareProvider();

    /**
     * @return OCRProvider of the settop.
     */
    public OCRProvider getOCRProvider();

    /**
     * @return Recorder of the settop.
     */
    public Recorder getRecorder();

    /**
     * @return Logger.
     */
    public Logger getLogger();

    /**
     * Method for logging Info message
     * 
     * @param message
     *            -String
     */
    public void logInfo( String message );

    /**
     * Method for logging Error message
     * 
     * @param message
     *            -String
     */
    public void logError( String message );

    /**
     * Method for logging Warn message
     * 
     * @param message
     *            -String
     */
    public void logWarn( String message );

    /**
     * Method for logging Debug message
     * 
     * @param message
     *            -String
     */
    public void logDebug( String message );

    /**
     * Method will get the LogDirectory
     * 
     * @return String
     */
    public String getLogDirectory();

    /**
     * getting locked status
     * 
     * @return boolean
     */
    public boolean isLocked();

    /**
     * setting locked status
     * 
     * @param value
     *            - boolean
     */
    public void setLocked( boolean value );
}
