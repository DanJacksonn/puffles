package entities.impl;

import resources.TilePosition;

import com.badlogic.gdx.utils.Array;

/**
 * Represents the current state of the edit screen.
 */
public class Editor {

	Array<Block> placedBlocks;
	
	public Editor() {
		placedBlocks = new Array<Block>();
	}
	
	/**
	 * Places block into editor.
	 * 
	 * @param block Block to be placed.
	 */
	public void placeBlock(Block block) {
		placedBlocks.add(block);
	}
	
	/**
	 * Removes block at given tile position if one exists in the editor at that position.
	 * 
	 * @param tilePosition Tile position within the level.
	 */
	public void unplaceBlock(TilePosition tilePosition) {
		for (Block placedBlock : placedBlocks) {
			if (placedBlock.getTilePosition().equals(tilePosition)) {
				placedBlocks.removeValue(placedBlock, true);
				break;
			}
		}
	}
	
	/**
	 * Clears all blocks placed in the editor.
	 */
	public void clearPlacedBlocks() {
		placedBlocks.clear();
	}
	
	public Array<Block> getPlacedBlocks() {
		return placedBlocks;
	}
	
	public boolean isBlockPlacedAt(TilePosition tilePosition) {
		for (Block placedBlock : placedBlocks) {
			if (placedBlock.getTilePosition().equals(tilePosition)) {
				return true;
			}
		}
		return false;
	}

}
