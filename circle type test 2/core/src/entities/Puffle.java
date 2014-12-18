package entities;

import com.badlogic.gdx.math.Circle;
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
	
	public static final float SIZE = 0.7f;
	
	Vector2 position = new Vector2();
	Circle bounds = new Circle();
	
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
		this.bounds.radius = 0.6f;
		this.rotation = 90;
		this.state = State.STOPPED;
		this.jumping = false;
	}

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
	
	public void applyAccleration(float accelerationX, float accerlationY) {
		velocity.add(accelerationX, accerlationY);
	}
	
	public void scaleVelocity(float scale) {
		velocity.scl(scale);
	}
	
	public void scaleVelocity(float scaleX, float scaleY) {
		velocity.scl(scaleX, scaleY);
	}
	
	public void scaleAccleration(float scale) {
		acceleration.scl(scale);
	}
		
	// Getters ----------------
	public Vector2 getPosition() {
		return position;
	}
	
	public Circle getBounds() {
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
	public void setPosition(Vector2 newPosition) {
		this.position = newPosition;
		this.bounds.setX(newPosition.x);
		this.bounds.setY(newPosition.y);
	}
	
	public void setXVelocity(float newVelocity) {
		this.velocity.x = newVelocity;
	}
	
	public void setYVelocity(float newVelocity) {
		this.velocity.y = newVelocity;
	}
	
	public void setXAcceleration(float newAcceleration) {
		this.acceleration.x = newAcceleration;
	}
	
	public void setYAcceleration(float newAcceleration) {
		this.acceleration.y = newAcceleration;
	}
	
	public void setJumping(boolean newJumping) {
		this.jumping = newJumping;
	}
		
	public void setState(State newState) {
		this.state = newState;
	}
	// -------------------------
}
