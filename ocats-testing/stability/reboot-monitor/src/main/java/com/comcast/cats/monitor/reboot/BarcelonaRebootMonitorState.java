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
package com.comcast.cats.monitor.reboot;

import java.util.Date;

public class BarcelonaRebootMonitorState
{
    private long uptime = 0L;
    private Date timestamp = new Date();

    public long getUptime()
    {
        return uptime;
    }
    public void setUptime( long uptime )
    {
        this.uptime = uptime;
    }
    public Date getTimestamp()
    {
        return timestamp;
    }
    public void setTimestamp( Date timestamp )
    {
        this.timestamp = timestamp;
    }
}
