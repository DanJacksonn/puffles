package entities;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Inventory {
	
	/**
	 * This class stores items in the player's inventory.
	 */
	public static final float MAX_BLOCKS = 10;
	public static final float SIZE = 0.6f;
	public static final float X_POSITION = 0.6f;
	public static final float Y_POSITION = 0.1f;
	public static final float TXT_X_OFFSET = 0.08f;
	public static final float TXT_Y_OFFSET = 0.08f;
	
	private Rectangle bounds;
	private Vector2 textOffset;
	private int noOfBlocks;
	
	public Inventory() {
		this.bounds = new Rectangle(X_POSITION, Y_POSITION, SIZE, SIZE);
		this.textOffset = new Vector2(TXT_X_OFFSET, TXT_Y_OFFSET);
		this.noOfBlocks = 0;
	}
	
	public void addBlock() {
		noOfBlocks += 1;
	}
	
	public void removeBlock() {
		noOfBlocks -= 1;
	}
	
	// Getters ------------
	public boolean isEmpty() {
		return noOfBlocks == 0;
	}
	
	public int getNoOfBlocks(){
		return noOfBlocks;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getTextOffset() {
		return textOffset;
	}
	// --------------------
	public void setPosition(Vector2 newPosition) {
		this.bounds.setX(newPosition.x);
		this.bounds.setY(newPosition.y);
	}
	public void setSize(float Size){
		this.bounds.setWidth(Size);
		this.bounds.setHeight(Size);
	}
	

}

