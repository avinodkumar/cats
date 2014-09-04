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

public class KeyCodeFormatTest extends AbstractJPABaseClass {

	Logger logger = LoggerFactory.getLogger(KeyCodeFormatTest.class);

	@Test
	public void testAvailableKeyCodeFormat() {
		try {
			List kFormatList = this.eMgr.createQuery("from KeyCodeFormat")
					.getResultList();
			for (int i = 0; i < kFormatList.size(); i++) {
				KeyCodeFormat kFormat = (KeyCodeFormat) kFormatList.get(i);
				logger.debug(kFormat.getKeyCodeFormatName());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}

	@Test
	public void testAddKeyCodeFormat() {
		try {

			KeyCodeFormat kFormat = new KeyCodeFormat(3, "DCII",
					"Key Code in DCII format");
			logger.debug("Adding new keyss code format" + kFormat.toString());

			eMgr.persist(kFormat);
			eMgr.flush();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}

	@Test
	public void testRemoveKeyCodeFormat() {
		try {
			KeyCodeFormat kFormat = this.eMgr.find(KeyCodeFormat.class, 3);
			if (kFormat != null) {
				logger.debug("Removing KeyCodeFormat" + kFormat.toString());
				this.eMgr.remove(kFormat);
				this.eMgr.flush();
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
		}
		logger.debug("Done");
	}

}
