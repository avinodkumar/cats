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

import java.io.Serializable;

/**
 * 
 * The Audio Monitoring module will utilize the audio alarming capabilities of
 * the Chromatic AM-xx to implement detection of "normal" audio levels on a
 * settop box. The alarming is based on three parameters that can be set on a
 * per-port basis:
 * 
 * <p>
 * <b>Sampling Interval</b> - The length of time that the audio will be sampled
 * for threshold detection.
 * </p>
 * 
 * <p>
 * <b>Over Threshold</b> - If the audio level rises above this threshold on the
 * audio port during the sample interval, an alarm indication will be set for
 * this port in the next alarm packet sent by the AM-xx.
 * </p>
 * 
 * <p>
 * <b>Loss Threshold</b> - If the audio level falls below this threshold on the
 * audio port during the sample interval, an alarm indication will be set for
 * this port in the next alarm packet sent by the AM-xx.
 * </p>
 * 
 * 
 */
public class NormalAudioParameters implements Serializable
{

    /**
     * Serialization support
     */
    private static final long serialVersionUID = -3495644189098901038L;

    private int               overThreshold;
    private int               lossThreshold;
    private int               samplingIntervalSecs;

    /**
     * Constructor
     * 
     * @param overThreshold
     * @param lossThreshold
     * @param samplingIntervalSecs
     */
    public NormalAudioParameters( int overThreshold, int lossThreshold, int samplingIntervalSecs )
    {
        this.overThreshold = overThreshold;
        this.lossThreshold = lossThreshold;
        this.samplingIntervalSecs = samplingIntervalSecs;
    }

    /**
     * Constructor
     */
    public NormalAudioParameters()
    {
    }

    /**
     * To get the Over threshold. If the audio level rises above this threshold
     * on the audio port during the sample interval, an alarm indication will be
     * set for this port in the next alarm packet sent by the AM-xx.
     * 
     * @return Over threshold level
     */
    public int getOverThreshold()
    {
        return overThreshold;
    }

    /**
     * To set the Over Threshold
     * 
     * @param overThreshold
     */
    public void setOverThreshold( int overThreshold )
    {
        this.overThreshold = overThreshold;
    }

    /**
     * To get the Loss Threshold. If the audio level falls below this threshold
     * on the audio port during the sample interval, an alarm indication will be
     * set for this port in the next alarm packet sent by the AM-xx.
     * 
     * @return int
     */
    public int getLossThreshold()
    {
        return lossThreshold;
    }

    /**
     * To set the Loss Threshold
     * 
     * @param lossThreshold
     */
    public void setLossThreshold( int lossThreshold )
    {
        this.lossThreshold = lossThreshold;
    }

    /**
     * To get the Sampling Interval. The length of time that the audio will be
     * sampled for threshold detection.
     * 
     * @return int
     */
    public int getSamplingIntervalSecs()
    {
        return samplingIntervalSecs;
    }

    /**
     * To set the sampling Interval
     * 
     * @param samplingIntervalSecs
     */
    public void setSamplingIntervalSecs( int samplingIntervalSecs )
    {
        this.samplingIntervalSecs = samplingIntervalSecs;
    }

    @Override
    public String toString()
    {
        return "Normal Audio Parameters [Loss Threshold=" + lossThreshold + ", Over Threshold=" + overThreshold
                + ", Sampling Interval In Sec=" + samplingIntervalSecs + "]";
    }
}
