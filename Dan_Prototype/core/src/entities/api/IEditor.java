package entities.api;

import entities.impl.Block;
import entities.impl.TilePosition;

/**
 * Editor Interface.
 */
public interface IEditor {

	/**
	 * Places block into editor.
	 * 
	 * @param block Block to be placed.
	 */
	public abstract void placeBlock(Block block);

	/**
	 * Removes block at given tile position if one exists in the editor at that position.
	 * 
	 * @param tilePosition Tile position within the level.
	 */
	public abstract void unplaceBlock(TilePosition tilePosition);

	/**
	 * Clears all blocks placed in the editor.
	 */
	public abstract void clearPlacedBlocks();

}