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
package com.comcast.cats.vision.panel.imagecompare;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * JPG File filter. Used to filter jpg files in FileChooser.
 */
public class JPGFilter extends FileFilter
{

    /**
     * JPG file extention.
     */
    public static final String JPG = "jpg";

    /**
     * Checks if a files extention is jpg.
     * 
     * @param f
     *            the file to check.
     * @return true if the file ends in a jpg extension.
     */
    @Override
    public boolean accept( File f )
    {
        if ( f.isDirectory() )
        {
            return true;
        }

        String filepath = f.getAbsolutePath();
        int dotPos = filepath.lastIndexOf( "." );
        String filename = filepath.substring( dotPos );

        if ( filename != null && filename.equals( JPG ) )
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return "*." + JPG;
    }
}
