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
package com.comcast.cats.vision.components;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Frame to show the error details
 * 
 * @author aswathyann
 * 
 */
public class ErrorConsole implements ListSelectionListener
{

    private JFrame                   mainFrame;

    private JTextArea                headingTextArea;

    private JPanel                   errorTablePanel;
    private JScrollPane              errorTableScrollPane;
    private DefaultTableModel        errorTableModel;
    private JTable                   errorTable;

    private JPanel                   detailsTextAreaPanel;
    private JScrollPane              detailsTextAreaScrollPane;
    private JTextArea                detailsTextArea;

    private JPanel                   buttonPanel;
    private JButton                  okButton;
    private JToggleButton            detailsButton;

    private Map< String, Exception > settopExceptionsMap = new LinkedHashMap< String, Exception >();

    private String[]                 ERROR_TABLE_STRINGS =
                                                             {

                                                             "MAC Address", "Error Message" };

    private int[]                    indexRowsSeleted    =
                                                             { -1 };

    /**
     * Constructor for ShowErrorDetails
     * 
     * @param title
     *            title of the frame
     * @param settopExceptionsMap
     *            map which holds the key(settop name) and value(exception
     *            object).
     */
    public ErrorConsole( String title, Map< String, Exception > settopExceptionsMap )
    {
        this.settopExceptionsMap = settopExceptionsMap;
        if ( !settopExceptionsMap.isEmpty() )
        {
            initComponents( title );
            getMainFrame().setVisible( true );
        }
    }

