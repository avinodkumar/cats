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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

/**
 * Panel that holds usage information about Image Compare module
 * 
 * @author sajayjk
 * 
 */
public class ImageCompareInfoPanel extends JPanel
{
    private static final long      serialVersionUID = -758016305574347046L;
    /**
     * Logger instance for RemotePanel.
     */
    private static final Logger    logger           = Logger.getLogger( ImageCompareInfoPanel.class );

    JTextArea                      icUsageInfo;

    private static final Integer   IC_PANEL_WIDTH   = 30;
    private static final Integer   IC_PANEL_HEIGHT  = 40;
    private static final Dimension icPanelDim       = new Dimension( IC_PANEL_WIDTH, IC_PANEL_HEIGHT );

    public ImageCompareInfoPanel()
    {
        icUsageInfo = new JTextArea( getHelpText() );
        icUsageInfo.setBorder( BorderFactory.createTitledBorder( "Snapshot Options" ) );
        icUsageInfo.setEditable( false );
        setMaximumSize( icPanelDim );
        add( icUsageInfo );
        logger.info( "Inintialzed ImageCompare Info Panel" );
    }

    /**
     * Help Text
     * 
     * @return
     */
    private String getHelpText()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "- Load a snapshot from disk\n" );
        builder.append( "  through the File menu\n\n\n" );
        builder.append( "- Or create a new Region from \n" );
        builder.append( "  Image Compare -> New Region\n" );
        builder.append( "  -> Image Compare \n\n\n" );
        builder.append( "- Select an area on the snapshot\n" );
        builder.append( "  with the mouse \n\n\n" );
        builder.append( "- Test the region from\n" );
        builder.append( "  Image Compare -> Test Current Region\n" );
        builder.append( "  or from  \n" );
        builder.append( "  Image Compare -> Test All Regions \n\n\n" );
        builder.append( "- Add the region by pressing\n" );
        builder.append( "  \"Add\" Button \n\n\n" );
        builder.append( "- Save the snapshot to disk from\n" );
        builder.append( "  Image Compare -> Save Snapshot" );
        return builder.toString();
    }

}
