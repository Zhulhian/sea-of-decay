package seaofdecay;

import java.awt.Color;
import seaofdecay.util.asciipanel.AsciiPanel;

/** The different tiles the game is made up of. Each tile has a
 * glyph/character, a foreground color, and a background color. */
public enum Tile {
	/**  -  -  SEA OF DECAY TILES  -  -  **/
	/** The Sea of Decay ground tile */
	SOD_GROUND((char)34, WorldType.SEA_OF_DECAY, true, true, new Color(152, 193, 193), new Color(34, 72, 72)), //34
	/** The Sea of Decay wall tile */
	SOD_WALL((char)6, WorldType.SEA_OF_DECAY, false, false, new Color(34, 102, 102), new Color(152, 193, 193)), // 158 is good as well.
	/** Exit Portal SOD */
	SOD_PORTAL((char)234, WorldType.SEA_OF_DECAY, true, true, new Color(50, 60, 230), new Color(100, 80, 250)),


	/**  -  -  VALLEY TILES  -  -  **/
	/** The Valley - wall tile */
	VALLEY_WALL('#', WorldType.VALLEY, false, false, new Color(255, 153, 51), new Color(255, 217, 102)),
	/** The Valley - gate tile */
	VALLEY_GATE((char)216, WorldType.VALLEY, true, false, new Color(77, 61, 38), new Color(128, 102, 64)),
	/** The Valley - grass tile */
	VALLEY_GRASS('"', WorldType.VALLEY, true, true, new Color(0, 217, 108), new Color(0, 140, 105)),
	/** The Valley - shaded grass tile */
	VALLEY_SHADEGRASS(';', WorldType.VALLEY, true, true, new Color(0, 140, 70), new Color(0, 102, 77)),
	/** The Valley - portal tile, leads to the Sea of Decay */
	VALLEY_PORTAL((char)234, WorldType.VALLEY,true, true, new Color(217, 0, 163), new Color(102, 0, 77)),
	/** The Valley - portal frame tile. */
	VALLEY_PORTAL_FRAME((char)227, WorldType.VALLEY, true, false, new Color(140, 0, 140), new Color(255, 102, 178)),
	/** The Valley - floorboard tile, for inside the houses */
	VALLEY_FLOORBOARD((char)240, WorldType.VALLEY, true, true, new Color(102, 82, 51), new Color(128, 102, 64)),
	/** The Valley - closed door tile. */
	VALLEY_DOOR_CLOSED((char)255, WorldType.VALLEY, false, false, new Color(108, 82, 51), new Color(77, 61, 38)),
	/** The Valley - open door tile. */
	VALLEY_DOOR_OPEN((char)254, WorldType.VALLEY, true, true, new Color(108, 82, 51), new Color(77, 61, 38)),
	/** The Valley - wheat tile. */
	VALLEY_WHEAT((char)231, WorldType.VALLEY, true, true, new Color(255, 191, 0), new Color(64, 48, 0)),
	/** The Valley - path tile. */
	VALLEY_PATH((char)176, WorldType.VALLEY, true, true, new Color(217, 108, 0), new Color(255, 204, 51)),
	/** The Valley - dirt tile. */
	VALLEY_DIRT((char)199, WorldType.VALLEY, true, true, new Color(102, 77, 0), new Color(64, 48, 0)),
	/** The Valley - top of table tile. */
	VALLEY_TABLE_TOP((char)219, WorldType.VALLEY, true, false, new Color(128, 102, 64), new Color(102, 82, 51)),
	/** The Valley - front of table tile. */
	VALLEY_TABLE_FRONT((char)220, WorldType.VALLEY, true, false, new Color(102, 82, 51), new Color(128, 102, 64)),
	/** The Valley - window tile. */
	VALLEY_WINDOW((char)8, WorldType.VALLEY, true, false, new Color(255, 217, 102), new Color(102, 178, 255)),

	/** A tile not yet seen. */
	UNKNOWN(' ', WorldType.ABYSS, true, true, new Color (255, 255, 255), new Color (255, 255, 255)),

	/** The tile for outside the maps/unknown tiles. */
	BOUNDS('?', WorldType.ABYSS, false, false, AsciiPanel.brightBlack, AsciiPanel.black);

	public boolean isDiggable() {
		return this == SOD_WALL;
	}

	public boolean isInteractable() {
		return this == VALLEY_DOOR_CLOSED || this == VALLEY_DOOR_OPEN;
	}

	private final boolean isGround;

	public boolean isGround() {
		return isGround;
	}

	/** If true, player can see through the tile. */
	public final boolean transparent;

	public final boolean isSeeThrough() {
		return transparent;
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

	Tile(char glyph, WorldType worldType, boolean transparent, boolean isGround, Color fgColor, Color bgColor) {
		this.glyph = glyph;
		this.worldType = worldType;
		this.transparent = transparent;
		this.isGround = isGround;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}
