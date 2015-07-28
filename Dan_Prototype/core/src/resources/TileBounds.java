package resources;

public class TileBounds {

	public int left;
	public int right;
	public int bottom;
	public int top;
	
	public TileBounds() {
		
	}
	
	public TileBounds(int leftEdge, int rightEdge, int lowestEdge, int highestEdge) {
		this.left = leftEdge;
		this.right = rightEdge;
		this.bottom = lowestEdge;
		this.top = highestEdge;
	}
	
	public TileBounds(int leftEdge, int lowestEdge, int size) {
		this.left = leftEdge;
		this.right = leftEdge + size;
		this.bottom = lowestEdge;
		this.top = lowestEdge + size;
	}
	
	public int getWidth() {
		return right - left;
	}
	
	public int getHeight() {
		return top - bottom;
	}
	
	/** Returns whether given coordinates lie within bounds **/
	public boolean isWithinBounds(int x, int y) {
		return x > left && x < right && y > bottom && y < top;
	}
}

