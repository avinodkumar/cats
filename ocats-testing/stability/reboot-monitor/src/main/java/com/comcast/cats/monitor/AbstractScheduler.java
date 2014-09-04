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
package com.comcast.cats.monitor;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public abstract class AbstractScheduler
{
    protected static Scheduler scheduler;

    public AbstractScheduler() throws SchedulerException
    {
        scheduler = new StdSchedulerFactory().getScheduler();
    }

    public AbstractScheduler( Scheduler scheduler )
    {
        this.scheduler = scheduler;
    }

    public void start() throws SchedulerException
    {
        scheduler.start();
    }

    public void shutdown() throws SchedulerException
    {
        scheduler.shutdown();
    }
}