    @SuppressWarnings( "serial" )
    private void initComponents( String title )
    {

        mainFrame = new JFrame();

        headingTextArea = new JTextArea();

        errorTablePanel = new JPanel();
        errorTableScrollPane = new JScrollPane();
        errorTable = new JTable();

        detailsTextAreaPanel = new JPanel();
        detailsTextAreaScrollPane = new JScrollPane();
        detailsTextArea = new JTextArea();

        buttonPanel = new JPanel();
        okButton = new JButton();
        detailsButton = new JToggleButton();

        headingTextArea.setColumns( 20 );
        headingTextArea.setOpaque( false );
        headingTextArea.setEditable( false );

        headingTextArea.setText( "Problems encountered while performing the action.\n" );

        errorTableModel = new DefaultTableModel( new Object[][] { }, ERROR_TABLE_STRINGS )
        {
            @Override
            public boolean isCellEditable( int row, int column )
            {
                return false;
            }
        };

        errorTable.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        ListSelectionModel errorTableListSelectionModel = errorTable.getSelectionModel();

        /*
         * Add ListSelectionListener for errorTableListSelectionModel
         */
        errorTableListSelectionModel.addListSelectionListener( this );

        setTablePanelLayout();

        fillErrorTable( settopExceptionsMap );

        /*
         * Setting constant width for 'Mac Address' column
         */
        DefaultTableColumnModel colModel = ( DefaultTableColumnModel ) errorTable.getColumnModel();
        TableColumn col = colModel.getColumn( 0 );
        col.setMinWidth( 120 );
        col.setWidth( 120 );
        col.setMaxWidth( 120 );

        errorTablePanel.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED ) );

        errorTableScrollPane.setSize( new Dimension( errorTable.getWidth(), errorTable.getRowCount()
                * errorTable.getRowHeight() ) );

        errorTableScrollPane.setViewportView( errorTable );

        errorTableScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        errorTableScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        errorTableScrollPane.setBorder( BorderFactory.createLineBorder( new java.awt.Color( 0, 0, 0 ) ) );

        detailsTextAreaPanel.setBorder( BorderFactory.createTitledBorder( "Error Details" ) );

        detailsTextArea.setColumns( 20 );

        detailsTextArea.setRows( 5 );

        detailsTextArea.setWrapStyleWord( true );

        detailsTextArea.setEditable( false );

        detailsTextAreaScrollPane.setViewportView( detailsTextArea );

        detailsTextAreaScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        detailsTextAreaScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        setDetailsTextAreaPanelLayout();

        okButton.setText( "OK" );

        okButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent actionEvent )
            {
                mainFrame.dispose();
            }
        } );

        detailsButton.setText( "Details >>" );

        detailsButton.addMouseListener( new MouseAdapter()
        {

            public void mouseClicked( MouseEvent evt )
            {

                detailsToggleButtonMouseClicked( evt );
            };
        } );
        setButtonPanelLayout();

        mainFrame.setTitle( title );
        mainFrame.setBounds( new Rectangle( 600, 300, 450, 330 ) );
        mainFrame.addWindowStateListener( new WindowStateListener()
        {

            @Override
            public void windowStateChanged( WindowEvent windowEvent )
            {

                if ( windowEvent.getNewState() == MAXIMIZED_BOTH )
                {}
                if ( windowEvent.getNewState() == NORMAL )
                {}
            }
        } );
        setDefaultLayout();
    }

    /*
     * Get main frame
     */
    private JFrame getMainFrame()
    {
        return mainFrame;
    }

    /*
     * Handles the mouse clicked event on details button
     */
    private void detailsToggleButtonMouseClicked( MouseEvent evt )
    {

        if ( detailsButton.isSelected() )
        {

            detailsButton.setText( "Details <<" );

            setDetailsLayout();
        }
        else if ( !detailsButton.isSelected() )
        {

            detailsButton.setText( "Details >>" );

            mainFrame.getContentPane().remove( detailsTextAreaPanel );

            setDefaultLayout();
        }
        mainFrame.pack();

    }

    /*
     * Populate ErrorTable
     */
    private void fillErrorTable( Map< String, Exception > exceptionsMap )
    {

        errorTableModel.getDataVector().removeAllElements();

        if ( exceptionsMap != null && !exceptionsMap.isEmpty() )
        {

            for ( String macID : exceptionsMap.keySet() )
            {

                final Vector< Object > tableRow = new Vector< Object >();

                tableRow.add( macID );

                tableRow.add( exceptionsMap.get( macID ).getMessage() );

                errorTableModel.addRow( tableRow );
            }
        }
        errorTable.setModel( errorTableModel );

        errorTable.repaint();
    }

    @Override
    public void valueChanged( ListSelectionEvent listSelectionEvent )
    {

        if ( listSelectionEvent.getValueIsAdjusting() )
        {

            return;
        }

        final ListSelectionModel listSelectionMode = ( ListSelectionModel ) listSelectionEvent.getSource();

        if ( !( listSelectionMode.isSelectionEmpty() ) )
        {

            indexRowsSeleted = errorTable.getSelectedRows();

            List< String > macIDList = new ArrayList< String >();

            for ( int i : indexRowsSeleted )
            {

                macIDList.add( errorTable.getValueAt( i, 0 ).toString() );
            }

            fillTextArea( macIDList );
        }
    }

    /*
     * Populate text area
     */
    private void fillTextArea( List< String > macIDList )
    {

        StringBuilder errorText = new StringBuilder();

        int i = 0;

        boolean isTrue = ( macIDList.size() > 1 ) ? true : false;

        for ( String macID : macIDList )
        {
            if ( isTrue )
            {

                errorText.append( ++i + ") " );
            }

            String errorMessage = getStackTrace( settopExceptionsMap.get( macID ) );

            if ( errorMessage != null )
            {
                errorText.append( errorMessage );
            }
            else
            {
                errorText.append( settopExceptionsMap.get( macID ) );
            }
            errorText.append( "\n\n" );
        }
        detailsTextArea.setText( errorText.toString() );
    }

    /*
     * Get StackTrace for the exception
     */
    private String getStackTrace( Exception exception )
    {

        StringBuilder stackTraceMessage = new StringBuilder();

        stackTraceMessage.append( "Caused by: " + exception );

        if ( exception.getStackTrace() != null )
        {
            StackTraceElement[] stackTraces = exception.getStackTrace();

            if ( stackTraces.length != 0 )
            {

                for ( StackTraceElement stackTrace : stackTraces )
                {

                    stackTraceMessage.append( "\n      at " + stackTrace.toString() );
                }
            }
        }
        return stackTraceMessage.toString();
    }

    /*
     * Layout for detailsTextAreaPanel
     */
    private void setDetailsTextAreaPanelLayout()
    {

        GroupLayout detailsTextAreaPanelLayout = new GroupLayout( detailsTextAreaPanel );

        detailsTextAreaPanel.setLayout( detailsTextAreaPanelLayout );

        detailsTextAreaPanelLayout.setHorizontalGroup( detailsTextAreaPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addComponent( detailsTextAreaScrollPane,
                GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE ) );

        detailsTextAreaPanelLayout.setVerticalGroup( detailsTextAreaPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addComponent( detailsTextAreaScrollPane, GroupLayout.PREFERRED_SIZE,
                152, GroupLayout.PREFERRED_SIZE ) );
    }

    /*
     * Layout for errorTablePanel
     */
    private void setTablePanelLayout()
    {

        GroupLayout errorTablePanelLayout = new GroupLayout( errorTablePanel );

        errorTablePanel.setLayout( errorTablePanelLayout );

        errorTablePanelLayout.setHorizontalGroup( errorTablePanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addGroup(
                errorTablePanelLayout.createSequentialGroup().addContainerGap().addGroup(
                        errorTablePanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING ).addComponent(
                                headingTextArea ).addComponent( errorTableScrollPane, GroupLayout.DEFAULT_SIZE, 388,
                                Short.MAX_VALUE ) ).addContainerGap() ) );

        errorTablePanelLayout.setVerticalGroup( errorTablePanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING ).addGroup(
                errorTablePanelLayout.createSequentialGroup().addContainerGap().addComponent( headingTextArea )
                        .addComponent( errorTableScrollPane, GroupLayout.PREFERRED_SIZE, 116,
                                GroupLayout.PREFERRED_SIZE ).addContainerGap( 24, Short.MAX_VALUE ) ) );
    }

    /*
     * Default layout for the frame
     */
    private void setDefaultLayout()
    {

        mainFrame.setPreferredSize( new Dimension( 450, 330 ) );

        GroupLayout mainFrameLayout = new GroupLayout( mainFrame.getContentPane() );

        mainFrame.getContentPane().setLayout( mainFrameLayout );

        mainFrameLayout.setHorizontalGroup( mainFrameLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING ).addGroup(
                mainFrameLayout.createSequentialGroup().addContainerGap().addGroup(
                        mainFrameLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING ).addComponent(
                                buttonPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ).addComponent( errorTablePanel,
                                GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE ) ).addContainerGap() ) );

        mainFrameLayout.setVerticalGroup( mainFrameLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING ).addGroup(
                mainFrameLayout.createSequentialGroup().addContainerGap().addComponent( errorTablePanel,
                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE ).addGap( 18,
                        18, 18 ).addComponent( buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE ).addGap( 23, 23, 23 ) ) );
    }

    /*
     * Layout after pressing 'Details' button
     */
    private void setDetailsLayout()
    {

        mainFrame.setPreferredSize( new Dimension( 450, 480 ) );

        GroupLayout mainFrameLayout = new GroupLayout( mainFrame.getContentPane() );

        mainFrame.getContentPane().setLayout( mainFrameLayout );

        mainFrameLayout.setHorizontalGroup( mainFrameLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        mainFrameLayout.createSequentialGroup().addContainerGap().addGroup(
                                mainFrameLayout.createParallelGroup( GroupLayout.Alignment.LEADING ).addComponent(
                                        detailsTextAreaPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE ).addComponent( errorTablePanel, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ).addComponent( buttonPanel,
                                        GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) ).addContainerGap() ) );

        mainFrameLayout.setVerticalGroup( mainFrameLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        mainFrameLayout.createSequentialGroup().addContainerGap().addComponent( errorTablePanel,
                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE )
                                .addPreferredGap( LayoutStyle.ComponentPlacement.RELATED ).addComponent(
                                        detailsTextAreaPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE ).addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED ).addComponent( buttonPanel,
                                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE ).addContainerGap() ) );
    }

    /*
     * Layout for buttonPanel
     */
    private void setButtonPanelLayout()
    {

        GroupLayout buttonPanelLayout = new GroupLayout( buttonPanel );

        buttonPanel.setLayout( buttonPanelLayout );

        buttonPanelLayout.setHorizontalGroup( buttonPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        buttonPanelLayout.createSequentialGroup().addContainerGap( 248, Short.MAX_VALUE ).addComponent(
                                okButton ).addPreferredGap( LayoutStyle.ComponentPlacement.UNRELATED ).addComponent(
                                detailsButton ).addGap( 20, 20, 20 ) ) );

        buttonPanelLayout.setVerticalGroup( buttonPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        buttonPanelLayout.createSequentialGroup().addContainerGap( GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE ).addGroup(
                                buttonPanelLayout.createParallelGroup( GroupLayout.Alignment.BASELINE ).addComponent(
                                        okButton ).addComponent( detailsButton ) ).addContainerGap() ) );
    }

    public static void main( String args[] )
    {

        Map< String, Exception > settopExceptionsMap = new LinkedHashMap< String, Exception >();
        settopExceptionsMap
                .put(
                        "00:19:5E:BF:56:A4",
                        new Exception(
                                "Error - AA:B:C:DD...........test...........test...........test...........test...........test...........test" ) );
        settopExceptionsMap
                .put(
                        "00:19:5E:BF:56:A3",
                        new Exception(
                                "Error - YA:X:C:DD...........test...........test...........test...........test...........test...........test" ) );

        settopExceptionsMap
                .put(
                        "AA:B:C:DD1",
                        new Exception(
                                "Error - AA:B:C:DD...........test...........test...........test...........test...........test...........test" ) );
        settopExceptionsMap
                .put(
                        "YA:X:C:DD2",
                        new Exception(
                                "Error - YA:X:C:DD...........test...........test...........test...........test...........test...........test" ) );

        settopExceptionsMap
                .put(
                        "AA:B:C:DD3",
                        new Exception(
                                "Error - AA:B:C:DD...........test...........test...........test...........test...........test...........test" ) );
        settopExceptionsMap
                .put(
                        "YA:X:C:DD4",
                        new Exception(
                                "Error - YA:X:C:DD...........test...........test...........test...........test...........test...........test" ) );

        settopExceptionsMap
                .put(
                        "AA:B:C:DD5",
                        new Exception(
                                "Error - AA:B:C:DD...........test...........test...........test...........test...........test...........test" ) );
        settopExceptionsMap
                .put(
                        "YA:X:C:DD6",
                        new Exception(
                                "Error - YA:X:C:DD...........test...........test...........test...........test...........test...........test" ) );

        new ErrorConsole( "Lock Error", settopExceptionsMap );
    }
}
