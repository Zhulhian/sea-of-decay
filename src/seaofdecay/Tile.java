package seaofdecay;

import java.awt.Color;
import seaofdecay.util.asciipanel.AsciiPanel;

/** The different tiles the game is made up of. Each tile has a
 * glyph/character, a foreground color, and a background color. */
public enum Tile {
	/** The Sea of Decay ground tile */
	SOD_GROUND((char)34, WorldType.SEA_OF_DECAY, new Color(152, 193, 193), new Color(34, 72, 72)), //34
	/** The Sea of Decay wall tile */
	SOD_WALL((char)6, WorldType.SEA_OF_DECAY, new Color(34, 102, 102), new Color(152, 193, 193)), // 158 is good as well.

	/** The Valley wall tile */
	VALLEY_WALL('#', WorldType.VALLEY, new Color(255, 153, 51), new Color(255, 217, 102)),
	/** The Valley grass tile */
	VALLEY_GRASS('"', WorldType.VALLEY, new Color(0, 217, 108), new Color(0, 140, 105)),
	/** The Valley portal tile, leads to the Sea of Decay */
	VALLEY_PORTAL((char)234, WorldType.VALLEY, new Color(82, 123, 187), new Color(187, 82, 123)),
	/** The Valley floorboard tile, for inside the houses */
	VALLEY_FLOORBOARD((char)240, WorldType.VALLEY, new Color(102, 82, 51), new Color(128, 102, 64)),
	/** The Valley door tile. */
	VALLEY_DOOR_CLOSED((char)255, WorldType.VALLEY, new Color(108, 82, 51), new Color(77, 61, 38)),
	VALLEY_DOOR_OPEN((char)254, WorldType.VALLEY, new Color(108, 82, 51), new Color(77, 61, 38)),

	/** A tile not yet seen. */
	UNKNOWN(' ', WorldType.ABYSS, AsciiPanel. white, AsciiPanel.white),

	/** The tile for outside the maps/unknown tiles. */
	BOUNDS('?', WorldType.ABYSS, AsciiPanel.brightBlack, AsciiPanel.black);

	public boolean isDiggable() {
		return this == SOD_WALL;
	}

	public boolean isInteractable() {
		return this == VALLEY_DOOR_CLOSED || this == VALLEY_DOOR_OPEN;
	}

	public boolean isGround() {
		return this != SOD_WALL &&
				this != VALLEY_WALL &&
				this != VALLEY_DOOR_CLOSED &&
				this != BOUNDS;
	}

	private final char glyph;
	public char glyph() { return glyph; }

	private final Color fgColor;
	public Color fgColor() { return fgColor; }

	private final WorldType worldType;
	public WorldType worldType() { return worldType; }

	private final Color bgColor;
	public Color bgColor() { return bgColor; }

	public static Tile getTile(char glyph, WorldType worldType) {
		for (Tile tile : Tile.values()) {
			if (tile.glyph() == glyph && tile.worldType() == worldType)
				return tile;
		}
		return Tile.BOUNDS;
	}

	Tile(char glyph, WorldType worldType, Color fgColor, Color bgColor) {
		this.glyph = glyph;
		this.worldType = worldType;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}
