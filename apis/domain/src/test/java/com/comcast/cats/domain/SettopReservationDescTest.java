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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettopReservationDescTest
{
    protected final Logger LOGGER     = LoggerFactory.getLogger( SettopReservationDescTest.class );

    String                 deviceHost = "192.168.100.2";
    int                    devicePort = 8003;
    int                    connPort   = 99;

    @Test
    public void testSettopMarshalling() throws URISyntaxException, JAXBException, IOException, ClassNotFoundException
    {
        SettopReservationDesc settop = new SettopReservationDesc();
        settop.setLocation( getLocation() );

        LOGGER.info( settop.toString() );

        //settop.setHardwareConnections( getHardwareConnectionList() );
        settop.setExtraProperties( getExtraProperties() );
        settop.setFirmwareVersion( "12345" );

        //settop.setVideoPath( new URI( "test" ) );
        //Assert.assertEquals( "test", settop.getVideoPath().toString() );

        settop.setRack( getRack() );
        settop.setActiveReservationList( getActiveReservationList() );

        settop.setRFPlant( getPlant() );
        Assert.assertEquals( "controller1", settop.getController().getName() );

        JAXBContext context = JAXBContext.newInstance( SettopReservationDesc.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( settop, System.out );

        ObjectOutputStream objStream = new ObjectOutputStream( System.out );
        settop.writeExternal( objStream );

    }

    private List< Reservation > getActiveReservationList()
    {
        List< Reservation > reservations = new ArrayList< Reservation >();
        reservations.add( getReservation() );
        return reservations;
    }

    private Reservation getReservation()
    {
        Reservation reservation = new Reservation();
        reservation.setId( "1" );
        reservation.setName( "Test Reservation" );
        reservation.setStartDate( new Date() );
        reservation.setEndDate( new Date() );
        reservation.setStatus( "Active" );
        reservation.setReservationOwner( getUser() );
        reservation.setEnvironment( getEnvironment() );
        return reservation;
    }

    private Environment getEnvironment()
    {
        Environment environment = new Environment();
        environment.setId( "2" );
        environment.setName( "Test Env" );
        return environment;
    }

    private User getUser()
    {
        User user = new User();
        user.setId( "1" );
        user.setName( "Test User" );
        user.setActive( true );
        return user;
    }

    private Rack getRack()
    {
        Rack rack = new Rack();
        rack.setId( "1" );
        rack.setName( "CATS dev rack" );
        return rack;
    }

    private Map< String, String > getExtraProperties()
    {
        Map< String, String > extraProperties = new HashMap< String, String >();
        extraProperties.put( "1", "one" );
        extraProperties.put( "2", "two" );
        extraProperties.put( "3", "three" );

        return extraProperties;
    }

    private Location getLocation()
    {
        Location location = new Location();
        location.setZone( "04" );
        location.setRow( "04" );
        location.setRack( "13" );
        location.setLab( "usa" );

        return location;
    }

    private RFPlant getPlant()
    {
        RFPlant plant = new RFPlant();
        plant.setName( "plant1" );
        plant.setController( getController() );
        return plant;
    }

    private Controller getController()
    {
        Controller controller = new Controller();
        controller.setId( "1" );
        controller.setType( "8888" );
        controller.setIpAddress( "192.168.100.21" );
        controller.setName( "controller1" );
        controller.setVersion( "v1.0" );
        return controller;
    }
}
