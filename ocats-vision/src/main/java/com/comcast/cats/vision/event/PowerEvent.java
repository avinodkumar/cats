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

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventType;

public class PowerEvent extends CatsEvent
{
    private static final long serialVersionUID = 4010835911311383551L;
    private ActionType        actionType;
    private ActionCommand     actionCommand;
    private static int        eventCount       = 0;
    private int               count;

    public PowerEvent( ActionCommand actionCommand, String sourceId, Object source )
    {
        super( ++eventCount, sourceId, source, CatsEventType.POWER );
        this.actionCommand = actionCommand;
    }

    public PowerEvent( ActionCommand actionCommand, String sourceId, Object source, int count )
    {
        super( ++eventCount, sourceId, source, CatsEventType.POWER );
        this.actionCommand = actionCommand;
        this.count = count;
    }

    public ActionCommand getActionCommand()
    {
        return actionCommand;
    }

    public void setActionCommand( ActionCommand actionCommand )
    {
        this.actionCommand = actionCommand;
    }

    public ActionType getActionType()
    {
        return actionType;
    }

    public void setActionType( ActionType actionType )
    {
        this.actionType = actionType;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount( int count )
    {
        this.count = count;
    }
}
