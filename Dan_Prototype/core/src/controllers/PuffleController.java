package controllers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import entities.Block;
import entities.Inventory;
import entities.Level;
import entities.Puffle;
import entities.World;
import entities.Puffle.State;

public class PuffleController {

	/**
	 * This class controls the puffle's movement. -Reacts to particular keys
	 * being pressed.
	 */

	enum Keys {
		LEFT, RIGHT, JUMP
	}

	static Map<Keys, Boolean> keys = new HashMap<PuffleController.Keys, Boolean>();

	// horizontal vectors
	private static final float ROLL_ACCELERATION = 11f;
	private static final float ROLL_SPEED = 2.6f;
	private static final float FRICTION = 0.9f;
	// vertical vectors
	private static final float GRAVITY = -29f;
	private static final float JUMP_SPEED = 8.8f;

	// world entities
	private Puffle puffle;
	private Level level;
	private Inventory inventory;

	private boolean grounded;

	// pool of rectangles can be reused to avoid allocation
	private Pool<Circle> circlePool = new Pool<Circle>() {
		@Override
		protected Circle newObject() {
			return new Circle();
		}
	};

	private Array<Block> collidableBlocks;

	public PuffleController(World world) {
		this.level = world.getLevel();
		this.inventory = world.getInventory();
		this.puffle = world.getPuffle();
		this.grounded = true;
		collidableBlocks = new Array<Block>();
		resetKeys();
	}

	public void resetKeys() {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
	}

	public void update(float delta) {
		processInput();

		if (puffle.isJumping() && grounded) {
			// stop jumping when grounded
			puffle.setJumping(false);
		}

		// set initial vertical acceleration
		puffle.setYAcceleration(GRAVITY);

		// scale acceleration to time frame units
		puffle.scaleAccleration(delta);

		// add puffle's acceleration it's velocity
		puffle.applyAccleration(puffle.getAcceleration().x,
				puffle.getAcceleration().y);

		checkCollisionWithBlocks(delta);

		// apply friction when not accelerating
		if (puffle.getAcceleration().x == 0) {
			if (puffle.getVelocity().x == 0) {
				puffle.setState(State.STOPPED);
			} else {
				puffle.scaleVelocity(FRICTION, 1);
			}
		}

		// make sure max speed is not exceeded
		if (puffle.getVelocity().x > ROLL_SPEED) {
			puffle.setXVelocity(ROLL_SPEED);
		} else if (puffle.getVelocity().x < -ROLL_SPEED) {
			puffle.setXVelocity(-ROLL_SPEED);
		}

		// update puffle's position
		puffle.update(delta);
	}

	private void checkCollisionWithBlocks(float delta) {
		Circle puffleCircle;
		int x1, x2, y1, y2;

		// scale velocity to time frame units
		puffle.scaleVelocity(delta);

		// set rectangle to puffle's bounding box
		puffleCircle = circlePool.obtain();
		puffleCircle.set(puffle.getBounds().x, puffle.getBounds().y,
				puffle.getBounds().radius);

		// store y area inhabited by puffle
		y1 = (int) (puffle.getBounds().y - puffle.getBounds().radius);
		y2 = (int) (puffle.getBounds().y + puffle.getBounds().radius);

		if (puffle.getVelocity().x < 0) {
			// store position after moving left
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					- puffle.getBounds().radius + puffle.getVelocity().x);

		} else {
			// store position after moving right
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					+ puffle.getBounds().radius + puffle.getVelocity().x);

		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);

		// simulate horizontal movement
		puffleCircle.x += puffle.getVelocity().x;

		// if puffle collides then change direction
		for (Block block : collidableBlocks) {
			if (block != null
					&& Intersector.overlaps(puffleCircle, block.getBounds())) {
				if (block.isBreakable()) {
					if (level.breakBlock(block.getPosition())) {
						inventory.addBlock();
					}
				}
				puffle.getVelocity().x *= -1;
				break;
			}
		}

		// reset x position of puffle's bounding box
		puffleCircle.x = puffle.getPosition().x;

		// store x position
		x1 = (int) (puffle.getBounds().x - puffle.getBounds().radius);
		x2 = (int) (puffle.getBounds().x + puffle.getBounds().radius);

		if (puffle.getVelocity().y < 0) {
			// store position after moving down
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					- puffle.getBounds().radius + puffle.getVelocity().y);

		} else {
			// store position after moving up
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					+ puffle.getBounds().radius + puffle.getVelocity().y);
		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);

		// simulate vertical movement
		puffleCircle.y += puffle.getVelocity().y;

		// if puffle collides stop vertical movement
		for (Block block : collidableBlocks) {
			if (block != null
					&& Intersector.overlaps(puffleCircle, block.getBounds())) {
				if (puffle.getVelocity().y < 0)
					grounded = true;
				puffle.setYVelocity(0);
				break;
			}
		}

		// un-scale velocity from time frame units
		puffle.scaleVelocity(1 / delta);
	}

	/** Stores all blocks found in enclosing coordinates **/
	private void findCollidableBlocks(int x1, int y1, int x2, int y2) {
		collidableBlocks.clear();

		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				// if not outside of world
				if (x >= 0 && x < level.getWidth() && y >= 0
						&& y < level.getHeight()) {
					collidableBlocks.add(level.getBlock(x, y));
				}
			}
		}
	}

	// Events ----------------
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
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

	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
	}

	// -------------------------

	private void processInput() {
		if (keys.get(Keys.JUMP)) {
			// if not already jumping
			if (!puffle.isJumping()) {
				// jump
				puffle.setJumping(true);
				puffle.setYVelocity(JUMP_SPEED);
				grounded = false;
			}
		}

		if (keys.get(Keys.LEFT)) {
			// start moving puffle left
			puffle.setState(State.ROLLING);
			puffle.setXAcceleration(-ROLL_ACCELERATION);
		} else if (keys.get(Keys.RIGHT)) {
			// start moving puffle right
			puffle.setState(State.ROLLING);
			puffle.setXAcceleration(ROLL_ACCELERATION);
		} else {
			// stop moving
			puffle.setState(State.STOPPED);
			puffle.setXAcceleration(0);
		}
	}

}
