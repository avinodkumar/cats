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
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.comcast.cats.image.OCRRegionInfo;

public class OCRSettingsPanel extends JPanel{

	private static final long serialVersionUID = 2089684606227092226L;

	private JLabel                         lblOCRExpectedText;
    private JLabel                         lblTimeout;
	private JLabel                         lblMatchPct;
	
    private JTextField                     txtExpectedOCRText;
    private JTextField                     txtOCRMatchPct;
    private JTextField                     txtTimeout;
    
    private JSlider                        sldOCRMatchPercentage;
    private JSlider                        sldTimeout;
    
    private static final int               MATCH_PCT_MIN     = 1;
    private static final int               MATCH_PCT_MAX     = 100;
    private static final int               TIMEOUT_MIN     = 0;
    private static final int               TIMEOUT_MAX     = 60;
    
	
	public OCRSettingsPanel(){
        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createTitledBorder( "OCR Settings" ) );

        // Constraints for Text label
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.anchor = GridBagConstraints.LINE_END;
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;

        // Add Text label
        lblOCRExpectedText = ImageCompareUtil.createLabel( "Text:", SwingConstants.RIGHT, SwingConstants.RIGHT );

        add( lblOCRExpectedText, textConstraints );
        // Constraints for OCRText text field
        textConstraints.anchor = GridBagConstraints.LINE_START;
        textConstraints.gridx = 1;
        textConstraints.gridy = 0;
        textConstraints.gridwidth = 2;
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.insets = new Insets( 10,10, 10, 0 );

        // Add OCRText text field
        txtExpectedOCRText = ImageCompareUtil.createTextField( 10, "The OCR Text", "", true );
        add( txtExpectedOCRText, textConstraints );
        
        
        // Constraints for 'Match %:' label
        GridBagConstraints matchPercentageConstraints = new GridBagConstraints();
        matchPercentageConstraints.anchor = GridBagConstraints.LINE_END;
        matchPercentageConstraints.gridx = 0;
        matchPercentageConstraints.gridy = 1;

        // Add label for 'Match %:'
        lblMatchPct = ImageCompareUtil.createLabel( "Match %:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblMatchPct, matchPercentageConstraints );

        // Constraints for 'Match %:' slider
        matchPercentageConstraints.gridx = 1;
        matchPercentageConstraints.gridy = 1;

        // Add Slider for 'Match %:'
        sldOCRMatchPercentage = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, MATCH_PCT_MIN, MATCH_PCT_MAX,
                ( int ) OCRRegionInfo.DEFAULT_SUCCESS_TOLERANCE,
                "The percentage of the region that must match to result in a pass" );

        add( sldOCRMatchPercentage, matchPercentageConstraints );

        // Constraints for 'Match %:' text field
        matchPercentageConstraints.gridx = 2;
        matchPercentageConstraints.gridy = 1;
        matchPercentageConstraints.ipadx = 5;
        
        // Add text field for 'Match %:'
        txtOCRMatchPct = ImageCompareUtil.createTextField( 2, sldOCRMatchPercentage.getToolTipText(), String.valueOf( sldOCRMatchPercentage
                .getValue() ), false );
        add( txtOCRMatchPct, matchPercentageConstraints );       
        
        // Constraints for 'Timeout:' label
        GridBagConstraints timeOutConstraints = new GridBagConstraints();
        timeOutConstraints.anchor = GridBagConstraints.LINE_END;
        timeOutConstraints.gridx = 0;
        timeOutConstraints.gridy = 2;

        // Add label for 'Timeout:'
        lblTimeout = ImageCompareUtil.createLabel( "Timeout:", SwingConstants.RIGHT, SwingConstants.RIGHT );
        add( lblTimeout, timeOutConstraints );

        // Constraints for 'Timeout:' slider
        timeOutConstraints.gridx = 1;
        timeOutConstraints.gridy = 2;

        // Add Slider for 'Timeout:'
        sldTimeout = ImageCompareUtil.createSlider( JSlider.HORIZONTAL, TIMEOUT_MIN, TIMEOUT_MAX,
                ( int ) OCRRegionInfo.DEFAULT_TIMEOUT,
                "The percentage of the region that must match to result in a pass" );

        add( sldTimeout, timeOutConstraints );

        // Constraints for 'Timeout:' text field
        timeOutConstraints.gridx = 2;
        timeOutConstraints.gridy = 2;
        timeOutConstraints.ipadx = 5;

        // Add text field for 'Timeout:'
        txtTimeout = ImageCompareUtil.createTextField( 2, sldTimeout.getToolTipText(), String.valueOf( sldTimeout
                .getValue() ), false );
        add( txtTimeout, timeOutConstraints );        

        setupListeners();
	}
	
	public void setExpectedText(String expectedOCRText){
		txtExpectedOCRText.setText(expectedOCRText);
	}
	
	public void setMatchPct(int matchPct){
		sldOCRMatchPercentage.setValue( matchPct);
		txtOCRMatchPct.setText( String.valueOf( sldOCRMatchPercentage.getValue() ) );
	}
	
	public void setTimeout(int timeout){
		sldTimeout.setValue( timeout);
		txtTimeout.setText( String.valueOf( sldTimeout.getValue() ) );
	}

	public String getExpectedText(){
		return txtExpectedOCRText.getText(); 
	}
	
	public int getMatchPct(){
		return Integer.parseInt(txtOCRMatchPct.getText()); 
	}
	
	public int  getTimeOut(){
		return Integer.parseInt(txtTimeout.getText()); 
	}
	
	public void clearValues() {
		txtExpectedOCRText.setText("");
        sldOCRMatchPercentage.setValue((int) OCRRegionInfo.DEFAULT_SUCCESS_TOLERANCE);
        txtOCRMatchPct.setText(String.valueOf(sldOCRMatchPercentage.getValue()));
        sldTimeout.setValue((int) OCRRegionInfo.DEFAULT_TIMEOUT);
        txtTimeout.setText(String.valueOf(sldTimeout.getValue()));
	}
	
	
    private void setupListeners(){
    	 // OCR Match Percent slider
        sldOCRMatchPercentage.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtOCRMatchPct.setText( String.valueOf( sldOCRMatchPercentage.getValue() ) );
            }
        } );
        
        // Timeout slider
        sldTimeout.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                txtTimeout.setText( String.valueOf( sldTimeout.getValue() ) );
            }
        } );
    }
}
