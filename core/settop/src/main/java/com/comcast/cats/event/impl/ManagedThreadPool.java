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
package com.comcast.cats.event.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Idea behind the managed thread pool is that this threadpool will hold threads
 * that service one settop.
 * 
 * Tasks to be executed on a settop are provided to the threadpool. The
 * threadpool picks up a thread that services a settop. i.e the same thread
 * process requests to a settop. The threadpool then assigns that task to the
 * thread via the addToQueue method of the thread.
 * 
 * @author sajayjk
 */
@Named
public class ManagedThreadPool
{
    private Map< Object, ManagedWorker > taskMap          = new ConcurrentHashMap< Object, ManagedWorker >();
    private static final int             THREAD_POOL_SIZE = 20;
    private static ExecutorService       executor = Executors.newFixedThreadPool( THREAD_POOL_SIZE );

    @Inject
    public ManagedThreadPool( CatsEventDispatcherThreaded catsEventDispatcherThreaded )
    {
        ManagedWorker.setCatsEventDispatcher( catsEventDispatcherThreaded );
        ManagedWorker.setManagedThreadPool( this );
    }

    /**
     * Adds task to the worker.
     * 
     * @param managedTask
     *            Task to be executed.
     */
    public void addTask( final AbstractManagedTask managedTask )
    {
        if ( ( managedTask != null ) && ( managedTask.getIdentifier() != null ) )
        {
            ManagedWorker managedWorker = taskMap.get( managedTask.getIdentifier() );

            if ( managedWorker == null )
            {
                managedWorker = new ManagedWorker( managedTask.getIdentifier() );
                taskMap.put( managedTask.getIdentifier(), managedWorker );
                managedWorker.addToQueue( managedTask );
                executor.execute( managedWorker );
            }
            else
            {
                managedWorker.addToQueue( managedTask );
            }
        }
    }

    /**
     * Removes worker from the taskMap.
     * 
     * @param identifier
     *            identifier for the worker thread
     */
    public synchronized void freeWorker( Object identifier )
    {
        if ( identifier != null )
        {
            ManagedWorker managedWorker = taskMap.get( identifier );
            if ( managedWorker != null )
            {
                taskMap.remove( identifier );
            }
        }
    }
}