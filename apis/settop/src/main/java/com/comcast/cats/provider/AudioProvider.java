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
package com.comcast.cats.provider;

import com.comcast.cats.info.NormalAudioParameters;

/**
 * Provide access to the Chromatec audio device
 * 
 * @author jtyrre001
 */
public interface AudioProvider extends BaseProvider
{
    /**
     * Gets the current audio parameters set for a particular audio device.
     * 
     * @return {@link NormalAudioParameters} object (loss, over and sampling
     *         intervals).
     */
    public NormalAudioParameters getDefaultNormalAudioParameters();

    /**
     * Determines if audio is present on a channel/port for a particular audio
     * device. Audio device is specified by the IP.
     * 
     * @return true if audio is present. false, otherwise.
     */
    public boolean isNormalAudioPresent();

    /**
     * Determines if audio is present on a channel/port for a particular audio
     * device. Audio device is specified by the IP.
     * 
     * @param normalAudioParameters
     *            - Specifies loss threshold, over threshold and sampling
     *            interval.
     * @return true if audio is present. false, otherwise.
     */
    public boolean isNormalAudioPresent( NormalAudioParameters normalAudioParameters );

    /**
     * Setter method to set the parent object(Settop)
     * 
     * @param parent
     *            object
     */
    public void setParent( Object parent );
}
