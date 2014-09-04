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
package com.comcast.cats.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;

public class SettopDescTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( SettopDescTest.class );

    @Test
    public void loadStoreDefaultSettop() throws JAXBException, URISyntaxException
    {
        SettopDesc sOut = new SettopDesc();
        SettopDesc sOut2 = new SettopDesc();
        sOut.setMake( "make" );
        sOut.setModel( "model" );

        sOut2.setMake( "make2" );
        sOut2.setModel( "model2" );
        sOut.setRemotePath( new URI( "gc100://192.168.100.2:4998/?port=4" ) );

        SettopList settops = new SettopList();
        settops.add( sOut );
        settops.add( sOut2 );

        LOGGER.info( "Settop Output = " + sOut.toString() );

        final File output = new File( "target/testme.xml" );
        final JAXBContext ctx = JAXBContext.newInstance( SettopList.class );
        ctx.createMarshaller().marshal( settops, output );

        final File input = new File( "target/testme.xml" );
        final SettopList sList = ( SettopList ) ctx.createUnmarshaller().unmarshal( input );

        SettopDesc sIn = sList.get( 0 );
        LOGGER.info( "Settop Input = " + sIn.toString() );
        assertEquals( sOut.getMake(), sIn.getMake() );

        /*
         * fileIn = new FileInputStream(file); if (fileIn == null) { LOG.error(
         * "Unable to find the keyList.default file, which contains all the IR codes"
         * ); } final KeyFile kf = (KeyFile)
         * ctx.createUnmarshaller().unmarshal(fileIn);
         */

    }

    @Test
    public void testExtraProperties() throws JAXBException
    {
        final JAXBContext ctx = JAXBContext.newInstance( SettopDesc.class );
        SettopDesc settop = new SettopDesc();
        settop.setMake( "Make" );
        settop.setModel( "Model" );
        Map< String, String > map = new HashMap< String, String >();
        map.put( "prop1", "prop1value" );

        settop.setExtraProperties( map );
        final File output = new File( "target/settopExtraProperties.xml" );
        ctx.createMarshaller().marshal( settop, output );

        final File input = new File( "target/settopExtraProperties.xml" );
        SettopDesc inputSettop = ( SettopDesc ) ctx.createUnmarshaller().unmarshal( input );
        LOGGER.info( "{}", inputSettop );
    }

    @Test
    public void testSettopList() throws JAXBException, URISyntaxException
    {
        final JAXBContext ctx = JAXBContext.newInstance( SettopList.class );
        SettopList sl = createSettopList();

        final File output = new File( "target/settopList.xml" );
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( sl, output );

        final File input = new File( "target/settopList.xml" );
        SettopList settopList = ( SettopList ) ctx.createUnmarshaller().unmarshal( input );
        LOGGER.info( "{}", settopList );
    }

    private SettopList createSettopList() throws URISyntaxException
    {
        SettopList list = new SettopList();
        SettopDesc s = new SettopDesc();
        s.setId( "ID-00:19:A6:6E:B5:CB" );
        s.setHostMacAddress( "00:19:A6:6E:B5:CB" );
        s.setAudioPath( new URI( "chromamxx://192.168.100.2/?port=3" ) );
        s.setRemotePath( new URI( "gc100://192.168.100.2/?port=2" ) );
        s.setTracePath( new URI( "traceserver://192.168.100.2/" ) );
        s.setVideoPath( new URI( "axis://192.168.100.2/?camera=1" ) );
        s.setManufacturer( "Motorola" );
        s.setRemoteType( "XR2" );
        s.setModel( "DCH3200" );
        s.setContent( "Guide" );
        s.setSerialNumber( "" );
        s.setUnitAddress( "000-03342-87134-163" );
        s.setPowerPath( new URI( "wti1600://192.168.100.2:23/?outlet=2" ) );
        list.add( s );

        // Add in Joe's settop info
        SettopDesc s2 = new SettopDesc();
        s2.setId( "ID-00:19:5E:BF:56:A4" );
        s2.setHostMacAddress( "00:19:5E:BF:56:A4" );
        s2.setAudioPath( new URI( "" ) );
        s2.setRemotePath( new URI( "gc100://192.168.100.2/?port=4" ) );
        s2.setTracePath( new URI( "traceserver://192.168.100.2/" ) );
        s2.setVideoPath( new URI( "axis://192.168.100.2/?camera=1" ) );
        s2.setManufacturer( "Motorola" );
        s2.setRemoteType( "XR2" );
        s2.setModel( "DCH6416" );
        s2.setSerialNumber( "" );
        s2.setUnitAddress( "000-02989-92341-115" );
        s2.setPowerPath( new URI( "wti1600://192.168.100.2:23/?outlet=4" ) );
        list.add( s2 );

        // Add in Tahmina's settop info
        SettopDesc s3 = new SettopDesc();
        s3.setId( "ID-00:19:5E:C2:9F:2A" );
        s3.setHostMacAddress( "00:19:5E:C2:9F:2A" );
        s3.setAudioPath( new URI( "" ) );
        s3.setRemotePath( new URI( "gc100://192.168.100.2/?port=1" ) );
        s3.setTracePath( new URI( "traceserver://192.168.100.2/" ) );
        s3.setVideoPath( new URI( "axis://192.168.100.2/?camera=1" ) );
        s3.setManufacturer( "Motorola" );
        s3.setRemoteType( "XR2" );
        s3.setModel( "DCH6416" );
        s3.setSerialNumber( "" );
        s3.setUnitAddress( "000-03347-08592-192" );
        s3.setPowerPath( new URI( "wti1600://192.168.100.2:23/?outlet=1" ) );
        list.add( s3 );

        return list;
    }

    @Test
    public void findExtraProperty()
    {
        SettopDesc settopDesc = new SettopDesc();
        String value = "PropertyValue";
        settopDesc.getExtraProperties().put( "Property Key", value );

        // Test the standard condition where they match.
        assertTrue( settopDesc.findExtraProperty( "Property Key" ).equals( value ) );

        // Add some additional whitespace
        assertTrue( settopDesc.findExtraProperty( "Property     Key     " ).equals( value ) );

        // Test ignoring case.
        assertTrue( settopDesc.findExtraProperty( "propertykey" ).equals( value ) );

        // Negative Test
        assertTrue( settopDesc.findExtraProperty( "propertykeya" ) == null );
    }
}
