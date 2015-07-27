package entities.impl;

import resources.TilePosition;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Puffle {

	/**
	 *  Distance from the centre of the puffle to its outer edge. 
	 *  */
	public static final float RADIUS = 0.35f;
	
	/** Value added to the player's initial vertical spawn position so that 
	 * it 'plops' down slightly at the start of a level.
	 */
	public static final float PLOP_HEIGHT = 1f;
	
	/** Length of time to wait after damaging a block until the puffle can
	 * do any more damage
	 */
	public static final float COOLDOWN = 1f;

	/** 
	 * Starting angle of the puffle 
	 * */
	public static final float START_ROTATION = 90;
	
	/** 
	 * Angle at which to loop back to zero degrees. 
	 * */
	public static final float MAX_ROTATION = 360;
	
	/** 
	 * Current state of the puffle.
	 *  */
	public enum State {
		STOPPED, ROLLING, DYING
	}
	
	private Vector2 position;
	private Vector2 acceleration;
	private Vector2 velocity;
	private float rotation;
	private State state;
	private boolean jumping;

	public Puffle(TilePosition tilePosition) {
		this.position = new Vector2(tilePosition.x + RADIUS, tilePosition.y
				+ RADIUS + PLOP_HEIGHT);
		this.acceleration = new Vector2();
		this.velocity = new Vector2();
		this.rotation = START_ROTATION;
		this.state = State.STOPPED;
		this.jumping = false;
	}

	/**
	 * Update position of the puffle.
	 * @param delta Time interval between frames.
	 */
	public void update(float delta) {
		applyVelocity(delta);
		rotatePuffle();
	}
	
	private void applyVelocity(float delta) {
		// Scales the velocity by delta then adds the resulting
		// velocity to the current position of the puffle
		position.mulAdd(velocity, delta);
	}

	private void rotatePuffle() {
		rotation -= velocity.x;
		if (rotation > MAX_ROTATION) {
			rotation -= MAX_ROTATION;
		} else if (rotation < 0) {
			rotation += MAX_ROTATION;
		}
	}

	/**
	 * Add given acceleration values to the puffles acceleration values
	 * (this can increase or decrease the total acceleration in either direction).
	 * 
	 * @param accelerationX Acceleration in the x direction.
	 * @param accerlationY Acceleration in the y direction.
	 */
	public void applyAccleration(float accelerationX, float accerlationY) {
		velocity.add(accelerationX, accerlationY);
	}

	/** 
	 * Multiplies puffle velocity by a scalar.
	 * 
	 * @param scale The scalar.
	 */
	public void scaleVelocity(float scale) {
		velocity.scl(scale);
	}

	/** 
	 * Multiplies puffle velocity by a scalar.
	 * 
	 * @param x The x scalar.
	 * @param y The y scalar.
	 */
	public void scaleVelocity(float scaleX, float scaleY) {
		velocity.scl(scaleX, scaleY);
	}

	/**
	 * Flip puffles x velocity.
	 */
	public void changeDirection() {
		velocity.x *= -1;
	}

	/**
	 * Multiplies puffle acceleration by a scalar.
	 * @param scale The scalar.
	 */
	public void scaleAccleration(float scale) {
		acceleration.scl(scale);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Circle getBounds() {
		return new Circle(position.x, position.y, RADIUS);
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

	public void setPosition(Vector2 position) {
		this.position.x = position.x;
		this.position.y = position.y;
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

	public boolean isAccelerating() {
		return acceleration.x != 0;
	}

	public boolean isMoving() {
		return velocity.x != 0;
	}
}
