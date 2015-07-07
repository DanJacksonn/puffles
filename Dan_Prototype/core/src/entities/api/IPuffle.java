package entities.api;

public interface IPuffle {

	/** Distance from the centre of the puffle to its outer edge. */
	public static final float RADIUS = 0.35f;
	
	/** Value added to the player's initial vertical spawn position so that 
	 * it 'plops' down slightly at the start of a level.
	 */
	public static final float PLOP_HEIGHT = 1f;
	
	/** Length of time to wait after damaging a block until the puffle can
	 * do any more damage
	 */
	public static final float COOLDOWN = 1f;

	/** Starting angle of the puffle */
	public static final float START_ROTATION = 90;
	
	/** Angle at which to loop back to zero degrees. */
	public static final float MAX_ROTATION = 360;
	
	/** Current state of the puffle. */
	public enum State {
		STOPPED, ROLLING, DYING
	}
	
	/**
	 * Update position of the puffle.
	 * @param delta Time interval between frames.
	 */
	public abstract void update(float delta);

	/**
	 * Add given acceleration values to the puffles acceleration values
	 * (this can increase or decrease the total acceleration in either direction).
	 * @param accelerationX Acceleration in the x direction.
	 * @param accerlationY Acceleration in the y direction.
	 */
	public abstract void applyAccleration(float accelerationX,
			float accerlationY);

	/** 
	 * Multiplies puffle velocity by a scalar
	 * @param scale The scalar.
	 */
	public abstract void scaleVelocity(float scale);

	/**
	 * Multiplies puffle velocity by a scalar.
	 * @param scaleX Scalar x value.
	 * @param scaleY Scalar Y value.
	 */
	public abstract void scaleVelocity(float scaleX, float scaleY);

	/**
	 * Flip puffles x velocity.
	 */
	public abstract void changeDirection();

	/**
	 * Multiplies puffle acceleration by a scalar.
	 * @param scale The scalar.
	 */
	public abstract void scaleAccleration(float scale);
}