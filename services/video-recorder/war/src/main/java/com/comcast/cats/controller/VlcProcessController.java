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
package com.comcast.cats.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.DefaultVideoRecorderTask;
import com.comcast.cats.recorder.VideoRecorderTask;
import com.comcast.cats.service.util.HttpClientUtil;
import com.comcast.cats.service.util.VideoRecorderUtil;
import com.comcast.cats.view.ProcessBean;
import com.comcast.cats.view.VlcProcessBean;

/**
 * Controller for VLC process monitor.
 * 
 * @author ssugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class VlcProcessController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( VlcProcessController.class );

    public VlcProcessController()
    {
    }

    @SuppressWarnings( "unchecked" )
    public List< VlcProcessBean > getVlcProcesses()
    {
        List< VlcProcessBean > vlcBeans = new LinkedList< VlcProcessBean >();

        Map< Integer, VideoRecorderTask > vlcTasks = ( Map< Integer, VideoRecorderTask > ) HttpClientUtil.getForObject(
                getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_CURRENT_TASKS ), null );

        DefaultVideoRecorderTask defaultVideoRecorderTask = null;

        for ( Map.Entry< Integer, VideoRecorderTask > entry : vlcTasks.entrySet() )
        {
            defaultVideoRecorderTask = ( DefaultVideoRecorderTask ) entry.getValue();

            vlcBeans.add( new VlcProcessBean(
                    entry.getKey(),
                    System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST ),
                    defaultVideoRecorderTask.getTelnetPort(),
                    getFileName( defaultVideoRecorderTask.getFilePath() ),
                    ( int ) ( ( ( VideoRecorderUtil.getFileSize( defaultVideoRecorderTask.getFilePath() ) ) / 1024 ) / 1024 ),
                    defaultVideoRecorderTask.getFilePath(), getHttpDirUrl( defaultVideoRecorderTask.getFilePath() ),
                    VideoRecorderUtil.isPlayable( defaultVideoRecorderTask.getFilePath() ), VideoRecorderUtil
                            .isExists( defaultVideoRecorderTask.getFilePath() ), defaultVideoRecorderTask.getCommand() ) );
        }

        return vlcBeans;
    }

    private String getHttpDirUrl( String filePath )
    {
        String httpPath = VideoRecorderUtil.getHttpPath( filePath );

        String host = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getServerName();

        httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_IP, host );
        httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_NAME, host );

        String fileName = getFileName( filePath );

        return httpPath.substring( 0, httpPath.indexOf( fileName ) );
    }

    private String getFileName( String filePath )
    {
        return filePath.substring( filePath.lastIndexOf( System.getProperty( "file.separator" ) ) + 1,
                filePath.length() );
    }

    public void stop( int recordingId )
    {
        LOGGER.info( "Inside stop [" + recordingId + "]" );
    }

    public void killProcess( String pid )
    {
        LOGGER.info( "Inside killProcess [" + pid + "]" );

        /*
         * String os = System.getProperty( "os.name" ).toLowerCase();
         * 
         * if ( os.indexOf( "win" ) >= 0 ) { String command = "TASKKILL /PID " +
         * pid; VideoRecorderUtil.execcuteCommand( command ); } else { String
         * command = "kill -9 " + pid; VideoRecorderUtil.execcuteCommand(
         * command ); }
         */
    }

    public List< String > getProcessList()
    {
        String processes = null;

        List< String > processList = new LinkedList< String >();

        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "win" ) >= 0 )
        {
            processes = VideoRecorderUtil.listVlcProcess();
        }
        else
        {
            String command = "top -p `pgrep -d ',' \"java|jboss|vlc\"`";
            //FIXME
            processes = command;//VideoRecorderUtil.execcuteCommand( command );
        }

        if ( ( null != processes ) && ( !processes.isEmpty() ) )
        {
            StringTokenizer stringTokenizer = new StringTokenizer( processes, "\n" );

            while ( stringTokenizer.hasMoreElements() )
            {
                processList.add( ( String ) stringTokenizer.nextElement() );
            }
        }

        return processList;

    }

    public List< ProcessBean > getProcessBeanList()
    {
        List< ProcessBean > processBeanList = null;

        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "win" ) >= 0 )
        {
            processBeanList = getWindowsProcessBeanList();
        }
        else
        {
            processBeanList = getLinuxProcessBeanList();
        }

        return processBeanList;
    }

    private List< ProcessBean > getLinuxProcessBeanList()
    {
        String processes = VideoRecorderUtil.listVlcProcess();

        List< ProcessBean > processBeanList = new LinkedList< ProcessBean >();

        if ( ( null != processes ) && ( !processes.isEmpty() ) )
        {
            StringTokenizer stringTokenizer = new StringTokenizer( processes, "\n" );

            while ( stringTokenizer.hasMoreElements() )
            {
                processBeanList.add( new ProcessBean( ( String ) stringTokenizer.nextElement() ) );
            }
        }

        return processBeanList;
    }

    private List< ProcessBean > getWindowsProcessBeanList()
    {
        String processes = VideoRecorderUtil.listVlcProcess();

        List< ProcessBean > processBeanList = new LinkedList< ProcessBean >();

        if ( ( null != processes ) && ( !processes.isEmpty() ) )
        {
            StringTokenizer stringTokenizer = new StringTokenizer( processes, "\n" );

            List< String > processOutPutList = new LinkedList< String >();

            while ( stringTokenizer.hasMoreElements() )
            {
                processOutPutList.add( ( String ) stringTokenizer.nextElement() );
            }

            if ( processOutPutList.size() > 2 )
            {
                processOutPutList.remove( 0 );
                processOutPutList.remove( 0 );

                for ( String line : processOutPutList )
                {
                    stringTokenizer = new StringTokenizer( line, " " );

                    if ( stringTokenizer.hasMoreElements() )
                    {
                        ProcessBean processBean = new ProcessBean( ( String ) stringTokenizer.nextElement(),
                                ( String ) stringTokenizer.nextElement(), ( String ) stringTokenizer.nextElement(),
                                ( String ) stringTokenizer.nextElement(), ( String ) stringTokenizer.nextElement(),
                                ( String ) stringTokenizer.nextElement() );
                        processBeanList.add( processBean );
                    }
                }
            }
        }

        return processBeanList;
    }

    @SuppressWarnings( "unchecked" )
    public List< Integer > getActiveTelnetPortList()
    {
        String host = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getServerName();
        int port = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getLocalPort();

        String REST_BASE_URL = "http://" + host + ":" + port + "/video-recorder-service/rest"
                + VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH;

        String requestUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_ACTIVE_TELNET_PORTS;

        LOGGER.trace( "[REQUEST-URI-ACTIVE][" + requestUri + "]" );

        Map< String, String > emptyMap = Collections.emptyMap();

        List< Integer > ports = Collections.emptyList();

        try
        {
            ports = ( List< Integer > ) HttpClientUtil.getForObject( requestUri, emptyMap );
        }
        catch ( ClassCastException e )
        {
            LOGGER.warn( e.getMessage() );
        }

        return ports;
    }

    @SuppressWarnings( "unchecked" )
    public List< Integer > getRecentlyUsedTelnetPortList()
    {
        String host = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getServerName();
        int port = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getLocalPort();

        String REST_BASE_URL = "http://" + host + ":" + port + "/video-recorder-service/rest"
                + VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH;

        String requestUri = REST_BASE_URL + VideoRecorderServiceConstants.REST_REQUEST_RECENTLY_USED_TELNET_PORTS;

        LOGGER.trace( "[REQUEST-URI-RECENTLY-USED][" + requestUri + "]" );

        Map< String, String > emptyMap = Collections.emptyMap();

        List< Integer > ports = Collections.emptyList();

        try
        {
            ports = ( List< Integer > ) HttpClientUtil.getForObject( requestUri, emptyMap );
        }
        catch ( ClassCastException e )
        {
            LOGGER.warn( e.getMessage() );
        }

        return ports;
    }

    private String getRequestUri( String restRequest )
    {
        String requestUri = System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_REST_API_BASE_URL )
                + VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH + restRequest;
        return requestUri;
    }
}
