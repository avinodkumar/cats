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
package com.comcast.cats.script

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;

import com.comcast.cats.AbstractSettop;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.decorator.SettopDiagnostic;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.image.ImageCompareResult;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.AudioProvider;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.OCRProvider;
import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.VideoSelectionProvider;
import com.comcast.cats.provider.exceptions.ImageCompareException;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.provider.exceptions.VideoRecorderException;
import com.comcast.cats.recorder.Recorder;

/**
 * Settop implementation for supporting pause/resume and stop operations during script evaluation.
 * 
 * @author minu
 *
 */
class PausableSettop extends AbstractSettop {

    Settop decoratedSettop
    private Object lock
    private boolean paused = false
    private boolean stopFlag = false

    def PausableSettop(Settop settop) {
        this.decoratedSettop = settop
        lock = new Object()

        registerPauseOnMethods( decoratedSettop )
    }

    def registerPauseOnMethods(def object){
        if(object){
            /**
             * Overriding object's metaClass.invokeMethod. Any method call on the object will be routed through this method.
             * Here we are checking whether the execution need to be paused or continue. If the 'paused' flag is false, 
             * it will wait here until it becomes true, and then the actual method call happens.
             */
            object.metaClass.invokeMethod = {String name, args ->
                if( !(name.equals("pauseExecution") || name.equals("stopExecution") || name.equals("resumeExecution"))){
                    pause()
                }
                
                def validMethod = object.metaClass.getMetaMethod(name, args)
                if( validMethod ){
                    validMethod.doMethodInvoke(delegate, args)
                }
            }
        }
    }

    def pause() {
		if(stopFlag){
			//assigning the metaclass to null so that restart of script player does not cause exception
			destroyInterceptor()
			throw new Exception("Exception to stop script execution" )
		}
        synchronized(lock) {
            while(paused)   {
                if(stopFlag){
					destroyInterceptor()
                    throw new Exception("Exception to stop script execution")
                }
                //putting a timed wait so that this does not go into a deadlock
                lock.wait(1000)
            }
        }
    }
    
    def destroyInterceptor(){
        decoratedSettop.metaClass = null
    }

    def pauseExecution(){
        paused = true
    }

    def stopExecution(){
        stopFlag = true
    }

    def resumeExecution(){
        paused = false
        synchronized (lock) {
            lock.notifyAll()
        }
    }

    /**
     * Method to log message to settop's run time execution log.
     * Since the log level of logger in decoratedSettop is set to info by default,  
     * the messages are logged as info messages. 
     * (Refer settop-log4j.xml in cats-settop-impl/src/main/resources/)
     * @param message - Message to log
     */
    def logMessage(String message){
       decoratedSettop.logInfo(message);
   
    }

    /**
     * Diagnostic Support for Scripting
     */
    def diagScreenBinding(){
        pause()
        if( decoratedSettop instanceof SettopDiagnostic ){
            SettopDiagnostic settopDiagnostic = (SettopDiagnostic) decoratedSettop
            settopDiagnostic.showDiagMenu()
        }
        else{
            logMessage("SettopDiagnostic is not supported")
        }
    }

    public boolean waitForFullImage( String filePath, long timeOut )
    {
        decoratedSettop.getImageCompareProvider().waitForFullImage( filePath, timeOut )
    }

    public boolean waitForFullImage( String filePath )
    {
        decoratedSettop.getImageCompareProvider().waitForFullImage( filePath )
    }

    public boolean waitForImageRegion( String imgXMLPath, long timeOut )
    {
        decoratedSettop.getImageCompareProvider().waitForImageRegion( imgXMLPath, timeOut )
    }

    public boolean waitForImageRegion( String imgXMLPath, String regionName, long timeOut )
    {
        decoratedSettop.getImageCompareProvider().waitForImageRegion( imgXMLPath, regionName, timeOut )
    }

    public boolean waitForImageRegion( String imgXMLPath, String regionName )
    {
        decoratedSettop.getImageCompareProvider().waitForImageRegion( imgXMLPath, regionName )
    }

