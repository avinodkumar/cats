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
package com.comcast.cats.vision.event;

/**
 * An enumeration which defines the complete list remote keys. This enumeration supports the definition of a formal command name
 * which can be used for UI components and may also support an abbreviated name.
 */
public enum ActionCommand
{

    ON("On"),
    OFF("Off"),
    REBOOT("Reboot");
    
    /**
     * The string which represents the displayed string.
     */
    private String display;

    /**
     * Constructor which defaults the display to null.
     */
    private ActionCommand()
    {
        this.display = null;
    }

    /**
     * constructor which explicitly sets the display.
     * @param display the string which represents the text on a UI.
     */
    private ActionCommand(final String display)
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
     */
    public static ActionCommand parse(final String x)
    {
        for (ActionCommand c : values())
        {
            if (x != null && (x.equals(c.name()) || x.equals(c.display)))
            {
                return c;
            }
        }

        throw new IllegalArgumentException(x + " is not a kind of ActionCommand");
    }
}
