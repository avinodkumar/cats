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

import static com.comcast.cats.vision.util.CatsVisionConstants.ALLOCATED_SETTOP;
import static com.comcast.cats.vision.util.CatsVisionConstants.AVAILABLE_SETTOP;
import static com.comcast.cats.vision.util.CatsVisionUtils.getSettopInfoToolTipText;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.vision.components.IconButton;
import com.comcast.cats.vision.panel.video.VideoDisplayPanel;

/**
 * VideoPanel holds the video display panel and checkbox with settop name, which
 * helps in allocating the settop.
 * 
 * @author aswathyann
 * @author modified by bemman01c
 * 
 */
public class VideoPanel extends JPanel
{

    private static final ImageIcon CLOSE_ICON       = new ImageIcon(
    															VideoPanel.class
                                                                    .getResource( "/images/close_videopanel.png" ) );
    private static final ImageIcon STOP_ICON        = new ImageIcon(
    															VideoPanel.class.getResource( "/images/stop.png" ) );
    private static final ImageIcon PLAY_ICON        = new ImageIcon(
																VideoPanel.class.getResource( "/images/play.png" ) );

    private static final ImageIcon LOCK_ICON        = new ImageIcon(
    															VideoPanel.class.getResource( "/images/lock.png" ) );
    private static final ImageIcon UNLOCK_ICON      = new ImageIcon(
    															VideoPanel.class.getResource( "/images/unlock.png" ) );

    private static final long      serialVersionUID = -3637894528580768266L;
    private static Logger          logger           = Logger.getLogger( VideoPanel.class );

    private JCheckBox              selectionCheckBox;

    private JButton                closeButton;

    private JButton                playStopButton;

    private JButton                lockUnlockButton;

    private Settop                 settop;

    private boolean                showErrorMessage = true;

    private VideoDisplayPanel      vdPanel;

    /**
     * Constructor for VideoPanel
     * 
     * @param settop
     *            instance of Settop
     * @param vdPanel
     *            instance of VideoDisplayPanel
     */
    public VideoPanel( Settop settop, VideoDisplayPanel vdPanel )
    {
        logger.debug( "Inside the VideoPanel constructor." );
        this.vdPanel = vdPanel;

        this.settop = settop;

        initComponents();

        setBorder( BorderFactory.createLineBorder( Color.BLUE, 2 ) );

        setVideoPanelLayout();

        setToolTipText( getSettopInfoToolTipText( ( SettopInfo ) settop ) );

        setName( settop.getHostMacAddress() );
    }

    /*
     * Initialise Components
     */
    private void initComponents()
    {
        selectionCheckBox = new JCheckBox();

        selectionCheckBox.setText( settop.getHostMacAddress() );

        selectionCheckBox.setFocusable( false );

        // selectionCheckBox.setEnabled( false );

        selectionCheckBox.setName( "selectionCheckBox" );

        closeButton = new IconButton( CLOSE_ICON );

        closeButton.setToolTipText( "Close Video" );

        playStopButton = new IconButton( STOP_ICON );

        showStopStreamingButton();

        lockUnlockButton = new IconButton( UNLOCK_ICON );

        lockUnlockButton.setSize( new Dimension( 25, 25 ) );

        showUnLockButton();

        closeButton.setBorder( BorderFactory.createRaisedBevelBorder() );

        setToolTipText( getSettopInfoToolTipText( ( SettopInfo ) settop ) );
    }

    public void showStopStreamingButton()
    {
        playStopButton.setIcon( STOP_ICON );
        playStopButton.setToolTipText( "Stop Streaming" );
    }

    public void showStartStreamingButton()
    {
        playStopButton.setIcon( PLAY_ICON );
        playStopButton.setToolTipText( "Start Streaming" );
    }

    public void showLockButton()
    {
        lockUnlockButton.setIcon( LOCK_ICON );
        lockUnlockButton.setToolTipText( ALLOCATED_SETTOP );
    }

    public void showUnLockButton()
    {
        lockUnlockButton.setIcon( UNLOCK_ICON );
        lockUnlockButton.setToolTipText( AVAILABLE_SETTOP );
    }

    /*
     * Set VideoPanel layout
     */
    private void setVideoPanelLayout()
    {
        logger.debug( "Set layout for VideoPanel." );

        setLayout( new GridBagLayout() );

        add( selectionCheckBox, getCheckBoxConstraints() );

        add( vdPanel, getVideoDisplayConstraints() );

        add( playStopButton, getStreamStopButtonConstraints() );

        add( closeButton, getCloseButtonConstraints() );

        // add( lockUnlockButton, getLockUnlockButtonConstraints() );
    }

