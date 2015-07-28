package entities.impl;

import com.badlogic.gdx.math.Rectangle;

import resources.BlockType;
import resources.TilePosition;

/**
 * Represents a single block within the world tiles.
 */
public class Block extends Tile {

	/** Point at which a block will be destroyed. */
	static final int BREAKING_POINT = 3;
	/** Starting damage value. */
	static final int INITIAL_DAMAGE = 0;
	
	private BlockType blockType;
	private boolean breakable;
	private int damageValue;

	public Block(TilePosition position, BlockType blockType) {
		this.position = position;
		this.blockType = blockType;
		this.breakable = blockType.equals(BlockType.GRASS);
		this.damageValue = INITIAL_DAMAGE;
	}

	public Block() {
	}

	/**
	 * @return True if block can be broken.
	 */
	public boolean isBreakable() {
		return breakable;
	}
	
	/**
	 * Increases damage value of the block.
	 */
	public void damage() {
		damageValue++;
	}


	/** 
	 * @return True if damage value of block has reached or 
	 * exceeded it's breaking point.
	 */
	public boolean isBroken() {
		return damageValue >= BREAKING_POINT;
	}

	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, SIZE, SIZE);
	}

	public BlockType getBlockType() {
		return blockType;
	}

	public int getDamageValue() {
		return damageValue;
	}
}
