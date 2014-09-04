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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.comcast.cats.vision.util.CatsVisionUtils;
import com.comcast.cats.vision.components.TextFilter;

/**
 * LogPanel is used to log all the activities in CATS Vision.
 * 
 * @author aswathyann
 */
public class LogPanel extends JPanel
{

    private static final long      serialVersionUID = -4084773574566303582L;
    private static Logger          logger           = Logger.getLogger( LogPanel.class );
    private JButton                saveButton;
    private JButton                clearButton;
    private JScrollPane            scrollPane;
    private JTextArea              logTextArea;
    private StringBuilder          logText          = new StringBuilder();
    private JFileChooser           logFileSaver;
    private String                 filePath;
    private static String          TEXT             = ".txt";

    public LogPanel()
    {
        setName( "logPanel" );
        initComponents();
        setVisible( true );
        setBorder( BorderFactory.createTitledBorder( "Log" ) );
        createFileChooser();
    }

    private void initComponents()
    {
        GridBagConstraints gridBagConstraints;

        scrollPane = new JScrollPane();
        logTextArea = new JTextArea();
        saveButton = new JButton();
        saveButton.setName( "saveButton" );
        clearButton = new JButton();
        clearButton.setName( "clearButton" );
        filePath = "";

        saveButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent evt )
            {
                if ( ( logTextArea != null ) && ( !logTextArea.getText().isEmpty() ) )
                {
                    saveLog();
                }
                else
                {
                    CatsVisionUtils.showInfo( "Log is empty", "Please note that log is empty. No information to save." );
                }
            }
        } );

        clearButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent evt )
            {
                if ( ( logTextArea != null ) && ( !logTextArea.getText().isEmpty() ) )
                {
                    clearLog();
                }
            }
        } );

        setLayout( new GridBagLayout() );

        logTextArea.setColumns( 22 );
        logTextArea.setRows( 5 );
        logTextArea.setWrapStyleWord( true );
        logTextArea.setEditable( false );
        scrollPane.setViewportView( logTextArea );

        saveButton.setText( "Save Log" );
        clearButton.setText( "Clear Log" );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets( 5, 0, 5, 0 );
        add( scrollPane, gridBagConstraints );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add( clearButton, gridBagConstraints );

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weightx = 0.35;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add( saveButton, gridBagConstraints );

    }

    public static void main( String[] args )
    {
        JFrame frame = new JFrame();
        frame.add( new LogPanel() );
        frame.setSize( 1000, 1000 );
        frame.setVisible( true );
    }

    /**
     * Log text
     * 
     * @param text
     *            text to be logged
     */
    public void logText( String text )
    {
        logText.append( text );
        logTextArea.append( text );
        logTextArea.setCaretPosition( logTextArea.getDocument().getLength() );
    }

    public void saveLog()
    {
        if ( filePath.isEmpty() )
        {
            int retVal = logFileSaver.showSaveDialog( this );
            if ( retVal == JFileChooser.APPROVE_OPTION )
            {
                filePath = logFileSaver.getSelectedFile().getAbsolutePath();

                if ( !filePath.endsWith( TEXT ) )
                {
                    filePath = filePath + TEXT;
                }
            }
        }
        saveLogFile();
    }

    public void saveLogFile()
    {
        if ( !filePath.isEmpty() )
        {
            BufferedWriter out = null;

            try
            {
                File logFile = new File( filePath );

                if ( !logFile.exists() )
                {

                    if ( !logFile.createNewFile() )
                    {
                        if ( logger.isDebugEnabled() )
                        {
                            logger.debug( "Unable to create file -" + filePath );
                        }
                    }

                }
                out = new BufferedWriter( new FileWriter( logFile, true ) );

                if ( ( out != null ) && ( !logText.toString().isEmpty() ) )
                {
                    out.write( logText.toString() );

                    logText = logText.replace( 0, logText.length(), "" );

                    out.flush();
                }
            }
            catch ( IOException ioException )
            {
                logger.error( "IOException : " + ioException.getMessage() );
            }
            finally
            {
                try
                {
                    if ( out != null )
                    {
                        out.close();
                    }
                }
                catch ( IOException ioException )
                {
                    logger.error( "IOException : " + ioException.getMessage() );
                }

                out = null;
            }
        }
    }

    protected void clearLog()
    {
        if ( ( logTextArea != null ) && ( !logTextArea.getText().isEmpty() ) )
        {

            logTextArea.setText( "" );

        }
        if ( !logText.toString().isEmpty() )
        {
            logText = logText.replace( 0, logText.length(), "" );
        }
    }

    /**
     * Create JFileChoosers for save.
     */
    private void createFileChooser()
    {
        logFileSaver = new JFileChooser();
        logFileSaver.addChoosableFileFilter( new TextFilter() );
        logFileSaver.setAcceptAllFileFilterUsed( false );

    }

    public void setPanelSize( Dimension dimension )
    {
        setPreferredSize( dimension );
        setMinimumSize( dimension );
    }
}
