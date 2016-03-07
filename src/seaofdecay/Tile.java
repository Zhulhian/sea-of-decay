package seaofdecay;

import java.awt.Color;
import seaofdecay.util.asciipanel.AsciiPanel;


public enum Tile {
	FUNGI_FLOOR((char)34, new Color(152, 193, 193), new Color(34, 72, 72)), //34
	FUNGI_WALL((char)6, new Color(34, 102, 102), new Color(152, 193, 193)), // 158 is good as well.
	VALLEY_WALL('#', new Color(255, 153, 51), new Color(255, 217, 102)),
	VALLEY_GRASS((char)247, new Color(0, 217, 108), new Color(0, 140, 70)),
	VALLEY_PORTAL((char)234, AsciiPanel.brightBlue, AsciiPanel.brightCyan),
	BOUNDS('?', AsciiPanel.brightBlack, AsciiPanel.black);

	public boolean isDiggable() {
		return this == FUNGI_WALL || this == VALLEY_WALL;
	}

	public boolean isGround() {
		return this != FUNGI_WALL && this != VALLEY_WALL && this != BOUNDS;
	}

	private char glyph;
	public char glyph() { return glyph; }

	private Color fgColor;
	public Color fgColor() { return fgColor; }

	private Color bgColor;
	public Color bgColor() { return bgColor; }

	Tile(char glyph, Color fgColor, Color bgColor) {
		this.glyph = glyph;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}
