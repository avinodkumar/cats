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
package com.comcast.cats.provider;

import java.util.List;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.AllocationException;

/**
 * Interface responsible for locking and unlocking settops.
 * 
 * @author cfrede001
 * 
 */
public interface SettopExclusiveAccessEnforcer
{
    /**
     * Locks this settop for use.
     * 
     * @param settop {@linkplain Settop}
     * @throws AllocationException
     */
    public void lock( Settop settop ) throws AllocationException;
    
    /**
     * Reacquire this settop for use.
     * 
     * @param settop {@linkplain Settop}
     * @throws AllocationException
     */
    public void reacquire( Settop settop ) throws AllocationException;

    /**
     * Release the lock for this settop.
     * 
     * @param settop {@linkplain Settop}
     * @throws AllocationException
     */
    public void release( Settop settop ) throws AllocationException;

    /**
     * Fast check to see if settop is locked. No service call will be performed
     * to verify the lock.
     * 
     * @param settop {@linkplain Settop}
     * @return boolean
     */
    public boolean isLocked( Settop settop );

    /**
     * Verify that this settop is still locked. This results in a service call
     * for allocation.
     * 
     * @param settop {@link Settop}
     * @return boolean
     * @throws AllocationException
     */
    public boolean verify( Settop settop ) throws AllocationException;

    /**
     * Attempt to lock all settops in the list. Silently ignore any locking
     * errors for some settops.
     * 
     * @param settopList
     *            - List of {@linkplain Settop} to be locked.
     * @return List of {@linkplain Settop}
     * @throws AllocationException
     *             - If no settops could be locked an AllocationException will
     *             result.
     */
    public List< Settop > lock( List< Settop > settopList ) throws AllocationException;

    /**
     * Attempt to lock all settops in the list. Silently ignore any locking
     * errors for some settops.
     * 
     * @param settopList
     *            - List of {@linkplain Settop} to be locked.
     * @param failOnLockError
     *            - true - if any lock fails an allocation exception will occur.
     *            if false, then it will perform as lock from above.
     * @return List of {@linkplain Settop}
     * @throws AllocationException
     *             - If no settops could be locked an AllocationException will
     *             result.
     */
    public List< Settop > lock( List< Settop > settopList, boolean failOnLockError ) throws AllocationException;

    /**
     * Release all the settops in the list and handle exceptions silently.
     * 
     * @param settopList
     *            - list of {@linkplain Settop} to be unlocked.
     * @throws AllocationException
     */
    public void release( List< Settop > settopList ) throws AllocationException;
    
    /**
     * Convenience method to release all the currently allocated settops.
     * @throws AllocationException
     */
    public void release() throws AllocationException;

    /**
     * Convenience method to get all the currently allocated settops.
     * @return list of currently allocated {@linkplain Settop}.
     */
    public List<Settop> getActiveSettops();
    
    /**
     * Get the exception messages while releasing the settops in the list
     * 
     * @return exception message
     */
    public String getReleaseErrorMessage();

    /**
     * Get the exception messages while locking the settops in the list
     * 
     * @return exception message
     */
    public String getLockErrorMessage();
}
