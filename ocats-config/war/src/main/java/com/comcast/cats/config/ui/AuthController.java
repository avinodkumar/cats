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
package com.comcast.cats.config.ui;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthController
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@RequestScoped
public class AuthController
{

    private static Logger logger = LoggerFactory.getLogger( AuthController.class );

    String  logoutURL;
    private static String hostAddress;
    
    public AuthController()
    {
    }
    
    @PostConstruct
    public void constructLogoutURL(){
        boolean logoutURLCreated = false;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = ( HttpServletRequest ) externalContext.getRequest(); 
        String hostAddress =  request.getHeader( "Host" );
        setHostAddress( hostAddress );
//        if(hostAddress.startsWith( "rhel" )){
//            try{
//                String domain = hostAddress.substring( hostAddress.indexOf( "." ) );
//                String gatewayAddress = "gw"+domain;
//                logoutURL = "http://"+gatewayAddress+"/logout?var=http://"+hostAddress; //custom format to help relogin. var parameter will be used by microtik.
//                logoutURLCreated = true;
//            }catch(Exception e){
//                logger.warn( "Could not construct logout URL. request.getRequestURI() "+request.getRequestURI() );//maybe it does not conform to the std rhel.<domain>
//                logoutURLCreated = false;
//            }
//        }
        
        if(!logoutURLCreated){
            logoutURL = "http://"+hostAddress+request.getRequestURI(); 
        }
        logger.debug("logoutURL "+logoutURL);
    }

     public void logout(){
         ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
         try
        {
             HttpServletRequest request = ( HttpServletRequest ) externalContext.getRequest(); 
             request.getSession().invalidate();
             externalContext.redirect(getLogoutURL());
        }
        catch ( IOException e )
        {
            logger.debug( "logout failed "+e.getMessage() );
        }
     }
     
     public String getLogoutURL(){
         if(logoutURL == null){
             constructLogoutURL();
         }
         return logoutURL;
     }

    public static String getHostAddress()
    {
            return hostAddress;
    }

    public static void setHostAddress( String hostAddress )
    {
        AuthController.hostAddress = hostAddress;
    }
}
