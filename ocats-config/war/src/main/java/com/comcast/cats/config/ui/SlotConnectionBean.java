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

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.local.domain.Rack;
import com.comcast.cats.local.domain.Slot;

/**
 * ManagedBean shows the connections of a slot.
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@ApplicationScoped
public class SlotConnectionBean implements Comparable<SlotConnectionBean>
{

    SettopDesc   settop;
    Slot         slot;
    Rack         rack;

    List< Slot > listableSlots = new ArrayList< Slot >();

    public SettopDesc getSettop()
    {
        return settop;
    }

    public void setSettop( SettopDesc settop )
    {
        this.settop = settop;
    }

    public Slot getSlot()
    {
        return slot;
    }

    public void setSlot( Slot slot )
    {
        this.slot = slot;
    }

    public Rack getRack()
    {
        return rack;
    }

    public void setRack( Rack rack )
    {
        this.rack = rack;
    }

    @Override
    public boolean equals( Object object )
    {
        boolean retVal = false;

        if ( object instanceof SlotConnectionBean )
        {
            retVal = true;
            if ( !( ( ( SlotConnectionBean ) object ).getSettop().getId().equals( this.getSettop().getId() ) ) )
            {
                retVal = false;
            }
        }
        return retVal;
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        String retVal = "Unitialized SlotConnection";
        if ( settop != null )
        {
            retVal = settop.getName();
        }
        if ( rack != null )
        {
            retVal += " rack " + rack.getName();
        }
        if ( slot != null )
        {
            retVal += " Slot " + slot.getNumber();
        }
        return retVal;
    }

    public List< Slot > getListableSlots()
    {
        return listableSlots;
    }

    @Override
    public int compareTo( SlotConnectionBean rhs)
    {
        int compareResult = -1;
        if(rhs != null && getRack() != null){
            compareResult = getRack().compareTo( rhs.getRack() );
            if(compareResult == 0  && getSlot() != null){
                compareResult = getSlot().compareTo( rhs.getSlot() );
            }
        }
        return compareResult;
    }
}
