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
import java.util.EnumSet;
import java.util.ListIterator;
import java.util.Stack;
import org.openide.util.lookup.ServiceProvider;


import pl.edu.amu.wmi.kino.rtc.client.plans.transformators.IItemReferenceDetector.Reference;

/**
 * Text transformer which provides converting wiki text into html code.
 * Usage: Lookup.getDefault.lookup(WikiTransformer.class).transform(wikiString)
 * @author Michał Wojciechowski
 */
@ServiceProvider(service = WikiTransformer.class)
public class WikiTransformer {

	public enum Option {
		REFERENCES, VARIABLES, XHTML;
	}

	public static final EnumSet<Option> SAFE_OPTIONS= EnumSet.of(Option.REFERENCES, Option.VARIABLES);

	public static final EnumSet<Option> ALL_OPTIONS= EnumSet.allOf(Option.class);

	private static final class StackItem {

		public final Token token;
		public final int index;

		public StackItem(Token token, int index) {
			this.token = token;
			this.index = index;
		}
	}

	private static final class OutputItem {

		private Object fOutput;

		public OutputItem(Object output) {
			fOutput= output;
		}

		public void transform(String str, Object... args) {
			fOutput= args.length == 0 ? str : format(str, args);
		}
	}

	public static String format(String str, Object... args) {
		if(args.length == 0)
			return str;

		if(args.length == 1)
			return str.replace("%", String.valueOf(args[0])); //$NON-NLS-1$

		StringBuilder text= new StringBuilder();
		int index= 0;
		char[] chars= str.toCharArray();

		for (int i= 0; i < chars.length; i++) {
			char c= chars[i];

			if(c == '%' ) {
				if(index < args.length)
					text.append(args[index++]);
				else
					text.append("null"); //$NON-NLS-1$
			} else {
				text.append(c);
			}
		}
		return text.toString();
	}

	private IItemReferenceDetector fItemReferenceDetector;
	private IReferenceTransformer fReferenceTransformer;
	private IVariableResolver fVariableResolver;

	private final ArrayList<OutputItem> fOutput= new ArrayList<OutputItem>(70);
	private final StringBuilder fXhtmlBuffer= new StringBuilder(2 << 10);
	private final Lexer fLexer= new Lexer();
	private Symbol fCurrent;
	private EnumSet<Option> fSettings;
	private Reference fContext;

	private boolean escapeXhtmlChars= true;
	private boolean starStarColorAllowed= true;

	public String transform(String wiki) {
		return transform(wiki, ALL_OPTIONS);
	}

	public String transform(String wiki, EnumSet<Option> settings) {

		if(wiki == null)
			return Empty.STRING;

		if(wiki.length() == 0)
			return wiki;

		init(wiki, settings);
		_text();
		return fXhtmlBuffer.toString();
	}

	private void init(String wiki, EnumSet<Option> settings) {
		fSettings= settings;

		fOutput.clear();
		fXhtmlBuffer.delete(0, fXhtmlBuffer.length());
		fXhtmlBuffer.ensureCapacity((int) (wiki.length() * 2.3));
                LexerInput l = new LexerInput(wiki);
		fLexer.setInput(l);
		next();
	}

	// -- API like methods ------------------------------

	public void setVariableResolver(IVariableResolver variableResolver) {
		fVariableResolver= variableResolver;
	}

	public void setItemReferenceDetector(IItemReferenceDetector itemReferenceDetector) {
		fItemReferenceDetector= itemReferenceDetector;
	}

	public void setReferenceTransformer(IReferenceTransformer itemReferenceCreator) {
		fReferenceTransformer= itemReferenceCreator;
	}

	// --- grammar implementation -----------------------

	protected void _text() {

		// -- parse wiki syntax
		Token match= null;
		while(!matches(Token.EOF)) {

			if((match= find(Tokens.HEADINGS)) != null) {
				_headings(match);

			} else if((match= find(Token.BULLET_1, Token.NUMBERED_1)) != null) {
				_item(match);

			} else if(matches(Token.SEPARATOR)) {
				_table();

			} else if(matches(Token.HORIZONTAL_LINE)){
				_hline();

			} else {
				_paragraph();
			}

			// -- transfer so-far-output to buffer
			for (OutputItem item : fOutput) {
				fXhtmlBuffer.append(item.fOutput);
			}
			fOutput.clear();
		}
	}

