package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import entities.Block;
import entities.Editor;
import entities.Inventory;
import entities.Puffle;
import entities.World;

public class WorldRenderer {

	/**
	 * This class renders the game world to the screen. -Each block in the game
	 * has the size 1 unit -All other images are scaled to this
	 */

	// file locations
	public static final FileHandle TILESET_LOCATION = Gdx.files
			.internal("images/textures/tileset.pack");
	public static final FileHandle FONT_FNT_LOCATION = Gdx.files
			.internal("data/font.fnt");
	public static final FileHandle FONT_PNG_LOCATION = Gdx.files
			.internal("data/font.png");

	// colours
	public static final Color OVERLAY_COLOR = new Color(0.5f, 0.5f, 0.7f, 0.4f);
	public static final Color GRID_COLOR = new Color(0.35f, 0.35f, 0.5f, 0);

	public static final float CAMERA_HEIGHT = 10f; // units
	public static final int MAX_BLOCK_TYPES = 2;
	public static final int MAX_DAMAGE = 2;

	private World world;
	private Editor editor;

	// allows the camera to only render part of the world
	private OrthographicCamera camera;

	private ShapeRenderer shapeRenderer;

	private SpriteBatch spriteBatch;

	// textures
	private TextureRegion[] blockTextures;
	private TextureRegion[] damageTextures;
	private TextureRegion puffleTexture;
	BitmapFont font;

	private float ppu; // pixels per unit
	private float cameraWidth; // units
	private float cameraHeight; // units

	public WorldRenderer(World world, Editor editor) {
		// load renderables
		this.world = world;
		this.editor = editor;

		// load renderer tools
		this.camera = new OrthographicCamera();
		this.cameraHeight = CAMERA_HEIGHT;
		this.shapeRenderer = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();

		// load textures
		blockTextures = new TextureRegion[MAX_BLOCK_TYPES];
		damageTextures = new TextureRegion[MAX_DAMAGE];
		loadTextures();

		// load font
		this.font = new BitmapFont(FONT_FNT_LOCATION, FONT_PNG_LOCATION, false);
		font.setScale(0.5f);
		font.setColor(Color.WHITE);
	}

	/** Loads textures from texture atlas */
	private void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(TILESET_LOCATION);
		this.puffleTexture = atlas.findRegion("player");

		this.blockTextures[0] = atlas.findRegion("stone");
		this.blockTextures[1] = atlas.findRegion("grass");

		// load crack textures
		for (int i = 0; i < damageTextures.length; i++) {
			this.damageTextures[i] = atlas.findRegion("crack" + (i + 1));
		}
	}

	/** Scales the size of the camera to the size of the screen */
	public void setSize(int screenWidth, int screenHeight) {
		// calculate number of pixels per unit
		ppu = screenHeight / cameraHeight;
		this.cameraWidth = screenWidth / ppu;

		// update camera viewport
		camera.viewportHeight = screenHeight;
		camera.viewportWidth = screenWidth;
		camera.position.set(screenWidth / 2f, screenHeight / 2f, 0);
		camera.update();

		// update render tools to fit new camera size
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	public void render() {
		// draw world
		spriteBatch.begin();
		drawBlocks();
		drawPuffle();
		spriteBatch.end();

		// draw editor
		if (editor != null) {
			drawEditor();
			drawPlacedBlocks();
		}

		// draw inventory
		drawInventory();
	}

	private void drawBlocks() {
		for (Block block : world.getDrawableBlocks((int) cameraWidth,
				(int) CAMERA_HEIGHT)) {
			float blockSize = Block.SIZE * ppu;
			// draw block to screen
			spriteBatch.draw(blockTextures[block.getBlockID()],
					block.getPosition().x * ppu, block.getPosition().y * ppu,
					blockSize, blockSize);
			// draw damage to screen
			int damageValue = block.getDamageValue();
			if (damageValue > 0) {
				spriteBatch.draw(damageTextures[damageValue - 1],
						block.getPosition().x * ppu, block.getPosition().y
								* ppu, blockSize, blockSize);
			}
		}
	}

	private void drawPuffle() {
		Puffle puffle = world.getPuffle();
		float puffleSize = (Puffle.RADIUS * 2) * ppu;
		// draw puffle to screen with rotation
		spriteBatch.draw(puffleTexture,
				(puffle.getPosition().x - Puffle.RADIUS) * ppu,
				(puffle.getPosition().y - Puffle.RADIUS) * ppu, puffleSize / 2,
				puffleSize / 2, puffleSize, puffleSize, 1f, 1f,
				puffle.getRotation(), true);
	}

	private void drawInventory() {
		Inventory inventory = world.getInventory();
		
		// store size of inventory
		float invWidth = inventory.getBounds().width * ppu;
		float invHeight = inventory.getBounds().height * ppu;

		// store position of inventory
		float invXPosition = inventory.getBounds().x * ppu;
		float invYPosition = ((cameraHeight - inventory.getBounds().y) * ppu)
				- invHeight;

		// store number of blocks in inventory as string
		String invText = Integer.toString(inventory.getNoOfBlocks());

		// get height of text when written in font
		float textHeight = font.getBounds(invText).height;

		// store position of text in relation to inventory
		float textXPosition = invXPosition
				+ (inventory.getTextOffset().x * ppu);
		float textYPosition = invYPosition + textHeight
				+ (inventory.getTextOffset().y * ppu);

		// draw
		spriteBatch.begin();
		// inventory
		spriteBatch.draw(blockTextures[1], invXPosition, invYPosition,
				invWidth, invHeight);
		// text
		font.draw(spriteBatch, invText, textXPosition, textYPosition);
		spriteBatch.end();
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
			shapeRenderer.line(row * ppu, 0, row * ppu, cameraHeight * ppu);
		}
		for (int col = 0; col < cameraHeight; col++) {
			shapeRenderer.line(0, col * ppu, cameraWidth * ppu, col * ppu);
		}
		shapeRenderer.end();
	}

	private void drawPlacedBlocks() {
		spriteBatch.begin();
		// draw placeable blocks to screen
		for (Block block : editor.getPlacedBlocks()) {
		float blockSize = Block.SIZE * ppu;
			spriteBatch.draw(blockTextures[block.getBlockID()],
					block.getPosition().x * ppu, block.getPosition().y * ppu,
					blockSize, blockSize);
		}
		spriteBatch.end();
	}

	public float getPpu() {
		return ppu;
	}

	public float getScreenHeight() {
		return cameraHeight * ppu;
	}
}
