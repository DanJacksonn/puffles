package entities.impl;

import resources.BlockType;
import resources.LevelData;
import resources.TilePosition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level {

	private int width;
	private int height;
	private Block[][] blocks;
	private TilePosition spawnPoint;

	public Level(int levelNumber) {
		LevelData levelData = new LevelData(levelNumber);
		this.width = levelData.getWidth();
		this.height = levelData.getHeight();
		this.blocks = new Block[width][height];
		convertLevelDataToBlocks(levelData);
	}

	private void convertLevelDataToBlocks(LevelData levelData) {
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
			return new Block(tilePosition, BlockType.STONE);
		case '%':
			return new Block(tilePosition, BlockType.GRASS);
		case 'S':
			this.spawnPoint = tilePosition;
			return null;
		case 'G':
			return new Block(tilePosition, BlockType.GOAL);
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

	public TilePosition getSpawnPoint() {
		return spawnPoint;
	}
}
