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
package com.comcast.cats.vision.panel.videogrid.model;

import com.comcast.cats.Settop;
import com.comcast.cats.vision.panel.videogrid.VideoPanel;

/**
 * Class to hold additional information on Settop in CatsVision.
 * 
 * @author minu
 * 
 */
public class CatsVisionSettop
{
    private Settop                  settop;
    // private boolean isAllocated;
    private boolean                 isSelected;
    private VideoPanel              videoPanel;

    public CatsVisionSettop( Settop settop )
    {
        this.settop = settop;
    }

    public Settop getSettop()
    {
        return settop;
    }

    public void setAllocated( boolean isAllocated )
    {
        // this.isAllocated = isAllocated;
    }

    public boolean isAllocated()
    {
        // return isAllocated;
        return true;
    }

    public VideoPanel getVideoPanel()
    {
        return videoPanel;
    }

    public void setVideoPanel( VideoPanel videoPanel )
    {
        this.videoPanel = videoPanel;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected( boolean isSelected )
    {
        this.isSelected = isSelected;
    }
}
