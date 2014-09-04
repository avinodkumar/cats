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
package com.comcast.cats.vision.panel.videogrid;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.vision.panel.ConfigurableButtonPanel;
import com.comcast.cats.vision.panel.power.PowerPanel;
import com.comcast.cats.vision.panel.remote.RemoteControlView;

/**
 * VideoControlPanel holds Allocation Panel and Remote Panel
 * 
 * @author aswathyann
 * 
 */
public class VideoControlPanel extends JPanel
{

    private static final long           serialVersionUID = 4891176250372320577L;
    private static final Dimension      PREFERRED_SIZE   = new Dimension( 270, 350 );
    private static Logger               logger           = Logger.getLogger( VideoControlPanel.class );
    private RemoteControlView           remoteControl;
    private AllocationAndSelectionPanel allocationAndSelectionPanel;
    private PowerPanel                  powerPanel;
    private ConfigurableButtonPanel     configurableButtonPanel;

    /**
     * Constructor of VideoControlPanel
     * 
     */

    public VideoControlPanel( RemoteControlView remoteControl, AllocationAndSelectionPanel allocationPanel,
            PowerPanel powerPanel, ConfigurableButtonPanel configurableButtonPanel )
    {
        logger.debug( "Creating VideoControlPanel (panel which holds AllocationPanel and GridRemotePanel)." );

        this.remoteControl = remoteControl;
        this.allocationAndSelectionPanel = allocationPanel;
        this.powerPanel = powerPanel;
        this.configurableButtonPanel = configurableButtonPanel;

        setName( "videoControlPanel" );

        setVideoControlLayout();

        setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );

        setPreferredSize( PREFERRED_SIZE );
        setMinimumSize( PREFERRED_SIZE );

        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        setVisible( true );
    }

    /*
     * Sets Video control panel layout
     */
    private void setVideoControlLayout()
    {
        logger.debug( "Setting Layout for VideoControlPanel" );
        setLayout( new GridBagLayout() );

        add( allocationAndSelectionPanel, getCheckBoxConstraints() );

        add( powerPanel, getPowerConstraints() );

        add( remoteControl, getRemoteConstraints() );

        add( configurableButtonPanel, getGridConfigurableButtonConstraints() );

    }

    /*
     * Get GridBagConstraints for check box panel
     */
    private GridBagConstraints getCheckBoxConstraints()
    {
        GridBagConstraints checkBoxConstraints = new GridBagConstraints();
        checkBoxConstraints.gridx = 0;
        checkBoxConstraints.gridy = 0;
        checkBoxConstraints.fill = GridBagConstraints.BOTH;
        checkBoxConstraints.anchor = GridBagConstraints.PAGE_START;
        checkBoxConstraints.weighty = 0.1;
        checkBoxConstraints.insets = new Insets( 1, 1, 1, 1 );
        return checkBoxConstraints;
    }

    /*
     * Get GridBagConstraints for Remote panel
     */
    private GridBagConstraints getRemoteConstraints()
    {
        GridBagConstraints remoteConstraints = new GridBagConstraints();
        remoteConstraints.gridx = 0;
        remoteConstraints.gridy = 2;
        remoteConstraints.weightx = 1;
        remoteConstraints.fill = GridBagConstraints.BOTH;
        remoteConstraints.anchor = GridBagConstraints.PAGE_START;
        remoteConstraints.weighty = 0.6;
        remoteConstraints.insets = new Insets( 1, 1, 1, 1 );
        return remoteConstraints;
    }

    /*
     * Get GridBagConstraints for power panel
     */
    private GridBagConstraints getPowerConstraints()
    {
        GridBagConstraints powerConstraints = new GridBagConstraints();
        powerConstraints.gridx = 0;
        powerConstraints.gridy = 1;
        powerConstraints.weightx = 1;
        powerConstraints.fill = GridBagConstraints.BOTH;
        powerConstraints.anchor = GridBagConstraints.PAGE_START;
        powerConstraints.weighty = 0.1;
        powerConstraints.insets = new Insets( 1, 1, 1, 1 );
        return powerConstraints;
    }

    private Object getGridConfigurableButtonConstraints()
    {
        GridBagConstraints gridConfigurableButtonConstraints = new GridBagConstraints();
        gridConfigurableButtonConstraints.gridx = 0;
        gridConfigurableButtonConstraints.gridy = 3;
        gridConfigurableButtonConstraints.weightx = 1;
        gridConfigurableButtonConstraints.anchor = GridBagConstraints.PAGE_START;
        gridConfigurableButtonConstraints.weighty = 1;
        gridConfigurableButtonConstraints.fill = GridBagConstraints.BOTH;
        gridConfigurableButtonConstraints.insets = new Insets( 1, 1, 1, 1 );
        return gridConfigurableButtonConstraints;
    }

    /**
     * Get AllocationAndSelectionPanel.
     * 
     * @return allocationAndSelectionPanel
     */
    public AllocationAndSelectionPanel getAllocationAndSelectionPanel()
    {
        return allocationAndSelectionPanel;
    }

    /**
     * Get Remote Panel.
     * 
     * @return remotePanel
     */
    /*
     * public GridRemotePanel getRemotePanel() { return remotePanel; }
     */
}
