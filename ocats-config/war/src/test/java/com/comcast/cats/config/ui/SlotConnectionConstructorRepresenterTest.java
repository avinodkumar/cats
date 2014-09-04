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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.convert.Converter;

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
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.RackServiceImpl;
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
public class SlotConnectionConstructorRepresenterTest
{
    SettopSlotConfigServiceImpl settopSlotConfigService;
    RackServiceImpl             rackService;
    SlotConnectionBean          slotConnection;
    static String               yamlFilePath        = "/usr/test_tmp/";
    String                      RACK_1              = "04-04-R09";
    String                      RACK_2              = "dummyrack";
    String[]                    rackNames           =
                                                        { RACK_1, RACK_2 };
    int                         INITIAL_NO_OF_SLOTS = 4;
    String                      MAC_ADDRESS         = "12:23:34:45:45:45";
    String                      SETTOP_NAME         = "Settop";
    String                      ID                  = "0";

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

        settopSlotConfigService = new SettopSlotConfigServiceImpl();
        settopSlotConfigService.init();
        rackService = new RackServiceImpl();
        rackService.init();

        slotConnection = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ADDRESS );
        settop.setName( SETTOP_NAME );
        slotConnection.setSettop( settop );
        slotConnection.setRack( rack );
        slotConnection.setSlot( slot );

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

            rackService.refresh();
            settopSlotConfigService = null;
            rackService = null;
            slotConnection = null;
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void SlotRepresentationTest()
    {
        Rack rack = rackService.findRack( RACK_1 );
        SlotConnectionRepresenter representer = new SlotConnectionRepresenter();
        Node node = representer.represent( rack );
        System.out.println( "Node :" + node.getTag() + ":" );
        assertEquals( SlotConnectionRepresenter.RACK_TAG, node.getTag().toString() );
        assertEquals( rack.getName(), ( ( ScalarNode ) node ).getValue() );

        Slot slot = rack.getSlots().get( 0 );
        Node node1 = representer.represent( slot );
        System.out.println( "Node :" + node1.getTag() + ":" );
        assertEquals( SlotConnectionRepresenter.SLOT_TAG, node1.getTag().toString() );
        assertEquals( rack.getName() + SlotConnectionRepresenter.SLOT_DELIMITER + slot.getNumber(),
                ( ( ScalarNode ) node1 ).getValue() );
    }

    @Test
    public void SlotConstructorTest()
    {
        settopSlotConfigService.saveSlotConnection( slotConnection );
        settopSlotConfigService.refresh();
        // SlotConnectionConstructor constructor = new
        // SlotConnectionConstructor();
    }
}
