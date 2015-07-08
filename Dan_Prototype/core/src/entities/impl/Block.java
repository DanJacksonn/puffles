package entities.impl;

import com.badlogic.gdx.math.Rectangle;

import entities.api.IBlock;

/**
 * Represents a single block within the world tiles.
 */
public class Block extends Tile implements IBlock {

	private Type blockType;
	private boolean breakable;
	private int damageValue;

	public Block(TilePosition position, Type blockType) {
		this.position = position;
		this.blockType = blockType;
		this.breakable = blockType.equals(Type.GRASS);
		this.damageValue = INITIAL_DAMAGE;
	}

	@Override
	public boolean isBreakable() {
		return breakable;
	}
	
	@Override
	public void damage() {
		damageValue++;
	}

	@Override
	public boolean isBroken() {
		return damageValue >= BREAKING_POINT;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, SIZE, SIZE);
	}

	@Override
	public Type getBlockID() {
		return blockType;
	}

	@Override
	public int getDamageValue() {
		return damageValue;
	}
}