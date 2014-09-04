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

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.recording.SettopRecordingController;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

/**
 * RackController
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@SessionScoped
public class RackController implements Converter
{

    @Inject
    RackService             rackService;
    @Inject
    SettopSlotConfigService settopSlotConfigService;
    @Inject
    SettopRecordingController settopRecordingController;


    Rack                    selectedRack;

    private static Logger   logger = LoggerFactory.getLogger( RackController.class );

    public RackController()
    {
    }

    public List< Rack > getRackList()
    {
        return rackService.getAllRacks();
    }

    public int getNoOfEmptySlots( String rackName )
    {
        return settopSlotConfigService.getAllEmptySlots( rackService.findRack( rackName ) ).size();
    }

    public String getSettopName( Slot slot )
    {
        String retVal = "--";
        SlotConnectionBean slotConnection = settopSlotConfigService.getSlotConnection( slot );
        if ( slotConnection != null )
        {
            retVal = slotConnection.getSettop().getName();
        }
        return retVal;
    }

    public void setSelectedRack( Rack selectedRack )
    {
        this.selectedRack = selectedRack;
    }

    public Rack getSelectedRack()
    {
        return selectedRack;
    }

    /**
     * Rack Convertor for selectOneMenu
     */
    @Override
    public Object getAsObject( FacesContext context, UIComponent comp, String value )
    {
        Object object = rackService.findRack( value );
        logger.trace( "value " + value + " object " + object );
        return object;
    }

    /**
     * Rack Convertor for selectOneMenu
     */
    @Override
    public String getAsString( FacesContext context, UIComponent comp, Object object )
    {
        String retVal = "";
        if ( object instanceof Rack )
        {
            Rack rack = ( Rack ) object;
            retVal = rack.getName();
        }
        logger.trace( "object " + object + " value  " + retVal );
        return retVal;
    }

    public void refresh()
    {
        rackService.refresh();
        settopSlotConfigService.refresh();
        settopRecordingController.refreshAll();
    }
}
