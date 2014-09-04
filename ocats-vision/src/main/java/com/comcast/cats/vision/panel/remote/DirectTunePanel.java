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
package com.comcast.cats.vision.panel.remote;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static com.comcast.cats.vision.util.CatsVisionConstants.DIRECT_TUNE_TEXT_FIELD_NAME;

public class DirectTunePanel extends JPanel
{

    private static final long      serialVersionUID = -4528967150282172311L;

    private JTextField             channelNumberTextField;
    private JButton                directTuneButton;

    private static final Integer   BUTTON_WIDTH     = 100;
    private static final Integer   BUTTON_HEIGHT    = 22;
    private static final Font      DEFAULT_FONT     = new Font( "Arial", Font.BOLD, 10 );
    private static final Dimension BUTTON_SIZE      = new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT );
    private static final Insets    MARGIN           = new Insets( 0, 0, 0, 0 );

    public DirectTunePanel()
    {
        initComponents();
    }

    private void initComponents()
    {
        final GridBagLayout gbl = new GridBagLayout();
        setLayout( gbl );
        setName( "DirectTunePanel" );

        channelNumberTextField = new JTextField();
        channelNumberTextField.setFont( DEFAULT_FONT );
        channelNumberTextField.setPreferredSize( BUTTON_SIZE );
        channelNumberTextField.setMargin( MARGIN );
        channelNumberTextField.setName( DIRECT_TUNE_TEXT_FIELD_NAME );

        directTuneButton = new JButton( "Direct Tune" );
        directTuneButton.setFont( DEFAULT_FONT );
        directTuneButton.setPreferredSize( BUTTON_SIZE );
        directTuneButton.setName( "DirectTuneButton" );

        GridBagConstraints textFeildConstraints = new GridBagConstraints();
        textFeildConstraints.gridx = 0;
        textFeildConstraints.gridy = 0;
        textFeildConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        textFeildConstraints.weightx = 0.75;
        textFeildConstraints.insets = new Insets( 0, 20, 0, 10 );
        textFeildConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add( channelNumberTextField, textFeildConstraints );

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 0;
        buttonConstraints.weightx = 0.25;
        this.add( directTuneButton, buttonConstraints );
        this.setVisible( true );
    }

    public JButton getDirectTuneButton()
    {
        return directTuneButton;
    }

    public JTextField getDirectTuneTextField()
    {
        return channelNumberTextField;
    }
}
