package entities.impl;

import entities.api.IInventory;

/**
 * Inventory Implementation
 */
public class Inventory implements IInventory {
	
	private int noOfBlocks;
	
	public Inventory() {
		this.noOfBlocks = INITIAL_BLOCK_COUNT;
	}
	
	@Override
	public void addBlock() {
		noOfBlocks += 1;
	}
	
	@Override
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

