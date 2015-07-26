package entities.api;

/**
 * Inventory Interface.
 */
public interface IInventory {

	/** Max number of blocks allowed in inventory. */
	static final float MAX_BLOCKS = 10;
	
	/** Number of blocks initially in inventory */
	static final int INITIAL_BLOCK_COUNT = 10;

	/** 
	 * Adds a block to the inventory.
	 * */
	void addBlock();

	/**
	 * Removes a block from the inventory.
	 */
	void removeBlock();
	
}