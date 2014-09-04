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

import java.net.URI;

/**
 * Provides access to settop output trace.
 * 
 * @author jtyrre001
 */
public interface TraceProvider extends BaseProvider
{
    /**
     * Start trace method. This will add an entry to the accessHistory table. It
     * will invoke the Trace servlet's start method. If the servlet invocation
     * is success, trace load table is also updated.
     * 
     * @throws Exception
     */

    void startTrace() throws Exception;

    /**
     * Start trace method. This will add an entry to the accessHistory table. It
     * will invoke the Trace servlet's start method. If the servlet invocation
     * is success, trace load table is also updated.
     * 
     * @param timeout
     *            in milliseconds
     * @throws Exception
     */
    void startTrace( Integer timeout ) throws Exception;

    /**
     * Stop Trace method. Invoking the Trace Servlet's stop trace.
     * 
     * @throws Exception
     */

    void stopTrace() throws Exception;

    /**
     * This method will return the latest data in the trace file. The number of
     * lines that this method will read is configurable in the trace.properties.
     * It is assumed that one line contains about 80 bytes. So last 10 lines
     * means it will read 800 characters.
     * 
     * @return trace data as String
     * @throws Exception
     */

    String getTraceData() throws Exception;

    /**
     * This method will return the path to the trace file.
     * 
     * @return URI of the trace file path
     * @throws Exception
     */

    URI getTraceFile() throws Exception;

    /**
     * This method will return the status of trace operation.
     * 
     * @return status from the trace_load table as String
     * @throws Exception
     */

    String getTraceStatus() throws Exception;

    /**
     * Gets trace text from an offset into the trace text. The offset is in
     * character spaces.
     */
    public void getTraceOffset();

    /**
     * Trace blocks while waiting/searching for an expression.
     * 
     * @param regExpression
     *            Regular expression to wait for.
     */
    public void waitForTraceExpression( String regExpression );

    /**
     * Trace blocks while waiting/searching for a string.
     * 
     * @param stringText
     *            Text string to wait for.
     */
    public void waitForTraceString( String stringText );

    /**
     * Send text to the trace stream.
     * 
     * @param stringText
     *            String text to send to trace.
     */
    public void sendTraceString( String stringText );

    /**
     * Send text to the trace stream.
     * 
     * @param stringText
     *            String to text to send to trace.
     * @param isHexString
     *            true if data should be of type hex, otherwise false, text
     *            data.
     */
    public void sendTraceString( String stringText, boolean isHexString );

    /**
     * Send a byte to the trace stream.
     * 
     * @param traceByte
     *            bytes to send to trace
     */
    public void sendTraceByte( byte traceByte );

    /**
     * Set a custom folder for log file.<br />
     * <br />
     * The custom trace log file will be created at
     * <code>{traceFileLocation}/{mac-id}/trace.log</code> <br />
     * <br />
     * Please note ":" from macId will be replaced with "-". <br />
     * <br />
     * E.g. D:\log\00-00-41-12-DD-04\trace.log <br />
     * 
     * @param traceFileLocation
     * @param append
     *            - append-true/overwrite-false
     */
    public void setTraceLogLocation( String traceFileLocation, boolean append );
}
