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
package com.comcast.cats.vision.test.utils;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;

import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.Window;

public class CommonTestUtils
{
    /**
     * Allocated table headers
     */
    public static String[] ALLOCATED_TABLE_STRINGS =
                                                       {
            "Id",
            "Name",
            "MAC Address",
            "IPAddress",
            "Reservation Name",
            "Rack Name",
            "Environment Name",
            "Content",
            "Model"                                   };

    /**
     * Available table headers
     */
    public static String[] AVAILABLE_TABLE_STRINGS =
                                                       {
            "Id",
            "Name",
            "MAC Address",
            "IPAddress",
            "Reservation Name",
            "Rack Name",
            "Environment Name",
            "Content",
            "Model"                                   };

    public static boolean isLabelPresent( final Panel containerPanel, final String labelName, final String labelText )
    {
        boolean isLabelPresent = false;

        Component[] components = containerPanel.getSwingComponents( JLabel.class );

        for ( Component component : components )
        {
            if ( component.getName() == labelText )
            {
                JLabel label = ( JLabel ) component;

                isLabelPresent = label.getText().equals( labelText );
                if ( isLabelPresent )
                {
                    break;
                }
            }
        }
        return isLabelPresent;
    }

    public static Button getConfigPanelButton( final Window window, String buttonName )
    {
        TabGroup mainTabGroup = window.getTabGroup( "visionTabs" );

        mainTabGroup.selectTab( "My Settops" );

        JTabbedPane mainTabbedPane = mainTabGroup.getAwtComponent();

        mainTabbedPane.setSelectedIndex( 0 );

        JPanel configPanel = getConfigPanelFromTabbedPane( mainTabbedPane );

        JPanel configButtonPanel = ( JPanel ) configPanel.getComponent( 0 );

        Button configButton = null;

        for ( Component component : configButtonPanel.getComponents() )
        {
            if ( buttonName.equals( component.getName() ) )
            {
                JButton button = ( JButton ) component;

                configButton = new Button( button );

                break;
            }
        }
        return configButton;
    }

    public static JTextField getConfigPanelTextField( final Window window, String textFieldName )
    {
        JTextField textField = null;

        TabGroup mainTabGroup = window.getTabGroup( "visionTabs" );

        mainTabGroup.selectTab( "My Settops" );

        JTabbedPane mainTabbedPane = mainTabGroup.getAwtComponent();

        mainTabbedPane.setSelectedIndex( 0 );

        JPanel configPanel = getConfigPanelFromTabbedPane( mainTabbedPane );

        JPanel configButtonPanel = ( JPanel ) configPanel.getComponent( 0 );

        for ( Component component : configButtonPanel.getComponents() )
        {
            if ( textFieldName.equals( component.getName() ) )
            {
                textField = ( JTextField ) component;
                break;
            }
        }
        return textField;
    }

    public static JPanel getConfigPanel( final Window window )
    {
        TabGroup mainTabGroup = window.getTabGroup( "visionTabs" );

        mainTabGroup.selectTab( "My Settops" );

        JTabbedPane mainTabbedPane = mainTabGroup.getAwtComponent();

        mainTabbedPane.setSelectedIndex( 0 );

        JPanel configPanel = getConfigPanelFromTabbedPane( mainTabbedPane );

        JPanel configTablePanel = ( JPanel ) configPanel.getComponent( 1 );

        return configTablePanel;
    }

    public static JPanel getConfigButtonPanel( final Window window )
    {
        TabGroup mainTabGroup = window.getTabGroup( "visionTabs" );

        mainTabGroup.selectTab( "My Settops" );

        JTabbedPane mainTabbedPane = mainTabGroup.getAwtComponent();

        mainTabbedPane.setSelectedIndex( 0 );

        JPanel configPanel = getConfigPanelFromTabbedPane( mainTabbedPane );

        JPanel configTablePanel = ( JPanel ) configPanel.getComponent( 1 );

        return configTablePanel;
    }

    public static JPanel getConfigPanelFromTabbedPane( JTabbedPane mainTabbedPane )
    {
        JPanel configPanel = null;

        Component[] mainComponents = mainTabbedPane.getComponents();

        for ( Component component : mainComponents )
        {
            if ( component instanceof JScrollPane )
            {
                if ( ( component.getName() != null ) && ( component.getName().equals( "mySettopsPane" ) ) )
                {

                    JScrollPane mySettopsPane = ( JScrollPane ) component;

                    Component viewPortComponent = mySettopsPane.getComponent( 0 );

                    JViewport viewPort = ( JViewport ) viewPortComponent;

                    Component configPanelComponent = viewPort.getComponent( 0 );

                    configPanel = ( JPanel ) configPanelComponent;

                    break;
                }
            }
        }
        return configPanel;
    }

    public static Table getTableFromConfigPanel( JPanel panel, String scrollPaneName, String tabLabel, int index )
    {
        Table uiTable = null;

        for ( Component component : panel.getComponents() )
        {
            if ( ( component.getName() != null ) && ( "resultsTabbedPane".equals( component.getName() ) ) )
            {
                JTabbedPane resultsTabbedPane = ( JTabbedPane ) component;

                TabGroup resultsTabGroup = new TabGroup( resultsTabbedPane );

                resultsTabGroup.selectTab( tabLabel );

                if ( scrollPaneName.equals( resultsTabbedPane.getComponent( index ).getName() ) )
                {
                    JScrollPane settopsScrollPane = ( JScrollPane ) resultsTabbedPane.getComponent( index );

                    for ( Component settopsScrollPaneComponent : settopsScrollPane.getComponents() )
                    {
                        if ( settopsScrollPaneComponent instanceof JViewport )
                        {
                            JViewport viewPort = ( JViewport ) settopsScrollPaneComponent;

                            for ( Component innerComponent : viewPort.getComponents() )
                            {
                                if ( innerComponent instanceof JTable )
                                {
                                    JTable settopsTable = ( JTable ) innerComponent;

                                    uiTable = new Table( settopsTable );
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return uiTable;
    }
}
