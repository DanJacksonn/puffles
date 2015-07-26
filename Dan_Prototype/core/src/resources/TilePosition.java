package resources;

import com.badlogic.gdx.math.Vector2;

public class TilePosition extends Vector2 {

	public TilePosition() {
	}
	
	public TilePosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public TilePosition(TilePosition position) {
		this.x = position.x;
		this.y = position.y;
	}
}
