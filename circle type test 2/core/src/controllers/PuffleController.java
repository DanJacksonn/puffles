package controllers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.puffles.Puffles;

import entities.Block;
import entities.Inventory;
import entities.Level;
import entities.Puffle;
import entities.World;
import entities.Puffle.State;

public class PuffleController {

	/**
	 * This class controls the puffle's movement. 
	 * -Reacts to particular keys being pressed.
	 */

	enum Keys {
		LEFT, RIGHT, JUMP, EDIT
	}

	// horizontal vectors
	private static final float ROLL_ACCELERATION = 11f;
	private static final float ROLL_SPEED = 2.5f;
	private static final float FRICTION = 0.9f;
	// vertical vectors
	private static final float GRAVITY = -29f;
	private static final float JUMP_SPEED = 9f;

	private Puffles game;
	private World world;
	
	// world entities
	private Level level;
	private Inventory inventory;
	private Puffle puffle;
	
	private boolean grounded;

	// pool of rectangles can be reused to avoid allocation
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	private Array<Block> collidableBlocks;

	static Map<Keys, Boolean> keys = new HashMap<PuffleController.Keys, Boolean>();

	public PuffleController(Puffles game, World world) {
		this.game = game;
		this.world = world;
		this.level = world.getLevel();
		this.inventory = world.getInventory();
		this.puffle = world.getPuffle();
		this.grounded = true;
		collidableBlocks = new Array<Block>();
		resetKeys();
	}
	
	private void resetKeys() {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.EDIT, false);
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
			puffle.scaleVelocity(FRICTION, 1);
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
		Rectangle puffleRect;
		int x1, x2, y1, y2;

		// scale velocity to time frame units
		puffle.scaleVelocity(delta);

		// set rectangle to puffle's bounding box
		puffleRect = rectPool.obtain();
		puffleRect.set(puffle.getBounds().x, puffle.getBounds().y,puffle.getBounds().radius, puffle.getBounds().radius);

		// store current y position
		y1 = (int) puffle.getBounds().y;
		y2 = (int) (puffle.getBounds().y + puffle.getBounds().radius);

		if (puffle.getVelocity().x < 0) {
			// store position after moving left
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					+ puffle.getVelocity().x);

		} else {
			// store position after moving right
			x1 = x2 = (int) Math.floor(puffle.getBounds().x
					+ puffle.getBounds().radius + puffle.getVelocity().x);

		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);

		// simulate horizontal movement
		puffleRect.x += puffle.getVelocity().x;

		// if puffle collides then change direction
		for (Block block : collidableBlocks) {
			if (block != null && puffleRect.overlaps(block.getBounds())) {
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
		puffleRect.x = puffle.getPosition().x;

		// store x position
		x1 = (int) puffle.getBounds().x;
		x2 = (int) (puffle.getBounds().x + puffle.getBounds().radius);

		if (puffle.getVelocity().y < 0) {
			// store position after moving down
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					+ puffle.getVelocity().y);

		} else {
			// store position after moving up
			y1 = y2 = (int) Math.floor(puffle.getBounds().y
					+ puffle.getBounds().radius + puffle.getVelocity().y);
		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);

		// simulate vertical movement
		puffleRect.y += puffle.getVelocity().y;

		// if puffle collides stop vertical movement
		for (Block block : collidableBlocks) {
			if (block != null && puffleRect.overlaps(block.getBounds())) {
				if (puffle.getVelocity().y < 0)
					grounded = true;
				puffle.setYVelocity(0);
				break;
			}
		}

		// reset y position of puffle
		puffleRect.y = puffle.getPosition().y;

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

	public void editPressed(Puffles game) {
		keys.get(keys.put(Keys.EDIT, true));
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
		if (keys.get(Keys.EDIT)) {
			resetKeys();
			// update the game world
			game.editorScreen.updateWorld(world);
			// switch to edit mode
			game.setScreen(game.editorScreen);
		} else {

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

}
