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
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.convert.Converter;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.extensions.compactnotation.CompactConstructor;

import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.SettopController;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.config.ui.SettopSlotConfigServiceImpl;
import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.config.utility.SlotConnectionRepresenter;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.SettopType;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.local.domain.TraceType;
import com.comcast.cats.service.util.YAMLUtils;

@RunWith(Arquillian.class)
public class SettopControllerTest
{

    @Inject
    SettopController settopController;
    @Inject
    SettopSlotConfigService settopSlotService;
    @Inject
    RackService rackService;
    
    static String yamlFilePath = "/usr/test_tmp/";
    String RACK_1 = "04-04-R09";
    String RACK_2 = "dummyrack";
    String[] rackNames = { RACK_1,RACK_2 };
    int INITIAL_NO_OF_SLOTS = 4;
    String MAC_ID = "12:12:12:12:12:12";
    String NAME = "Settop";
    static List<SettopType> settopTypes;
    
   
    
    Behavior behaviour = new Behavior()
    {
        @Override
        public void broadcast( BehaviorEvent arg0 )
        {
            
        }
    };
    
    static{
        System.setProperty(ConfigServiceConstants.CONFIG_PATH, yamlFilePath);
        settopTypes = new ArrayList< SettopType >();
        SettopType cisco_rng150 = new SettopType();
        cisco_rng150.setName( "Cisco RNG 150" );
        cisco_rng150.setManufacturer( "Cisco" );
        cisco_rng150.setModel( "RNG 150" );
        cisco_rng150.setRemoteType( "COMCAST" );
        cisco_rng150.setTraceType( TraceType.GI_TRACE_ENABLED.getRepresentation() );
        settopTypes.add( cisco_rng150 );
        
        SettopType sa_exp3100HD = new SettopType();
        sa_exp3100HD.setName( "SA Explorer 3100HD" );
        sa_exp3100HD.setManufacturer( "SA" );
        sa_exp3100HD.setModel( "Explorer 3100HD" );
        sa_exp3100HD.setRemoteType( "DTA" );
        sa_exp3100HD.setTraceType( TraceType.NORMAL_TRACE.getRepresentation() );
        settopTypes.add( sa_exp3100HD );
    }

