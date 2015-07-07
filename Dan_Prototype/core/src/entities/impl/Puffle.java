package entities.impl;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import entities.api.IPuffle;

public class Puffle implements IPuffle {
	
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

	@Override
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

	@Override
	public void applyAccleration(float accelerationX, float accerlationY) {
		velocity.add(accelerationX, accerlationY);
	}

	@Override
	public void scaleVelocity(float scale) {
		velocity.scl(scale);
	}

	@Override
	public void scaleVelocity(float scaleX, float scaleY) {
		velocity.scl(scaleX, scaleY);
	}

	@Override
	public void changeDirection() {
		velocity.x *= -1;
	}

	@Override
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
}
