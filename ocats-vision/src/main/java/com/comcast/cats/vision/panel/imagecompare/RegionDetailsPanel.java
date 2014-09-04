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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.image.RegionInfo;

/**
 * The RegionDetailsPanel helps to define regions selected in the
 * FreezeVideoPanel. Contains three panels 1) Panel for defining region info 2)
 * Panel for defining image compare settings 3)Panel to Add/update region.
 * 
 * @author Sajay J K
 * 
 */
public class RegionDetailsPanel extends JPanel
{
    private static final long      serialVersionUID = -758016305574347046L;

    /**
     * Logger instance for RegionDetailsPanel.
     */
    private static final Logger    logger           = Logger.getLogger( RegionDetailsPanel.class );

    private JLabel                 lblPassFail;
    private JLabel                 lbltimeTaken;
    private JButton                btnAddUpdate;

    private RegionInfoPanel        panelRegionInfo;
    private ICSettingsPanel        panelICSettings;
    private OCRSettingsPanel       panelOCRSettings;
    private JPanel                 panelAddUpdate;

    private List< RegionInfo >     regionsList;
    private RegionInfo             currentRegion;

    private String                 snapshotFilepath = null;
    private boolean                testMode         = false;

    private int                    mode             = 1;

    private static final Integer   IC_PANEL_WIDTH   = 30;
    private static final Integer   IC_PANEL_HEIGHT  = 40;

    private static final Dimension icPanelDim       = new Dimension( IC_PANEL_WIDTH, IC_PANEL_HEIGHT );
    private static final String    ADD_TEXT         = "Add";
    private static final String    UPDATE_TEXT      = "Update";
    private static final String    FAILURE_STRING   = "Fail";
    private static final String    PASS_STRING      = "Pass";
    public static final int        IC_MODE          = 1;
    public static final int        OCR_MODE         = 2;

    /**
     * Constructor for creating the region details panel.
     */
    public RegionDetailsPanel()
    {
        setName( "regionDetailsPanel" );
        setLayout( new GridBagLayout() );
        regionsList = new ArrayList< RegionInfo >();

        panelRegionInfo = createRegionInfoPanel();
        panelICSettings = createImageCompareSettingsPanel();
        panelOCRSettings = createOCRSettingsPanel();
        panelAddUpdate = createAddUpdatePanel();

        this.add( panelRegionInfo, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        this.add( panelICSettings, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        this.add( panelOCRSettings, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        this.add( panelAddUpdate, new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 5, 0, 0, 0 ), 0, 0 ) );

        panelOCRSettings.setVisible( false );
        setMaximumSize( icPanelDim );
        setVisible( false );
        setupListeners();
        logger.info( "Initialized RegionDetailsPanel" );
    }

    /**
     * Creates panel for defining region info
     */
    private RegionInfoPanel createRegionInfoPanel()
    {
        return new RegionInfoPanel();
    }

    /**
     * Creates panel for defining OCR settings
     */
    private OCRSettingsPanel createOCRSettingsPanel()
    {
        return new OCRSettingsPanel();
    }

    /**
     * Creates panel for defining image compare settings
     */
    private ICSettingsPanel createImageCompareSettingsPanel()
    {
        return new ICSettingsPanel();
    }

    /**
     * Creates panel for Add/Update region
     */
    private JPanel createAddUpdatePanel()
    {
        JPanel addUpdatePanel = new JPanel();
        setName( "addUpdatePanel" );
        addUpdatePanel.setLayout( new GridBagLayout() );

        GridBagConstraints addUpdatePanelConstraints = new GridBagConstraints();
        addUpdatePanelConstraints.insets = new Insets( 5, 200, 0, 5 );

        btnAddUpdate = new JButton();
        btnAddUpdate.setName( "btnAddUpdate" );
        btnAddUpdate.setText( ADD_TEXT );
        btnAddUpdate.setToolTipText( "Adds or updates the current region" );
        btnAddUpdate.setPreferredSize( new java.awt.Dimension( 141, 22 ) );
        addUpdatePanelConstraints.anchor = GridBagConstraints.LINE_END;
        addUpdatePanelConstraints.gridx = 0;
        addUpdatePanelConstraints.gridy = 1;

        addUpdatePanel.add( btnAddUpdate, addUpdatePanelConstraints );

        lblPassFail = new JLabel( FAILURE_STRING, JLabel.CENTER );
        lblPassFail.setBackground( Color.RED );
        lblPassFail.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
        lblPassFail.setOpaque( true );
        lblPassFail.setPreferredSize( new java.awt.Dimension( 141, 22 ) );
        lblPassFail.setVisible( false );
        lblPassFail.setName( "lblPassFail" );
        addUpdatePanelConstraints.anchor = GridBagConstraints.LINE_END;
        addUpdatePanelConstraints.gridx = 0;
        addUpdatePanelConstraints.gridy = 3;
        addUpdatePanel.add( lblPassFail, addUpdatePanelConstraints );

        lbltimeTaken = new JLabel( "Time Passed : " );
        lbltimeTaken.setToolTipText( "Time Passed" );
        lbltimeTaken.setVisible( true );
        addUpdatePanelConstraints.anchor = GridBagConstraints.LINE_START;
        addUpdatePanelConstraints.gridx = 0;
        addUpdatePanelConstraints.gridy = 4;
        addUpdatePanel.add( lbltimeTaken, addUpdatePanelConstraints );

        return addUpdatePanel;
    }

    public void setMode( int mode )
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return mode;
    }

