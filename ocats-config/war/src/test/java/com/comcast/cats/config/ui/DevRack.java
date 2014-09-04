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


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.extensions.compactnotation.CompactConstructor;

import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

public class DevRack
{
    List< HardwareInterface >              irdevices    = new ArrayList< HardwareInterface >();
    List< HardwareInterface >              powerdevices = new ArrayList< HardwareInterface >();
    List< HardwareInterface >              videodevices = new ArrayList< HardwareInterface >();
    List< HardwareInterface >              tracedevices = new ArrayList< HardwareInterface >();

    Map< HardwarePurpose, HardwareInterface > deviceMap    = new HashMap< HardwarePurpose, HardwareInterface >();

    public List< Rack > create( String[] rackName, int[] noOfSlots )
    {
        List< Rack > rackList = new ArrayList< Rack >();

        String[] irIPs = null;
        String[] powerIPs = null;
        String[] videoIPs = null;

        for ( int i = 0; i < rackName.length; i++ )
        {
            irIPs = new String[ noOfSlots[ i ] ];
            powerIPs = new String[ noOfSlots[ i ] ];
            videoIPs = new String[ noOfSlots[ i ] ];
            
            int maxPorts = 6;
                    
            int[] genericMaxPorts = new int[maxPorts];

            
            
            for ( int j = 0; j < (noOfSlots[i]%maxPorts); j++ )
            {
                irIPs[ i ] = i + ".1.1." + j;
                powerIPs[ i ] = i + ".2.2." + j;
                videoIPs[ i ] = i + ".3.3." + j;
                
                genericMaxPorts[j] = maxPorts;
            }

            rackList.addAll( create( rackName[ i ], noOfSlots[ i ], irIPs, powerIPs, videoIPs,genericMaxPorts,genericMaxPorts,genericMaxPorts ) );
        }

        return rackList;
    }

    public List< Rack > create( String rackName, int noOfSlots, String[] irIPs, String[] powerIPs, String[] videoIPs, int[] irMaxPorts, int[] powerMaxPorts, int[] videoMaxPorts )
    {
        List< Rack > rackList = new ArrayList< Rack >();

        List< HardwareInterface > devices = createHardwareDevices( noOfSlots, irIPs, powerIPs, videoIPs,irMaxPorts,powerMaxPorts,videoMaxPorts  );
        Rack rack = new Rack( rackName, createSlots( devices, noOfSlots ), noOfSlots );
        for ( Slot slot : rack.getSlots() )
        {
            slot.setRack( rack );
        }
        rackList.add( rack );

        return rackList;
    }

    public void dumpRacksToFile( List< Rack > rackList, String filePath )
    {
        if ( rackList != null )
        {

            Yaml yaml = new Yaml( new CompactConstructor() );
            try
            {
                FileWriter fw = new FileWriter( filePath );
                yaml.dump( rackList, fw );
                fw.close();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    public List< HardwareInterface > createHardwareDevices( int noOfSlots, String[] irIPs, String[] powerIPs,
            String[] videoIPs, int[] irMaxPorts, int[] powerMaxPorts, int[] videoMaxPorts  )
    {
        List< HardwareInterface > hardwareInterfaces = new ArrayList< HardwareInterface >();

        for ( int i = 0; i < irIPs.length; i++ )
        {
            int maxPort = irMaxPorts[i];
            for(int j=1 ; j<=maxPort ; j++){
                HardwareInterface irdevice = new HardwareInterface( UUID.randomUUID().toString(), "IRDEVICE" + i,
                        HardwarePurpose.IR, "gc100", irIPs[ i ], 0, j );
    
                irdevices.add( irdevice );
            }

        }

        for ( int i = 0; i < videoIPs.length; i++ )
        {
            int maxPort = videoMaxPorts[i];
            for(int j=1 ; j<=maxPort ; j++){
                HardwareInterface videodevice = new HardwareInterface( UUID.randomUUID().toString(), "VIDEODEVICE" + i,
                        HardwarePurpose.VIDEOSERVER, "axis", videoIPs[ i ], 0, j );
                videodevices.add( videodevice );
            }
        }

        for ( int i = 0; i < powerIPs.length; i++ )
        {
            int maxPort = powerMaxPorts[i];
            for(int j=1 ; j<=maxPort ; j++){
                HardwareInterface powerdevice = new HardwareInterface( UUID.randomUUID().toString(), "POWERDEVICE" + i,
                        HardwarePurpose.POWER, "wti1600", powerIPs[ i ], 0, j );
                powerdevices.add( powerdevice );
            }

        }
        return hardwareInterfaces;
    }

    public List< Slot > createSlots( List< HardwareInterface > devices, int noOfSlots)
    {
        List< Slot > slots = new ArrayList< Slot >();

        for ( int i = 1; i <= noOfSlots; i++ )
        {
            Map< HardwarePurpose, HardwareInterface > map1 = new HashMap< HardwarePurpose, HardwareInterface >();

            if ( irdevices.size() > 0 && irdevices.get( 0 ) != null )
            {
                map1.put( HardwarePurpose.IR,  irdevices.remove( 0 ) );
            }

            if ( videodevices.size() > 0 && videodevices.get( 0 ) != null )
            {
                map1.put( HardwarePurpose.VIDEOSERVER,  videodevices.remove( 0 ) );
            }

            if ( powerdevices.size() > 0 && powerdevices.get( 0 ) != null )
            {
                map1.put( HardwarePurpose.POWER,  powerdevices.remove( 0 ) );
            }

            Slot slot = new Slot( i, map1 );
            slots.add( slot );

        }

        for ( Slot slot : slots )
        {
            System.out.println( "slot " + slot.getIrHost() +" "+slot.getNumber());
            System.out.println( "slot " + slot.getPowerHost()+" "+slot.getNumber() );
            System.out.println( "slot " + slot.getVideoHost() +" "+slot.getNumber());
        }

        return slots;
    }

    public static void main( String[] args )
    {
        String rackNames = "CATS Dev Rack";
        int noOfSlots = 16;
        String[] irIps =
            { "192.168.100.11", "192.168.100.12", "192.168.100.13"};
        int[] irMaxPorts = {5,5,6};
        String[] powerIps =
            { "192.168.100.10"};
        int[] powerMaxPorts = {16};
        String[] videoIps =
            { "192.168.160.202", "192.168.160.203", "192.168.160.204", "192.168.160.205" };
        int[] videoMaxPorts = {4,4,4,4};

        DevRack devRack = new DevRack();
        devRack.dumpRacksToFile( devRack.create( rackNames, noOfSlots, irIps, powerIps, videoIps,
                irMaxPorts,powerMaxPorts,videoMaxPorts),
                "E:/CATS_HOME/rackconfig.catsrack_ocats" );
    }

}
