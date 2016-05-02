package seaofdecay;

import java.util.Objects;

/**
 * Created by dan on 29-Apr-16.
 */
public class Inventory {

	private Item[] items;
	public Item[] getItems() { return items; }
	/** Not used now, but might be useful for future. */
	public Item get(int i) { return items[i]; }

	public Inventory(int max) {
		items = new Item[max];
	}

	public void add(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}

	public void remove(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (Objects.equals(items[i], item)) {
				items[i] = null;
				return;
			}
		}
	}

	public boolean isFull() {
		int size = 0;
		for (Item item : items) {
			if (item != null)
				size++;
		}
		return size == items.length;
	}
}
