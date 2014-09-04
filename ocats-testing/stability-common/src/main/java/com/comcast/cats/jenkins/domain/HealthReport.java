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
package com.comcast.cats.jenkins.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * @author SSugun00c
 * 
 */
@Root
public class HealthReport extends BaseJenkinsDomain
{
    private static final long serialVersionUID = 1L;

    @Element( required = false )
    private String            description;

    @Element( required = false )
    private String            iconUrl;

    @Element( required = false )
    private int               score;

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getIconUrl()
    {
        return iconUrl;
    }

    public void setIconUrl( String iconUrl )
    {
        this.iconUrl = iconUrl;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore( int score )
    {
        this.score = score;
    }
}
