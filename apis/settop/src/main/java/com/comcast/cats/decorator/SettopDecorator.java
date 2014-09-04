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
package com.comcast.cats.decorator;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.Settop;
import com.comcast.cats.domain.Controller;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.RFPlant;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.info.RfMode;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.AudioProvider;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.OCRProvider;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.VideoSelectionProvider;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.provider.exceptions.RFControlProviderException;
import com.comcast.cats.provider.exceptions.VideoRecorderException;
import com.comcast.cats.recorder.Recorder;

/**
 * Implement a Settop decorator so that dynamic functionality can be added to a
 * settop at runtime. Examples: Cable Card Support DCLI Support Tru2Way Support
 * Legacy Support etc.
 * 
 * @author cfrede001
 */
public abstract class SettopDecorator implements Settop
{
    /**
     * serial Version ID
     */
    private static final long     serialVersionUID = 1L;

    /*
     * At the moment DTA_UI is not available for HD_DTA,According to Sandeep
     * this will come in the future. The remote command sequence for HD_DTA with
     * DTA_UI could be different.
     */
    protected static final String DTA_UI           = "DTA_UI";

    /**
     * Settop instance
     */
    protected Settop              decoratedSettop;

    /**
     * Constructor
     * 
     * @param settop
     *            Settop
     */
    public SettopDecorator( Settop settop )
    {
        this.decoratedSettop = settop;
    }

    /**
     * @return AudioProvider of the settop.
     */
    @Override
    public AudioProvider getAudio()
    {
        return decoratedSettop.getAudio();
    }

    /**
     * @return ImageCompareProvider of the settop.
     */
    @Override
    public ImageCompareProvider getImageCompareProvider()
    {
        return decoratedSettop.getImageCompareProvider();
    }

    /**
     * @return Logger.
     */
    @Override
    public Logger getLogger()
    {
        return decoratedSettop.getLogger();
    }

    /**
     * @return Logging directory.
     */
    public String getLogDirectory()
    {
        return getHostMacAddress().trim().replace( ":", "" ).toUpperCase();
    }

    /**
     * @return OCRProvider of the settop.
     */
    @Override
    public OCRProvider getOCRProvider()
    {
        return decoratedSettop.getOCRProvider();
    }

    /**
     * Method for logging Info message
     * 
     * @param message
     *            -String
     */
    @Override
    public void logInfo( String message )
    {
        decoratedSettop.logInfo( message );
    }

    /**
     * Method for logging Error message
     * 
     * @param message
     *            -String
     */
    @Override
    public void logError( String message )
    {
        decoratedSettop.logError( message );
    }

    /**
     * Method for logging Warn message
     * 
     * @param message
     *            -String
     */
    @Override
    public void logWarn( String message )
    {
        decoratedSettop.logWarn( message );
    }

    /**
     * Method for logging Debug message
     * 
     * @param message
     *            -String
     */
    @Override
    public void logDebug( String message )
    {
        decoratedSettop.logDebug( message );
    }

    /**
     * @return PowerProvider of the settop.
     */
    @Override
    public PowerProvider getPower()
    {
        return decoratedSettop.getPower();
    }

    /**
     * @return Recorder of the settop.
     */
    @Override
    public Recorder getRecorder()
    {
        return decoratedSettop.getRecorder();
    }

    /**
     * @return RemoteProvider of the settop.
     */
    @Override
    public RemoteProvider getRemote()
    {
        return decoratedSettop.getRemote();
    }

    /**
     * @return SettopInfo of the settop.
     */
    @Override
    public SettopInfo getSettopInfo()
    {
        return decoratedSettop.getSettopInfo();
    }

    /**
     * @return TraceProvider of the settop.
     */
    @Override
    public TraceProvider getTrace()
    {
        return decoratedSettop.getTrace();
    }

    /**
     * @return VideoProvider of the settop.
     */
    @Override
    public VideoProvider getVideo()
    {
        return decoratedSettop.getVideo();
    }

