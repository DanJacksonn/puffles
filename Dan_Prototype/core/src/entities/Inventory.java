package entities;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Inventory {
	
	/**
	 * This class stores items in the player's inventory.
	 */
	private Rectangle bounds = new Rectangle();
	public static final float MAX_BLOCKS = 10;
	
	static int noOfBlocks;
	
	public Inventory() {
		this.noOfBlocks = 0;
		
	}
	
	public void addBlock() {
		noOfBlocks += 1;
	}
	
	public void removeBlock() {
		noOfBlocks -= 1;
	}
	
	// Getters ------------
	public static boolean isEmpty() {
		return noOfBlocks == 0;
	}
	public static int getInventory(){
		return noOfBlocks;
	}
	public Rectangle getBounds() {
		return bounds;
	}
	public void setPosition(Vector2 newPosition) {
		this.bounds.setX(newPosition.x);
		this.bounds.setY(newPosition.y);
	}
	public void setSize(float Size){
		this.bounds.setWidth(Size);
		this.bounds.setHeight(Size);
	}
	

	
	// --------------------

}

