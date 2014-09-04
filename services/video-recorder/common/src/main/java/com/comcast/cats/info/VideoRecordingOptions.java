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
package com.comcast.cats.info;

import java.util.HashMap;
import java.util.Map;

/**
 * Recording options for Video Server and Video Player.
 * 
 * <pre>
 * <b>Video</b>
 * 
 * Codec : H-264
 * BitRate : 41Kbps
 * FrameRate : 5 fps
 * scale : 1 (bringing this down to .75 reduces the size much but the video resolution will be low)
 * 
 * <b>Audio</b>
 * 
 * Codec : AAC
 * BitRate : 92Kbps
 * Sample Rate : 11025 ( 44100 is the setting recommended by the VLCJ group, for mp3)
 * </pre>
 * 
 * @author ssugun00c
 * 
 */
public class VideoRecordingOptions extends HashMap< String, String >
{
    private static final long   serialVersionUID       = 1L;

    private static final String MP4_FILE_EXTENSION     = ".mp4";

    public static final String  DEFAULT_VIDEO_CODEC    = "h264";
    public static final String  DEFAULT_VB             = "41";

    public static final String  DEFAULT_AUDIO_CODEC    = "aac";
    public static final String  DEFAULT_AB             = "92";

    // FIXME:Is it pretty low?
    public static final String  DEFAULT_FRAME_RATE     = "5";
    public static final String  DEFAULT_SAMPLE_RATE    = "11025";
    public static final String  DEFAULT_SCALE          = "1";
    public static final String  DEFAULT_NO_OF_CHANNELS = "1";

    public static final String  DEFAULT_FILE_PREFIX    = "";
    public static final String  DEFAULT_FILE_EXTENSION = MP4_FILE_EXTENSION;

    // FIXME: Add more javadoc
    /**
     * Video Codec
     */
    private String              vCodec                 = DEFAULT_VIDEO_CODEC;

    /**
     * Video BitRate
     */
    private String              vb                     = DEFAULT_VB;

    /**
     * Audio Codec
     */
    private String              aCodec                 = DEFAULT_AUDIO_CODEC;

    /**
     * Audio BitRate
     */
    private String              ab                     = DEFAULT_AB;

    /**
     * FrameRate
     */
    private String              fps                    = DEFAULT_FRAME_RATE;

    /**
     * Scale
     */
    private String              scale                  = DEFAULT_SCALE;

    /**
     * 
     */
    private String              channels               = DEFAULT_NO_OF_CHANNELS;

    /**
     * Sample Rate
     */
    private String              sampleRate             = DEFAULT_SAMPLE_RATE;

    public VideoRecordingOptions()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * Options for Video Server.
     * 
     * E.g. Axis url options.
     * 
     * @return
     */
    public String getVideoServerParams()
    {
        return null;
    }

    /**
     * Any other generic options.
     * 
     * 
     * @return
     */
    public Map< String, String > getExtraOptions()
    {
        return this;
    }

    /**
     * Any other generic option that may set from outside.
     * 
     * 
     * @return
     */
    public void setExtraOptions( Map< String, String > extraOptions )
    {
        this.putAll( extraOptions );
    }

    /**
     * Configuration parameters for video recording.
     * 
     * E.g. VLC command line options.
     * 
     * @return
     */
    public String getVideoRecorderParams()
    {
        String params = ":sout=#transcode{vcodec=" + getvCodec() + ",vb=" + getVb() + ",fps=" + getFps() + ",scale="
                + getScale() + ",acodec=" + getaCodec() + ",ab=" + getAb() + ",samplerate=" + getSampleRate() + "}";

        return params;
    }

    public String getvCodec()
    {
        return vCodec;
    }

    public void setvCodec( String vCodec )
    {
        this.vCodec = vCodec;
    }

    public String getVb()
    {
        return vb;
    }

    public void setVb( String vb )
    {
        this.vb = vb;
    }

    public String getFps()
    {
        return fps;
    }

    public void setFps( String fps )
    {
        this.fps = fps;
    }

    public String getScale()
    {
        return scale;
    }

    public void setScale( String scale )
    {
        this.scale = scale;
    }

    public String getaCodec()
    {
        return aCodec;
    }

    public void setaCodec( String aCodec )
    {
        this.aCodec = aCodec;
    }

    public String getAb()
    {
        return ab;
    }

    public void setAb( String ab )
    {
        this.ab = ab;
    }

    public String getChannels()
    {
        return channels;
    }

    public void setChannels( String channels )
    {
        this.channels = channels;
    }

    public String getSampleRate()
    {
        return sampleRate;
    }

    public void setSampleRate( String sampleRate )
    {
        this.sampleRate = sampleRate;
    }

    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [ videoRecorderOptions=" + getVideoRecorderParams()
                + ", videoServerOptions=" + getVideoServerParams() + ";";
    }
}
