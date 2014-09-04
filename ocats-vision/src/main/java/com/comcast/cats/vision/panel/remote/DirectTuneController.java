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
package com.comcast.cats.vision.panel.remote;

import static com.comcast.cats.vision.util.CatsVisionConstants.PLEASE_ALLOCATE_MSG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Set;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.vision.event.ActionType;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.vision.panel.videogrid.model.GridDataModel;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * Controller for direct tune functionality
 * 
 * @author bemman01c
 * 
 */
public class DirectTuneController implements ActionListener, HierarchyListener
{

    private static final Logger logger     = Logger.getLogger( DirectTuneController.class );
    private static final String PANLE_NAME = "DirectTunePanel";
    protected DirectTunePanel   directTunePanel;
    private CatsEventDispatcher eventDispatcher;
    private FocusListener       focusListener;
    
    /**
     * Model in MVC for multi grid view.
     */
    private GridDataModel       gridDataModel;

    public DirectTuneController( CatsEventDispatcher eventDispatcher, FocusListener focusListener, GridDataModel model)
    {
        this.eventDispatcher = eventDispatcher;
        this.focusListener = focusListener;
        this.gridDataModel = model;
        initView();
    }

    /**
     * Init the view and add the required listeners
     */
    private void initView()
    {
        directTunePanel = new DirectTunePanel();
        directTunePanel.getDirectTuneButton().removeActionListener( this );
        directTunePanel.removeHierarchyListener( this );
        directTunePanel.getDirectTuneButton().addActionListener( this );
        directTunePanel.addHierarchyListener( this );
        directTunePanel.getDirectTuneTextField().addFocusListener( focusListener );
    }

    /**
     * Return the direct tune view which has to be added to the remote panel
     * 
     * @return
     */
    public DirectTunePanel getDirectTunePanel()
    {
        return directTunePanel;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource() == directTunePanel.getDirectTuneButton() )
        {
            String channelNumber = directTunePanel.getDirectTuneTextField().getText();
            directTune( channelNumber );
        }
    }

    @Override
    public void hierarchyChanged( HierarchyEvent e )
    {
        if ( ( e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED ) != 0 )
        {
            // clear the text if the component is hidden.
            directTunePanel.getDirectTuneTextField().setText( "" );
        }
    }

    /**
     * Direct Tune to the channel number
     * 
     * @param channelNumber
     */
    protected void directTune( String channelNumber )
    {
        if ( checkChannelValidity( channelNumber ) )
        {
            Set< Settop > allocatedAndSelectedSettops = gridDataModel.getAllocatedAndSelectedSettops();

            if ( ( allocatedAndSelectedSettops != null ) && !( allocatedAndSelectedSettops.isEmpty() ) )
            {
                if ( eventDispatcher != null )
                {
                    logger.debug( "tune to  " + channelNumber );
                    // dispatch the even so that the tune is invoked on all the
                    // settops in the video grid.
                    eventDispatcher.sendCatsEvent( new RemoteEvent( ActionType.TUNE, channelNumber, PANLE_NAME, this ) );
                }
            }
            else
            {
                CatsVisionUtils.showWarning( "Unable to perform the operation", PLEASE_ALLOCATE_MSG + "Direct Tune" );
            }
        }
    }

    private boolean checkChannelValidity( String channelNumber ) throws IllegalArgumentException
    {
        boolean isValidChannel = CatsVisionUtils.isValidChannelNumber( channelNumber );
        logger.debug( "Channel Number " + channelNumber + " validty is " + isValidChannel );

        if ( !isValidChannel )
        {
            logger.debug( "Not a valid channel number : " + channelNumber );
            CatsVisionUtils.showError( "Not a valid channel number : " + channelNumber );
        }
        return isValidChannel;
    }
}
