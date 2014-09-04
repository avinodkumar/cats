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


import groovy.lang.Binding;

import java.io.InputStream;
import java.io.File;
import com.comcast.cats.script.SettopBinding;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.script.PausableSettop;
import com.comcast.cats.Settop;
import com.comcast.cats.script.playback.exceptions.ScriptPlaybackException;


/**
 * Class implements the script play back feature.
 * @author minu
 *
 */
class ScriptPlayerImpl implements ScriptPlayer{

    private SettopBinding binding
	private PausableSettop pausableSettop
	private InputStream scriptFile
    private boolean stopFlag = false;

    public ScriptPlayerImpl (Settop settop){
		//decorate the settop.
        this.pausableSettop = new PausableSettop( settop )
        this.binding =  new SettopBinding(pausableSettop)
    }

    /**
     * Method to play back the Cats DSL script in String 
     */
    boolean playBackScript(String script) throws ScriptPlaybackException {
        createShellAndEvaluate(script)
    }

    /**
     * Method to play back the Cats DSL script in InputStream
     */
    boolean playBackScript( InputStream scriptStream) throws ScriptPlaybackException {
        createShellAndEvaluate(scriptStream)
    }

    /**
     * Method to play back the Cats DSL script in File
     */
    boolean playBackScript( File scriptFile) throws ScriptPlaybackException {
        createShellAndEvaluate(scriptFile)
    }

    /**
     * Method creates GroovyShell object and evaluate the script against the binding.
     */
    def createShellAndEvaluate = {script ->
        def shell
        def playBackResult = false

        try {
            shell = new GroovyShell(binding)
            shell.evaluate(script)
            playBackResult = true
        }
        catch(Exception e){
            throw new ScriptPlaybackException(e.getMessage())
        }
        finally{
            pausableSettop.destroyInterceptor()
        }
        playBackResult
    }
	
	@Override
	void pause() 
	{
        pausableSettop.pauseExecution()	
	}
	
	@Override
	void resume()
	{
        pausableSettop.resumeExecution()
	}
	
	@Override
	void stop()
	{
        pausableSettop.stopExecution()
	}
}
