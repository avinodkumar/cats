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
package com.comcast.cats.vision.util;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.comcast.cats.provider.BaseProvider;

/**
 * Holds the Panel and its providers
 * 
 * @author aswathyann
 * 
 */
public class PanelAndProviders
{
    private JPanel                            panel;
    private Map< ProviderType, BaseProvider > providers = new LinkedHashMap< ProviderType, BaseProvider >();

    public PanelAndProviders()
    {
    }

    public PanelAndProviders( JPanel panel, Map< ProviderType, BaseProvider > providers )
    {
        this.panel = panel;
        this.providers = providers;
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public void setPanel( JPanel panel )
    {
        this.panel = panel;
    }

    public Map< ProviderType, BaseProvider > getProviders()
    {
        return providers;
    }

    public void setProviders( Map< ProviderType, BaseProvider > providers )
    {
        this.providers = providers;
    }

    public void setProvider( ProviderType providerType, BaseProvider baseProvider )
    {
        providers.put( providerType, baseProvider );
    }

    public BaseProvider getProvider( ProviderType providerType )
    {
        return providers.get( providerType );
    }
}
