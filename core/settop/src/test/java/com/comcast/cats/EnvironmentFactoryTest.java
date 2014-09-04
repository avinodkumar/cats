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

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.comcast.cats.configuration.ApplicationContextProvider;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.ServiceInstantiationException;

/**
 * Test case for {@link EnvironmentFactory}
 * 
 * @author subinsugunan
 * 
 */
public class EnvironmentFactoryTest extends CatsAbstarctTestCase
{
    public static final String CATS_DEV  = "cats-dev-test-env-id";
    public static final String CATS_STAG = "cats-stag-test-env-id";

    @Inject
    private EnvironmentFactory environmentFactory;

    @Test
    public void testContext()
    {

        assertNotNull( environmentFactory );

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        assertNotNull( context );
        SettopFactoryImpl factoryImpl = context.getBean( SettopFactoryImpl.class );
        assertNotNull( factoryImpl );

    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNull() throws ServiceInstantiationException
    {
        environmentFactory.wireSettop( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNullSettopInfo() throws ServiceInstantiationException
    {
        SettopImpl settop = new SettopImpl();
        environmentFactory.wireSettop( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopNullEnvironmentId() throws ServiceInstantiationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop = new SettopImpl();
        settopDesc.setEnvironmentId( null );
        settop.setSettopInfo( settopDesc );

        environmentFactory.wireSettop( settop );
    }

    @Test( expected = IllegalArgumentException.class )
    public void wireSettopEmptyEnvironmentId() throws ServiceInstantiationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop = new SettopImpl();
        settopDesc.setEnvironmentId( DataProvider.EMPTY_STRING );
        settop.setSettopInfo( settopDesc );

        environmentFactory.wireSettop( settop );
    }

    /**
     * Any 'NEW' wiring against an environment will take around 3 seconds.
     * 
     * @throws ServiceInstantiationException
     */
    @Test
    // ( timeout = 4000 )
    public void wireSettopOneSettopOneEnv() throws ServiceInstantiationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop_dev = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_dev.setSettopInfo( settopDesc );
        environmentFactory.wireSettop( settop_dev );
    }

    @Test
    // ( timeout = 2000 )
    public void wireSettopTwoSettopOneEnv() throws ServiceInstantiationException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        SettopImpl settop_dev = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_dev.setSettopInfo( settopDesc );
        environmentFactory.wireSettop( settop_dev );

        SettopImpl settop_stag = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_stag.setSettopInfo( settopDesc );
        environmentFactory.wireSettop( settop_stag );
    }

    /**
     * Any 'NEW' wiring against an environment will take around 3 seconds.
     * 
     * @throws ServiceInstantiationException
     */
    @Test
    @Ignore
    // ( timeout = 4000 )
    public void wireSettopTwoSettopTwoEnv() throws ServiceInstantiationException, SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        SettopDesc settopDesc = dataProvider.getSettopDesc();

        // This settop will be wire with services in cats-dev
        SettopImpl settop_dev = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_DEV );
        settop_dev.setSettopInfo( settopDesc );
        environmentFactory.wireSettop( settop_dev );

        // This settop will be wire with services in cats-stag
        SettopImpl settop_stag = new SettopImpl();
        settopDesc.setEnvironmentId( CATS_STAG );
        settop_stag.setSettopInfo( settopDesc );
        environmentFactory.wireSettop( settop_stag );

        // Using reflection API , we can verify the WSDL bindings.
        /*
         * Field serviceField =
         * RemoteProviderServiceImpl.class.getDeclaredField( "irService" );
         * serviceField.setAccessible( true );
         * 
         * IRService irService_dev = ( IRService ) serviceField.get(
         * settop_dev.getRemote() ); assertNotNull( irService_dev );
         * LOGGER.info( irService_dev );
         * 
         * IRService irService_stag = ( IRService ) serviceField.get(
         * settop_stag.getRemote() ); assertNotNull( irService_stag );
         * LOGGER.info( irService_stag );
         * 
         * Assert.assertNotSame( irService_dev, irService_stag );
         */
    }

}
