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
package com.comcast.cats.script;

import com.comcast.cats.event.CatsEvent;

/**
 * The Scripter interface should be implemented by all the classes which does
 * the actual scripting.
 * 
 * @author aswathyann
 * 
 */
public interface Scripter
{
    /**
     * Generate script according to the input text provided. Input text can be
     * control strings that the implementation can understand.
     * 
     * @param text
     *            text to be written
     * @return String
     */
    String generateScript( final String text );

    /**
     * Generate script according to the given input provided.
     * 
     * @param catsEvent
     *            instance of CatsEvent
     * @return String
     */
    String generateScript( final CatsEvent catsEvent );
}
