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
// ORG:  MOVE: CATS video imagecompare
package com.comcast.cats.image;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * Stores General Region information.
 */
public class RegionInfo implements Serializable, Cloneable
{
    private static final long   serialVersionUID    = -5315143643735859113L;

    /**
     * The default X tolerance.
     */
    public static final Integer DEFAULT_X_TOLERANCE = 10;

    /**
     * The default Y tolerance.
     */
    public static final Integer DEFAULT_Y_TOLERANCE = 10;

    protected String            name;
    protected Integer           x;
    protected Integer           y;
    protected Integer           width;
    protected Integer           height;
    protected Integer           xTolerance;
    protected Integer           yTolerance;
    protected String            filepath;

    /**
     * Creates an instance of region info with default values.
     */
    public RegionInfo()
    {
        this( "", 0, 0, 0, 0 );
    }

    /**
     * Sets the given parameters.
     * 
     * @param name
     *            The name of the region.
     * @param x
     *            The x coordinate.
     * @param y
     *            The y coordinate.
     * @param w
     *            The region width.
     * @param h
     *            The region height.
     */
    public RegionInfo( String name, Integer x, Integer y, Integer w, Integer h )
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        xTolerance = DEFAULT_X_TOLERANCE;
        yTolerance = DEFAULT_Y_TOLERANCE;
    }

    /**
     * Copy Constructor.
     * 
     * @param org
     *            The original RegionInfo.
     * @deprecated Use clone();
     */
    @Deprecated
    protected RegionInfo( RegionInfo org )
    {
        this.name = org.getName();
        this.x = org.getX();
        this.y = org.getY();
        this.width = org.getWidth();
        this.height = org.getHeight();
        this.xTolerance = org.getXTolerance();
        this.yTolerance = org.getYTolerance();
    }

    /**
     * Creates a new instance of this object with the same attributes.
     * 
     * @return a new instance of this object with the same attributes.
     * @deprecated Use clone();
     */
    @Deprecated
    public RegionInfo getCopy()
    {
        return null;
    }

    /**
     * Sets the width.
     * 
     * @param width
     *            the width to set
     */
    public final void setWidth( Integer width )
    {
        this.width = width;
    }

    /**
     * Sets the filepath to the image and metadata.
     * 
     * @param redTolerance
     *            the redTolerance to set
     */
    public final void setFilepath( String filepath )
    {
        this.filepath = filepath;
    }

    /**
     * Gets the filepath to the image and metadata
     * 
     * @return the filepath
     */
    public final String getFilepath()
    {
        return filepath;
    }

    /**
     * Gets the width.
     * 
     * @return the width
     */
    @XmlElement( name = "Width" )
    public final Integer getWidth()
    {
        return width;
    }

    /**
     * Sets the height.
     * 
     * @param height
     *            the height to set
     */
    public final void setHeight( Integer height )
    {
        this.height = height;
    }

    /**
     * Gets the height.
     * 
     * @return the height
     */
    @XmlElement( name = "Height" )
    public final Integer getHeight()
    {
        return height;
    }

    /**
     * Sets the y value.
     * 
     * @param y
     *            the y to set
     */
    public final void setY( Integer y )
    {
        this.y = y;
    }

    /**
     * gets the y value.
     * 
     * @return the y
     */
    @XmlElement( name = "Y" )
    public final Integer getY()
    {
        return y;
    }

    /**
     * Sets the x value.
     * 
     * @param x
     *            the x to set
     */
    public void setX( Integer x )
    {
        this.x = x;
    }

    /**
     * Gets the x value.
     * 
     * @return the x
     */
    @XmlElement( name = "X" )
    public final Integer getX()
    {
        return x;
    }

    /**
     * Sets the region name.
     * 
     * @param name
     *            the name to set
     */
    public final void setName( String name )
    {
        this.name = name;
    }

    /**
     * Gets the region name.
     * 
     * @return the name
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Sets the x tolerance.
     * 
     * @param xTolerance
     *            the xTolerance to set
     */
    public final void setXTolerance( Integer xTolerance )
    {
        this.xTolerance = xTolerance;
    }

    /**
     * Gets the x tolerance.
     * 
     * @return the xTolerance
     */
    public final Integer getXTolerance()
    {
        return xTolerance;
    }

    /**
     * Sets the y tolerance.
     * 
     * @param yTolerance
     *            the yTolerance to set
     */
    public final void setYTolerance( Integer yTolerance )
    {
        this.yTolerance = yTolerance;
    }

    /**
     * Gets the y tolerance.
     * 
     * @return the yTolerance
     */
    public final Integer getYTolerance()
    {
        return yTolerance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException e )
        {
            throw new InternalError( e.toString() );
        }
    }
}
