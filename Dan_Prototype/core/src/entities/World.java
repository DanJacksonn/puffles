package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class World {

	/**
	 * This class represents the current game world. 
	 * -Every world has a level, a player and an inventory.
	 */

	Level level;
	Puffle puffle;
	Inventory inventory;
	Settings settings;
	public World(Settings settings) {
		level = new Level();
		puffle = new Puffle(new Vector2(2, 5));
		inventory = new Inventory();
		this.settings = settings;
		
		
	}

	/** Returns a list of blocks that are in the cameras window **/
	public List<Block> getDrawableBlocks(int cameraWidth, int cameraHeight) {
		List<Block> blocks = new ArrayList<Block>();
		Block block; // temporary
		
		// find leftmost block
		int x = (int) puffle.getPosition().x - cameraWidth;
		// find lowest block
		int y = (int) puffle.getPosition().y - cameraHeight;
		
		// if out of world set to edge of world
		if (x < 0) x = 0;
		if (y < 0) y = 0;

		// find rightmost block
		int x2 = x + 2 * cameraWidth;
		// find highest block
		int y2 = y + 2 * cameraHeight;

		// if out of world set to edge of world
		if (x2 > level.getWidth()) x2 = level.getWidth() - 1;
		if (y2 > level.getHeight()) y2 = level.getHeight() - 1;

		// add blocks within camera window to list
		for (int row = x; row <= x2; row++) {
			for (int col = y; col <= y2; col++) {
				block = level.getBlocks()[row][col];
				if (block != null) {
					blocks.add(block);
				}
			}
		}
		
		return blocks;
	}
	
	public void updateLevel(Level level) {
		this.level = level;
	}
	
	// Getters --------------------
	public Puffle getPuffle() {
		return puffle;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public Level getLevel() {
		return level;
	}
	public Settings getSettings(){
		
		return settings;
		
		
	}
	// -----------------------------
}
