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
package com.comcast.cats;
import com.comcast.cats.SnmpManager;
import com.comcast.cats.SnmpManagerImpl;
import com.comcast.cats.info.SnmpServiceReturnMesage;


public class Snmp4jTest {

	SnmpManager snmpManager;
	String communityName;
	String targetIP;
	int portNumber;
	String oid;
	
	public Snmp4jTest(){

		snmpManager = new SnmpManagerImpl();
	}
	
	public SnmpServiceReturnMesage testGet(String communityName, String targetIP,
			int portNumber, String oid){
		this.communityName = communityName;
		this.targetIP = targetIP;
		this.portNumber = portNumber;
		this.oid = oid;
		return snmpManager.get(oid, communityName, targetIP, portNumber);
	}
	
	
	public static void main(String[] args) {
		Snmp4jTest sj = new Snmp4jTest();
		System.out.println("Up Time "+sj.testGet("communityPassString","192.168.160.202",161,".1.3.6.1.2.1.1.1.0"));
	}

}
