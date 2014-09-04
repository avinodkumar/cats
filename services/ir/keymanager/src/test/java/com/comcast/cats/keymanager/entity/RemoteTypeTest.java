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
package com.comcast.cats.keymanager.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

/**
 * @author thusai000
 * 
 */
public class RemoteTypeTest extends AbstractJPABaseClass {

	Logger logger = LoggerFactory.getLogger(RemoteTypeTest.class);

	@Test
	public void testAvailableRemotes() {
		try {
			List remoteList = this.eMgr.createQuery("from RemoteType")
					.getResultList();
			for (int i = 0; i < remoteList.size(); i++) {
				RemoteType remote = (RemoteType) remoteList.get(i);
				logger.debug(remote.getRemoteTypeName());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}

	@Test
	public void testAddRemoteType() {
		try {

			RemoteType remote = new RemoteType(5, "FAKE", "Faked out");
			System.out.println("Adding Remote" + remote.toString());

			this.eMgr.persist(remote);
			this.eMgr.flush();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}

	@Test
	public void testRemoveRemoteType() {
		try {
			RemoteType remote = this.eMgr.find(RemoteType.class, 5);
			if (remote != null) {
				logger.debug("Removing Remotes" + remote.toString());
				this.eMgr.remove(remote);
				this.eMgr.flush();
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}
}
