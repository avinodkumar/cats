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
package com.comcast.cats.domain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author SSugun00c
 * 
 */
public class SettopDescTest
{
    protected final Logger LOGGER              = LoggerFactory.getLogger( SettopDescTest.class );

    String                 dummyDeviceHost     = "192.168.100.2";
    int                    dummyDevicePort     = 8003;
    int                    dummyConnectionPort = 99;

    @Test
    public void testSettopMarshalling() throws URISyntaxException, JAXBException, IOException, ClassNotFoundException
    {
        SettopDesc settop = new SettopDesc();
        settop.setLocation( getLocation() );

        LOGGER.info( settop.toString() );

        List< HardwareInterface > hardwareInterfaces = new ArrayList< HardwareInterface >();

        hardwareInterfaces.add( new HardwareInterface( null, null, HardwarePurpose.IR, "gc100", "192.168.100.2", 0, 3 ) );
        hardwareInterfaces.add( new HardwareInterface( null, null, HardwarePurpose.POWER, "wti100", "192.168.100.22", 23,
                13 ) );
        hardwareInterfaces.add( new HardwareInterface( null, null, HardwarePurpose.VIDEOSERVER, "axis", "192.168.100.3",
                0, 1 ) );

        settop = new SettopDesc( hardwareInterfaces );

        settop.setExtraProperties( getExtraProperties() );
        settop.setId( "6b83d56c-251d-42cd-a56f-c36bdeff94c8" );
        settop.setMake( "Motorola" );
        settop.setModel( "Explorer 2200" );
        settop.setManufacturer( "SA" );
        settop.setHostMacAddress( "11:11:11:11:11:11" );
        settop.setFirmwareVersion( "12345" );
        settop.setName( "Settop.00:19:47:25:AC:A8" );
        settop.setComponentType( "Settop.SA.Explorer 2200" );
        settop.setRackId( "4dc910a7-36a6-4419-b45f-1bfab90e901f" );
        settop.setEnvironmentId( "c8718a04-441f-4c2a-8b31-7937cd70f7e3" );
        settop.setHostMacAddress( "00:19:47:25:AC:A8" );
        settop.setRemoteType( "SA" );

        settop.setRFPlant( getPlant() );
        Assert.assertNotNull( settop.getHardwareInterfaceByType( HardwarePurpose.IR ) );

        JAXBContext context = JAXBContext.newInstance( SettopDesc.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( settop, System.out );

        ObjectOutputStream objStream = new ObjectOutputStream( System.out );
        settop.writeExternal( objStream );

        /*
         * ObjectOutputStream output = new ObjectOutputStream( new
         * FileOutputStream( "settopdesc" ) ); settop.writeExternal( output );
         * output.close();
         * 
         * ObjectInputStream input = new ObjectInputStream( new FileInputStream(
         * "settopdesc" ) ); SettopDesc settop2 = new SettopDesc();
         * 
         * settop2 = ( SettopDesc ) settop2.readExternalObject( input );
         * input.close();
         * 
         * LOGGER.info( settop ); LOGGER.info( settop2 );
         */
    }

    private Map< String, String > getExtraProperties()
    {
        Map< String, String > extraProperties = new HashMap< String, String >();
        extraProperties.put( "Tag", "Settop" );
        extraProperties.put( "Plant", "03" );
        extraProperties.put( "Owner", "Tester" );
        extraProperties.put( "Controller", "Controller" );

        return extraProperties;
    }

    private Location getLocation()
    {
        Location location = new Location();
        location.setZone( "1" );
        location.setRow( "02" );
        location.setRack( "13" );
        location.setLab( "USA" );

        return location;
    }

    private RFPlant getPlant()
    {
        RFPlant plant = new RFPlant();
        plant.setId( UUID.randomUUID().toString() );
        plant.setName( "03" );
        plant.setController( getController() );
        return plant;
    }

    private Controller getController()
    {
        Controller controller = new Controller();
        controller.setId( UUID.randomUUID().toString() );
        controller.setName( "Legacy" );
        return controller;
    }
}
