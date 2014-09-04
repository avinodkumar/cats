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
package com.comcast.cats.keymanager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.keymanager.domain.Key;
import com.comcast.cats.keymanager.entity.IrNetBoxProEntity;
import com.comcast.cats.keymanager.entity.KeyCodeFormat;
import com.comcast.cats.keymanager.entity.KeyCodes;
import com.comcast.cats.keymanager.entity.KeyLayout;
import com.comcast.cats.keymanager.entity.RedRatHubEntity;
import com.comcast.cats.keymanager.entity.RemoteType;

/**
 * Implementation of KeyManager Service.  
 * @author thusai000
 */

// @Stateless(mappedName = "cats/services/KeyManagerService")
@Stateless
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
public class KeyManagerServiceImpl implements KeyManagerService
{

	Logger logger = LoggerFactory.getLogger(KeyManagerServiceImpl.class);

	@PersistenceContext(unitName = "key-manager-pu")
	private EntityManager entityManager;

    public KeyManagerServiceImpl()
    {

    }

    public KeyManagerServiceImpl( EntityManager entityManager )
    {
        this.entityManager = entityManager;
    }

    public void setEntityManager( EntityManager entityManager )
    {
        this.entityManager = entityManager;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addRemoteType(RemoteType remote) {
	//	{
		//	try {
				this.entityManager.persist(remote);
		//	} catch (javax.ejb.EJBTransactionRolledbackException trb)  {
		//		logger.warn("Cannot add remote due to " + trb.getMessage());
		//	}
		//}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addKeyCodeFormat(KeyCodeFormat kcFormat) {
		try {
			this.entityManager.persist(kcFormat);
		} catch (Exception e) {
			logger.warn("Cannot add new format due to " + e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addKeyCode(KeyCodes kCode) {
		try {
			this.entityManager.persist(kCode);
		} catch (Exception e) {
			logger.warn("Cannot add new key code due to " + e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addKeyLayout(KeyLayout layout) {
		try {
			this.entityManager.persist(layout);
		} catch (Exception e) {
			logger.warn("Cannot add new layout due to " + e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteRemoteType(RemoteType remote) {
		RemoteType toRem = this.entityManager.find(RemoteType.class, remote
				.getRemoteTypeId());
		if (toRem != null) {
			this.entityManager.remove(toRem);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteKeyCodeFormat(KeyCodeFormat kcFormat) {
		KeyCodeFormat toRem = this.entityManager.find(KeyCodeFormat.class,
				kcFormat.getKeyCodeFormatId());
		if (toRem != null) {
			this.entityManager.remove(toRem);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteKeyCode(KeyCodes kCode) {
		KeyCodes toRem = this.entityManager.find(KeyCodes.class, kCode
				.getKeyCodesId());
		if (toRem != null) {
			this.entityManager.remove(toRem);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteKeyLayout(KeyLayout layout) {
		KeyLayout toRem = this.entityManager.find(KeyLayout.class, layout
				.getKeyLayoutId());
		if (toRem != null) {
			this.entityManager.remove(toRem);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RemoteType updateRemoteType(RemoteType remote) {
		return (this.entityManager.merge(remote));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KeyCodeFormat updateKeyCodeFormat(KeyCodeFormat kcFormat) {
		return (this.entityManager.merge(kcFormat));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KeyCodes updateKeyCode(KeyCodes kCode) {
		return (this.entityManager.merge(kCode));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KeyLayout updateKeyLayout(KeyLayout layout) {
		return (this.entityManager.merge(layout));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<RemoteType> getAvailableRemotes() {
		return (this.entityManager.createQuery("from RemoteType")
				.getResultList());
	}

	@Override
	public List<KeyCodeFormat> getAvailableKeyCodeFormats() {
		return (this.entityManager.createQuery("from KeyCodeFormat")
				.getResultList());
	}

	@Override
	public List<KeyCodes> getAvailableKeys(String remoteTypeName,
			String formatName) {
		Query q = this.entityManager
				.createNamedQuery("KeyCodes.FindByTypeFormat");
		q.setParameter("remoteType", remoteTypeName);
		q.setParameter("formatName", formatName);
		List<KeyCodes> kcList = q.getResultList();
		return kcList;
	}

	@Override
	public KeyCodes getKey(String remoteType, String format, String name) {
		KeyCodes kc = null;
		Query q = this.entityManager
				.createNamedQuery("KeyCodes.FindByTypeFormatName");
		q.setParameter("formatName", format);
		q.setParameter("remoteType", remoteType);
		q.setParameter("keyName", name);
		try {
			kc = (KeyCodes) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("No key found based on criteria " + nre.getMessage());
		}

		return kc;
	}

	@Override
	public RemoteType getRemote(String remoteType) {
		RemoteType rt = null;
		Query q = this.entityManager.createNamedQuery("RemoteType.FindByName");
		q.setParameter("remoteType", remoteType);
		try {
			rt = (RemoteType) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("No remote found with given criteria "
					+ nre.getMessage());
		}

		return (rt);
	}

	@Override
	public KeyCodeFormat getFormat(String format) {
		KeyCodeFormat kcf = null;
		Query q = this.entityManager
				.createNamedQuery("KeyCodeFormat.FindByName");
		q.setParameter("keyCodeFormat", format);
		try {
			kcf = (KeyCodeFormat) q.getSingleResult();

		} catch (NoResultException nre) {
			logger.warn("No format found with given criteria "
					+ nre.getMessage());
		}
		return (kcf);
	}

	@Override
	public KeyLayout getLayout(String remoteType, String format, String name) {

		KeyLayout kl = null;
		Query q = this.entityManager
				.createNamedQuery("KeyLayout.FindByTypeFormatName");
		q.setParameter("formatName", format);
		q.setParameter("remoteType", remoteType);
		q.setParameter("keyName", name);
		try {
			kl = (KeyLayout) q.getSingleResult();
		} catch (NoResultException nre) {
			logger
					.warn("No Layout found based on criteria "
							+ nre.getMessage());
		}
		return kl;
	}
	
	@Override
	public List<KeyLayout> getLayouts(String remoteType, String format) {
		List<KeyLayout> layoutList = null;
		Query q = this.entityManager
				.createNamedQuery("KeyLayout.FindByTypeFormat");
		q.setParameter("formatName", format);
		q.setParameter("remoteType", remoteType);
		try {
			layoutList = q.getResultList();
		} catch (NoResultException nre) {
			logger
					.warn("No Layout found based on criteria "
							+ nre.getMessage());
		}
		return layoutList;
	}

	@Override
	public List<com.comcast.cats.keymanager.domain.Remote> getRemoteTransferObjects() {
		List<com.comcast.cats.keymanager.domain.Remote> remotes = new ArrayList<com.comcast.cats.keymanager.domain.Remote>();
		for (RemoteType remote : getAvailableRemotes()) {
			com.comcast.cats.keymanager.domain.Remote r = new com.comcast.cats.keymanager.domain.Remote();
			r.setName(remote.getRemoteTypeName());
			for (KeyCodes code : remote.getKeyCodes()) {
				Key key = new Key();
				key.setFormat(code.getKeyCodeFormat().getKeyCodeFormatName());
				key.setName(code.getKeyName());
				key.setValue(code.getKeyCodeValue());
				r.getKeys().add(key);
			}
			remotes.add(r);
		}
		return remotes;
	}
	@Override
	/**
	 * Note: This method will return the PROTO( format) key layout for a specified remoteType
	 * We can also specify a list of remote in which case we will return a union of all the keys
	 * TODO: Overloaded methods to get the layout for Raw format might be required,but
	 * for the time being this seems sufficient.
	 */
	public List<com.comcast.cats.RemoteLayout> getRemoteLayoutTrasferObjects(
			String remoteTypes) {
		logger.debug(" getRemoteLayoutTrasferObjects called for{} ",
				remoteTypes);
		Set<com.comcast.cats.RemoteLayout> remoteLayoutList = new HashSet<com.comcast.cats.RemoteLayout>();
		StringTokenizer tokens = new StringTokenizer(remoteTypes, ",");
		while (tokens.hasMoreTokens()) {
			String remoteType = tokens.nextToken();
			logger.debug(" Getting layout for remoteType:{}",remoteType);
			for (KeyLayout remote : getLayouts(remoteType, "PRONTO")) {
				com.comcast.cats.RemoteLayout remoteLayout = new com.comcast.cats.RemoteLayout();
				remoteLayout.setBackgroundColor(remote.getBackColor());
				remoteLayout.setColumn(remote.getColumn());
				remoteLayout.setForegroundColor(remote.getForeColor());
				remoteLayout.setPanel(remote.getPanel());
				remoteLayout.setRemote(RemoteCommand.valueOf(remote
						.getKeyName()));
				remoteLayout.setRow(remote.getRow());
				logger.debug("Retrieved remote layout: {}",
						remoteLayout.toString());
				remoteLayoutList.add(remoteLayout);
			}
		}
		List<com.comcast.cats.RemoteLayout> returnList = new ArrayList();
		returnList.addAll(remoteLayoutList);
		return returnList;
	}
       
	   @Override
    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public void addIrNetBoxDevice( IrNetBoxProEntity irNetBox )
    {

        if ( getIrNetBox( irNetBox.getIp() ) == null )
        {
            try
            {
                entityManager.persist( irNetBox );
            }
            catch ( Exception e )
            {
                logger.warn( "Cannot add new irNetBox due to " + e.getMessage() );
                e.printStackTrace();
            }
        }
    }

    @Override
    public List< IrNetBoxProEntity > getAllIrNetBox()
    {
        List< IrNetBoxProEntity > irNetBoxes = new ArrayList< IrNetBoxProEntity >();
        try{
            Query q = entityManager.createNamedQuery( "IrNetBoxProEntity.findAllIrNetBox", IrNetBoxProEntity.class );
            irNetBoxes = q.getResultList();
        }catch(Exception e){
            logger.warn( "Cannot fetch IrNetBoxList because of "+ e.getMessage() );
        }
        return irNetBoxes;
    }

    @Override
    public IrNetBoxProEntity getIrNetBox( String selectedIrNetBoxDeviceIp )
    {
        IrNetBoxProEntity irNetBox =null;
        try{
            Query q = entityManager.createNamedQuery( "IrNetBoxProEntity.findIrNetBox", IrNetBoxProEntity.class );
            q.setParameter( "ip", selectedIrNetBoxDeviceIp );
            irNetBox = ( IrNetBoxProEntity ) q.getSingleResult();
        }catch(Exception e){
            logger.warn( "Cannot fetch IrNetBox with IP"+selectedIrNetBoxDeviceIp+" because of "+ e.getMessage() );
        }
        return irNetBox;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateIrNetBoxDevice(IrNetBoxProEntity selectedIrNetBoxProEntity) {
		try {
			entityManager.merge(selectedIrNetBoxProEntity);
		} catch (Exception e) {
			logger.warn("Cannot add new irNetBox due to " + e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteIrNetBoxDevice(IrNetBoxProEntity toDeleteIrNetBoxDevice) {
		try {
			IrNetBoxProEntity netBox = getIrNetBox(toDeleteIrNetBoxDevice
					.getIp());
			entityManager.remove(netBox);
		} catch (Exception e) {
			logger.warn("Cannot remove new irNetBox due to " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public RedRatHubEntity getRedRatHub(String redratHubIp) {
		RedRatHubEntity redratHub = null;
		try {
			Query q = entityManager.createNamedQuery(
					"RedRatHubEntity.findRedRatHub", RedRatHubEntity.class);
			q.setParameter("ip", redratHubIp);
			redratHub = (RedRatHubEntity) q.getSingleResult();
		} catch (Exception e) {
			logger.warn("Cannot fetch RedratHub with IP" + redratHubIp
					+ " because of " + e.getMessage());
		}
		return redratHub;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addRedRatHub(RedRatHubEntity rrHub) {
		if (getRedRatHub(rrHub.getIp()) == null) {
			try {
				entityManager.persist(rrHub);
			} catch (Exception e) {
				logger.warn("Cannot add new rrHub due to " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<RedRatHubEntity> getAllRedRatHubs() {
		List<RedRatHubEntity> redratHubs = new ArrayList<RedRatHubEntity>();
		try {
			Query q = entityManager.createNamedQuery(
					"RedRatHubEntity.findAllRedRatHubs", RedRatHubEntity.class);
			redratHubs = q.getResultList();
		} catch (Exception e) {
			logger.warn("Cannot fetch RedRatHub list because of "
					+ e.getMessage());
		}
		return redratHubs;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateRedRatHub(RedRatHubEntity selectedRRHub) {
		if (getIrNetBox(selectedRRHub.getIp()) == null) {
			try {
				entityManager.merge(selectedRRHub);
			} catch (Exception e) {
				logger.warn("Cannot update redrathub due to " + e.getMessage());
			}
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteRedRatHub(RedRatHubEntity toDeleteRRHub) {
		 try{
			 RedRatHubEntity rrHub = (RedRatHubEntity)getRedRatHub( toDeleteRRHub.getIp() );
	         entityManager.remove( rrHub );
	        }
	        catch ( Exception e )
	        {
	            logger.warn( "Cannot remove redratHub due to " + e.getMessage() );
	            e.printStackTrace();
	        }
		
	}
}
