package entities.impl;

import resources.TilePosition;

/**
 * Abstract representation of a tile within the level.
 */
public abstract class Tile {

	/** Size of a tile. */
	public static final float SIZE = 1f;
	
	/** Position of tile on level */
	public TilePosition position;
	
	public TilePosition getTilePosition() {
		return position;
	}
}
