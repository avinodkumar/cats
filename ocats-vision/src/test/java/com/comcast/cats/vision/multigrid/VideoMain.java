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
package com.comcast.cats.vision.multigrid;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.CatsEventDispatcherImpl;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.video.service.AxisVideoSize;
import com.comcast.cats.video.service.VideoServiceImpl;
import com.comcast.cats.vision.MockSettop;
import com.comcast.cats.vision.panel.video.VideoDisplayPanel;
import com.comcast.cats.vision.util.CatsVisionConstants;

public class VideoMain {
	CatsEventDispatcher dispatcher = new CatsEventDispatcherImpl();
	List<VideoProvider> videoProviders = new ArrayList<VideoProvider>();
	List<VideoDisplayPanel> panels = new ArrayList<VideoDisplayPanel>();
	
	String[] videoURIs = {
			"axis://192.168.160.202/?camera=1",
	};
	
	public VideoProvider createVideoProvider(String videoPath) throws URISyntaxException, MalformedURLException {
		URI videoURI = new URI(videoPath);
		VideoProvider vp = new VideoServiceImpl(dispatcher, videoURI);
		return vp;
	}
	
	public VideoDisplayPanel createVideoPanel(VideoProvider vp) {
		MockSettop settop = new MockSettop();
		settop.setVideo(vp);
		VideoDisplayPanel panel = new VideoDisplayPanel();
		dispatcher.addListener(panel, CatsEventType.VIDEO,vp);
		return panel;
	}
	
	public List<VideoDisplayPanel> getVideoPanels() throws MalformedURLException, URISyntaxException {
		for(String videoStr : videoURIs) {
			VideoProvider vp = createVideoProvider(videoStr);
			videoProviders.add(vp);
			panels.add(createVideoPanel(vp));
		}
		return panels;
	}
	
	public void startVideo() {
		for(VideoProvider video : videoProviders) {
			video.connectVideoServer();
			video.setFrameRate(5);
			video.setVideoSize(AxisVideoSize.AXIS_QCIF.getDimension());
		}
	}
	
	public void stopVideo() {
		for(VideoProvider video : videoProviders) {
			video.disconnectVideoServer();
		}
	}
	
	/**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        final VideoMain videoMain = new VideoMain();
        try {
        	List<VideoDisplayPanel> panels = videoMain.getVideoPanels();
			//videoMain.startVideo();
			JFrame frame = new JFrame(CatsVisionConstants.APPLICATION_TITLE+" Video Demo");
			frame.setSize(new Dimension(800,600));
			frame.addWindowListener(new WindowListener() {

				@Override
				public void windowActivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowClosed(WindowEvent arg0) {
					
				}

				@Override
				public void windowClosing(WindowEvent arg0) {
					// TODO Auto-generated method stub
					videoMain.stopVideo();
					System.exit(0);
				}

				@Override
				public void windowDeactivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			frame.setLayout(new GridLayout(4,4));
			for(VideoDisplayPanel panel : panels) {
				panel.setVisible(true);
				frame.add(panel);
			}
			frame.setVisible(true);
			videoMain.startVideo();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