    @Override
    public void repaint()
    {
        if ( OCR_MODE == mode )
        {
            panelICSettings.setVisible( false );
            panelOCRSettings.setVisible( true );
        }
        else if ( IC_MODE == mode )
        {
            panelOCRSettings.setVisible( false );
            panelICSettings.setVisible( true );
        }

        super.repaint();
    }

    /**
     * Sets regions as the current region list.
     * 
     * @param regionsList
     */
    public void setRegionsList( List< RegionInfo > regionsList )
    {
        if ( null != regionsList )
        {
            this.regionsList = regionsList;
        }
    }

    /**
     * Returns a list of all the added regions.
     * 
     * @return
     */
    public List< RegionInfo > getRegionsList()
    {
        return regionsList;
    }

    /**
     * Loads the region details.
     * 
     * @param regionInfo
     *            contains all the region details.
     */
    public void loadRegionDetails( RegionInfo regionInfo )
    {
        if ( null != regionInfo )
        {
            panelRegionInfo.setRegionName( regionInfo.getName() );
            panelRegionInfo.setXValue( Integer.toString( regionInfo.getX() ) );
            panelRegionInfo.setYValue( Integer.toString( regionInfo.getY() ) );
            panelRegionInfo.setWidthValue( Integer.toString( regionInfo.getWidth() ) );
            panelRegionInfo.setHeightValue( Integer.toString( regionInfo.getHeight() ) );

            if ( regionInfo instanceof ImageCompareRegionInfo )
            {
                ImageCompareRegionInfo icRegionInfo = ( ImageCompareRegionInfo ) regionInfo;
                panelICSettings.setMatchPct(  icRegionInfo.getMatchPct().intValue() );
                panelICSettings.setXTolerance( ( int ) icRegionInfo.getXTolerance() );
                panelICSettings.setYTolerance( ( int ) icRegionInfo.getYTolerance() );
                panelICSettings.setRedTolerance( ( int ) icRegionInfo.getRedTolerance() );
                panelICSettings.setGreenTolerance( ( int ) icRegionInfo.getGreenTolerance() );
                panelICSettings.setBlueTolerance( ( int ) icRegionInfo.getBlueTolerance() );
                setMode( IC_MODE );
            }
            else if ( regionInfo instanceof OCRRegionInfo )
            {
                OCRRegionInfo ocrRegionInfo = ( OCRRegionInfo ) regionInfo;
                panelOCRSettings.setMatchPct( ocrRegionInfo.getSuccessTolerance() );
                panelOCRSettings.setTimeout( ocrRegionInfo.getTimeout() );
                panelOCRSettings.setExpectedText( ocrRegionInfo.getExpectedResult() );
                setMode( OCR_MODE );
            }
            lblPassFail.setText( FAILURE_STRING );
            lblPassFail.setBackground( Color.red );
            lbltimeTaken.setText( "Time Passed : " );
            btnAddUpdate.setText( UPDATE_TEXT );
            currentRegion = regionInfo;
            repaint();
        }
    }
    public void setCurrentRegion( RegionInfo currentRegion )
    {
        this.currentRegion = currentRegion;
    }
    /**
     * Returns the Current region
     * 
     * @return the current region (ImageCompareRegionInfo)
     */
    public RegionInfo getCurrentRegion()
    {
        RegionInfo currentRegionInfo = null;

        String regionName = panelRegionInfo.getRegionName();

        int x = Integer.parseInt( panelRegionInfo.getXValue() );
        int y = Integer.parseInt( panelRegionInfo.getYValue() );
        int width = Integer.parseInt( panelRegionInfo.getWidthValue() );
        int height = Integer.parseInt( panelRegionInfo.getHeightValue() );

        if ( mode == IC_MODE )
        {

            currentRegionInfo = new ImageCompareRegionInfo();

            float matchPct = panelICSettings.getMatchPct();
            int xTolerance = panelICSettings.getXTolerance();
            int yTolerance = panelICSettings.getYTolerance();
            int redTolerance = panelICSettings.getRedTolerance();
            int blueTolerance = panelICSettings.getBlueTolerance();
            int greenTolerance = panelICSettings.getGreenTolerance();

            currentRegionInfo.setName( regionName );
            currentRegionInfo.setX( x );
            currentRegionInfo.setY( y );
            currentRegionInfo.setWidth( width );
            currentRegionInfo.setHeight( height );
            currentRegionInfo.setXTolerance( xTolerance );
            currentRegionInfo.setYTolerance( yTolerance );
            ( ( ImageCompareRegionInfo ) currentRegionInfo ).setMatchPct( matchPct );
            ( ( ImageCompareRegionInfo ) currentRegionInfo ).setRedTolerance( redTolerance );
            ( ( ImageCompareRegionInfo ) currentRegionInfo ).setBlueTolerance( blueTolerance );
            ( ( ImageCompareRegionInfo ) currentRegionInfo ).setGreenTolerance( greenTolerance );

        }
        else if ( mode == OCR_MODE )
        {
            currentRegionInfo = new OCRRegionInfo();

            int matchPct = panelOCRSettings.getMatchPct();
            String expectedResult = panelOCRSettings.getExpectedText();
            int timeout = panelOCRSettings.getTimeOut();

            currentRegionInfo.setName( regionName );
            currentRegionInfo.setX( x );
            currentRegionInfo.setY( y );
            currentRegionInfo.setWidth( width );
            currentRegionInfo.setHeight( height );
            ( ( OCRRegionInfo ) currentRegionInfo ).setSuccessTolerance( matchPct );
            ( ( OCRRegionInfo ) currentRegionInfo ).setExpectedResult( expectedResult );
            ( ( OCRRegionInfo ) currentRegionInfo ).setTimeout( timeout );

        }
        return currentRegionInfo;
    }

