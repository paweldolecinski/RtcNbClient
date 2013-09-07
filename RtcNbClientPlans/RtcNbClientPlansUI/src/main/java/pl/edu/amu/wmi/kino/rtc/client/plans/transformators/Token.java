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


public enum Token {
	
	HEADING_1("="),  //$NON-NLS-1$
	HEADING_2("=="),  //$NON-NLS-1$
	HEADING_3("==="),  //$NON-NLS-1$
	HEADING_4("===="),  //$NON-NLS-1$
	HEADING_5("====="), //$NON-NLS-1$
	HEADING_6("======"), //$NON-NLS-1$
	BULLET_1("*"),  //$NON-NLS-1$
	BULLET_2("**"),  //$NON-NLS-1$
	BULLET_3("***"), //$NON-NLS-1$
	BULLET_4("****"), //$NON-NLS-1$
	BULLET_5("*****"), //$NON-NLS-1$
	NUMBERED_1("#"),  //$NON-NLS-1$
	NUMBERED_2("##"),  //$NON-NLS-1$
	NUMBERED_3("###"), //$NON-NLS-1$
	NUMBERED_4("####"), //$NON-NLS-1$
	NUMBERED_5("#####"), //$NON-NLS-1$
	BOLD("**"),  //$NON-NLS-1$
	ITALICS("//"),  //$NON-NLS-1$
	STRIKE_OUT("--"),  //$NON-NLS-1$
	UNDERLINE("__"), //$NON-NLS-1$
	SEPARATOR("|"), //$NON-NLS-1$
	IMG_START("{{"), //$NON-NLS-1$
	IMG_END("}}"), //$NON-NLS-1$
	LINK_START("[["), //$NON-NLS-1$
	LINK_END("]]"), //$NON-NLS-1$
	COLOR("##"), //$NON-NLS-1$
	HORIZONTAL_LINE("----"), //$NON-NLS-1$
	LINE_BREAK("\\\\"), //$NON-NLS-1$
	CODE_START("{{{"), //$NON-NLS-1$
	CODE_END("}}}"), //$NON-NLS-1$
	VAR_START("${"), //$NON-NLS-1$
	VAR_END("}"), //$NON-NLS-1$
	XHTML_START("<<<xhtml"), //$NON-NLS-1$
	XHTML_END(">>>"), //$NON-NLS-1$
	ANGLE_OPEN("<"), //$NON-NLS-1$
	ANGLE_CLOSE(">"), //$NON-NLS-1$
	
