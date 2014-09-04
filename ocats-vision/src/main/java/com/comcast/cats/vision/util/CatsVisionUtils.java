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

import static com.comcast.cats.vision.util.CatsVisionConstants.FIRMWARE;
import static com.comcast.cats.vision.util.CatsVisionConstants.HOST_IP_ADDRESS_LABEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.HOST_MAC_ADDRESS_LABEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.MAKE;
import static com.comcast.cats.vision.util.CatsVisionConstants.MANUFACTURER;
import static com.comcast.cats.vision.util.CatsVisionConstants.MODEL;
import static com.comcast.cats.vision.util.CatsVisionConstants.POWER_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.REMOTE_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.SERIAL_NUMBER;
import static com.comcast.cats.vision.util.CatsVisionConstants.TRACE_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.UNIT_ADDRESS;
import static com.comcast.cats.vision.util.CatsVisionConstants.VIDEO_LOCATOR;
import static com.comcast.cats.vision.util.CatsVisionConstants.VIDEO_SELECTION_LOCATOR;

import java.awt.Image;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import com.comcast.cats.image.OCRCompareResult;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.vision.CATSVisionView;

/**
 * The CatsVisionUtils class contains methods that brings up a dialog when error
 * occurs
 * 
 * @author aswathyann
 * 
 */
public class CatsVisionUtils
{
    private static final Logger logger      = Logger.getLogger( CatsVisionUtils.class );
    private static ResourceMap  resourceMap = Application.getInstance().getContext()
                                                    .getResourceMap( CATSVisionView.class );

    /**
     * When an error occurs while locking settop, the method brings up a dialog
     * that displays the mac address and error message.
     * 
     * @param macAddress
     *            mac address to be displayed
     */
    public static void showSettopLockError( String macAddress )
    {
        JOptionPane.showMessageDialog( null, "Problems in attempting to lock  Settop[" + macAddress + "].\n"
                + "NOTE: You must be streaming the settop prior to checking the lock!\n"
                + "Please click the Streaming button to allow selecting the lock button.", "Unable to lock Settop",
                JOptionPane.ERROR_MESSAGE );
    }

    /**
     * When an error occurs while unlocking settop, the method brings up a
     * dialog that displays the mac address and error message.
     * 
     * @param macAddress
     *            mac address to be displayed
     */
    public static void showSettopUnlockError( String macAddress )
    {
        JOptionPane.showMessageDialog( null, "Problems in attempting to unlock  Settop[" + macAddress + "].\n"
                + "NOTE: You must be streaming the settop prior to checking the lock!\n"
                + "Please click the Streaming button to allow selecting the lock button.", "Unable to unlock Settop",
                JOptionPane.ERROR_MESSAGE );
    }

    /**
     * When an error occurs while releasing settop, the method brings up a
     * dialog that displays the mac address and error message.
     * 
     * @param macAddress
     *            mac address to be displayed
     * @param message
     *            error message to be displayed
     */
    public static void showSettopReleaseError( String macAddress, String message )
    {
        JOptionPane.showMessageDialog( null, "Problems in attempting to release Settop[" + macAddress + "].\n"
                + message + "\n" + "Please see catsvision.log for more details.", "Unable to release Settop",
                JOptionPane.ERROR_MESSAGE );
    }

    /**
     * When an error occurs while retrieving Settop, the method brings up a
     * dialog that displays the mac address and error message.
     * 
     * @param macAddress
     *            mac address to be displayed
     * @param message
     *            error message to be displayed
     */
    public static void showSettopError( String macAddress, String message )
    {
        JOptionPane.showMessageDialog( null, "Problems in attempting to retrieve Settop[" + macAddress + "].\n"
                + message + "\n" + "Please see catsvision.log for more details.", "Unable to retrieve Settop",
                JOptionPane.ERROR_MESSAGE );
    }

