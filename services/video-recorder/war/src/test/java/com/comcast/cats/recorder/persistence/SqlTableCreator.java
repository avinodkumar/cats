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
package com.comcast.cats.recorder.persistence;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * A simple utility to create schema based on Entity classes.
 * 
 */
public class SqlTableCreator
{
    private AnnotationConfiguration cfg;

    public SqlTableCreator( String packageName ) throws Exception
    {
        cfg = new AnnotationConfiguration();
        cfg.setProperty( "hibernate.hbm2ddl.auto", "create" );

        for ( Class< Object > clazz : getClasses( packageName ) )
        {
            cfg.addAnnotatedClass( clazz );
        }
    }

    /**
     * Method that actually creates the file.
     * 
     * @param dbDialect
     *            to use
     */
    private void generate( Dialect dialect )
    {
        cfg.setProperty( "hibernate.dialect", dialect.getDialectClass() );

        SchemaExport export = new SchemaExport( cfg );
        export.setDelimiter( ";" );
        export.setOutputFile( "ddl_" + dialect.name().toLowerCase() + ".sql" );
        export.execute( true, false, false, false );
    }

    /**
     * Utility method used to fetch Class list based on a package name.
     * 
     * @param packageName
     *            (should be the package containing your annotated beans.
     */
    @SuppressWarnings( "rawtypes" )
    private List< Class > getClasses( String packageName ) throws Exception
    {
        List< Class > classes = new ArrayList< Class >();
        File directory = null;
        try
        {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if ( cld == null )
            {
                throw new ClassNotFoundException( "Can't get class loader." );
            }
            String path = packageName.replace( '.', '/' );
            URL resource = cld.getResource( path );
            if ( resource == null )
            {
                throw new ClassNotFoundException( "No resource for " + path );
            }
            directory = new File( resource.getFile() );
        }
        catch ( NullPointerException x )
        {
            throw new ClassNotFoundException( packageName + "does not appear to be a valid package. " + x.getMessage() );
        }
        if ( directory.exists() )
        {
            String[] files = directory.list();
            for ( int i = 0; i < files.length; i++ )
            {
                if ( files[ i ].endsWith( ".class" ) )
                {
                    // removes the .class extension
                    classes.add( Class.forName( packageName + '.' + files[ i ].substring( 0, files[ i ].length() - 6 ) ) );
                }
            }
        }
        else
        {
            throw new ClassNotFoundException( packageName + " is not a valid package" );
        }

        return classes;
    }

    /**
     * Holds the classnames of hibernate dialects for easy reference.
     */
    private static enum Dialect
    {
        ORACLE( "org.hibernate.dialect.Oracle10gDialect" ), MYSQL( "org.hibernate.dialect.MySQLDialect" ), HSQL(
                "org.hibernate.dialect.HSQLDialect" );

        private String dialectClass;

        private Dialect( String dialectClass )
        {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass()
        {
            return dialectClass;
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception
    {
        SqlTableCreator gen = new SqlTableCreator( "com.comcast.cats.reorder.persistence.domain" );
        gen.generate( Dialect.MYSQL );
        gen.generate( Dialect.ORACLE );
        gen.generate( Dialect.HSQL );
    }

}
