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
package com.comcast.cats.keymanager.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.keymanager.entity.IrNetBoxProEntity;
import com.comcast.cats.keymanager.entity.KeyCodeFormat;
import com.comcast.cats.keymanager.entity.KeyCodes;
import com.comcast.cats.keymanager.entity.KeyLayout;
import com.comcast.cats.keymanager.entity.RedRatHubEntity;
import com.comcast.cats.keymanager.entity.RemoteType;
import com.comcast.cats.keymanager.service.KeyManagerService;

/**
 * @author thusai000
 * 
 */
@ManagedBean(name = "KeyManager")
@SessionScoped
public class KeyManager implements ValueChangeListener {

	Logger logger = LoggerFactory.getLogger(KeyManager.class);

	@EJB
	private KeyManagerService keyMgrService;

	private List<RemoteType> remoteList;
	private HtmlSelectOneMenu remoteDropDown;
	private RemoteType remote;
	private String selectRemote;
	private String selectedRemoteName;
	private String selectedRemoteDesc;

	private List<KeyCodeFormat> kcfList;
	private HtmlSelectOneMenu kcFormatDropDown;
	private KeyCodeFormat format;
	private String selectedFormat;
	private String selectedFormatName;
	private String selectedFormatDesc;

	private String key;
	private String keyCode;
	private List<KeyCodes> kcList;

	private KeyLayout layout;
	private String layoutPanel;
	private String layoutRow;
	private String layoutColumn;
	private String layoutBackColor;
	private String layoutForeColor;
	private List<KeyLayout> layoutList;
	
	private String irNetBoxDeviceIp;
	private  List<IrNetBoxProEntity>  irNetBoxList;
	private  String  irNetBoxHub;
	private IrNetBoxProEntity selectedIrNetBoxDevice;
	private IrNetBoxProEntity toDeleteIrNetBoxDevice;
	
	private String redratHubIp;
	private Integer redratHubPort;
	private  List<RedRatHubEntity>  rrHubList;
	private RedRatHubEntity selectedRRHub;
	private RedRatHubEntity toDeleteRRHub;
	private String oldHubIp;
	private Integer oldHubPort;
	
	private String oldIp;
	String hostIp ="Unknown";

	/**
	 * 
	 */
	public KeyManager() {

		this.selectedRemoteDesc = null;
		this.selectedRemoteName = null;

		this.selectedFormatDesc = null;
		this.selectedFormatName = null;
	}
	
	@PostConstruct
	public void init(){
	    irNetBoxList = this.keyMgrService.getAllIrNetBox();
	    rrHubList = this.keyMgrService.getAllRedRatHubs();
	    try
        {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        }
        catch ( UnknownHostException e )
        {
            e.printStackTrace();
        }
	}

	/**
	 * @return
	 */
	public RemoteType getRemote() {
		this.remote = this.keyMgrService.getRemote(this.selectRemote);
		return remote;
	}

	/**
	 * @param remote
	 */
	public void setRemote(RemoteType remote) {
		this.remote = remote;
	}


	/**
	 * @return
	 */
	public int getSelectedRemoteId() {

		int remoteId = -1;
		if(remote != null)
		{
			remoteId = remote.getRemoteTypeId();
		}
		return remoteId;
	}

	/**
	 * @return
	 */
	public int getSelectedFormatId() {
		int formatId = -1;
		if(format != null)
		{
			formatId =  format.getKeyCodeFormatId();
		}
		return formatId;
	}

	/**
	 * @param name
	 */
	public void setSelectedRemoteName(String name) {
		this.selectedRemoteName = name;
	}

	/**
	 * @return
	 */
	public String getSelectedRemoteName() {
		String name = "";
		if(remote != null)
		{
			name = remote.getRemoteTypeName();
		}
		return name;
	}

	/**
	 * @param name
	 */
	public void setSelectedFormatName(String name) {
		this.selectedFormatName = name;
	}