    public boolean waitForImageRegion( String imgXMLPath )
    {
        decoratedSettop.getImageCompareProvider().waitForImageRegion( imgXMLPath )
    }   
    
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
        int xtolerance, int yTolerance, int redTolerance, int greenTolerance, int blueTolerance,
        float matchPercentage ) throws IOException, JAXBException
    {
        decoratedSettop.getImageCompareProvider().saveImageRegion( imageFilePath, regionName, width, height, x, y, 
            xtolerance, yTolerance, redTolerance, greenTolerance, blueTolerance, matchPercentage )
    }
    
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y,
        float matchPercentage ) throws IOException, JAXBException
    {
        decoratedSettop.getImageCompareProvider().saveImageRegion( imageFilePath, regionName, width, height, x, y, matchPercentage )
    }
    
    public String saveImageRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
    throws IOException, JAXBException
    {
        decoratedSettop.getImageCompareProvider().saveImageRegion( imageFilePath, regionName, width, height, x, y )
    }
    
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName )
    {
        decoratedSettop.getImageCompareProvider().waitForRegionOnTargetRegion(xmlFilePath, refRegionName, targetRegionName )
    }
    
    ImageCompareResult waitForRegionOnTargetRegion( String xmlFilePath, String refRegionName, String targetRegionName, long timeout)
    {
        decoratedSettop.getImageCompareProvider().waitForRegionOnTargetRegion(xmlFilePath, refRegionName, targetRegionName, timeout );
    }
    
    String saveSearchRegion( String imageFilePath, String regionName, int width, int height, int x, int y )
        throws IOException, JAXBException
    {
        decoratedSettop.getImageCompareProvider().saveSearchRegion( imageFilePath, regionName, width, height, x, y )
    }
    
    public String saveVideoImage()
    {
        decoratedSettop.getVideo().saveVideoImage()
    }

    public void saveVideoImage( String filePath )
    {
        decoratedSettop.getVideo().saveVideoImage( filePath )
    }

    @Override
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
    List< Integer > delay )
    {
        decoratedSettop.enterCustomKeySequence( commands, repeatCount, delay )
    }

    @Override
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {
        decoratedSettop.enterRemoteCommandSequence( commands )
    }

    @Override
    public String findExtraProperty( String key )
    {
        decoratedSettop.findExtraProperty( key )
    }

    @Override
    public AudioProvider getAudio()
    {
        decoratedSettop.getAudio()
    }

    @Override
    public URI getAudioPath()
    {
        decoratedSettop.getAudioPath()
    }

    @Override
    public URI getClickstreamPath()
    {
        decoratedSettop.getClickstreamPath()
    }

    @Override
    public URI getClusterPath()
    {
        decoratedSettop.getClusterPath()
    }

    @Override
    public String getContent()
    {
        decoratedSettop.getContent()
    }

    @Override
    public Integer getDelay()
    {
        decoratedSettop.getDelay()
    }

    @Override
    public String getEnvironmentId()
    {
        decoratedSettop.getEnvironmentId()
    }

    @Override
    public Map< String, String > getExtraProperties()
    {
        decoratedSettop.getExtraProperties()
    }

    @Override
    public String getFirmwareVersion()
    {
        decoratedSettop.getFirmwareVersion()
    }

    @Override
    public String getHardwareRevision()
    {
        decoratedSettop.getHardwareRevision()
    }

    @Override
    public String getHostIp4Address()
    {
        decoratedSettop.getHostIp4Address()
    }

    @Override
    public Inet4Address getHostIp4InetAddress()
    {
        decoratedSettop.getHostIp4InetAddress()
    }

    @Override
    public String getHostIp6Address()
    {
        decoratedSettop.getHostIp6Address()
    }

    @Override
    public Inet6Address getHostIp6InetAddress()
    {
        decoratedSettop.getHostIp6InetAddress()
    }

    @Override
    public String getHostIpAddress()
    {
        decoratedSettop.getHostIpAddress()
    }

    @Override
    public InetAddress getHostIpInetAddress()
    {
        decoratedSettop.getHostIpInetAddress()
    }

    @Override
    public String getHostMacAddress()
    {
        decoratedSettop.getHostMacAddress()
    }

    @Override
    public String getId()
    {
        decoratedSettop.getId()
    }

    @Override
    public ImageCompareProvider getImageCompareProvider()
    {
        decoratedSettop.getImageCompareProvider()
    }

    @Override
    public Logger getLogger()
    {
        decoratedSettop.getLogger()
    }

    @Override
    public String getMCardSerialNumber()
    {
        decoratedSettop.getMCardSerialNumber()
    }

    @Override
    public String getMake()
    {
        decoratedSettop.getMake()
    }

    @Override
    public String getManufacturer()
    {
        decoratedSettop.getManufacturer()
    }

    @Override
    public String getMcardMacAddress()
    {
        decoratedSettop.getMcardMacAddress()
    }

    @Override
    public String getModel()
    {
        decoratedSettop.getModel()
    }

    @Override
    public OCRProvider getOCRProvider()
    {
        decoratedSettop.getOCRProvider()
    }

    @Override
    public Object getParent()
    {
        decoratedSettop.getParent()
    }

    @Override
    public PowerProvider getPower()
    {
        decoratedSettop.getPower()
    }

    @Override
    public URI getPowerLocator()
    {
        decoratedSettop.getPowerLocator()
    }

    @Override
    public URI getPowerPath()
    {
        decoratedSettop.getPowerPath()
    }

    @Override
    public String getPowerStatus()
    {
        decoratedSettop.getPowerStatus()
    }

    @Override
    public String getRackId()
    {
        decoratedSettop.getRackId()
    }

    @Override
    public Recorder getRecorder()
    {
        decoratedSettop.getRecorder()
    }

    @Override
    public RemoteProvider getRemote()
    {
        decoratedSettop.getRemote()
    }

    @Override
    public URI getRemoteLocator()
    {
        decoratedSettop.getRemoteLocator()
    }

    @Override
    public URI getRemotePath()
    {
        decoratedSettop.getRemotePath()
    }

    @Override
    public String getRemoteType()
    {
        decoratedSettop.getRemoteType()
    }

    @Override
    public String getSerialNumber()
    {
        decoratedSettop.getSerialNumber()
    }

    @Override
    public SettopInfo getSettopInfo()
    {
        decoratedSettop.getSettopInfo()
    }

    @Override
    public TraceProvider getTrace()
    {
        decoratedSettop.getTrace()
    }

    @Override
    public URI getTracePath()
    {
        decoratedSettop.getTracePath()
    }

    @Override
    public String getUnitAddress()
    {
        decoratedSettop.getUnitAddress()
    }

    @Override
    public List< RemoteLayout > getValidKeys()
    {
        decoratedSettop.getValidKeys()
    }

    @Override
    public VideoProvider getVideo()
    {
        decoratedSettop.getVideo()
    }

    @Override
    public URI getVideoPath()
    {
        decoratedSettop.getVideoPath()
    }

    @Override
    public VideoSelectionProvider getVideoSelection()
    {
        decoratedSettop.getVideoSelection()
    }

    @Override
    public URI getVideoSelectionPath()
    {
        decoratedSettop.getVideoSelectionPath()
    }

    @Override
    public boolean isAutoTuneEnabled()
    {
        decoratedSettop.isAutoTuneEnabled()
    }

    @Override
    public void pauseRecord()
    {
        decoratedSettop.pauseRecord()
    }

    @Override
    public void powerOff() throws PowerProviderException
    {
        decoratedSettop.powerOff()
    }

    @Override
    public void powerOn() throws PowerProviderException
    {
        decoratedSettop.powerOn()
    }

    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        decoratedSettop.pressKey( count, delay, commands )
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        decoratedSettop.pressKey( count, command, delay )
    }

    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        decoratedSettop.pressKey( count, command )
    }

    @Override
    public boolean pressKey( Integer command )
    {
        decoratedSettop.pressKey( command )
    }

    @Override
    public boolean pressKey( RemoteCommand command, Integer delay )
    {
        decoratedSettop.pressKey( command, delay )
    }

    @Override
    public boolean pressKey( RemoteCommand command )
    {
        decoratedSettop.pressKey( command )
    }

    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        decoratedSettop.pressKey( commands )
    }
	
	@Override
    public boolean pressKeyAndHold( RemoteCommand command, Integer count )
    {
        decoratedSettop.pressKeyAndHold( command, count )
    }

    @Override
    public boolean pressKeys( List< RemoteCommand > commands, Integer delay )
    {
        decoratedSettop.pressKeys( commands, delay )
    }

    @Override
    public boolean pressKeys( List< RemoteCommand > commands )
    {
        decoratedSettop.pressKeys( commands )
    }

    @Override
    public void reboot() throws PowerProviderException
    {
        decoratedSettop.reboot()
    }

    @Override
    public void record( int sampleInterval, float compressionRatio, Date startTime, Date endTime, String filepath )
    {
        decoratedSettop.record( sampleInterval, compressionRatio, startTime, endTime, filepath )
    }

    @Override
    public void record( int sampleInterval, float compressionRatio, int durationSec, String filepath )
    {
        decoratedSettop.record( sampleInterval, compressionRatio, durationSec, filepath )
    }

    @Override
    public void record( int sampleInterval, String filepath )
    {
        decoratedSettop.record( sampleInterval, filepath )
    }

    @Override
    public void record( String filepath )
    {
        decoratedSettop.record( filepath )
    }

    @Override
    public void resumeRecord()
    {
        decoratedSettop.resumeRecord()
    }

    @Override
    public boolean sendText( String text )
    {
        decoratedSettop.sendText( text )
    }
	@Override
	public boolean performShorthandCommandSequence( String text )
	{
		decoratedSettop.performShorthandCommandSequence( text )
	}
	@Override
	public boolean performShorthandCommandSequence( String text,Integer delay )
	{
		decoratedSettop.performShorthandCommandSequence( text,delay )
	}
    @Override
    public void setAutoTuneEnabled( boolean autoTuneEnabled )
    {
        decoratedSettop.setAutoTuneEnabled( autoTuneEnabled )
    }

    @Override
    public void setDelay( Integer delay )
    {
        decoratedSettop.setDelay( delay )
    }

    @Override
    public void stopRecord()
    {
        decoratedSettop.stopRecord()
    }

    @Override
    public boolean tune( Integer channel )
    {
        decoratedSettop.tune( channel )
    }

    @Override
    public boolean tune( String channel )
    {
        decoratedSettop.tune( channel )
    }

    @Override
    public void setImageCompareProvider( ImageCompareProvider imageCompare )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setLogger( Logger logger )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setOCRProvider( OCRProvider ocr )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setParent( Object parent )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setPower( PowerProvider power )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setRecorder( Recorder recorder )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setRemote( RemoteProvider remote )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setRemoteLocator( URI path )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setSettopInfo( SettopInfo settopInfo )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setTrace( TraceProvider trace )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setVideo( VideoProvider video )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setVideoSelection( VideoSelectionProvider videoSelection )
    {
        throw new Exception("Unsupporetd operation")
    }

    @Override
    public void setAudio( AudioProvider audio )
    {
        throw new Exception("Unsupported operation")
    }

    @Override
    public void setPowerLocator( URI path )
    {
        throw new Exception("Unsupported operation")
    }

    public boolean startVideoRecording( String recordingAliasName )
    {
        boolean retVal = false;
        try{
           retVal = decoratedSettop.startVideoRecording( recordingAliasName );
        }catch(VideoRecorderException e){
            decoratedSettop.logError("startVideoRecording error : "+e.getMessage());
        }
        return retVal;
    }

    public boolean startVideoRecording()
    {
        boolean retVal = false;
        try{
           retVal = decoratedSettop.startVideoRecording();
        }catch(VideoRecorderException e){
            decoratedSettop.logError("startVideoRecording error : "+e.getMessage());
        }
        return retVal;
    }

    public boolean stopVideoRecording( )
    {
        boolean retVal = false;
        try{
           retVal = decoratedSettop.stopVideoRecording();
        }catch(VideoRecorderException e){
            decoratedSettop.logError("stopVideoRecording error : "+e.getMessage());
        }
        return retVal;
    }

    public String getRecordingInfo( )
    {
        String retVal = "Could not retrieve recording status";
        try{
           retVal = decoratedSettop.getRecordingInfo();
        }catch(VideoRecorderException e){
            decoratedSettop.logError("getRecordingInfo error : "+e.getMessage());
        }
        return retVal;
    }
    
    /**
     * Over write remoteType to use the newly set remote type.
     * @param remoteType
     */
    @Override
    public void setRemoteType( String remoteType )
    {
        decoratedSettop.setRemoteType( remoteType );
    }

}
