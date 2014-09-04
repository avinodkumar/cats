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

import static com.comcast.cats.vision.util.CatsVisionConstants.CLEAR_CURRENT_REGION;
import static com.comcast.cats.vision.util.CatsVisionConstants.DELETE_REGION;
import static com.comcast.cats.vision.util.CatsVisionConstants.IMAGE_COMPARE;
import static com.comcast.cats.vision.util.CatsVisionConstants.IMAGE_WINDOW;
import static com.comcast.cats.vision.util.CatsVisionConstants.LOAD_REGION;
import static com.comcast.cats.vision.util.CatsVisionConstants.LOAD_SNAPSHOT;
import static com.comcast.cats.vision.util.CatsVisionConstants.OCR;
import static com.comcast.cats.vision.util.CatsVisionConstants.OPTIONS;
import static com.comcast.cats.vision.util.CatsVisionConstants.SAVE_SNAPSHOT;
import static com.comcast.cats.vision.util.CatsVisionConstants.SAVE_SNAPSHOT_AS;
import static com.comcast.cats.vision.util.CatsVisionConstants.TEST_ALL_IMAGE_COMPARES;
import static com.comcast.cats.vision.util.CatsVisionConstants.TEST_CURRENT_REGION;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.comcast.cats.vision.panel.TabbedFrame;

/**
 * TabbedFrame for Image Comparison
 * 
 * @author aswathyann
 * 
 */
