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
import static com.comcast.cats.vision.util.CatsVisionConstants.SAVE_SNAPSHOT;
import static com.comcast.cats.vision.util.CatsVisionConstants.SAVE_SNAPSHOT_AS;
import static com.comcast.cats.vision.util.CatsVisionConstants.SETTOP;
import static com.comcast.cats.vision.util.CatsVisionConstants.TEST_ALL_IMAGE_COMPARES;
import static com.comcast.cats.vision.util.CatsVisionConstants.TEST_CURRENT_REGION;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.image.ImageCompareRegionInfo;
import com.comcast.cats.image.OCRCompareResult;
import com.comcast.cats.image.OCRRegionInfo;
import com.comcast.cats.image.RegionInfo;
import com.comcast.cats.provider.BaseProvider;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.OCRProvider;
import com.comcast.cats.provider.RegionLocatorProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.exceptions.ImageCompareException;
import com.comcast.cats.provider.exceptions.OCRException;
import com.comcast.cats.provider.impl.RegionLocatorProviderImpl;
import com.comcast.cats.vision.panel.video.ResolutionType;
import com.comcast.cats.vision.util.CatsVisionUtils;
import com.comcast.cats.vision.util.PanelAndProviders;
import com.comcast.cats.vision.util.ProviderType;

/**
 * The controller class for the ImageCompareWindow.
 * 
 * @author sajayjk
 * 
 */
@Named
public class ImageController implements ActionListener, MouseListener, WindowListener
{
    private static final Logger              logger                    = Logger.getLogger( ImageController.class );

    private RegionLocatorProvider            regionLocatorProviderImpl;

    private static final String              IC_DIALOG_NAME            = "imageWindow";
    private static final Dimension           DIMENSION                 = new Dimension( 1090, 780 );
    private static final Point               POINT                     = new Point( 50, 50 );
    private static final Rectangle           BOUNDS                    = new Rectangle( POINT, DIMENSION );

    private Map< String, PanelAndProviders > macIdPanelAndProvidersMap = new LinkedHashMap< String, PanelAndProviders >();

    private ImageCompareTabbedFrame          icTabbedFrame;
    // TODO make this configurable
    private final static int                 IC_TIMEOUT_MS             = 5000;

    private CatsEventDispatcher              catsEventDispatcher;

    @Inject
    public ImageController( CatsEventDispatcher catsEventDispatcher )
    {
        this.catsEventDispatcher = catsEventDispatcher;
        regionLocatorProviderImpl = new RegionLocatorProviderImpl();
    }

    public void addImageCompareTab( Settop settop )
    {
        if ( icTabbedFrame == null )
        {
            logger.debug( "Creating ImageCompareTab" );
            icTabbedFrame = new ImageCompareTabbedFrame( IMAGE_WINDOW, IC_DIALOG_NAME, DIMENSION );
            icTabbedFrame.addWindowListener( this );
            icTabbedFrame.setBounds( BOUNDS );
            addImageComparePanelListeners();
        }
        String macID = settop.getHostMacAddress();
        VideoProvider videoProvider = settop.getVideo();

        PanelAndProviders panelProviders = macIdPanelAndProvidersMap.get( macID );
        if ( null != panelProviders )
        {
            logger.debug( "Replacing old video image with new" );
            ImageComparePanel imgComparePanel = ( ImageComparePanel ) panelProviders.getPanel();
            icTabbedFrame.setImageOnPanel( imgComparePanel, videoProvider.getVideoImage() );
        }
        else
        {
            logger.debug( "Creating new ImageComparePanel for settop- " + settop.getHostMacAddress() );
            ImageComparePanel panel = createImageComparePanel( settop );

            Map< ProviderType, BaseProvider > providers = new LinkedHashMap< ProviderType, BaseProvider >();

            OCRProvider ocrProvider = settop.getOCRProvider();

            providers.put( ProviderType.OCR, ocrProvider );
            providers.put( ProviderType.IMAGE_COMPARE, settop.getImageCompareProvider() );
            providers.put( ProviderType.VIDEO, videoProvider );

            if ( null != ocrProvider )
            {
                ocrProvider.setVideoURL( videoProvider.getVideoURL() );
            }

            PanelAndProviders panelAndProviders = new PanelAndProviders( panel, providers );

            macIdPanelAndProvidersMap.put( macID, panelAndProviders );
            icTabbedFrame.addTab( macID, panel );
        }
    }

