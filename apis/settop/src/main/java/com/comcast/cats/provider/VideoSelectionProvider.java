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

import java.net.URI;

import com.comcast.cats.VideoSourceType;

/**
 * Interface to specify one of various video source types on an STB to be
 * selected for video streaming to the axis video servers.
 * 
 * @author thusai000
 */
public interface VideoSelectionProvider extends BaseProvider
{

    /**
     * Get IR or Video device information as a URI based on implementation.
     * 
     * @return The URI for the device sending the source selection commands.
     */
    public URI getVSSLocator();

    /**
     * Operation to perform video source selection.
     * 
     * @param vSource
     *            - video source to be selected such as HDMI, Component or
     *            Composite
     * @return - true/false depending on successful operation.
     */
    boolean selectVideoSource( VideoSourceType vSource );

}
