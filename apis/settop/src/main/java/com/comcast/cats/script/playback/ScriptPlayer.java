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
package com.comcast.cats.script.playback;

import java.io.File;
import java.io.InputStream;

import com.comcast.cats.script.playback.exceptions.ScriptPlaybackException;

public interface ScriptPlayer
{
    /**
     * Method to play back the Cats DSL script
     * 
     * @param script
     *            CATS DSL script as string
     * @return True if play back happened successfully, else false
     * @throws {@link ScriptPlaybackException}
     */
    boolean playBackScript( String script ) throws ScriptPlaybackException;

    /**
     * Method to play back the Cats DSL script
     * 
     * @param scriptStream
     *            CATS DSL script as InputStream
     * @return True if play back happened successfully, else false
     * @throws {@link ScriptPlaybackException}
     */
    boolean playBackScript( InputStream scriptStream ) throws ScriptPlaybackException;

    /**
     * Method to play back the Cats DSL script
     * 
     * @param scriptFile
     *            CATS DSL script as File
     * @return True if play back happened successfully, else false
     * @throws {@link ScriptPlaybackException}
     */
    boolean playBackScript( File scriptFile ) throws ScriptPlaybackException;

    /**
     * Method to Pauses the script play back.
     */
    void pause();

    /**
     * Method to Resumes script execution.
     */
    void resume();

    /**
     * Method to Stop the script execution.
     */
    void stop();
}
