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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.comcast.cats.image.ImageCompareRegionInfo;

public class ICSettingsPanel extends JPanel {

	private static final long serialVersionUID = -820880179539663455L;
	
	private JLabel                         lblMatchPct;
    private JLabel                         lblXTolerance;
    private JLabel                         lblYTolerance;
    private JLabel                         lblBlueTolerance;
    private JLabel                         lblGreenTolerance;
    private JLabel                         lblRedTolerance;
    
    private JTextField                     txtBlueTolerance;
    private JTextField                     txtGreenTolerance;
    private JTextField                     txtRedTolerance;
    private JTextField                     txtMatchPct;
    private JTextField                     txtXTolerance;
    private JTextField                     txtYTolerance;
    
    private JSlider                        sldMatchPercentage;
    private JSlider                        sldXTolerance;
    private JSlider                        sldYTolerance;
    private JSlider                        sldBlueTolerance;
    private JSlider                        sldGreenTolerance;
    private JSlider                        sldRedTolerance;
    
    private static final int               MATCH_PCT_MIN     = 1;
    private static final int               MATCH_PCT_MAX     = 100;

    private static final int               RGB_MIN           = 0;
    private static final int               RGB_MAX           = 255;
    
    private static final int               REG_TOLERANCE_MIN = 0;
    private static final int               REG_TOLERANCE_MAX = 20;
	
	public ICSettingsPanel(){
	
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createTitledBorder( "Tolerance" ) );

        // Constraints for 'Match %:' label
        GridBagConstraints matchPercentageConstraints = new GridBagConstraints();
        matchPercentageConstraints.anchor = GridBagConstraints.LINE_END;
        matchPercentageConstraints.gridx = 0;
        matchPercentageConstraints.gridy = 0;

        // Add label for 'Match %:'
        lblMatchPct = ImageCompareUtil.createLabel( "Match %:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblMatchPct, matchPercentageConstraints );

        // Constraints for 'Match %:' slider
        matchPercentageConstraints.gridx = 1;
        matchPercentageConstraints.gridy = 0;

        // Add Slider for 'Match %:'
        sldMatchPercentage = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, MATCH_PCT_MIN, MATCH_PCT_MAX,
                ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT.intValue(),
                "The percentage of the region that must match to result in a pass" );

        add( sldMatchPercentage, matchPercentageConstraints );

        // Constraints for 'Match %:' text field
        matchPercentageConstraints.gridx = 2;
        matchPercentageConstraints.gridy = 0;
        matchPercentageConstraints.ipadx = 5;

        // Add text field for 'Match %:'

        txtMatchPct = ImageCompareUtil.createTextField( 2, sldMatchPercentage.getToolTipText(), String.valueOf( sldMatchPercentage
                .getValue() ), false );
        add( txtMatchPct, matchPercentageConstraints );

        // Constraints for X Tolerance label
        GridBagConstraints xToleranceConstraints = new GridBagConstraints();
        xToleranceConstraints.anchor = GridBagConstraints.LINE_END;
        xToleranceConstraints.gridx = 0;
        xToleranceConstraints.gridy = 1;

        // Add label for for X Tolerance
        lblXTolerance = ImageCompareUtil.createLabel( "X:", SwingConstants.RIGHT, SwingConstants.RIGHT );

        add( lblXTolerance, xToleranceConstraints );

        // Constraints for X Tolerance slider

        xToleranceConstraints.gridx = 1;
        xToleranceConstraints.gridy = 1;

