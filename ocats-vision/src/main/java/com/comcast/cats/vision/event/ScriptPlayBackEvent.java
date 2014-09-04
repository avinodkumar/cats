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

public class ScriptPlayBackEvent extends CatsEvent
{

    private static final long serialVersionUID = 1L;

    private String            script;

    private static int        eventCount       = 0;

    public ScriptPlayBackEvent( String script, String sourceId, Object Source )
    {
        super( eventCount++, sourceId, Source, CatsEventType.SCRIPT_PLAY_BACK );
        this.script = script;
    }

    public String getScript()
    {
        return script;
    }

    public void setScript( String script )
    {
        this.script = script;
    }
}