    /*
     * Get GridBagConstraints for startStopStreaming
     */
    private GridBagConstraints getStreamStopButtonConstraints()
    {
        GridBagConstraints streamStopButtonConstraints = new GridBagConstraints();

        streamStopButtonConstraints.gridx = 0;

        streamStopButtonConstraints.gridy = 0;
        streamStopButtonConstraints.insets = new Insets( 4, 0, 0, 0 );
        streamStopButtonConstraints.weighty = 0.02;
        streamStopButtonConstraints.weightx = 0.01;

        streamStopButtonConstraints.anchor = GridBagConstraints.LINE_END;

        return streamStopButtonConstraints;
    }

    /*
     * Get GridBagConstraints for closeButton
     */
    private GridBagConstraints getLockUnlockButtonConstraints()
    {
        GridBagConstraints lockButtonConstraints = new GridBagConstraints();

        lockButtonConstraints.gridx = 1;

        lockButtonConstraints.gridy = 0;
        lockButtonConstraints.insets = new Insets( 4, 0, 0, 0 );

        lockButtonConstraints.weighty = 0.02;
        lockButtonConstraints.weightx = 0.01;
        lockButtonConstraints.anchor = GridBagConstraints.LINE_START;

        return lockButtonConstraints;
    }

    /*
     * Get GridBagConstraints for CheckBox
     */
    private GridBagConstraints getCheckBoxConstraints()
    {
        GridBagConstraints checkBoxConstraints = new GridBagConstraints();

        checkBoxConstraints.gridx = 2;
        checkBoxConstraints.gridy = 0;

        checkBoxConstraints.weighty = 0.01;
        checkBoxConstraints.weightx = 0.94;

        checkBoxConstraints.anchor = GridBagConstraints.CENTER;

        return checkBoxConstraints;
    }

    /*
     * Get GridBagConstraints for closeButton
     */
    private GridBagConstraints getCloseButtonConstraints()
    {
        GridBagConstraints closeButtonConstraints = new GridBagConstraints();

        closeButtonConstraints.gridx = 3;

        closeButtonConstraints.gridy = 0;
        closeButtonConstraints.weighty = 0.02;
        closeButtonConstraints.weightx = 0.04;
        closeButtonConstraints.anchor = GridBagConstraints.LINE_END;

        return closeButtonConstraints;
    }

    /*
     * Get GridBagConstraints for VideoDisplay panel
     */
    private GridBagConstraints getVideoDisplayConstraints()
    {
        GridBagConstraints videoDisplayConstraints = new GridBagConstraints();

        videoDisplayConstraints.gridx = 0;

        videoDisplayConstraints.gridy = 1;

        videoDisplayConstraints.weighty = 0.99;
        videoDisplayConstraints.weightx = 1;
        videoDisplayConstraints.gridwidth = 4;
        videoDisplayConstraints.fill = GridBagConstraints.BOTH;
        videoDisplayConstraints.anchor = GridBagConstraints.CENTER;

        return videoDisplayConstraints;
    }

    /**
     * Check if settop is allocated
     * 
     * @return true if selected, else false
     */
    public boolean getSelectionStatus()
    {
        return selectionCheckBox.isSelected();
    }

    /**
     * Get SelectionCheckBox
     * 
     * @return selectionCheckBox
     */
    public JCheckBox getSelectionCheckBox()
    {
        return selectionCheckBox;
    }

    public void setEnabledSelectionCheckBox( boolean value )
    {
        selectionCheckBox.setEnabled( value );
    }

    public JButton getCloseButton()
    {
        return closeButton;
    }

    public VideoDisplayPanel getDisplayPanel()
    {
        return vdPanel;
    }

    public Settop getSettop()
    {
        return settop;
    }

    public void setSettop( Settop settop )
    {
        this.settop = settop;
    }

    public boolean isShowErrorMessage()
    {
        return showErrorMessage;
    }

    public void setShowErrorMessage( boolean showErrorMessage )
    {
        this.showErrorMessage = showErrorMessage;
    }

    public JButton getStreamStopButton()
    {
        return playStopButton;
    }

    public JButton getLockUnlockButton()
    {
        return lockUnlockButton;
    }

    public void resize( Dimension dimension )
    {
        this.setMinimumSize( dimension );
        this.setPreferredSize( dimension );
    }

    public void addItemListener( ItemListener listener )
    {
        selectionCheckBox.addItemListener( listener );
    }

    public void removeItemListener( ItemListener listener )
    {
        selectionCheckBox.removeItemListener( listener );
    }

    public void addActionListener( ActionListener listener )
    {
        closeButton.addActionListener( listener );
        playStopButton.addActionListener( listener );
        lockUnlockButton.addActionListener( listener );
    }

    public void removeActionListener( ActionListener listener )
    {
        closeButton.removeActionListener( listener );
        playStopButton.removeActionListener( listener );
        lockUnlockButton.removeActionListener( listener );
    }
}
