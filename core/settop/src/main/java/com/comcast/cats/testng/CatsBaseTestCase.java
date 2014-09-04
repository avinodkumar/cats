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
package com.comcast.cats.testng;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.Settop;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;
import com.comcast.cats.testng.SettopProvider;

/**
 * TestNG base test case.
 * 
 * @author subin
 * 
 */
public abstract class CatsBaseTestCase
{
    private SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer;
    private Settop                        settop;
    private CatsFramework                 catsFramework;

    /**
	 * Constructor
	 */
    public CatsBaseTestCase()
    {
        // Initialize CatsFramework
        catsFramework = new CatsFramework();
        settopExclusiveAccessEnforcer = SettopProvider.settopExclusiveAccessEnforcer;
    }

    /**
	 * Get the settop details.
	 *
	 * @return Settop {@linkplain Settop}
	 */
    public Settop getSettop()
    {
        return this.settop;
    }

    /**
	 * Set the settop.
	 *
	 * @param settop {@linkplain Settop}
	 */
    public void setSettop( Settop settop )
    {
        this.settop = settop;
    }

    /**
	 * Get the SettopExclusiveAccessEnforcer.
	 *
	 * @return SettopExclusiveAccessEnforcer {@linkplain SettopExclusiveAccessEnforcer}
	 */
    public SettopExclusiveAccessEnforcer getSettopExclusiveAccessEnforcer()
    {
        return this.settopExclusiveAccessEnforcer;
    }

    /**
	 * Get the CatsFramework.
	 *
	 * @return CatsFramework {@linkplain CatsFramework}
	 */
    public CatsFramework getCatsFramework()
    {
        return this.catsFramework;
    }
}