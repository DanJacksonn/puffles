package handlers;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

import entities.impl.Block;
import entities.impl.Puffle;

import resources.BlockType;

import entities.impl.World;

/**
 * Controls the puffle's movement and handles puffle inputs.
 */
public class PuffleHandler {

	enum Keys {
		LEFT, RIGHT, JUMP
	}
	static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    private static  float ROLL_SPEED = 2.7f;
    private static final float NORMAL_ROLL_SPEED = 2.7f;
	private static final float ROLL_ACCELERATION = 15f;
	private static final float FRICTION = 0.9f;
	private static final float GRAVITY = -29f;
	private static final float JUMP_SPEED = 9f;
	private static final int DAMAGE_COOLDOWN_PERIOD = 18;

	/** Pool of rectangles can be reused to avoid allocation */
	private Pool<Circle> circlePool = new Pool<Circle>() {
		@Override
		protected Circle newObject() {
			return new Circle();
		}
	};
	
	private World world;
	private Array<Block> collidableBlocks;
    private Array<Block> walkedOnBlocks;
	private boolean grounded;
	private int damageCooldown;

	public PuffleHandler(World world) {
		this.world = world;
		collidableBlocks = new Array<Block>();
        walkedOnBlocks = new Array<Block>();
		this.grounded = true;
		this.damageCooldown = 0;
		resetKeys();
	}

	public void resetKeys() {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
	}

	public void update(float delta) {
		if (damageCooldown > 0) {
			damageCooldown--;
		}
		processInput();
		if (world.puffle.isJumping() && grounded) {
			world.puffle.setJumping(false);
		}
		// set initial vertical acceleration
		world.puffle.setYAcceleration(GRAVITY);
		// scale acceleration to time frame units
		world.puffle.scaleAccleration(delta);
		// add puffle's acceleration it's velocity
		world.puffle.applyAccleration(world.puffle.getAcceleration().x,
				world.puffle.getAcceleration().y);
		checkCollisionWithBlocks(delta);

		updateVelocity();

		// update puffle's position
		world.puffle.update(delta);
	}

	private void updateVelocity() {
		if (!world.puffle.isAccelerating()) {
			if (world.puffle.isMoving()) {
				world.puffle.scaleVelocity(FRICTION, 1);
			} else {
				world.puffle.setState(Puffle.State.STOPPED);
			}
		}
		if (world.puffle.getVelocity().x > ROLL_SPEED) {
			world.puffle.setXVelocity(ROLL_SPEED);
		} else if (world.puffle.getVelocity().x < -ROLL_SPEED) {
			world.puffle.setXVelocity(-ROLL_SPEED);
		}
	}

