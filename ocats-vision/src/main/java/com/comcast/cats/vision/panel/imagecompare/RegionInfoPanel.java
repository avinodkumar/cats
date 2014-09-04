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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegionInfoPanel extends JPanel{

	private static final long 			   serialVersionUID = 1L;
	private JLabel                         lblX;
    private JLabel                         lblY;
    private JLabel                         lblWidth;
    private JLabel                         lblHeight;
    private JLabel                         lblRegionName;
    
    private JTextField                     txtRegionName;
    private JTextField                     txtY;
    private JTextField                     txtX;
    private JTextField                     txtWidth;
    private JTextField                     txtHeight;
    
    private static final String            DEFAULT_VALUE     = "0";
	
	public RegionInfoPanel(){
	        setLayout( new GridBagLayout() );
	        setBorder( BorderFactory.createTitledBorder( "Region Info" ) );
	        setName( "regionInfoPanel" );

	        GridBagConstraints regionNameConstraints = new GridBagConstraints();

	        // Constraints for Region name label
	        regionNameConstraints.anchor = GridBagConstraints.LINE_END;
	        regionNameConstraints.gridx = 0;
	        regionNameConstraints.gridy = 0;

	        // Add Region name label
	        lblRegionName = ImageCompareUtil.createLabel( "Name:", SwingConstants.RIGHT, SwingConstants.RIGHT );

	        add( lblRegionName, regionNameConstraints );
	        // Constraints for Region name text field
	        regionNameConstraints.anchor = GridBagConstraints.LINE_START;
	        regionNameConstraints.gridx = 1;
	        regionNameConstraints.gridy = 0;
	        regionNameConstraints.gridwidth = 3;
	        regionNameConstraints.ipadx = 100;
	        regionNameConstraints.insets = new Insets( 10, 20, 10, 20 );

	        // Add Region name text field
	        txtRegionName = ImageCompareUtil.createTextField( 10, "The region name", "", true );
	        txtRegionName.setName( "txtRegionName" );
	        add( txtRegionName, regionNameConstraints );

	        // Constraints for X label
	        GridBagConstraints xConstraints = new GridBagConstraints();
	        xConstraints.anchor = GridBagConstraints.LINE_END;
	        xConstraints.gridx = 0;
	        xConstraints.gridy = 1;

	        // Add label 'X'
	        lblX = ImageCompareUtil.createLabel( "X:", SwingConstants.RIGHT, SwingConstants.RIGHT );
	        add( lblX, xConstraints );

	        // Constraints for 'X' text field
	        xConstraints.anchor = GridBagConstraints.LINE_START;
	        xConstraints.gridx = 1;
	        xConstraints.gridy = 1;
	        xConstraints.insets = new Insets( 10, 20, 10, 20 );

	        // Add 'X' text field
	        txtX = ImageCompareUtil.createTextField( 3, "", DEFAULT_VALUE, false );
	        add( txtX, xConstraints );

	        // Constraints for 'Y' label
	        GridBagConstraints yConstraints = new GridBagConstraints();
	        yConstraints.anchor = GridBagConstraints.LINE_END;
	        yConstraints.gridx = 2;
	        yConstraints.gridy = 1;

	        // Add label 'X'
	        lblY = ImageCompareUtil.createLabel( "Y:", SwingConstants.RIGHT, SwingConstants.RIGHT );
	        add( lblY, yConstraints );

	        // Constraints for 'Y' text field
	        yConstraints.anchor = GridBagConstraints.LINE_START;
	        yConstraints.gridx = 3;
	        yConstraints.gridy = 1;
	        yConstraints.insets = new Insets( 10, 10, 10, 20 );

	        // Add 'Y' text field
	        txtY = ImageCompareUtil.createTextField( 3, "", DEFAULT_VALUE, false );
	        add( txtY, yConstraints );

	        // Constraints for Width label
	        GridBagConstraints widthConstraints = new GridBagConstraints();
	        widthConstraints.anchor = GridBagConstraints.LINE_END;
	        widthConstraints.gridx = 0;
	        widthConstraints.gridy = 2;

	        // Add label 'Width'
	        lblWidth = ImageCompareUtil.createLabel( "Width:", SwingConstants.RIGHT, SwingConstants.RIGHT );
	        add( lblWidth, widthConstraints );

	        // Constraints for Width text field
	        widthConstraints.anchor = GridBagConstraints.LINE_START;
	        widthConstraints.gridx = 1;
	        widthConstraints.gridy = 2;
	        widthConstraints.insets = new Insets( 10, 20, 10, 20 );

	        // Add 'Width' text field
	        txtWidth = ImageCompareUtil.createTextField( 3, "", DEFAULT_VALUE, false );
	        add( txtWidth, widthConstraints );

	        // Constraints for Height label
	        GridBagConstraints heightConstraints = new GridBagConstraints();
	        heightConstraints.anchor = GridBagConstraints.LINE_END;
	        heightConstraints.gridx = 2;
	        heightConstraints.gridy = 2;

	        // Add label 'Height'
	        lblHeight = ImageCompareUtil.createLabel( "Height:", SwingConstants.RIGHT, SwingConstants.RIGHT );
	        add( lblHeight, heightConstraints );

	        // Constraints for 'Height' text field
	        heightConstraints.anchor = GridBagConstraints.LINE_START;
	        heightConstraints.gridx = 3;
	        heightConstraints.gridy = 2;
	        heightConstraints.insets = new Insets( 10, 10, 10, 20 );

	        // Add 'Height' text field
	        txtHeight = ImageCompareUtil.createTextField( 3, "", DEFAULT_VALUE, false );
	        add( txtHeight, heightConstraints );

	        setBorder(BorderFactory.createTitledBorder("Region Info"));
	}

    public void setXValue( String xValue )
    {
        txtX.setText( xValue );
    }

    public void setYValue( String yValue )
    {
        txtY.setText( yValue );
    }

    public void setWidthValue( String width )
    {
        txtWidth.setText( width );
    }

    public void setHeightValue( String height )
    {
        txtHeight.setText( height );
    }
    
    public void setRegionName( String name )
    {
        txtRegionName.setText( name );
    }


	public String getXValue() {
		return txtX.getText();
	}
	
	public String getYValue() {
		return txtY.getText();
	}
	
	public String getWidthValue() {
		return txtWidth.getText();
	}
	
	public String getHeightValue() {
		return txtHeight.getText();
	}
	
	public String getRegionName() {
		return txtRegionName.getText();
	}


	public void clearValues() {
        txtRegionName.setText( "" );
        txtY.setText( DEFAULT_VALUE );
        txtX.setText( DEFAULT_VALUE );
        txtWidth.setText( DEFAULT_VALUE );
        txtHeight.setText( DEFAULT_VALUE );
	}

}
