package entities;

public class Inventory {
	
	/**
	 * This class stores items in the player's inventory.
	 */
	
	public static final float MAX_BLOCKS = 10;
	int noOfBlocks;
	
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
	public boolean isEmpty() {
		return noOfBlocks == 0;
	}
	
	// --------------------

}
