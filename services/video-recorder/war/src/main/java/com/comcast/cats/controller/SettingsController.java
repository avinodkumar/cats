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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.DiskSpaceUsage;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.service.util.VideoRecorderUtil;
import com.comcast.cats.view.SettingsBean;

/**
 * Controller bean for settings.
 * 
 * @author SSugun00c
 * 
 */
@ManagedBean
@ViewScoped
public class SettingsController implements Converter
{
    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    public SettingsController()
    {
    }

    @Override
    public Object getAsObject( FacesContext arg0, UIComponent arg1, String arg2 )
    {
        return null;
    }

    @Override
    public String getAsString( FacesContext arg0, UIComponent arg1, Object arg2 )
    {
        return null;
    }

    public List< SettingsBean > getSettings()
    {
        LOGGER.trace( "[WEB][SETTINGS]" );

        List< SettingsBean > settings = new ArrayList< SettingsBean >();

        settings.add( new SettingsBean( "VLC Telnet Host ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_HOST ) ) );

        settings.add( new SettingsBean( "VLC Executable Path ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_EXECUTABLE_PATH + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_EXECUTABLE_PATH ) ) );

        settings.add( new SettingsBean( "VLC Telnet Password ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_VLC_TELNET_PASSWORD ) ) );

        settings.add( new SettingsBean( "VLC Telnet Port Range Start ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_START + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_START ) ) );

        settings.add( new SettingsBean( "VLC Telnet Port Range End ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_END + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_VLC_TELNET_PORT_RANGE_END ) ) );

        settings.add( new SettingsBean( "PVR File System Base Directory ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH ) ) );

        settings.add( new SettingsBean( "PVR HTTP Base Url ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH ) ) );

        settings.add( new SettingsBean( "PVR REST API Base Url ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_REST_API_BASE_URL + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_REST_API_BASE_URL ) ) );

        settings.add( new SettingsBean( "PVR Database User ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_USER_ID + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_USER_ID ) ) );

        settings.add( new SettingsBean( "PVR Database Connection Url ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_CONNECTION_URL + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_CONNECTION_URL ) ) );

        settings.add( new SettingsBean( "PVR Database Driver Class Name ["
                + VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_DRIVER_CLASS_NAME + "]", System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_DB_DRIVER_CLASS_NAME ) ) );

        return settings;
    }

    public List< DiskSpaceUsage > getDiskSpaceUsage()
    {
        LOGGER.trace( "[WEB][DISK SPACE USAGE]" );

        List< DiskSpaceUsage > diskSpaceUsageList = VideoRecorderUtil.getDiskSpaceUsage();
        diskSpaceUsageList.add( VideoRecorderUtil.getDiskSpaceUsage( new File( System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH ) ) ) );

        return diskSpaceUsageList;
    }
}
