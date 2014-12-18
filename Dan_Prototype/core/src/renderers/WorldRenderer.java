package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import entities.Block;
import entities.Editor;
import entities.Puffle;
import entities.World;

public class WorldRenderer {

	/**
	 * This class renders the game world to the screen. 
	 * -Each block in the game has the size 1 unit
	 * -All other images are scaled to this
	 */

	// number of units to fit on the screen vertically
	public static final float CAMERA_HEIGHT = 10f;

	private World world;
	private Editor editor;

	// allows the camera to only render part of the world
	private OrthographicCamera cam;
	
	private ShapeRenderer shapeRenderer;

	// textures
	private TextureRegion puffleTexture;
	private TextureRegion[] blockTextures;
	private SpriteBatch spriteBatch;

	private float ppu; // pixels per unit
	private float cameraHeight;
	private float cameraWidth;
	private int screenHeight;

	public WorldRenderer(World world, Editor editor) {
		this.world = world;
		this.editor = editor;
		// set view port size
		this.cam = new OrthographicCamera(cameraWidth, CAMERA_HEIGHT);
		// set view port to middle of the world
		this.cam.position.set(0, 0, 0);
		this.cam.update();
		
		shapeRenderer = new ShapeRenderer();
		
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
		// update camera viewport
		cam.viewportHeight = cameraHeight;
		cam.viewportWidth = cameraWidth;
		this.cam.position.set(cameraWidth / 2f, cameraHeight / 2f, 0);
		cam.update();
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
		if (editor.isEnabled()) drawPlacedBlocks();
		spriteBatch.end();
		if (editor.isEnabled()) drawEditor();
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
	
	private void drawPlacedBlocks() {
		// make sprite batch transparent
		Color color = spriteBatch.getColor();
		color.a = 0.5f;
		spriteBatch.setColor(color);
		
		// draw placeable blocks to screen
		for (Block block : editor.getPlacedBlocks()) {
			float blockSize = Block.SIZE * ppu;
			spriteBatch.draw(blockTextures[block.getBlockID()], block.getPosition().x * ppu,
					block.getPosition().y * ppu, blockSize, blockSize);
		}
		
		// reset transparency
		color.a = 1f;
		spriteBatch.setColor(color);
	}
	
	private void drawEditor() {
		shapeRenderer.setColor(0.50f, 0.50f, 0.60f, 0f);
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		
		for (int row = 0; row < cam.viewportWidth; row++) {
			shapeRenderer.line(row , 0, row, cam.viewportHeight);
		}
		
		for (int col = 0; col < cam.viewportHeight; col++) {
			shapeRenderer.line(0, col, cam.viewportWidth, col);
		}
		shapeRenderer.end();
	}
	
	public float getPpu() {
		return ppu;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
}
