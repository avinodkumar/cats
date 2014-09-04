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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * AllocationPanel holds the checkbox for allocating/releasing all settops in
 * Video Grid panel
 * 
 * @author aswathyann
 * 
 */
public class AllocationAndSelectionPanel extends JPanel
{

    private static final long      serialVersionUID                = 3709021829354227730L;
    private static Logger          logger                          = Logger.getLogger( AllocationAndSelectionPanel.class );
    private static final Dimension ALLOCATION_SELECTION_PANEL_SIZE = new Dimension( 270, 65 );

    private JButton                lockButton                      = new JButton( "Allocate All Settops" );
    private JButton                unlockButton                    = new JButton( "Release All Settops" );

    private JButton                selectButton                    = new JButton( "Select All Settops" );
    private JButton                deselectButton                  = new JButton( "Deselect All Settops" );

    private static final Dimension BUTTON_DIMENSION                = new Dimension( 126, 25 );

    /**
     * Constructor for AllocationPanel
     * 
     */
    public AllocationAndSelectionPanel()
    {
        logger.debug( "Creating AllocationAndSelectionPanel." );

        lockButton.setFocusable( false );
        unlockButton.setFocusable( false );
        selectButton.setFocusable( false );
        deselectButton.setFocusable( false );

        lockButton.setSize( BUTTON_DIMENSION );
        unlockButton.setSize( BUTTON_DIMENSION );
        selectButton.setSize( BUTTON_DIMENSION );
        deselectButton.setSize( BUTTON_DIMENSION );

        setLayout( new GridBagLayout() );

        // add( lockButton, getLockButtonConstraints() );
        // add( unlockButton, getUnlockButtonConstraints() );

        add( selectButton, getSelectButtonConstraints() );

        add( deselectButton, getUnselectButtonConstraints() );

        setName( "allocationAndSelectionPanel" );

        setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );

        setMinimumSize( ALLOCATION_SELECTION_PANEL_SIZE );
    }

    private GridBagConstraints getUnselectButtonConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets( 3, 0, 1, 1 );
        return constraints;
    }

    private GridBagConstraints getSelectButtonConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets( 3, 0, 1, 1 );
        return constraints;
    }

    private GridBagConstraints getLockButtonConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets( 1, 0, 1, 1 );
        return constraints;
    }

    private GridBagConstraints getUnlockButtonConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets( 1, 0, 1, 1 );
        return constraints;
    }

    public JButton getLockButton()
    {
        return lockButton;
    }

    public JButton getUnlockButton()
    {
        return unlockButton;
    }

    public JButton getSelectButton()
    {
        return selectButton;
    }

    public JButton getDeselectButton()
    {
        return deselectButton;
    }

    public void addActionListener( ActionListener listener )
    {
        lockButton.addActionListener( listener );
        unlockButton.addActionListener( listener );
        selectButton.addActionListener( listener );
        deselectButton.addActionListener( listener );
    }

    public void removeActionListener( ActionListener listener )
    {
        lockButton.removeActionListener( listener );
        unlockButton.removeActionListener( listener );
        selectButton.removeActionListener( listener );
        deselectButton.removeActionListener( listener );
    }
}
