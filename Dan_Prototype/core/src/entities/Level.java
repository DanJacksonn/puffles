package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level {

	/**
	 * This class represents a level.
	 * A level is an array of blocks.
	 */
	
	private int width;
	private int height;
	private Block[][] blocks;

	public Level() {
		loadDemoLevel();
	}

	public void loadDemoLevel() {
		width = 15;
		height = 10;
		blocks = new Block[width][height];

		// fill level with empty blocks
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				blocks[row][col] = null;
			}
		}

		// add blocks
		for (int row = 0; row < width; row++) {
			if (row != 5 && row != 6) {
				blocks[row][0] = new Block(new Vector2(row, 0), 0, false);
				blocks[row][1] = new Block(new Vector2(row, 1), 0, false);
				if (row > 2 && row != 9) {
					blocks[row][2] = new Block(new Vector2(row, 2), 0, false);
				}
				if (row > 9) {
				blocks[row][3] = new Block(new Vector2(row, 3), 0, false);
				}
			}
		}
	}
	
	public void addBlocks(Array<Block> newBlocks) {
		for (Block block : newBlocks) {
			blocks[(int) block.getPosition().x][(int) block.getPosition().y] = block;
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