    public ImageComparePanel createImageComparePanel( Settop settop )
    {
        RegionDetailsPanel regionDetailsPanel = new RegionDetailsPanel();

        FreezeVideoPanel freezeVideoPanel = new FreezeVideoPanel( settop.getVideo().getVideoImage() );
        ImageCompareInfoPanel icInfoPanel = new ImageCompareInfoPanel();
        ImageComparePanel imageComparePanel = new ImageComparePanel( settop.getHostMacAddress(), regionDetailsPanel,
                freezeVideoPanel, icInfoPanel );
        return imageComparePanel;
    }

    private void addImageComparePanelListeners()
    {
        icTabbedFrame.addActionListener( this );
        icTabbedFrame.addMouseListener( this );
    }

    private void removeImageComparePanelListeners()
    {
        icTabbedFrame.removeActionListener( this );
        icTabbedFrame.removeMouseListener( this );
    }

    public void removeImageTab( String mac )
    {
        if ( macIdPanelAndProvidersMap.containsKey( mac ) )
        {
            logger.debug( "Removing Audio Tab with title -" + SETTOP + mac );

            // Remove ImageCompare tab
            icTabbedFrame.removeTab( mac );

            macIdPanelAndProvidersMap.remove( mac );

            if ( macIdPanelAndProvidersMap.isEmpty() )
            {
                // Remove ImageCompare frame
                removeImageCompareFrame();
            }
        }
    }

    public void removeImageCompareFrame()
    {
        removeImageComparePanelListeners();
        macIdPanelAndProvidersMap.clear();
        icTabbedFrame.setVisible( false );
        icTabbedFrame = null;

    }

    @Override
    public void actionPerformed( ActionEvent event )
    {

        ImageComparePanel imageWindow = getImageComparePanel();

        Object soucre = event.getSource();

        if ( soucre instanceof JMenuItem )
        {

            JMenuItem menuItem = ( JMenuItem ) soucre;

            String text = menuItem.getText();

            if ( text.equals( LOAD_SNAPSHOT ) )
            {
                loadImage( imageWindow );

            }
            else if ( text.equals( SAVE_SNAPSHOT ) )
            {
                save( imageWindow );
            }
            else if ( text.equals( SAVE_SNAPSHOT_AS ) )
            {
                saveAs( imageWindow );
            }
            else if ( text.equals( IMAGE_COMPARE ) )
            {
                createNewRegion( imageWindow, RegionDetailsPanel.IC_MODE );
            }
            else if ( text.equals( OCR ) )
            {
                createNewRegion( imageWindow, RegionDetailsPanel.OCR_MODE );
            }
            else if ( text.equals( CLEAR_CURRENT_REGION ) )
            {
                clearRegion( imageWindow );
            }
            else if ( text.equals( TEST_CURRENT_REGION ) )
            {
                testCurrentRegion( imageWindow );
            }
            else if ( text.equals( TEST_ALL_IMAGE_COMPARES ) )
            {
                testAllRegion( imageWindow );
            }
        }
    }

    private ImageComparePanel getImageComparePanel()
    {
        JTabbedPane icTabbedPane = icTabbedFrame.getTabbedPane();

        int tabIndex = icTabbedPane.getSelectedIndex();
        Component component = icTabbedPane.getComponent( tabIndex );
        ImageComparePanel imageComparePanel = ( ImageComparePanel ) component;

        return imageComparePanel;

    }

