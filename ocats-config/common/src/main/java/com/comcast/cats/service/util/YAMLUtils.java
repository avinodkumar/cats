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
package com.comcast.cats.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang.IllegalClassException;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Provides a util to handle YAML files.
 * 
 * @author skurup00c
 * 
 */
public class YAMLUtils
{

    private static Logger logger = Logger.getLogger( YAMLUtils.class );

    @SuppressWarnings( "unchecked" )
    public static synchronized < T > T loadFromYAML( String filePath, T resultObject, Constructor constructor )
            throws IllegalClassException, IOException
    {
        Yaml yaml;
        FileInputStream fileIS = null;
        
        if ( constructor != null )
        {
            yaml = new Yaml( constructor );
        }
        else
        {
            yaml = new Yaml();
        }
        try
        {
            fileIS = new FileInputStream( filePath );
            resultObject = ( T ) yaml.load( fileIS );
        }
        catch ( FileNotFoundException exception )
        {
            File file = new File( filePath );
            file.createNewFile(); // create file if one does not exist.
            fileIS = new FileInputStream( filePath );
            resultObject = ( T ) yaml.load( fileIS );
        }
        catch ( ClassCastException e )
        {
            logger.warn( "YAML content found at " + filePath + " does not conform to passed object " + e.getMessage() );
            throw new IllegalClassException( "YAML content found at " + filePath
                    + " does not conform to passed object " + e.getMessage() );
        }
        finally{
            if(fileIS != null)
            {
                fileIS.close();
            }
        }

        return resultObject;
    }

    public static synchronized < T > void saveAsYAML( String filePath, T objectToSave, Representer representer )
            throws FileNotFoundException
    {
        if ( objectToSave != null )
        {
            Yaml yaml;
            FileWriter fw = null;
            if ( representer == null )
            {
                yaml = new Yaml();
            }
            else
            {
                yaml = new Yaml( representer );
            }
            try
            {
                
                File file = new File( filePath );
                file.createNewFile(); // create file if one does not exist.

                fw = new FileWriter( filePath );
                yaml.dump( objectToSave, fw );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            finally{
                if(fw != null)
                {
                    try
                    {
                        fw.close();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