    /**
     * When an error occurs while retrieving Settop, the method brings up a
     * dialog that displays the mac address and error message.
     * 
     * @param macAddress
     *            mac address to be displayed
     * @param message
     *            error message to be displayed
     */
    public static void showError( String macAddress, String message )
    {
        JOptionPane.showMessageDialog( null, "Problems in attempting to perform the action on Settop[" + macAddress
                + "].\n" + message + "\n" + "Please see catsvision.log for more details.",
                "Unable to perform the action", JOptionPane.ERROR_MESSAGE );
    }

    /**
     * The method brings up a dialog that displays the error message.
     * 
     * @param message
     *            error message to be displayed
     */
    public static void showError( String message )
    {
        JOptionPane.showMessageDialog( null, message, "Unable to perform the action", JOptionPane.ERROR_MESSAGE );
    }

    /**
     * The method brings up a dialog that displays the error message.
     * 
     * @param title
     *            to be displayed
     * @param message
     *            error message to be displayed
     */
    public static void showException( String title, String message )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Exception : " + message );
        }
        JOptionPane.showMessageDialog( null, "Caught exception. \n" + message, title, JOptionPane.ERROR_MESSAGE );
    }

    /**
     * When Settop mac address is not found, the method brings up a dialog that
     * displays the error message.
     */
    public static void showNoMacFoundError()
    {
        JOptionPane.showMessageDialog( null, "Mac address is not present for the settop.", "No Mac address found",
                JOptionPane.ERROR_MESSAGE );
    }

    /**
     * The method brings up a dialog that displays the warning message.
     * 
     * @param title
     *            title to be displayed
     * @param message
     *            warning message to be displayed
     */
    public static void showWarning( String title, String message )
    {
        JOptionPane.showMessageDialog( null, message, "Warning :" + title, JOptionPane.WARNING_MESSAGE );
    }

    /**
     * Show pop-up when audio device is not available for the particular settop
     * 
     * @param macId
     */
    public static void showAudioNotAvailableError( String macId )
    {
        JOptionPane.showMessageDialog( null, "No audio device attached with Settop : " + macId,
                "No audio device found", JOptionPane.ERROR_MESSAGE );
    }

    /**
     * Show pop-up when audio/settop is not initialized.
     * 
     * @param macId
     */
    public static void showAudioNotInitializedError()
    {
        JOptionPane.showMessageDialog( null, resourceMap.getString( "audio.init.error" ),
                resourceMap.getString( "audio.init.error.title" ), JOptionPane.ERROR_MESSAGE );
    }

    /**
     * Show pop-up when digital controller cannot be initialized.
     * 
     * @param macId
     */
    public static void showDigtalControllerNotInitializedError()
    {
        JOptionPane.showMessageDialog( null,
                "Cannot start digital controller panel. Settop for the panel is not set properly.",
                "Unable to start Digital Controller panel.", JOptionPane.ERROR_MESSAGE );
    }

    /**
     * The method brings up a dialog that displays the info message.
     * 
     * @param title
     *            title to be displayed
     * @param message
     *            info message to be displayed
     */
    public static void showInfo( String title, String message )
    {
        JOptionPane.showMessageDialog( null, message, "Info : " + title, JOptionPane.INFORMATION_MESSAGE );
    }

    public static void showOCRResultOnSuccess( OCRCompareResult compareResult )
    {
        String message = "";
        if ( null != compareResult )
        {
            message = "Text Result: " + compareResult.getTextResult() + "\n" + "Expected Result: "
                    + compareResult.getExpectedText() + "\n" + "Accuracy: " + compareResult.getAccuracy() + "%\n"
                    + "Distance: " + compareResult.getDistance();
        }
        else
        {
            message = "Could not get a result from web services";
        }

        JOptionPane.showMessageDialog( null, message, "OCR Passed", JOptionPane.INFORMATION_MESSAGE );
    }

    public static void showOCRResultOnFailure( OCRCompareResult compareResult )
    {
        String message = "";
        if ( null != compareResult )
        {
            message = "Return Code: " + compareResult.getReturnCode() + "\n" + "Text Result: "
                    + compareResult.getTextResult() + "\n" + "Accuracy: " + compareResult.getAccuracy() + "%\n"
                    + "Distance: " + compareResult.getDistance() + "\n" + "Error Message: ";

            if ( null != compareResult.getErrorMsg() )
            {
                message = message + compareResult.getErrorMsg();
            }
        }
        else
        {
            message = "Could not get a result from web services";
        }

        JOptionPane.showMessageDialog( null, message, "OCR Failed", JOptionPane.WARNING_MESSAGE );
    }

    /**
     * Get the current system date time.
     */
    public static String getDateTime()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy/MM/dd hh:mm:ss a zzz" );

        return ft.format( dNow ) + " : ";
    }

    /**
     * Checks whether the provided string is a number. Additionally checks if
     * the number fits within a maximum length. provide maxLength as negative to
     * skip length check.
     * 
     * @param string
     * @param maxLength
     * @return
     */
    public static boolean isValidNumberAndLength( String string, int maxLength )
    {

        boolean isValidString = true;
        try
        {
            Integer.parseInt( string );
            if ( maxLength > -1 && string != null && string.length() > maxLength )
            {
                isValidString = false;
            }
        }
        catch ( NumberFormatException e )
        {
            isValidString = false;
        }
        return isValidString;
    }

    /**
     * Checks if the passed in number is a valid channel number
     * 
     * @param channelNumber
     * @return - true/false
     */
    public static boolean isValidChannelNumber( String channelNumber )
    {
        // The regular expression that should be used to validate a direct tune
        // channel.
        Pattern CHANNEL_VALIDATOR = Pattern.compile( "\\d{1,4}" );
        return CHANNEL_VALIDATOR.matcher( channelNumber ).matches();
    }

    public static String URINullHelper( final URI uri )
    {
        if ( uri == null )
        {
            return "Not Found";
        }
        return uri.toString();
    }

    public static String nullChecker( final String input )
    {
        if ( input == null )
        {
            return "Not Found";
        }
        return input.toString();
    }

    /**
     * Shows dialog for image comparison
     */
    public static void showImageCompareDialog( String message, int messageLevel )
    {
        JOptionPane.showMessageDialog( null, message, "Load Failed", messageLevel );
    }

    /**
     * Get SettopInfo ToolTip Text
     * 
     * @param settopInfo
     * @return SettopInfo ToolTip Text
     */
    public static String getSettopInfoToolTipText( final SettopInfo settopInfo )
    {
        return new String( "<html>" + HOST_MAC_ADDRESS_LABEL + " : " + nullChecker( settopInfo.getHostMacAddress() )
                + "<br/>" + HOST_IP_ADDRESS_LABEL + " : " + nullChecker( settopInfo.getHostIpAddress() ) + "<br/>"
                + MAKE + " : " + nullChecker( settopInfo.getMake() ) + "<br/>" + MANUFACTURER + " : "
                + nullChecker( settopInfo.getManufacturer() ) + "<br/>" + MODEL + " : "
                + nullChecker( settopInfo.getModel() ) + "<br/>" + SERIAL_NUMBER + " : "
                + nullChecker( settopInfo.getSerialNumber() ) + "<br/>" + UNIT_ADDRESS + " : "
                + nullChecker( settopInfo.getUnitAddress() ) + "<br/>" + FIRMWARE + " : "
                + nullChecker( settopInfo.getFirmwareVersion() ) + "<br/>" + VIDEO_LOCATOR + " : "
                + URINullHelper( settopInfo.getVideoPath() ) + "<br/>" + REMOTE_LOCATOR + " : "
                + URINullHelper( settopInfo.getRemotePath() ) + "<br/>" + POWER_LOCATOR + " : "
                + URINullHelper( settopInfo.getPowerPath() ) + "<br/>" + TRACE_LOCATOR + " : "
                + URINullHelper( settopInfo.getAudioPath() ) + "<br/>" + VIDEO_SELECTION_LOCATOR + " : "
                + URINullHelper( settopInfo.getVideoSelectionPath() ) + "</html>" );
    }

    public static Image getApplicationIcon()
    {
        Image icon = new ImageIcon( CatsVisionUtils.class.getResource( CatsVisionConstants.APPLICATION_ICON_URL ) ).getImage();
        return icon;
    }
}
