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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.comcast.cats.domain.Controller;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.RFPlant;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.info.RfMode;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.AudioProvider;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.OCRProvider;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.RFControlProvider;
import com.comcast.cats.provider.RecorderProvider;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.VideoSelectionProvider;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.provider.exceptions.RFControlProviderException;
import com.comcast.cats.provider.exceptions.VideoRecorderException;
import com.comcast.cats.recorder.Recorder;

/**
 * Common starting point for most settop operations. The settop delegates most
 * of its operations to a particular provider, so for now an abstract class
 * seems to make sense.
 * 
 * @author cfrede001
 */
public abstract class AbstractSettop implements Settop
{
    private static final long      serialVersionUID = 1L;
    private Object                 parent           = null;
    private SettopInfo             settopInfo;
    private RemoteProvider         remote;
    private PowerProvider          power;
    private AudioProvider          audio;
    private TraceProvider          trace;
    private VideoProvider          video;
    private VideoSelectionProvider videoSelection;
    private ImageCompareProvider   imageCompare;
    private OCRProvider            ocr;
    private RecorderProvider       recorderProvider;
    private Recorder               recorder;
    private RFControlProvider      rfControl;
    private Logger                 logger;
    private boolean                locked;

    /**
     * All properties that don't currently map to a specific interface should be
     * added to this Map.
     */
    private Map< String, String >  extraProperties  = new HashMap< String, String >();

    /**
     * Constructor with no argument
     * 
     */
    public AbstractSettop()
    {
    }

    /**
     * Constructor - parameter passed will be stored as settop description in
     * SettopInfo
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
    public AbstractSettop( String id, String make, String model, String manufacturer, String content,
            String hostMacAddress, String hostIp4Address, String hostIp6Address, String componentType,
            String firmwareVersion, String hardwareRevision, String environmentId )
    {
        // TODO - cef 06/27/2012 - Wouldn't it be nice to do this with the
        // SettopDesc constructor...
        SettopDesc desc = new SettopDesc();
        desc.setId( id );
        desc.setMake( make );
        desc.setManufacturer( manufacturer );
        desc.setModel( model );
        desc.setContent( content );
        desc.setHostMacAddress( hostMacAddress );
        desc.setHostIp4Address( hostIp4Address );
        desc.setHostIp6Address( hostIp6Address );
        desc.setComponentType( componentType );
        desc.setFirmwareVersion( firmwareVersion );
        desc.setHardwareRevision( hardwareRevision );
        desc.setEnvironmentId( environmentId );

        this.settopInfo = desc;
    }

    /**
     * Constructor - parameter passed will be stored as settop details in
     * SettopInfo
     * 
     * @param settopInfo
     *            - settop details
     */
    public AbstractSettop( SettopInfo settopInfo )
    {
        this.settopInfo = settopInfo;
    }

    /**
     * Constructor - parameter passed will be stored as settop description in
     * SettopInfo
     * 
     * @param settopInfo
     *            - settop details
     * @param remote
     *            - RemoteProvider
     * @param power
     *            - PowerProvider
     * @param audio
     *            - AudioProvider
     * @param trace
     *            - TraceProvider
     * @param video
     *            - settop hostMacAddress
     * @param videoSelection
     *            - settop hostIp4Address
     */
    public AbstractSettop( SettopInfo settopInfo, RemoteProvider remote, PowerProvider power, AudioProvider audio,
            TraceProvider trace, VideoProvider video, VideoSelectionProvider videoSelection )
    {
        this.settopInfo = settopInfo;
        this.remote = remote;
        this.power = power;
        this.audio = audio;
        this.trace = trace;
        this.video = video;
        this.videoSelection = videoSelection;
    }

    /**
     * Getter method for Settop Id
     * 
     * @return Id as String
     */
    public String getId()
    {
        return settopInfo.getId();
    }

    /**
     * Getter method for Settop name
     * 
     * @return name as String
     */
    public String getName()
    {
        return settopInfo.getName();
    }

    /**
     * Getter method for Settop mac address
     * 
     * @return mac address as String
     */
    public String getHostMacAddress()
    {
        return settopInfo.getHostMacAddress();
    }

