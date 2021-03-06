package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import entities.Block;
import entities.Puffle;
import entities.World;

public class WorldRenderer {

	/**
	 * This class renders the game world to the screen. 
	 * -Each block in the game has the size 1 unit
	 * -All other images are scaled to this
	 */

	// number of units that fit on the screen vertically
	public static final float CAMERA_HEIGHT = 10f;

	private World world;
	private boolean editing;

	// allows the camera to only render part of the world
	private OrthographicCamera cam;

	// textures
	private TextureRegion puffleTexture;
	private TextureRegion[] blockTextures;
	private SpriteBatch spriteBatch;

	private float ppu; // pixels per unit
	private float cameraHeight;
	private float cameraWidth;
	private int screenHeight;

	public WorldRenderer(World world, boolean editing) {
		this.world = world;
		this.editing = editing;

		// set view port size
		this.cam = new OrthographicCamera(cameraWidth, CAMERA_HEIGHT);
		// set view port to middle of the world
		this.cam.position.set(cameraWidth / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		
		spriteBatch = new SpriteBatch();
		this.cameraHeight = CAMERA_HEIGHT;
		
		blockTextures = new TextureRegion[2];
		loadTextures();
	}
	
	/** Scales the size of the camera to the size of the screen */
	public void setSize(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		// calculate number of pixels per unit
		ppu = screenHeight / cameraHeight;
		this.cameraWidth = screenWidth / ppu;
	}

	/** Loads textures from texture atlas */
	private void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("images/textures/tileset.pack"));
		puffleTexture = atlas.findRegion("player");
		blockTextures[0] = atlas.findRegion("rock");
		blockTextures[1] = atlas.findRegion("grass");
	}

	public void render() {
		spriteBatch.begin();
		drawBlocks();
		drawPuffle();
		if (editing) {
			drawEditor();
		}
		spriteBatch.end();
	}

	private void drawBlocks() {
		for (Block block : world.getDrawableBlocks((int)cameraWidth, (int)CAMERA_HEIGHT)) {
			float blockSize = Block.SIZE * ppu;
			// draw block to screen
			spriteBatch.draw(blockTextures[block.getBlockID()], block.getPosition().x * ppu,
					block.getPosition().y * ppu, blockSize, blockSize);
		}
	}

	private void drawPuffle() {
		Puffle puffle = world.getPuffle();
		float puffleSize = Puffle.SIZE * ppu;
		// draw puffle to screen with rotation
		spriteBatch.draw(puffleTexture, puffle.getPosition().x * ppu,
				puffle.getPosition().y * ppu, puffleSize / 2, puffleSize / 2,
				puffleSize, puffleSize, 1f, 1f, puffle.getRotation(), true);
	}
	
	private void drawEditor() {
		
	}
	
	public float getPpu() {
		return ppu;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
}
