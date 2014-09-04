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
package com.comcast.cats.vision.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;


@SuppressWarnings("serial")
public class PreferencePanel extends JPanel implements TreeSelectionListener, TreeWillExpandListener{
	
	JTree preferenceTree;
	DefaultMutableTreeNode top;
	HashMap<String, JScrollPane> prefereancePanelMap = new HashMap<String, JScrollPane>();
	JSplitPane splitPane;
	JPanel defPanel;

	public PreferencePanel(){	
		
		top = new DefaultMutableTreeNode("CATSVision");
		
		preferenceTree = new JTree(top);
		preferenceTree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		preferenceTree.addTreeSelectionListener(this);
		preferenceTree.addTreeWillExpandListener(this);
		setMinimumSize(new Dimension(200,800));
		JScrollPane treeView = new JScrollPane(preferenceTree);
		
		//Add the scroll panes to a split pane.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        
        defPanel = new JPanel();
        JLabel label = new JLabel("Configure settings for CATSVision");
        defPanel.add(label);
        JScrollPane defaultScrollPane = new JScrollPane(defPanel);
        prefereancePanelMap.put("CATSVision", defaultScrollPane);
        
        splitPane.setRightComponent(defaultScrollPane);

        Dimension minimumSize = new Dimension(200, 50);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(200); 
        splitPane.setPreferredSize(new Dimension(700, 300));
		
        JScrollPane scrollPane = new JScrollPane(splitPane);
		add(scrollPane);
		expandAll();
	}
	
	public void expandAll(){
		for (int i = 0; i < preferenceTree.getRowCount(); i++) {
			preferenceTree.expandRow(i);
	}
	}
	
	public void addPreference(String preferenceName, JScrollPane prefernceEditorPanel, DefaultMutableTreeNode parentNode){
		try{
			createNode(preferenceName , parentNode);
			prefereancePanelMap.put(preferenceName, prefernceEditorPanel);
		}catch (IllegalStateException e) {
			throw new IllegalStateException(e);
		}
	}

	private void createNode(String preferenceName, DefaultMutableTreeNode parentNode){
		DefaultMutableTreeNode node= new DefaultMutableTreeNode(preferenceName);
		
		if(parentNode == null){
			parentNode = top;
		}
		
		for (int i =0; i< parentNode.getChildCount();i++){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
			if(childNode.getUserObject() == preferenceName){
				throw new IllegalStateException("Parent Node already has child for the same preferance");
			}
		}
		parentNode.add(node);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLocation(300,300);
		frame.setPreferredSize(new Dimension(600,800));
		PreferencePanel pp = new PreferencePanel();
		pp.expandAll();
		frame.add(pp);
		frame.setVisible(true);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
	
		 DefaultMutableTreeNode node = (DefaultMutableTreeNode)
         preferenceTree.getLastSelectedPathComponent();
		 
        if (node == null){
        	return;
        }

        String nodeInfo = (String)node.getUserObject();
        JScrollPane scrollPane = prefereancePanelMap.get(nodeInfo);
        splitPane.setRightComponent(scrollPane);
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        preferenceTree.getLastSelectedPathComponent();
		
		if(node == top){
			throw new ExpandVetoException(event);
		}
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {

	}
}