        // Add slider for for X Tolerance
        sldXTolerance = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, REG_TOLERANCE_MIN, REG_TOLERANCE_MAX,
                ImageCompareRegionInfo.DEFAULT_X_TOLERANCE,
                "The number of pixels to the left and right to check for the region" );
        add( sldXTolerance, xToleranceConstraints );

        // Constraints for X Tolerance text field
        xToleranceConstraints.gridx = 2;
        xToleranceConstraints.gridy = 1;
        xToleranceConstraints.ipadx = 5;

        // Add text field for for X Tolerance
        txtXTolerance = ImageCompareUtil.createTextField( 2, sldXTolerance.getToolTipText(), String.valueOf( sldXTolerance.getValue() ),
                false );
        add( txtXTolerance, xToleranceConstraints );

        // Constraints for Y Tolerance label
        GridBagConstraints yToleranceConstraints = new GridBagConstraints();
        yToleranceConstraints.anchor = GridBagConstraints.LINE_END;
        yToleranceConstraints.gridx = 0;
        yToleranceConstraints.gridy = 2;

        // Add label for for Y Tolerance
        lblYTolerance = ImageCompareUtil.createLabel( "Y:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblYTolerance, yToleranceConstraints );

        // Constraints for Y Tolerance Slider
        yToleranceConstraints.gridx = 1;
        yToleranceConstraints.gridy = 2;

        // Add Slider for Y Tolerance
        sldYTolerance = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, REG_TOLERANCE_MIN, REG_TOLERANCE_MAX,
                ImageCompareRegionInfo.DEFAULT_Y_TOLERANCE, "The number of pixels up and down to check for the region" );
        add( sldYTolerance, yToleranceConstraints );

        // Constraints for Y Tolerance text field
        yToleranceConstraints.gridx = 2;
        yToleranceConstraints.gridy = 2;
        yToleranceConstraints.ipadx = 5;

        // Add text field for Y Tolerance
        txtYTolerance = ImageCompareUtil.createTextField( 2, sldYTolerance.getToolTipText(), String.valueOf( sldYTolerance.getValue() ),
                false );
        add( txtYTolerance, yToleranceConstraints );

        // Constraints for Red Tolerance label
        GridBagConstraints redToleranceConstraints = new GridBagConstraints();
        redToleranceConstraints.anchor = GridBagConstraints.LINE_END;
        redToleranceConstraints.gridx = 0;
        redToleranceConstraints.gridy = 3;

        // Add label for for Red Tolerance
        lblRedTolerance = ImageCompareUtil.createLabel( "Red:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblRedTolerance, redToleranceConstraints );

        // Constraints for Red Tolerance slider
        redToleranceConstraints.gridx = 1;
        redToleranceConstraints.gridy = 3;

        // Add Slider for Red Tolerance
        sldRedTolerance =ImageCompareUtil. createSlider( JSlider.HORIZONTAL, RGB_MIN, RGB_MAX,
                ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE, "The red tolerance of the image compare" );
        add( sldRedTolerance, redToleranceConstraints );

        // Constraints for red tolerance text field
        redToleranceConstraints.gridx = 2;
        redToleranceConstraints.gridy = 3;
        redToleranceConstraints.ipadx = 5;

        // Add Red tolerance text field
        txtRedTolerance = ImageCompareUtil.createTextField( 2, sldRedTolerance.getToolTipText(), String.valueOf( sldRedTolerance
                .getValue() ), false );
        add( txtRedTolerance, redToleranceConstraints );

        // Constraints for Green Tolerance label
        GridBagConstraints greenToleranceConstraints = new GridBagConstraints();
        greenToleranceConstraints.anchor = GridBagConstraints.LINE_END;
        greenToleranceConstraints.gridx = 0;
        greenToleranceConstraints.gridy = 4;

        // Add label for for Green Tolerance
        lblGreenTolerance = ImageCompareUtil.createLabel( "Green:", SwingConstants.RIGHT, SwingConstants.RIGHT );

        add( lblGreenTolerance, greenToleranceConstraints );

        // Constraints for Green Tolerance slider
        greenToleranceConstraints.gridx = 1;
        greenToleranceConstraints.gridy = 4;

        // Add Slider for Red Tolerance
        sldGreenTolerance = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, RGB_MIN, RGB_MAX,
                ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE, "The green tolerance of the image compare" );
        add( sldGreenTolerance, greenToleranceConstraints );

        // Constraints for Green tolerance text field
        greenToleranceConstraints.gridx = 2;
        greenToleranceConstraints.gridy = 4;
        greenToleranceConstraints.ipadx = 5;

        // Add Green tolerance text field
        txtGreenTolerance = ImageCompareUtil.createTextField( 2, sldGreenTolerance.getToolTipText(), String.valueOf( sldGreenTolerance
                .getValue() ), false );
        add( txtGreenTolerance, greenToleranceConstraints );

        // Constraints for Blue tolerance label
        GridBagConstraints blueToleranceConstraints = new GridBagConstraints();
        blueToleranceConstraints.anchor = GridBagConstraints.LINE_END;
        blueToleranceConstraints.gridx = 0;
        blueToleranceConstraints.gridy = 5;

        // Add label for for Blue Tolerance
        lblBlueTolerance = ImageCompareUtil.createLabel( "Blue:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblBlueTolerance, blueToleranceConstraints );

        // Constraints for Blue tolerance slider
        blueToleranceConstraints.gridx = 1;
        blueToleranceConstraints.gridy = 5;

        // Add slider for for Blue Tolerance
        sldBlueTolerance = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, RGB_MIN, RGB_MAX,
                ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE, "The blue tolerance of the image compare" );
        add( sldBlueTolerance, blueToleranceConstraints );

        // Constraints for Blue tolerance text field
        blueToleranceConstraints.gridx = 2;
        blueToleranceConstraints.gridy = 5;
        blueToleranceConstraints.ipadx = 5;

        // Add Blue tolerance text field
        txtBlueTolerance = ImageCompareUtil.createTextField( 2, sldBlueTolerance.getToolTipText(), String.valueOf( sldBlueTolerance
                .getValue() ), false );
        add( txtBlueTolerance, blueToleranceConstraints );
        
        setupListeners();
	}
	
	public void setMatchPct(int matchPct){
		sldMatchPercentage.setValue( matchPct);
		txtMatchPct.setText( String.valueOf( sldMatchPercentage.getValue() ) );
	}
	
	public void setXTolerance(int xTolerance){
		 sldXTolerance.setValue( xTolerance );
         txtXTolerance.setText( String.valueOf( sldXTolerance.getValue() ) );
	}
	
	public void setYTolerance(int yTolerance){
		 sldYTolerance.setValue( yTolerance );
        txtYTolerance.setText( String.valueOf( sldYTolerance.getValue() ) );
	}
	
	public void setRedTolerance(int redTolerance){
		sldRedTolerance.setValue( redTolerance );
		txtRedTolerance.setText( String.valueOf( sldRedTolerance.getValue() ) );
	}
	
	public void setGreenTolerance(int greenTolerance){
		sldGreenTolerance.setValue( greenTolerance );
		txtGreenTolerance.setText( String.valueOf( sldGreenTolerance.getValue() ) );
	}
	
	public void setBlueTolerance(int blueTolerance){
		sldBlueTolerance.setValue( blueTolerance );
		txtBlueTolerance.setText( String.valueOf( sldBlueTolerance.getValue() ) );
	}
	
	public int getMatchPct(){
		return Integer.parseInt(txtMatchPct.getText());
	}
	
	public int getXTolerance(){
		return Integer.parseInt(txtXTolerance.getText());
	}
	
	public int getYTolerance(){
		return Integer.parseInt(txtYTolerance.getText());
	}
	
	public int getRedTolerance(){
		return Integer.parseInt(txtRedTolerance.getText());
	}
	
	public int getGreenTolerance(){
		return Integer.parseInt(txtGreenTolerance.getText());
	}
	
	public int getBlueTolerance(){
		return Integer.parseInt(txtBlueTolerance.getText());
	}
	
	public void clearValues() {
		sldMatchPercentage.setValue(ImageCompareRegionInfo.DEFAULT_MATCH_PERCENT.intValue());
        txtMatchPct.setText( String.valueOf( sldMatchPercentage.getValue() ) );
        sldXTolerance.setValue( ImageCompareRegionInfo.DEFAULT_X_TOLERANCE );
        txtXTolerance.setText( String.valueOf( sldXTolerance.getValue() ) );
        sldYTolerance.setValue( ImageCompareRegionInfo.DEFAULT_Y_TOLERANCE );
        txtYTolerance.setText( String.valueOf( sldYTolerance.getValue() ) );
        sldRedTolerance.setValue( ImageCompareRegionInfo.DEFAULT_RED_TOLERANCE );
        txtRedTolerance.setText( String.valueOf( sldRedTolerance.getValue() ) );
        sldGreenTolerance.setValue( ImageCompareRegionInfo.DEFAULT_GREEN_TOLERANCE );
        txtGreenTolerance.setText( String.valueOf( sldGreenTolerance.getValue() ) );
        sldBlueTolerance.setValue( ImageCompareRegionInfo.DEFAULT_BLUE_TOLERANCE );
        txtBlueTolerance.setText( String.valueOf( sldBlueTolerance.getValue() ) );
	}
	
    /**
     * Adding ChangeListener for Sliders, ActionListener for 'Add/Update'
     * button.
     */
    private void setupListeners()
    {

        // Match Percent slider
        sldMatchPercentage.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtMatchPct.setText( String.valueOf( sldMatchPercentage.getValue() ) );
            }
        } );

        // X Tolerance slider.
        sldXTolerance.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtXTolerance.setText( String.valueOf( sldXTolerance.getValue() ) );
            }
        } );

        // Y Tolerance slider.
        sldYTolerance.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtYTolerance.setText( String.valueOf( sldYTolerance.getValue() ) );
            }
        } );

        // Red Tolerance slider.
        sldRedTolerance.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtRedTolerance.setText( String.valueOf( sldRedTolerance.getValue() ) );
            }
        } );

        // Green Tolerance slider.
        sldGreenTolerance.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtGreenTolerance.setText( String.valueOf( sldGreenTolerance.getValue() ) );
            }
        } );

        // Blue Tolerance slider.
        sldBlueTolerance.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtBlueTolerance.setText( String.valueOf( sldBlueTolerance.getValue() ) );
            }
        } );
    }
}
