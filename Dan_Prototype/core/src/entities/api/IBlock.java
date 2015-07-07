package entities.api;

import com.badlogic.gdx.math.Rectangle;

/**
 * Block Interface
 */
public interface IBlock {

	/** Point at which a block will be destroyed. */
	public static final int BREAKING_POINT = 3;
	/** Starting damage value. */
	public static final int INITIAL_DAMAGE = 0;
	
	/** Block type. */
	public enum Type {
		AIR, GRASS, STONE
	}

	/**
	 * @return True if block can be broken.
	 */
	public abstract boolean isBreakable();

	/**
	 * Increases damage value of the block.
	 */
	public abstract void damage();

	/** 
	 * @return True if damage value of block has reached or 
	 * exceeded it's breaking point.
	 */
	public abstract boolean isBroken();

	/**
	 * 
	 * @return Area on level occupied by block.
	 */
	public abstract Rectangle getBounds();

	/**
	 * @return Id of the block.
	 */
	public abstract Type getBlockID();

	/**
	 * @return How damaged the block is.
	 */
	public abstract int getDamageValue();

}