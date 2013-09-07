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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Lexer {
	
	private static class Node {
		
		private int fCharacter;
		private List<Token> fTokens;
		private Map<Integer, Node> fChildren;
		
		public Node(int ch) {
			fCharacter= ch;
			fChildren= new HashMap<Integer, Node>(8);
		}
		
		public void addToken(Token token) {
			if(fTokens == null) {
				fTokens= new ArrayList<Token>(5);
			}
			fTokens.add(token);
		}
		
		public void addChild(Node node) {
			fChildren.put(node.fCharacter, node);
		}
		
		public Node findChild(int ch) {
			Node node= fChildren.get(ch);
			return node;
		}
	}
	
	public static final int ESCAPE_CHAR= '~';
	
	private Node fDictTree;
	private ILexerInput fInput;
	private final Stack<Symbol> fHistory= new Stack<Symbol>();
	
	public Lexer() {
		this(Token.values());
	}
	
	public Lexer(Token... tokens) {
		fDictTree= init(tokens);
	}

	/**
	 * Builds a dict tree of all the 
	 * passed in tokens 
	 */
	protected Node init(Token[] tokens) {
		
		final Node root= new Node(-1);
		
		for (Token token : tokens) {
			
			if(token.wiki == null)
				continue;
			
			final char[] chars= token.wiki.toCharArray();
			final int indexLast= chars.length - 1;

			Node current= root;
			for (int i= 0; i < chars.length; i++) {
				final char ch= chars[i];
				Node child= current.findChild(ch);
				if(child == null) {
					child= new Node(ch);
					current.addChild(child);
				}
				
				if(i == indexLast) {
					child.addToken(token);
				}
				
				current= child;
			}
		}
		
		return root;
	}
	
	// ---- SPI methods -----------------------------
	
	public void setInput(ILexerInput input) {
		fInput= input;
	}
	
	public Symbol nextSymbol() {
		return internalNextSymbol();
	}
	
	private Symbol internalNextSymbol() {
		
		if(!fHistory.isEmpty())
			return fHistory.pop();
		
		if(fInput.eof())
			return Symbol.EOF;
		
		int read = fInput.read();
		if(read != ILexerInput.EOF) {
			
			List<Token> result= checkDictTree(read);
			if(result != null) {
				Symbol symbol= new Symbol(result.get(0).wiki, result);
				return symbol;
			
			} else if(read == ESCAPE_CHAR) {
				if(checkTokens(Token.WHITESPACE, Token.TAB, Token.LF, Token.CR, Token.CR_LF, Token.EOF).isEmpty()) {
					read= fInput.read();
					if(read != ILexerInput.EOF) {
						return new Symbol((char) read, Token.CHARACTER);
					}
					
				} else {
					return new Symbol((char) read, Token.CHARACTER);
				}
				
			} else {
				return new Symbol((char) read, Token.CHARACTER);
			}
		}
		
		return Symbol.EOF;
	}

	private List<Token> checkDictTree(int ch) {
		
		Node child= fDictTree.findChild(ch);
		if(child == null)
			return null;
		
		return internalCheckDictTree(child);
	}
	
	private List<Token> internalCheckDictTree(Node node){
		
		List<Token> result= null;
		int read= fInput.read();
		Node child= node.findChild(read);
		if(child != null) {
			result= internalCheckDictTree(child);
		}
		
		if(child == null || result == null)
			fInput.unread(read);
		
		if(result == null && node.fTokens != null) {
			result= node.fTokens;
		}
		
		return result;
	}
	
	/**
	 * The symbol will be returned on the
	 * next call of {@link #nextSymbol()}. 
	 * Does not unread input.
	 * 
	 * @param symbol
	 */
	public void returnSymbol(Symbol symbol) {
		fHistory.push(symbol);
	}


	private List<Token> checkTokens(Token... tokens) {
		List<Token> result= new LinkedList<Token>();
		for (Token token : tokens) {
			Token t= checkToken(token);
			if(t != null) {
				result.add(t);
			}
		}
		return result;
	}
	
	private Token checkToken(Token token) {
		if(token.wiki == null)
			return  null;
		
		int len = token.wiki.length();
		char[] cbuf= fInput.read(len);
		if(cbuf != null) {
			fInput.unread(cbuf);
			if(token.wiki.toCharArray().equals(cbuf)) {
				return token;
			}
		}
		
		return null;
	}
}
