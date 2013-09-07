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

//import com.ibm.team.apt.internal.common.wiki.transformer.ILexerInput;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public final class LexerInput implements ILexerInput {
	
	private static final char[] EMPTY= new char[] {};
	
	private final PushbackReader fReader;
	private int fCharPos;
	
	private boolean fEOF;
	
	public LexerInput(String str) {
                
		fReader= new PushbackReader(new StringReader(str), str.length());
	}
	
	public boolean eof() {
		return fEOF;
	}
	
	public char[] read(int len) {
		if(len == 0)
			return EMPTY;
		
		if(fEOF)
			return null;
		
		try {
			char[] cbuf= new char[len];
			int r= fReader.read(cbuf);
			if(r != len && r != -1) {
				fReader.unread(cbuf, 0, r);
			}
			
			if(r == -1) {
				fEOF= true;
			}
			
			if(r != len) {
				return null;
			}
			
			fCharPos+= len;
			return cbuf;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int read() {
		if(fEOF)
			return EOF;
		
		try {
			int read= fReader.read();
			if(read == -1) {
				fEOF= true;
				return EOF;
			}
			
			fCharPos+= 1;
			return read;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void unread(char[] cbuf) {
		if(cbuf.length == 0)
			return;
		
		try {
			fReader.unread(cbuf);
			fCharPos-= cbuf.length;
			fEOF= false;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void unread(int read) {
		if(read == -1)
			return;
		
		try {
			fReader.unread(read);
			fCharPos-= 1;
			fEOF= false;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}