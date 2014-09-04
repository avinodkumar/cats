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
package com.comcast.cats.image;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Contains all the region information objects for a specific image.
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "region_info_list" )
public class ImageRegionInfo implements Serializable, Cloneable
{
    private static final long              serialVersionUID = -125047854867040467L;
    @XmlElement( name = "image_path" )
    private String                         imagePath        = "";
    @XmlElement( name = "image_compare_region" )
    private List< ImageCompareRegionInfo > icRegionList;
    @XmlElement( name = "ocr_region" )
    private List< OCRRegionInfo >          ocrRegionList;
    @XmlElement( name = "search_region" )
    private List< RegionInfo > searchRegionList;

    public ImageRegionInfo()
    {
        icRegionList = new ArrayList< ImageCompareRegionInfo >();
        ocrRegionList = new ArrayList< OCRRegionInfo >();
        searchRegionList = new ArrayList< RegionInfo >();
    }

    /**
     * Adds a region info object to the appropriate internal list.
     * 
     * @param info
     *            The RegionInfo object.
     * @throws IllegalArgumentException
     *             is info is null.
     */
    public final void addRegionInfo( RegionInfo info )
    {
        if ( info == null )
        {
            throw new IllegalArgumentException( "info cannot be null" );
        }
        if ( info instanceof ImageCompareRegionInfo )
        {
            icRegionList.add( ( ImageCompareRegionInfo ) info );
        }
        else if ( info instanceof OCRRegionInfo )
        {
            ocrRegionList.add( ( OCRRegionInfo ) info );
        }
        else
        {
            searchRegionList.add( info );
        }
    }

    /**
     * Adds a region info object to the internal list.
     * 
     * @param idx
     *            The index to add the info object at.
     * @param info
     *            The RegionInfo object.
     * @throws IllegalArgumentException
     *             is info is null.
     */
    public final void addRegionInfoAt( int idx, RegionInfo info )
    {
        if ( info == null )
        {
            throw new IllegalArgumentException( "info cannot be null" );
        }
        if ( info instanceof ImageCompareRegionInfo )
        {
            icRegionList.add( idx, ( ImageCompareRegionInfo ) info );
        }
        else if ( info instanceof OCRRegionInfo )
        {
            ocrRegionList.add( idx, ( OCRRegionInfo ) info );
        }
        else
        {
            searchRegionList.add( idx, info );
        }
    }

    public final void writeToXML(final OutputStream outputStream) throws JAXBException {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream cannot be null.");
        }

