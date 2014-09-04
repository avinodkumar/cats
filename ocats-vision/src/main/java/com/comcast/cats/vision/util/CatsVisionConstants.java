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
package com.comcast.cats.vision.util;

import java.awt.Dimension;

import com.comcast.cats.video.service.AxisVideoSize;

public interface CatsVisionConstants
{

    /*
     * Power panel constants
     */

    String    POWER_ON                    = "On";

    String    POWER_OFF                   = "Off";

    String    POWER_REBOOT                = "Reboot";

    /*
     * Zoom panel Constants
     */

    String    ZOOM_VIDEO                  = "Zoomed video";

    /*
     * Settop Info Constants
     */
    String    HOST_MAC_ADDRESS_LABEL      = "Host Mac Address";

    String    HOST_IP_ADDRESS_LABEL       = "Host Ip Address";

    String    MAKE                        = "Make";

    String    MODEL                       = "Model";

    String    MANUFACTURER                = "Manufacturer";

    String    SERIAL_NUMBER               = "Serial Number";

    String    REMOTE_LOCATOR              = "Remote URI";

    String    REMOTE_TYPE                 = "Remote Type";

    String    POWER_LOCATOR               = "Power URI";

    String    VIDEO_LOCATOR               = "Video URI";

    String    TRACE_LOCATOR               = "Trace URI";

    String    VIDEO_SELECTION_LOCATOR     = "Video Selection URI";

    String    FIRMWARE                    = "Firmware";

    String    UNIT_ADDRESS                = "Unit Address";

    Dimension DEFAULT_DIMENSION           = AxisVideoSize.AXIS_D1_SQUARE.getDimension();

    int       ZOOM_FPS                    = 5;

    String    DIAG_BUTTON_NAME            = "DIAG SCREEN";
    /**
     * ConfigurableButtonPanel constants -end
     */
    String    APPLICATION_TITLE           = "CATSVision";

    String    APPLICATION_ICON_URL        = "/images/cats-icon.png";

    String    APPLICATION_LOGO_URL        = "/images/cats-logo.png";

    String    FPS                         = "FPS";

    String    IMAGE_WINDOW                = "Image Window";

    String    MY_SETTOPS                  = "My Settops";

    String    SCRIPT_PANEL                = "Scripting";

    String    PLEASE_ALLOCATE_MSG         = "Please select allocated settop(s) before performing operations on ";

    String    SNAP_IMAGE                  = "Snap Image";

    String    TRACE                       = "Trace";

    String    SETTOP_INFO                 = "Settop Info";

    String    DIRECT_TUNE_TEXT_FIELD_NAME = "DirectTuneTextField";

    /*
     * Trace Constants -start
     */

    String    STOP_LOGGING_BUTTON_TXT     = "Stop";

    String    APPEND_TRACE_BUTTON_TEXT    = "Append Log";

    String    BROWSE_BUTTON_TEXT          = "Browse";

    String    CUSTOM_LOG_START_BUTTON_TXT = "Start";

    String    START_TRACE_BUTTON_TEXT     = "Start Trace";

    String    STOP_TRACE_BUTTON_TEXT      = "Stop Trace";

    String    CLEAR_TRACE_BUTTON_TEXT     = "Clear";

    String    HEX_STRING_BUTTON_TEXT      = "HexString";

    String    SEND_COMMAND_BUTTON_TEXT    = "Send Command";

    String    TIME_STAMP_CHECK_BOX_TEXT   = "Time Stamp Prefix";
    /*
     * Trace Constants -end
     */

    /*
     * Image Compare constants - start
     */
    String    LOAD_SNAPSHOT               = "Load Snapshot";

    String    OPTIONS                     = "Options";

    String    SAVE_SNAPSHOT               = "Save Snapshot";

    String    SAVE_SNAPSHOT_AS            = "Save Snapshot As";

    String    NEW_REGION                  = "New Region";

    String    IMAGE_COMPARE               = "Image Compare";

    String    OCR                         = "OCR";

    String    LOAD_REGION                 = "Load Region";

    String    DELETE_REGION               = "Delete Region";

    String    CLEAR_CURRENT_REGION        = "Clear Current Region";

    String    TEST_CURRENT_REGION         = "Test Current Region";

    String    TEST_ALL_IMAGE_COMPARES     = "Test All Image Compares";
    /*
     * Image Compare constants - end
     */

    String    KEYBOARD_SHORTCUTS          = "Keyboard shortcuts";

    String    ABOUT                       = "About";

    String    PREFERENCES                 = "Preferences";

    String    USER_INFO                   = "User Info";

    String    EMPTY_STRING                = "";

    String    SETTOP                      = "Settop ";

    /**
     * Maximum number of settops that can be launched in CATS Vision.
     */
    int       MULTIVISION_MAX_SETTOPS     = 16;

    String    SELECT_ALL_SETTOPS          = "Select All Settops";

    String    ALLOCATED_SETTOP            = "Allocated Settop";

    String    AVAILABLE_SETTOP            = "Available Settop";

    String    SELECT_ALL                  = "Select All";

    String    UNSELECT_ALL                = "Unselect All";
}
