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
package com.comcast.cats.ir.commands;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatsCommandTest
{
    
    CatsCommand pressGuide;
    String commandName = "PressGuide";
    CatsCommand enableCC;
    CatsCommand pressRight;
    CatsCommand pressSelect;

    @Before
    public void setUp() throws Exception
    {
        pressGuide = new CatsCommand( "PressGuide" );
        pressRight  = new CatsCommand( "pressRight" );
        pressSelect = new CatsCommand( "pressSelect" );
        
    }

    @After
    public void tearDown() throws Exception
    {
        pressGuide = null;
        pressRight  = null;
        pressSelect = null;
    }

    @Test
    public void CatsCommandTest()
    {
        //CatsCommand iterate through a full set of commands and then hasNext provides false. 
        // the next hasNext and next sequence will start from the begining.
        assertEquals( commandName, pressGuide.getName() );
        assertTrue( pressGuide.hasNext() );
        assertEquals( pressGuide , pressGuide.next() );
        assertFalse( pressGuide.hasNext() );
        assertEquals( null , pressGuide.next() );
        assertTrue( pressGuide.hasNext() );
        assertEquals( pressGuide , pressGuide.next() );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void CatsCommandNullTest()
    {
        //CatsCommand iterate through a full set of commands and then hasNext provides false. 
        // the next hasNext and next sequence will start from the begining.
        pressGuide.add( null );
    }
    
    @Test
    public void enableCCTest()
    {
       enableCC = pressGuide.add(pressRight).add(pressRight).add( pressSelect );
       int count = 0;
       while(enableCC.hasNext()){
           if(count == 0){
               assertEquals( enableCC.next(),pressGuide );
               count++;
           }else if(count == 1){
               assertEquals( enableCC.next(),pressRight );
               count++;
           }else if (count == 2){
               assertEquals( enableCC.next(),pressRight );
               count++;
           }else if (count == 3){
               assertEquals( enableCC.next(),pressSelect );
               count++;
           }else{
               fail("more steps than expected");
           }
       }
    }
    
    @Test
    public void enableCCAndSelectTest()
    {
       enableCC = pressGuide.add(pressRight).add(pressRight).add( pressSelect );
       CatsCommand enableCCAndSelect = enableCC.add( pressSelect );
       int count = 0;
       while(enableCCAndSelect.hasNext()){
           CatsCommand nextCommand = enableCCAndSelect.next();
           if(count == 0){
               assertEquals( pressGuide, nextCommand );
               count++;
           }else if(count == 1){
               assertEquals( pressRight, nextCommand );
               count++;
           }else if (count == 2){
               assertEquals( pressRight, nextCommand );
               count++;
           }else if (count == 3){
               assertEquals( pressSelect, nextCommand );
               count++;
           }else if (count == 4){
               assertEquals( pressSelect, nextCommand );
               count++;
           }else{
               fail("more steps than expected");
           }
       }
    }
    
    @Test
    public void enableCCAndDisableAudioTest()
    {
       enableCC = new CatsCommand().add( pressGuide ).add(pressRight).add(pressRight).add( pressSelect );
       CatsCommand disableAudio =  new CatsCommand().add(pressRight).add(pressSelect);
       CatsCommand enableCCAndDisableAudio = new CatsCommand().add(enableCC).add( disableAudio );
       int count = 0;
       while(enableCCAndDisableAudio.hasNext()){
           CatsCommand nextCommand = enableCCAndDisableAudio.next();
           assertNotNull( nextCommand );
           count++;
           if(count  > 9){
               fail("more stpes than expected");
           }
       }
    }

}