	/**
	 * @return
	 */
	public String getSelectedFormatName() {
		String name = "";
		if(format != null)
		{
			name = format.getKeyCodeFormatName();
		}
		return name;
	}

	/**
	 * @param desc
	 */
	public void setSelectedRemoteDesc(String desc) {
		this.selectedRemoteDesc = desc;
	}

	/**
	 * @return
	 */
	public String getSelectedRemoteDesc() {
		String desc = "";
		if(remote != null)
		{
			desc = remote.getRemoteTypeDescription();
		}
		return desc;
	}

	/**
	 * @param desc
	 */
	public void setSelectedFormatDesc(String desc) {
		this.selectedFormatDesc = desc;
	}

	/**
	 * @return
	 */
	public String getSelectedFormatDesc() {
		String desc = "";
		if(format != null)
		{
			desc = format.getKeyCodeFormatDescription();
		}
		return desc;
	}

	/**
	 * 
	 */
	public void setRemoteList() {
		this.remoteList = this.keyMgrService.getAvailableRemotes();
	}

	/**
	 * @return
	 */
	public List<RemoteType> getRemoteList() {
		if (remoteList == null) {
			this.setRemoteList();
		}
		return remoteList;
	}

	/**
	 * 
	 */
	public void setKcList() {
		kcList = this.keyMgrService.getAvailableKeys(this.selectRemote,
				this.selectedFormat);
	}

	/**
	 * @return
	 */
	public List<KeyCodes> getKcList() {
		kcList = this.keyMgrService.getAvailableKeys(this.selectRemote,
				this.selectedFormat);
		return kcList;
	}
	
	/**
	 * 
	 */
	public void setLayoutList() {
		layoutList = this.keyMgrService.getLayouts(this.selectRemote,
				this.selectedFormat);
	}

	/**
	 * @return
	 */
	public List<KeyLayout> getLayoutList() {
		layoutList = this.keyMgrService.getLayouts(this.selectRemote,
				this.selectedFormat);
		return layoutList;
	}

	/**
	 * 
	 */
	public void setkcfList() {
		this.kcfList = this.keyMgrService.getAvailableKeyCodeFormats();
	}

	/**
	 * @return
	 */
	public List getkcfList() {
		setkcfList();
		return kcfList;
	}

	/**
	 * @return
	 */
	public List getParamKey() {
		FacesContext context = FacesContext.getCurrentInstance();
		String remoteType = (String) context.getExternalContext()
				.getRequestParameterMap().get("paramName");
		String formatName = (String) context.getExternalContext()
				.getRequestParameterMap().get("formatName");
		this.kcList = this.keyMgrService.getAvailableKeys(remoteType,
				formatName);
		return kcList;
	}

	/**
	 * @return
	 */
	public HtmlSelectOneMenu getSelectRemoteType() {
		remoteDropDown = new HtmlSelectOneMenu();
		final Collection<SelectItem> list = new ArrayList<SelectItem>();

		initComponents();

		for (RemoteType r : this.remoteList) {
			list.add(new SelectItem(r.getRemoteTypeName()));
		}
		final UISelectItems items = new UISelectItems();
		items.setValue(list);
		remoteDropDown.getChildren().add(items);

		return remoteDropDown;
	}
	
	/**
	 * @param remoteTypeDD
	 */
	public void setSelectRemoteType(HtmlSelectOneMenu remoteTypeDD) {
		
		remoteDropDown = remoteTypeDD;
	}
	
	/**
	 * @param remoteTypeDD
	 */
	public void setSelectRemoteType(String remoteTypeDD) {		
		
	}
	
	/**
	 * @param kcFormatDDown
	 */
	public void setSelectKeyCodeFormat(HtmlSelectOneMenu kcFormatDDown) {
		
		kcFormatDropDown = kcFormatDDown;
	}
	
	/**
	 * @param remoteTypeDD
	 */
	public void setSelectKeyCodeFormat(String remoteTypeDD) {		
		
	}

