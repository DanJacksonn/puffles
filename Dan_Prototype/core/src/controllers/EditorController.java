package controllers;

import java.util.HashMap;
import java.util.Map;

import entities.Inventory;
import entities.World;

public class EditorController {

	/**
	 * This class controls the puffle's movement.
	 * -Reacts to particular keys being pressed.
	 */
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN, PLACE, BACK
	}

	private World world;
	private Inventory inventory;
	
	int selectedX;
	int selectedY;

	static Map<Keys, Boolean> keys = new HashMap<EditorController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.PLACE, false);
		keys.put(Keys.BACK, false);
	};

	public EditorController(World world) {
		this.world = world;
		this.inventory = world.getInventory();
		this.selectedX = 0;
		this.selectedY = 0;
	}
	
	public void update(float delta) {
		processInput();
		
		// do things depending on input
		
	}

	// Events ----------------
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}

	public void downPressed() {
		keys.get(keys.put(Keys.DOWN, true));
	}
	
	public void placePressed(int selectedX, int selectedY) {
		keys.get(keys.put(Keys.PLACE, true));
		this.selectedX = selectedX;
		this.selectedY = selectedY;
	}
	
	public void backPressed() {
		keys.get(keys.put(Keys.BACK, true));
	}

	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}

	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}
	
	public void placeReleased() {
		keys.get(keys.put(Keys.PLACE, false));
	}
	
	public void backReleased() {
		keys.get(keys.put(Keys.BACK, false));
	}
	// -------------------------

	private boolean processInput() {
		// if left key is pressed
		if (keys.get(Keys.PLACE)) {
			if (world.getLevel().isEmpty(selectedX, selectedY) && inventory.getNoOfBlocks() > 0) {
				world.addBlock(selectedX, selectedY, 1);
				inventory.removeBlock();
				keys.get(keys.put(Keys.PLACE, false));
			}
		}
		return false;
	}
	
}