    /**
     * LoadImage Action for the menuLoadImage. Loads an image and its details to
     * IC CATS Vision.
     */
    private void loadImage( ImageComparePanel icPanel )
    {
        // TODO: check if existing region needs to be saved first.
        int retVal = icPanel.getRegionFileLoader().showOpenDialog( icPanel );
        if ( JFileChooser.APPROVE_OPTION == retVal )
        {
            String xmlPath = icPanel.getRegionFileLoader().getSelectedFile().getAbsolutePath();
            icPanel.getRegionDetailsPanel().resetRegionDetails();
            icPanel.getFreezeVideoPanel().clearRegion();

            try
            {
                loadImage( icPanel, xmlPath );
            }
            catch ( IOException exception )
            {
                logger.error( exception.getMessage() );
                CatsVisionUtils.showImageCompareDialog(
                        "There is a problem with the xml file you have selected. Cannot load.",
                        JOptionPane.ERROR_MESSAGE );
                return;
            }
            catch ( IllegalArgumentException exception )
            { // if illegal xml was chosen
                logger.error( exception.getMessage() );
                CatsVisionUtils.showImageCompareDialog( "The selected xml file is not appropriate.",
                        JOptionPane.ERROR_MESSAGE );
                return;
            }
            enableMenus();
            icPanel.getIcInfoPanel().setVisible( false );
            icPanel.getRegionDetailsPanel().setVisible( true );
            /*
             * connect the image with the region data.
             */
            icPanel.getFreezeVideoPanel().registerDetailsPanel( icPanel.getRegionDetailsPanel() );
            icPanel.getRegionFileSaver().setSelectedFile( icPanel.getRegionFileLoader().getSelectedFile() );
            /*
             * to provide Save functionality.
             */
            icPanel.getRegionDetailsPanel().setSnapshotFilepath( ImageCompareUtil.changeExtensionToJPG( xmlPath ) );
        }
    }

    /**
     * Test All Regions Action.
     * 
     * currently supports only IC. OCR not supported.
     */
    private void testAllRegion( ImageComparePanel icPanel )
    {
        disableMenus();
        List< RegionInfo > regionsList = icPanel.getRegionDetailsPanel().getRegionsList();
        List< RegionInfo > icRegions = new ArrayList< RegionInfo >();
        for ( RegionInfo regionInfo : regionsList )
        {
            if ( regionInfo instanceof ImageCompareRegionInfo )
            {
                icRegions.add( ( ImageCompareRegionInfo ) regionInfo );
            }
        }
        boolean doesICRegionExist = false;
        if ( !icRegions.isEmpty() )
        {
            doesICRegionExist = true;
            Thread t = new Thread( new ImageCompareThread( icRegions ) );
            t.start(); // starting a new thread to avoid blocking AWT's
            // thread.
        }
        if ( doesICRegionExist == false )
        {
            enableMenus();
            CatsVisionUtils.showInfo( "Test All Regions", "There was no Image Compare Regions to test" );
        }
    }

    /**
     * Test Current Region
     */
    private void testCurrentRegion( ImageComparePanel icPanel )
    {
        disableMenus();
        // starting a new thread to avoid blocking AWT's
        // thread.
        Runnable compareJob;
        if ( icPanel.getRegionDetailsPanel().getMode() == RegionDetailsPanel.IC_MODE )
        {
            compareJob = new ImageCompareThread( icPanel.getRegionDetailsPanel().getCurrentRegion() );
        }
        else
        {
            compareJob = new OCRCompareThread( icPanel.getRegionDetailsPanel().getCurrentRegion() );
        }
        Thread t = new Thread( compareJob );
        t.start();
    }

    /**
     * Clear Region Action
     */
    private void clearRegion( ImageComparePanel icPanel )
    {
        icPanel.getFreezeVideoPanel().clearRegion();
    }

