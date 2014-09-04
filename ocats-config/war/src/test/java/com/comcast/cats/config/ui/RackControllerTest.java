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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.comcast.cats.config.ui.RackController;
import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.config.ui.SettopSlotConfigServiceImpl;
import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.config.utility.SlotConnectionRepresenter;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.service.util.YAMLUtils;

@RunWith( Arquillian.class )
public class RackControllerTest
{
    @Inject
    RackController          rackController;
    @Inject
    SettopSlotConfigService settopSlotConfigService;
    @Inject
    RackService             rackService;

    static String           yamlFilePath        = "/usr/test_tmp/";
    String                  RACK_1              = "04-04-R09";
    String                  RACK_2              = "dummyrack";
    String[]                rackNames           =
                                                    { RACK_1, RACK_2 };
    int                     INITIAL_NO_OF_SLOTS = 4;

    static
    {
        System.setProperty( ConfigServiceConstants.CONFIG_PATH, yamlFilePath );
    }

    @Deployment
    public static WebArchive createDeployment()
    {
        MavenDependencyResolver resolver = DependencyResolvers.use( MavenDependencyResolver.class )
                .loadMetadataFromPom( "pom.xml" );

        return ShrinkWrap.create(WebArchive.class)
                .addClasses( HtmlSelectOneMenu.class, YAMLUtils.class, Converter.class, SettopInfo.class, DevRack.class)
                .addAsLibraries(resolver.artifact("org.yaml:snakeyaml:1.10").resolveAsFiles())
                .addAsLibraries(resolver.artifact("commons-lang:commons-lang:2.3").resolveAsFiles())
                .addAsLibraries(resolver.artifact("log4j:log4j:1.2.14").resolveAsFiles())
                .addAsLibraries(resolver.artifact("org.jboss.spec.javax.servlet:jboss-servlet-api_3.0_spec:1.0.0.Final").resolveAsFiles())
                .addAsLibraries(resolver.artifact("org.primefaces:primefaces:3.4.1").resolveAsFiles())
                .addAsLibraries(resolver.artifact("com.comcast.cats:ir-common:3.2.0.5").resolveAsFiles())
         /*     .addAsLibraries(resolver.artifact("org.jboss.spec.javax.faces:jboss-jsf-api_2.0_spec:1.0.0.Final").resolveAsFiles())*/
                .addPackage( SlotConnectionRepresenter.class.getPackage() )
                .addPackage( SettopSlotConfigServiceImpl.class.getPackage() )
                .addPackage( Slot.class.getPackage() )
                .addAsResource(new File("src/test/resources", "rackconfig.catsrack"))
                .addAsResource(new File("src/test/resources", "settops.catsrack"))
                .addAsResource(new File("src/test/resources", "settopTypes.catsrack"))
                .addPackage( SettopDesc.class.getPackage() )
                .addAsWebInfResource( new File("src/main/webapp/WEB-INF/beans.xml"))
                .addAsWebInfResource( new File("src/main/webapp/WEB-INF/faces-config.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @Before
    public void setUp()
    {
        int[] noOfSlots =
            { INITIAL_NO_OF_SLOTS, INITIAL_NO_OF_SLOTS };
        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create( rackNames, noOfSlots ),
                yamlFilePath + System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
        rackController.refresh();
    }

    @After
    public void tearDown()
    {
        try
        {
            File file = new File( yamlFilePath + System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
            PrintWriter writer;
            writer = new PrintWriter( file );
            writer.print( "" );
            writer.close();

            File file2 = new File( yamlFilePath + System.getProperty( "file.separator" )
                    + SettopSlotConfigService.SLOT_MAPPING_CONFIG );
            PrintWriter writer2 = new PrintWriter( file2 );
            writer2.print( "" );
            writer2.close();

            rackController.refresh();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void getRackListTest()
    {
        List< Rack > racks = rackController.getRackList();
        assertNotNull( racks );
        assertEquals( rackNames.length, racks.size() );
    }

    @Test
    public void getNoOfEmptySlotsTest()
    {
        rackController.refresh();
        assertEquals( INITIAL_NO_OF_SLOTS, rackController.getNoOfEmptySlots( RACK_1 ) );

        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackController.getRackList().get( 0 );
        Slot slot = rackService.findSlotByRack( rack.getName(), 1 );
        SettopDesc settop = new SettopDesc();
        slotConnection.setSettop( settop );
        settop.setId( "1" );
        slotConnection.setRack( rack );
        slotConnection.setSlot( slot );
        settopSlotConfigService.saveSlotConnection( slotConnection );

        assertEquals( INITIAL_NO_OF_SLOTS - 1, rackController.getNoOfEmptySlots( rack.getName() ) );
    }

    @Test
    public void getSettopTest()
    {
        rackController.refresh();
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackController.getRackList().get( 0 );
        Slot slot = rackService.findSlotByRack( rack.getName(), 1 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "2" );
        settop.setName( "Settop" );
        slotConnection.setSettop( settop );
        slotConnection.setRack( rack );
        slotConnection.setSlot( slot );

        settopSlotConfigService.saveSlotConnection( slotConnection );

        assertEquals( settop.getName(), rackController.getSettopName( slot ) );
    }

    @Test
    public void getSelectedRackTest()
    {
        rackController.refresh();
        Rack rack = rackController.getRackList().get( 0 );
        rackController.setSelectedRack( rack );

        assertEquals( rack, rackController.getSelectedRack() );
    }

    @Test
    public void getAsObjectTest()
    {
        rackController.refresh();
        Rack rack1 = rackService.findRack( RACK_1 );
        Rack rack = ( Rack ) rackController.getAsObject( null, null, RACK_1 );

        assertEquals( rack1, rack );
    }

    @Test
    public void getAsStringTest()
    {
        rackController.refresh();
        Rack rack1 = rackService.findRack( RACK_1 );
        String rackName = rackController.getAsString( null, null, rack1 );

        assertEquals( RACK_1, rackName );
    }
}