	/**
	 * @return
	 */
	public HtmlSelectOneMenu getSelectKeyCodeFormat() {

		kcFormatDropDown = new HtmlSelectOneMenu();
		final Collection<SelectItem> list = new ArrayList<SelectItem>();

		initComponents();

		for (KeyCodeFormat kcf : this.kcfList) {
			list.add(new SelectItem(kcf.getKeyCodeFormatName()));
		}
		final UISelectItems items = new UISelectItems();
		items.setValue(list);
		this.kcFormatDropDown.getChildren().add(items);

		return kcFormatDropDown;
	}

	/**
	 * @param remote
	 */
	public void setSelectRemote(String rem) {
		this.selectRemote = rem;
		this.remote = this.keyMgrService.getRemote(this.selectRemote);
	}

	/**
	 * @return
	 */
	public String getSelectRemote() {
		return this.selectRemote;
	}

	/**
	 * @param format
	 */
	public void setSelectedFormat(String format) {
		this.selectedFormat = format;
		this.format = this.keyMgrService.getFormat(format);
	}

	/**
	 * @return
	 */
	public String getSelectedFormat() {
		return this.selectedFormat;
	}

	/**
	 * 
	 */
	private void initComponents() {
		// update remote list
		this.setRemoteList();
		// update selected remote
		if (this.remoteList.size() > 0) {
			this.remote = this.remoteList.get(0);

			// update key value for selected remite
			this.selectRemote = this.remote.getRemoteTypeName();
		}

		// update remote list
		this.setkcfList();
		// update selected remote
		if (this.kcfList.size() > 0) {
			this.format = this.kcfList.get(0);

			// update key value for selected remite
			this.selectedFormat = this.format.getKeyCodeFormatName();
		}	
	}

	/**
	 * 
	 */
	public void refreshPage() {
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();
		ViewHandler handler = context.getApplication().getViewHandler();
		UIViewRoot root = handler.createView(context, viewId);
		root.setViewId(viewId);
		context.setViewRoot(root);
	}

	/**
	 * 
	 */
	public void addRemote() {
		try {
			RemoteType tmpRemote = new RemoteType(selectedRemoteName, selectedRemoteDesc);
			this.keyMgrService.addRemoteType(tmpRemote);
			refreshPage();
		} catch (javax.ejb.EJBTransactionRolledbackException trb) {
			this.displayMsg("Could not add remote: " + trb.getMessage());
		}
	}

	/**
	 * 
	 */
	public void addFormat() {
		try {
			this.keyMgrService.addKeyCodeFormat(new KeyCodeFormat(selectedFormatName, selectedFormatDesc));
			refreshPage();
		} catch (javax.ejb.EJBTransactionRolledbackException trb) {
			this.displayMsg("Could not add format: " + trb.getMessage());
		}
	}

	/**
	 * 
	 */
	public void updateRemote() {	
		if (this.selectedRemoteDesc != null) {
			this.remote.setRemoteTypeDescription(selectedRemoteDesc);
		}

		if (this.selectedRemoteName != null) {
			this.remote.setRemoteTypeName(selectedRemoteName);
		}
		this.keyMgrService.updateRemoteType(remote);
	}

	/**
	 * 
	 */
	public void updateFormat() {
	
		if (this.selectedFormatDesc != null) {
			this.format.setKeyCodeFormatDescription(selectedFormatDesc);
		}

		if (this.selectedFormatName != null) {
			this.format.setKeyCodeFormatName(selectedFormatName);
		}
		this.keyMgrService.updateKeyCodeFormat(format);
	}

	/**
	 * 
	 */
	public void deleteRemote() {
		this.keyMgrService.deleteRemoteType(this.remote);
		refreshPage();

	}

	/**
	 * 
	 */
	public void deleteFormat() {
		this.keyMgrService.deleteKeyCodeFormat(format);
		refreshPage();

	}

