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

public enum ActionType
{

    PRESS,
    PRESS_AND_HOLD,
    TUNE;
    
    private String actionTypeStr;


    private ActionType()
    {
        this.actionTypeStr = null;
    }

    private ActionType(final String actionTypeStr)
    {
        this.actionTypeStr = actionTypeStr;
    }

    /**
     * Overridden to return the display name when present.
     * @return the display name when present.
     */
    @Override
    public String toString()
    {
        return (actionTypeStr == null) ? name() : actionTypeStr;
    }

    public static ActionType getActionType(String actionType) {
        ActionType[] sizes = ActionType.values();
        for(int i=0;i<sizes.length;i++) {
            String actionTypeStr = sizes[i].toString();
            if(actionTypeStr.equals(actionType)) {
                return sizes[i];
            }
        }
        return null;
    }
}
