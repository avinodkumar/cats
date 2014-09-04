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

import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.comcast.cats.domain.SettopReservationDesc;

/**
 * The ConfigPanelTableModel class is a sub class of DefaultTableModel. Created
 * for making the table non editable.
 * 
 * @author aswathyann
 * 
 */
public class ConfigPanelTableModel extends DefaultTableModel
{

    private static final long serialVersionUID = -2462098407298633672L;

    /**
     * Constructs a default <code>ConfigPanelTableModel</code> which is a table
     * of zero columns and zero rows.
     */
    public ConfigPanelTableModel()
    {
        super();
    }

    /**
     * Constructs a <code>ConfigPanelTableModel</code> with
     * <code>rowCount</code> and <code>columnCount</code> of <code>null</code>
     * object values.
     * 
     * @param rowCount
     *            the number of rows the table holds
     * @param columnCount
     *            the number of columns the table holds
     * 
     * 
     */
    public ConfigPanelTableModel( int rowCount, int columnCount )
    {
        super( rowCount, columnCount );
    }

    /**
     * Constructs a <code>ConfigPanelTableModel</code> with as many columns as
     * there are elements in <code>columnNames</code> and <code>rowCount</code>
     * of <code>null</code> object values. Each column's name will be taken from
     * the <code>columnNames</code> vector.
     * 
     * @param columnNames
     *            <code>vector</code> containing the names of the new columns;
     *            if this is <code>null</code> then the model has no columns
     * @param rowCount
     *            the number of rows the table holds
     * 
     */
    public ConfigPanelTableModel( Vector< Object > columnNames, int rowCount )
    {
        super( columnNames, rowCount );
    }

    /**
     * Constructs a <code>ConfigPanelTableModel</code> with as many columns as
     * there are elements in <code>columnNames</code> and <code>rowCount</code>
     * of <code>null</code> object values. Each column's name will be taken from
     * the <code>columnNames</code> array.
     * 
     * @param columnNames
     *            <code>array</code> containing the names of the new columns; if
     *            this is <code>null</code> then the model has no columns
     * @param rowCount
     *            the number of rows the table holds
     * 
     */
    public ConfigPanelTableModel( Object[] columnNames, int rowCount )
    {
        super( columnNames, rowCount );
    }

    /**
     * Constructs a <code>ConfigPanelTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code> to the
     * <code>setDataVector</code> method.
     * 
     * @param data
     *            the data of the table, a <code>Vector</code> of
     *            <code>Vector</code>s of <code>Object</code> values
     * @param columnNames
     *            <code>vector</code> containing the names of the new columns
     */
    public ConfigPanelTableModel( Vector< Object > data, Vector< Object > columnNames )
    {
        super( data, columnNames );
    }

    /**
     * Constructs a <code>ConfigPanelTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code> to the
     * <code>setDataVector</code> method. The first index in the
     * <code>Object[][]</code> array is the row index and the second is the
     * column index.
     * 
     * @param data
     *            the data of the table
     * @param columnNames
     *            the names of the columns
     */
    public ConfigPanelTableModel( Object[][] data, Object[] columnNames )
    {
        super( data, columnNames );
    }

    @Override
    public boolean isCellEditable( int row, int column )
    {
        // Making the cells non editable
        return false;
    }

    /**
     * Populate AllocatedSettop data in Vector
     * 
     * @param settopReservationDesc
     *            SettopReservationDesc
     * @return Vector of SettopReservationDesc
     */
    private Vector< Object > populateAllocatedSettopData( SettopReservationDesc settopReservationDesc )
    {
        final Vector< Object > settopData = new Vector< Object >();
        settopData.add( settopReservationDesc.getId() );
        settopData.add( settopReservationDesc.getName() );
        settopData.add( settopReservationDesc.getHostMacAddress() );
        settopData.add( settopReservationDesc.getHostIp4Address() );

        settopData.add( settopReservationDesc.getContent() );
        settopData.add( settopReservationDesc.getModel() );
        return settopData;

    }

    /**
     * Populate AvailableSettop data in Vector
     * 
     * @param settopReservationDesc
     *            SettopReservationDesc
     * @return Vector of SettopReservationDesc
     */
    private Vector< Object > populateAvailableSettopData( SettopReservationDesc settopReservationDesc )
    {
        final Vector< Object > settopData = new Vector< Object >();
        settopData.add( settopReservationDesc.getId() );
        settopData.add( settopReservationDesc.getName() );
        settopData.add( settopReservationDesc.getHostMacAddress() );
        settopData.add( settopReservationDesc.getHostIp4Address() );
        settopData.add( settopReservationDesc.getContent() );
        settopData.add( settopReservationDesc.getModel() );
        return settopData;

    }

    /**
     * Add a row to the table
     * 
     * @param settopReservationDesc
     *            instance of SettopReservationDesc
     * @param tableType
     *            Table Type(ALLOCATED_SETTOPS_TABLE, AVAILABLE_SETTOPS_TABLE)
     */
    public void addRow( SettopReservationDesc settopReservationDesc, TableType tableType )
    {
        if ( tableType == TableType.ALLOCATED_SETTOPS_TABLE )
        {
            addRow( populateAllocatedSettopData( settopReservationDesc ) );
        }
        else if ( tableType == TableType.AVAILABLE_SETTOPS_TABLE )
        {
            addRow( populateAvailableSettopData( settopReservationDesc ) );
        }
    }
}