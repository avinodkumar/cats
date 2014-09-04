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
package com.comcast.cats.service.impl;

import java.util.StringTokenizer;

public class GlobalCacheCode extends ProntoCode {
	public static final String IR_COMMAND = "sendir"; 
	public static final String UNKOWN_COMMAND = "unkowncommand";
	public static final String COMPLETE_IR = "completeir";
	
	public GlobalCacheCode(String irCode) {
		super(irCode);
	}
	
	/**
	 * Generate a GC100 IR command for sending.
	 * @param count - Number of times to send this IR signal.
	 * @param offset - Value <= 0 with count > 1 will attempt to use the first burst pair count to calculate the GC100 offset.
	 * @param requestId - Id used for GC100 request this allows validation of response.
	 * @param outlet - GC100 outlet that should be used for IR transmission.
	 * @return GC100 command.
	 */
	public String getSendIRCommand(int count, int offset, int requestId, String outlet) {
        // Build up the GC-100 code. We know it's a slightly longer string, so
        // reserve some memory.
        StringBuilder gcCode = new StringBuilder(256).append(IR_COMMAND).append(",");

        // Add connector module and connector addresses.
        gcCode.append(outlet).append(',');

        // Add the request id (0, ..., 65535).
        gcCode.append(requestId).append(',');

        // Add the frequency.
        gcCode.append(getFrequency()).append(',');

        // Add count
        gcCode.append(count).append(",");

        //Append the offset here.
        /*
         * If offset is <= 0 passed in base this on the code being sent it.
         * Double the first burst count and add one to get the offset.
         */
        if(offset <= 0 && count > 1) {
        	offset = getFirstBurstPairCnt() * 2 + 1; 
        }
        gcCode.append(offset);
        
        // Add all the burst pairs.
        String burstPairs = this.getBurstPairs();
        //String burstPairs = this.getFirstBurstPair();
        for (int i=0, len = burstPairs.length(); i < len; i += 4) {
           gcCode.append(',').append(
                 Integer.parseInt(burstPairs.substring(i, i + 4), 16));
        }

        // Add final '\r'. Don't use '\n'. It won't work.
        gcCode.append('\r');
        return gcCode.toString();
	}
	
	/**
	 * Verify the completed GC 100 response code.
	 * @param response - GC100 return.
	 * @param requestId - Request id for sent message.
	 * @param outlet - Outlet IR was sent to.
	 * @return True if verified, false otherwise.
	 */
	public static boolean verifyCompleteIRCommand(String response, int requestId, String outlet) {
		StringTokenizer st = new StringTokenizer(response, ",");
        // Check response. We expect something like
        // "completeir,module:connector,requestId".
        return (3 == st.countTokens())
              && (COMPLETE_IR.equals(st.nextToken()))
              && ((outlet).equals(st.nextToken()))
              && (Integer.toString(requestId).equals(st.nextToken()));
	}
	
	/**
	 * Determine if response is unkowncommand from GC100
	 * @param response - GC100 response code.
	 * @return True is unkowncommand, false otherwise.
	 */
	public static boolean isUnkownCommand(String response) {
		if(response == null) {
			return false;
		}
		return response.contains(UNKOWN_COMMAND);
	}
	 
}
