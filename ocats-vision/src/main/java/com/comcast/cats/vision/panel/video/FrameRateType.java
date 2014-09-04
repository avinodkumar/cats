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
package com.comcast.cats.vision.panel.video;

/**
 * FrameRateType is an enum for holding the frame rates supported by the CATS
 * Vision
 * 
 * @author aswathyann
 * 
 */
public enum FrameRateType
{
    DEFAULT_FPS( 30, "Default(30 FPS)" ),
    FPS_1( 1, "1 FPS" ),
    FPS_2( 2, "2 FPS" ),
    FPS_3( 3, "3 FPS" ),
    FPS_4( 4, "4 FPS" ),
    FPS_5( 5, "5 FPS" ),
    FPS_6( 6, "6 FPS" ),
    FPS_7( 7, "7 FPS" ),
    FPS_8( 8, "8 FPS" ),
    FPS_9( 9, "9 FPS" ),
    FPS_10( 10, "10 FPS" ),
    FPS_11( 11, "11 FPS" ),
    FPS_12( 12, "12 FPS" ),
    FPS_13( 13, "13 FPS" ),
    FPS_14( 14, "14 FPS" ),
    FPS_15( 15, "15 FPS" ),
    FPS_16( 16, "16 FPS" ),
    FPS_17( 17, "17 FPS" ),
    FPS_18( 18, "18 FPS" ),
    FPS_19( 19, "19 FPS" ),
    FPS_20( 20, "20 FPS" ),
    FPS_21( 21, "21 FPS" ),
    FPS_22( 22, "22 FPS" ),
    FPS_23( 23, "23 FPS" ),
    FPS_24( 24, "24 FPS" ),
    FPS_25( 25, "25 FPS" ),
    FPS_26( 26, "26 FPS" ),
    FPS_27( 27, "27 FPS" ),
    FPS_28( 28, "28 FPS" ),
    FPS_29( 29, "29 FPS" ),
    FPS_30( 30, "30 FPS" );

    private int    frameRate;
    private String frameRateStr;

    /**
     * Constructor for FrameRateType
     * 
     * @param frameRate
     *            frame rate
     * @param frameRateStr
     *            frame rate in String
     */
    FrameRateType( int frameRate, String frameRateStr )
    {
        this.frameRate = frameRate;
        this.frameRateStr = frameRateStr;
    }

    /**
     * Get string representation of frame rate
     * 
     * @param frameRateType
     *            a FrameRateType
     * @return string representation of frame rate
     */
    public static String getName( FrameRateType frameRateType )
    {
        return frameRateType.frameRateStr;
    }

    public static int getFrameRate( FrameRateType frameRateType )
    {
        return frameRateType.frameRate;
    }

    /**
     * Return frame rate for the given action command
     * 
     * @param actionCommand
     *            the command string associated with this action event
     * @return frame rate
     */
    public static int getFrameRate( String actionCommand )
    {
        int fps = getFrameRate( DEFAULT_FPS );
        FrameRateType[] frameRates = FrameRateType.values();

        for ( FrameRateType frameRate : frameRates )
        {
            if ( actionCommand.equals( FrameRateType.getName( frameRate ) ) )
            {
                fps = FrameRateType.getFrameRate( frameRate );
                break;
            }
        }
        return fps;
    }
}
