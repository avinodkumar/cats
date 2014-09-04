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
package com.comcast.cats.provider.impl;

import java.net.URI;

import com.comcast.cats.provider.TraceProvider;

public class TraceProviderImpl implements TraceProvider
{

    private static final long serialVersionUID = 1L;

    @Override
    public Object getParent()
    {
        return null;
    }

    @Override
    public void startTrace() throws Exception
    {
        
    }

    @Override
    public void startTrace( Integer timeout ) throws Exception
    {
        
    }

    @Override
    public void stopTrace() throws Exception
    {
        
    }

    @Override
    public String getTraceData() throws Exception
    {
        return "Trace not supported";
    }

    @Override
    public URI getTraceFile() throws Exception
    {
        return null;
    }

    @Override
    public String getTraceStatus() throws Exception
    {
        return "Trace not supported";
    }

    @Override
    public void getTraceOffset()
    {
    }

    @Override
    public void waitForTraceExpression( String regExpression )
    {
    }

    @Override
    public void waitForTraceString( String stringText )
    {
    }

    @Override
    public void sendTraceString( String stringText )
    {
    }

    @Override
    public void sendTraceString( String stringText, boolean isHexString )
    {
    }

    @Override
    public void sendTraceByte( byte traceByte )
    {
    }

    @Override
    public void setTraceLogLocation( String traceFileLocation, boolean append )
    {
    }

}
