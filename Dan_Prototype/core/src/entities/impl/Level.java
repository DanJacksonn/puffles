package entities.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import entities.api.IBlock;

public class Level {

	/**
	 * This class represents a level. A level is an array of blocks.
	 */

	private int levelID;
	private int width;
	private int height;
	private Block[][] blocks;

	public Level() {
		this.levelID = 3;
		loadLevelText();
	}

	/** Loads a level from a file */
	private void loadLevelText() {
		char tileType;

		// read map from file
		FileHandle file = Gdx.files.internal(
				"levels/lvl" + levelID+ ".map");
		String map = file.readString();

		// store width of level
		this.width = getLevelWidth(map);
		// store width of map (accounts for \r\n on map)
		int mapWidth = width + 2;

		// store height of level (same as map)
		this.height = (map.length() + 2) / mapWidth;

		// store level blocks
		this.blocks = new Block[width + 1][height + 1];

		// for every tile in level
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// get tile type from map
				tileType = map.charAt((y * mapWidth) + x);
				// add new block of type tileType
				blocks[x][(height - y) - 1] = 
						toBlock(tileType, x, (height - y) - 1);
			}
		}
	}

	/** Calculates width of the level */
	private int getLevelWidth(String gmap) {
		char[] map = gmap.toCharArray();
		int count = 0;
		for (char character : map) {
			if (character == '\r') {
				return count;
			}
			count++;
		}
		return count;
	}

	/** Converts map character to block */
	private Block toBlock(char c, int row, int col) {
		TilePosition position = new TilePosition(row, col);
		switch (c) {
		case '#':
			return new Block(position, IBlock.Type.AIR);
		case '%':
			return new Block(position, IBlock.Type.GRASS);
		default:
			return null;
		}
	}

	public void addBlocks(Array<Block> newBlocks) {
		for (Block block : newBlocks) {
			blocks[(int) block.getTilePosition().x][(int) block.getTilePosition().y] = block;
		}
	}

	public boolean breakBlock(Vector2 position) {
		IBlock block = blocks[(int) position.x][(int) position.y];
		block.damage();

		if (block.isBroken()) {
			blocks[(int) position.x][(int) position.y] = null;
			return true;
		} else {
			return false;
		}
	}

	// Getters -----------
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Block[][] getBlocks() {
		return blocks;
	}

	public Block getBlock(int row, int col) {
		return blocks[row][col];
	}

	public boolean isEmpty(Vector2 position) {
		return blocks[(int) position.x][(int) position.y] == null;
	}

	// --------------------

	// Setters ------------
	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	// --------------------
}
