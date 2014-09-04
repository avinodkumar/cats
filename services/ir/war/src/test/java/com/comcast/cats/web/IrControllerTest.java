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
package com.comcast.cats.web;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.testng.annotations.Test;
import com.comcast.cats.service.KeyManager;

/**
 * The Class IrControllerTest.
 * 
 * @Author : cfrede001
 * @since :
 * @Description : The Class IrControllerTest is the unit test of
 *              {@link IrController}. This test should be ignored until a
 *              solution to having a mocked servlet layer or stand-in servlet
 *              layer can be obtained. For now, this is of limited value given
 *              the simplistically of the given page
 */
public class IrControllerTest
{

    @Test( enabled = false )
    public void doGet()
    {
        HttpServletRequest request = createMock( HttpServletRequest.class );
        HttpServletResponse response = createMock( HttpServletResponse.class );
        KeyManager keyManager = createMock( KeyManager.class );
        expect( request.getAttribute( "refresh" ) ).andReturn( "refresh" );
        expect( request.getRequestDispatcher( "/remotes.jsp" ) ).andReturn( createMock( RequestDispatcher.class ) );
        replay( request );
        // new IrController().doGet(request, response);
        verify( request );
    }
}
