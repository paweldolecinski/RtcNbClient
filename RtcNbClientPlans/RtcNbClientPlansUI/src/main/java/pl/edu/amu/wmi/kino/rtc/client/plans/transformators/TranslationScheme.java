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
import java.util.Arrays;
import java.util.List;



public enum TranslationScheme {
	
	HEADING_1("<h1>", "</h1>", Token.HEADING_1), //$NON-NLS-1$ //$NON-NLS-2$
	HEADING_2("<h2>", "</h2>", Token.HEADING_2), //$NON-NLS-1$ //$NON-NLS-2$
	HEADING_3("<h3>", "</h3>", Token.HEADING_3), //$NON-NLS-1$ //$NON-NLS-2$
	HEADING_4("<h4>", "</h4>", Token.HEADING_4), //$NON-NLS-1$ //$NON-NLS-2$
	HEADING_5("<h5>", "</h5>", Token.HEADING_5), //$NON-NLS-1$ //$NON-NLS-2$
	HEADING_6("<h6>", "</h6>", Token.HEADING_6), //$NON-NLS-1$ //$NON-NLS-2$
	
	ITALICS("<span style=\"font-style: italic; \">", "</span>", Token.ITALICS), //$NON-NLS-1$ //$NON-NLS-2$
	BOLD("<span style=\"font-weight: bold;\">", "</span>", Token.BOLD), //$NON-NLS-1$ //$NON-NLS-2$
	STRIKE_OUT("<span style=\"text-decoration: line-through; \">", "</span>", Token.STRIKE_OUT), //$NON-NLS-1$ //$NON-NLS-2$
	UNDERLINE("<span style=\"text-decoration: underline; \">", "</span>", Token.UNDERLINE), //$NON-NLS-1$ //$NON-NLS-2$
	HLINE("<hr />", "", Token.HORIZONTAL_LINE), //$NON-NLS-1$ //$NON-NLS-2$
	LINE_BREAK("<br />", "", Token.LINE_BREAK), //$NON-NLS-1$ //$NON-NLS-2$
	LINK("<a href=\"%\">", "</a>", Token.LINK_START, Token.LINK_END), //$NON-NLS-1$ //$NON-NLS-2$
	IMAGE("<img src=\"%\" alt=\"%\" id=\"%\">", "", Token.IMG_START, Token.IMG_END), //$NON-NLS-1$ //$NON-NLS-2$
	FG_COLOR("<span style=\"color: %;\">", "</span>", Token.COLOR), //$NON-NLS-1$ //$NON-NLS-2$
	BG_COLOR("<span style=\"background-color: %;\">", "</span>", Token.COLOR), //$NON-NLS-1$ //$NON-NLS-2$
	FG_BG_COLOR("<span style=\"color: %; background-color: %;\">", "</span>", Token.COLOR), //$NON-NLS-1$ //$NON-NLS-2$
	CODE("<pre>", "</pre>", Token.CODE_START, Token.CODE_END), //$NON-NLS-1$ //$NON-NLS-2$
	
	TABLE("<table class=\"default_WTH\">", "</table>"), //$NON-NLS-1$ //$NON-NLS-2$
	ROW("<tr class=\"%\">", "</tr>"), //$NON-NLS-1$ //$NON-NLS-2$
	CELL("<td>", "</td>"), //$NON-NLS-1$ //$NON-NLS-2$
	HCELL("<th>", "</th>"), //$NON-NLS-1$ //$NON-NLS-2$
	PARAGRPH("<p>", "</p>"), //$NON-NLS-1$ //$NON-NLS-2$
	BULLET("<ul>", "</ul>"), //$NON-NLS-1$ //$NON-NLS-2$
	NUMBERED("<ol>", "</ol>"), //$NON-NLS-1$ //$NON-NLS-2$
	ITEM("<li>", "</li>", Token.BULLET_1, Token.BULLET_2, Token.BULLET_3, Token.NUMBERED_1, Token.NUMBERED_2, Token.NUMBERED_3); //$NON-NLS-1$ //$NON-NLS-2$
	
	public final List<Token> token;
	public final String start;
	public final String end;
	
	private TranslationScheme(String start, String end, Token... token) {
		this.token= Arrays.asList(token);
		this.start= start;
		this.end= end;
	}
	
	public static String start(Token token) {
		return schemeForToken(token).start;
	}
	
	public static String start(Token token, Object... args) {
		String string = schemeForToken(token).start;
		return WikiTransformer.format(string, args);
	}
	
	public static String end(Token token) {
		return schemeForToken(token).end;
	}
	
	private static TranslationScheme schemeForToken(Token token) {
		for (TranslationScheme element : values()) {
			if(element.token.contains(token))
				return element;
		}
		
		return null;
	}
}