    @Deployment
    public static WebArchive createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

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
    public void setUp(){
        int[] noOfSlots = { INITIAL_NO_OF_SLOTS,INITIAL_NO_OF_SLOTS };
        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create(rackNames, noOfSlots),yamlFilePath+ System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
        rackService.refresh();
        
        
        
        Yaml yaml = new Yaml( new CompactConstructor() );
        try
        {
            FileWriter fw = new FileWriter( yamlFilePath+ System.getProperty( "file.separator" ) + SettopSlotConfigServiceImpl.SETTOP_TYPE_MAPPING_CONFIG );
            yaml.dump( settopTypes, fw );
            fw.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown(){
        try
        {
            File file = new File(yamlFilePath+ System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            
            File file2 = new File(yamlFilePath+ System.getProperty( "file.separator" ) + SettopSlotConfigService.SLOT_MAPPING_CONFIG );
            PrintWriter writer2 = new PrintWriter(file2);
            writer2.print("");
            writer2.close();
            
            File file3 = new File(yamlFilePath+ System.getProperty( "file.separator" ) + SettopSlotConfigService.SETTOP_TYPE_MAPPING_CONFIG );
            PrintWriter writer3 = new PrintWriter(file3);
            writer3.print("");
            writer3.close();
            
            
            rackService.refresh();
            settopController.refresh();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void getSlotConnectionListTest(){
        List<SlotConnectionBean>  slotConnections = settopController.getAllConnectedSlots();
        assertNotNull( slotConnections );
        assertEquals( 0, slotConnections.size() );
        saveASlotConnection();
        assertEquals( 1, settopController.getAllConnectedSlots().size() );
    }
    
    @Test
    public void getListableSlotsTest(){
        List<Slot>  slots = new ArrayList< Slot >();
        settopController.setListableSlots( slots );
        assertEquals( slots, settopController.getListableSlots() );
    }
    
    @Test
    public void rowEditedTest(){
        SlotConnectionBean slotConnection = saveASlotConnection();
        
        Slot slot2 = rackService.findSlotByRack( RACK_1, 2 );
        slotConnection.setSlot( slot2 );
        Behavior behaviour = new Behavior()
        {
            @Override
            public void broadcast( BehaviorEvent arg0 )
            {
                
            }
        };
        RowEditEvent event = new RowEditEvent( new UICommand(), behaviour, slotConnection );
        settopController.rowEdited( event );
        settopSlotService.refresh();
        SlotConnectionBean slotConnection1 = settopSlotService.getSlotConnection( "1" );
        assertEquals( slot2, slotConnection1.getSlot() );
    }
    
    @Test
    public void saveSlotConnectionTest(){
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ID );
        settop.setName( NAME );
        slotConnection.setSettop( settop );
        slotConnection.setRack( rack );
        slotConnection.setSlot(slot );
        settopController.saveSlotConnection( slotConnection );
        
        assertEquals( 1, settopController.getAllConnectedSlots().size() );
    }
    
    
    @Test
    public void saveSlotConnection1Test(){
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ID );
        settop.setName( NAME );
        slotConnection.setRack( rack );
        slotConnection.setSlot(slot );
        settopController.saveSlotConnection( slotConnection,settop );
        
        assertEquals( 1, settopController.getAllConnectedSlots().size() );
    }
    
    
    @Test
    public void deleteSettopAndSlotConnectionsTest(){
        SlotConnectionBean slotConnection = saveASlotConnection();
        
        assertEquals( 1, settopController.getAllConnectedSlots().size() );
        
        settopController.deleteSettopAndSlotConnection();
        assertEquals( 1, settopController.getAllConnectedSlots().size() );
        
        SlotConnectionBean[] connections = {slotConnection};
        settopController.setSelectedSlotConnections( connections );
        assertEquals( connections, settopController.getSelectedSlotConnections() );
        
        settopController.deleteSettopAndSlotConnection();
        assertEquals( 0, settopController.getAllConnectedSlots().size() );
    }
    
    @Test
    public void getAsObjectTest(){
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        String value = slot.getRack().getName() + " -- " + slot.getNumber().toString();
        assertEquals( slot, settopController.getAsObject( null, null, value ));
    }
    
    @Test
    public void getAsStringTest(){
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        String value = slot.getRack().getName() + " -- " + slot.getNumber().toString();
        assertEquals( value, settopController.getAsString( null, null, slot ));
    }
    
    @Test(expected = ValidatorException.class)
    public void validateMacAddressTest(){
        settopController.validateMacAddress( null, null, "" );
    }
    
    @Test
    public void validateMacAddress1Test(){
        Behavior behaviour = new Behavior()
        {
            @Override
            public void broadcast( BehaviorEvent arg0 )
            {
                
            }
        };
        
        SlotConnectionBean slotConnection = saveASlotConnection();
       // settopController.rowSelected(new SelectEvent( new UIColumn(), behaviour, slotConnection ));
        settopController.setSelectedSlotConnection(slotConnection);
        settopController.validateMacAddress( null, null, "12:23:23:23:23:23" );
    }
    
    @Test(expected = ValidatorException.class)
    public void validateMacAddressNegativeTest(){

        SlotConnectionBean slotConnection = saveASlotConnection();
   //     settopController.rowSelected(new SelectEvent( new UIColumn(), behaviour, slotConnection ));
        settopController.setSelectedSlotConnection(slotConnection);
        settopController.validateMacAddress( null, null, "12:23:" );
    }
    
    @Test(expected = ValidatorException.class)
    public void validateMacAddressNegative1Test(){
        saveASlotConnection();
        
        SlotConnectionBean slotConnection2 = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 2 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "2" );
        settop.setName( NAME );
        slotConnection2.setSettop( settop );
        slotConnection2.setRack( rack );
        slotConnection2.setSlot(slot );

       // settopController.rowSelected(new SelectEvent(  new UIColumn(), behaviour, slotConnection2 ));
        settopController.setSelectedSlotConnection(slotConnection2);
        settopController.validateMacAddress( null, null, MAC_ID );
    }
    
    @Test
    public void validateNameTest(){
        settopController.validateName( null, null, "Name" );
    }
    
    @Test
    public void validateName1Test(){
        SlotConnectionBean slotConnection = saveASlotConnection();
        
    //    settopController.rowSelected(new SelectEvent( new UIColumn(), behaviour, slotConnection ));
        settopController.setSelectedSlotConnection(slotConnection);
        settopController.validateName( null, null, "Different Name" );
    }
    
    @Test(expected = ValidatorException.class)
    public void validateNameEmptyTest(){
        settopController.validateName( null, null, "" );
    }
    
    @Test(expected = ValidatorException.class)
    public void validateNameNegativeTest(){
        SlotConnectionBean slotConnection = saveASlotConnection();
       // settopController.rowSelected(new SelectEvent(  new UIColumn(), behaviour, slotConnection ));
        settopController.setSelectedSlotConnection(slotConnection);
        settopController.validateName( null, null, "" );
    }
    
    @Test(expected = ValidatorException.class)
    public void validateNameNegative1Test(){
        saveASlotConnection();
        SlotConnectionBean slotConnection2 = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 2 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "2" );
        slotConnection2.setSettop( settop );
        slotConnection2.setRack( rack );
        slotConnection2.setSlot(slot );
        
     //   settopController.rowSelected(new SelectEvent(  new UIColumn(), behaviour, slotConnection2 ));
        settopController.setSelectedSlotConnection(slotConnection2);
        settopController.validateName( null, null, NAME );
    }
    
    @Test
    public void handleRackSelectTest(){
        settopController.handleRackSelect(new ValueChangeEvent( new UIColumn(), null, rackService.findRack( RACK_1 ) ));
    }
    
    @Test
    public void handleSlotSelectTest(){
        settopController.setRackSelectOneMenu(new HtmlSelectOneMenu());
        settopController.setSlotSelectOneMenu(new HtmlSelectOneMenu());
        assertNotNull( settopController.getRackSelectOneMenu() );
        assertNotNull( settopController.getSlotSelectOneMenu() );
        settopController.handleSlotSelect(new ValueChangeEvent( new UIColumn(), null, rackService.findSlotByRack( RACK_1, 1) ));
    }
    
    @Test
    public void getTraceTypeTest(){
        List<String> traceTypes = settopController.getTraceTypes();
       assertNotNull( traceTypes );
    }
    
    @Test
    public void getSettopTypeTest(){
        List<String> settoptypes = settopController.getSettopTypes();
        assertNotNull( settoptypes );
        assertTrue(settoptypes.contains( "Cisco RNG 150" )); 
    }
    
    @Test
    public void getSettopTypeNegativeTest(){
        File file3 = new File(yamlFilePath+ System.getProperty( "file.separator" ) + SettopSlotConfigService.SETTOP_TYPE_MAPPING_CONFIG );
        PrintWriter writer3;
        try
        {
            writer3 = new PrintWriter(file3);
            writer3.print("");
            writer3.close();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        
        List<String> settoptypes = settopController.getSettopTypes();
        assertNotNull( settoptypes );
    }
    
    @Test
    public void autoPopulateTest(){
        List<String> settoptypes = settopController.getSettopTypes();
        
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ID );
        settop.setName( NAME );
        settop.setComponentType( settoptypes.get( 0 ) );
        settopController.autoPopulate( settop );
        assertEquals( settopSlotService.getSettopTypeByName( settoptypes.get( 0 )).getModel(), settop.getModel() );
        
        settop.setComponentType( null );
        settopController.autoPopulate( settop );
        assertEquals(null, settop.getModel() );
    }
    
    @Test
    public void getTraceTypeKeyTest(){
        assertEquals( SettopController.TRACE_TYPE_KEY, settopController.getTraceTypeKey());
    }

    
    private SlotConnectionBean saveASlotConnection(){
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        SettopDesc settop = new SettopDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ID );
        settop.setName( NAME );
        slotConnection.setSettop( settop );
        slotConnection.setRack( rack );
        slotConnection.setSlot(slot );
        settopSlotService.saveSlotConnection( slotConnection );
        settopSlotService.refresh();
        return settopSlotService.getSlotConnection( "1" );
    }
}
