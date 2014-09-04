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
package com.comcast.cats.service;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.HashMap;
import java.util.Map;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.comcast.cats.service.impl.IRCommunicator;
import com.comcast.cats.service.impl.IRManagerImpl;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * The Class IRManagerMockTest.
 * 
 * @Author : 
 * @since : 
 * @Description : The Class IRManagerMockTest is the unit test
 *              of {@link IRManagerMock}.
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest(
    { IRManagerImpl.class } )
public class IRManagerMockTest
{

    @Test
    public void testGetIRCommunicators()
    {
        Map< String, IRCommunicator > irGroupExpected = new HashMap< String, IRCommunicator >();
        IRManager irManager = createMock( IRManagerImpl.class );
        expect( irManager.getIRCommunicators() ).andReturn( irGroupExpected );
        replayAll( irManager );
        AssertJUnit.assertEquals( irGroupExpected, irManager.getIRCommunicators() );
        verifyAll();
    }
}
