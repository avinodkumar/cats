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
package com.comcast.cats.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.comcast.cats.info.VideoRecorderState;
import com.comcast.cats.service.util.ApplicationConfigUtil;

/**
 * A plain JDBC manager to do clean up befor JPA layer initialize.
 * 
 * @author SSugun00c
 * 
 */
class RecorderJdbcManager
{
    private static PreparedStatement cleanupStatement = null;
    private static final String      PVR_CLEAN_UP_SQL = "UPDATE pvr_recording_status status SET state = ?, message=? WHERE status.state = ? OR status.state = ? OR status.state = ?";

    private RecorderJdbcManager()
    {
        try
        {
            Class.forName( ApplicationConfigUtil.getDbDriverClassName() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws java.sql.SQLException
    {
        return DriverManager.getConnection( ApplicationConfigUtil.getDbConnectionUrl(),
                ApplicationConfigUtil.getDbUserId(), ApplicationConfigUtil.getDbPassword() );
    }

    public static int cleanUp() throws SQLException
    {
        int noOfRowsAffected = 0;

        Connection con = getConnection();

        try
        {
            cleanupStatement = con.prepareStatement( PVR_CLEAN_UP_SQL );
            cleanupStatement.setString( 1, VideoRecorderState.FORCE_CLOSE.toString() );
            cleanupStatement.setString( 2, "The recording session was closed unexpectedly. Playback may not work" );
            cleanupStatement.setString( 3, VideoRecorderState.INITIALIZING.toString() );
            cleanupStatement.setString( 4, VideoRecorderState.BUFFERING.toString() );
            cleanupStatement.setString( 5, VideoRecorderState.RECORDING.toString() );

            noOfRowsAffected = cleanupStatement.executeUpdate();

            if ( !con.getAutoCommit() )
            {
                // java.sql.SQLException may happen here.
                con.commit();
            }
        }
        finally
        {
            cleanupStatement.close();
            con.close();
        }

        return noOfRowsAffected;
    }
}