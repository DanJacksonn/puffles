package model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Puffle {

	/**
	 * This class represents the player's character.
	 * -A puffle has a position within the world and a bounding box.
	 * -A puffle has horizontal and vertical movement.
	 * -Whilst moving horizontally the puffle rolls.
	 */
	
	public enum State {
		STOPPED, ROLLING, DYING
	}
	
	public static final float ROLL_VELOCITY = 2f;
	public static final float JUMP_VELOCITY = 1f;
	public static final float SIZE = 0.7f;
	
	Vector2 position = new Vector2();
	Rectangle bounds = new Rectangle();
	
	// movement vectors
	Vector2 acceleration = new Vector2();
	Vector2 velocity = new Vector2();
	
	float rotation;
	
	// states
	State state;
	boolean jumping;
	
	public Puffle(Vector2 position) {
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.height = SIZE;
		this.bounds.width = SIZE;
		this.rotation = 90;
		this.state = State.STOPPED;
		this.jumping = false;
	}

	// Getters ----------------
	public Vector2 getPosition() {
		return position;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public boolean isJumping() {
		return jumping;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public State getState() {
		return state;
	}
	// -------------------------
	
	// Setters -----------------
	public void setPosition(Vector2 position) {
		this.position = position;
		this.bounds.setX(position.x);
		this.bounds.setY(position.y);
	}
	
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
		
	public void setState(State newState) {
		this.state = newState;
	}
	// -------------------------
	
	/** Move puffle **/
	public void update(float delta) {
		// add distance travelled in delta seconds to current position
		position.add(velocity.cpy().scl(delta));
		bounds.x = position.x;
		bounds.y = position.y;
		roll();
	}
	
	/** Rotate puffle **/
	private void roll() {
		// rotate by velocity
		rotation -= velocity.x;
		
		// loop 360 degrees
		if (rotation > 360) {
			rotation = 0 + (rotation - 360);
		} else if (rotation < 0) {
			rotation = 360 + rotation;
		}
	}
}
