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
package com.comcast.cats.event;

import org.junit.Test;

public class CatsEventDispatcherThreadedTest implements CatsEventHandler {
	String payload = "Fake TraceEvent here.";
	String responsePayload;
	
	@Test
	public void sendEvent() throws InterruptedException {/*
		//TraceEvent te = new TraceEvent(1, "1", this);
		TraceEvent te = new TraceEvent();
		te.setPayload("Fake TraceEvent here.");
		CatsEventDispatcher dispatcher = new CatsEventDispatcherThreaded();
		dispatcher.addListener(this, CatsEventType.TRACE);
		dispatcher.sendCatsEvent(te);
		Thread.sleep(200);
		
		assertEquals(payload, responsePayload);
	*/}
	
	@Override
	public void catsEventPerformed(CatsEvent evt) {/*
		TraceEvent te = (TraceEvent) evt;
		LOGGER.info("CatsEventPerformed");
		responsePayload = te.getPayload();
	*/}

}