    /**
     * @return VideoSelectionProvider of the settop.
     */
    @Override
    public VideoSelectionProvider getVideoSelection()
    {
        return decoratedSettop.getVideoSelection();
    }

    /**
     * Find the extra property value for the given key. The find routine should
     * ignore whitespace and be case insensitive when searching against the list
     * of extra properties.
     * 
     * @param key
     * @return - Value corresponding to key or null otherwise.
     */
    @Override
    public String findExtraProperty( String key )
    {
        return decoratedSettop.findExtraProperty( key );
    }

    /**
     * Getter method for Settop audio path
     * 
     * @return URI of audio
     */
    @Override
    public URI getAudioPath()
    {
        return decoratedSettop.getAudioPath();
    }

    /**
     * Getter method for Clickstream Path
     * 
     * @return URI
     */
    @Override
    public URI getClickstreamPath()
    {
        return decoratedSettop.getClickstreamPath();
    }

    /**
     * Getter method for Cluster Path
     * 
     * @return URI
     */
    @Override
    public URI getClusterPath()
    {
        return decoratedSettop.getClusterPath();
    }

    /**
     * @return the Content
     */
    @Override
    public String getContent()
    {
        return decoratedSettop.getContent();
    }

    /**
     * @return the Environment Id
     */
    @Override
    public String getEnvironmentId()
    {
        return decoratedSettop.getEnvironmentId();
    }

    /**
     * Method will get the extra properties
     * 
     * @return Map of properties
     */
    @Override
    public Map< String, String > getExtraProperties()
    {
        return decoratedSettop.getExtraProperties();
    }

    /**
     * Getter method for Settop firmware version
     * 
     * @return firmware version as String
     */
    @Override
    public String getFirmwareVersion()
    {
        return decoratedSettop.getFirmwareVersion();
    }

    /**
     * Getter method for Settop hardware revision
     * 
     * @return hardware revision as String
     */
    @Override
    public String getHardwareRevision()
    {
        return decoratedSettop.getHardwareRevision();
    }

    /**
     * Getter method for Settop ip4 address
     * 
     * @return ip4 address as String
     */
    @Override
    public String getHostIp4Address()
    {
        return decoratedSettop.getHostIp4Address();
    }

    /**
     * Getter method for Settop ip4 Inet address as of now this is Unsupported
     * operation
     * 
     * @return Inet4Address
     */
    @Override
    public Inet4Address getHostIp4InetAddress()
    {
        return decoratedSettop.getHostIp4InetAddress();
    }

    /**
     * Getter method for Settop ip6 address
     * 
     * @return ip6 address as String
     */
    @Override
    public String getHostIp6Address()
    {
        return decoratedSettop.getHostIp6Address();
    }

    /**
     * Getter method for Settop ip6 Inet address as of now this is Unsupported
     * operation
     * 
     * @return Inet6Address
     */
    @Override
    public Inet6Address getHostIp6InetAddress()
    {
        return decoratedSettop.getHostIp6InetAddress();
    }

    /**
     * Getter method for Settop ip address
     * 
     * @return ip as String
     */
    @Override
    public String getHostIpAddress()
    {
        return decoratedSettop.getHostIpAddress();
    }

    /**
     * Getter method for Settop Inet address
     * 
     * @return InetAddress
     */
    @Override
    public InetAddress getHostIpInetAddress()
    {
        return decoratedSettop.getHostIpInetAddress();
    }

    /**
     * Getter method for Settop mac address
     * 
     * @return mac address as String
     */
    @Override
    public String getHostMacAddress()
    {
        return decoratedSettop.getHostMacAddress();
    }

    /**
     * Getter method for Settop Id
     * 
     * @return Id as String
     */
    @Override
    public String getId()
    {
        return decoratedSettop.getId();
    }

    /**
     * Getter method for Settop name
     * 
     * @return name as String
     */
    public String getName()
    {
        return decoratedSettop.getName();
    }

