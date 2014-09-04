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
package com.comcast.cats.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.view.DirectoryParser;
import com.comcast.cats.view.DocumentBean;

/**
 * Helper class to support video recorder browser UI.
 * 
 * @author ssugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class DocumentsController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DocumentsController.class );

    private TreeNode            root;

    private TreeNode            selectedDocument;

    public DocumentsController()
    {
        initRoot();
    }

    public TreeNode getRoot()
    {
        return root;
    }

    private void initRoot()
    {
        root = new DefaultTreeNode( "root", null );

        String baseDirectory = System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH );

        String host = ( ( HttpServletRequest ) FacesContext.getCurrentInstance().getExternalContext().getRequest() )
                .getServerName();

        DocumentBean node = new DocumentBean();
        node.setChilds( DirectoryParser.parse( baseDirectory, host ) );

        for ( DocumentBean year : node.getChilds() )
        {
            TreeNode years = new DefaultTreeNode( year, root );

            for ( DocumentBean month : year.getChilds() )
            {
                TreeNode months = new DefaultTreeNode( month, years );

                for ( DocumentBean day : month.getChilds() )
                {
                    TreeNode days = new DefaultTreeNode( day, months );

                    for ( DocumentBean mac : day.getChilds() )
                    {
                        TreeNode macs = new DefaultTreeNode( mac, days );

                        for ( DocumentBean file : mac.getChilds() )
                        {
                            new DefaultTreeNode( file, macs );
                        }
                    }
                }
            }
        }

    }

    public void setRoot( TreeNode root )
    {
        this.root = root;
    }

    public TreeNode getSelectedDocument()
    {
        return selectedDocument;
    }

    public void setSelectedDocument( TreeNode selectedDocument )
    {
        this.selectedDocument = selectedDocument;
    }

    public void onNodeSelect( NodeSelectEvent event )
    {
        selectedDocument = event.getTreeNode();
        LOGGER.info( "Selected:" + selectedDocument.getData() );
    }

    public void onNodeExpand( NodeExpandEvent event )
    {
        String node = event.getTreeNode().getData().toString();
        LOGGER.info( "Expanded:" + node );
    }

    public void onNodeCollapse( NodeCollapseEvent event )
    {
        String node = event.getTreeNode().getData().toString();
        LOGGER.info( "Collapsed:" + node );
    }

    public void deleteNode()
    {
        LOGGER.info( "[WEB][DOCUMENT-DELETE" );
        DocumentBean documentBean = ( DocumentBean ) selectedDocument.getData();
        LOGGER.info( documentBean.toString() );
    }
}
