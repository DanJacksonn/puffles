package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import entities.Block;
import entities.Editor;
import entities.Inventory;
import entities.Puffle;
import entities.Settings;
import entities.World;

public class WorldRenderer {

	/**
	 * This class renders the game world to the screen. Each block in the game
	 * has the size 1 unit: all other images are scaled to this.
	 */

	// file locations
	public static final FileHandle BACKGROUND_LOCATION = Gdx.files
			.internal("images/background.png");
	public static final FileHandle TILESET_LOCATION = Gdx.files
			.internal("images/textures/tileset.pack");
	public static final FileHandle FONT_FNT_LOCATION = Gdx.files
			.internal("data/font.fnt");
	public static final FileHandle FONT_PNG_LOCATION = Gdx.files
			.internal("data/font.png");

	// colours
	public static final Color OVERLAY_COLOR = new Color(0.5f, 0.5f, 0.7f, 0.4f);
	public static final Color GRID_COLOR = new Color(0.35f, 0.35f, 0.5f, 0);

	public static final float CAMERA_HEIGHT = 11f; // units
	public static final float SCROLL_GAP = 5f;
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
	private Texture backgroundTexture;
	private float backgroundWidth;
	BitmapFont font;

	private float ppu; // pixels per unit
	private Vector2 cameraPosition; // bottom left of camera
	private float cameraWidth; // units
	private float cameraHeight; // units

	public WorldRenderer(World world, Editor editor) {
		// load renderables
		this.world = world;
		this.editor = editor;

		// load render tools
		this.camera = new OrthographicCamera();
		this.cameraHeight = CAMERA_HEIGHT;
		this.cameraPosition = new Vector2(world.getPuffle().getPosition().x, world.getPuffle().getPosition().y);
		this.shapeRenderer = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();

		// load textures
		this.blockTextures = new TextureRegion[MAX_BLOCK_TYPES];
		this.damageTextures = new TextureRegion[MAX_DAMAGE];

		loadTextures();

		// load font
		this.font = new BitmapFont(FONT_FNT_LOCATION, FONT_PNG_LOCATION, false);
		font.setScale(0.5f);
		font.setColor(Color.WHITE);

		// load background
		this.backgroundTexture = new Texture(BACKGROUND_LOCATION);
		// calculate how much of background to render on width
		this.backgroundWidth = (cameraHeight * backgroundTexture.getWidth())
				/ backgroundTexture.getHeight();
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
	}

	public void updateCameraPosition() {
		Vector2 pufflePosition = world.getPuffle().getPosition();
		// keep puffle in middle of camera on x axis
		cameraPosition.x = pufflePosition.x - (cameraWidth / 2);

		// if camera is outside of the world

		if (cameraPosition.x < 0) {
			// snap to left of world
			cameraPosition.x = 0;
			
		} else if (cameraPosition.x + cameraWidth > world.getLevel().getWidth()) {
			cameraPosition.x = world.getLevel().getWidth() - cameraWidth;
		}

		// if puffle is near top/bottom of camera
		if (pufflePosition.y - SCROLL_GAP < cameraPosition.y) {
			// move camera down
			cameraPosition.y = pufflePosition.y - SCROLL_GAP;
		} else if (pufflePosition.y + SCROLL_GAP > cameraPosition.y
				+ cameraHeight) {
			// move camera up
			cameraPosition.y = (pufflePosition.y - cameraHeight) + SCROLL_GAP;
		}

		// if camera is outside of the world
		if (cameraPosition.y < 0) {
			// snap to bottom of world
			cameraPosition.y = 0;
		} else if (cameraPosition.y + cameraHeight > world.getLevel()
				.getHeight()) {
			// snap to top of world
			cameraPosition.y = world.getLevel().getHeight() - cameraHeight;
		}

		// set camera position
		camera.position.set((cameraPosition.x + (cameraWidth / 2)) * ppu,
				(cameraPosition.y + (cameraHeight / 2)) * ppu, 0);

		// update camera and render tools
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	public void render() {
		updateCameraPosition();

		// draw world
		spriteBatch.begin();
		drawBackground();
		drawBlocks();
		drawPuffle();
		spriteBatch.end();

		// draw editor
		if (editor != null) {
			drawEditor();
			drawPlacedBlocks();
		}

		// draw inventory
		Inventory inventory = world.getInventory();
		if (!inventory.isEmpty()) {
			drawInventory(inventory);
		}
		drawSettings();
	}

	private void drawBackground() {
		spriteBatch.draw(backgroundTexture, cameraPosition.x * ppu,
				cameraPosition.y * ppu, backgroundWidth * ppu, cameraHeight
						* ppu);
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

	private void drawInventory(Inventory inventory) {
		// store size of inventory
		float invWidth = inventory.getBounds().width * ppu;
		float invHeight = inventory.getBounds().height * ppu;

		// store position of inventory
		float invXPosition = (inventory.getBounds().x + cameraPosition.x) * ppu;
		float invYPosition = ((cameraHeight - inventory.getBounds().y + cameraPosition.y) * ppu)
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

	private void drawSettings() {
		// this needs to be changed to prevent settings being stored in main ,
		// but for the time being i wanted to get
		// stuff working to a usefull level
		// sets bounds for setting button
		Settings settings = world.getSettings();
		float settingWidth = settings.getButtonBounds().width * ppu;
		float settingHeight = settings.getButtonBounds().height * ppu;
		float settingButtonX = (settings.getButtonBounds().x + cameraPosition.x)
				* ppu;
		float settingButtonY = ((cameraHeight - settings.getButtonBounds().y + cameraPosition.y) * ppu)
				- settingHeight;
		// useless this will just change the box type
		if (settings.getIfSettingsOn()) {
			shapeRenderer.begin(ShapeType.Filled);
		} else {
			shapeRenderer.begin(ShapeType.Line);
		}
		// draws the setting button
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.rect(settingButtonX, settingButtonY, settingWidth,
				settingHeight);
		shapeRenderer.end();
	}

	private void drawEditor() {
		// draw overlay
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setColor(OVERLAY_COLOR);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(cameraPosition.x * ppu, cameraPosition.y * ppu,
				cameraWidth * ppu, cameraHeight * ppu);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		float gridLeft = (float) Math.ceil(cameraPosition.x);
		float gridBottom = (float) Math.ceil(cameraPosition.y);

		// draw grid
		shapeRenderer.setColor(GRID_COLOR);
		shapeRenderer.begin(ShapeType.Line);
		for (int x = 0; x < cameraWidth; x++) {
			shapeRenderer.line((gridLeft + x) * ppu, cameraPosition.y * ppu,
					(gridLeft + x) * ppu, (cameraPosition.y + cameraHeight)
							* ppu);
		}
		for (int y = 0; y < cameraHeight; y++) {
			shapeRenderer.line(cameraPosition.x * ppu, (gridBottom + y) * ppu,
					(cameraPosition.x + cameraWidth) * ppu, (gridBottom + y)
							* ppu);
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

	// Getters ------------
	public float getPpu() {
		return ppu;
	}

	public Vector2 getCameraPosition() {
		return cameraPosition;
	}

	public float getCameraHeight() {
		return cameraHeight;
	}

	// --------------------
}
