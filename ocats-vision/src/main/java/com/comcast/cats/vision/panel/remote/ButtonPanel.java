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
package com.comcast.cats.vision.panel.remote;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * Panel to hold a group of remote buttons.
 * @author cfrede001
 *
 */
public class ButtonPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4528967150282172311L;
	private List<RemoteButton> buttons = new ArrayList<RemoteButton>();
	final GridBagConstraints c = new GridBagConstraints();
	final GridBagLayout gbl = new GridBagLayout();
	//Dimension d = new Dimension();
	
	/**
	 * Logger instance for RemotePanel.
	 */
	private static final Logger logger = Logger
			.getLogger(ButtonPanel.class);
	
	public ButtonPanel() {
		initComponents();
	}
	
	private void initComponents() {
		setLayout(gbl);
		//this.setSize(200, 100);
		//this.setPreferredSize(new Dimension(200,100));
		this.setVisible(true);
	}
	
	public void addButtonToPanel(RemoteButton button) {
		Integer col = button.getRemoteLayout().getColumn();
		Integer row = button.getRemoteLayout().getRow();
		//c.anchor = GridBagConstraints.CENTER;
		//c.fill = GridBagConstraints.NONE;
		logger.info(button.getRemoteCommand()+ 
				" gw=" + c.gridwidth + 
				" x=" + col +
				" gh=" + c.gridheight +
				" y=" + row);
		c.gridx = col;
		c.gridy = row;
		
		buttons.add(button);
		
		this.add(button, c);
	}

    public RemoteButton getButton( String buttonName )
    {
        for ( RemoteButton button : buttons )
        {

            if ( buttonName.equalsIgnoreCase( button.getText() ) )
            {
                return button;
            }
        }
        return null;
    }
}