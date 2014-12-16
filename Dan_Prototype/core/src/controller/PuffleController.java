package controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import model.Block;
import model.Puffle;
import model.World;
import model.Puffle.State;

public class PuffleController {

	enum Keys {
		LEFT, RIGHT, STOP, JUMP
	}

	private static final float ACCELERATION = 10f;
	private static final float GRAVITY = -28f;
	private static final float JUMP_SPEED = 9f;
	private static final float FRICTION = 0.9f;
	private static final float MAX_SPEED = 3f;

	private World world;
	private Puffle puffle;
	private boolean grounded = false;

	// Rectangle pool used for collision detection
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	private Pool<Circle> circlePool = new Pool<Circle>() {
		@Override
		protected Circle newObject() {
			return new Circle();
		}
	};

	private Array<Block> collidable = new Array<Block>();

	static Map<Keys, Boolean> keys = new HashMap<PuffleController.Keys, Boolean>();

	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.STOP, false);
		keys.put(Keys.JUMP, false);
	};

	public PuffleController(World world) {
		this.world = world;
		this.puffle = world.getPuffle();
	}

	// Events ----------------
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void stopPressed() {
		keys.get(keys.put(Keys.STOP, true));
	}

	public void jumpPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void stopReleased() {
		keys.get(keys.put(Keys.STOP, false));
	}

	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
	}
	// -------------------------

	public void update(float delta) {
		processInput();

		// if bob is jumping and grounded stop jumping
		if (grounded && puffle.isJumping()) {
			puffle.setJumping(false);
		}
		
		// set initial vertical acceleration
		puffle.getAcceleration().y = GRAVITY;
		
		// convert acceleration to time frame
		puffle.getAcceleration().scl(delta);

		// apply acceleration
		puffle.getVelocity().add(puffle.getAcceleration().x,
				puffle.getAcceleration().y);

		checkCollisionWithBlocks(delta);
		
		if (puffle.getAcceleration().x == 0) {
			puffle.getVelocity().x *= FRICTION;
		}

		// make sure max speed is not exceeded
		if (puffle.getVelocity().x > MAX_SPEED) {
			puffle.getVelocity().x = MAX_SPEED;
		} else if (puffle.getVelocity().x < -MAX_SPEED) {
			puffle.getVelocity().x = -MAX_SPEED;
		}

		// update state time
		puffle.update(delta);
	}

	private void checkCollisionWithBlocks(float delta) {
		int x1, x2, y1, y2;
		
		// scale velocity to frame units
		puffle.getVelocity().scl(delta);

		// set rectangle to puffle's bounding box
		Rectangle puffleRect = rectPool.obtain();
		puffleRect.set(puffle.getBounds().x, puffle.getBounds().y,
				puffle.getBounds().width, puffle.getBounds().height);

		// store current y position
		y1 = (int) puffle.getBounds().y;
		y2 = (int) (puffle.getBounds().y + puffle.getBounds().height);

		if (puffle.getVelocity().x < 0) {
			// store position after moving left
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					+ puffle.getVelocity().x);

		} else {
			// store position after moving right
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					+ puffle.getBounds().width + puffle.getVelocity().x);

		}

		// store all blocks the puffle can collide with
		populateCollidableBlocks(x1, y1, x2, y2);

		// simulate horizontal movement
		puffleRect.x += puffle.getVelocity().x;
		
		// if puffle collides then change direction
		for (Block block : collidable) {
			if (block == null) continue;
			if (puffleRect.overlaps(block.getBounds())) {
				puffle.getVelocity().x *= -1;
				break;
			}
		}

		// reset x position of puffle's bounding box
		puffleRect.x = puffle.getPosition().x;

		// store x position
		x1 = (int) puffle.getBounds().x;
		x2 = (int) (puffle.getBounds().x + puffle.getBounds().width);

		if (puffle.getVelocity().y < 0) {
			// store position after moving down
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					+ puffle.getVelocity().y);

		} else {
			// store position after moving up
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					+ puffle.getBounds().height + puffle.getVelocity().y);
		}
		
		// store all blocks the puffle can collide with
		populateCollidableBlocks(x1, y1, x2, y2);
		
		// simulate vertical movement
		puffleRect.y += puffle.getVelocity().y;

		// if puffle collides stop vertical movement
		for (Block block : collidable) {
			if (block == null) continue;
			if (puffleRect.overlaps(block.getBounds())) {
				if (puffle.getVelocity().y < 0) {
					grounded = true;
				}
				puffle.getVelocity().y = 0;
				break;
			}
		}

		// reset y position of puffle
		puffleRect.y = puffle.getPosition().y;

		// un-scale velocity
		puffle.getVelocity().scl(1 / delta);
	}

	/** Stores all blocks found in enclosing coordinates **/
	private void populateCollidableBlocks(int x1, int y1, int x2,
			int y2) {
		collidable.clear();

		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				// if not outside of world
				if (x >= 0 && x < world.getLevel().getWidth() && y >= 0
						&& y < world.getLevel().getHeight()) {
					collidable.add(world.getLevel().getBlock(x, y));
				}
			}
		}

	}

	private boolean processInput() {
		// if jump key is pressed
		if (keys.get(Keys.JUMP)) {
			// if not already jumping
			if (!puffle.isJumping()) {
				// jump
				puffle.setJumping(true);
				puffle.getVelocity().y = JUMP_SPEED;
				grounded = false;
			}
		}

		if (keys.get(Keys.LEFT)) {
			// start moving puffle left
			puffle.setState(State.ROLLING);
			puffle.getAcceleration().x = -ACCELERATION;
		} else if (keys.get(Keys.RIGHT)) {
			// start moving puffle right
			puffle.setState(State.ROLLING);
			puffle.getAcceleration().x = ACCELERATION;
		} else {
			// stop moving
			puffle.setState(State.STOPPED);
			puffle.getAcceleration().x = 0;
		}

		return false;
	}

}