    /**
     * Getter method for Settop MCard SerialNumber
     * 
     * @return MCard SerialNumber as String
     */
    @Override
    public String getMCardSerialNumber()
    {
        return decoratedSettop.getMCardSerialNumber();
    }

    /**
     * Getter method for Settop Make
     * 
     * @return make as String
     */
    @Override
    public String getMake()
    {
        return decoratedSettop.getMake();
    }

    /**
     * Getter method for Settop Manufacturer
     * 
     * @return Manufacturer as String
     */
    @Override
    public String getManufacturer()
    {
        return decoratedSettop.getManufacturer();
    }

    /**
     * Getter method for Settop Mcard Mac Address
     * 
     * @return Mcard Mac Address as String
     */
    @Override
    public String getMcardMacAddress()
    {
        return decoratedSettop.getMcardMacAddress();
    }

    /**
     * Getter method for Settop model
     * 
     * @return model as String
     */
    @Override
    public String getModel()
    {
        return decoratedSettop.getModel();
    }

    /**
     * Getter method for Settop power path
     * 
     * @return URI of power
     */
    @Override
    public URI getPowerPath()
    {
        return decoratedSettop.getPowerPath();

    }

    /**
     * Method will get the RackId
     * 
     * @return String
     */
    @Override
    public String getRackId()
    {
        return decoratedSettop.getRackId();
    }

    /**
     * Getter method for Settop Remote path
     * 
     * @return URI of Remote
     */
    @Override
    public URI getRemotePath()
    {
        return decoratedSettop.getRemotePath();
    }

    /**
     * Getter method for Settop Remote Type
     * 
     * @return String RemoteType
     */
    @Override
    public String getRemoteType()
    {
        return decoratedSettop.getRemoteType();
    }

    /**
     * Getter method for Settop serial number
     * 
     * @return serial number as String
     */
    @Override
    public String getSerialNumber()
    {
        return decoratedSettop.getSerialNumber();
    }

    /**
     * Getter method for Settop trace path
     * 
     * @return URI of trace
     */
    @Override
    public URI getTracePath()
    {
        return decoratedSettop.getTracePath();
    }

    /**
     * Getter method for Settop unit address
     * 
     * @return unit address as String
     */
    @Override
    public String getUnitAddress()
    {
        return decoratedSettop.getUnitAddress();
    }

    /**
     * Getter method for Settop video path
     * 
     * @return URI of video
     */
    @Override
    public URI getVideoPath()
    {
        return decoratedSettop.getVideoPath();
    }

    /**
     * Getter method for Settop VideoSelection path
     * 
     * @return URI of VideoSelection
     */
    @Override
    public URI getVideoSelectionPath()
    {
        return decoratedSettop.getVideoSelectionPath();
    }

    /**
     * Getter method for Settop PowerLocator path
     * 
     * @return URI of PowerLocator
     */
    @Override
    public URI getPowerLocator()
    {
        return decoratedSettop.getPowerPath();
    }

    /**
     * Hard powers ON a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @Override
    public void powerOn() throws PowerProviderException
    {
        decoratedSettop.powerOn();
    }

    /**
     * Hard powers OFF a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @Override
    public void powerOff() throws PowerProviderException
    {
        decoratedSettop.powerOff();
    }

    /**
     * Hard power cycles a settop outlet OFF and then ON using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @Override
    public void reboot() throws PowerProviderException
    {
        decoratedSettop.reboot();
    }

    /**
     * Return the ON or Off status state of a WTI device power outlet.
     * 
     * @return The power state returned by the WTI or Netboost power strips.
     */
    @Override
    public String getPowerStatus()
    {
        return decoratedSettop.getPowerStatus();
    }

    /**
     * Method will return settop parent object
     * 
     * @return settop parent object.
     */
    @Override
    public Object getParent()
    {
        return decoratedSettop.getParent();
    }

    /**
     * Getter method for Remote locator info
     * 
     * @return URI containing remote hardware definition.
     */
    @Override
    public URI getRemoteLocator()
    {
        return decoratedSettop.getRemoteLocator();
    }

