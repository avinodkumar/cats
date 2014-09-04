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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.comcast.cats.recorder.VideoRecorderTask;

/**
 * Represents state of a {@link VideoRecorderTask}
 * 
 * @author ssugun00c
 * 
 */
@XmlType
@XmlEnum( String.class )
public enum VideoRecorderState
{
    UNKNOWN, INITIALIZING, BUFFERING, RECORDING, STOPPED, ERROR, FORCE_CLOSE;
}
