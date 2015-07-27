package entities.impl;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import resources.Bounds;
import resources.LevelData;
import resources.TilePosition;

/**
 * The current state of the game entities.
 */
public class World {

	public Puffle puffle;
	public Level level;
	public Inventory inventory;
	
	public World() {
		this.level = new Level(3);
		TilePosition puffleSpawn = level.getSpawnPoint();
		this.puffle = new Puffle(puffleSpawn);
		this.inventory = new Inventory();
	}
	

	/** 
	 * Returns the list of blocks that are within the cameras window
	 * 
	 * @param cameraWidth
	 * @param cameraHeight
	 * @return
	 */
	public List<Block> getDrawableBlocks(int cameraWidth, int cameraHeight) {
		Bounds windowBounds = findVisibleBlock(cameraWidth,
				cameraHeight);
		return getBlocksWithinBounds(windowBounds);
	}

	/**
	 * Calculates which level blocks are within the cameras window. The cameras
	 * view cannot see outside of the level and locks into place if the
	 * puffle approaches the edges of the level.
	 * 
	 * @param cameraWidth
	 *            The minimum number of blocks which can fit horizontally into
	 *            the camera window.
	 * @param cameraHeight
	 *            The minimum number of blocks which can fit vertically into the
	 *            camera window.
	 * @return
	 */
	private Bounds findVisibleBlock(int cameraWidth, int cameraHeight) {
		Bounds levelBounds = new Bounds(0, level.getWidth() - 1, 0, level.getHeight() - 1);
		Vector2 pufflePosition = puffle.getPosition();
		float horizontalViewDistance = calaculateViewDistanceOfPuffle(cameraWidth);
		float verticalViewDistance = calaculateViewDistanceOfPuffle(cameraHeight);
		float leftMostVisibleBlock = floor(pufflePosition.x - horizontalViewDistance);
		float rightMostVisibleBlock = ceil(pufflePosition.x + horizontalViewDistance);
		float lowestVisibleBlock = floor(pufflePosition.y - verticalViewDistance);
		float highestVisibleBlock = ceil(pufflePosition.y + verticalViewDistance);
		if (leftMostVisibleBlock < levelBounds.left) {
			leftMostVisibleBlock = levelBounds.left;
			rightMostVisibleBlock = levelBounds.left + (2 * horizontalViewDistance);
		}
		if (rightMostVisibleBlock > levelBounds.right) {
			rightMostVisibleBlock = levelBounds.right;
			leftMostVisibleBlock = levelBounds.right - (2 * horizontalViewDistance);
		}
		if (lowestVisibleBlock < levelBounds.bottom) {
			lowestVisibleBlock = levelBounds.bottom;
			highestVisibleBlock = levelBounds.bottom + (2 * verticalViewDistance);
		}
		if (highestVisibleBlock > levelBounds.top) {
			highestVisibleBlock = levelBounds.top;
			lowestVisibleBlock = levelBounds.top - (2 * verticalViewDistance);
		}
		return new Bounds(leftMostVisibleBlock, rightMostVisibleBlock, lowestVisibleBlock, highestVisibleBlock);
	}

	private int calaculateViewDistanceOfPuffle(int cameraWidth) {
		return ceil(cameraWidth / 2) + 1;
	}
	
	private int floor(float x) {
		return (int) Math.floor(x);
	}

	private int ceil(float x) {
		return (int) Math.ceil(x);
	}
	
	private List<Block> getBlocksWithinBounds(Bounds bounds) {
		List<Block> blocks = new ArrayList<Block>();
		Block block = new Block();
		int leftMostBlock = (int) Math.floor(bounds.left);
		int bottomBlock = (int) Math.floor(bounds.bottom);
		for (int row = leftMostBlock; row <= bounds.right; row++) {
			for (int col = bottomBlock; col <= bounds.top; col++) {
				block = level.getBlocks()[row][col];
				if (block != null) {
					blocks.add(block);
				}
			}
		}
		return blocks;
	}
	
	public void updateLevel(Level level) {
		this.level = level;
	}

	public void addBlockToInventory() {
		inventory.addBlock();
	}


	public void removeBlockFromInventory() {
		inventory.removeBlock();
		
	}


	public void addBlocksToLevel(Array<Block> placedBlocks) {
		level.addBlocks(placedBlocks);
	}
	
}
