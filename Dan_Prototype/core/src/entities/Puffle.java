package entities;

import com.badlogic.gdx.math.Circle;
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
	
	public static final float RADIUS = 0.35f;
	
	// position
	private Vector2 position;
	private Circle bounds;
	
	// movement
	private Vector2 acceleration;
	private Vector2 velocity;
	private float rotation;
	
	// states
	private State state;
	private boolean jumping;
	
	public Puffle(Vector2 newPosition) {
		this.position = new Vector2(newPosition.add(RADIUS, RADIUS + 1));
		this.bounds = new Circle(position.x, position.y, RADIUS);
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
		this.rotation = 90;
		this.state = State.STOPPED;
		this.jumping = false;
	}

	/** Move puffle **/
	public void update(float delta) {
		// add distance travelled in delta seconds to current position
		position.add(velocity.cpy().scl(delta));
		
		//update bounds
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
	
	public float getRadius() {
		return bounds.radius;
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
