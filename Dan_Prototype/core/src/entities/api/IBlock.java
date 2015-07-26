package entities.api;

import com.badlogic.gdx.math.Rectangle;

/**
 * Block Interface
 */
public interface IBlock {

	/** Point at which a block will be destroyed. */
	static final int BREAKING_POINT = 3;
	/** Starting damage value. */
	static final int INITIAL_DAMAGE = 0;
	
	/** Block type. */
	enum Type {
		STONE, GRASS, AIR, SPAWN_POINT, GOAL
	}

	/**
	 * @return True if block can be broken.
	 */
	boolean isBreakable();

	/**
	 * Increases damage value of the block.
	 */
	void damage();

	/** 
	 * @return True if damage value of block has reached or 
	 * exceeded it's breaking point.
	 */
	boolean isBroken();
}