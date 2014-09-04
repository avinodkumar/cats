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
package com.comcast.cats.domain.test;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.util.SimpleListWrapper;

/**
 * Data provider class to support testing.
 * 
 * @author subinsugunan
 * 
 */
public class DataProvider
{
    private PropertyUtil        testProperties;

    public static final String  DUMMY_COUNT           = "10";
    public static final String  EMPTY_STRING          = "";
    public static final String  INVALID_MAC_ID        = "00:00:00:00:00:00";
    public static final String  INVALID_MAC_ID_FORMAT = "1234567890";
    public static final String  TEST_ID               = "1234567890";
    public static final String  INVALID_ID            = "1234567890";
    public static final String  INVALID_NAME          = "1234567890";
    public static final String  INVALID_URL           = "http://invalid.domain.com";
    public static final String  PARAM_MAC_ID          = "mac";
    public static final String  PARAM_NAME            = "name";
    public static final String  PARAM_VALUE           = "value";
    public static final String  STB_PROP_MANUFACTURER = "Manufacturer";
    public static final String  STB_PROP_MODEL        = "Model";

    public static final String  TRUE                  = "true";
    public static final String  FALSE                 = "false";

    public static final String  SERVER_URL            = "http://cats.config.com";    ;

    private static DataProvider dataProvider          = null;

    /**
     * Singleton enforcer.
     */
    public DataProvider()
    {
        testProperties = new PropertyUtil();
    }

    public static DataProvider getInstance()
    {
        if ( null == dataProvider )
        {
            dataProvider = new DataProvider();
        }
        return dataProvider;
    }

    public SettopDesc getSettopDesc()
    {
        return getSettopDesc( testProperties.getMacId() );
    }

    public SettopDesc getSettopDesc( String macId )
    {
        return getSettopDesc(macId,false);
    }
    
    public SettopDesc getSettopDesc( String macId,boolean isShallow )
    {
        SettopDesc settop = new SettopDesc();
		try {
			JAXBContext jc = JAXBContext.newInstance(SettopDesc.class);
			Unmarshaller um = jc.createUnmarshaller();
			if (isShallow == true) {
				settop = (SettopDesc) um.unmarshal(new java.io.FileInputStream(
						"src/test/resources/settop-shallow.xml"));

			}
			if (isShallow == false) {
				settop = (SettopDesc) um.unmarshal(new java.io.FileInputStream(
						"src/test/resources/settop.xml"));

			}

			settop.setHostMacAddress(macId);
		}
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return settop;
    }
    public SettopReservationDesc getSettopResDesc( String macId,boolean isShallow )
    {
        SettopReservationDesc settop = new SettopReservationDesc();
        try {
            JAXBContext jc = JAXBContext.newInstance(SettopReservationDesc.class);
            Unmarshaller um = jc.createUnmarshaller();
            if (isShallow == true) {
                settop =  (  SettopReservationDesc ) um.unmarshal(new java.io.FileInputStream(
                        "src/test/resources/settopRes-shallow.xml"));

            }
            if (isShallow == false) {
                settop = (SettopReservationDesc) um.unmarshal(new java.io.FileInputStream(
                        "src/test/resources/settopRes.xml"));
                

            }

            settop.setHostMacAddress(macId);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return settop;
    }

    @SuppressWarnings( "unchecked" )
    public List< SettopDesc > getSettopDescListFromFile()
    {
        SimpleListWrapper< SettopDesc > simpleListWrapper = null;

        List< SettopDesc > settops = new ArrayList< SettopDesc >();

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, SettopDesc.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< SettopDesc > ) unmarshaller
                    .unmarshal( new java.io.FileInputStream( "src/test/resources/settop-list.xml" ) );
            settops = simpleListWrapper.getList();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return settops;
    }

    public List< SettopDesc > getSettopDescList()
    {
        List< SettopDesc > settops = new ArrayList< SettopDesc >();
        settops.add( getSettopDesc( testProperties.getMacId() ) );
      
        settops.add( getSettopDesc( testProperties.getMacId() ) );
        return settops;
    }
    
    public List< SettopReservationDesc > getSettopResDescList( )
    {
        
        return getSettopResDescList(false) ;
        
    }
    
    public List< SettopReservationDesc > getSettopResDescList(boolean isShallow )
    {
        List< SettopReservationDesc > settops = new ArrayList< SettopReservationDesc >();
        settops.add( getSettopResDesc( testProperties.getMacId(),isShallow) );
      
        settops.add( getSettopResDesc( testProperties.getMacId(),isShallow ) );
        return settops;
    }


    public List< SettopDesc > getSettopDescList(boolean isShallow )
    {
        List< SettopDesc > settops = new ArrayList< SettopDesc >();
        settops.add( getSettopDesc( testProperties.getMacId(),isShallow ) );
      
        settops.add( getSettopDesc( testProperties.getMacId(),isShallow ) );
        return settops;
    }
    
    
    public String[] getManufacturerList()
    {
        return new String[]
            { "Pace", "Motorola" };
    }

    public void setTestProperties( PropertyUtil testProperties )
    {
        this.testProperties = testProperties;
    }

    public Object getUser()
    {
        User user = new User();
        user.setFirstName( "Chad" );
        user.setLastName( "Frederick" );
        return user;
    }

    public String getReservedSettopId()
    {
        return testProperties.getReservedSettopId();
    }

    public Allocation getAllocation( String componentId )
    {
        Allocation allocation = new Allocation();

        try
        {
            JAXBContext jc = JAXBContext.newInstance( Allocation.class );
            Unmarshaller um = jc.createUnmarshaller();
            allocation = ( Allocation ) um
                    .unmarshal( new java.io.FileInputStream( "src/test/resources/allocation.xml" ) );
            if ( null != componentId )
            {
                allocation.getComponent().setId( componentId );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return allocation;
    }

    public List< Allocation > getAllocationList()
    {
        List< Allocation > allocations = new ArrayList< Allocation >();
        allocations.add( getAllocation( null ) );
        return allocations;
    }

    public int getDuration()
    {
        int duration = 30;
        return duration;
    }

    public String getCountAsString()
    {
        String count = "30";
        return count;
    }

    public Server getServer()
    {
        Server server = new Server();
        server.setHost( "cats-dev" );
        return server;
    }

    public List< Server > getServerList()
    {
        List< Server > servers = new ArrayList< Server >();
        servers.add( getServer() );
        return servers;
    }

    public Environment getEnvironment()
    {
        Environment environment = new Environment();
        return environment;
    }
}
