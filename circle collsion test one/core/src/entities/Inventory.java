package entities;

public class Inventory {
	
	public static final float MAX_BLOCKS = 10;
	int noOfBlocks
	;
	
	public Inventory() {
		this.noOfBlocks = 10;
	}
	
	public int getNoOfBlocks() {
		return noOfBlocks;
	}
	
	public void addBlock() {
		noOfBlocks += 1;
	}
	
	public void removeBlock() {
		noOfBlocks -= 1;
	}
}
