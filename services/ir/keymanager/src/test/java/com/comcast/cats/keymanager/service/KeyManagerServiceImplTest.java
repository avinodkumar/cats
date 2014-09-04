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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.keymanager.entity.KeyCodeFormat;
import com.comcast.cats.keymanager.entity.KeyCodes;
import com.comcast.cats.keymanager.entity.RemoteType;

/**
 * 
 * Test all operations possible such as: add/delete remotes add/delete keys
 * add/delete key code formats add/delete key layouts
 * 
 * @author thusai000
 */
public class KeyManagerServiceImplTest extends AbstractService {
	KeyManagerServiceImpl keyMgr;
	
	Logger logger = LoggerFactory.getLogger(KeyManagerServiceImplTest.class);

	/* (non-Javadoc)
	 * @see com.comcast.cats.keymanager.service.AbstractService#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		keyMgr = new KeyManagerServiceImpl(this.em);
	}

	/**
	 * 
	 */
	@Test
	public void testGetAvailableRemotes() {

		if (keyMgr != null) {
			List<RemoteType> remoteList = keyMgr.getAvailableRemotes();
			for (int i = 0; i < remoteList.size(); i++) {
				RemoteType remote = (RemoteType) remoteList.get(i);
				logger.debug(remote.getRemoteTypeName());
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetAvailableKeyCodeFormat() {

		if (keyMgr != null) {
			List<KeyCodeFormat> kcfList = keyMgr.getAvailableKeyCodeFormats();
			for (int i = 0; i < kcfList.size(); i++) {
				KeyCodeFormat kcf = (KeyCodeFormat) kcfList.get(i);
				logger.debug(kcf.getKeyCodeFormatName());
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetAvailableKeys() {

		if (keyMgr != null) {
			List<KeyCodes> kcList = keyMgr.getAvailableKeys("XMP","PRONTO");
			for (int i = 0; i < kcList.size(); i++) {
				KeyCodes kc = (KeyCodes) kcList.get(i);
				logger.debug(kc.getKeyName());
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void addDeleteKey() {
		if (keyMgr != null) {
			// add Exit key to XMP Remote with Pronto Code
			RemoteType rt = keyMgr.getRemote("XMP");
			KeyCodeFormat kcf = keyMgr.getFormat("PRONTO");
			KeyCodes kc = new KeyCodes(rt, kcf, RemoteCommand.EXIT.name(),
					"60000000013001234");
			keyMgr.addKeyCode(kc);

			// assert key exists as added above
			kc = keyMgr.getKey("XMP", "PRONTO", "EXIT");
			assertNotNull(kc);

			// delete Exit key from XMP Remote
			keyMgr.deleteKeyCode(kc);
			// verify key does not exist after deletion and handle exceptions
			kc = keyMgr.getKey("XMP", "PRONTO", "EXIT");
			assertNull(kc);
		}
	}

	/**
	 * 
	 */
	@Test
	public void addDeleteKeyCodeFormat() {

		String format = "DCII";
		KeyCodeFormat kcf = new KeyCodeFormat(5, format, "DCII format");

		// add new key code format
		if (keyMgr != null) {
			keyMgr.addKeyCodeFormat(kcf);

			// verify format added matches
			assertEquals(keyMgr.getFormat(format), kcf);

			// delete DCII format added previously
			kcf = keyMgr.getFormat(format);
			keyMgr.deleteKeyCodeFormat(kcf);

			// verify format was deleted properly
			assertEquals(keyMgr.getFormat(format), null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void addDeleteRemoteType() {

		String remoteType = "SA-Legacy";
		RemoteType remote = new RemoteType(4, remoteType, remoteType
				+ " remotes");

		// add new remote
		if (keyMgr != null) {
			keyMgr.addRemoteType(remote);

			// verify remote added matches
			assertEquals(keyMgr.getRemote(remoteType), remote);

			// delete remote added previously
			remote = keyMgr.getRemote(remoteType);
			keyMgr.deleteRemoteType(remote);

			// verify remote was deleted properly
			assertEquals(keyMgr.getRemote(remoteType), null);
		}
	}
}