        RegionInfoXmlUtil.writeToJaxbXML( this, outputStream );
    }

    public ImageRegionInfo loadFromXML(final InputStream inputStream ) throws JAXBException
    {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null.");
        }
        return RegionInfoXmlUtil.loadFromJaxbXML( inputStream );

    }

    /**
     * Sets the image Path.
     * 
     * @param imagePath
     *            the imagePath to set
     * @throws IllegalArgumentException
     *             if imagePath is null.
     */
    public final void setImagePath( String imagePath )
    {
        if ( imagePath == null )
        {
            throw new IllegalArgumentException( "imagePath cannot be null" );
        }
        this.imagePath = imagePath;
    }

    /**
     * Gets the image path.
     * 
     * @return the imagePath
     */
    public final String getImagePath()
    {
        return imagePath;
    }

    /**
     * Gets all the image compare regions.
     * 
     * @return A list of all the image compare regions.
     */
    public final List< ImageCompareRegionInfo > getImageCompareRegionInfoList()
    {
        return icRegionList;
    }

    protected final List< ImageCompareRegionInfo > getImageComapareRegionInfoList()
    {

        return icRegionList;
    }

    protected final List< OCRRegionInfo > getOcrRegionInfoList()
    {

        return ocrRegionList;
    }
    
    protected final List< RegionInfo > getSearchRegionInfoList()
    {
        return searchRegionList;
    }

    public List< RegionInfo > getRegionInfoList()
    {
        List< RegionInfo > regionInfoList = new ArrayList< RegionInfo >();
        regionInfoList.addAll( getOcrRegionInfoList() );
        regionInfoList.addAll( getImageCompareRegionInfoList() );
        regionInfoList.addAll( getSearchRegionInfoList() );
        return regionInfoList;
    }

    /**
     * Sets all the image compare regions.
     * 
     * @return A list of all the image compare regions.
     */
    public final void setRegionInfoList( List< RegionInfo > regionInfoList )
    {

        for ( RegionInfo info : regionInfoList )
        {
            if ( info instanceof ImageCompareRegionInfo )
            {
                addRegionInfo( ( ImageCompareRegionInfo ) info );
            }
            else if ( info instanceof OCRRegionInfo )
            {
                addRegionInfo( ( OCRRegionInfo ) info );
            }
            else
            {
                addRegionInfo( info );
            }
        }
    }

    /**
     * Get a specific region from the List of RegionInfo.
     * 
     * @param regionName
     *            The name of the region wanted.
     * @return The populated RegionInfo object.
     */
    public final RegionInfo getRegion( String regionName )
    {
        RegionInfo retVal = null;
        
        synchronized ( this )
        {

            Iterator< RegionInfo > iter = getRegionInfoList().iterator();
            while ( iter.hasNext() )
            {
                RegionInfo next = iter.next();
                if ( next.getName().equalsIgnoreCase( regionName ) )
                {
                    retVal = next;
                    break;
                }
            }
        }
        return ( RegionInfo ) retVal;
    }

    /**
     * Deletes a region from the region list if it exists.
     * 
     * @param regionName
     *            The region name.
     */
    public synchronized final void deleteRegion( String regionName )
    {
        RegionInfo info = getRegion( regionName );
        if( null != info )
        {
            if ( info instanceof ImageCompareRegionInfo )
            {
                icRegionList.remove( ( ImageCompareRegionInfo ) info );
            }
            else if ( info instanceof OCRRegionInfo )
            {
                ocrRegionList.remove( ( OCRRegionInfo ) info );
            }
            else
            {
                searchRegionList.remove( info );
            }
        }
    }

    /**
     * Compares this object with the specified object for equality.
     * 
     * @param o
     *            the object to compare against.
     * @return true if they are equal.
     */
    @Override
    public boolean equals( final Object o )
    {
        boolean ret = false;
        if ( !( o instanceof ImageRegionInfo ) )
        {
            ret = false;
        }
        else if ( this == o )
        {
            ret = true;
        }
        else
        {
            ImageRegionInfo imgInfo = ( ImageRegionInfo ) o;
            // Do a basic check first.
            if ( this.imagePath.equals( imgInfo.getImagePath() ) )
            {
                EqualsBuilder eBuilder = new EqualsBuilder().append( this.imagePath, imgInfo.getImagePath() );
                List< RegionInfo > infoList = imgInfo.getRegionInfoList();

                synchronized ( this )
                {
                    List< ImageCompareRegionInfo > icRegionInfoList = getImageComapareRegionInfoList();
                    for ( int i = 0; i != icRegionInfoList.size(); ++i )
                    {
                        eBuilder.append( icRegionInfoList.get( i ), infoList.get( i ) );
                    }
                    List< OCRRegionInfo > ocrRegionInfoList = getOcrRegionInfoList();
                    for ( int i = 0; i != ocrRegionInfoList.size(); ++i )
                    {
                        eBuilder.append( ocrRegionInfoList.get( i ), infoList.get( i ) );
                    }
                    List< RegionInfo > searchRegionInfoList = getSearchRegionInfoList();
                    for ( int i = 0; i != searchRegionInfoList.size(); ++i )
                    {
                        eBuilder.append( searchRegionInfoList.get( i ), infoList.get( i ) );
                    }
                }
                ret = eBuilder.isEquals();
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int SEVENTEEN = 17;
        final int THIRTYSEVEN = 37;
        return new HashCodeBuilder( SEVENTEEN, THIRTYSEVEN ).append( this.imagePath ).toHashCode();
    }
}
