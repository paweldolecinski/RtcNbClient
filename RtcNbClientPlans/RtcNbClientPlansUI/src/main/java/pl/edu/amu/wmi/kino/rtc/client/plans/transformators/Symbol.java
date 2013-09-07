/*
 * Copyright (C) 2009-2011 RtcNbClient Team (http://rtcnbclient.wmi.amu.edu.pl/)
 *
 * This file is part of RtcNbClient.
 *
 * RtcNbClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RtcNbClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RtcNbClient. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.amu.wmi.kino.rtc.client.plans.transformators;
import java.util.List;


public class Symbol {

	public final static Symbol EOF= new Symbol(null, Token.EOF);
	
	private final int sizeOfLong= 63; // sizeOf signed long
	private final int nLongs= Math.round(Token.values().length / (float) sizeOfLong); // number of longs to store all tokens
	
	private final long[] fValues= new long[nLongs];
	private final Object fData;
	
	public Symbol(Object data, Token... tokens) {
		for (Token token : tokens) {
			final int ordinal= token.ordinal();
			final int index= ordinal / sizeOfLong;
			final int shift= ordinal % sizeOfLong;
			fValues[index]|= 1L << shift;
		}
		
		this.fData= data;
	}
	
	public Symbol(Object data, List<Token> tokens) {
		for (Token token : tokens) {
			final int ordinal= token.ordinal();
			final int index= ordinal / sizeOfLong;
			final int shift= ordinal % sizeOfLong;
			fValues[index]|= 1L << shift;
		}

		this.fData = data;
	}
	
	public Object getData() {
		return fData;
	}
	
	public Token couldBe(Token... tokens) {
		for (Token token : tokens) {
			final int ordinal= token.ordinal();
			final int index= ordinal / sizeOfLong;
			final int shift= ordinal % sizeOfLong;
			if((fValues[index] & 1L << shift) != 0) {
				return token;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder output= new StringBuilder();
		Token[] values= Token.values();
		for (Token token : values) {
			if(couldBe(token) != null) {
				output.append(token.name()).append('@').append(token.ordinal());
			}
		}
		
		return String.format("tokens=[%s], data=[%s]", output, fData); //$NON-NLS-1$
	}
}
