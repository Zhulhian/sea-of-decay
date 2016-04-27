package seaofdecay;

/**
 * Represents a point (coordinate) in the world.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**  We want two points in the world with the same location to be treated as equal.
	 *   We tell Java that by overriding the hashCode and equals methods. */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public List<Point> neighbors8() {
		List<Point> points = new ArrayList<>();

		for (int nx = -1; nx < 2; nx++) {
			for (int ny = -1; ny < 2; ny++) {
				if (nx == 0 && ny == 0)
					continue;

				points.add(new Point(x + nx, y + ny));
			}
		}

		/** We shuffle the neighbour list so we don't introduce bias. Having it always
		 * start from upper left might cause odd things and having it checking in random order
		 * makes things more interesting. */
		Collections.shuffle(points);
		return points;
	}
}
