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

import com.comcast.cats.AbstractSettop;
import com.comcast.cats.domain.HardwareDevice;
import com.comcast.cats.domain.HardwareInterface;
import com.comcast.cats.domain.HardwarePurpose;
import com.comcast.cats.domain.HardwareType;
import com.comcast.cats.provider.VideoProvider;

public class MockSettop extends AbstractSettop
{
    private static final long serialVersionUID = 1L;
    private VideoProvider     vp;

    public void setVideo( VideoProvider vp )
    {
        this.vp = vp;
    }

    @Override
    public VideoProvider getVideo()
    {
        return vp;
    }

    @Override
    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose )
    {
        return null;
    }

}