	private void checkCollisionWithBlocks(float delta) {
		Circle puffleCircle;
		int x1, x2, y1, y2;

		// scale velocity to time frame units
		world.puffle.scaleVelocity(delta);

		// set rectangle to puffle's bounding box
		puffleCircle = circlePool.obtain();
		puffleCircle.set(world.puffle.getBounds().x, world.puffle.getBounds().y,
				world.puffle.getBounds().radius);

		// store y area inhabited by puffle
		y1 = (int) (world.puffle.getBounds().y - world.puffle.getBounds().radius);
		y2 = (int) (world.puffle.getBounds().y + world.puffle.getBounds().radius);

		if (world.puffle.getVelocity().x < 0) {
			// store position after moving left
			x1 = x2 = (int) Math.floor(world.puffle.getBounds().x
					- world.puffle.getBounds().radius + world.puffle.getVelocity().x);

		} else {
			// store position after moving right
			x1 = x2 = (int) Math.floor(world.puffle.getBounds().x
					+ world.puffle.getBounds().radius + world.puffle.getVelocity().x);

		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);
        //handles collitions with blocks below the player
        checkUnderfootCollision(x1, y1, x2);
		// simulate horizontal movement
		puffleCircle.x += world.puffle.getVelocity().x;

		// if puffle collides then change direction
		for (Block block : collidableBlocks) {

			if (block != null
					&& Intersector.overlaps(puffleCircle, block.getBounds())) {

				if (damageCooldown == 0 && block.isBreakable()) {
					// returns true if block is broken completely

					world.level.damageBlock(block.getTilePosition());
					if (world.level.isBlockBroken(block.getTilePosition())) {
						world.inventory.addBlock();
					}
					damageCooldown = DAMAGE_COOLDOWN_PERIOD;
				}
				world.puffle.getVelocity().x *= -1;
				break;
			}
		}

		// reset x position of puffle's bounding box
		puffleCircle.x = world.puffle.getPosition().x;

		// store x position
		x1 = (int) (world.puffle.getBounds().x - world.puffle.getBounds().radius);
		x2 = (int) (world.puffle.getBounds().x + world.puffle.getBounds().radius);

		if (world.puffle.getVelocity().y < 0) {
			// store position after moving down
			y1 = y2 = (int) Math.floor(world.puffle.getBounds().y
					- world.puffle.getBounds().radius + world.puffle.getVelocity().y);

		} else {
			// store position after moving up
			y1 = y2 = (int) Math.floor(world.puffle.getBounds().y
					+ world.puffle.getBounds().radius + world.puffle.getVelocity().y);
		}

		// store all blocks the puffle can collide with
		findCollidableBlocks(x1, y1, x2, y2);

		// simulate vertical movement
		puffleCircle.y += world.puffle.getVelocity().y;

		// if puffle collides stop vertical movement
		for (Block block : collidableBlocks) {
			if (block != null
					&& Intersector.overlaps(puffleCircle, block.getBounds())) {
				if (world.puffle.getVelocity().y < 0) {
					grounded = true;
				} else if (world.puffle.getVelocity().y > 0) {
					if (damageCooldown == 0 && block.isBreakable()) {
						// returns true if block is broken completely
						world.level.damageBlock(block.getTilePosition());
						if (world.level.isBlockBroken(block.getTilePosition())) {
							world.inventory.addBlock();
						}
						damageCooldown = DAMAGE_COOLDOWN_PERIOD;
					}
				}
				world.puffle.setYVelocity(0);
				break;
			}
		}

		// un-scale velocity from time frame units
		world.puffle.scaleVelocity(1 / delta);
	}

	/** Stores all blocks found in enclosing coordinates **/
	private void findCollidableBlocks(int x1, int y1, int x2, int y2) {
        collidableBlocks.clear();

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                // if not outside of world
                if (x >= 0 && x < world.level.getWidth() && y >= 0
                        && y < world.level.getHeight()) {
                    collidableBlocks.add(world.level.getBlock(x, y));
                }
            }
        }
    }
    private void findUnderfootBlocks(int x1, int y1, int x2) {
        walkedOnBlocks.clear();
        for (int x = x1; x <= x2; x++) {
                // if not outside of world
                if (x >= 0 && x < world.level.getWidth()) {
                    walkedOnBlocks.add(world.level.getBlock(x, y1-1));
                }

        }
	}
    private void checkUnderfootCollision(int x1, int y1, int x2){
        //finds block under player
        findUnderfootBlocks(x1, y1, x2);
        //increases speed when player is on a ice block
        for (Block block : walkedOnBlocks) {
            if (block != null  && block.getBlockType().equals(BlockType.ICE) ){

                ROLL_SPEED = 8f;
                System.out.println(block.getBlockType());
            }else{
        //when not on ice speed is reduced
                if(ROLL_SPEED > NORMAL_ROLL_SPEED){
                    ROLL_SPEED -= 0.1f;
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

	public void upPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.JUMP, false));
	}

	// -------------------------

	private void processInput() {
		if (keys.get(Keys.JUMP)) {
			// if not already jumping
			if (!world.puffle.isJumping()) {
				// jump
				world.puffle.setJumping(true);
				world.puffle.setYVelocity(JUMP_SPEED);
				grounded = false;
			}
		}

		if (keys.get(Keys.LEFT)) {
			// start moving puffle left
			world.puffle.setState(Puffle.State.ROLLING);
			world.puffle.setXAcceleration(-ROLL_ACCELERATION);
		} else if (keys.get(Keys.RIGHT)) {
			// start moving puffle right
			world.puffle.setState(Puffle.State.ROLLING);
			world.puffle.setXAcceleration(ROLL_ACCELERATION);
		} else {
			// stop moving
			world.puffle.setState(Puffle.State.STOPPED);
			world.puffle.setXAcceleration(0);
		}
	}

}
