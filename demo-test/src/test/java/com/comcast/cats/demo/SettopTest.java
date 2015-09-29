package com.comcast.cats.demo;

import org.apache.log4j.Logger;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.comcast.cats.CatsFramework;

import static com.comcast.cats.RemoteCommand.*;

import com.comcast.cats.Settop;
import com.comcast.cats.configuration.OCatsContext;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.test.AbstractSettopTest;
import com.comcast.cats.test.CatsSettopDataProvider;
import com.comcast.cats.testng.SettopProvider;

import comcast.cats.annotation.CatsTestStep;

/**
 * Sample test case to show case usage of {@link SettopProvider} <br>
 * @author skurup00c
 * 
 */
public class SettopTest extends AbstractSettopTest
{


	private static final long serialVersionUID = 3653445642465824645L;
	private static final Logger logger = Logger.getLogger( SettopTest.class );

    static {
        CatsFramework catsFramework = new CatsFramework(new OCatsContext());
        CatsSettopDataProvider.setCatsFramework( catsFramework );
    }
	
	
    public SettopTest()
    {
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
    public SettopTest( Settop settop )
    {
        super( settop );
        System.out.println("settop.getName "+settop.getName());

    }

    @CatsTestStep( name = "settopPressKey" )
    @Test
    public void keyPressTest()
    {
        logger.info("---------------------------------------Test Started----------------------------------------------");
        logger.info("Settop MAC : "+settop.getHostMacAddress());
        logger.info("Settop Name : "+settop.getName());
        
        settop.pressKey(MENU,2000);
        settop.pressKey(RIGHT,2000);
        settop.pressKey(RIGHT,2000);
        settop.pressKey(RIGHT,2000);
        settop.pressKey(SELECT,2000);
        settop.pressKey(EXIT,2000);

        settop.pressKey(GUIDE,5000);
        settop.pressKey(EXIT,2000);
        
        settop.pressKey(INFO,5000);
        settop.pressKey(EXIT,2000);
        
//        try {
//			settop.reboot();
//		} catch (PowerProviderException e) {
//			e.printStackTrace();
//		}
    }
}