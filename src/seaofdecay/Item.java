package seaofdecay;

import java.awt.*;

/**
 * An item that can be picked up and used.
 */
public class Item {

	private char glyph;
	public char getGlyph() { return glyph; }

	private Color color;
	public Color getColor() { return color; }

	private String name;
	public String getName() { return name; }

	public Item(char glyph, Color color, String name) {
		this.glyph = glyph;
		this.color = color;
		this.name = name;
	}
}
