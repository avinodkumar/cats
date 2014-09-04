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
package com.comcast.cats;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple layout class for remote buttons to allow them to be dynamically placed
 * on the RemotePanel.
 * 
 * @author cfrede001
 * 
 */
@XmlRootElement
public class RemoteLayout implements Serializable
{
    private static final long serialVersionUID = 4849031215071291208L;

    private Integer           row;
    private Integer           column;
    private Integer           panel;
    private String            foregroundColor;
    private String            backgroundColor;
    private RemoteCommand     remote;

    /**
     * Empty constructor
     * 
     */
    // You must have this for JAXB to function correctly.
    public RemoteLayout()
    {

    }

    /**
     * Constructor with row, column, panel and RemoteCommand
     * 
     * @param row
     * @param column
     * @param panel
     * @param remote
     *            RemoteCommand
     * 
     */
    public RemoteLayout( Integer row, Integer column, Integer panel, RemoteCommand remote )
    {
        super();
        this.row = row;
        this.column = column;
        this.panel = panel;
        this.remote = remote;
    }

    /**
     * Constructor with row, column, panel and RemoteCommand
     * 
     * @param row
     * @param column
     * @param panel
     * @param foregroundColor
     *            String
     * @param backgroundColor
     *            String
     * @param remote
     *            RemoteCommand
     * 
     */
    public RemoteLayout( Integer row, Integer column, Integer panel, String foregroundColor, String backgroundColor,
            RemoteCommand remote )
    {
        super();
        this.row = row;
        this.column = column;
        this.panel = panel;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.remote = remote;
    }

    /**
     * @return the row
     */
    public Integer getRow()
    {
        return row;
    }

    /**
     * @param row
     *            the row to set
     */
    public void setRow( Integer row )
    {
        this.row = row;
    }

    /**
     * @return the column
     */
    public Integer getColumn()
    {
        return column;
    }

    /**
     * @param column
     *            the column to set
     */
    public void setColumn( Integer column )
    {
        this.column = column;
    }

    /**
     * @return the panel
     */
    public Integer getPanel()
    {
        return panel;
    }

    /**
     * @param panel
     *            the panel to set
     */
    public void setPanel( Integer panel )
    {
        this.panel = panel;
    }

    /**
     * @return the foregroundColor
     */
    public String getForegroundColor()
    {
        return foregroundColor;
    }

    /**
     * @param foregroundColor
     *            the foregroundColor to set
     */
    public void setForegroundColor( String foregroundColor )
    {
        this.foregroundColor = foregroundColor;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor( String backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the remote
     */
    public RemoteCommand getRemote()
    {
        return remote;
    }

    /**
     * @param remote
     *            the remote to set
     */
    public void setRemote( RemoteCommand remote )
    {
        this.remote = remote;
    }

    @Override
    public boolean equals( Object remoteLayout )
    {
        RemoteLayout layoutObject = null;
        boolean isSame = false;
        if ( remoteLayout instanceof RemoteLayout )
        {
            layoutObject = ( RemoteLayout ) remoteLayout;
            if ( row.equals( layoutObject.row ) && column.equals( layoutObject.column )
                    && panel.equals( layoutObject.panel ) )
            {
                /*
                 * We are not bothered about foreground and background color and
                 * the key for making a decision on whether these are same
                 * objects. This is because when we try to decide overlapping
                 * object in remoteLayout we are bothered about position over
                 * the actual element type( TODO: revisit this decision)
                 */

                isSame = true;
            }
        }
        return isSame;
    }

    @Override
    public int hashCode()
    {
        return row + column + panel;
    }

    /**
     * @return the RemoteLayout (row,column,panel and remote command) as string
     */
    @Override
    public String toString()
    {
        return "RemoteLayout [column=" + column + ", panel=" + panel + ", remote=" + remote + ", row=" + row + "]";
    }
}
