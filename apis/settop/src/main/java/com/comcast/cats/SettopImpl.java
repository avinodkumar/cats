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
package com.comcast.cats;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.AudioProvider;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.VideoSelectionProvider;

/**
 * For now no specific functionality is required for the concrete SettopImpl
 * class.
 * 
 * @author cfrede001
 * @since 2.0.0
 * 
 */
public class SettopImpl extends AbstractSettop
{
    /**
     * serial Version ID
     */
    private static final long serialVersionUID = -2885906655174979260L;

    /**
     * Constructor
     */
    public SettopImpl()
    {
        super( new SettopDesc() );
    }

    /**
     * Constructor SettopImpl
     * 
     * @param settopInfo
     *            - SettopInfo
     * @param remote
     *            - RemoteProvider
     * @param power
     *            - PowerProvider
     * @param audio
     *            - AudioProvider
     * @param trace
     *            - TraceProvider
     * @param video
     *            - VideoProvider
     * @param videoSelection
     *            - VideoSelectionProvider
     */
    public SettopImpl( SettopInfo settopInfo, RemoteProvider remote, PowerProvider power, AudioProvider audio,
            TraceProvider trace, VideoProvider video, VideoSelectionProvider videoSelection )
    {
        super( settopInfo, remote, power, audio, trace, video, videoSelection );
    }

    /**
     * Constructor SettopImpl
     * 
     * @param settopInfo
     *            - SettopInfo
     */
    public SettopImpl( SettopInfo settopInfo )
    {
        super( settopInfo );
    }

    /**
     * Constructor
     * 
     * @param id
     *            - settop id
     * @param make
     *            - settop make
     * @param model
     *            - settop model
     * @param manufacturer
     *            - settop manufacturer
     * @param content
     * @param hostMacAddress
     *            - settop hostMacAddress
     * @param hostIp4Address
     *            - settop hostIp4Address
     * @param hostIp6Address
     *            - settop hostIp6Address
     * @param componentType
     *            - settop componentType
     * @param firmwareVersion
     * @param hardwareRevision
     * @param environmentId
     */
    public SettopImpl( String id, String make, String model, String manufacturer, String content,
            String hostMacAddress, String hostIp4Address, String hostIp6Address, String componentType,
            String firmwareVersion, String hardwareRevision, String environmentId )
    {
        super( id, make, model, manufacturer, content, hostMacAddress, hostIp4Address, hostIp6Address, componentType,
                firmwareVersion, hardwareRevision, environmentId );
    }

    /**
     * @return Settop info as String
     */
    @Override
    public String toString()
    {
        return getSettopInfo().toString();
    }

}
