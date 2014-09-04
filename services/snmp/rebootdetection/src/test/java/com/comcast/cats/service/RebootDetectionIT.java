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
package com.comcast.cats.service;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.jboss.resteasy.client.ProxyFactory;

import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootHostStatus;

public class RebootDetectionIT {
	public String MAC = "00:00:00:00:00:01";
	public String IP = "172.1.1.1";
	public String IP_UPDATE = "172.1.1.2";
	public String ECM = "EC:00:00:00:00:01";
	public String ECM_UPDATE = "EC:00:00:00:00:01";
	public String URL = "http://localhost:8080/snmp-service-reboot/rest/reboot/detection/" + MAC;
	
	public static RebootDetection reboot;
	
	@BeforeSuite
	public void setup() {
		reboot = ProxyFactory.create(RebootDetection.class, URL);
	}
	
	public void assertMonitorTarget(MonitorTarget target, boolean added) {
		if(added) {
			assertEquals(ECM, target.getEcmMacAddress());
			assertEquals(IP, target.getIpAddress());
			assertEquals(RebootHostStatus.ENABLED, target.getStatus());
		} else {
			assertEquals(ECM_UPDATE, target.getEcmMacAddress());
			assertEquals(IP_UPDATE, target.getIpAddress());
			assertEquals(RebootHostStatus.DISABLED, target.getStatus());
		}
	}
	
	@Test
	public void testAdd() {
		MonitorTarget target = reboot.current();
		//No existing MAC address should be present in the DB.
		assertEquals(null, target);
		reboot.add(IP, ECM, RebootHostStatus.ENABLED);
		
		target = reboot.current();
		assertMonitorTarget(target, true);
	}
	
	@Test(dependsOnMethods={"testAdd"})
	public void testUpdate() {
		MonitorTarget target = reboot.current();
		//Let's make sure the target is currently correct.
		assertMonitorTarget(target, true);
		
		//Update the target and verify new values.
		reboot.update(IP_UPDATE, ECM_UPDATE, RebootHostStatus.DISABLED);
		target = reboot.current();
		
		assertMonitorTarget(target, false);
	}
	
	@Test(dependsOnMethods={"testUpdate"})
	public void testDelete() {
		MonitorTarget target = reboot.current();
		assertMonitorTarget(target, false);
		reboot.delete();
		
		target = reboot.current();
		assertEquals(null, target);
	}
}
