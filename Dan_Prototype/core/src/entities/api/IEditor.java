package entities.api;

import resources.TilePosition;
import entities.impl.Block;

/**
 * Editor Interface.
 */
public interface IEditor {

	/**
	 * Places block into editor.
	 * 
	 * @param block Block to be placed.
	 */
	void placeBlock(Block block);

	/**
	 * Removes block at given tile position if one exists in the editor at that position.
	 * 
	 * @param tilePosition Tile position within the level.
	 */
	void unplaceBlock(TilePosition tilePosition);

	/**
	 * Clears all blocks placed in the editor.
	 */
	void clearPlacedBlocks();

}