    /**
     * Clear current region details.
     */
    public void clearCurrentRegionDetails()
    {
        panelRegionInfo.clearValues();
        panelICSettings.clearValues();
        panelOCRSettings.clearValues();
        lblPassFail.setText( FAILURE_STRING );
        lblPassFail.setBackground( Color.red );
        lbltimeTaken.setText( "Time Passed : " );
        btnAddUpdate.setText( ADD_TEXT );
        repaint();
    }
    /**
     * Clears information of all the regions defined.
     */
    public void clearAllRegions(){
        regionsList.clear();
    }

    /**
     * Change the panel look for test mode.
     */
    public void showTestMode()
    {
        testMode = true;
        lblPassFail.setBackground( Color.yellow );
        lblPassFail.setForeground( Color.black );
        lblPassFail.setText( "Comparing..." );
        lblPassFail.repaint();
        lblPassFail.setVisible( testMode );
    }

    /**
     * Display the text 'PASS' if the test is passed else 'FAIL' in the
     * 'Add/Update' panel.
     * 
     * @param testResult
     *            the test result(true if the test is passed, else false.).
     * @param timeSpent
     *            the time spent on testing
     */
    public void updateTestResults( boolean testResult, int timeSpentms )
    {
        lblPassFail.setBackground( testResult ? Color.green : Color.red );
        lblPassFail.setText( testResult ? PASS_STRING : FAILURE_STRING );
        lbltimeTaken.setText( "TimePassed : " + Math.round( timeSpentms / 1000 ) + " sec" );
        lblPassFail.repaint();
    }

