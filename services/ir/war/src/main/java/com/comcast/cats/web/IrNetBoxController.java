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
package com.comcast.cats.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.provider.RemoteRest;
import com.comcast.cats.service.RedRatManager;
import com.comcast.cats.service.ir.redrat.RedRatCommands;
import com.comcast.cats.service.ir.redrat.RedRatDevice;
import com.comcast.cats.service.ir.redrat.RedRatHub;
import com.comcast.cats.service.ir.redrat.RedRatManagerImpl;
import com.comcast.cats.telnet.TelnetConnection;

import static com.comcast.cats.service.ir.redrat.RedRatCommands.*;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.*;

@ManagedBean
@RequestScoped
public class IrNetBoxController
{

    String                 irServiceUrl;
    String                 irNetBoxIp;
    Integer                irNetBoxPort;
    String                 type           = "irnetboxpro3";
    String                 remoteType;
    String                 remoteCommand;
    Integer                count;
    String                 diagnoseIrNetBoxIp;
    Integer                diagnoseIrNetBoxPort;
    private boolean        result;
    private List< String > diagnoseResult = new ArrayList<String>();
    
    @Inject
    RedRatManager rrManager;
    
    private static final Logger logger = LoggerFactory.getLogger( IrNetBoxController.class );

    public void sendPressKey()
    {

        logger.info( "IR WEBPAGE: sendPressKey by "+ getRemoteIPAddress() );
        logger.info( "IR WEBPAGE: sendPressKey irServiceUrl"+ irServiceUrl+" irNetBoxPort "+irNetBoxPort
                +" type "+type+" remoteType "+remoteType);
        boolean response = false;
        try
        {
            String url = "http://" + irServiceUrl + "/ir-service/rest/";
            RemoteRest remoterest = new RemoteRest( url, irNetBoxIp, irNetBoxPort, type, remoteType );
            response = remoterest.pressKey( RemoteCommand.parse( remoteCommand ) );
        }
        catch ( IllegalArgumentException e)
        {
            logger.warn("IrWeb controller sendPressKey exception "+e.getMessage());
        }
        catch( URISyntaxException e )
        {
            logger.warn("IrWeb controller sendPressKey exception "+e.getMessage());    
        }

        setResult( response );
    }