    /**
     * Actual ImageCompare Region and image loading happens here.
     * 
     * @param xmlPath
     * @throws IOException
     */
    private void loadImage( ImageComparePanel icPanel, String xmlPath ) throws IOException
    {
        List< RegionInfo > regionsList = regionLocatorProviderImpl.getRegionInfo( xmlPath );
        if ( null == regionsList || regionsList.size() <= 0 )
        {
            CatsVisionUtils.showImageCompareDialog( "No Regions available for this image",
                    JOptionPane.INFORMATION_MESSAGE );
        }
        String imagePath = ImageCompareUtil.changeExtensionToJPG( xmlPath );
        BufferedImage image = ImageCompareUtil.loadImageFromFile( imagePath );
        if ( null == image )
        {
            CatsVisionUtils.showImageCompareDialog( "Image cannot be found for the selected snapshot xml",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }
        icPanel.setSnapImageSize( new Dimension( image.getWidth(), image.getHeight() ) );

        icPanel.setCurrentImage( image, regionsList );
    }

    /**
     * Create new Region Action.
     */
    private void createNewRegion( ImageComparePanel icPanel, int mode )
    {

        icPanel.getRegionDetailsPanel().setMode( mode );
        if ( icPanel.getRegionDetailsPanel().isVisible() )
        { // if it already exists; just clear it to get new data.

            icPanel.getRegionDetailsPanel().clearCurrentRegionDetails();
        }
        else
        {
            icPanel.getIcInfoPanel().setVisible( false ); // hide the first
            // information panel.
            icPanel.getRegionDetailsPanel().setVisible( true );
            icPanel.getFreezeVideoPanel().registerDetailsPanel( icPanel.getRegionDetailsPanel() );
            enableMenus();
        }

    }

    /**
     * Enables all disabled menues after region is loaded or new region is
     * selected.
     */
    private void enableMenus()
    {
        icTabbedFrame.getMenuSaveImage().setEnabled( true );
        icTabbedFrame.getMenuSaveAsImage().setEnabled( true );
        icTabbedFrame.getMenuLoadRegion().setEnabled( true );
        icTabbedFrame.getMenuDeleteRegion().setEnabled( true );
        icTabbedFrame.getMenuClearRegion().setEnabled( true );
        icTabbedFrame.getMenuTestCurrentRegion().setEnabled( true );
        icTabbedFrame.getMenuTestAllRegions().setEnabled( true );
        icTabbedFrame.getMenuLoadImage().setEnabled( true );
        icTabbedFrame.getMenuNewImageCompareRegion().setEnabled( true );
        icTabbedFrame.getMenuNewRegion().setEnabled( true );
    }

    /**
     * Disables all menues while testing occurs. selected.
     */
    private void disableMenus()
    {
        icTabbedFrame.getMenuSaveImage().setEnabled( false );
        icTabbedFrame.getMenuSaveAsImage().setEnabled( false );
        icTabbedFrame.getMenuLoadRegion().setEnabled( false );
        icTabbedFrame.getMenuDeleteRegion().setEnabled( false );
        icTabbedFrame.getMenuClearRegion().setEnabled( false );
        icTabbedFrame.getMenuTestCurrentRegion().setEnabled( false );
        icTabbedFrame.getMenuTestAllRegions().setEnabled( false );
        icTabbedFrame.getMenuLoadImage().setEnabled( false );
        icTabbedFrame.getMenuNewImageCompareRegion().setEnabled( false );
        icTabbedFrame.getMenuNewRegion().setEnabled( false );
    }

    /**
     * Save the current image and regions to disk.
     */
    private void save( ImageComparePanel icPanel )
    {
        String filePath = icPanel.getRegionDetailsPanel().getSnapshotFilepath();
        if ( filePath != null )
        {
            try
            {
                RegionLocatorProvider regionLocatorProvider = regionLocatorProviderImpl;
                regionLocatorProvider.saveImageAndRegion( icPanel.getRegionDetailsPanel().getRegionsList(), icPanel
                        .getFreezeVideoPanel().getSnapshot(), filePath );
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage() );
                JOptionPane.showMessageDialog( icPanel, "An error occured while saving: "
                        + icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath(), "Save Failed",
                        JOptionPane.ERROR_MESSAGE );
            }
            catch ( JAXBException e )
            {
                logger.error( e.getMessage() );
                JOptionPane.showMessageDialog( icPanel, "An error occured while saving: "
                        + icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath() + "\n" + e.getMessage(),
                        "Save Failed", JOptionPane.ERROR_MESSAGE );
            }
        }
        else
        {
            saveAs( icPanel );
        }
    }