    /**
     * Setter method for Auto tune enabling
     * 
     * @param autoTuneEnabled
     *            true for enabling auto tune false otherwise.
     */
    @Override
    public void setAutoTuneEnabled( boolean autoTuneEnabled )
    {
        decoratedSettop.setAutoTuneEnabled( autoTuneEnabled );
    }

    /**
     * Method for getting Auto tune status
     * 
     * @return boolean true if auto tune enabled false otherwise.
     */
    @Override
    public boolean isAutoTuneEnabled()
    {
        return decoratedSettop.isAutoTuneEnabled();
    }

    /**
     * To tune a particular channel
     * 
     * @param channel
     *            - channel number
     * @return - True if tuning is successful, false otherwise.
     */
    @Override
    public boolean tune( String channel )
    {
        return decoratedSettop.tune( channel );
    }

    /**
     * To tune a particular channel
     * 
     * @param channel
     *            - channel number
     * @return - True if tuning is successful, false otherwise.
     */
    @Override
    public boolean tune( Integer channel )
    {
        return decoratedSettop.tune( channel );
    }

    /**
     * Setter method for delay
     * 
     * @param delay
     *            - delay in milliseconds
     */
    @Override
    public void setDelay( Integer delay )
    {
        decoratedSettop.setDelay( delay );
    }