	protected boolean _headings(Token token) {
		if(!accept(token))
			return false;

		int last= fOutput.size() - 1;
		fOutput.get(last).transform(TranslationScheme.start(token));
		while(!matches(Tokens.EOL) && !matches(Token.EOF, Token.LINE_BREAK)) {
			if(!_lines()) {
				if(matches(token) && (sneak(Tokens.EOL) || sneak(Token.EOF))) // in case the headings token is repeated at the end
					next();
				else
					accept();
			}
		}
		fOutput.add(new OutputItem(TranslationScheme.end(token)));

		if(matches(Token.LINE_BREAK))
			next();
		else
			accept(); // adds line break

		if(!accept(Tokens.EOL) && !accept(Token.EOF))
			return false;

		if(matches(Token.CHARACTER))
			return false;

		return true;
	}

	protected boolean _item(Token token) {
		accept(token);
		Stack<OutputItem> stack= new Stack<OutputItem>();
		starStarColorAllowed= false;
		_itemInternal(stack, token, 1 /* it always starts with level 1 items*/);
		starStarColorAllowed= true;
		while(!stack.isEmpty()) {
			fOutput.add(stack.pop());
		}
		return true;
	}

	private boolean _itemInternal(Stack<OutputItem> stack, Token token, int level) {
		boolean removeLast= true;
		if(stack.size() != level) {
			removeLast= false;
			int last= fOutput.size() - 1;
			fOutput.remove(last);

			while(level < stack.size()) {
				fOutput.add(stack.pop());
			}
			while(level > stack.size()) {
				boolean ol= EnumSet.of(Token.NUMBERED_1, Token.NUMBERED_2, Token.NUMBERED_3, Token.NUMBERED_4, Token.NUMBERED_5).contains(token);
				fOutput.add(new OutputItem(ol ? TranslationScheme.NUMBERED.start : TranslationScheme.BULLET.start));
				stack.push(new OutputItem(ol ? TranslationScheme.NUMBERED.end : TranslationScheme.BULLET.end));
			}
		}

		if(removeLast) {
			int last= fOutput.size() - 1;
			fOutput.remove(last);
		}
		fOutput.add(new OutputItem(TranslationScheme.ITEM.start));
		_formatted(true);
		fOutput.add(new OutputItem(TranslationScheme.ITEM.end));

		if(accept(Token.EOF))
			return true;

		accept(Tokens.EOL);

		// check for another list item
		Token nextToken = find(EnumSet.range(Token.BULLET_1, Token.NUMBERED_5).toArray(new Token[] {}));
		if(nextToken != null) {
			int nextLevel= _itemLevel(nextToken);
			if(nextLevel != -1){
				if(nextLevel < level || (nextLevel >= level && nextLevel - level <= 1)) {
					accept(nextToken);
					_itemInternal(stack, nextToken, nextLevel);
				}
			}
		}

		return true;
	}

	private int _itemLevel(Token token) {
		switch (token) {
		case BULLET_1:
		case NUMBERED_1:
			return 1;

		case BULLET_2:
		case NUMBERED_2:
			return 2;

		case BULLET_3:
		case NUMBERED_3:
			return 3;

		case BULLET_4:
		case NUMBERED_4:
			return 4;

		case BULLET_5:
		case NUMBERED_5:
			return 5;

		default:
			return -1;
		}
	}

	private boolean _paragraph() {
		fOutput.add(new OutputItem(TranslationScheme.PARAGRPH.start));
		_formatted(true);
		fOutput.add(new OutputItem(TranslationScheme.PARAGRPH.end));

		if(accept(Token.EOF))
			return true;

		if(!accept(Tokens.EOL))
			return false;

		if(!accept(Tokens.EOL) && !accept(Token.EOF))
			return false;

		return true;
	}

