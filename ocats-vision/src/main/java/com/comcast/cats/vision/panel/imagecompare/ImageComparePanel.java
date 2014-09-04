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
package com.comcast.cats.vision.panel.imagecompare;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;

import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.vision.components.XMLFilter;

/**
 * The ImageCompare window that holds all image compare functionalities. Appears
 * when snap image menu item is clicked on CATS Vision View.
 * 
 * @author Sajay JK
 * 
 *         Modified on July 9th,2012 by aswathyann to incorporate MVC pattern
 * 
 */
public class ImageComparePanel extends JPanel
{

    private static final long     serialVersionUID      = 1L;

    private static final Logger   logger                = Logger.getLogger( ImageComparePanel.class );

    private JFileChooser          regionFileLoader;
    private JFileChooser          regionFileSaver;

    private RegionDetailsPanel    regionDetailsPanel;
    private FreezeVideoPanel      freezeVideoPanel;
    private ImageCompareInfoPanel icInfoPanel;

    private JScrollPane           freezeVideoScrollPane = new JScrollPane();

    private Dimension             snapImageSize;

    public ImageComparePanel( String macID, RegionDetailsPanel regionDetailsPanel, FreezeVideoPanel freezeVideoPanel,
            ImageCompareInfoPanel icInfoPanel )
    {
        this.regionDetailsPanel = regionDetailsPanel;
        this.freezeVideoPanel = freezeVideoPanel;
        this.icInfoPanel = icInfoPanel;

        freezeVideoScrollPane.setAutoscrolls( true );
        freezeVideoScrollPane.setViewportView(freezeVideoPanel);
        freezeVideoScrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        freezeVideoScrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );        
        freezeVideoScrollPane.setMinimumSize( freezeVideoPanel.getPreferredSize());
        
        setName( macID );

        setLayout( new GridBagLayout() );
        initGui();
        setVisible( true );
        logger.info( "ImageCompare Window Initialized" );
    }

    private void initGui()
    {
        createPanels();
        createFileChoosers();
    }

    /**
     * Create necessary panels for IC Window.
     */
    private void createPanels()
    {

        GridBagConstraints freezeVideoPanelConstraints = new GridBagConstraints();
        freezeVideoPanelConstraints.gridx = 0;
        freezeVideoPanelConstraints.gridy = 0;
        freezeVideoPanelConstraints.weightx = 0.50;

        freezeVideoPanelConstraints.anchor = GridBagConstraints.CENTER;
        freezeVideoPanelConstraints.insets = new Insets( 0, 50, 0, 10 );
        add( freezeVideoScrollPane, freezeVideoPanelConstraints );

        GridBagConstraints sidePanelConstraints = new GridBagConstraints();
        sidePanelConstraints.gridx = 1;
        sidePanelConstraints.gridy = 0;
        sidePanelConstraints.fill = GridBagConstraints.VERTICAL;
        sidePanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        add( icInfoPanel, sidePanelConstraints );
        add( regionDetailsPanel, sidePanelConstraints );
        regionDetailsPanel.setVisible( false );
    }

    /**
     * Create JFileChoosers for load and save.
     */
    private void createFileChoosers()
    {
        regionFileSaver = new JFileChooser( "Save Region Info XML" );
        regionFileSaver.addChoosableFileFilter( new XMLFilter() );
        regionFileSaver.setAcceptAllFileFilterUsed( false );

        regionFileLoader = new JFileChooser()
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
        regionFileLoader.addChoosableFileFilter( new XMLFilter() );
        regionFileLoader.setAcceptAllFileFilterUsed( false );
    }

    public void setCurrentImage( BufferedImage currentSnaphot, List< RegionInfo > regionsList )
    {

        freezeVideoPanel.setSnapshot( currentSnaphot );
        logger.debug( "Set Current Snapshot as " + currentSnaphot );
        // then update the panels with the loaded information.
        if ( regionsList != null && regionsList.size() > 0 )
        {
            RegionInfo defaultRegion = regionsList.get( 0 );
            freezeVideoPanel.paintRegion( defaultRegion );
            regionDetailsPanel.loadRegionDetails( defaultRegion );
            regionDetailsPanel.setRegionsList( regionsList );
            for ( RegionInfo regionInfo : regionsList )
            {
                logger.debug( "loading Regions as " + regionInfo );
            }
        }
    }

    public Dimension getSnapImageSize()
    {
        return snapImageSize;
    }

    public void setSnapImageSize( Dimension snapImageSize )
    {
        this.snapImageSize = snapImageSize;
    }

    public RegionDetailsPanel getRegionDetailsPanel()
    {
        return regionDetailsPanel;
    }

    public void setRegionDetailsPanel( RegionDetailsPanel regionDetailsPanel )
    {
        this.regionDetailsPanel = regionDetailsPanel;
    }

    public FreezeVideoPanel getFreezeVideoPanel()
    {
        return freezeVideoPanel;
    }

    public void setFreezeVideoPanel( FreezeVideoPanel freezeVideoPanel )
    {
        this.freezeVideoPanel = freezeVideoPanel;
    }

    public JFileChooser getRegionFileLoader()
    {
        return regionFileLoader;
    }

    public void setRegionFileLoader( JFileChooser regionFileLoader )
    {
        this.regionFileLoader = regionFileLoader;
    }

    public JFileChooser getRegionFileSaver()
    {
        return regionFileSaver;
    }

    public void setRegionFileSaver( JFileChooser regionFileSaver )
    {
        this.regionFileSaver = regionFileSaver;
    }

    public ImageCompareInfoPanel getIcInfoPanel()
    {
        return icInfoPanel;
    }

    public void setIcInfoPanel( ImageCompareInfoPanel icInfoPanel )
    {
        this.icInfoPanel = icInfoPanel;
    }
}
