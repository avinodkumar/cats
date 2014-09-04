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
package com.comcast.cats.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Helper class to support settings view
 * 
 * @author ssugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class SettingsBean
{
    private String key;
    private String value;

    public SettingsBean()
    {
        super();
    }

    public SettingsBean( String key, String value )
    {
        setKey( key );
        setValue( value );
    }

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

}