public class ImageCompareTabbedFrame extends TabbedFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 6621955049529342705L;
    private JMenu             menuImageCompare;
    private JMenu             menuNewRegion;
    private JMenuItem         menuLoadImage;
    private JMenuItem         menuSaveImage;
    private JMenuItem         menuSaveAsImage;
    private JMenu             menuLoadRegion;
    private JMenu             menuDeleteRegion;
    private JMenuItem         menuNewImageCompareRegion;
    private JMenuItem         menuNewOCRRegion;
    private JMenuItem         menuClearRegion;
    private JMenuItem         menuTestCurrentRegion;
    private JMenuItem         menuTestAllRegions;
    private JMenuBar          jMenuBar;

    public static void main( String[] args )
    {
        final String IC_DIALOG_NAME = "imageWindow";
        final Dimension DIMENSION = new Dimension( 1090, 780 );
        ImageCompareTabbedFrame icTabbedFrame = new ImageCompareTabbedFrame( IMAGE_WINDOW, IC_DIALOG_NAME, DIMENSION );
        icTabbedFrame.addTab( "macID", new JPanel() );

    }

    public ImageCompareTabbedFrame( String title, String name, Dimension dimension )
    {
        super( title, name, dimension );
        initView();
    }

    /**
     * Method to set the image on ImageComparePanel.
     * 
     * @param imageComparePanel
     *            ImageComparePanel
     * @param bi
     *            BufferedImage
     */
    public void setImageOnPanel( ImageComparePanel imageComparePanel, BufferedImage bi )
    {
        RegionDetailsPanel regionDetailsPanel = imageComparePanel.getRegionDetailsPanel();
        FreezeVideoPanel freezeVideoPanel = imageComparePanel.getFreezeVideoPanel();

        freezeVideoPanel.setImage( bi );
        regionDetailsPanel.clearAllRegions();
        regionDetailsPanel.clearCurrentRegionDetails();
        freezeVideoPanel.clearRegion();

        selectTab(imageComparePanel);
    }

    private void initView()
    {

        jMenuBar = new JMenuBar();
        setJMenuBar( jMenuBar );

        menuImageCompare = new JMenu( OPTIONS );
        menuImageCompare.setMnemonic( KeyEvent.VK_I );
        jMenuBar.add( menuImageCompare );

        menuLoadImage = new JMenuItem( LOAD_SNAPSHOT );
        menuLoadImage.setMnemonic( KeyEvent.VK_L );
        menuImageCompare.add( menuLoadImage );

        menuImageCompare.addSeparator();

        menuSaveImage = new JMenuItem( SAVE_SNAPSHOT );
        menuSaveImage.setEnabled( false );
        menuSaveImage.setMnemonic( KeyEvent.VK_S );

        menuImageCompare.add( menuSaveImage );

        menuSaveAsImage = new JMenuItem( SAVE_SNAPSHOT_AS );
        menuSaveAsImage.setMnemonic( KeyEvent.VK_A );
        menuSaveAsImage.setEnabled( false );

        menuImageCompare.add( menuSaveAsImage );
        menuImageCompare.addSeparator();

        menuNewRegion = new JMenu( "New Region" );
        menuNewRegion.setMnemonic( KeyEvent.VK_R );
        menuImageCompare.add( menuNewRegion );

        menuNewImageCompareRegion = new JMenuItem( IMAGE_COMPARE );

        menuNewRegion.add( menuNewImageCompareRegion );

        menuNewOCRRegion = new JMenuItem( OCR );
        menuNewRegion.add( menuNewOCRRegion );

        menuLoadRegion = new JMenu( LOAD_REGION );
        menuLoadRegion.setEnabled( false );

        menuImageCompare.add( menuLoadRegion );

        menuDeleteRegion = new JMenu( DELETE_REGION );
        menuDeleteRegion.setEnabled( false );

        menuImageCompare.add( menuDeleteRegion );

        menuImageCompare.addSeparator();

        menuClearRegion = new JMenuItem( CLEAR_CURRENT_REGION );
        menuClearRegion.setEnabled( false );
        menuClearRegion.setMnemonic( KeyEvent.VK_C );
        menuClearRegion.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.ALT_MASK ) );

        menuImageCompare.add( menuClearRegion );

        menuTestCurrentRegion = new JMenuItem( TEST_CURRENT_REGION );
        menuTestCurrentRegion.setEnabled( false );
        menuTestCurrentRegion.setMnemonic( KeyEvent.VK_T );
        menuTestCurrentRegion.setDisplayedMnemonicIndex( 0 );
        menuTestCurrentRegion.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_T, ActionEvent.ALT_MASK ) );

        menuImageCompare.add( menuTestCurrentRegion );

        menuTestAllRegions = new JMenuItem( TEST_ALL_IMAGE_COMPARES );
        menuTestAllRegions.setEnabled( false );
        menuTestAllRegions.setMnemonic( KeyEvent.VK_A );
        menuTestAllRegions.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.ALT_MASK ) );

        menuImageCompare.add( menuTestAllRegions );

    }

    public JMenu getMenuImageCompare()
    {
        return menuImageCompare;
    }

    public void setMenuImageCompare( JMenu menuImageCompare )
    {
        this.menuImageCompare = menuImageCompare;
    }

    public JMenu getMenuNewRegion()
    {
        return menuNewRegion;
    }

    public void setMenuNewRegion( JMenu menuNewRegion )
    {
        this.menuNewRegion = menuNewRegion;
    }

    public JMenuItem getMenuLoadImage()
    {
        return menuLoadImage;
    }

    public void setMenuLoadImage( JMenuItem menuLoadImage )
    {
        this.menuLoadImage = menuLoadImage;
    }

    public JMenuItem getMenuSaveImage()
    {
        return menuSaveImage;
    }

    public void setMenuSaveImage( JMenuItem menuSaveImage )
    {
        this.menuSaveImage = menuSaveImage;
    }

    public JMenuItem getMenuSaveAsImage()
    {
        return menuSaveAsImage;
    }

    public void setMenuSaveAsImage( JMenuItem menuSaveAsImage )
    {
        this.menuSaveAsImage = menuSaveAsImage;
    }

    public JMenu getMenuLoadRegion()
    {
        return menuLoadRegion;
    }

    public void setMenuLoadRegion( JMenu menuLoadRegion )
    {
        this.menuLoadRegion = menuLoadRegion;
    }

    public JMenu getMenuDeleteRegion()
    {
        return menuDeleteRegion;
    }

    public void setMenuDeleteRegion( JMenu menuDeleteRegion )
    {
        this.menuDeleteRegion = menuDeleteRegion;
    }

    public JMenuItem getMenuNewImageCompareRegion()
    {
        return menuNewImageCompareRegion;
    }

    public void setMenuNewImageCompareRegion( JMenuItem menuNewImageCompareRegion )
    {
        this.menuNewImageCompareRegion = menuNewImageCompareRegion;
    }

    public JMenuItem getMenuNewOCRRegion()
    {
        return menuNewOCRRegion;
    }

    public void setMenuNewOCRRegion( JMenuItem menuNewOCRRegion )
    {
        this.menuNewOCRRegion = menuNewOCRRegion;
    }

    public JMenuItem getMenuClearRegion()
    {
        return menuClearRegion;
    }

    public void setMenuClearRegion( JMenuItem menuClearRegion )
    {
        this.menuClearRegion = menuClearRegion;
    }

    public JMenuItem getMenuTestCurrentRegion()
    {
        return menuTestCurrentRegion;
    }

    public void setMenuTestCurrentRegion( JMenuItem menuTestCurrentRegion )
    {
        this.menuTestCurrentRegion = menuTestCurrentRegion;
    }

    public JMenuItem getMenuTestAllRegions()
    {
        return menuTestAllRegions;
    }

    public void setMenuTestAllRegions( JMenuItem menuTestAllRegions )
    {
        this.menuTestAllRegions = menuTestAllRegions;
    }

    public JMenuBar getJmenuBar()
    {
        return jMenuBar;
    }

    public void setJmenuBar( JMenuBar jMenuBar )
    {
        this.jMenuBar = jMenuBar;
    }

    public void addActionListener( ActionListener listener )
    {
        menuLoadImage.addActionListener( listener );
        menuSaveImage.addActionListener( listener );
        menuSaveAsImage.addActionListener( listener );
        menuNewImageCompareRegion.addActionListener( listener );
        menuNewOCRRegion.addActionListener( listener );
        menuClearRegion.addActionListener( listener );
        menuTestCurrentRegion.addActionListener( listener );
        menuTestAllRegions.addActionListener( listener );
    }

    public void addMouseListener( MouseListener listener )
    {
        menuLoadRegion.addMouseListener( listener );
        menuDeleteRegion.addMouseListener( listener );
    }

    public void removeActionListener( ActionListener listener )
    {
        menuLoadImage.removeActionListener( listener );
        menuSaveImage.removeActionListener( listener );
        menuSaveAsImage.removeActionListener( listener );
        menuNewImageCompareRegion.removeActionListener( listener );
        menuNewOCRRegion.removeActionListener( listener );
        menuClearRegion.removeActionListener( listener );
        menuTestCurrentRegion.removeActionListener( listener );
        menuTestAllRegions.removeActionListener( listener );
    }

    public void removeMouseListener( MouseListener listener )
    {
        menuLoadRegion.removeMouseListener( listener );
        menuDeleteRegion.removeMouseListener( listener );
    }
}
