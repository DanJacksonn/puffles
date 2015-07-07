package entities.api;

/**
 * Inventory Interface.
 */
public interface IInventory {

	/** Max number of blocks allowed in inventory. */
	public static final float MAX_BLOCKS = 10;

	/** 
	 * Adds a block to the inventory.
	 * */
	public abstract void addBlock();

	/**
	 * Removes a block from the inventory.
	 */
	public abstract void removeBlock();

}