    /**
     * Getter method for Settop make
     * 
     * @return make as String
     */
    public String getMake()
    {
        return settopInfo.getMake();
    }

    /**
     * Getter method for Settop manufacturer
     * 
     * @return manufacturer as String
     */
    public String getManufacturer()
    {
        return settopInfo.getManufacturer();
    }

    /**
     * Getter method for Settop model
     * 
     * @return model as String
     */
    public String getModel()
    {
        return settopInfo.getModel();
    }

    /**
     * Getter method for Settop serial number
     * 
     * @return serial number as String
     */
    public String getSerialNumber()
    {
        return settopInfo.getSerialNumber();
    }

    /**
     * Getter method for Settop unit address
     * 
     * @return unit address as String
     */
    public String getUnitAddress()
    {
        return settopInfo.getUnitAddress();
    }

    /**
     * Getter method for Settop ip address For now the default hostIp address is
     * the ip4 address.
     * 
     * @return ip as String
     */
    public String getHostIpAddress()
    {
        // For now the default hostIp address is the ip4 address.
        return settopInfo.getHostIp4Address();
    }

    /**
     * Getter method for Settop firmware version
     * 
     * @return firmware version as String
     */
    public String getFirmwareVersion()
    {
        return settopInfo.getFirmwareVersion();
    }

    /**
     * Getter method for Settop hardware revision
     * 
     * @return hardware revision as String
     */
    public String getHardwareRevision()
    {
        return settopInfo.getHardwareRevision();
    }

    /**
     * Getter method for Settop content
     * 
     * @return content as String
     */
    public String getContent()
    {
        return settopInfo.getContent();
    }

    /**
     * Getter method for Settop ip4 address
     * 
     * @return ip4 address as String
     */
    public String getHostIp4Address()
    {
        return settopInfo.getHostIp4Address();
    }

    /**
     * Getter method for Settop ip6 address
     * 
     * @return ip6 address as String
     */
    public String getHostIp6Address()
    {
        return settopInfo.getHostIp6Address();
    }

    /**
     * Getter method for Settop ip4 Inet address as of now this is Unsupported
     * operation
     * 
     * @return Inet4Address
     * @throws UnsupportedOperationException
     */
    public Inet4Address getHostIp4InetAddress()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Getter method for Settop ip6 Inet address as of now this is Unsupported
     * operation
     * 
     * @return Inet6Address
     * @throws UnsupportedOperationException
     */
    public Inet6Address getHostIp6InetAddress()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Getter method for Settop Inet address as of now this is Unsupported
     * operation
     * 
     * @return InetAddress
     * @throws UnsupportedOperationException
     */
    public InetAddress getHostIpInetAddress()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Getter method for Settop audio path
     * 
     * @return URI of audio
     */
    public URI getAudioPath()
    {
        return settopInfo.getAudioPath();
    }

    /**
     * Getter method for Settop click stream path
     * 
     * @return URI of click stream
     */
    public URI getClickstreamPath()
    {
        return settopInfo.getClickstreamPath();
    }

    /**
     * Getter method for Settop cluster path
     * 
     * @return URI of cluster
     */
    public URI getClusterPath()
    {
        return settopInfo.getClusterPath();
    }

    /**
     * Getter method for Settop power path
     * 
     * @return URI of power
     */
    public URI getPowerPath()
    {
        return settopInfo.getPowerPath();
    }

    /**
     * Getter method for Settop remote path
     * 
     * @return URI of remote
     */
    public URI getRemotePath()
    {
        return settopInfo.getRemotePath();
    }

    /**
     * Getter method for Settop trace path
     * 
     * @return URI of trace
     */
    public URI getTracePath()
    {
        return settopInfo.getTracePath();
    }

    /**
     * Getter method for Settop video path
     * 
     * @return URI of video
     */
    public URI getVideoPath()
    {
        return settopInfo.getVideoPath();
    }

    /**
     * Getter method for Settop video selection path
     * 
     * @return URI of video selection
     */
    public URI getVideoSelectionPath()
    {
        return settopInfo.getVideoSelectionPath();
    }

    /**
     * Getter method for Settop info
     * 
     * @return SettopInfo
     */
    public SettopInfo getSettopInfo()
    {
        return settopInfo;
    }

