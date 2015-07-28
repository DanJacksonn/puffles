package resources;

public class Bounds {
	
	public float left;
	public float right;
	public float bottom;
	public float top;
	
	public Bounds(float leftEdge, float rightEdge, float lowestEdge, float highestEdge) {
		this.left = leftEdge;
		this.right = rightEdge;
		this.bottom = lowestEdge;
		this.top = highestEdge;
	}
	
	public Bounds(float leftEdge, float lowestEdge, float size) {
		this.left = leftEdge;
		this.right = leftEdge + size;
		this.bottom = lowestEdge;
		this.top = lowestEdge + size;
	}
	
	public float getWidth() {
		return right - left;
	}
	
	public float getHeight() {
		return top - bottom;
	}
	
	/** Returns whether given coordinates lie within bounds **/
	public boolean isWithinBounds(float x, float y) {
		return x > left && x < right && y > bottom && y < top;
	}
}
