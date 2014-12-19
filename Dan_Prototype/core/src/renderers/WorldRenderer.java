package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType; 
import com.badlogic.gdx.math.Circle;

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
	public static final Color OVERLAY_COLOR = new Color(0.5f, 0.5f, 0.7f, 0.4f);
	public static final Color GRID_COLOR = new Color(0.35f, 0.35f, 0.5f, 0);

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
	private float cameraWidth; // units
	private float cameraHeight; // units

	public WorldRenderer(World world, Editor editor) {
		this.world = world;
		this.editor = editor;
		this.cam = new OrthographicCamera();
		this.shapeRenderer = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();
		this.cameraHeight = CAMERA_HEIGHT;
		
		blockTextures = new TextureRegion[2];
		loadTextures();
	}
	
	/** Scales the size of the camera to the size of the screen */
	public void setSize(int screenWidth, int screenHeight) {		
		// calculate number of pixels per unit
		ppu = screenHeight / cameraHeight;
		this.cameraWidth = screenWidth / ppu;
		
		// update camera viewport
		cam.viewportHeight = screenHeight;
		cam.viewportWidth = screenWidth;
		cam.position.set(screenWidth / 2f, screenHeight / 2f, 0);
		cam.update();
		
		spriteBatch.setProjectionMatrix(cam.combined);
		shapeRenderer.setProjectionMatrix(cam.combined);
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
		spriteBatch.end();
		//debug();
		if (editor != null) drawEditor();
		if (editor != null) {
			spriteBatch.begin();
			drawPlacedBlocks();
			spriteBatch.end();
		}
	}
	
	private void debug() {
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.begin(ShapeType.Filled);
		Circle bounds = world.getPuffle().getBounds();
		shapeRenderer.circle(bounds.x * ppu, bounds.y * ppu, bounds.radius * ppu);
		shapeRenderer.end();
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
		spriteBatch.draw(puffleTexture, (puffle.getPosition().x - puffle.getRadius()) * ppu,
				(puffle.getPosition().y - puffle.getRadius()) * ppu, puffleSize / 2, puffleSize / 2,
				puffleSize, puffleSize, 1f, 1f, puffle.getRotation(), true);
	}
	
	private void drawPlacedBlocks() {
		// draw placeable blocks to screen
		for (Block block : editor.getPlacedBlocks()) {
			float blockSize = Block.SIZE * ppu;
			spriteBatch.draw(blockTextures[block.getBlockID()], block.getPosition().x * ppu,
					block.getPosition().y * ppu, blockSize, blockSize);
		}
	}
	
	private void drawEditor() {
		// draw overlay
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setColor(OVERLAY_COLOR);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(0, 0, cameraWidth * ppu, cameraHeight * ppu);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		// draw grid
		shapeRenderer.setColor(GRID_COLOR);
		shapeRenderer.begin(ShapeType.Line);
		for (int row = 0; row < cameraWidth; row++) {
			shapeRenderer.line(row * ppu , 0, row * ppu, cameraHeight * ppu);
		}
		for (int col = 0; col < cameraHeight; col++) {
			shapeRenderer.line(0, col * ppu, cameraWidth * ppu, col * ppu);
		}
		shapeRenderer.end();
	}
	
	public float getPpu() {
		return ppu;
	}
	
	public float getScreenHeight() {
		return cameraHeight * ppu;
	}
}
