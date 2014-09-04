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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.cats.configuration.OCatsContext;
import com.comcast.cats.domain.Controller;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.RFPlant;
import com.comcast.cats.provider.TraceProvider;
import com.comcast.cats.provider.impl.ImageCompareProviderImpl;
import com.comcast.cats.script.playback.ScriptPlayer;
import com.comcast.cats.script.playback.ScriptPlayerImpl;
import com.comcast.cats.script.playback.exceptions.ScriptPlaybackException;
import com.comcast.cats.test.CatsSettopDataProvider;
import com.comcast.cats.test.CatsTestWithMonitoring;

import comcast.cats.annotation.CatsTestCase;
import comcast.cats.annotation.CatsTestStep;

/**
 * Basic Stability Test Example.
 * @author thusai000
 */
@CatsTestCase( name = "StabilityTest" )
public class CatsStabilityClient extends CatsTestWithMonitoring
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static int iteration  = 0;
    private static final Logger logger = LoggerFactory.getLogger(CatsStabilityClient.class);
    /**
     * This is the env variable in cats.props which has the script location.
     */
    protected static String SCRIPTLOC_ENV_VAR = "cats.script.url";

    static String scriptPath = null;
    
    public CatsStabilityClient()
    {
    }

    static {
        CatsFramework catsFramework = new CatsFramework(new OCatsContext());
        scriptPath = catsFramework.getCatsProperties().getProperty( SCRIPTLOC_ENV_VAR );
        CatsSettopDataProvider.setCatsFramework( catsFramework );
    }
    /**
     * Utilize the DataProvider so that N number of these Test Classes are
     * created based on the number of settops returned from the DataProvider.
     * These "can" be executed in parallel given the configuration parameters
     * for the DataProvider implementation.
     *
     * @param settop
     *            - Settop being injected by the DataProvider obtained from the
     *            CatsFramework.
     */
    @Factory( dataProvider = CatsSettopDataProvider.SETTOP_LIST_PROVIDER, dataProviderClass = CatsSettopDataProvider.class )
    public CatsStabilityClient( Settop settop )
    {
        super( settop );
        logger.info("Read env variable from cats.props. scriptPath = " + scriptPath);

    }

    @CatsTestStep( name = "BasicProfile" )
    @Test(invocationCount=1)
    public void basicProfile()
    {
        logger.debug("Started basic profile " + settop);
        Assert.assertNotNull(settop, "Settop creation failed.\nPlease check the CATS_HOME property in the environment or check " + System.getProperty("cats.home") + "\\cats.props file for following configurations - cats.settop.list, cats.config.url, cats.user.authToken(optional for OCATS).");
        Assert.assertNotNull(scriptPath,"Please provide a valid script path for "+SCRIPTLOC_ENV_VAR+" in cats.props file");

        ScriptPlayer player = new ScriptPlayerImpl( settop );
        logger.debug("Player " + player);
        iteration++;
        logger.info("Started basic profile  " + settop + " on iteration: " + iteration);           
    //    ScriptExecutor scriptExecutor=ScriptExecutor.getInstance();
        //register this script player with the script execution manager so that 
        //pause/resume can be executed from outside.
    //    scriptExecutor.add(player);
        Logger settopLogger = settop.getLogger();
        logger.info("Retrieved the settopLogger " + settopLogger+" for settop:"+settop.getHostMacAddress());
        TraceProvider trace = settop.getTrace();
        if(trace != null)
        {
            try {
                trace.startTrace();
                logger.info("Trace provider started");
                settopLogger.info(" Trace Provider started ");
            } catch (Exception e) {

                logger.error("Start of trace provider failed with exception: " + e.getMessage()+" on :"+settop.getHostMacAddress());
                settopLogger.info(" Start of trace provider failed with exception "+e.getMessage());
            }
        }else{
            logger.info("No trace provider available for :"+settop.getHostMacAddress());
        }
        URL path = null;
        try {
             path = new URL(scriptPath);
             InputStream is = path.openStream();
             settopLogger.info("Started running script: " + path);
             logger.info("Going to start script playback from :"+path +"on "+settop.getHostMacAddress());
             player.playBackScript(is);
             logger.info("Playback completed on "+settop.getHostMacAddress());
             settopLogger.info("Completed running script");
        } catch (ScriptPlaybackException se) {
            logger.error("ScriptPlaybackException in " + path + " ," + se);
        } catch (FileNotFoundException fne) {
            logger.error("FileNotFoundException in " + path + " ," + fne);
        } catch (IOException ie) {
            logger.error("IOException in " + path + " ," + ie);
        }
        finally{
        //    scriptExecutor.remove(player);
        }
    
    }

    @Override
    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose )
    {
        return null;
    }

}