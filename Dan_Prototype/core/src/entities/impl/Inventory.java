package entities.impl;

/**
 * Inventory Implementation
 */
public class Inventory {
	
	/** Max number of blocks allowed in inventory. */
	static final float MAX_BLOCKS = 10;
	
	/** Number of blocks initially in inventory */
	static final int INITIAL_BLOCK_COUNT = 0;
	
	private int noOfBlocks;
	
	public Inventory() {
		this.noOfBlocks = INITIAL_BLOCK_COUNT;
	}
	
	/** 
	 * Adds a block to the inventory.
	 * */
	public void addBlock() {
		noOfBlocks += 1;
	}
	
	/**
	 * Removes a block from the inventory.
	 */
	public void removeBlock() {
		noOfBlocks -= 1;
	}

	public boolean isEmpty() {
		return noOfBlocks == 0;
	}
	
	public int getNoOfBlocks(){
		return noOfBlocks;
	}
}

