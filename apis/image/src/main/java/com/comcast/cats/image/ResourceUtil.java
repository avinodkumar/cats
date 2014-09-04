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

/**
 * Class contains resource utility functions.
 * @author mzmuda
 *
 */
public class ResourceUtil {
    /**
     * Attempts to load a resource from the specified path.
     * @param resourceClass
     *           The Class that is in the same location as the resources we will load.
     * @param path The path to load.
     * @return The ImputStream for the resource. null if the resource is not found.
     */
    public static InputStream loadResource(Class< ? > resourceClass, String path) {
        InputStream is = null;
        if (resourceClass != null && path != null) {
            is = resourceClass.getResourceAsStream(path);
            // this might work if the resource is in the classpath.
            if (is == null && !path.startsWith("/")) {
                is = resourceClass.getResourceAsStream("/" + path);
            }
        }
        return is;
    }
}