    /**
     * Setter method for Settop info
     * 
     * @param settopInfo
     */
    public void setSettopInfo( SettopInfo settopInfo )
    {
        this.settopInfo = settopInfo;
    }

    /**
     * Getter method for Remote locator info
     * 
     * @return URI containing remote hardware definition.
     */
    public URI getRemoteLocator()
    {
        return remote.getRemoteLocator();
    }

    /**
     * Getter method for Remote provider
     * 
     * @return RemoteProvider of the settop.
     */
    public RemoteProvider getRemote()
    {
        return remote;
    }

    /**
     * Setter method for Auto tune enabling
     * 
     * @param autoTuneEnabled
     *            true for enabling auto tune false otherwise.
     */
    public void setAutoTuneEnabled( boolean autoTuneEnabled )
    {
        logInfo( "setAutoTuneEnabled " + autoTuneEnabled );
        remote.setAutoTuneEnabled( autoTuneEnabled );
    }

    /**
     * Method for getting Auto tune status
     * 
     * @return boolean true if auto tune enabled false otherwise.
     */
    public boolean isAutoTuneEnabled()
    {
        return remote.isAutoTuneEnabled();
    }

    /**
     * Getter method for delay
     * 
     * @return Integer - delay in milliseconds
     */
    public Integer getDelay()
    {
        return remote.getDelay();
    }

    /**
     * Setter method for delay
     * 
     * @param delay
     *            - delay in milliseconds
     */
    public void setDelay( Integer delay )
    {
        remote.setDelay( delay );
    }

    /**
     * To tune a particular channel
     * 
     * @param channel
     *            - channel number
     * @return - True if tuning is successful, false otherwise.
     */
    public boolean tune( String channel )
    {
        boolean toReturn = remote.tune( channel );
        logInfo( "tune: " + channel + " returned: " + toReturn );
        return toReturn;
    }

