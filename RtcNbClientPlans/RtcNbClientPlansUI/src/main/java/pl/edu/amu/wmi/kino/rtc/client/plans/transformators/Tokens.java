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

public final class Tokens {

	public static final Token[] COLORS= new Token[] { Token.INDIANRED, Token.LIGHTCORAL, Token.SALMON, Token.DARKSALMON, 
		Token.LIGHTSALMON, Token.CRIMSON, Token.RED, Token.FIREBRICK, Token.DARKRED, Token.PINK, Token.LIGHTPINK, 
		Token.HOTPINK, Token.DEEPPINK, Token.MEDIUMVIOLETRED, Token.PALEVIOLETRED, Token.LIGHTSALMON, Token.CORAL, 
		Token.TOMATO, Token.ORANGERED, Token.DARKORANGE, Token.ORANGE, Token.GOLD, Token.YELLOW, Token.LIGHTYELLOW, 
		Token.LEMONCHIFFON, Token.LIGHTGOLDENRODYELLOW, Token.PAPAYAWHIP, Token.MOCCASIN, Token.PEACHPUFF, Token.PALEGOLDENROD, 
		Token.KHAKI, Token.DARKKHAKI, Token.LAVENDER, Token.THISTLE, Token.PLUM, Token.VIOLET, Token.ORCHID, Token.FUCHSIA, 
		Token.MAGENTA, Token.MEDIUMORCHID, Token.MEDIUMPURPLE, Token.BLUEVIOLET, Token.DARKVIOLET, Token.DARKORCHID, 
		Token.DARKMAGENTA, Token.PURPLE, Token.INDIGO, Token.SLATEBLUE, Token.DARKSLATEBLUE, Token.GREENYELLOW, Token.CHARTREUSE, 
		Token.LAWNGREEN, Token.LIME, Token.LIMEGREEN, Token.PALEGREEN, Token.LIGHTGREEN, Token.MEDIUMSPRINGGREEN, Token.SPRINGGREEN, 
		Token.MEDIUMSEAGREEN, Token.SEAGREEN, Token.FORESTGREEN, Token.GREEN, Token.DARKGREEN, Token.YELLOWGREEN, Token.OLIVEDRAB, 
		Token.OLIVE, Token.DARKOLIVEGREEN, Token.MEDIUMAQUAMARINE, Token.DARKSEAGREEN, Token.LIGHTSEAGREEN, Token.DARKCYAN, 
		Token.TEAL, Token.AQUA, Token.CYAN, Token.LIGHTCYAN, Token.PALETURQUOISE, Token.AQUAMARINE, Token.TURQUOISE, 
		Token.MEDIUMTURQUOISE, Token.DARKTURQUOISE, Token.CADETBLUE, Token.STEELBLUE, Token.LIGHTSTEELBLUE, Token.POWDERBLUE, 
		Token.LIGHTBLUE, Token.SKYBLUE, Token.LIGHTSKYBLUE, Token.DEEPSKYBLUE, Token.DODGERBLUE, Token.CORNFLOWERBLUE, 
		Token.MEDIUMSLATEBLUE, Token.ROYALBLUE, Token.BLUE, Token.MEDIUMBLUE, Token.DARKBLUE, Token.NAVY, Token.MIDNIGHTBLUE, 
		Token.CORNSILK, Token.BLANCHEDALMOND, Token.BISQUE, Token.NAVAJOWHITE, Token.WHEAT, Token.BURLYWOOD, Token.TAN, 
		Token.ROSYBROWN, Token.SANDYBROWN, Token.GOLDENROD, Token.DARKGOLDENROD, Token.PERU, Token.CHOCOLATE, Token.SADDLEBROWN, 
		Token.SIENNA, Token.BROWN, Token.MAROON, Token.WHITE, Token.SNOW, Token.HONEYDEW, Token.MINTCREAM, Token.AZURE, 
		Token.ALICEBLUE, Token.GHOSTWHITE, Token.WHITESMOKE, Token.SEASHELL, Token.BEIGE, Token.OLDLACE, Token.FLORALWHITE, 
		Token.IVORY, Token.ANTIQUEWHITE, Token.LINEN, Token.LAVENDERBLUSH, Token.MISTYROSE, Token.GAINSBORO, Token.LIGHTGREY, 
		Token.SILVER, Token.DARKGRAY, Token.GRAY, Token.DIMGRAY, Token.LIGHTSLATEGRAY, Token.SLATEGRAY, Token.DARKSLATEGRAY, 
		Token.BLACK };
	
	public static final Token[] BASE_COLORS= new Token[] {
		Token.BLUE, Token.DARKBLUE, 
		Token.RED, 
		Token.MAGENTA, Token.DARKMAGENTA, 
		Token.GREEN, Token.DARKGREEN, 
		Token.YELLOW, Token.ORANGERED,
		Token.WHITE, Token.GRAY, Token.DARKGRAY, Token.BLACK
	};
	
	public static final Token[] EOL= new Token[] { Token.LF, Token.CR, Token.CR_LF };
	public static final Token[] FORMATS= new Token[] { Token.BOLD, Token.ITALICS, Token.UNDERLINE, Token.STRIKE_OUT };
	public static final Token[] HEADINGS= new Token[] { Token.HEADING_1, Token.HEADING_2, Token.HEADING_3, Token.HEADING_4, Token.HEADING_5, Token.HEADING_6};
	public static final Token[] NUMBERED_ITEMS= new Token[] { Token.NUMBERED_1, Token.NUMBERED_2, Token.NUMBERED_3, Token.NUMBERED_4, Token.NUMBERED_5 };
	public static final Token[] BULLET_ITEMS= new Token[] { Token.BULLET_1, Token.BULLET_2, Token.BULLET_3, Token.BULLET_4, Token.BULLET_5 };

	public static String[] asStringArray(Token... tokens) {
		String[] strings= new String[tokens.length];
		for (int i= 0; i < tokens.length; i++) {
			strings[i]= tokens[i].wiki;
		}

		return strings;
	}

	public static String getDisplayString(Token token) {
		String name= token.name().replace('_', ' ');
		name= name.substring(0, 1) + name.substring(1).toLowerCase();
		return name;
	}
}
