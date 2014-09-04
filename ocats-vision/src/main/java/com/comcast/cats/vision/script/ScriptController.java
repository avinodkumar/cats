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
package com.comcast.cats.vision.script;

import static com.comcast.cats.vision.script.ScriptConstants.CATS;
import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;
import static com.comcast.cats.vision.script.ScriptConstants.QTP;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG;
import static com.comcast.cats.vision.script.ScriptConstants.TESTNG_IMPORTS;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_EXTN;
import static com.comcast.cats.vision.script.ScriptConstants.TEXT_EXTN;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_PRESS_KEY_START;
import static com.comcast.cats.vision.script.ScriptConstants.PLAY_BACK_BUTTON_NAME;
import static com.comcast.cats.vision.script.ScriptConstants.CLEAR_BUTTON;
import static com.comcast.cats.vision.script.ScriptConstants.SAVE_BUTTON;
import static com.comcast.cats.vision.script.ScriptConstants.LOAD_SCRIPT_BUTTON;
import static com.comcast.cats.vision.script.ScriptConstants.TESTNG_DIAG_IMPORT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import org.apache.log4j.Logger;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventHandler;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.vision.components.CATSScriptFilter;
import com.comcast.cats.vision.components.TextFilter;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.event.ScriptPlayBackEvent;

/**
 * The main responsibility of ScriptController is to delegate the CatsEvent to
 * the appropriate Scripter implementation.
 * 
 * @author aswathyann
 * 
 */
@Named
public class ScriptController implements CatsEventHandler, ActionListener
{

    private Scripter             scripter;
    private ScriptPanel          scriptPanel;
    private String               filePath                = EMPTY_STRING;
    private final TestNGScripter testNGScripter;
    private final QtpScripter    qtpScripter;
    private final CatsScripter   catsScripter;
    private long                 qtpStartTime;
    private static StringBuilder scriptText              = new StringBuilder( EMPTY_STRING );

    private static Logger        logger                  = Logger.getLogger( ScriptController.class );
    private CATSScriptFilter     CATS_SCRIPT_FILE_FILTER = new CATSScriptFilter();
    private TextFilter           TEXT_FILE_FILTER        = new TextFilter();

    private CatsEventDispatcher  catsEventDispatcher;

    @Inject
    private ScriptController( CatsEventDispatcher catsEventDispatcher )
    {
        this.catsEventDispatcher = catsEventDispatcher;

        scriptPanel = new ScriptPanel();

        qtpScripter = new QtpScripter();
        testNGScripter = new TestNGScripter();
        catsScripter = new CatsScripter();

        /*
         * By default Script will be QtpScripter
         */
        scripter = qtpScripter;

        List< CatsEventType > catsEventTypes = new ArrayList< CatsEventType >();
        catsEventTypes.add( CatsEventType.POWER );
        catsEventTypes.add( CatsEventType.REMOTE );
        catsEventTypes.add( CatsEventType.SCREEN_CAPTURE );
        catsEventTypes.add( CatsEventType.CATS_CONFIG_BUTTON_EVENT );
        
        filePath = EMPTY_STRING;
        catsEventDispatcher.addListener( this, catsEventTypes );

        scriptPanel.getScriptPlayButton().addActionListener( this );

        scriptPanel.getClearButton().addActionListener( this );

        scriptPanel.getSaveButton().addActionListener( this );

        scriptPanel.getScriptLoadButton().addActionListener( this );

        scriptPanel.getScriptTypeComboBox().addActionListener( this );

        scriptPanel.getScriptRecordButton().addActionListener( this );
    }

    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {

        Object source = actionEvent.getSource();

        if ( source instanceof JComboBox )
        {
            comboBoxActionPerformed( ( JComboBox ) source );
        }
        else if ( source instanceof JToggleButton )
        {
            toggleButtonActionPerformed();
        }
        if ( ( source instanceof JButton ) )
        {
            buttonActionPerformed( ( JButton ) source );
        }
    }

