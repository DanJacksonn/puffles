package entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {
	
	/**
	 * This class represents a block within the game.
	 * -A block has a position within the world and a bounding box.
	 * -A block can be breakable or unbreakable.
	 */

	public static final float SIZE = 1f; // 1 unit
	public static final int BREAKING_POINT = 3;
	
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	int blockID;
	int damageValue;
	boolean breakable;
	
	public Block(Vector2 position, int blockID, boolean breakable) {
		this.position = position;
		this.bounds.setX(position.x);
		this.bounds.setY(position.y);
		this.bounds.width = SIZE;
		this.bounds.height = SIZE;
		this.blockID = blockID;
		this.damageValue = 0;
		this.breakable = breakable;
	}
	
	public void damage() {
		damageValue++;
	}
	
	public boolean isBroken() {
		return damageValue == BREAKING_POINT;
	}
	
	// Getters ---------------
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getBlockID() {
		return blockID;
	}
	
	public int getDamageValue() {
		return damageValue;
	}
	
	public boolean isBreakable() {
		return breakable;
	}
	// -----------------------
}
