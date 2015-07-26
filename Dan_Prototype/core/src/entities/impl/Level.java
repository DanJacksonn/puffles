package entities.impl;

import resources.LevelData;
import resources.TilePosition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import entities.api.IBlock;

public class Level {

	private int width;
	private int height;
	private Block[][] blocks;

	public Level(LevelData levelData) {
		this.width = levelData.getWidth();
		this.height = levelData.getHeight();
		convertLevelDataToBlocks(levelData);
	}

	private void convertLevelDataToBlocks(LevelData levelData) {
		blocks = new Block[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				blocks[x][y] = createBlock(levelData.dataAt(x, y), x, y);
			}
		}
	}

	/** Maps level data to block types */
	private Block createBlock(char dataValue, int x, int y) {
		TilePosition tilePosition = new TilePosition(x, y);
		switch (dataValue) {
		case '#':
			return new Block(tilePosition, IBlock.Type.STONE);
		case '%':
			return new Block(tilePosition, IBlock.Type.GRASS);
		case 'S':
			return new Block(tilePosition, IBlock.Type.SPAWN_POINT);
		case 'G':
			return new Block(tilePosition, IBlock.Type.GOAL);
		default:
			return null;
		}
	}

	public void addBlocks(Array<Block> newBlocks) {
		for (Block block : newBlocks) {
			blocks[(int) Math.floor(block.getTilePosition().x)]
				  [(int) Math.floor(block.getTilePosition().y)] 
				   = block;
		}
	}

	public void damageBlock(Vector2 position) {
		Block block = blocks[(int) Math.floor(position.x)]
							[(int) Math.floor(position.y)];
		block.damage();
		if (block.isBroken()) {
			blocks[(int) position.x][(int) position.y] = null;
		}
	}

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

	public boolean isPositionEmpty(Vector2 position) {
		return blocks[(int) position.x][(int) position.y] == null;
	}
	
	public boolean isBlockBroken(Vector2 position) {
		return blocks[(int) position.x][(int) position.y] == null;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}
}