    /**
     * Getter method for delay
     * 
     * @return Integer - delay in milliseconds
     */
    @Override
    public Integer getDelay()
    {
        return decoratedSettop.getDelay();
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - command to sent to remote
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( RemoteCommand command )
    {
        return decoratedSettop.pressKey( command );
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - RemoteCommand - command to sent to remote
     * @param delay
     *            - Integer - delay in milliseconds
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( RemoteCommand command, Integer delay )
    {
        return decoratedSettop.pressKey( command, delay );
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - RemoteCommand - command to sent to remote
     * @param count
     *            - Integer - number of times the remote command to be
     *            performed.
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        return decoratedSettop.pressKey( count, command );
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - RemoteCommand - command to sent to remote
     * @param count
     *            - Integer - number of times the remote command to be
     *            performed.
     * @param delay
     *            - Integer - delay in milliseconds
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        return decoratedSettop.pressKey( count, command, delay );
    }

    /**
     * To press a remote key
     * 
     * @param commands
     *            - Array of RemoteCommand - command to sent to remote
     * @param count
     *            - Integer - number of times the remote command to be
     *            performed.
     * @param delay
     *            - Integer - delay in milliseconds
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        return decoratedSettop.pressKey( count, delay, commands );
    }

    /**
     * To press a remote key
     * 
     * @param commands
     *            - Array of RemoteCommand - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        return decoratedSettop.pressKey( commands );
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - Integer - command to sent to remote
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKey( Integer command )
    {
        return decoratedSettop.pressKey( command );
    }

    /**
     * To press a remote key and hold it for some extend
     * 
     * @param command
     *            - RemoteCommand - command to sent to remote
     * @param count
     *            - Integer - number of times the remote command to be
     *            performed.
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKeyAndHold( RemoteCommand command, Integer count )
    {
        return decoratedSettop.pressKeyAndHold( command, count );
    }

    /**
     * To press a set of remote keys
     * 
     * @param commands
     *            - List of RemoteCommand - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKeys( List< RemoteCommand > commands )
    {
        return decoratedSettop.pressKeys( commands );
    }

    /**
     * To press a set of remote keys
     * 
     * @param commands
     *            - List of RemoteCommand - command to sent to remote
     * @param delay
     *            - Integer - delay in milliseconds
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean pressKeys( List< RemoteCommand > commands, Integer delay )
    {
        return decoratedSettop.pressKeys( commands, delay );
    }

    /**
     * To enter custom remote key sequences
     * 
     * @param commands
     *            - List of RemoteCommand - command to sent to remote
     * @param repeatCount
     *            - List of Integer - number of times the remote command to be
     *            performed.
     * @param delay
     *            - List of Integer - delay in milliseconds
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay )
    {
        return decoratedSettop.enterCustomKeySequence( commands, repeatCount, delay );
    }

    /**
     * Return a list of valid commands for this remote.
     * 
     * @return - List of RemoteLayout
     */
    @Override
    public List< RemoteLayout > getValidKeys()
    {
        return decoratedSettop.getValidKeys();
    }

    /**
     * Text entry for Astro remote. The string would be parsed and the
     * individual characters would be transformed to corresponding key codes in
     * the Astro remote.
     * 
     * @param text
     *            - Text Message
     * @return true if the text message executed successfully, false otherwise.
     */
    @Override
    public boolean sendText( String text )
    {
        return decoratedSettop.sendText( text );
    }

    /**
     * getting locked status
     * 
     * @return locked - boolean
     */
    @Override
    public boolean isLocked()
    {
        return decoratedSettop.isLocked();
    }

    /**
     * setting locked status
     * 
     * @param value
     *            - boolean
     */
    @Override
    public void setLocked( boolean value )
    {
        decoratedSettop.setLocked( value );
    }

    /**
     * To enter remote command sequences
     * 
     * @param commands
     *            - List of RemoteCommandSequence - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    @Override
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {
        return decoratedSettop.enterRemoteCommandSequence( commands );
    }

    /**
     * Get all remote types available.
     * 
     * @return all remote types available in this CATS system.
     */
    @Override
    public List< String > getAllRemoteTypes()
    {
        return decoratedSettop.getAllRemoteTypes();
    }

    /**
     * Over write remoteType to use the newly set remote type.
     * 
     * @param remoteType
     */
    @Override
    public void setRemoteType( String remoteType )
    {
        decoratedSettop.setRemoteType( remoteType );
    }

    /**
     * Get the RFPlant.
     * 
     * @return RFPlant
     */
    @Override
    public RFPlant getRFPlant()
    {
        return decoratedSettop.getRFPlant();
    }

    /**
     * Get the Controller.
     * 
     * @return Controller
     */
    @Override
    public Controller getController()
    {
        return decoratedSettop.getController();
    }

    /**
     * Start recording at the server.
     * 
     * @param recordingAliasName
     *            - Mark the session with the name provided.
     * @return true if recording has been started successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    @Override
    public boolean startVideoRecording( String recordingAliasName ) throws VideoRecorderException
    {
        return decoratedSettop.startVideoRecording( recordingAliasName );
    }

    /**
     * Start recording at the server. The files be named using the start time
     * timestamp.
     * 
     * @return true if recording has been started successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    @Override
    public boolean startVideoRecording() throws VideoRecorderException
    {
        return decoratedSettop.startVideoRecording();
    }

    /**
     * Stop recording
     * 
     * @return true if recording has been stopped successfully
     * @throws VideoRecorderException
     *             in case of any error.
     */
    @Override
    public boolean stopVideoRecording() throws VideoRecorderException
    {
        return decoratedSettop.stopVideoRecording();
    }

    /**
     * Get the status of the latest recording.
     * 
     * @return status of the recording session
     * @throws VideoRecorderException
     *             in case of any error.
     */
    @Override
    public String getRecordingInfo() throws VideoRecorderException
    {
        return decoratedSettop.getRecordingInfo();
    }

    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose )
    {
        return decoratedSettop.getHardwareInterfaceByType( hardwarePurpose );
    }

    @Override
    public void connectRF() throws RFControlProviderException
    {
        decoratedSettop.connectRF();
    }

    @Override
    public boolean performShorthandCommandSequence( String text )
    {
        return decoratedSettop.performShorthandCommandSequence( text );
    }

    @Override
    public boolean performShorthandCommandSequence( String text, Integer delay )
    {
        return decoratedSettop.performShorthandCommandSequence( text, delay );
    }

    @Override
    public void disconnectRF() throws RFControlProviderException
    {
        decoratedSettop.disconnectRF();
    }

    @Override
    public RfMode getRFStatus()
    {
        return decoratedSettop.getRFStatus();
    }
}