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
package com.comcast.cats.domain.util;

import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLHandshakeException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for {@link SSLUtil}
 * 
 * @author subinsugunan
 * 
 */
public class SSLUtilTest
{
    private static final String HTTPS_URL = "https://localhost/";

    /**
     * This may not run in CI server as that sever will already have SSL root
     * certificate. But good enough for testing in local.
     * 
     * @throws Exception
     */
    @Test( expected = SSLHandshakeException.class )
    @Ignore
    public void testDisableCertificateValidationException() throws Exception
    {
        URL url = new URL( HTTPS_URL );
        URLConnection myURLConnection = url.openConnection();
        myURLConnection.connect();
    }

    @Test
    public void testDisableCertificateValidation() throws Exception
    {
        // This should called after all negative test cases.
        SSLUtil.disableCertificateValidation();
        URL url = new URL( HTTPS_URL );
        URLConnection myURLConnection = url.openConnection();
        myURLConnection.connect();
    }

    @Test
    public void getInstance()
    {
       // AssertUtil.isNull( SSLUtil.getInstance() );// FIXME this throws compilation failure
    }

}
