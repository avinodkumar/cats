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
package com.comcast.cats.service.ir.redrat;

public class RedRatConstants
{
    /**
     * The RedRatHub ip and port are available through props file.
     */
    public static final String                     REDRAT_PROPERTIES_FILE = "redrat.props";
    public static final String                     REDRAT_HOST_PROPERTY   = "redratHubHost";
    public static final String                     REDRAT_HOST_PORT       = "redratHubPort";
    public static final String                     REDRATHUB_POOL_SIZE   = "redrathub.pool.size";

    public static final String                     DEFAULT_REDRAT_HOST    = "localhost";
    public static final int                        DEFAULT_REDRAT_PORT    = 40000;
    public static final int                        DEFAULT_POOL_SIZE    = 48;
    public static final int                        POOL_WAIT_TIME    = 60; //sec


    public static final String                     REDRAT_PROMPT_STRING_1 = "\n";
    public static final String                     REDRAT_PROMPT_STRING_2 = "}\n";

    /**
     * Max Idle time.
     */
    protected static final long                    MAX_IDLE_TIME          = 5 * 60 * 1000;

}