	protected boolean _table() {
		int index= fOutput.size();
		if(_tableRows(1)) {
			fOutput.add(index, new OutputItem(TranslationScheme.TABLE.start));
			fOutput.add(new OutputItem(TranslationScheme.TABLE.end));
			return true;
		}
		return false;
	}

	protected boolean _tableRows(int odd) {
		int index= fOutput.size();
		boolean match= _tableCells();

		if(match) {
			fOutput.add(index, new OutputItem(WikiTransformer.format(TranslationScheme.ROW.start, (odd%2 == 1 ? "odd" : "even") ))); //$NON-NLS-1$ //$NON-NLS-2$
			fOutput.add(new OutputItem(TranslationScheme.ROW.end));

			if(accept(Tokens.EOL)) {
				_tableRows(odd+1);
			}
		}
		return match;
	}

	/**
	 * <code>| text</code>, or <code>|= text</code>
	 */
	protected boolean _tableCells() {
		if(!accept(Token.SEPARATOR)) {
			return false;
		}

		int index= fOutput.size() - 1;
		boolean hCell= accept(Token.HEADING_1);
		if(hCell) {
			int last= fOutput.size() - 1;
			fOutput.remove(last);
		}

		if(!_formatted(false, Token.SEPARATOR)) {
			if(hCell) {
				fOutput.add(index + 1, new OutputItem(Token.HEADING_1.wiki));
			}
			fOutput.remove(index);
			return false;
		}

		fOutput.get(index).transform(hCell ? TranslationScheme.HCELL.start : TranslationScheme.CELL.start); // enclose in <td>...</td>
		fOutput.add(new OutputItem(hCell ? TranslationScheme.HCELL.end : TranslationScheme.CELL.end));
		_tableCells();
		return true;
	}

	protected boolean _formatted(boolean eol, Token... returnOn) {
		Stack<StackItem> stack= new Stack<StackItem>();
		boolean result= _formattedInternal(stack, eol, returnOn);
		while(!stack.isEmpty()) {
			Token token= stack.peek().token;
			if(EnumSet.of(Token.COLOR, Token.LINK_START, Token.IMG_START).contains(token)) { // not support (argument comes with closing element)
				stack.pop();
				continue;
			}

			fOutput.add(new OutputItem(token.wiki));
			popClose(stack, token, TranslationScheme.start(token));
		}
		return result;
	}

	private boolean _formattedInternal(Stack<StackItem> stack, boolean eol, Token... returnOn) {

		if(matches(returnOn))
			return false;

		boolean match= (eol && _lines()) ||
			_characters() ||
			_linebreak() ||
			_code() ||
			_xhtml() ||
			_variable() ||
			_formats(stack) ||
			_wikilink(stack, eol, returnOn) ||
			_foreground(stack, eol, returnOn) ||
			_background(stack, eol, returnOn) ||
			_image(stack) ||
			_unspecified();

		if(match) {
			_formattedInternal(stack, eol, returnOn);
			return true;

		} else {
			return false;
		}
	}

	protected boolean _unspecified() {
		if(matches(Token.CR, Token.CR_LF, Token.LF, Token.EOF))
			return false;

		accept();
		return true;
	}

	/**
	 * <code>\\</code>
	 */
	protected boolean _linebreak() {
		if(!accept(Token.LINE_BREAK))
			return false;

		int last= fOutput.size() - 1;
		fOutput.get(last).transform(TranslationScheme.LINE_BREAK.start);
		return true;
	}

	/**
	 * <code>**text**</code>, <code>//text//</code>,
	 * <code>__text__</code>, or <code>--text--</code>
	 */
	protected boolean _formats(Stack<StackItem> stack) {
		Token token= find(Tokens.FORMATS);
		if(token == null)
			return false;

		accept(token);

		if(findOnStack(stack, token) == null) {
			pushOpen(stack, token);

		} else {
			popClose(stack, token, TranslationScheme.start(token));
		}

		return true;
	}

