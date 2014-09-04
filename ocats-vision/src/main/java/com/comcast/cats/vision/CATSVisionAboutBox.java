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
package com.comcast.cats.vision;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import com.comcast.cats.vision.components.JHyperlink;
import com.comcast.cats.vision.util.CatsVisionConstants;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * Simple about box. This will pull informations from MANIFEST.MF file.
 * 
 * @author ssugun00c
 * 
 */
public class CATSVisionAboutBox extends JDialog
{
    private static final long   serialVersionUID    = 1L;

    private static final String HOME_URL            = "OCATS";
    private static final String VERSION_TXT         = "Version : ";
    

    private static final int    PREFERRED_WIDTH     = 450;
    private static final int    PREFERRED_HEIGHT    = 200;
    private static final int    FONT_SIZE           = 11;

    private static final String DEFAULT_TITLE       = CatsVisionConstants.APPLICATION_TITLE;
    private static final String DEFAULT_VERSION     = "UNKNOWN";

    private JLabel              logoLbl;
    private JLabel              versionLabel;
    private JHyperlink          homepageLabel;

    public CATSVisionAboutBox( Frame parent )
    {
        super( parent );
        setTitle( getApplicationTitle() );
        initComponents( getContentPane() );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setModal( true );
        setMinimumSize( new Dimension( PREFERRED_WIDTH, PREFERRED_HEIGHT ) );
        setLocationRelativeTo( null );
        setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS ) );
        setResizable( false );

        setIconImage( CatsVisionUtils.getApplicationIcon() );

        pack();
    }

    private void initComponents( Container contentPane )
    {
        logoLbl = new JLabel();
        versionLabel = new JLabel( VERSION_TXT + getApplicationVersion() );
        homepageLabel = new JHyperlink( HOME_URL );
        homepageLabel.setUrl( HOME_URL );

        addSeparator( contentPane );

        logoLbl.setAlignmentX( Component.CENTER_ALIGNMENT );
        logoLbl.setIcon( new ImageIcon( getClass().getResource( CatsVisionConstants.APPLICATION_LOGO_URL ) ) );
        logoLbl.setBorder( BorderFactory.createRaisedBevelBorder() );
        contentPane.add( logoLbl );

        addSeparator( contentPane );

        versionLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
        contentPane.add( versionLabel );

        addSeparator( contentPane );


        addSeparator( contentPane );

        homepageLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
        homepageLabel.setFont( new Font( Font.DIALOG_INPUT, Font.PLAIN, FONT_SIZE ) );
        contentPane.add( homepageLabel );

        addSeparator( contentPane );
    }


    private String getApplicationTitle()
    {
        String title = DEFAULT_TITLE;

        if ( null != getClass().getPackage().getImplementationTitle() )
        {
            title = getClass().getPackage().getImplementationTitle().trim();
        }
        return title;
    }

    private String getApplicationVersion()
    {
        String version = DEFAULT_VERSION;

        if ( null != getClass().getPackage().getImplementationVersion() )
        {
            version = getClass().getPackage().getImplementationVersion().trim();
        }
        return version;
    }

    private void addSeparator( Container contentPane )
    {
        contentPane.add( new JSeparator( JSeparator.VERTICAL ), BorderLayout.LINE_START );
    }
}