	/**
	 * @return
	 */
	public SelectItem[] getKeys() {
		SelectItem[] items = new SelectItem[RemoteCommand.values().length];
		int i = 0;
		for (RemoteCommand r : RemoteCommand.values()) {
			items[i++] = new SelectItem(r, r.name());
		}
		if((key == null) && (i> 0))
		{
			RemoteCommand item = (RemoteCommand)items[0].getValue();
			this.key = item.name();
		}
		return items;
	}

	/**
	 * @param k
	 */
	public void setKey(String k) {
		key = k;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param k
	 */
	public void setKeyCode(String k) {
		keyCode = k;
	}

	/**
	 * @return
	 */
	public String getKeyCode() {		
		keyCode = "";
		KeyCodes selected = this.keyMgrService.getKey(this.selectRemote,
				this.selectedFormat, this.key);
		if (selected != null) {			
			keyCode = selected.getKeyCodeValue();			
		}		
		return keyCode;
	}

	/**
	 * 
	 */
	public void addKey() {
		RemoteType _remote = this.keyMgrService.getRemote(selectRemote);
		KeyCodeFormat _format = this.keyMgrService
				.getFormat(this.selectedFormat);
		KeyCodes kCode = new KeyCodes(_remote, _format, this.key, this.keyCode);
		try {
			this.keyMgrService.addKeyCode(kCode);
		} catch (Exception e) {
			this.displayMsg("Could not add key: " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void updateKey() {
		KeyCodes selected = this.keyMgrService.getKey(this.selectRemote,
				this.selectedFormat, this.key);
		if (selected != null) {
			selected.setKeyCodeValue(this.keyCode);			
			this.keyMgrService.updateKeyCode(selected);			
		}
	}

	/**
	 * 
	 */
	public void deleteKey() {
		KeyCodes selected = this.keyMgrService.getKey(this.selectRemote,
				this.selectedFormat, this.key);
		if (selected != null) {
			this.keyMgrService.deleteKeyCode(selected);
			refreshPage();
		} else {
			displayMsg("Could not delete key code, no entity found for: "
					+ this.selectRemote + ", " + this.selectedFormat + ", "
					+ this.key);
		}
	}

	/**
	 * 
	 */
	public void addLayout() {
		RemoteType _remote = this.keyMgrService.getRemote(selectRemote);
		KeyCodeFormat _format = this.keyMgrService
				.getFormat(this.selectedFormat);

		try {
			KeyLayout _layout = new KeyLayout(_remote, _format, this.key,
					Integer.parseInt(this.layoutPanel), Integer
							.parseInt(this.layoutRow), Integer
							.parseInt(this.layoutColumn), this.layoutForeColor,
					this.layoutBackColor);
			try {
				this.keyMgrService.addKeyLayout(_layout);
				layout = _layout;
			} catch (javax.ejb.EJBTransactionRolledbackException trb) {
				this.displayMsg("Could not add layout: " + trb.getMessage());
			}
		} catch (java.lang.NumberFormatException nfe) {

			this
					.displayMsg("Could not add layout due to invalid Panel/Row/Column entry. "
							+ nfe.getMessage());
		}
	}

	/**
	 * 
	 */
	public void updateLayout() {
		KeyLayout layout = this.keyMgrService.getLayout(selectRemote,
				selectedFormat, this.key);

		if (layout != null) {
			layout.setPanel(Integer.parseInt(this.layoutPanel));
			layout.setRow(Integer.parseInt(this.layoutRow));
			layout.setColumn(Integer.parseInt(this.layoutColumn));
			layout.setBackColor(this.layoutBackColor);
			layout.setForeColor(this.layoutForeColor);
			this.keyMgrService.updateKeyLayout(layout);
			refreshPage();
		}
	}

	/**
	 * 
	 */
	public void deleteLayout() {

		KeyLayout layout = this.keyMgrService.getLayout(selectRemote,
				selectedFormat, this.key);
		if (layout != null) {
			this.keyMgrService.deleteKeyLayout(layout);
			refreshPage();
		}
		else {
			displayMsg("Could not delete layout, no entity found for: "
					+ this.selectRemote + ", " + this.selectedFormat + ", "
					+ this.key);
		}
	}

	/**
	 * @param panel
	 */
	public void setLayoutPanel(String panel) {
		this.layoutPanel = panel;
	}

	/**
	 * @return
	 */
	public String getLayoutPanel() {
		this.layoutPanel = "";
		if (layout == null) {
			layout = this.keyMgrService.getLayout(this.selectRemote,
					this.selectedFormat, this.key);
		}
		if (layout != null) {
			this.layoutPanel = "" + layout.getPanel();
		}
		return layoutPanel;
	}

	/**
	 * @param row
	 */
	public void setLayoutRow(String row) {
		this.layoutRow = row;
	}

	/**
	 * @return
	 */
	public String getLayoutRow() {

		this.layoutRow = "";
		if (layout == null) {
			layout = this.keyMgrService.getLayout(this.selectRemote,
					this.selectedFormat, this.key);
		}
		if (layout != null) {
			this.layoutRow = "" + layout.getRow();
		}
		return layoutRow;
	}

	/**
	 * @param column
	 */
	public void setLayoutColumn(String column) {
		this.layoutColumn = column;
	}

	/**
	 * @return
	 */
	public String getLayoutColumn() {

		this.layoutColumn = null;
		if (layout == null) {
			layout = this.keyMgrService.getLayout(this.selectRemote,
					this.selectedFormat, this.key);
		}
		if (layout != null) {
			this.layoutColumn = "" + layout.getColumn();
		}
		return layoutColumn;
	}

	/**
	 * @param color
	 */
	public void setLayoutBackColor(String color) {
		this.layoutBackColor = color;
	}

	/**
	 * @return
	 */
	public String getLayoutBackColor() {

		this.layoutBackColor = null;
		if (layout == null) {
			layout = this.keyMgrService.getLayout(this.selectRemote,
					this.selectedFormat, this.key);
		}
		if (layout != null) {
			this.layoutBackColor = "" + layout.getBackColor();
		}
		return layoutBackColor;
	}

	/**
	 * @param color
	 */
	public void setLayoutForeColor(String color) {
		this.layoutForeColor = color;
	}

	/**
	 * @return
	 */
	public String getLayoutForeColor() {

		this.layoutForeColor = null;
		if (layout == null) {
			layout = this.keyMgrService.getLayout(this.selectRemote,
					this.selectedFormat, this.key);
		}
		if (layout != null) {
			this.layoutForeColor = "" + layout.getForeColor();
		}
		return layoutForeColor;
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.ValueChangeListener#processValueChange(javax.faces.event.ValueChangeEvent)
	 */
	@Override
	public void processValueChange(ValueChangeEvent e)
			throws AbortProcessingException {
		String str = (String) e.toString();
	}

	/**
	 * @param message
	 */
	public void displayMsg(String message) {
		FacesMessage fm = new FacesMessage(message);
		FacesContext.getCurrentInstance().addMessage("remote", fm);
	}
	

    public String getIrNetBoxDeviceIp()
    {
        return irNetBoxDeviceIp;
    }

    public void setIrNetBoxDeviceIp( String irNetBoxDeviceIp )
    {
        this.irNetBoxDeviceIp = irNetBoxDeviceIp;
    }
	
	public  List<IrNetBoxProEntity> getIrNetBoxList()
    {
        return irNetBoxList;
    }

    public void setIrNetBoxList(  List<IrNetBoxProEntity> irNetBoxList )
    {
        this.irNetBoxList = irNetBoxList;
    }

    public IrNetBoxProEntity getSelectedIrNetBoxDevice()
    {
        return selectedIrNetBoxDevice;
    }

    public void setSelectedIrNetBoxDevice( IrNetBoxProEntity selectedIrNetBoxDevice )
    {
        this.selectedIrNetBoxDevice = selectedIrNetBoxDevice;
        if(selectedIrNetBoxDevice != null){
            oldIp = selectedIrNetBoxDevice.getIp();
        }else{
            oldIp = null;
        }
 //       selectedIrNetBoxProEntity = keyMgrService.getIrNetBox(selectedIrNetBoxDeviceIp);
    }
    
    public void resetSelectedIrNetBoxDevice(){
        if(selectedIrNetBoxDevice != null){
            selectedIrNetBoxDevice.setIp( oldIp );
            selectedIrNetBoxDevice = null;
        }
    }

    public void addIrNetBoxDevice(){
        if (irNetBoxDeviceIp != null && !irNetBoxDeviceIp.isEmpty() && keyMgrService.getIrNetBox( irNetBoxDeviceIp ) == null )
        {
    	    IrNetBoxProEntity irNetBox = new IrNetBoxProEntity(irNetBoxDeviceIp);
    	    RedRatHubEntity rrHub = keyMgrService.getRedRatHub(irNetBoxHub);
    	    irNetBox.setRedratHub(rrHub);
    	    this.keyMgrService.addIrNetBoxDevice(irNetBox);
    	    irNetBoxList = this.keyMgrService.getAllIrNetBox();
    	    irNetBoxDeviceIp = null;
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Component with Ip "+irNetBoxDeviceIp+" already exists or is empty", null));
        }
	    
	}
    
    public IrNetBoxProEntity getToDeleteIrNetBoxDevice()
    {
        return toDeleteIrNetBoxDevice;
    }

    public void setToDeleteIrNetBoxDevice( IrNetBoxProEntity toDeleteIrNetBoxDevice )
    {
        this.toDeleteIrNetBoxDevice = toDeleteIrNetBoxDevice;
    }

    public void updateIrNetBoxDevice(){

        if(selectedIrNetBoxDevice != null && selectedIrNetBoxDevice.getIp() != null && !selectedIrNetBoxDevice.getIp().isEmpty()){
            if ( !oldIp.equals(selectedIrNetBoxDevice.getIp()) && keyMgrService.getIrNetBox( selectedIrNetBoxDevice.getIp() ) == null )
            {

            	RedRatHubEntity rrHub = keyMgrService.getRedRatHub(irNetBoxHub);
            	selectedIrNetBoxDevice.setRedratHub(rrHub);
                keyMgrService.updateIrNetBoxDevice(selectedIrNetBoxDevice);
                irNetBoxList = keyMgrService.getAllIrNetBox();
                selectedIrNetBoxDevice = null;
                oldIp = null;
            }
            else if(!oldIp.equals(selectedIrNetBoxDevice.getIp()) && keyMgrService.getIrNetBox( selectedIrNetBoxDevice.getIp() ) != null)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Component with Ip "+selectedIrNetBoxDevice.getIp()+" already exists or is empty", null));
                selectedIrNetBoxDevice.setIp( oldIp );
            }
            else if(oldIp.equals(selectedIrNetBoxDevice.getIp())){
            	RedRatHubEntity rrHub = keyMgrService.getRedRatHub(irNetBoxHub);
            	selectedIrNetBoxDevice.setRedratHub(rrHub);
                keyMgrService.updateIrNetBoxDevice(selectedIrNetBoxDevice);
                irNetBoxList = keyMgrService.getAllIrNetBox();
                selectedIrNetBoxDevice = null;
                oldIp = null;
            }
        }
    }
    
    public void  deleteIrNetBoxDevice(){
        keyMgrService.deleteIrNetBoxDevice(toDeleteIrNetBoxDevice);
        irNetBoxList = keyMgrService.getAllIrNetBox();
        toDeleteIrNetBoxDevice = null;
    }

    public String getIrNetBoxHub() {
		return irNetBoxHub;
	}

	public void setIrNetBoxHub(String irNetBoxHub) {
		this.irNetBoxHub = irNetBoxHub;
	}

    public String getHostIp()
    {
        return hostIp;
    }

    public void setHostIp( String hostIp )
    {
        this.hostIp = hostIp;
    }

	public String getRedratHubIp() {
		return redratHubIp;
	}

	public void setRedratHubIp(String redratHubIp) {
		this.redratHubIp = redratHubIp;
	}

	public List<RedRatHubEntity> getRrHubList() {
		return rrHubList;
	}

	public void setRrHubList(List<RedRatHubEntity> rrHubList) {
		this.rrHubList = rrHubList;
	}

	public RedRatHubEntity getSelectedRRHub() {
		return selectedRRHub;
	}

	public void setSelectedRRHub(RedRatHubEntity selectedRRHub) {
		 this.selectedRRHub = selectedRRHub;
	        if(selectedRRHub != null){
	            oldHubIp = selectedRRHub.getIp();
	            oldHubPort = selectedRRHub.getPort();
	        }else{
	        	oldHubIp = null;
	        	oldHubPort = null;
	        }
	}

	public RedRatHubEntity getToDeleteRRHub() {
		return toDeleteRRHub;
	}

	public void setToDeleteRRHub(RedRatHubEntity toDeleteRRHub) {
		this.toDeleteRRHub = toDeleteRRHub;
	}

	public Integer getRedratHubPort() {
		return redratHubPort;
	}

	public void setRedratHubPort(Integer redratHubPort) {
		this.redratHubPort = redratHubPort;
	}
	
	public void addRedratHub(){
		if (redratHubIp != null && !redratHubIp.isEmpty() &&
				redratHubPort != null  &&
				keyMgrService.getRedRatHub( redratHubIp ) == null )
        {
    	    RedRatHubEntity rrHub = new RedRatHubEntity(redratHubIp,redratHubPort);
    	    this.keyMgrService.addRedRatHub(rrHub);
    	    rrHubList = this.keyMgrService.getAllRedRatHubs();
    	    redratHubIp = null;
    	    redratHubPort = null;
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Inputs. Either null or RedRatHub with Ip "+redratHubIp+" already exists", null));
        }
	}
	
	public void updateRedRatHub(){

		if(selectedRRHub != null && selectedRRHub.getIp() != null && !selectedRRHub.getIp().isEmpty())
		{

            if (  !oldHubIp.trim().equals(selectedRRHub.getIp().trim()) && keyMgrService.getRedRatHub( selectedRRHub.getIp() ) == null )
            {
  
                keyMgrService.updateRedRatHub(selectedRRHub);
                rrHubList = keyMgrService.getAllRedRatHubs();
                selectedRRHub = null;
                oldHubIp = null;
                oldHubPort = null;
                
            }
            else if ( !oldHubIp.trim().equals(selectedRRHub.getIp().trim()) && keyMgrService.getRedRatHub( selectedRRHub.getIp() ) != null )
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Component with Ip "+selectedRRHub.getIp()+" already exists or is empty", null));
                selectedRRHub.setIp( oldHubIp );
                selectedRRHub.setPort( oldHubPort );
            }
            else if (oldHubIp.trim().equals(selectedRRHub.getIp().trim()))
            {
            	 keyMgrService.updateRedRatHub(selectedRRHub);
                 rrHubList = keyMgrService.getAllRedRatHubs();
                 selectedRRHub = null;
                 oldHubIp = null;
                 oldHubPort = null;
            }
        }else if(selectedRRHub.getIp().isEmpty()){
        	selectedRRHub.setIp(oldHubIp); 
        }
	}
	
	 public void resetSelectedRedRatHub(){
	        if(selectedRRHub != null){
	        	selectedRRHub.setIp( oldHubIp );
	        	selectedRRHub.setPort(oldHubPort);
	        	selectedRRHub = null;
	        }
	    }
	 
	 public void deleteRedRatHub(){
		 keyMgrService.deleteRedRatHub(toDeleteRRHub);
		 rrHubList = keyMgrService.getAllRedRatHubs();
	     toDeleteRRHub = null;
	 }

}