	/**
	 * <code>&lt;color_name&gt;##text##</code> or <code>&lt;color_name&gt;##text##&lt;color_name&gt;</code>
	 */
	protected boolean _foreground(Stack<StackItem> stack, boolean eol, Token... returnOn) {
		Token token= find(Tokens.COLORS);
		if(token == null)
			return false;

		StackItem item= findOnStack(stack, Token.COLOR); // no nested colors
		if(item != null)
			return false;

		accept(token);
		String fgColor= token.wiki;
		int index= fOutput.size() - 1;
		if(accept(Token.COLOR)) {
			pushOpen(stack, Token.COLOR);

			if(_formattedInternal(stack, eol, merge(returnOn, Token.COLOR))) {
				if(accept(Token.COLOR)) {
					Token match;
					if((match= find(Tokens.COLORS)) != null) {
						String bgColor= match.wiki;
						next();
						popClose(stack, Token.COLOR, TranslationScheme.FG_BG_COLOR.start, fgColor, bgColor);

					} else {
						popClose(stack, Token.COLOR, TranslationScheme.FG_COLOR.start, fgColor);
					}

					fOutput.remove(index);
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * <code>##text##&lt;color_name&gt;</code>
	 */
	protected boolean _background(Stack<StackItem> stack, boolean eol, Token... returnOn) {
		if(!accept(Token.COLOR))
			return false;

		pushOpen(stack, Token.COLOR);
		if(_formattedInternal(stack, eol, merge(returnOn, Token.COLOR))) {
			if(accept(Token.COLOR)) {
				Token match;
				if((match= find(Tokens.COLORS)) != null) {
					String color= match.wiki;
					next();
					popClose(stack, Token.COLOR, TranslationScheme.BG_COLOR.start, color);
				}
			}
		}
		return true;
	}

	/**
	 * <code>{{ reference }}</code> or <code>{{ reference | title }}</code>
	 */
	protected boolean _image(Stack<StackItem> stack) {
		if(!accept(Token.IMG_START))
			return false;


		StringBuilder title= new StringBuilder();
		StringBuilder src= new StringBuilder();
		if(_until(src, Token.SEPARATOR, Token.IMG_END, Token.CR, Token.CR_LF, Token.LF, Token.EOF)) {
			pushOpen(stack, Token.IMG_START);

			title.append(src.toString());
			Reference reference= null;
			if(fSettings.contains(Option.REFERENCES) && fItemReferenceDetector != null) {
				reference = fItemReferenceDetector.detect(src.toString().trim());
				if(reference != null) {
					if(fReferenceTransformer != null)
						fReferenceTransformer.transform(reference);

					title.setLength(0);
					title.append(Token.IMG_START.wiki);
					title.append(src.toString());
					title.append(Token.IMG_END.wiki);

					src.setLength(0);
					src.append(reference.getReference());
				}
			}
			String id= reference != null ? reference.getId() : null;

			if(matches(Token.SEPARATOR)) {
				next();

				title.delete(0, title.length());
				if(_until(title, Token.IMG_END, Token.EOF, Token.CR, Token.CR_LF)) {
					if(accept(Token.IMG_END)) {
						popClose(stack, Token.IMG_START, TranslationScheme.IMAGE.start, src, title, id);
						return true;
					}
				}

			} else if(accept(Token.IMG_END)) {
				popClose(stack, Token.IMG_START, TranslationScheme.IMAGE.start, src, title, id);
				return true;

			} else {
				fOutput.add(new OutputItem(src));
				return false;
			}
		}

		return false;
	}

	/**
	 * <code>[[target]]</code> or <code>[[target | label ]]</code>
	 */
	protected boolean _wikilink(Stack<StackItem> stack, boolean eol, Token... returnOn) {
		if(!accept(Token.LINK_START))
			return false;

		StringBuilder buffer= new StringBuilder();
		if(_until(buffer, Token.SEPARATOR, Token.LINK_END, Token.CR, Token.CR_LF, Token.LF)) { //_read(buffer, Token.CHARACTER, Token.WHITESPACE, Token.TAB, Token.ITALICS /*ambiguity*/)) {
			pushOpen(stack, Token.LINK_START);

			String link= buffer.toString().trim();
			Reference reference= null;
			if(fSettings.contains(Option.REFERENCES)) {
				if(fItemReferenceDetector != null)
					reference= fItemReferenceDetector.detect(link);
			}
			fContext= reference;
			final String label= buffer.toString();
			if(reference != null) {
				if(fReferenceTransformer != null) {
					fReferenceTransformer.transform(reference);
				}
			}
			link= reference != null ? reference.getReference() : link;

			if(matches(Token.SEPARATOR)) {
				next();
				if(_formattedInternal(stack, eol, merge(returnOn, Token.LINK_END))) {
					if(accept(Token.LINK_END)) {
						popClose(stack, Token.LINK_START, TranslationScheme.start(Token.LINK_START, link));
					}
				}
				fContext= null;
				return true;

			} else if(matches(Token.LINK_END)) {
				fOutput.add(new OutputItem(label));
				accept(Token.LINK_END);
				popClose(stack, Token.LINK_START, TranslationScheme.start(Token.LINK_START, link));
				fContext= null;
				return true;

			} else {
				fOutput.add(new OutputItem(label));
				fContext= null;
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * <code>----</code>
	 */
	protected boolean _hline() {
		if(!accept(Token.HORIZONTAL_LINE))
			return false;

		int last= fOutput.size() - 1;
		fOutput.get(last).transform(TranslationScheme.HLINE.start);
		return true;
	}

	/**
	 * <code>&lt;&lt;&lt;xhtml ... &gt;&gt;&gt;</code>
	 */
	protected boolean _xhtml() {
		if(!fSettings.contains(Option.XHTML))
			return false;

		if(!accept(Token.XHTML_START))
			return false;

		final int index= fOutput.size() -1;
		escapeXhtmlChars= false;
		while(!matches(Token.XHTML_END, Token.EOF)) {
			accept();
		}
		escapeXhtmlChars= true;

		if(accept(Token.XHTML_END)) {
			fOutput.remove(index);
			fOutput.remove(fOutput.size() - 1);
			return true;
		}

		return false;
	}

	/**
	 * <code>{{{ text }}}</code>
	 */
	protected boolean _code() {

		if(!accept(Token.CODE_START)) {
			return false;
		}

		int index= fOutput.size() - 1;
		while(!matches(Token.CODE_END, Token.EOF)) {
			accept();
		}

		if(accept(Token.CODE_END)) {
			fOutput.get(index).transform(TranslationScheme.CODE.start);
			int last= fOutput.size() - 1;
			fOutput.get(last).transform(TranslationScheme.CODE.end);
			return true;
		}
		return false;
	}


	/**
	 * <code>${ &lt;var_name&gt; }</code>
	 */
	protected boolean _variable() {

		if(!accept(Token.VAR_START))
			return false;

		StringBuilder buffer= new StringBuilder();
		if(!_until(buffer, Token.VAR_END, Token.EOF, Token.CR, Token.CR_LF, Token.LF))
			return false;

		if(!accept(Token.VAR_END)) {
			fOutput.add(new OutputItem(buffer));
			return false;
		}

		String variable= buffer.toString();
		String text= null;
		boolean resolved= false;
		if(!fSettings.contains(Option.VARIABLES)) {
			resolved= true;
			text= variable;

		} else if(fVariableResolver != null){
			text= fVariableResolver.revolve(fContext, variable.toLowerCase().trim());
			resolved= text != null;
		}

		if(!resolved) {
			int last= fOutput.size() - 1;
			fOutput.add(last, new OutputItem(buffer));
			return true;
		}

		int last= fOutput.size() - 1;
		fOutput.remove(last); // }
		fOutput.remove(last - 1); // ${
		fOutput.add(new OutputItem(text));
		return true;
	}

	// tokens that may be first in a text line
	private static final Token[] LINE_START= merge(
			Tokens.COLORS,
			Token.CHARACTER, Token.WHITESPACE, Token.TAB,
			Token.LINK_START, Token.IMG_START,
			Token.ITALICS, Token.UNDERLINE, Token.STRIKE_OUT, Token.LINE_BREAK);

	protected boolean _line() {
		if(_characters())
			return true;

		if(matches(Tokens.EOL) && (sneak(LINE_START) || starStarColorAllowed && (sneak(Token.BOLD, Token.COLOR)))) {
			accept(); // consume line break
			_characters(); // consume line start
			return true;
		}

		return false;
	}

	protected boolean _lines() {
		if(!_line())
			return false;

		while(_line()) ;
		return true;
	}

	protected boolean _character() {
		return accept(Token.CHARACTER, Token.WHITESPACE, Token.TAB);
	}

	protected boolean _characters() {
		if(!_character())
			return false;

		while(_character()) ;
		return true;
	}

	protected boolean _until(StringBuilder buffer, Token first, Token... rest) {
		int length= buffer.length();
		while(!matches(first) && !matches(rest) && !matches(Token.EOF)) {
			buffer.append(fCurrent.getData());
			next();
		}
		return length != buffer.length();
	}


	// -- symbol helper ---------------------------------------------------------------------------------------------

	private void next() {
		fCurrent= fLexer.nextSymbol();
	}

	private boolean sneak(Token... tokens) {
		Symbol symbol = fLexer.nextSymbol();
		boolean matches= symbol.couldBe(tokens) != null;
		fLexer.returnSymbol(symbol);
		return matches;
	}

	private boolean matches(Token... tokens) {
		return fCurrent.couldBe(tokens) != null;
	}

	private Token find(Token... tokens) {
		return fCurrent.couldBe(tokens);
	}

	protected boolean accept(Token... token) {

		if(token.length == 0 || fCurrent.couldBe(token) != null) {
			addToOutput();
			next();
			return true;
		}
		return false;
	}

	private void addToOutput() {
		if(matches(Token.EOF))
			return;

		if(matches(Tokens.EOL)) {
			fOutput.add(new OutputItem('\n'));

		} else if(escapeXhtmlChars && matches(Token.ANGLE_OPEN)) {
			fOutput.add(new OutputItem("&lt;")); //$NON-NLS-1$

		} else if(escapeXhtmlChars && matches(Token.ANGLE_CLOSE)) {
			fOutput.add(new OutputItem("&gt;")); //$NON-NLS-1$

		} else {
			fOutput.add(new OutputItem(fCurrent.getData()));
		}
	}

	// -- stack helper ------------------

	private void pushOpen(Stack<StackItem> stack, Token token) {
		stack.add(new StackItem(token, fOutput.size() - 1));
	}

	private void popClose(Stack<StackItem> stack, Token token, String xhtml, Object... args) {
		 if(stack.isEmpty())
			 throw new IllegalStateException();

		 if(stack.peek().token == token) {
			 StackItem item = stack.pop();
			 fOutput.get(item.index).transform(xhtml, args);
			 fOutput.get(fOutput.size() - 1).transform(TranslationScheme.end(token));

		 } else {
			 StackItem item = stack.pop();
			 fOutput.get(item.index).transform(TranslationScheme.start(item.token));

			 int index= -1;
			 ListIterator<OutputItem> iter = fOutput.listIterator(fOutput.size());
			 while(iter.hasPrevious()) {
				 OutputItem tmp = iter.previous();
				 if(token.wiki.equals(tmp.fOutput)) {
					 index= iter.previousIndex() + 1;
					 break;
				 }
			 }
			 fOutput.add(index, new OutputItem(TranslationScheme.end(item.token)));

			 popClose(stack, token, xhtml, args);

			 fOutput.add(new OutputItem(item.token.wiki));
			 stack.push(new StackItem(item.token, fOutput.size() - 1));
		 }
	}

	private StackItem findOnStack(Stack<StackItem> stack, Token token) {
		if(stack.isEmpty())
			return null;

		ListIterator<StackItem> iter = stack.listIterator(stack.size());
		while(iter.hasPrevious()) {
			StackItem item = iter.previous();
			if(item.token == token) {
				return item;
			}
		}

		return null;
	}

	private static Token[] merge(Token[] a, Token first, Token... rest) {
		int len= a.length + 1 + rest.length;
		Token[] result= new Token[len];
		System.arraycopy(a, 0, result, 0, a.length);
		result[a.length]= first;
		if(rest.length != 0) {
			System.arraycopy(rest, 0, result, a.length + 1, rest.length);
		}
		return result;
	}
}

