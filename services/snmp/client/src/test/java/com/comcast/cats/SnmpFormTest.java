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
package com.comcast.cats;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.comcast.cats.SnmpForm;

import junit.framework.TestCase;

/**
 * The class for testing the Snmp swing client.
 *
 * @author TATA
 *
 */
public class SnmpFormTest extends TestCase
{

    /**
     * Dummy.
     */
    public void testDummy()
    {

    }

    /**
     * The main method which creates a JFrame instance for holding the snmp JPanel.
     * @param args String array contains command line arguments
     */
    public static void main(final String[] args)
    {

        try
        {
            // Setting System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        final JFrame frame = new JFrame("Snmp Swing Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setBounds(500, 250, 730, 650);
        final SnmpForm newSnmpForm = new SnmpForm();
        frame.setContentPane(newSnmpForm);
        frame.pack();
        frame.setVisible(true);
    }
}