    private void toggleButtonActionPerformed()
    {
        JComboBox comboBox = scriptPanel.getScriptTypeComboBox();

        JTextArea scriptTextArea = scriptPanel.getScriptTextArea();

        if ( scriptPanel.getScriptRecordButton().isSelected() )
        {
            scriptPanel.getScriptRecordButton().setIcon( scriptPanel.RECORD_PRESSED_ICON );

            if ( CATS.equals( comboBox.getModel().getSelectedItem() ) )
            {
                scriptTextArea.setEditable( false );
            }
        }
        else
        {
            scriptPanel.getScriptRecordButton().setIcon( scriptPanel.RECORD_ICON );

            if ( CATS.equals( comboBox.getModel().getSelectedItem() ) )
            {
                scriptTextArea.setEditable( true );

                ( ( CatsScripter ) scripter ).setLastScriptedCatsText( EMPTY_STRING );
            }
        }

    }

    private void comboBoxActionPerformed( JComboBox comboBox )
    {

        clearAllTextArea();

        filePath = EMPTY_STRING;

        JTextArea scriptTextArea = scriptPanel.getScriptTextArea();

        boolean isTextAreaEditable = scriptTextArea.isEditable();

        if ( CATS.equals( comboBox.getModel().getSelectedItem() ) )
        {
            if ( !isTextAreaEditable && !scriptPanel.getScriptRecordButton().isSelected() )
            {
                scriptTextArea.setEditable( true );
            }
            scripter = catsScripter;
            scriptPanel.getScriptLoadButton().setEnabled( true );
            scriptPanel.getScriptPlayButton().setEnabled( true );
        }
        else
        {
            if ( isTextAreaEditable )
            {
                scriptTextArea.setEditable( false );
            }

            if ( TEST_NG.equals( comboBox.getModel().getSelectedItem() ) )
            {
                scripter = testNGScripter;
                writeScript( TESTNG_IMPORTS );
            }
            else if ( QTP.equals( comboBox.getModel().getSelectedItem() ) )
            {
                scripter = qtpScripter;
            }

            scriptPanel.getScriptLoadButton().setEnabled( false );
            if( scriptPanel.getScriptPlayButton().isEnabled() )
            {
                scriptPanel.getScriptPlayButton().setEnabled( false );
            }
        }
    }

    private void buttonActionPerformed( JButton button )
    {

        if ( button.getName().equals( PLAY_BACK_BUTTON_NAME ) )
        {
            if ( isScriptTextAreaNotNullorEmpty() )
            {
                catsEventDispatcher.sendCatsEvent( new ScriptPlayBackEvent( scriptPanel.getScriptTextArea().getText(),
                        "ScriptController", this ) );
            }
        }
        else if ( button.getName().equals( CLEAR_BUTTON ) )
        {
            if ( isScriptTextAreaNotNullorEmpty() )
            {
                clearTextArea();
            }
        }
        else if ( button.getName().equals( SAVE_BUTTON ) )
        {
            if ( isScriptTextAreaNotNullorEmpty() )
            {
                saveScript();
            }
        }
        else if ( button.getName().equals( LOAD_SCRIPT_BUTTON ) )
        {

            loadScript();
        }
    }

    private boolean isScriptTextAreaNotNullorEmpty()
    {
        JTextArea scriptTextArea = scriptPanel.getScriptTextArea();
        return ( scriptTextArea != null ) && ( !scriptTextArea.getText().isEmpty() );
    }

    /**
     * Get ScriptPanel
     * 
     * @return instance of scriptPanel
     */
    public ScriptPanel getScriptPanel()
    {
        return scriptPanel;
    }

    /**
     * Get ScriptText
     * 
     * @return instance of StringBuilder
     */
    public StringBuilder getScriptText()
    {
        return scriptText;
    }

