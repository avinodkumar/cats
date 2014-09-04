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
package com.comcast.cats.service.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.recorder.exception.VideoRecorderConnectionException;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * A custom JAX-RS exception handler.
 * 
 * @author ssugun00c
 * 
 */
@Provider
public class VideoRecorderConnectionExceptionHandler implements ExceptionMapper< VideoRecorderConnectionException >
{
    @Override
    public Response toResponse( VideoRecorderConnectionException exception )
    {
        VideoRecorderResponse videoRecorderResponse = new VideoRecorderResponse( WebServiceReturnEnum.FAILURE );
        videoRecorderResponse.setMessage( "Video recorder custom JAX-RS exception handler for [" + exception.getClass()
                + "]. " + exception.getMessage() );
        videoRecorderResponse.setException( exception.getClass().getName() );
        return Response.serverError().entity( videoRecorderResponse ).build();
    }
}