    /**
     * Set the snapshot filepath. Used when the image is saved or loaded.
     * 
     * @param filepath
     */
    public void setSnapshotFilepath( String filepath )
    {
        if ( null != filepath && !filepath.isEmpty() )
        {
            snapshotFilepath = filepath;
        }
    }

    /**
     * Get the snapshot filepath. Null if snapshot was not loaded or saved
     * before.
     * 
     * @return
     */
    public String getSnapshotFilepath()
    {
        return snapshotFilepath;
    }

    /**
     * Checks if a given region is present in the list.
     * 
     * @param currentRegionName
     *            region to be checked.
     * @return true if region is present, else false.
     */
    private boolean isRegionPresent( String currentRegionName )
    {
        boolean retVal = false;
        for ( RegionInfo regionInfo : regionsList )
        {
            if ( regionInfo.getName().equals( currentRegionName ) )
            {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    /**
     * Reset The panel to initial conditions.
     * 
     * @param currentRegionName
     *            region to be checked.
     * @return true if region is present, else false.
     */
    public void resetRegionDetails()
    {
        clearCurrentRegionDetails();
        regionsList.clear();
    }

    public void setRegionInfo( int x, int y, int width, int height )
    {
        panelRegionInfo.setXValue( String.valueOf( x ) );
        panelRegionInfo.setYValue( String.valueOf( y ) );
        panelRegionInfo.setWidthValue( String.valueOf( width ) );
        panelRegionInfo.setHeightValue( String.valueOf( height ) );
    }

    /**
     * Adding ChangeListener for Sliders, ActionListener for 'Add/Update'
     * button.
     */
    private void setupListeners()
    {
        // Add/Update button.
        btnAddUpdate.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                RegionInfo currentRegionInfo = getCurrentRegion();
                if ( currentRegionInfo == null || currentRegionInfo.getWidth() == 0 || currentRegionInfo.getHeight() == 0 )
                {
                    JOptionPane.showMessageDialog( RegionDetailsPanel.this,
                            "Please select a region with a width and height > 0", "Cannot add",
                            JOptionPane.INFORMATION_MESSAGE );
                    return;
                }
                    if ( currentRegionInfo.getName().trim().isEmpty() )
                    { // if blank region name
                        JOptionPane.showMessageDialog( RegionDetailsPanel.this, "Please enter a region name",
                                "Cannot add", JOptionPane.INFORMATION_MESSAGE );
                        return;
                    }

                    if ( btnAddUpdate.getText().equals( ADD_TEXT ) )
                    {
                        if ( isRegionPresent( currentRegionInfo.getName() ) )
                        { // if same region name exists
                            JOptionPane.showMessageDialog( RegionDetailsPanel.this,
                                    "A region with this name already exists", "Cannot add",
                                    JOptionPane.INFORMATION_MESSAGE );
                            return;
                        }
                        regionsList.add( currentRegionInfo );
                        setCurrentRegion(currentRegionInfo);
                        btnAddUpdate.setText( UPDATE_TEXT );
                    }
                    else
                    {
                        regionsList.remove( currentRegion );
                        // since ArrayList is ordered; 0th element will always
                        // be
                        // the last updated element
                        // i.e the current region
                        regionsList.add( currentRegionInfo );
                        currentRegion = currentRegionInfo;
                    }
                }            
        } );
    }

}