    /**
     * Clear script text
     */
    public void clearScriptText()
    {
        if ( scriptText.length() != 0 )
        {
            scriptText.replace( 0, scriptText.length(), "" );
        }
    }

    protected void clearAllTextArea()
    {
        scriptPanel.getScriptTextArea().setText( EMPTY_STRING );
        clearScriptText();

        // Clearing the last scripted cats text
        if ( scripter instanceof CatsScripter )
        {
            ( ( CatsScripter ) scripter ).setLastScriptedCatsText( EMPTY_STRING );
        }
    }

    /**
     * Clear TextArea in the Script panel
     */
    protected void clearTextArea()
    {
        clearAllTextArea();

        if ( scripter instanceof TestNGScripter )
        {
            writeScript( TESTNG_IMPORTS );
        }
    }

    /**
     * Saves Script, pops a file handler if filepath is empty
     */
    protected void saveScript()
    {
        JFileChooser scriptFileSaver = scriptPanel.getScriptFileSaver();
        String extn;
        if ( CATS.equals( scriptPanel.getScriptTypeComboBox().getModel().getSelectedItem() ) )
        {
            scriptFileSaver.removeChoosableFileFilter( TEXT_FILE_FILTER );
            scriptFileSaver.addChoosableFileFilter( CATS_SCRIPT_FILE_FILTER );
            extn = CATS_EXTN;
        }
        else
        {
            scriptFileSaver.removeChoosableFileFilter( CATS_SCRIPT_FILE_FILTER );
            scriptFileSaver.addChoosableFileFilter( TEXT_FILE_FILTER );
            extn = TEXT_EXTN;
        }

        int retVal = scriptPanel.getScriptFileSaver().showSaveDialog( scriptPanel );
        if ( retVal == JFileChooser.APPROVE_OPTION )
        {
            filePath = scriptPanel.getScriptFileSaver().getSelectedFile().getAbsolutePath();

            if ( !filePath.endsWith( extn ) )
            {
                filePath = filePath + extn;
            }
            logger.debug( "Script is saved in the path - " + filePath );
        }
        saveScriptFile();
    }

    /**
     * Saves Script file
     */
    protected void saveScriptFile()
    {

        if ( !filePath.isEmpty() )
        {

            BufferedWriter out = null;

            try
            {
                File scriptFile = new File( filePath );

                if ( !scriptFile.exists() )
                {

                    scriptFile.createNewFile();

                }
                out = new BufferedWriter( new FileWriter( scriptFile, false ) );

                String scriptText = scriptPanel.getScriptTextArea().getText();

                if ( ( out != null ) && ( !scriptText.isEmpty() ) )
                {

                    out.write( scriptText );

                    clearScriptText();

                    out.flush();
                }
            }
            catch ( IOException ioException )
            {

                logger.error( "IOException : " + ioException.getMessage() );

            }
            finally
            {
                try
                {
                    if ( out != null )
                    {
                        out.close();
                    }
                }
                catch ( IOException ioException )
                {

                    logger.error( "IOException : " + ioException.getMessage() );

                }
                logger.debug( "Script saving completed." );
                out = null;
            }
        }
    }

    @Override
    public void catsEventPerformed( CatsEvent catsEvent )
    {

        logger.debug( "CatsEvent received" );

        if ( scriptPanel.getScriptRecordButton().isSelected() )
        {
            writeScript( catsEvent );
        }
    }

    private void writeScript( CatsEvent catsEvent )
    {

        if ( scripter instanceof QtpScripter )
        {
            writeQtpScript( catsEvent );
        }
        else if ( scripter instanceof CatsScripter )
        {
            writeCatsScript( catsEvent );
        }
        else if ( scripter instanceof TestNGScripter )
        {
            writeTestNGScript( catsEvent );
        }
    }

