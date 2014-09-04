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
package com.comcast.cats.vision.panel.configuration;

import static com.comcast.cats.vision.panel.configuration.ConfigConstants.ALLOCATED_TABLE_STRINGS;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.AVAILABLE_SETTOPS;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.AVAILABLE_TABLE_STRINGS;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.LAUNCH_VIDEO;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.PROPERTY_VALUE_STRINGS;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.REFRESH_SETTOP_LIST;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.SEARCH_SETTOP;
import static com.comcast.cats.vision.panel.configuration.TableType.ALLOCATED_SETTOPS_TABLE;
import static com.comcast.cats.vision.panel.configuration.TableType.AVAILABLE_SETTOPS_TABLE;
import static com.comcast.cats.vision.panel.configuration.TableType.SETTOP_PROERTIES_TABLE;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.inject.Named;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.comcast.cats.vision.util.CatsVisionConstants;

/**
 * The class ConfigPanel creates a panel that is used in the CATS Vision to show
 * the settops that are allocated and available for a particular user identified
 * by the UserUUID.
 * 
 * @author ajith
 */
@Named
public class ConfigPanel extends JPanel
{

    /**
     * Generated Serial Version ID.
     */
    private static final long      serialVersionUID                = -3751135817022815804L;

    private static final Logger    logger                          = Logger.getLogger( ConfigPanel.class );

    private JScrollPane            availableSettopsScrollPane;
    private JScrollPane            allocatedSettopsScrollPane;
    private JScrollPane            settopPropertiesScrollPane;

    private JTabbedPane            resultsTabbedPane;

    private JTable                 availableSettopsTable;
    private JTable                 allocatedSettopsTable;
    private JTable                 settopPropertiesTable;

    private JButton                launchVideoButton;

    private JCheckBox              launchVideoCheckBox;

    private final ImageIcon        LOADING_ICON                    = new ImageIcon(
                                                                           ConfigPanel.class
                                                                                   .getResource( "/images/loading_animation.gif" ) );

    private ConfigPanelTableModel  availableSettopModel;
    private ConfigPanelTableModel  allocatedSettopModel;
    private ConfigPanelTableModel  propertyValueModel;

    private JButton                searchButton;
    private JButton                refreshButton;

    private JTextField             searchTextField                 = new JTextField( 25 );

    private TableType              focusedTable;

    private JLabel                 availableLoadingLabel;
    private JLabel                 allocatedLoadingLabel;

    private static final Dimension TABLE_DIMENSION                 = new Dimension( 500, 0 );
    private static final Dimension RESULT_PANE_DIMENSION           = new Dimension( 300, 0 );

    private static final Dimension SETTOP_PROPERTY_TABLE_DIMENSION = new Dimension( 250, 200 );
    private static final Dimension ICON_PREFERRED_SIZE             = new Dimension( 100, 100 );

    /**
     * Constructor for ConfigPanel
     */

