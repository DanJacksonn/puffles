package model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {

	/**
	 * This class represents the current game world. 
	 * -Every world has a level and a player.
	 */

	Level level;
	Puffle puffle;

	public World() {
		createDemoWorld();
	}

	public void createDemoWorld() {
		puffle = new Puffle(new Vector2(1, 2));
		level = new Level();
	}

	// Getters --------------------
	public Puffle getPuffle() {
		return puffle;
	}

	public Level getLevel() {
		return level;
	}

	// -----------------------------

	/** Returns a list of blocks that are in the cameras window **/
	public List<Block> getDrawableBlocks(int width, int height) {
		List<Block> blocks = new ArrayList<Block>();
		Block block; // temporary
		
		// find leftmost block
		int x = (int) puffle.getPosition().x - width;
		// find lowest block
		int y = (int) puffle.getPosition().y - height;
		
		// if out of world set to edge of world
		if (x < 0) x = 0;
		if (y < 0) y = 0;

		// find rightmost block
		int x2 = x + 2 * width;
		// find highest block
		int y2 = y + 2 * height;

		// if out of world set to edge of world
		if (x2 > level.getWidth()) x2 = level.getWidth() - 1;
		if (y2 > level.getHeight()) y2 = level.getHeight() - 1;

		// add blocks within camera window to list
		for (int col = x; col <= x2; col++) {
			for (int row = y; row <= y2; row++) {
				block = level.getBlocks()[col][row];
				if (block != null) {
					blocks.add(block);
				}
			}
		}
		
		return blocks;
	}
}