    private void writeTestNGScript( CatsEvent catsEvent )
    {
        String output = scripter.generateScript( catsEvent );

        if ( !output.isEmpty() )
        {
            if( catsEvent instanceof ConfigButtonEvent  )
            {
                if ( null != scriptText && -1 == scriptText.indexOf( TESTNG_DIAG_IMPORT )){ 
                    scriptText.insert( 0, TESTNG_DIAG_IMPORT );
                    scriptPanel.getScriptTextArea().insert( TESTNG_DIAG_IMPORT, 0 );
                }
            }
            scriptText.append( output );

            scriptPanel.getScriptTextArea().append( output );
        }
    }

    private void writeCatsScript( CatsEvent catsEvent )
    {

        JTextArea scriptTextArea = scriptPanel.getScriptTextArea();

        if ( scriptTextArea.getText().isEmpty() )
        {
            ( ( CatsScripter ) scripter ).setLastScriptedCatsText( EMPTY_STRING );
        }
        else
        {
            if ( ( ( CatsScripter ) scripter ).getLastScriptedCatsText().isEmpty() )
            {
                ( ( CatsScripter ) scripter ).setScriptExistsOnStartRecord( true );
            }
        }

        String output = scripter.generateScript( catsEvent );

        if ( !output.trim().isEmpty() )
        {
            scriptTextArea.append( output );
        }

    }

    private void writeQtpScript( CatsEvent catsEvent )
    {
        String output = scripter.generateScript( catsEvent );

        if ( !output.isEmpty() )
        {
            updateElapsedTime( getElapsedTime( qtpStartTime ) );

            scriptText.append( output );

            scriptPanel.getScriptTextArea().append( output );

            qtpStartTime = System.currentTimeMillis();
        }

    }

    /*
     * Updates the elapsed time between two button clicks.
     */
    private void updateElapsedTime( final long count )
    {
        String text = scriptText.toString();

        JTextArea scriptTextArea = scriptPanel.getScriptTextArea();

        if ( !text.isEmpty() && !scriptTextArea.getText().isEmpty()
                && qtpScripter.getLastUpdatedText().contains( QTP_SCRIPT_PRESS_KEY_START ) )
        {

            scriptText.delete( text.lastIndexOf( "," ), text.length() );

            scriptText.append( ", " + count + "\n" );

            scriptTextArea.replaceRange( ", " + count + "\n", text.lastIndexOf( "," ), text.length() );
        }
    }

    /*
     * Gets the elapsed time
     */
    private long getElapsedTime( long startTime )
    {

        long elapsedTimeMillis = System.currentTimeMillis() - startTime;

        long elapsedTimeSec = elapsedTimeMillis / 1000;

        return elapsedTimeSec;
    }

    private void writeScript( String text )
    {

        scriptText.append( text );

        scriptPanel.getScriptTextArea().append( text );
    }

    /**
     * Method to load the script
     */
    protected void loadScript()
    {
        int retVal = scriptPanel.scriptFileLoader.showOpenDialog( scriptPanel );
        if ( JFileChooser.APPROVE_OPTION == retVal )
        {
            filePath = scriptPanel.scriptFileLoader.getSelectedFile().getAbsolutePath();
            clearTextArea();

            String script = loadScript( filePath );

            scriptText.append( script );
            scriptPanel.getScriptTextArea().append( script );
        }
    }

    private String loadScript( String filePath )
    {
        StringBuilder contents = new StringBuilder();
        BufferedReader fileReader = null;
        try
        {
            fileReader = new BufferedReader( new FileReader( filePath ) );
            String line = null;
            while ( ( line = fileReader.readLine() ) != null )
            {
                contents.append( line );
                contents.append( "\n" );
            }
        }
        catch ( IOException exception )
        {
            logger.error( exception.getMessage() );
        }
        finally
        {
            try
            {
                if ( fileReader != null )
                {
                    fileReader.close();
                }
            }
            catch ( IOException ioException )
            {
                logger.error( "IOException : " + ioException.getMessage() );
            }
            fileReader = null;
        }

        return contents.toString();
    }
}