    public ConfigPanel()
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Creating ConfigPanel." );
        }
        setName( "configPanel" );
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents()
    {

        resultsTabbedPane = new JTabbedPane();
        availableSettopsTable = new JTable();
        allocatedSettopsTable = new JTable();
        settopPropertiesTable = new JTable();
        availableSettopsScrollPane = new JScrollPane( availableSettopsTable );
        allocatedSettopsScrollPane = new JScrollPane( allocatedSettopsTable );
        settopPropertiesScrollPane = new JScrollPane( settopPropertiesTable );

        availableSettopsScrollPane.setName( "availableSettopsScrollPane" );
        allocatedSettopsScrollPane.setName( "allocatedSettopsScrollPane" );
        settopPropertiesScrollPane.setName( "settopPropertiesScrollPane" );

        launchVideoButton = new JButton( LAUNCH_VIDEO );
        launchVideoButton.setName( "launchVideoButton" );

        launchVideoCheckBox = new JCheckBox();
        launchVideoCheckBox.setText( "Add to existing " + CatsVisionConstants.APPLICATION_TITLE + " video(s)" );

        availableLoadingLabel = new JLabel( LOADING_ICON );
        availableLoadingLabel.setName( "availableLoadingAnimationLabel" );
        availableLoadingLabel.setToolTipText( "Loading Available Settops..." );
        availableLoadingLabel.setPreferredSize( ICON_PREFERRED_SIZE );

        allocatedLoadingLabel = new JLabel( LOADING_ICON );
        allocatedLoadingLabel.setName( "allocatedloadingAnimationLabel" );
        allocatedLoadingLabel.setToolTipText( "Loading Allocated Settops..." );
        allocatedLoadingLabel.setPreferredSize( ICON_PREFERRED_SIZE );

        searchTextField.setToolTipText( "Enter keyword to be searched" );
        searchTextField.setName( "searchTextField" );
        searchTextField.setPreferredSize( new Dimension( 120, 20 ) );
        searchTextField.setMaximumSize( new Dimension( 120, 20 ) );
        searchTextField.setSize( new Dimension( 120, 20 ) );
        searchTextField.setAutoscrolls( true );
        searchTextField.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
        searchTextField.setBounds( new Rectangle( new Dimension( 120, 20 ) ) );

        searchButton = new JButton( SEARCH_SETTOP );
        searchButton.setToolTipText( "Enter keyword to be searched" );
        searchButton.setName( "searchButton" );

        refreshButton = new JButton( REFRESH_SETTOP_LIST );
        refreshButton.setToolTipText( "Refresh Table" );
        refreshButton.setName( "Refresh Table" );

        availableSettopsScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        availableSettopsScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        // Creating TableModel for availableSettopsTable
        availableSettopModel = new ConfigPanelTableModel( new Object[][] { }, AVAILABLE_TABLE_STRINGS );
        availableSettopsTable.setModel( availableSettopModel );

        // Creating TableModel for settopPropertiesTable
        propertyValueModel = new ConfigPanelTableModel( new Object[][] { }, PROPERTY_VALUE_STRINGS );
        settopPropertiesTable.setModel( propertyValueModel );

        // Creating TableModel for allocatedSettopsTable
        allocatedSettopModel = new ConfigPanelTableModel( new Object[][] { }, ALLOCATED_TABLE_STRINGS );
        allocatedSettopsTable.setModel( allocatedSettopModel );

        availableSettopsTable.setFillsViewportHeight( true );
        availableSettopsTable.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        availableSettopsTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        availableSettopsTable.getTableHeader().setFont( new Font( "Arial", Font.BOLD, 12 ) );
        availableSettopsTable.getTableHeader().setForeground( new Color( 22, 55, 104 ) );

        autoResizeTable( AVAILABLE_SETTOPS_TABLE );

        // Hiding the first column in the table
        DefaultTableColumnModel colModel = ( DefaultTableColumnModel ) availableSettopsTable.getColumnModel();
        TableColumn col = colModel.getColumn( 0 );
        col.setMinWidth( 0 );
        col.setWidth( 0 );
        col.setMaxWidth( 0 );

        /*
         * Multi select enabled available settops table.
         */
        availableSettopsTable.setRowSelectionAllowed( true );
        availableSettopsTable.setName( "availableSettopsTable" );
        availableSettopsTable.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        availableSettopsScrollPane.setViewportView( availableSettopsTable );

        allocatedSettopsTable.setName( "allocatedSettopsTable" );
        allocatedSettopsTable.setFillsViewportHeight( true );
        allocatedSettopsTable.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        allocatedSettopsTable.getTableHeader().setFont( new Font( "Arial", Font.BOLD, 12 ) );
        allocatedSettopsTable.getTableHeader().setForeground( new Color( 22, 55, 104 ) );

        autoResizeTable( ALLOCATED_SETTOPS_TABLE );

        colModel = ( DefaultTableColumnModel ) allocatedSettopsTable.getColumnModel();
        col = colModel.getColumn( 0 );
        col.setMinWidth( 0 );
        col.setWidth( 0 );
        col.setMaxWidth( 0 );

        allocatedSettopsTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        allocatedSettopsScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        allocatedSettopsScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        allocatedSettopsScrollPane.setViewportView( allocatedSettopsTable );

        resultsTabbedPane.setName( "resultsTabbedPane" );
        resultsTabbedPane.addTab( AVAILABLE_SETTOPS, availableSettopsScrollPane );

        // resultsTabbedPane.addTab( ALLOCATED_SETTOPS,
        // allocatedSettopsScrollPane );
        resultsTabbedPane.setFont( new Font( "Arial", Font.PLAIN, 13 ) );
        resultsTabbedPane.setForeground( new Color( 34, 71, 134 ) );

        settopPropertiesScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        settopPropertiesScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        settopPropertiesScrollPane.setViewportView( settopPropertiesTable );

        settopPropertiesTable.setName( "settopPropertiesTable" );
        settopPropertiesTable.setPreferredSize( SETTOP_PROPERTY_TABLE_DIMENSION );
        settopPropertiesTable.setFillsViewportHeight( true );
        settopPropertiesTable.setForeground( new Color( 34, 71, 134 ) );
        settopPropertiesTable.getTableHeader().setFont( new Font( "Arial", Font.BOLD, 12 ) );
        settopPropertiesTable.getTableHeader().setForeground( new Color( 22, 55, 104 ) );
        settopPropertiesTable.setForeground( new Color( 34, 71, 134 ) );
        settopPropertiesTable.setFont( new Font( "Arial", Font.PLAIN, 12 ) );

        settopPropertiesTable.setEnabled( true );

        JPanel buttonPanel = new JPanel();
        buttonPanel.setName( "buttonPanel" );
        buttonPanel.setLayout( new GridBagLayout() );

        GridBagConstraints searchTextFieldConstraints = new GridBagConstraints();
        searchTextFieldConstraints.gridx = 1;
        searchTextFieldConstraints.gridy = 0;
        searchTextFieldConstraints.weightx = 0;
        searchTextFieldConstraints.weighty = 0.10;
        searchTextFieldConstraints.ipadx = 200;
        searchTextFieldConstraints.insets = new Insets( 0, 10, 0, 0 );
        buttonPanel.add( searchTextField, searchTextFieldConstraints );

        GridBagConstraints searchButtonConstraints = new GridBagConstraints();
        searchButtonConstraints.gridx = 2;
        searchButtonConstraints.gridy = 0;
        searchButtonConstraints.weightx = 0;
        searchButtonConstraints.weighty = 0.10;
        searchButtonConstraints.insets = new Insets( 0, 5, 0, 0 );
        buttonPanel.add( searchButton, searchButtonConstraints );

        GridBagConstraints refreshButtonConstraints = new GridBagConstraints();
        searchButtonConstraints.gridx = 3;
        searchButtonConstraints.gridy = 0;
        searchButtonConstraints.weightx = 0;
        searchButtonConstraints.weighty = 0.10;
        searchButtonConstraints.insets = new Insets( 0, 5, 0, 5 );
        buttonPanel.add( refreshButton, refreshButtonConstraints );

        JPanel tablePanel = new JPanel();
        tablePanel.setName( "tablePanel" );
        tablePanel.setPreferredSize( TABLE_DIMENSION );
        tablePanel.setLayout( new GridBagLayout() );

        GridBagConstraints resultsTabbedPaneConstraints = new GridBagConstraints();
        resultsTabbedPaneConstraints.gridx = 0;
        resultsTabbedPaneConstraints.gridy = 0;
        resultsTabbedPaneConstraints.anchor = GridBagConstraints.NORTH;
        resultsTabbedPaneConstraints.insets = new Insets( 0, 0, 0, 5 );
        resultsTabbedPaneConstraints.fill = GridBagConstraints.BOTH;
        resultsTabbedPaneConstraints.ipady = 900;
        resultsTabbedPaneConstraints.weightx = 0.80;
        resultsTabbedPaneConstraints.weighty = 1;
        tablePanel.add( resultsTabbedPane, resultsTabbedPaneConstraints );

        JPanel propAndLaunchButtonPane = new JPanel();
        propAndLaunchButtonPane.setName( "propAndLaunchButtonPane" );
        propAndLaunchButtonPane.setPreferredSize( RESULT_PANE_DIMENSION );
        propAndLaunchButtonPane.setLayout( new GridBagLayout() );

        GridBagConstraints settopPropertiesConstraints = new GridBagConstraints();
        settopPropertiesConstraints.gridx = 0;
        settopPropertiesConstraints.gridy = 0;
        settopPropertiesConstraints.insets = new Insets( 22, 5, 0, 0 );
        settopPropertiesConstraints.anchor = GridBagConstraints.NORTH;
        settopPropertiesConstraints.fill = GridBagConstraints.HORIZONTAL;
        settopPropertiesConstraints.ipady = 400;
        settopPropertiesConstraints.weightx = 0.25;
        settopPropertiesConstraints.weighty = 0.8;
        // settopPropertiesConstraints.gridwidth = 2;
        propAndLaunchButtonPane.add( settopPropertiesScrollPane, settopPropertiesConstraints );

        GridBagConstraints checkBoxConstraints = new GridBagConstraints();
        checkBoxConstraints.gridx = 0;
        checkBoxConstraints.gridy = 1;
        checkBoxConstraints.weightx = 0.25;
        checkBoxConstraints.weighty = 0.10;
        checkBoxConstraints.insets = new Insets( 22, 5, 0, 0 );
        checkBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        propAndLaunchButtonPane.add( launchVideoCheckBox, checkBoxConstraints );

        GridBagConstraints launchButtonConstraints = new GridBagConstraints();
        launchButtonConstraints.gridx = 0;
        launchButtonConstraints.gridy = 2;
        launchButtonConstraints.weightx = 0.25;
        launchButtonConstraints.weighty = 0.10;
        launchButtonConstraints.ipady = 20;
        launchButtonConstraints.insets = new Insets( 0, 5, 0, 0 );
        launchButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        propAndLaunchButtonPane.add( launchVideoButton, launchButtonConstraints );

        GridBagConstraints resultsTabbedPaneConstraints2 = new GridBagConstraints();
        resultsTabbedPaneConstraints2.gridx = 1;
        resultsTabbedPaneConstraints2.gridy = 0;
        resultsTabbedPaneConstraints2.anchor = GridBagConstraints.WEST;
        resultsTabbedPaneConstraints2.insets = new Insets( 0, 5, 25, 5 );
        resultsTabbedPaneConstraints2.fill = GridBagConstraints.BOTH;
        resultsTabbedPaneConstraints2.ipady = 300;
        resultsTabbedPaneConstraints2.weightx = 0.20;
        resultsTabbedPaneConstraints2.weighty = 1;
        tablePanel.add( propAndLaunchButtonPane, resultsTabbedPaneConstraints2 );

        setLayout( new GridBagLayout() );

        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.anchor = GridBagConstraints.NORTH;
        buttonPanelConstraints.insets = new Insets( 0, 10, 5, 0 );
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.weightx = 1;
        buttonPanelConstraints.weighty = 0.1;
        add( buttonPanel, buttonPanelConstraints );

        GridBagConstraints tablePanelConstraints = new GridBagConstraints();
        tablePanelConstraints.gridx = 0;
        tablePanelConstraints.gridy = 1;
        tablePanelConstraints.anchor = GridBagConstraints.NORTH;
        tablePanelConstraints.fill = GridBagConstraints.BOTH;
        tablePanelConstraints.weightx = 1;
        tablePanelConstraints.weighty = 0.9;
        tablePanelConstraints.insets = new Insets( 0, 10, 5, 0 );
        add( tablePanel, tablePanelConstraints );

    }

    /**
     * Auto resize table based on table type.
     * 
     * @param tableType
     *            table type
     */
    public void autoResizeTable( TableType tableType )
    {
        if ( tableType == ALLOCATED_SETTOPS_TABLE )
        {
            autoResizeTableSize( allocatedSettopsTable, allocatedSettopModel );
        }
        else if ( tableType == AVAILABLE_SETTOPS_TABLE )
        {
            autoResizeTableSize( availableSettopsTable, availableSettopModel );
        }
        else if ( tableType == SETTOP_PROERTIES_TABLE )
        {
            autoResizeTableSize( settopPropertiesTable, propertyValueModel );
        }
    }

    /**
     * Update table data based on table type and table model
     * 
     * @param tableType
     *            table type
     * @param tableModel
     *            contains column class of object.
     */
    public void updateTableData( TableType tableType, ConfigPanelTableModel tableModel )
    {
        if ( tableType == ALLOCATED_SETTOPS_TABLE )
        {
            allocatedSettopModel = tableModel;
            allocatedSettopsTable.repaint();
        }
        else if ( tableType == AVAILABLE_SETTOPS_TABLE )
        {
            availableSettopModel = tableModel;
            availableSettopsTable.repaint();
        }
        else if ( tableType == SETTOP_PROERTIES_TABLE )
        {
            propertyValueModel = tableModel;
            settopPropertiesTable.repaint();
        }
    }

    /**
     * Function resizing the table width according to header width or the data
     * width, which ever is larger.
     * 
     * @param table
     *            JTable
     * @param model
     *            The ConfigPanelTableModel
     */
    public void autoResizeTableSize( final JTable table, final ConfigPanelTableModel model )
    {
        UIDefaults uiDefaults = UIManager.getDefaults();
        int scrollBarWidth = Integer.parseInt( uiDefaults.get( "ScrollBar.width" ).toString() );

        int tableParentWidth = table.getParent().getWidth();

        final int margin = 5;
        int tableWidthWithScrollbar = tableParentWidth - scrollBarWidth;
        int maxTableHeight = 0;
        int initialTableWidth = tableParentWidth;
        int originalTableHeight = 0;

        // if the table is PropertyValue table
        if ( table == settopPropertiesTable )
        {
            originalTableHeight = 200;
        }
        else
        {
            originalTableHeight = table.getParent().getHeight();
        }

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setModel( model );
        table.setPreferredSize( null );

        for ( int i = 0; i < table.getColumnCount(); i++ )
        {
            final int vColIndex = i;
            final DefaultTableColumnModel colModel = ( DefaultTableColumnModel ) table.getColumnModel();
            final TableColumn col = colModel.getColumn( vColIndex );
            int width = 0;

            // Get width of column header
            TableCellRenderer renderer = col.getHeaderRenderer();
            if ( renderer == null )
            {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            java.awt.Component comp = renderer.getTableCellRendererComponent( table, col.getHeaderValue(), false,
                    false, 0, 0 );
            width = comp.getPreferredSize().width;

            // Get maximum width of column data
            for ( int row = 0; row < table.getRowCount(); row++ )
            {
                renderer = table.getCellRenderer( row, vColIndex );
                comp = renderer.getTableCellRendererComponent( table, table.getValueAt( row, vColIndex ), false, false,
                        row, vColIndex );
                width = Math.max( width, comp.getPreferredSize().width );
            }

            // Add margin
            width += 2 * margin;
            // Set the width
            col.setPreferredWidth( width );
        }
        maxTableHeight = table.getRowHeight() * table.getRowCount();

        if ( maxTableHeight > originalTableHeight )
        {
            initialTableWidth = tableWidthWithScrollbar;
        }

        if ( table.getPreferredSize().getWidth() < initialTableWidth )
        {
            table.setPreferredSize( new Dimension( initialTableWidth, maxTableHeight ) );
            table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        }

        ( ( DefaultTableCellRenderer ) table.getTableHeader().getDefaultRenderer() )
                .setHorizontalAlignment( SwingConstants.LEFT );

        table.getTableHeader().setReorderingAllowed( false );
    }

    /**
     * Return tabbed pane containing Allocated and available Settops
     * 
     * @return tabbed pane
     */
    public JTabbedPane getTabbedPane()
    {
        return resultsTabbedPane;
    }

    /**
     * Return available Settops ScrollPane
     * 
     * @return available Settops ScrollPane
     */
    public JScrollPane getAvailableSettopsScrollPane()
    {
        return availableSettopsScrollPane;
    }

    /**
     * Return allocated settops ScrollPane
     * 
     * @return allocated settops ScrollPane
     */
    public JScrollPane getAllocatedSettopsScrollPane()
    {
        return allocatedSettopsScrollPane;
    }

    /**
     * Return allocated settop model
     * 
     * @return allocated settop model
     */
    public ConfigPanelTableModel getAllocatedSettopModel()
    {
        return allocatedSettopModel;
    }

    /**
     * Return Available settop model
     * 
     * @return Available settop model
     */
    public ConfigPanelTableModel getAvailableSettopModel()
    {
        return availableSettopModel;
    }

    /**
     * Return Property value model
     * 
     * @return Property value model
     */
    public ConfigPanelTableModel getPropertyValueModel()
    {
        return propertyValueModel;
    }

    /**
     * Return Available settops table
     * 
     * @return Available settops table
     */
    public JTable getAvailableSettopsTable()
    {
        return availableSettopsTable;
    }

    /**
     * Return Allocated settops table
     * 
     * @return Allocated settops table
     */
    public JTable getAllocatedSettopsTable()
    {
        return allocatedSettopsTable;
    }

    /**
     * Return TabbedPane containing Available and Allocated stbs
     * 
     * @return TabbedPane containing Available and Allocated stbs
     */
    public JTabbedPane getResultsTabbedPane()
    {
        return resultsTabbedPane;
    }

    /**
     * Return LaunchVideo Button
     * 
     * @return LaunchVideo Button
     */
    public JButton getLaunchVideoButton()
    {
        return launchVideoButton;
    }

    /**
     * Return LoadingAnimation JLabel for available tab.
     * 
     * @return LoadingAnimation JLabel
     */
    public JLabel getAvailableLoadingLabel()
    {
        return availableLoadingLabel;
    }

    /**
     * Return LoadingAnimation JLabel for allocated tab.
     * 
     * @return LoadingAnimation JLabel
     */
    public JLabel getAllocatedLoadingLabel()
    {
        return allocatedLoadingLabel;
    }

    /**
     * Return Search Settop Button
     * 
     * @return Search Settop Button
     */
    public JButton getSearchButton()
    {
        return searchButton;
    }

    /**
     * Return the refresh button
     * 
     * @return Search Settop Button
     */
    public JButton getRefeshButton()
    {
        return refreshButton;
    }

    /**
     * Return Search TextArea
     * 
     * @return Search TextArea
     */
    public JTextField getSearchTextField()
    {
        return searchTextField;
    }

    public JCheckBox getLaunchVideoCheckBox()
    {
        return launchVideoCheckBox;
    }

    /**
     * Get FocusedTable
     * 
     * @return focused TableType
     */
    public TableType getFocusedTable()
    {
        return focusedTable;
    }

    /**
     * Set FocusedTable
     * 
     * @param focusedTable
     *            TableType
     */
    public void setFocusedTable( final TableType focusedTable )
    {
        this.focusedTable = focusedTable;
    }

    public void addActionListener( ActionListener listener )
    {
        launchVideoButton.addActionListener( listener );
        searchButton.addActionListener( listener );
        searchTextField.addActionListener( listener );
        refreshButton.addActionListener( listener );
    }

    public void addChangeListener( ChangeListener listener )
    {
        // To enable or disable 'Launch Video' button
        resultsTabbedPane.addChangeListener( listener );
    }

    public void addMouseListener( MouseListener listener )
    {
        availableSettopsTable.addMouseListener( listener );
    }

    public void addListSelectionListener( ListSelectionListener listener )
    {
        availableSettopsTable.getSelectionModel().addListSelectionListener( listener );
        allocatedSettopsTable.getSelectionModel().addListSelectionListener( listener );

    }

    public void setAvailableSorter( RowSorter< ConfigPanelTableModel > sorter )
    {
        availableSettopsTable.setRowSorter( sorter );
    }

    public void setAllocationSorter( RowSorter< ConfigPanelTableModel > sorter )
    {
        allocatedSettopsTable.setRowSorter( sorter );
    }
}