    /**
     * Save the current image and regions to disk.
     * 
     */
    private void saveAs( ImageComparePanel icPanel)
    {
        // if "Save AS" or file not saved before
        int retVal = icPanel.getRegionFileSaver().showSaveDialog( icPanel );
        if ( retVal == JFileChooser.APPROVE_OPTION )
        {

            String fileName = icPanel.getRegionFileSaver().getSelectedFile().getName();

            if ( fileName.endsWith( ".xml" ) )
            {
                fileName = icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath().substring( 0,
                        fileName.lastIndexOf( ".xml" ) );
            }
            fileName = icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath().concat( ".jpg" );

            try
            {
                RegionLocatorProvider regionLocatorProvider = regionLocatorProviderImpl;

                regionLocatorProvider.saveImageAndRegion( icPanel.getRegionDetailsPanel().getRegionsList(), icPanel
                        .getFreezeVideoPanel().getSnapshot(), fileName );

                icPanel.getRegionDetailsPanel().setSnapshotFilepath( fileName );
            }
            catch ( IOException e )
            {
                JOptionPane.showMessageDialog( icPanel, "An error occured while saving: "
                        + icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath(), "Save Failed",
                        JOptionPane.ERROR_MESSAGE );
            }
            catch ( JAXBException e )
            {
                logger.error( e.getMessage() );
                JOptionPane.showMessageDialog( icPanel, "An error occured while saving: "
                        + icPanel.getRegionFileSaver().getSelectedFile().getAbsolutePath() + "\n" + e.getMessage(),
                        "Save Failed", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    @Override
    public void mouseClicked( MouseEvent event )
    {
        processEvent( event );

    }

    @Override
    public void mouseEntered( MouseEvent event )
    {
        processEvent( event );
    }

    @Override
    public void mouseExited( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    /**
     * Start video mode; The panel will run the current video.
     */
    public void startVideo( FreezeVideoPanel freezeVideoPanel, VideoProvider videoProvider )
    {
        /*
         * add listener to receive video events
         */
        catsEventDispatcher.addListener( freezeVideoPanel, CatsEventType.VIDEO, videoProvider );
        freezeVideoPanel.startVideoMode();

    }

    /**
     * Stop video.
     */
    public void stopVideo( FreezeVideoPanel freezeVideoPanel, VideoProvider videoProvider )
    {
        catsEventDispatcher.removeListener( freezeVideoPanel );
        freezeVideoPanel.stopVideoMode();
    }

    private void processEvent( MouseEvent event )
    {
        Object soucre = event.getSource();

        if ( soucre instanceof JMenuItem )
        {
            JMenuItem menuItem = ( JMenuItem ) soucre;
            String text = menuItem.getText();
            if ( text.equals( LOAD_REGION ) )
            {
                /*
                 * remove any existing menu items
                 */
                icTabbedFrame.getMenuLoadRegion().removeAll();

                final ImageComparePanel icPanel = getImageComparePanel();

                List< RegionInfo > regionList = icPanel.getRegionDetailsPanel().getRegionsList();
                for ( RegionInfo regionInfo : regionList )
                {
                    JMenuItem regionMenuItem = new JMenuItem( regionInfo.getName() );
                    // add the latest set of regions
                    icTabbedFrame.getMenuLoadRegion().add( regionMenuItem );

                    regionMenuItem.addActionListener( new ActionListener()
                    { // on Clicking a region; load that to the window.
                                @Override
                                public void actionPerformed( ActionEvent actionEvent )
                                {
                                    List< RegionInfo > regionList = icPanel.getRegionDetailsPanel().getRegionsList();
                                    for ( RegionInfo regionInfo : regionList )
                                    {
                                        if ( regionInfo.getName().equals(
                                                ( ( JMenuItem ) actionEvent.getSource() ).getText() ) )
                                        {
                                            icPanel.getFreezeVideoPanel().paintRegion( regionInfo );
                                            icPanel.getRegionDetailsPanel().loadRegionDetails( regionInfo );
                                        }
                                    }
                                }
                            } );
                }

            }
            else if ( text.equals( DELETE_REGION ) )
            {
                /*
                 * remove any existing menu items
                 */
                icTabbedFrame.getMenuDeleteRegion().removeAll();
                final ImageComparePanel icPanel = getImageComparePanel();

                List< RegionInfo > regionList = icPanel.getRegionDetailsPanel().getRegionsList();
                for ( RegionInfo regionInfo : regionList )
                {
                    JMenuItem regionMenuItem = new JMenuItem( regionInfo.getName() );
                    icTabbedFrame.getMenuDeleteRegion().add( regionMenuItem ); // add
                    // the
                    // latest
                    // set of regions

                    regionMenuItem.addActionListener( new ActionListener()
                    {
                        @Override
                        public void actionPerformed( ActionEvent actionEvent )
                        { // on Clicking a region; delete that region.
                            List< RegionInfo > regionList = icPanel.getRegionDetailsPanel().getRegionsList();
                            for ( RegionInfo regionInfo : regionList )
                            {
                                if ( regionInfo.getName().equals( ( ( JMenuItem ) actionEvent.getSource() ).getText() ) )
                                {
                                    regionList.remove( regionInfo );
                                    if ( icPanel.getRegionDetailsPanel().getCurrentRegion() != null
                                            && regionInfo.getName().equals(
                                                    icPanel.getRegionDetailsPanel().getCurrentRegion().getName() ) )
                                    {
                                        icPanel.getRegionDetailsPanel().clearCurrentRegionDetails();
                                        icPanel.getFreezeVideoPanel().clearRegion();
                                    }
                                    break;
                                }
                            }
                            icPanel.getRegionDetailsPanel().setRegionsList( regionList ); // update
                            // the
                            // current
                            // list
                        }
                    } );
                }

            }
        }
    }

    /**
     * Thread class that takes on the responsibility of actual comparision.
     * Else, the AWT thread will be blocked and unavailable for painting,
     * keypress etc.
     * 
     * @author sajayjk
     */
    class ImageCompareThread implements Runnable
    {
        RegionInfo         regionInfo     = null;
        List< RegionInfo > regionInfoList = null;

        public ImageCompareThread( RegionInfo regionInfo )
        {
            this.regionInfo = regionInfo;
        }

        public ImageCompareThread( List< RegionInfo > regionInfoList )
        {
            this.regionInfoList = ( ArrayList< RegionInfo > ) regionInfoList;
        }

        @Override
        public void run()
        {
            long startTime = 0;
            long endTime = 0;
            boolean testResult = false;
            final ImageComparePanel icPanel = getImageComparePanel();

            icPanel.getRegionDetailsPanel().showTestMode();

            PanelAndProviders panelAndProviders = macIdPanelAndProvidersMap.get( icPanel.getName() );

            ImageCompareProvider icProvider = ( ImageCompareProvider ) panelAndProviders
                    .getProvider( ProviderType.IMAGE_COMPARE );

            VideoProvider videoProvider = ( VideoProvider ) panelAndProviders.getProvider( ProviderType.VIDEO );

            BufferedImage refImage = icPanel.getFreezeVideoPanel().getSnapshot();
            try
            {
                if ( regionInfo != null && 0 != regionInfo.getWidth() && 0 != regionInfo.getHeight() )
                {
                    ArrayList< RegionInfo > list = new ArrayList< RegionInfo >();
                    list.add( regionInfo );
                    logger.debug( "Testing Region " + regionInfo );
                    icPanel.getFreezeVideoPanel().paintRegions( list );

                    // show test mode in all chils panels
                    // icPanel.getFreezeVideoPanel().startVideoMode();
                    startVideo( icPanel.getFreezeVideoPanel(), videoProvider );
                    startTime = System.currentTimeMillis();
//                    testResult = icProvider.waitForRegion( ( ImageCompareRegionInfo ) regionInfo, refImage,
//                            IC_TIMEOUT_MS );
                    endTime = System.currentTimeMillis();

                    logger.debug( "testResult " + testResult );
                }
                else if ( regionInfoList != null && regionInfoList.size() != 0 )
                {
                    // show test mode in all chils panels
                    // icPanel.getFreezeVideoPanel().startVideoMode();
                    startVideo( icPanel.getFreezeVideoPanel(), videoProvider );
                    logger.debug( "Testing All Regions" );

                    List< ImageCompareRegionInfo > icRegions = new ArrayList< ImageCompareRegionInfo >();
                    for ( RegionInfo regionInfo : regionInfoList )
                    {
                        icRegions.add( ( ImageCompareRegionInfo ) regionInfo );
                    }

                    icPanel.getFreezeVideoPanel().paintRegions( regionInfoList );
                    startTime = System.currentTimeMillis();
                    testResult = icProvider.waitForAllRegions( icRegions, refImage, IC_TIMEOUT_MS );
                    endTime = System.currentTimeMillis();
                    logger.debug( "testResult " + testResult );
                }
                else
                {
                    logger.debug( "Full Screen Compare" );
                    icPanel.getFreezeVideoPanel().clearRegion();

                    logger.debug( "Full Screen Compare IC" );
                    // show test mode in all chils panels
                    // icPanel.getFreezeVideoPanel().startVideoMode();
                    startVideo( icPanel.getFreezeVideoPanel(), videoProvider );
                    startTime = System.currentTimeMillis();
                    testResult = icProvider.waitForImageOnScreen( refImage, IC_TIMEOUT_MS );
                    endTime = System.currentTimeMillis();

                    logger.debug( "testResult " + testResult );
                }
            }
            catch ( ImageCompareException e )
            {
                CatsVisionUtils.showError( e.getMessage() );
            }
            icPanel.getRegionDetailsPanel().updateTestResults( testResult, ( int ) ( endTime - startTime ) );
            // icPanel.getFreezeVideoPanel().stopVideoMode();
            stopVideo( icPanel.getFreezeVideoPanel(), videoProvider );
            enableMenus();
        }
    }

    /**
     * Thread class that takes on the responsibility of actual OCR comparison.
     * Else, the AWT thread will be blocked and unavailable for painting, key
     * press etc.
     * 
     * @author minujames
     */
    class OCRCompareThread implements Runnable
    {

        RegionInfo regionInfo = null;

        public OCRCompareThread( RegionInfo regionInfo )
        {
            this.regionInfo = regionInfo;
        }

        public void run()
        {
            long startTime = 0;
            long endTime = 0;
            boolean testResult = false;
            OCRCompareResult ocrResult = null;
            final ImageComparePanel icPanel = getImageComparePanel();

            icPanel.getRegionDetailsPanel().showTestMode();

            PanelAndProviders panelAndProviders = macIdPanelAndProvidersMap.get( icPanel.getName() );

            VideoProvider videoProvider = ( VideoProvider ) panelAndProviders.getProvider( ProviderType.VIDEO );
            OCRProvider ocrProvider = ( OCRProvider ) panelAndProviders.getProvider( ProviderType.OCR );

            if ( null != ocrProvider )
            {
                try
                {
                    if ( regionInfo != null && 0 != regionInfo.getWidth() && 0 != regionInfo.getHeight() )
                    {
                        ArrayList< RegionInfo > list = new ArrayList< RegionInfo >();
                        list.add( regionInfo );
                        logger.debug( "Testing Region " + regionInfo );
                        icPanel.getFreezeVideoPanel().paintRegions( list );

                        if ( isSnapshotResolutionValid( icPanel.getSnapImageSize(), ocrProvider, videoProvider ) )
                        {
                            // show test mode in all chills panels
                            // icPanel.getFreezeVideoPanel().startVideoMode();
                            startVideo( icPanel.getFreezeVideoPanel(), videoProvider );

                            startTime = System.currentTimeMillis();

//                            ocrResult = ocrProvider.waitForOCRRegionCompareResult( ( OCRRegionInfo ) regionInfo );
//                            testResult = ocrProvider.isOCRResultAccurate( ( OCRRegionInfo ) regionInfo, ocrResult );

                            endTime = System.currentTimeMillis();
                            // setMainVideoResolution( mainVideoSize );
                        }
                        logger.debug( "testResult " + testResult );
                    }
                    else
                    {
                        logger.debug( "Full Screen Compare OCR" );
                        icPanel.getFreezeVideoPanel().clearRegion();

                        if ( isSnapshotResolutionValid( icPanel.getSnapImageSize(), ocrProvider, videoProvider ) )
                        {
                            // show test mode in all chills panels
                            // icPanel.getFreezeVideoPanel().startVideoMode();
                            startVideo( icPanel.getFreezeVideoPanel(), videoProvider );

                            startTime = System.currentTimeMillis();

                            OCRRegionInfo currentRegion = ( OCRRegionInfo ) icPanel.getRegionDetailsPanel()
                                    .getCurrentRegion();
//                            ocrResult = ocrProvider.getOCRTextOnScreenNow( currentRegion );
//                            testResult = ocrProvider.isOCRResultAccurate( ( OCRRegionInfo ) regionInfo, ocrResult );

                            endTime = System.currentTimeMillis();
                            // setMainVideoResolution( mainVideoSize );
                        }
                        logger.debug( "testResult " + testResult );
                    }
                }
                catch ( Exception e )
                {
                    CatsVisionUtils.showError( e.getMessage() );
                }

                // icPanel.getFreezeVideoPanel().stopVideoMode();
                stopVideo( icPanel.getFreezeVideoPanel(), videoProvider );

                if ( testResult )
                {
                    CatsVisionUtils.showOCRResultOnSuccess( ocrResult );
                }
                else
                {
                    CatsVisionUtils.showOCRResultOnFailure( ocrResult );
                }
            }
            else
            {
                CatsVisionUtils
                        .showError( "Cannot perform OCR. OCR service is not available or \n the url is not configured in cats.props file as 'cats.ocr.server.url'." );
            }
            icPanel.getRegionDetailsPanel().updateTestResults( testResult, ( int ) ( endTime - startTime ) );

            enableMenus();
        }

        // TODO - is this method needed?
        private boolean isSnapshotResolutionValid( Dimension snapImageSize, OCRProvider ocrProvider,
                VideoProvider videoProvider )
        {
            boolean validResolution = true;

            Dimension mainVideoSize = videoProvider.getVideoSize();
            if ( mainVideoSize.width != snapImageSize.width || mainVideoSize.height != snapImageSize.height )
            {
                if ( ResolutionType.isResolutionValid( snapImageSize ) )
                {
                    // setMainVideoResolution( snapImageSize );
                    ocrProvider.setVideoURL( videoProvider.getVideoURL() );
                }
                else
                {
                    validResolution = false;
                    CatsVisionUtils
                            .showError( "Cannot Perform OCR. \n The resolution of the loaded image is not supported by the video server." );
                }
            }
            return validResolution;
        }
    }

    @Override
    public void windowActivated( WindowEvent e )
    {

    }

    @Override
    public void windowClosed( WindowEvent e )
    {

    }

    @Override
    public void windowClosing( WindowEvent e )
    {
        removeImageCompareFrame();
    }

    @Override
    public void windowDeactivated( WindowEvent e )
    {

    }

    @Override
    public void windowDeiconified( WindowEvent e )
    {

    }

    @Override
    public void windowIconified( WindowEvent e )
    {

    }

    @Override
    public void windowOpened( WindowEvent e )
    {

    }
}
