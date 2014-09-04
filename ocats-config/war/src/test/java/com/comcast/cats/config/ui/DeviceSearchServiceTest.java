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
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import junit.framework.Assert;

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

import com.comcast.cats.config.service.DeviceSearchServiceImpl;
import com.comcast.cats.config.ui.RackService;
import com.comcast.cats.config.ui.SettopSlotConfigService;
import com.comcast.cats.config.ui.SettopSlotConfigServiceImpl;
import com.comcast.cats.config.ui.SlotConnectionBean;
import com.comcast.cats.config.utility.SlotConnectionRepresenter;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;
import com.comcast.cats.service.DeviceSearchService;
import com.comcast.cats.service.util.AssertUtil;
import com.comcast.cats.service.util.YAMLUtils;


@RunWith(Arquillian.class)
public class DeviceSearchServiceTest
{
    static String yamlFilePath = "/usr/test_tmp/";
    String RACK_1 = "04-04-R09";
    String RACK_2 = "dummyrack";
    String[] rackNames = { RACK_1,RACK_2 };
    int INITIAL_NO_OF_SLOTS = 4;
    String MAC_ID = "12:12:12:12:12:12";
    String NAME = "Settop";
    
    private static final String  INVALID_MAC_ID        = "00:00:00:00:00:00";

    @EJB(mappedName=ConfigServiceConstants.DEVICE_SEARCH_SERVICE_MAPPED_NAME)
    DeviceSearchService deviceSearchService;
    @Inject
    RackService rackService;
    
    @Inject
    SettopSlotConfigService settopSlotService;
    
    @Deployment
    public static WebArchive createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

        return ShrinkWrap.create(WebArchive.class)
            .addClasses(HtmlSelectOneMenu.class, DeviceSearchService.class, DeviceSearchServiceImpl.class, ConfigServiceConstants.class,AssertUtil.class,
                        DomainServiceException.class, DomainNotFoundException.class, SettopNotFoundException.class, YAMLUtils.class, Converter.class, SettopInfo.class, DevRack.class)
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
    public void setup(){
        int[] noOfSlots = { INITIAL_NO_OF_SLOTS,INITIAL_NO_OF_SLOTS };
        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create(rackNames, noOfSlots),yamlFilePath+ System.getProperty( "file.separator" ) + RackService.RACK_CONFIG );
        rackService.refresh();
        
        SlotConnectionBean slotConnection = new SlotConnectionBean();
        Rack rack = rackService.findRack( RACK_1 );
        Slot slot = rackService.findSlotByRack( RACK_1, 1 );
        SettopReservationDesc settop = new SettopReservationDesc();
        settop.setId( "1" );
        settop.setHostMacAddress( MAC_ID );
        settop.setName( NAME );
        slotConnection.setSettop( settop );
        slotConnection.setRack( rack );
        slotConnection.setSlot(slot );
        settopSlotService.saveSlotConnection( slotConnection );
        settopSlotService.refresh();
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
            
            rackService.refresh();
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void findByMacIdTest(){
        try
        {
            assertEquals(MAC_ID, deviceSearchService.findByMacId( MAC_ID ).getHostMacAddress());
        }
        catch ( SettopNotFoundException e )
        {
            Assert.fail();
        }
    }
    
    @Test
    public void findByMacIdNegativeTest(){
        boolean excpetionRecieved = false;
            try
            {
                deviceSearchService.findByMacId( INVALID_MAC_ID );
                Assert.fail();
            }
            catch ( SettopNotFoundException e )
            {
                excpetionRecieved = true;
            }
            
            assertTrue( excpetionRecieved );
    }
    
    
    @Test(expected=Exception.class)
    public void findByMacIdNegative1Test(){
        try
        {
            deviceSearchService.findByMacId( null );
        }
        catch ( SettopNotFoundException e )
        {
           Assert.fail();
        }
    }
    
    @Test
    public void findAllAvailableSettopReservationDescTest(){

       List<SettopReservationDesc> settops = deviceSearchService.findAllAvailableSettopReservationDesc( );
       assertNotNull( settops );
 
    }

}
