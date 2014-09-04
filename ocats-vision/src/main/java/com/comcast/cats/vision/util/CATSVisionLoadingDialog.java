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
package com.comcast.cats.vision.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CATSVisionLoadingDialog
{

    private static final long serialVersionUID = 1L;
    private JDialog loadingDialog;
    
    private static Color BACKGROUND_COLOR = new Color(255,225,132);
    private static Dimension DEFAULT_SIZE = new Dimension(200, 35);
    private static String DEFAULT_STRING = "Launching CATSVision...";
    
    private static CATSVisionLoadingDialog instance = new CATSVisionLoadingDialog();
    
    private CATSVisionLoadingDialog(){
        initComponents();
    }
    
    public static synchronized CATSVisionLoadingDialog getInstance(){
        return instance;
    }
        
    
    private void initComponents(){
        loadingDialog = new JDialog();
        
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        loadingDialog.setSize( DEFAULT_SIZE);
        loadingDialog.setLocation((screenDimension.width/2)-(DEFAULT_SIZE.width/2),
                            (screenDimension.height/2)-(DEFAULT_SIZE.height/2));
        loadingDialog.setUndecorated( true );

        JPanel panel = new JPanel();
        JLabel label = new JLabel(DEFAULT_STRING);
        panel.add(label);

        panel.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createEtchedBorder() ));
        panel.setBackground( BACKGROUND_COLOR ); //new Color(220,252,88) new Color(255,225,132)
        loadingDialog.add(panel);
    }
    
    public JDialog getLoadingDialog()
    {
        return loadingDialog;
    }


    public static void showDialog(){
        CATSVisionLoadingDialog.getInstance().getLoadingDialog().setVisible( true );
    }
    
    public static void hideDialog(){
        CATSVisionLoadingDialog.getInstance().getLoadingDialog().setVisible( false );
    }
}