	INDIANRED("indianRed"), //$NON-NLS-1$
	LIGHTCORAL("lightCoral"), //$NON-NLS-1$
	SALMON("salmon"), //$NON-NLS-1$
	DARKSALMON("darkSalmon"), //$NON-NLS-1$
	LIGHTSALMON("lightSalmon"), //$NON-NLS-1$
	CRIMSON("crimson"), //$NON-NLS-1$
	RED("red"), //$NON-NLS-1$
	FIREBRICK("fireBrick"), //$NON-NLS-1$
	DARKRED("darkRed"), //$NON-NLS-1$
	PINK("pink"), //$NON-NLS-1$
	LIGHTPINK("lightPink"), //$NON-NLS-1$
	HOTPINK("hotPink"), //$NON-NLS-1$
	DEEPPINK("deepPink"), //$NON-NLS-1$
	MEDIUMVIOLETRED("mediumVioletRed"), //$NON-NLS-1$
	PALEVIOLETRED("paleVioletRed"), //$NON-NLS-1$
//	LIGHTSALMON("lightSalmon"),
	CORAL("coral"), //$NON-NLS-1$
	TOMATO("tomato"), //$NON-NLS-1$
	ORANGERED("orangeRed"), //$NON-NLS-1$
	DARKORANGE("darkOrange"), //$NON-NLS-1$
	ORANGE("orange"), //$NON-NLS-1$
	GOLD("gold"), //$NON-NLS-1$
	YELLOW("yellow"), //$NON-NLS-1$
	LIGHTYELLOW("lightYellow"), //$NON-NLS-1$
	LEMONCHIFFON("lemonChiffon"), //$NON-NLS-1$
	LIGHTGOLDENRODYELLOW("lightGoldenrodYellow"), //$NON-NLS-1$
	PAPAYAWHIP("papayaWhip"), //$NON-NLS-1$
	MOCCASIN("moccasin"), //$NON-NLS-1$
	PEACHPUFF("peachPuff"), //$NON-NLS-1$
	PALEGOLDENROD("paleGoldenrod"), //$NON-NLS-1$
	KHAKI("khaki"), //$NON-NLS-1$
	DARKKHAKI("darkKhaki"), //$NON-NLS-1$
	LAVENDER("lavender"), //$NON-NLS-1$
	THISTLE("thistle"), //$NON-NLS-1$
	PLUM("plum"), //$NON-NLS-1$
	VIOLET("violet"), //$NON-NLS-1$
	ORCHID("orchid"), //$NON-NLS-1$
	FUCHSIA("fuchsia"), //$NON-NLS-1$
	MAGENTA("magenta"), //$NON-NLS-1$
	MEDIUMORCHID("mediumOrchid"), //$NON-NLS-1$
	MEDIUMPURPLE("mediumPurple"), //$NON-NLS-1$
	BLUEVIOLET("blueViolet"), //$NON-NLS-1$
	DARKVIOLET("darkViolet"), //$NON-NLS-1$
	DARKORCHID("darkOrchid"), //$NON-NLS-1$
	DARKMAGENTA("darkMagenta"), //$NON-NLS-1$
	PURPLE("purple"), //$NON-NLS-1$
	INDIGO("indigo"), //$NON-NLS-1$
	SLATEBLUE("slateBlue"), //$NON-NLS-1$
	DARKSLATEBLUE("darkSlateBlue"), //$NON-NLS-1$
	GREENYELLOW("greenYellow"), //$NON-NLS-1$
	CHARTREUSE("chartreuse"), //$NON-NLS-1$
	LAWNGREEN("lawnGreen"), //$NON-NLS-1$
	LIME("lime"), //$NON-NLS-1$
	LIMEGREEN("limeGreen"), //$NON-NLS-1$
	PALEGREEN("paleGreen"), //$NON-NLS-1$
	LIGHTGREEN("lightGreen"), //$NON-NLS-1$
	MEDIUMSPRINGGREEN("mediumSpringGreen"), //$NON-NLS-1$
	SPRINGGREEN("springGreen"), //$NON-NLS-1$
	MEDIUMSEAGREEN("mediumSeaGreen"), //$NON-NLS-1$
	SEAGREEN("seaGreen"), //$NON-NLS-1$
	FORESTGREEN("forestGreen"), //$NON-NLS-1$
	GREEN("green"), //$NON-NLS-1$
	DARKGREEN("darkGreen"), //$NON-NLS-1$
	YELLOWGREEN("yellowGreen"), //$NON-NLS-1$
	OLIVEDRAB("oliveDrab"), //$NON-NLS-1$
	OLIVE("olive"), //$NON-NLS-1$
	DARKOLIVEGREEN("darkOliveGreen"), //$NON-NLS-1$
	MEDIUMAQUAMARINE("mediumAquamarine"), //$NON-NLS-1$
	DARKSEAGREEN("darkSeaGreen"), //$NON-NLS-1$
	LIGHTSEAGREEN("lightSeaGreen"), //$NON-NLS-1$
	DARKCYAN("darkCyan"), //$NON-NLS-1$
	TEAL("teal"), //$NON-NLS-1$
	AQUA("aqua"), //$NON-NLS-1$
	CYAN("cyan"), //$NON-NLS-1$
	LIGHTCYAN("lightCyan"), //$NON-NLS-1$
	PALETURQUOISE("paleTurquoise"), //$NON-NLS-1$
	AQUAMARINE("aquamarine"), //$NON-NLS-1$
	TURQUOISE("turquoise"), //$NON-NLS-1$
	MEDIUMTURQUOISE("mediumTurquoise"), //$NON-NLS-1$
	DARKTURQUOISE("darkTurquoise"), //$NON-NLS-1$
	CADETBLUE("cadetBlue"), //$NON-NLS-1$
	STEELBLUE("steelBlue"), //$NON-NLS-1$
	LIGHTSTEELBLUE("lightSteelBlue"), //$NON-NLS-1$
	POWDERBLUE("powderBlue"), //$NON-NLS-1$
	LIGHTBLUE("lightBlue"), //$NON-NLS-1$
	SKYBLUE("skyBlue"), //$NON-NLS-1$
	LIGHTSKYBLUE("lightSkyBlue"), //$NON-NLS-1$
	DEEPSKYBLUE("deepSkyBlue"), //$NON-NLS-1$
	DODGERBLUE("dodgerBlue"), //$NON-NLS-1$
	CORNFLOWERBLUE("cornflowerBlue"), //$NON-NLS-1$
	MEDIUMSLATEBLUE("mediumSlateBlue"), //$NON-NLS-1$
	ROYALBLUE("royalBlue"), //$NON-NLS-1$
	BLUE("blue"), //$NON-NLS-1$
	MEDIUMBLUE("mediumBlue"), //$NON-NLS-1$
	DARKBLUE("darkBlue"), //$NON-NLS-1$
	NAVY("navy"), //$NON-NLS-1$
	MIDNIGHTBLUE("midnightBlue"), //$NON-NLS-1$
	CORNSILK("cornsilk"), //$NON-NLS-1$
	BLANCHEDALMOND("blanchedAlmond"), //$NON-NLS-1$
	BISQUE("bisque"), //$NON-NLS-1$
	NAVAJOWHITE("navajoWhite"), //$NON-NLS-1$
	WHEAT("wheat"), //$NON-NLS-1$
	BURLYWOOD("burlyWood"), //$NON-NLS-1$
	TAN("tan"), //$NON-NLS-1$
	ROSYBROWN("rosyBrown"), //$NON-NLS-1$
	SANDYBROWN("sandyBrown"), //$NON-NLS-1$
	GOLDENROD("goldenrod"), //$NON-NLS-1$
	DARKGOLDENROD("darkGoldenrod"), //$NON-NLS-1$
	PERU("peru"), //$NON-NLS-1$
	CHOCOLATE("chocolate"), //$NON-NLS-1$
	SADDLEBROWN("saddleBrown"), //$NON-NLS-1$
	SIENNA("sienna"), //$NON-NLS-1$
	BROWN("brown"), //$NON-NLS-1$
	MAROON("maroon"), //$NON-NLS-1$
	WHITE("white"), //$NON-NLS-1$
	SNOW("snow"), //$NON-NLS-1$
	HONEYDEW("honeydew"), //$NON-NLS-1$
	MINTCREAM("mintCream"), //$NON-NLS-1$
	AZURE("azure"), //$NON-NLS-1$
	ALICEBLUE("aliceBlue"), //$NON-NLS-1$
	GHOSTWHITE("ghostWhite"), //$NON-NLS-1$
	WHITESMOKE("whiteSmoke"), //$NON-NLS-1$
	SEASHELL("seashell"), //$NON-NLS-1$
	BEIGE("beige"), //$NON-NLS-1$
	OLDLACE("oldLace"), //$NON-NLS-1$
	FLORALWHITE("floralWhite"), //$NON-NLS-1$
	IVORY("ivory"), //$NON-NLS-1$
	ANTIQUEWHITE("antiqueWhite"), //$NON-NLS-1$
	LINEN("linen"), //$NON-NLS-1$
	LAVENDERBLUSH("lavenderBlush"), //$NON-NLS-1$
	MISTYROSE("mistyRose"), //$NON-NLS-1$
	GAINSBORO("gainsboro"), //$NON-NLS-1$
	LIGHTGREY("lightGrey"), //$NON-NLS-1$
	SILVER("silver"), //$NON-NLS-1$
	DARKGRAY("darkGray"), //$NON-NLS-1$
	GRAY("gray"), //$NON-NLS-1$
	DIMGRAY("dimGray"), //$NON-NLS-1$
	LIGHTSLATEGRAY("lightSlateGray"), //$NON-NLS-1$
	SLATEGRAY("slateGray"), //$NON-NLS-1$
	DARKSLATEGRAY("darkSlateGray"), //$NON-NLS-1$
	BLACK("black"), //$NON-NLS-1$
	
	CHARACTER(null),
	EOF(null),
	WHITESPACE(" "), //$NON-NLS-1$
	TAB("\t"), //$NON-NLS-1$
	LF("\n"), //$NON-NLS-1$
	CR("\r"), //$NON-NLS-1$
	CR_LF("\r\n"); //$NON-NLS-1$
	
	public final String wiki;
	
	private Token(String wiki) {
		this.wiki= wiki;
	}
}
