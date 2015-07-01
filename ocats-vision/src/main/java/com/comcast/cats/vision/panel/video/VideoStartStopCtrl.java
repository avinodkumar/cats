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
package com.comcast.cats.vision.panel.video;

import com.comcast.cats.provider.VideoProvider;

/**
 * This class provides a start/stop button panel and controls
 * the button action.
 *
 * @author jtyrre001
 */
public class VideoStartStopCtrl extends javax.swing.JPanel {

    /**
     * VideoServiceImple object.
     */
    VideoProvider videoProvider;

    /** 
     * Creates new form VideoStartStopCtrl
     */
    public VideoStartStopCtrl(VideoProvider videoProvider) {
    	this.videoProvider = videoProvider;
        initComponents();
        initProgram();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jStreamToggleButton1 = new javax.swing.JToggleButton();

        jStreamToggleButton1.setText("Stream");
        jStreamToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStreamToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jStreamToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jStreamToggleButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Streaming video toggle start/stop button control.
     *
     * @param evt
     */
    private void jStreamToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStreamToggleButton1ActionPerformed

        if(jStreamToggleButton1.isSelected()) {
            jStreamToggleButton1.setText("  Stop  ");
            videoProvider.connectVideoServer();
        } else {
            jStreamToggleButton1.setText("Stream");
            videoProvider.disconnectVideoServer();
        }
    }//GEN-LAST:event_jStreamToggleButton1ActionPerformed
    
    
    /**
     * Initialize class objects.
     */
    private void initProgram() {
        jStreamToggleButton1.setText(" Stop ");
        jStreamToggleButton1.setSelected(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jStreamToggleButton1;
    // End of variables declaration//GEN-END:variables
}