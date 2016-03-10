package seaofdecay;

import java.awt.Color;
import seaofdecay.util.asciipanel.AsciiPanel;

/** The different tiles the game is made up of. Each tile has a
 * glyph/character, a foreground color, and a background color. */
public enum Tile {
	/** The Sea of Decay ground tile */
	SOD_GROUND((char)34, new Color(152, 193, 193), new Color(34, 72, 72)), //34
	/** The Sea of Decay wall tile */
	SOD_WALL((char)6, new Color(34, 102, 102), new Color(152, 193, 193)), // 158 is good as well.

	/** The Valley wall tile */
	VALLEY_WALL('#', new Color(255, 153, 51), new Color(255, 217, 102)),
	/** The Valley grass tile */
	VALLEY_GRASS('"', new Color(0, 217, 108), new Color(0, 140, 105)),
	/** The Valley portal tile, leads to the Sea of Decay */
	VALLEY_PORTAL((char)234, new Color(82, 123, 187), new Color(187, 82, 123)),
	/** The Valley floorboard tile, for inside the houses */
	VALLEY_FLOORBOARD((char)240, new Color(102, 82, 51), new Color(128, 102, 64)),

	/** The tile for outside the maps/unknown tiles. */
	BOUNDS('?', AsciiPanel.brightBlack, AsciiPanel.black);

	public boolean isDiggable() {
		return this == SOD_WALL;
	}

	public boolean isGround() {
		return this != SOD_WALL && this != VALLEY_WALL && this != BOUNDS;
	}

	private final char glyph;
	public char glyph() { return glyph; }

	private final Color fgColor;
	public Color fgColor() { return fgColor; }

	private final Color bgColor;
	public Color bgColor() { return bgColor; }

	Tile(char glyph, Color fgColor, Color bgColor) {
		this.glyph = glyph;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}
