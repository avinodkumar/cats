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
package com.comcast.cats.vision.remote;

import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Window;

import com.comcast.cats.vision.CATSVisionTest;


public class TestDirectTune extends CATSVisionTest
{
    Window window;
    
    
    @Test
    public void testDirectTune() throws Exception
    {
        try{
            window = getMainWindow();
            streamAndAllocateSettop( window );
            Panel panel = window.getPanel( "mainPanel" );
            Panel remotePanel = panel.getPanel( "remotePanel" );
            Panel directTunePanel = remotePanel.getPanel("DirectTunePanel");
            TextBox textBox = directTunePanel.getTextBox( "DirectTuneTextField" );
            textBox.setText( "12" );
            Button direcTuneButton = directTunePanel.getButton( "DirectTuneButton" );
            direcTuneButton.click();
        }catch (Exception e) {
            
        }finally{
            releaseSettop( window );
        }

    }
    
    @Test
    public void testDirectTuneFocusEvents() throws Exception
    {
        window = getMainWindow();
       try{
           streamAndAllocateSettop( window );
       
        Panel panel = window.getPanel( "mainPanel" );
        Panel remotePanel = panel.getPanel( "remotePanel" );
        Panel directTunePanel = remotePanel.getPanel("DirectTunePanel");
        TextBox textBox = directTunePanel.getTextBox( "DirectTuneTextField" );
        textBox.pressKey( Key.A);
        textBox.focusLost();
        
        Button direcTuneButton = directTunePanel.getButton( "DirectTuneButton" );
        direcTuneButton.click();
       }catch (Exception e) {
        
       }finally{
           releaseSettop( window );
       }
    }
}
