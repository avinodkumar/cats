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
package com.comcast.cats.vision.script;

import static com.comcast.cats.vision.script.ScriptConstants.CATS;
import static com.comcast.cats.vision.script.ScriptConstants.CLEAR_BUTTON;
import static com.comcast.cats.vision.script.ScriptConstants.QTP;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG;
import static com.comcast.cats.vision.script.ScriptConstants.PLAY_BACK_BUTTON_NAME;
import static com.comcast.cats.vision.script.ScriptConstants.SAVE_BUTTON;
import static com.comcast.cats.vision.script.ScriptConstants.LOAD_SCRIPT_BUTTON;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.inject.Named;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import com.comcast.cats.vision.components.CATSScriptFilter;
import com.comcast.cats.vision.components.IconButton;
import com.comcast.cats.vision.components.IconToggleButton;

/**
 * The ScriptPanel displays the script to the end user
 * 
 * @author aswathyann
 * 
 */
@Named
public class ScriptPanel extends JPanel
{
    private static final long      serialVersionUID    = 2352810822344476231L;
    private JButton                clearButton;
    private JButton                saveButton;
    private JButton                playBackButton;
    private JButton                loadScriptButton;
    private JComboBox              scriptTypeComboBox;
    private JLabel                 scriptLabel;
    private JScrollPane            scriptScrollPane;
    private JTextArea              scriptTextArea;
    private JToggleButton          scriptRecordButton;
    private JFileChooser           scriptFileSaver;
    private JPanel                 scriptControlPanel;
    JFileChooser                   scriptFileLoader;
    private static final int       DEFAULT_WIDTH       = 5;
    private static final int       DEFAULT_HEIGHT      = 600;
    private static final Dimension DEFAULT_DIM         = new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );
    private static final Dimension MINIMUM_SIZE        = new Dimension( 335, 40 );
    final ImageIcon                RECORD_ICON         = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/record.png" ) );
    final ImageIcon                RECORD_PRESSED_ICON = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/record-pressed.png" ) );
    final ImageIcon                ERASER_ICON         = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/eraser.png" ) );
    final ImageIcon                ERASER_PRESSED_ICON = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/eraser-pressed.png" ) );
    final ImageIcon                SAVE_ICON           = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/save.png" ) );
    final ImageIcon                SAVE_PRESSED_ICON   = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/save-pressed.png" ) );
    final ImageIcon                PLAY_ICON           = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/play.png" ) );
    final ImageIcon                PLAY_PRESSED_ICON   = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/play-pressed.png" ) );
    final ImageIcon                PLAY_DISABLED_ICON  = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/play-disabled.png" ) );
    final ImageIcon                LOAD_ICON           = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/load.png" ) );
    final ImageIcon                LOAD_PRESSED_ICON   = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/load-pressed.png" ) );
    final ImageIcon                LOAD_DISBALED_ICON  = new ImageIcon( ScriptPanel.class
                                                               .getResource( "/images/load-disabled.png" ) );
    private static final Dimension ICON_PREFERRED_SIZE = new Dimension( 24, 24 );

    public ScriptPanel()
    {
        createFileChoosers();
        this.setMinimumSize( DEFAULT_DIM );
        this.setSize( DEFAULT_DIM );

        GridBagConstraints gridBagConstraints;

        scriptScrollPane = new JScrollPane();
        scriptControlPanel = new JPanel();
        scriptTextArea = new JTextArea();
        scriptTypeComboBox = new JComboBox();
        scriptLabel = new JLabel();

        scriptRecordButton = new IconToggleButton( RECORD_ICON );
        clearButton = new IconButton( ERASER_ICON );
        saveButton = new IconButton( SAVE_ICON );
        playBackButton = new IconButton( PLAY_ICON );
        loadScriptButton = new IconButton( LOAD_ICON );

        setName( "ScriptPanel" );
        setLayout( new GridBagLayout() );

        scriptScrollPane.setName( "scriptScrollPane" );

        scriptTextArea.setColumns( 10 );
        scriptTextArea.setRows( 5 );
        scriptTextArea.setName( "scriptTextArea" );
        scriptTextArea.setEditable( false );

        scriptScrollPane.setViewportView( scriptTextArea );
        scriptScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.ipady = 529;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new Insets( 10, 35, 36, 100 );
        add( scriptScrollPane, gridBagConstraints );

        scriptControlPanel.setName( "scriptControlPanel" );
        scriptControlPanel.setBorder( BorderFactory.createEtchedBorder() );
        scriptControlPanel.setLayout( new GridBagLayout() );
        scriptControlPanel.setPreferredSize( MINIMUM_SIZE );
        scriptControlPanel.setMinimumSize( MINIMUM_SIZE );
        scriptControlPanel.setMaximumSize( MINIMUM_SIZE );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        // gridBagConstraints.ipadx = 679;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new Insets( 30, 35, 0, 30 );
        add( scriptControlPanel, gridBagConstraints );

        scriptLabel.setText( "Script Type:" );
        scriptLabel.setName( "scriptLabel" );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        scriptControlPanel.add( scriptLabel, gridBagConstraints );

        scriptTypeComboBox.setModel( new DefaultComboBoxModel( new String[]
            { QTP, TEST_NG, CATS } ) );
        scriptTypeComboBox.setName( "scriptTypeComboBox" );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        scriptControlPanel.add( scriptTypeComboBox, gridBagConstraints );

        scriptRecordButton.setName( "scriptToggleButton" );
        scriptRecordButton.setToolTipText( "Record" );
        scriptRecordButton.setPreferredSize( ICON_PREFERRED_SIZE );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0;
        scriptControlPanel.add( scriptRecordButton, gridBagConstraints );

        playBackButton.setPressedIcon( PLAY_PRESSED_ICON );
        playBackButton.setToolTipText( "Playback" );
        playBackButton.setDisabledIcon( PLAY_DISABLED_ICON );
        playBackButton.setPreferredSize( ICON_PREFERRED_SIZE );
        playBackButton.setName( PLAY_BACK_BUTTON_NAME );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        scriptControlPanel.add( playBackButton, gridBagConstraints );
        /*
         * Disabling play back button for QTP and TestNG
         */
        if ( ( ( String ) ( scriptTypeComboBox.getSelectedItem() ) ) != CATS )
        {
            playBackButton.setEnabled( false );
        }
                
        clearButton.setName( CLEAR_BUTTON );
        clearButton.setToolTipText( "Clear" );
        clearButton.setPressedIcon( ERASER_PRESSED_ICON );
        clearButton.setPreferredSize( ICON_PREFERRED_SIZE );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        scriptControlPanel.add( clearButton, gridBagConstraints );

        saveButton.setPressedIcon( SAVE_PRESSED_ICON );
        saveButton.setToolTipText( "Save Script" );
        saveButton.setName( SAVE_BUTTON );
        saveButton.setPreferredSize( ICON_PREFERRED_SIZE );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;

        scriptControlPanel.add( saveButton, gridBagConstraints );

        loadScriptButton.setToolTipText( "Load Script" );
        loadScriptButton.setPressedIcon( LOAD_PRESSED_ICON );
        loadScriptButton.setDisabledIcon( LOAD_DISBALED_ICON );
        loadScriptButton.setPreferredSize( ICON_PREFERRED_SIZE );
        loadScriptButton.setName( LOAD_SCRIPT_BUTTON );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        scriptControlPanel.add( loadScriptButton, gridBagConstraints );
        if ( ( ( String ) ( scriptTypeComboBox.getSelectedItem() ) ) != CATS )
        {
            loadScriptButton.setEnabled( false );
        }
    }

    /**
     * Get Clear Button
     * 
     * @return instance of JButton
     */
    public JButton getClearButton()
    {
        return clearButton;
    }

    /**
     * Get Save Button
     * 
     * @return instance of JButton
     */
    public JButton getSaveButton()
    {
        return saveButton;
    }

    /**
     * Get ScriptType ComboBox
     * 
     * @return instance of JComboBox
     */
    public JComboBox getScriptTypeComboBox()
    {
        return scriptTypeComboBox;
    }

    /**
     * Get ScriptType LoadButton
     * 
     * @return instance of Load Button
     */
    public JButton getScriptLoadButton()
    {
        return loadScriptButton;
    }

    /**
     * Get ScriptType Play Button
     * 
     * @return instance of Play Button
     */
    public JButton getScriptPlayButton()
    {
        return playBackButton;
    }

    /**
     * Get Script Label
     * 
     * @return instance of JLabel
     */
    public JLabel getScriptLabel()
    {
        return scriptLabel;
    }

    /**
     * Get Script ScrollPane
     * 
     * @return instance of JScrollPane
     */
    public JScrollPane getScriptScrollPane()
    {
        return scriptScrollPane;
    }

    /**
     * Get Script TextArea
     * 
     * @return instance of JTextArea
     */
    public JTextArea getScriptTextArea()
    {
        return scriptTextArea;
    }

    /**
     * Get Script Record Button
     * 
     * @return instance of JToggleButton
     */
    public JToggleButton getScriptRecordButton()
    {
        return scriptRecordButton;
    }

    /*
     * Create JFileChoosers for load and save.
     */
    private void createFileChoosers()
    {
        scriptFileSaver = new JFileChooser();
        scriptFileSaver.setAcceptAllFileFilterUsed( false );

        scriptFileLoader = new JFileChooser()
        {
            private static final long serialVersionUID = -1975049860054501938L;

            public void approveSelection()
            {
                if ( getSelectedFile().exists() )
                {
                    super.approveSelection();
                }
            }
        };
        scriptFileLoader.addChoosableFileFilter( new CATSScriptFilter() );
        scriptFileLoader.setAcceptAllFileFilterUsed( false );
    }

    /**
     * Get Script FileSaver
     * 
     * @return instance of JFileChooser
     */
    public JFileChooser getScriptFileSaver()
    {
        return scriptFileSaver;
    }
}
