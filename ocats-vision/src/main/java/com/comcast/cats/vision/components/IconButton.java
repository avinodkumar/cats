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
package com.comcast.cats.vision.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

public class IconButton extends JButton {

	public IconButton(ImageIcon imageIcon) {
		super(imageIcon);
	}

	public void paint(Graphics g){
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		if(!isEnabled()){
			getDisabledIcon().paintIcon(this, g, 0, 0);
		}else if(getModel().isPressed() && getPressedIcon() != null){
			getPressedIcon().paintIcon(this, g, 0, 0);
		} else {
				if(getIcon()!= null){
				getIcon().paintIcon(this, g, 0	,0);
			}
		}	
	}
}
