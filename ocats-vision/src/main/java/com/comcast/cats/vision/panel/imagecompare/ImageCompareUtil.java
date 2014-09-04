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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.apache.log4j.Logger;


public class ImageCompareUtil
{

    private static final Logger logger = Logger.getLogger( ImageCompareUtil.class );

    /**
     * Utility method to change file extension to jpg.
     * 
     * @param filePath
     * @return changed filePath
     */
    public static String changeExtensionToJPG( String filePath )
    {
        String jpgfile = null;
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Filepath is null or empty "+filePath);
        }       

        int dotPos = filePath.lastIndexOf( "." );
        String filename = filePath.substring( 0, dotPos );
        jpgfile = filename + "." + "jpg";
        logger.debug( "JPEG Filee " + jpgfile );

        return jpgfile;
    }

    /**
     * Loads a BufferedImage from file.
     * 
     * @return The file as a Buffered Image. If the file is not found, null is
     *         returned.
     */
    public static BufferedImage loadImageFromFile( String path )
    {

        BufferedImage img = null;
        if(path != null && !path.isEmpty()){
            File theFile = new File( path );
            logger.debug( "File "+theFile );
            if ( theFile.isFile() )
            {
                logger.debug( "File.isFile "+theFile.isFile() );
                try
                {
                    img = ImageIO.read( theFile );
                    logger.debug( "img "+img );
                }
                catch ( IOException e )
                {
                    logger.error( e.getMessage() );
                }
            }
        }
        return img;
    }

    /**
     * Creates a <code>JLabel</code> instance with the specified name,
     * horizontal alignment, horizontal text position.
     * 
     * @param labelName
     *            The text to be displayed by the label
     * @param horizontalAlignment
     *            One of the following constants defined in
     *            <code>SwingConstants</code>: <code>LEFT</code>,
     *            <code>CENTER</code> (the default for image-only labels),
     *            <code>RIGHT</code>, <code>LEADING</code> (the default for
     *            text-only labels) or <code>TRAILING</code>.
     * @param horizontalTextPosition
     *            One of the following constants defined in
     *            <code>SwingConstants</code>: <code>LEFT</code>,
     *            <code>CENTER</code>, <code>RIGHT</code>, <code>LEADING</code>,
     *            or <code>TRAILING</code> (the default).
     * @return new JLabel instance
     */
    public static JLabel createLabel( String labelName, int horizontalAlignment, int horizontalTextPosition )
    {
        JLabel label = new JLabel( labelName );
        label.setHorizontalAlignment( horizontalAlignment );
        label.setHorizontalTextPosition( horizontalTextPosition );
        return label;
    }

    /**
     * Creates a new <code>TextField</code> initialized with the specified text
     * number of columns and tool tip text.
     * 
     * @param columns
     *            The number of columns to use to calculate the preferred width
     * @param toolTipText
     *            The string to display; if the text is <code>null</code>, the
     *            tool tip is turned off for this component
     * @param text
     *            The text to be displayed
     * @param focusable
     *            Indicates whether this Component is focusable
     * @return new TextField instance
     */
    public static JTextField createTextField( int columns, String toolTipText, String text, boolean focusable )
    {

        JTextField textField = new JTextField( columns );
        textField.setToolTipText( toolTipText );
        textField.setText( text );
        textField.setFocusable( focusable );
        return textField;
    }

    /**
     * Creates a slider with the specified orientation and the specified
     * minimum, maximum, and initial values. The orientation can be either
     * <code>SwingConstants.VERTICAL</code> or
     * <code>SwingConstants.HORIZONTAL</code>.
     * 
     * @param orientation
     *            the orientation of the slider
     * @param min
     *            the minimum value of the slider
     * @param max
     *            the maximum value of the slider
     * @param value
     *            the initial value of the slider
     * @param toolTipText
     *            the string to display
     * @return
     */
    public static JSlider createSlider( int orientation, int min, int max, int value, String toolTipText )
    {

        JSlider slider = new JSlider( orientation, min, max, value );
        slider.setToolTipText( toolTipText );
        return slider;
    }

    /**
     * Method to extract the file name from the file path.
     * 
     * @param filePath
     *            the file path.
     * @return the file name with extension
     */
    public static String getFileNameFromFilePath( String filePath )
    {
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Filepath is null or empty "+filePath);
        }       
        logger.debug("System.getProperty( File.separator )"+System.getProperty("file.separator"));
        int dotPos = filePath.lastIndexOf( System.getProperty("file.separator") );
        String fileName = filePath.substring( dotPos + 1 );
        logger.debug( "File Name: " + fileName );
        return fileName;
    }

    /**
     * Method to change the extension of file to XML.
     * 
     * @param filePath
     *            The file path.
     * @return The file path with extension .xml
     */
    public static String changeExtensionToXML( String filePath )
    {
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("Filepath is null or empty "+filePath);
        }       
        int dotPos = filePath.lastIndexOf( "." );
        String filename = filePath.substring( 0, dotPos );
        String xmlfile = filename + "." + "xml";
        return xmlfile;
    }
}
