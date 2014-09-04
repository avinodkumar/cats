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
package com.comcast.cats.vision.panel;

import static com.comcast.cats.vision.util.CatsVisionConstants.DIAG_BUTTON_NAME;
import static com.comcast.cats.vision.util.CatsVisionConstants.PLEASE_ALLOCATE_MSG;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * 
 * Configurable Button Panel
 * 
 * @author liyaj
 * 
 */
public class ConfigurableButtonPanel extends JPanel implements ActionListener
{

    private static final long      serialVersionUID = 1L;
    private static final Font      DEFAULT_FONT     = new Font( "Arial", Font.BOLD, 10 );
    private static final Integer   BUTTON_HEIGHT    = 20;
    private static final Integer   BUTTON_WIDTH     = 120;
    private static final String    PANEL_NAME       = "DiagnosticPanel";
    private Dimension              BUTTON_DIMENSION = new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT );
    private JButton                configButton;
    private Set< Settop >          allocatedSettops = new LinkedHashSet< Settop >();
    private CatsEventDispatcher    dispatcherThreaded;
    private static final Dimension DIMENSION        = new Dimension( 275, 45 );

    private GridDataModel          gridDataModel;
    private static final Logger    logger           = Logger.getLogger( ConfigurableButtonPanel.class );

    private static final String    LOGO_URL         = "/images/cats-logo.png";

    /**
     * Constructor for GridConfigurableButtonPanel
     * 
     * @param gridDataModel
     *            instance of GridDataModel
     * @param dispatcher
     *            instance of CatsEventDispatcher
     */
    public ConfigurableButtonPanel( GridDataModel gridDataModel, CatsEventDispatcher dispatcher )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Creating ConfigurableButtonPanel (panel which hold DIAG SCREEN  button)." );
        }
        this.gridDataModel = gridDataModel;

        this.dispatcherThreaded = dispatcher;
        // addDiagButton();
       // addLogo();

        setPreferredSize( DIMENSION );

        setMinimumSize( DIMENSION );

        setSize( DIMENSION );

        setBorder( BorderFactory.createLineBorder( Color.BLACK, 1 ) );

        setBackground( Color.LIGHT_GRAY );

        setVisible( true );
    }

    private void addLogo()
    {
        JLabel logoLbl = new JLabel();

        Image img;
        try
        {
            img = ImageIO.read( getClass().getResource( LOGO_URL ) );
            Image resizedImage = img.getScaledInstance( BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_SMOOTH );
            ImageIcon icon = new ImageIcon( resizedImage );
            logoLbl.setIcon( icon );
            logoLbl.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        this.add( logoLbl );
    }

    private void addDiagButton()
    {
        configButton = new JButton();

        configButton.setPreferredSize( BUTTON_DIMENSION );

        configButton.setText( DIAG_BUTTON_NAME );

        configButton.setFont( DEFAULT_FONT );

        configButton.setVisible( true );

        configButton.addActionListener( this );

        configButton.setFocusable( false );

        configButton.setName( PANEL_NAME );

        this.add( configButton );

    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Diag Screen Button Pressed." );
        }

        // enterDiagnosticScreen();
    }

    /**
     * Enters the Diagnostic Screen. Based on the type of the box, while
     * clicking the DIAG SCREEN button on the panel, a set of keys sequences
     * will be executed and enters into the Diagnostic Screen.
     */
    private void enterDiagnosticScreen()
    {
        allocatedSettops = gridDataModel.getAllocatedAndSelectedSettops();

        if ( ( allocatedSettops != null ) && !( allocatedSettops.isEmpty() ) )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Inside GridConfigurableButtonPanel- enterDiagnosticScreen" );
            }
            dispatcherThreaded.sendCatsEvent( new ConfigButtonEvent( DIAG_BUTTON_NAME, "GridConfigurableButtonPanel",
                    this ) );
        }
        else
        {
            CatsVisionUtils.showWarning( "Unable to perform the operation", PLEASE_ALLOCATE_MSG
                    + " DIAG SCREEN  button." );
        }
    }

}
