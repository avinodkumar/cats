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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;

import javax.swing.JButton;

/**
 *
 * @author cfrede001
 */
public class RemoteButton extends JButton {

	private static final long serialVersionUID = -8629402179319406075L;
	public static final Integer BUTTON_WIDTH = 80;
	public static final Integer BUTTON_WIDTH_LONG = 100;
	public static final Integer BUTTON_HEIGHT = 18;
	public static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 10);
	private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
	private static final Insets MARGIN = new Insets(0,0,0,0);
	private RemoteLayout remoteLayout;

	public RemoteButton(RemoteLayout remoteLayout)
	{
		super();
		this.remoteLayout = remoteLayout;
		RemoteCommand command=remoteLayout.getRemote();
		setName( command.toString() );
		setText(command.toString());
		initComponent();
		
	}

	private void initComponent() {
		this.setFont(DEFAULT_FONT);
		this.setPreferredSize(BUTTON_SIZE);
		this.setSize(BUTTON_SIZE);
		this.setMargin(MARGIN);
		this.setVisible(true);
	}
	
	public RemoteCommand getRemoteCommand()
	{
		return remoteLayout.getRemote();
	}
	
	public RemoteLayout getRemoteLayout() {
		return remoteLayout;
	}
}
