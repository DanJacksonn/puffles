package entities;

import com.badlogic.gdx.math.Vector2;

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
		height = 8;
		blocks = new Block[width][height];

		// fill level with empty blocks
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				blocks[col][row] = null;
			}
		}

		// add blocks
		for (int col = 0; col < width; col++) {
			if (col != 5 && col != 6) {
				blocks[col][0] = new Block(new Vector2(col, 0), false);
				blocks[col][1] = new Block(new Vector2(col, 1), false);
				if (col > 2 && col != 9) {
					blocks[col][2] = new Block(new Vector2(col, 2), false);
				}
				if (col > 9) {
				blocks[col][3] = new Block(new Vector2(col, 3), false);
				}
			}
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

	public Block getBlock(int x, int y) {
		return blocks[x][y];
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
