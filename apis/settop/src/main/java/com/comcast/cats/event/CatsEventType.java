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
package com.comcast.cats.event;

/**
 * An enumeration which defines the complete list of CatsEventType.
 */
public enum CatsEventType
{
    UNKOWN, 
    ALL, 
    ALLOCATION, 
    VIDEO, 
    TRACE, 
    VIDEO_CTRL, 
    REMOTE, 
    REMOTE_RESPONSE, 
    REMOTE_FAIL_RESPONSE, 
    POWER,
    POWER_RESPONSE, 
    POWER_FAIL_RESPONSE, 
    CATS_CONFIG_BUTTON_EVENT, 
    CATS_CONFIG_BUTTON_EVENT_RESPONSE,
    CATS_CONFIG_BUTTON_EVENT_FAIL_RESPONSE, 
    SCREEN_CAPTURE, 
    SCRIPT_PLAY_BACK, 
    SCRIPT_PLAY_BACK_RESPONSE,
    TRACE_PREFERENCE_CHANGED, 
    REMOTE_PREFERENCE_CHANGED, 
    REACQUIRE_EVENT, 
    IMAGE_COMPARE;
}
