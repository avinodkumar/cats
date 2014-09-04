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

/**
 * Common constants used in Script module
 * 
 * @author aswathyann
 * 
 */
public interface ScriptConstants
{

    String TEST_NG                             = "TestNG";

    String QTP                                 = "QTP";

    String CATS                                = "CATS";

    String CLEAR_SCRIPT                        = "Clear Script";

    String CLEAR_BUTTON                        = "clearButton";

    String START_SCRIPTING                     = "Start Scripting";

    String STOP_SCRIPTING                      = "Stop Scripting";

    String CATS_EXTN                           = ".catscript";

    String TEXT_EXTN                           = ".txt";

    String PLAY_BACK                           = "Play Back";

    String LOAD_SCRIPT                         = "Load Script";

    String TESTNG_IMPORTS                      = "import com.comcast.cats.AbstractSettop;\n" +
    		                                     "import static com.comcast.cats.RemoteCommand.*;\n\n" ;
    		                                     
    String TESTNG_DIAG_IMPORT                  = "import com.comcast.cats.decorator.SettopDiagnostic;\n";

    String QTP_SCRIPT_PRESS_KEY_START          = "pressKeyFunction oSettop, \"";

    String QTP_SCRIPT_PRESS_KEY_END            = "\" , 0\n";

    String QTP_SCRIPT_PRESS_KEY_AND_HOLD_START = "pressKeyAndHold oSettop, \"";

    String QTP_SCRIPT_TUNE_START               = "TuneToChannel ";

    String QTP_SCRIPT_TUNE_END                 = ", oSettop \n";

    String QTP_SCRIPT_POWER_ON                 = "hardPowerOn( oSettop )";

    String QTP_SCRIPT_POWER_OFF                = "hardPowerOff( oSettop )";

    String QTP_SCRIPT_POWER_TOGGLE             = "hardPowerToggle( oSettop )";

    String TEST_NG_SCRIPT_PRESS_KEY_HOLD_START = "settop.pressKeyAndHold( ";

    String TEST_NG_SCRIPT_PRESS_KEY_START      = "settop.pressKey( ";

    String TEST_NG_SCRIPT_END                  = " );\n";

    String TEST_NG_SCRIPT_TUNE                 = "settop.tune( ";

    String TEST_NG_SCRIPT_POWER_ON             = "settop.powerOn();";

    String TEST_NG_SCRIPT_POWER_OFF            = "settop.powerOff();";

    String TEST_NG_SCRIPT_POWER_REBOOT         = "settop.reboot();";

    String CATS_SCRIPT_POWER_ON                = "POWER_ON()";

    String CATS_SCRIPT_POWER_OFF               = "POWER_OFF()";

    String CATS_SCRIPT_POWER_REBOOT            = "REBOOT()";

    String CATS_SCRIPT_PRESS                   = "press ";

    String CATS_SCRIPT_PRESS_HOLD              = "pressHold ";

    String CATS_SCRIPT_SLEEP                   = "sleep ";

    String DSL_PRESS_KEY_SYNTAX                = CATS_SCRIPT_PRESS + "[A-Z]*";

    String DSL_PRESS_AND_HOLD_SYNTAX           = CATS_SCRIPT_PRESS_HOLD + ".*[A-Z]*, [0-9]*";

    String DSL_CONSECUTIVE_PRESSES_SYNTAX      = CATS_SCRIPT_PRESS + "[[A-Z], ]*";

    String DSL_NUMBER_PRESS_SYNTAX             = CATS_SCRIPT_PRESS + "\\d";

    String NEW_LINE                            = "\n";

    String COMMA_SEPARATOR                     = "\" , ";

    String PLAY_BACK_BUTTON_NAME               = "playBackButton";

    String SAVE_BUTTON                         = "saveButton";

    String LOAD_SCRIPT_BUTTON                  = "loadScriptButton";

    String CATS_SCRIPT_TUNE                    = "tune ";

    String TEST_NG_SCRIPT_DIAG_SCREEN         = "if( settop instanceof SettopDiagnostic ){ \n"+
                                                "   SettopDiagnostic settopDiagnostic = (SettopDiagnostic)settop;\n"+
                                                "   settopDiagnostic.showDiagMenu();\n"+
                                                "}\n";
    
    String CATS_SCRIPT_DIAG_SCREEN            = "showDiagMenu()";
}