    /**
     * To tune a particular channel
     * 
     * @param channel
     *            - channel number
     * @return - True if tuning is successful, false otherwise.
     */
    public boolean tune( Integer channel )
    {
        boolean toReturn = remote.tune( channel );
        logInfo( "tune: " + channel + " returned: " + toReturn );
        return toReturn;
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - RemoteCommand to sent to remote
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    public boolean pressKey( RemoteCommand command )
    {
        boolean toReturn = remote.pressKey( command );
        logInfo( "pressKey: " + command + " returned: " + toReturn );
        return toReturn;
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
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay )
    {
        boolean toReturn = remote.enterCustomKeySequence( commands, repeatCount, delay );
        logInfo( "enterCustomKeySequence: returned: " + toReturn );
        return false;
    }

    /**
     * To enter remote command sequences
     * 
     * @param commands
     *            - List of RemoteCommandSequence - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {
        boolean toReturn = remote.enterRemoteCommandSequence( commands );
        logInfo( "enterCustomKeySequence: returned: " + toReturn );
        return false;
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
    public boolean pressKey( RemoteCommand command, Integer delay )
    {
        boolean toReturn = remote.pressKey( command, delay );
        logInfo( "pressKey: " + command + " with delay: " + delay + " returned: " + toReturn );
        return toReturn;
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
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        boolean toReturn = remote.pressKey( count, command );
        logInfo( count + " times pressKey: " + command + " returned: " + toReturn );
        return false;
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
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        boolean toReturn = remote.pressKey( count, command, delay );
        logInfo( count + " times pressKey: " + command + " with delay: " + delay + " returned: " + toReturn );
        return false;
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
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        boolean toReturn = remote.pressKey( count, delay, commands );
        logInfo( count + " times pressKey with: " + commands.length + " commands, with delay: " + delay + " returned: "
                + toReturn );
        return toReturn;
    }

    /**
     * To press a remote key
     * 
     * @param commands
     *            - Array of RemoteCommand - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    public boolean pressKey( RemoteCommand[] commands )
    {
        boolean toReturn = remote.pressKey( commands );
        logInfo( "pressKey with: " + commands.length + " commands, returned: " + toReturn );
        return toReturn;
    }

    /**
     * To press a remote key
     * 
     * @param command
     *            - Integer - command to sent to remote
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    public boolean pressKey( Integer command )
    {
        boolean toReturn = remote.pressKey( command );
        logInfo( "pressKey: " + command + " returned: " + toReturn );
        return toReturn;
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
    public boolean pressKeyAndHold( RemoteCommand command, Integer count )
    {
        boolean toReturn = remote.pressKeyAndHold( command, count );
        logInfo( "pressKeyAndHold: " + command + " count: " + count + " returned: " + toReturn );
        return toReturn;
    }

    /**
     * To press a set of remote keys
     * 
     * @param commands
     *            - List of RemoteCommand - command to sent to remote
     * @return - True if all remote command operations are successful, false
     *         otherwise.
     */
    public boolean pressKeys( List< RemoteCommand > commands )
    {
        boolean toReturn = remote.pressKeys( commands );
        if ( commands != null )
        {
            logInfo( "pressKeys with: " + commands.size() + " commands returned: " + toReturn );
        }
        return toReturn;
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
    public boolean pressKeys( List< RemoteCommand > commands, Integer delay )
    {
        boolean toReturn = remote.pressKeys( commands, delay );
        if ( commands != null )
        {
            logInfo( "pressKeys with: " + commands.size() + " commands, delay: " + delay + " returned: " + toReturn );
        }
        return toReturn;
    }

    /**
     * Return a list of valid commands for this remote.
     * 
     * @return - List of RemoteLayout
     */
    public List< RemoteLayout > getValidKeys()
    {
        return remote.getValidKeys();
    }

    /**
     * To set a remote
     * 
     * @param remote
     *            - remote as RemoteProvider
     */
    public void setRemote( RemoteProvider remote )
    {
        this.remote = remote;
    }

    /**
     * Getter method for Power locator
     * 
     * @return URI of the power locator.
     */
    public URI getPowerLocator()
    {
        return power.getPowerLocator();
    }

    /**
     * Getter method for Power provider
     * 
     * @return PowerProvider.
     */
    public PowerProvider getPower()
    {
        return power;
    }

    /**
     * Setter method for Power provider
     * 
     * @param power
     *            as PowerProvider.
     */
    public void setPower( PowerProvider power )
    {
        this.power = power;
    }

    /**
     * Hard powers ON a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    public void powerOn() throws PowerProviderException
    {
        logInfo( "powerOn" );
        power.powerOn();
    }

    /**
     * Hard powers OFF a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    public void powerOff() throws PowerProviderException
    {
        logInfo( "powerOff" );
        power.powerOff();
    }

    /**
     * Hard power cycles a settop outlet OFF and then ON using the WTI device.
     * 
     * @throws PowerProviderException
     */
    public void reboot() throws PowerProviderException
    {
        logInfo( "reboot" );
        power.reboot();
    }

    /**
     * Return the ON or Off status state of a WTI device power outlet.
     * 
     * @return The power state returned by the WTI or Netboost power strips.
     */
    public String getPowerStatus()
    {
        return power.getPowerStatus();
    }

    /**
     * Getter method for Audio Provider
     * 
     * @return AudioProvider.
     */
    public AudioProvider getAudio()
    {
        return audio;
    }

    /**
     * Setter method for Audio Provider
     * 
     * @param audio
     *            as AudioProvider.
     */
    public void setAudio( AudioProvider audio )
    {
        this.audio = audio;
    }

    /**
     * Getter method for Trace Provider
     * 
     * @return TraceProvider.
     */
    public TraceProvider getTrace()
    {
        return trace;
    }

    /**
     * Setter method for Trace Provider
     * 
     * @param trace
     *            as TraceProvider.
     */
    public void setTrace( TraceProvider trace )
    {
        this.trace = trace;
    }

    /**
     * getter method for Video Provider
     * 
     * @return VideoProvider.
     */
    public VideoProvider getVideo()
    {
        return video;
    }

    /**
     * Setter method for Video Provider
     * 
     * @param video
     *            as VideoProvider.
     */
    public void setVideo( VideoProvider video )
    {
        this.video = video;
    }

    /**
     * Setter method for Recorder
     * 
     * @param recorder
     *            as Recorder.
     */
    public void setRecorder( Recorder recorder )
    {
        this.recorder = recorder;
    }

    /**
     * Getter method for Recorder
     * 
     * @return Recorder.
     */
    @Override
    public Recorder getRecorder()
    {
        return recorder;
    }

    /**
     * Getter method for video selection provider
     * 
     * @return VideoSelectionProvider.
     */
    public VideoSelectionProvider getVideoSelection()
    {
        return videoSelection;
    }

    /**
     * Setter method for video selection provider
     * 
     * @param videoSelection
     *            as VideoSelectionProvider.
     */
    public void setVideoSelection( VideoSelectionProvider videoSelection )
    {
        this.videoSelection = videoSelection;
    }

    /**
     * Method will return settop parent object
     * 
     * @return settop parent object.
     */
    public Object getParent()
    {
        return parent;
    }

    /**
     * Method will set settop parent object
     * 
     * @param parent
     *            as Object.
     */
    public void setParent( Object parent )
    {
        this.parent = parent;
    }

    /**
     * Method will set power locator path Not implemented
     * 
     * @param path
     *            as URI.
     */
    public void setPowerLocator( URI path )
    {
        // TODO Auto-generated method stub
    }

    /**
     * Method will set Remote locator path Not implemented
     * 
     * @param path
     *            as URI.
     */
    public void setRemoteLocator( URI path )
    {
        // TODO Auto-generated method stub

    }

    /**
     * Method will get the extra properties
     * 
     * @return Map of properties
     */
    public Map< String, String > getExtraProperties()
    {
        return extraProperties;
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
        return settopInfo.findExtraProperty( key );
    }

    /**
     * Method will set the ImageCompareProvider
     * 
     * @param imageCompare
     *            as ImageCompareProvider
     */
    public void setImageCompareProvider( ImageCompareProvider imageCompare )
    {
        this.imageCompare = imageCompare;
    }

    /**
     * Method will get the ImageCompareProvider
     * 
     * @return ImageCompareProvider
     */
    public ImageCompareProvider getImageCompareProvider()
    {
        return imageCompare;
    }

    /**
     * Method will get the McardMacAddress
     * 
     * @return String
     */
    @Override
    public String getMcardMacAddress()
    {
        return settopInfo.getMcardMacAddress();
    }

    /**
     * Method will get the MCardSerialNumber
     * 
     * @return String
     */
    @Override
    public String getMCardSerialNumber()
    {
        return settopInfo.getMCardSerialNumber();
    }

    /**
     * Method will set the OCRProvider
     * 
     * @param ocr
     *            OCRProvider
     */
    public void setOCRProvider( OCRProvider ocr )
    {
        this.ocr = ocr;
    }

    /**
     * Method will get the OCRProvider
     * 
     * @return OCRProvider
     */
    public OCRProvider getOCRProvider()
    {
        return ocr;
    }

    /**
     * Method will set the RecorderProvider
     * 
     * @param recorderProvider
     *            RecorderProvider
     */
    public void setRecorderProvider( RecorderProvider recorderProvider )
    {
        this.recorderProvider = recorderProvider;
    }

    /**
     * Method will get the RecorderProvider
     * 
     * @return RecorderProvider
     */
    public RecorderProvider getRecorderProvider()
    {
        return recorderProvider;
    }

    /**
     * Method will get the EnvironmentId
     * 
     * @return String
     */
    @Override
    public String getEnvironmentId()
    {
        return settopInfo.getEnvironmentId();
    }

    /**
     * Method will get the RackId
     * 
     * @return String
     */
    @Override
    public String getRackId()
    {
        return settopInfo.getRackId();
    }

    /**
     * Method will set the Logger
     * 
     * @param logger
     *            Logger
     */
    public void setLogger( Logger logger )
    {
        this.logger = logger;
    }

    @Deprecated
    /**
     * If this method is being used to get the Logger, then the calling method, should
     * also set the context (settop MAC id and log file name).
     * Refer the private method setLoggingContext() for more details.
     */
    public Logger getLogger()
    {

        return this.logger;
    }

    /**
     * Method will get the LogDirectory
     * 
     * @return String
     */
    public String getLogDirectory()
    {
        return getHostMacAddress().trim().replace( ":", "" ).toUpperCase();
    }

    /**
     * Method for logging Warn message
     * 
     * @param message
     *            - String
     */
    public void logWarn( String message )
    {
        if ( logger != null )
        {
            setLoggingContext();
            logger.warn( message );
        }
    }

    /**
     * Method for logging Debug message
     * 
     * @param message
     *            - String
     */
    public void logDebug( String message )
    {
        if ( logger != null )
        {
            setLoggingContext();
            logger.debug( message );
        }
    }

    /**
     * Method for logging Error message
     * 
     * @param message
     *            - String
     */
    public void logError( String message )
    {
        if ( logger != null )
        {
            setLoggingContext();
            logger.error( message );
        }
    }

    /**
     * Method for logging Info message
     * 
     * @param toLog
     *            - String
     */
    public void logInfo( String toLog )
    {
        if ( logger != null )
        {
            setLoggingContext();
            logger.info( toLog );
        }
    }

    /**
     * Method will set the LoggingContext
     */
    private void setLoggingContext()
    {
        Map< String, String > mdcMap = new HashMap< String, String >();
        mdcMap.put( "SettopMac", getLogDirectory() );
        mdcMap.put( "LogFileName", "RunTimeExecution.log" );
        MDC.setContextMap( mdcMap );
    }

    /**
     * setting locked status
     * 
     * @param locked
     *            - boolean
     */
    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }

    /**
     * getting locked status
     * 
     * @return locked - boolean
     */
    @Override
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * setting recorder with parameters filepath
     * 
     * @param filepath
     *            - String - file location
     */
    public void record( String filepath )
    {
        if ( recorder != null )
        {
            recorder.setOutputFileLocation( filepath );
            recorder.record();
        }
    }

    /**
     * setting recorder with parameters sampleInterval and filepath
     * 
     * @param sampleInterval
     *            - int
     * @param filepath
     *            - String - file location
     */
    public void record( int sampleInterval, String filepath )
    {
        if ( recorder != null )
        {
            recorder.setSampleInterval( sampleInterval );
            record( filepath );
        }
    }

    /**
     * setting recorder with parameters
     * 
     * @param sampleInterval
     *            - int
     * @param compressionRatio
     *            - float
     * @param startTime
     *            - Date
     * @param endTime
     *            - Date
     * @param filepath
     *            - String - file location
     */
    public void record( int sampleInterval, float compressionRatio, Date startTime, Date endTime, String filepath )
    {
        if ( recorder != null )
        {
            recorder.setCompressionRatio( compressionRatio );
            recorder.setRecordingStartTime( startTime );
            recorder.setRecordingEndTime( endTime );
            record( sampleInterval, filepath );
        }
    }

    /**
     * setting recorder with parameters
     * 
     * @param sampleInterval
     *            - int
     * @param compressionRatio
     *            - float
     * @param durationSec
     *            - int
     * @param filepath
     *            - String - file location
     */
    public void record( int sampleInterval, float compressionRatio, int durationSec, String filepath )
    {
        if ( recorder != null )
        {
            recorder.setCompressionRatio( compressionRatio );
            recorder.setSampleInterval( sampleInterval );
            recorder.setOutputFileLocation( filepath );
            recorder.record( durationSec );
        }
    }

    /**
     * Stop current recording
     */
    public void stopRecord()
    {
        if ( recorder != null )
        {
            recorder.stopRecording();
        }
    }

    /**
     * Pause current recording.
     */
    public void pauseRecord()
    {
        if ( recorder != null )
        {
            recorder.pauseRecording();
        }
    }

    /**
     * Resume recording.
     */
    public void resumeRecord()
    {
        if ( recorder != null )
        {
            recorder.resumeRecording();
        }
    }

    /**
     * Get all remote types available.
     * 
     * @return all remote types available in this CATS system.
     */
    @Override
    public List< String > getAllRemoteTypes()
    {
        return remote.getAllRemoteTypes();
    }

    /**
     * Over write remoteType to use the newly set remote type.
     * 
     * @param remoteType
     */
    @Override
    public void setRemoteType( String remoteType )
    {
        if ( remote != null )
        {
            remote.setRemoteType( remoteType );
            // Boby:A change in remote type should also update the info.
            ( ( SettopDesc ) settopInfo ).setRemoteType( remoteType );
        }
    }

    /**
     * Retrieve the remote type for this provider/settop.
     * 
     * @return String representing the type for this remote.
     */
    @Override
    public String getRemoteType()
    {
        String retVal;
        if ( remote != null )
        { // if remoteType is overridden.
            retVal = remote.getRemoteType();
        }
        else
        {
            retVal = settopInfo.getRemoteType();
        }
        return retVal;
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
        boolean toReturn = remote.sendText( text );
        logInfo( "sendText returned: " + toReturn );
        return toReturn;
    }

    /**
     * Function which enables users to send remote commands using single
     * character short hand notation Eg: M:Menu S:Select G:GUIDE 1:ONE etc. So
     * on specifying MS1G would be interpreted as Menu,Select,ONE and Guide
     * 
     * @param text
     *            - string having the single character representation for keys
     * @return true if the text message executed successfully, false otherwise.
     */
    @Override
    public boolean performShorthandCommandSequence( String text )
    {
        boolean toReturn = remote.performShorthandCommandSequence( text );
        logInfo( "enterShortCutKeys returned: " + toReturn );
        return toReturn;
    }

    /**
     * Function which enables users to send remote commands using single
     * character short hand notation Eg: M:Menu S:Select G:GUIDE 1:ONE etc. So
     * on specifying MS1G would be interpreted as Menu,Select,ONE and Guide
     * 
     * @param text
     *            - string having the single character representation for keys
     * @param delay
     *            -delay in milliseconds between each key press.
     * @return true if the text message executed successfully, false otherwise.
     */
    @Override
    public boolean performShorthandCommandSequence( String text, Integer delay )
    {
        boolean toReturn = remote.performShorthandCommandSequence( text, delay );
        logInfo( "enterShortCutKeys returned: " + toReturn );
        return toReturn;
    }

    /**
     * Get the RFPlant.
     * 
     * @return RFPlant
     */
    @Override
    public RFPlant getRFPlant()
    {
        return settopInfo.getRFPlant();
    }

    /**
     * Get the Controller.
     * 
     * @return Controller
     */
    @Override
    public Controller getController()
    {
        return settopInfo.getController();
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
        boolean retVal = false;
        if ( recorderProvider != null )
        {
            retVal = recorderProvider.startVideoRecording( recordingAliasName );
        }
        else
        {
            throw new VideoRecorderException( "No Provider for RecorderService found" );
        }
        return retVal;
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
        boolean retVal = false;
        if ( recorderProvider != null )
        {
            retVal = recorderProvider.startVideoRecording();
        }
        else
        {
            throw new VideoRecorderException( "No Provider for RecorderService found" );
        }
        return retVal;
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
        boolean retVal = false;
        if ( recorderProvider != null )
        {
            retVal = recorderProvider.stopVideoRecording();
        }
        else
        {
            throw new VideoRecorderException( "No Provider for RecorderService found" );
        }
        return retVal;
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
        String retVal = null;
        if ( recorderProvider != null )
        {
            retVal = recorderProvider.getRecordingInfo();
        }
        else
        {
            throw new VideoRecorderException( "No Provider for RecorderService found" );
        }
        return retVal;
    }

    @Override
    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose )
    {
        return settopInfo.getHardwareInterfaceByType( hardwarePurpose );
    }

    /**
     * Get the RFControlProvider for this settop.
     */
    public RFControlProvider getRfControl()
    {
        return rfControl;
    }

    /**
     * Set the RFControlProvider for this settop.
     */
    public void setRfControl( RFControlProvider rfControl )
    {
        logInfo( "Settop setRfControl " + rfControl );
        this.rfControl = rfControl;
    }

    @Override
    public void connectRF() throws RFControlProviderException
    {
        logInfo( "connectRF" );
        if ( rfControl != null )
        {
            rfControl.connectRF();
        }
    }

    @Override
    public void disconnectRF() throws RFControlProviderException
    {
        logInfo( "disconnectRF" );
        if ( rfControl != null )
        {
            rfControl.disconnectRF();
        }
    }

    @Override
    public RfMode getRFStatus()
    {
        logInfo( "getRFStatus" );

        RfMode mode = null;
        if ( rfControl != null )
        {
            mode = rfControl.getRFStatus();
        }
        return mode;
    }
}