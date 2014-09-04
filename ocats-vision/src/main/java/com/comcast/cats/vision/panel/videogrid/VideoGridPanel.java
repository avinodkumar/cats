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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;

/**
 * VideoGridPanel holds VideoPanels
 * 
 * @author aswathyann
 */
public class VideoGridPanel extends JPanel
{

    private static final long      serialVersionUID = -3896028340591131806L;
    private static Logger          logger           = Logger.getLogger( VideoGridPanel.class );
    private int                    rows;
    private int                    columns;
    private GridDataModel          model;

    /**
     * Constructor for VideoGridPanel
     * 
     * @param model
     *            GridDataModel
     * @param rows
     *            numbers of rows in VideoGridPanel
     * @param columns
     *            numbers of columns in VideoGridPanel
     */
    public VideoGridPanel( GridDataModel model, int rows, int columns )
    {
        logger.debug( "Creating VideoGridPanel (panel which holds VideoPanel(s))." );

        setName( "videoGridPanel" );
        this.rows = rows;
        this.columns = columns;
        this.model = model;
        arrangePanels( model.getVideoPanels() );
        setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );
    }

    /*
     * Algorithm to arrange panel
     */
    public void arrangePanels( List< VideoPanel > videoPanels )
    {
        if ( ( videoPanels != null ) && ( !videoPanels.isEmpty() ) )
        {
            int i = 0;

            setLayout( new GridBagLayout() );

            for ( int row = 0; row < rows; row++ )
            {
                for ( int col = 0; col < columns; col++ )
                {
                    if ( i < videoPanels.size() )
                    {
                        logger.debug( "Adding panel - " + i + " at (col,row) -(" + col + "," + row + ")" );
                        remove( videoPanels.get( i ) );
                        add( videoPanels.get( i ), createConstraints( col, row ) );

                        i++;
                    }
                }
            }
        }
        revalidate();
    }

    /*
     * Create GridBagConstraints for VideoPanel
     */
    private GridBagConstraints createConstraints( int gridx, int gridy )
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        //constraints.insets = new Insets( 1, 1, 1, 1 );
        return constraints;
    }

    /**
     * Get the video panels in the video grid
     * 
     * @return list of VideoPanel
     */
    public List< VideoPanel > getVideoPanels()
    {
        return model.getVideoPanels();
    }

    public void removeVideoPanelFromGrid( VideoPanel videoPanel )
    {
        remove( videoPanel );
    }

    public void resizeVideoPanels( List< VideoPanel > videoPanels, Dimension dimension )
    {
        for ( VideoPanel vidPanel : videoPanels )
        {
            vidPanel.resize( dimension );
        }
    }

    public void refreshRowsAndColumns( int rows, int columns )
    {
        this.rows = rows;
        this.columns = columns;
    }

    public void setPanelSize( Dimension dimension )
    {
        setPreferredSize( dimension );
        setMinimumSize( dimension );
    }
}
