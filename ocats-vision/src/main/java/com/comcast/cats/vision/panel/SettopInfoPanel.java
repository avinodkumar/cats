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
package com.comcast.cats.vision.panel;

import static com.comcast.cats.vision.util.CatsVisionConstants.FIRMWARE;
import static com.comcast.cats.vision.util.CatsVisionConstants.HOST_IP_ADDRESS_LABEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.HOST_MAC_ADDRESS_LABEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.MAKE;
import static com.comcast.cats.vision.util.CatsVisionConstants.MANUFACTURER;
import static com.comcast.cats.vision.util.CatsVisionConstants.MODEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.POWER_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.REMOTE_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.REMOTE_TYPE;
import static com.comcast.cats.vision.util.CatsVisionConstants.SERIAL_NUMBER;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.UNIT_ADDRESS;
import static com.comcast.cats.vision.util.CatsVisionConstants.VIDEO_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.VIDEO_SELECTION_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionUtils.URINullHelper;
import static com.comcast.cats.vision.util.CatsVisionUtils.nullChecker;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.comcast.cats.info.SettopInfo;

/**
 * Simple display panel for handling Settop Information. Using some sort of
 * Bindable bean would make this way easier.
 * 
 * @author cfrede001
 * 
 */
public class SettopInfoPanel extends JPanel
{
    /**
     * 
     */
    private static final long      serialVersionUID         = -941554026534367393L;
    private SettopInfo             settopInfo;

    public static Dimension        DEFAULT_SIZE             = new Dimension( 720, 250 );

    private GridBagConstraints     constraints              = new GridBagConstraints();
    private int                    column;
    private static final Dimension PREFERRED_TEXT_AREA_SIZE = new Dimension( 220, 20 );

    public SettopInfoPanel( SettopInfo settopInfo )
    {
        if ( settopInfo == null )
        {
            throw new IllegalArgumentException( "SettopInfo can't be null" );
        }
        this.settopInfo = settopInfo;
        initComponents();
    }

    private void incrementColumnGroup()
    {
        column += 2;
        constraints.gridy = 1;
    }

    private void addLabelPair( String label, String value )
    {
        JLabel labelText = new JLabel( label );
        JTextField labelValue = new JTextField( value );
        labelValue.setPreferredSize( PREFERRED_TEXT_AREA_SIZE );
        labelValue.setEditable( false );
        constraints.gridx = column;
        constraints.anchor = GridBagConstraints.EAST;
        this.add( labelText, constraints );
        constraints.gridx = constraints.gridx + 1;
        constraints.anchor = GridBagConstraints.WEST;
        this.add( labelValue, constraints );
        constraints.gridy = constraints.gridy + 1;
    }

    private void initComponents()
    {
        this.setSize( DEFAULT_SIZE );
        this.setLayout( new GridBagLayout() );
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets( 2, 5, 0, 0 );

        addLabelPair( HOST_MAC_ADDRESS_LABEL, nullChecker( settopInfo.getHostMacAddress() ) );
        addLabelPair( HOST_IP_ADDRESS_LABEL, nullChecker( settopInfo.getHostIpAddress() ) );
        addLabelPair( MAKE, nullChecker( settopInfo.getMake() ) );
        addLabelPair( MANUFACTURER, nullChecker( settopInfo.getManufacturer() ) );
        addLabelPair( MODEL, nullChecker( settopInfo.getModel() ) );
        addLabelPair( SERIAL_NUMBER, nullChecker( settopInfo.getSerialNumber() ) );
        addLabelPair( UNIT_ADDRESS, nullChecker( settopInfo.getUnitAddress() ) );
        addLabelPair( FIRMWARE, nullChecker( settopInfo.getFirmwareVersion() ) );
        addLabelPair( REMOTE_TYPE, nullChecker( settopInfo.getRemoteType() ) );

        incrementColumnGroup();

        addLabelPair( VIDEO_LOCATOR, URINullHelper( settopInfo.getVideoPath() ) );
        addLabelPair( REMOTE_LOCATOR, URINullHelper( settopInfo.getRemotePath() ) );
        addLabelPair( POWER_LOCATOR, URINullHelper( settopInfo.getPowerPath() ) );
        addLabelPair( TRACE_LOCATOR, URINullHelper( settopInfo.getTracePath() ) );
        addLabelPair( VIDEO_SELECTION_LOCATOR, URINullHelper( settopInfo.getVideoSelectionPath() ) );
    }

}
