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
package com.comcast.cats.vision.panel.videogrid;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * The panel that holds video grid, remote panel and log panel.
 * 
 * @author aswathyann
 * 
 */
public class MultivisionPanel extends JPanel
{
    private static final long serialVersionUID = 8638119874390786140L;

    private VideoControlPanel videoControlPanel;
    private VideoGridPanel    videoGridPanel;
    private LogPanel          logPanel;
    private static Logger     logger           = Logger.getLogger( MultivisionPanel.class );

    /**
     * Constructor for MultivisionPanel
     * 
     * @param videoControlPanel
     *            instance of VideoControlPanel
     * @param logPanel
     *            instance of LogPanel
     * @param videoGridPanel
     *            instance of VideoGridPanel
     * @throws MalformedURLException
     *             Thrown to indicate that a malformed URL has occurred.
     * @throws URISyntaxException
     *             Checked exception thrown to indicate that a string could not
     *             be parsed as a URI reference.
     */
    public MultivisionPanel( VideoControlPanel videoControlPanel, LogPanel logPanel, VideoGridPanel videoGridPanel )
                                                                                                                    throws MalformedURLException,
                                                                                                                    URISyntaxException
    {
        this.videoControlPanel = videoControlPanel;
        this.logPanel = logPanel;
        this.videoGridPanel = videoGridPanel;
        setMultivisionPanelLayout();
        setVisible( true );
    }

    /**
     * Set layout for VideoGridWindow
     */
    protected void setMultivisionPanelLayout()
    {
        logger.debug( "Set layout for VideoGridWindow." );
        setLayout( new GridBagLayout() );
        add( videoGridPanel, getVideoGridPanelConstraints() );
        add( videoControlPanel, getVideoControlPanelConstraints() );
        add( logPanel, getLogPanelConstraints() );

    }

    /*
     * Get GridBagConstraints for LogPanel
     */
    private GridBagConstraints getLogPanelConstraints()
    {
        GridBagConstraints logPanelConstraints = new GridBagConstraints();
        logPanelConstraints.gridx = 0;
        logPanelConstraints.gridy = 1;
        logPanelConstraints.gridwidth = 1;
        logPanelConstraints.weighty = .20;
        logPanelConstraints.fill = GridBagConstraints.BOTH;
        logPanelConstraints.insets = new Insets( 3, 3, 3, 3 );
        return logPanelConstraints;
    }

    /*
     * Get GridBagConstraints for VideoControlPanel
     */
    private GridBagConstraints getVideoControlPanelConstraints()
    {
        GridBagConstraints videoControlPanelConstraints = new GridBagConstraints();
        videoControlPanelConstraints.gridx = 1;
        videoControlPanelConstraints.gridy = 0;
        videoControlPanelConstraints.gridheight = 2;
        videoControlPanelConstraints.weightx = .20;
        videoControlPanelConstraints.weighty = .80;
        videoControlPanelConstraints.fill = GridBagConstraints.VERTICAL;
        videoControlPanelConstraints.insets = new Insets( 3, 3, 3, 3 );
        return videoControlPanelConstraints;
    }

    /*
     * Get GridBagConstraints for VideoGridPanel
     */
    protected GridBagConstraints getVideoGridPanelConstraints()
    {
        GridBagConstraints videoGridPanelConstraints = new GridBagConstraints();
        videoGridPanelConstraints.gridx = 0;
        videoGridPanelConstraints.gridy = 0;
        videoGridPanelConstraints.weightx = .80;
        videoGridPanelConstraints.weighty = .80;
        videoGridPanelConstraints.fill = GridBagConstraints.BOTH;
        videoGridPanelConstraints.insets = new Insets( 3, 3, 3, 3 );
        return videoGridPanelConstraints;
    }

    /**
     * Get VideoGridPanel
     * 
     * @return instance of VideoGridPanel
     */
    public VideoGridPanel getVideoGridPanel()
    {
        return videoGridPanel;
    }

    public VideoControlPanel getVideoControlPanel()
    {
        return videoControlPanel;
    }

    /**
     * Get VideoGridPanel
     * 
     * @return instance of VideoGridPanel
     */
    public LogPanel getLogPanel()
    {
        return logPanel;
    }

    public void setVideoGridPanel( VideoGridPanel videoGridPanel )
    {
        this.videoGridPanel = videoGridPanel;
    }

}
