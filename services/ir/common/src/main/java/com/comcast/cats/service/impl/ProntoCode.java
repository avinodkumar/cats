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

/**
 * Class to handle retrieving data from Pronto Code format.
 * @author cfrede001
 * <href>http://www.remotecentral.com/features/irdisp1.htm</href>
 */
public class ProntoCode {
	private static final float FREQUENCY_MULTIPLIER = 0.241246f;
	private static final int PRONTO_PREAMBLE_SIZE = 16;
	private static final int PRONTO_FREQ_POS = 4;
	private static final int PRONTO_CODE_LENGTH = 4;
	private static final int PRONTO_BURST_PAIRS_COUNTER_1_POS = 8;
	private static final int PRONTO_BURST_PAIRS_COUNTER_2_POS = 12;
	private String code;
	private int frequency = 0;
	private int firstBurstPairCnt = -1;
	private int secondBurstPairCnt = -1;
	private String firstBurstPair;
	private String secondBurstPair;
	
	public ProntoCode(String irCode) {
		this.code = irCode.replaceAll("\\s", "");
		if ((0 != (code.length() & 3)) || (code.length() < PRONTO_PREAMBLE_SIZE)) {
	         throw new IllegalArgumentException("Invalid PRONTO Code!");
	    }
	}
	
	private int parseFrequency() {
		frequency = 1000 * (int) (1000 / (Integer.parseInt(code.substring(
	               PRONTO_FREQ_POS, PRONTO_FREQ_POS + PRONTO_CODE_LENGTH), 16) * FREQUENCY_MULTIPLIER));
		return frequency;
	}
	
	private void parseBurstPairs() {
		firstBurstPairCnt = Integer.parseInt(code.substring(
	               PRONTO_BURST_PAIRS_COUNTER_1_POS,
	               PRONTO_BURST_PAIRS_COUNTER_1_POS + PRONTO_CODE_LENGTH), 16);
		
		secondBurstPairCnt = Integer.parseInt(code.substring(
	               PRONTO_BURST_PAIRS_COUNTER_2_POS,
	               PRONTO_BURST_PAIRS_COUNTER_2_POS + PRONTO_CODE_LENGTH), 16);
		
		/*
		 * Now parse the burst pairs.
		 * 		Burst count is pairs, multiply by 2.
		 * 		End index must include the PRONTO_PREAMBLE_SIZE.
		 */
		int firstBurstCharacterCount = firstBurstPairCnt * 2 * PRONTO_CODE_LENGTH;
		firstBurstPair = code.substring(PRONTO_PREAMBLE_SIZE, 
				firstBurstCharacterCount + PRONTO_PREAMBLE_SIZE);
		
		/**
		 * Find the starting offset and take until the end of the string.
		 */
		secondBurstPair = code.substring(PRONTO_PREAMBLE_SIZE + firstBurstCharacterCount);
	}
	
	/**
	 * Return the frequency for this Pronto command.
	 * @return
	 */
	public int getFrequency() {
		if(frequency == 0) {
			return parseFrequency();  
		}
		return frequency;
	}
	
	public int getFirstBurstPairCnt() {
		if(firstBurstPairCnt < 0) {
			parseBurstPairs();
		}
		return firstBurstPairCnt;
	}

	public String getFirstBurstPair() {
		if(firstBurstPairCnt < 0) {
			parseBurstPairs();
		}
		return firstBurstPair;
	}
	
	public int getSecondBurstPairCnt() {
		if(secondBurstPairCnt < 0) {
			parseBurstPairs();
		}
		return secondBurstPairCnt;
	}

	public String getSecondBurstPair() {
		if(secondBurstPairCnt < 0) {
			parseBurstPairs();
		}
		return secondBurstPair;
	}
	
	public String getBurstPairs() {
		return code.substring(PRONTO_PREAMBLE_SIZE);
	}
}
