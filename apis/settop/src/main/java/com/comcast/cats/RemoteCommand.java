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
 * An enumeration which defines the complete list remote keys. This enumeration
 * supports the definition of a formal command name which can be used for UI
 * components and may also support an abbreviated name.
 */
public enum RemoteCommand
{
    DASH("-"),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    A,
    ALT,
    ANGLE,
    ANYNET,
    APPLIST,
    AUDIO,
    B,
    BLUE,
    BYPASS,
    C,
    CAPS,
    CAPTION,
    CHDN,
    CHLIST,
    CHUP,
    D,
    D_LOCK("D/LOCK"),
    DAYDN,
    DAYUP,
    DELETE("DEL"),
    DNET,
    DOWN,
    EJECT,
    ENTER,
    EXIT,
    FAV,
    FF,
    FILE_MODE,
    FORMAT,
    FREEZE,
    GREEN,
    GUIDE,
    HDZOOM,
    HELP,
    HOME,
    INFO,
    INSTREPLAY,
    JUMPBACK,
    LANGUAGE,
    LAST,
    LEFT,
    LINK,
    LIVE,
    LOCK,
    MENU,
    MUTE,
    MYDVR,
    NEXT,
    OK,
    ONDEMAND,
    PAUSE,
    PGDN,
    PGUP,
    PIP,
    PIPCHDN,
    PIPCHUP,
    PIPMOVE,
    PIPONOFF,
    PIPSWAP,
    PLAY,
    POWER,
    POUND("#"),
    PPV,
    PREV,
    REC,
    RED,
    REPEAT,
    REPLAY,
    RETURN,
    REW,
    RIGHT,
    SAP,
    SDHC,
    SELECT,
    SETTINGS,
    SKIP,
    SKIPFWD,
    SLOW,
    SOURCE,
    STAR("*"),
    STOP,
    SUBTITLE,
    SUSPEND,
    SWAP,
    TEXT,
    THUMBDOWN,
    THUMBUP,
    TIMER,
    TIMESEEK,
    TITLE,
    TV_MODE,
    TV_VCR("TV/VCR"),
    TVMENU,
    TVVIDEO,
    TVRADIO,
    UP,
    VOLDN,
    VOLOPT,
    VOLUP,
    HDMI,
    COMPONENT,
    COMPOSITE,
    SETUP,
    YELLOW;
    
    /**
     * The string which represents the displayed string.
     */
    private String display;

    /**
     * Constructor which defaults the display to null.
     */
    private RemoteCommand()
    {
        this.display = null;
    }

    /**
     * constructor which explicitly sets the display.
     * @param display the string which represents the text on a UI.
     */
    private RemoteCommand(final String display)
    {
        this.display = display;
    }

    /**
     * Overridden to return the display name when present.
     * @return the display name when present.
     */
    @Override
    public String toString()
    {
        return (display == null) ? name() : display;
    }

    /**
     * Simple routine to convert either a display string or an enumeration name to
     * this enumeration.
     * @param x
     *          The display string or enumeration string to convert
     * @return The enumeration represented by the specified text
     * @throws IllegalArgumentException
     */
    public static RemoteCommand parse(final String x)
    {
        for (RemoteCommand c : values())
        {
            if (x != null && (x.equals(c.name()) || x.equals(c.display)))
            {
                return c;
            }
        }

        throw new IllegalArgumentException(x + " is not a kind of RemoteCommand");
    }
    
    /**
     * Method to convert an integer value to this enumeration
     * @param value 
     * @return RemoteCommand
     * @throws IllegalArgumentException
     */
    public static RemoteCommand parse(final int value)
    {        
        for (RemoteCommand c : values())
        {
           if(c.toString().equals( String.valueOf( value )))
           {
               return c;
           }
        }
        
        throw new IllegalArgumentException(value + " is not a kind of RemoteCommand");
    }
}
