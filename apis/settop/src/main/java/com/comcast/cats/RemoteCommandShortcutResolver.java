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

/**
 * Mapping class between single character representation to corresponding Remote
 * commands.
 * 
 * @author bemman01c
 * 
 */
public class RemoteCommandShortcutResolver
{

    public static RemoteCommand resolveRemoteCommand( char shortForm )
    {

        RemoteCommand returnValue = null;
        switch ( shortForm )
        {
        case 'U':
            returnValue = RemoteCommand.UP;
            break;
        case 'M':
            returnValue = RemoteCommand.MENU;
            break;
        case 'G':
            returnValue = RemoteCommand.GUIDE;
            break;
        case 'I':
            returnValue = RemoteCommand.INFO;
            break;
        case 'S':
            returnValue = RemoteCommand.SELECT;
            break;
        case 'R':
            returnValue = RemoteCommand.RIGHT;
            break;
        case 'L':
            returnValue = RemoteCommand.LEFT;
            break;
        case 'D':
            returnValue = RemoteCommand.DOWN;
            break;
        case 'E':
            returnValue = RemoteCommand.EXIT;
            break;
        case 'P':
            returnValue = RemoteCommand.POWER;
            break;
        default:
            returnValue = RemoteCommand.parse( Character.toString( shortForm ) );

        }
        return returnValue;
    }
}