    private String getRemoteIPAddress()
    {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public void sendPressKeyAndHold()
    {
        logger.info( "IR WEBPAGE: sendPressKeyAndHold by "+ getRemoteIPAddress() );
        logger.info( "IR WEBPAGE: sendPressKeyAndHold irServiceUrl"+ irServiceUrl+" irNetBoxPort "+irNetBoxPort
                +" type "+type+" remoteType "+remoteType+" count "+count);
        boolean response = false;
        try
        {
            String url = "http://" + irServiceUrl + "/ir-service/rest/";
            RemoteRest remoterest = new RemoteRest( url, irNetBoxIp, irNetBoxPort, type, remoteType );
            response = remoterest.pressKeyAndHold( RemoteCommand.parse( remoteCommand ), count );
        }
        catch ( IllegalArgumentException e)
        {
            logger.warn("IrWeb controller sendPressKey exception "+e.getMessage());
        }
        catch( URISyntaxException e )
        {
            logger.warn("IrWeb controller sendPressKey exception "+e.getMessage());    
        }

        setResult( response );
    }

    public void diagnose()
    {
        logger.info( "IR WEBPAGE: diagnose by "+ getRemoteIPAddress() );
        diagnoseResult.clear();
        
        RedRatDevice rrDevice = rrManager.getIrDevice(diagnoseIrNetBoxIp);
        RedRatHub hub = RedRatManagerImpl.getRedRatHub(rrDevice);
        if(hub != null){
	        if ( testHubConnection(hub) )
	        {
	            getAllActiveHubConnections(hub);
	            findConnectedRedRats(hub);
	            listDataSets(hub);
	            testConnectionToRedRat(hub);
	        }
        }else{
        	String response = "Hub is null for irnetbox "+diagnoseIrNetBoxIp;
        	diagnoseResult.add(response);
        }

    }

    private boolean testHubConnection(RedRatHub hub)
    {
        diagnoseResult.add( "" );
        diagnoseResult.add( "Testing connection to hub..." );
        diagnoseResult.add( "" );

        String response;
        boolean status = false;
        
        TelnetConnection telnetConnection = new TelnetConnection( hub.getRedratHubHost(), 
        		hub.getRedratHubPort(),
                REDRAT_PROMPT_STRING_1 );
        try
        {
            status = telnetConnection.connect( true );
            if ( status )
            {
                response = "Connection to HUB established. "+hub.getRedratHubHost();
            }
            else
            {
                response = "Could not connect to hub. Please make sure it is up. RedRatHub IP "
                        + hub.getRedratHubHost() + " Port " + hub.getRedratHubPort();
            }
            telnetConnection.disconnect();
        }
        catch ( IOException e )
        {
            response = "Exception while connecting to hub " + e.getMessage();
            status = false;
        }
        diagnoseResult.add( response );
        return status;
    }

    private void getAllActiveHubConnections(RedRatHub hub)
    {
//        diagnoseResult.add( "" );
//        diagnoseResult.add( "Currently active connections..." );
//        diagnoseResult.add( "" );
//        Collection< TelnetConnection > activeConnections = hub.getActiveConnections();
//        if ( activeConnections != null )
//        {
//            int index = 1;
//            for ( TelnetConnection telnetConnection : activeConnections )
//            {
//                diagnoseResult.add( index+" Host " + telnetConnection.getHost() + " : Last active Time "
//                        + telnetConnection.getLastActiveTime() );
//                diagnoseResult.add( "" );
//                index++;
//            }
//        }
    }

    private boolean findConnectedRedRats(RedRatHub hub)
    {
        diagnoseResult.add( "" );
        diagnoseResult.add( "Listing RedRats..." );
        diagnoseResult.add( "" );

        boolean status = false;
        TelnetConnection telnetConnection = new TelnetConnection( hub.getRedratHubHost(), hub.getRedratHubPort(),
                REDRAT_PROMPT_STRING_1 );
        try
        {
            status = telnetConnection.connect( true );
            if ( status )
            {
                diagnoseResult.add( telnetConnection.sendCommand( RedRatCommands.LIST_REDRATS,
                        REDRAT_PROMPT_STRING_2 ) );
            }
            else
            {
                diagnoseResult.add( "Could not connect to hub. Please make sure it is up. RedRatHub IP "
                        + hub.getRedratHubHost() + " Port " + hub.getRedratHubPort() );
            }
            telnetConnection.disconnect();
        }
        catch ( IOException e )
        {
            diagnoseResult.add( "Exception while connecting to hub " + e.getMessage() );
            status = false;
        }
        diagnoseResult.add( "" );
        return status;
    }

    private boolean listDataSets(RedRatHub hub)
    {
        diagnoseResult.add( "" );
        diagnoseResult.add( "Listing Datasets registered in Hub..." );
        diagnoseResult.add( "" );

        boolean status = false;
        TelnetConnection telnetConnection = new TelnetConnection( hub.getRedratHubHost(), hub.getRedratHubPort(),
                REDRAT_PROMPT_STRING_1 );
        try
        {
            status = telnetConnection.connect( true );
            if ( status )
            {
                diagnoseResult.add( telnetConnection.sendCommand( RedRatCommands.LIST_DATASETS,
                        REDRAT_PROMPT_STRING_2 ) );
            }
            else
            {
                diagnoseResult.add( "Could not connect to hub. Please make sure it is up. RedRatHub IP "
                        + hub.getRedratHubHost() + " Port " + hub.getRedratHubPort() );
            }
            telnetConnection.disconnect();
        }
        catch ( IOException e )
        {
            diagnoseResult.add( "Exception while connecting to hub " + e.getMessage() );
            status = false;
        }

        return status;
    }

    private void testConnectionToRedRat(RedRatHub hub)
    {
        if ( diagnoseIrNetBoxIp != null && !diagnoseIrNetBoxIp.isEmpty() && diagnoseIrNetBoxPort != null )
        {
            diagnoseResult.add( "" );
            diagnoseResult.add( "Sending command to redrat " + diagnoseIrNetBoxIp + " ..." );
            diagnoseResult.add( "" );
            TelnetConnection telnetConnection = new TelnetConnection(hub.getRedratHubHost(), hub.getRedratHubPort(),
                    REDRAT_PROMPT_STRING_1 );
            try
            {
                boolean status = telnetConnection.connect( true );
                if ( status )
                {
                    String command = IRNETBOX_IR_COMMAND.replace( IPADDRESS_ARGUMENT, diagnoseIrNetBoxIp )
                            .replace( KEYSET_ARGUMENT, "COMCAST" ).replace( KEY_ARGUMENT, "MUTE" )
                            .replace( PORT_ARGUMENT, diagnoseIrNetBoxPort.toString() );
                    diagnoseResult.add( telnetConnection.sendCommand( command, REDRAT_PROMPT_STRING_1 ) );
                }
                else
                {
                    diagnoseResult.add( "Could not connect to hub. Please make sure it is up. RedRatHub IP "
                            + hub.getRedratHubHost() + " Port " + hub.getRedratHubPort() );
                }
                telnetConnection.disconnect();
            }
            catch ( IOException e )
            {
                diagnoseResult.add( "Exception while connecting to hub " + e.getMessage() );
            }
        }
    }

    public String getIrServiceUrl()
    {
        return irServiceUrl;
    }

    public void setIrServiceUrl( String irServiceUrl )
    {
        this.irServiceUrl = irServiceUrl;
    }

    public String getIrNetBoxIp()
    {
        return irNetBoxIp;
    }

    public void setIrNetBoxIp( String irNetBoxIp )
    {
        this.irNetBoxIp = irNetBoxIp;
    }

    public Integer getIrNetBoxPort()
    {
        return irNetBoxPort;
    }

    public void setIrNetBoxPort( Integer irNetBoxPort )
    {
        this.irNetBoxPort = irNetBoxPort;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getRemoteType()
    {
        return remoteType;
    }

    public void setRemoteType( String remoteType )
    {
        this.remoteType = remoteType;
    }

    public String getRemoteCommand()
    {
        return remoteCommand;
    }

    public void setRemoteCommand( String remoteCommand )
    {
        this.remoteCommand = remoteCommand;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount( Integer count )
    {
        this.count = count;
    }

    public boolean getResult()
    {
        return result;
    }

    public void setResult( boolean result )
    {
        this.result = result;
    }

    public List< String > getDiagnoseResult()
    {
        return diagnoseResult;
    }

    public void setDiagnoseResult( List< String > diagnoseResult )
    {
        this.diagnoseResult = diagnoseResult;
    }

    public Integer getDiagnoseIrNetBoxPort()
    {
        return diagnoseIrNetBoxPort;
    }

    public void setDiagnoseIrNetBoxPort( Integer diagnoseIrNetBoxPort )
    {
        this.diagnoseIrNetBoxPort = diagnoseIrNetBoxPort;
    }

    public String getDiagnoseIrNetBoxIp()
    {
        return diagnoseIrNetBoxIp;
    }

    public void setDiagnoseIrNetBoxIp( String diagnoseIrNetBoxIp )
    {
        this.diagnoseIrNetBoxIp = diagnoseIrNetBoxIp;
    }

}
