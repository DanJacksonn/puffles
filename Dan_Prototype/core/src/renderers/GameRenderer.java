package renderers;

import resources.Bounds;
import resources.GameState;
import helpers.EditorAssetLoader;
import helpers.WorldAssetLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import entities.api.IBlock;
import entities.api.IPuffle;
import entities.impl.Block;
import entities.impl.Editor;
import entities.impl.Inventory;
import entities.impl.Puffle;
import entities.impl.World;

public class GameRenderer {

	/**
	 * This class renders the game world to the screen. Each block in the game
	 * has the size 1 unit: all other images are scaled to this.
	 */

	public static final float CAMERA_HEIGHT = 11f; // units
	public static final float SCROLL_GAP = 5f;

	private World world;
	private Editor editor;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch spriteBatch;

	// world assets
	private Texture backgroundTexture;
	private float backgroundWidth;
	private TextureRegion[] blockTextures;
	private TextureRegion[] damageTextures;
	private TextureRegion puffleTexture;
	BitmapFont font;
	private Bounds invBounds;
	private Vector2 invTextPos;

	private float ppu; // pixels per unit
	private Vector2 cameraPosition; // bottom left of camera
	private float cameraWidth; // units
	private float cameraHeight; // units

	public GameRenderer(World world, Editor editor) {
		this.world = world;
		this.editor = editor;

		// load render tools
		this.camera = new OrthographicCamera();
		this.cameraHeight = CAMERA_HEIGHT;
		this.cameraPosition = new Vector2(world.puffle.getPosition().x,
				world.puffle.getPosition().y);
		this.shapeRenderer = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();

		loadAssets();
	}

	private void loadAssets() {
		this.backgroundTexture = WorldAssetLoader.backgroundTexture;
		this.backgroundWidth = (cameraHeight * backgroundTexture.getWidth())
				/ backgroundTexture.getHeight();
		this.blockTextures = WorldAssetLoader.blockTextures;
		this.damageTextures = WorldAssetLoader.damageTextures;
		this.puffleTexture = WorldAssetLoader.puffleTexture;
		this.font = WorldAssetLoader.font;
		
		this.invBounds = WorldAssetLoader.invBounds;
		this.invTextPos = WorldAssetLoader.invTextPos;
	}
	
	public void render(GameState state) {
		updateCameraPosition();

		// clear and set background
		Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw world
		spriteBatch.begin();
		drawBackground();
		drawBlocks();
		drawPuffle();
		spriteBatch.end();

		// draw editor
		if (state == GameState.EDITING) {
			drawEditor();
			drawPlacedBlocks();
		} else {
			if (!world.inventory.isEmpty()) {
				drawInventory(world.inventory);
			}
		}
		
		//drawSettings();
	}

	public void updateCameraPosition() {
		Vector2 pufflePosition = world.puffle.getPosition();

		// keep puffle in middle of camera on x axis
		cameraPosition.x = pufflePosition.x - (cameraWidth / 2);

		// if camera is outside of the world
		if (cameraPosition.x < 0) {
			// snap to left of world
			cameraPosition.x = 0;
		} else if (cameraPosition.x + cameraWidth > world.level.getWidth()) {
			// snap to right of world
			cameraPosition.x = world.level.getWidth() - cameraWidth;
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
		} else if (cameraPosition.y + cameraHeight > world.level.getHeight()) {
			// snap to top of world
			cameraPosition.y = world.level.getHeight() - cameraHeight;
		}

		// set camera position
		camera.position.set((cameraPosition.x + (cameraWidth / 2)) * ppu,
				(cameraPosition.y + (cameraHeight / 2)) * ppu, 0);

		// update camera and render tools
		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
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
			IBlock.Type blockId = block.getBlockType();
			switch (blockId) {
			case STONE:
			case GRASS:
				spriteBatch.draw(blockTextures[blockId.ordinal()],
						block.getTilePosition().x * ppu,
						block.getTilePosition().y * ppu, blockSize, blockSize);
				break;
			default:
				break;
			}
			// draw damage to screen
			int damageValue = block.getDamageValue();
			if (damageValue > 0) {
				spriteBatch.draw(damageTextures[damageValue - 1],
						block.getTilePosition().x * ppu, block.getTilePosition().y
								* ppu, blockSize, blockSize);
			}
		}
	}

	private void drawPuffle() {
		Puffle puffle = world.puffle;
		float puffleSize = (IPuffle.RADIUS * 2) * ppu;
		// draw puffle to screen with rotation
		spriteBatch.draw(puffleTexture,
				(puffle.getPosition().x - IPuffle.RADIUS) * ppu,
				(puffle.getPosition().y - IPuffle.RADIUS) * ppu, puffleSize / 2,
				puffleSize / 2, puffleSize, puffleSize, 1f, 1f,
				puffle.getRotation(), true);
	}

	private void drawInventory(Inventory inventory) {
		// store size of inventory
		float invXSize = invBounds.getWidth() * ppu;
		float invYSize = invBounds.getHeight() * ppu;

		// store position of inventory
		float invXPosition = (invBounds.left + cameraPosition.x) * ppu;
		float invYPosition = ((cameraHeight - invBounds.bottom + cameraPosition.y) * ppu)
				- invYSize;

		// store number of blocks in inventory as string
		String invText = Integer.toString(inventory.getNoOfBlocks());

		// get height of text when written in font
		float textHeight = font.getBounds(invText).height;

		// store position of text in relation to inventory
		float textXPosition = invXPosition + (invTextPos.x * ppu);
		float textYPosition = invYPosition + textHeight + (invTextPos.y * ppu);

		// draw
		spriteBatch.begin();
		// inventory
		spriteBatch.draw(blockTextures[1], invXPosition, invYPosition, invXSize,
				invYSize);
		
		// text
		font.draw(spriteBatch, invText, textXPosition, textYPosition);
		spriteBatch.end();
	}

	private void drawSettings() {
		Bounds settingsBounds = WorldAssetLoader.settingsBounds;
		
		float settingsWidth = settingsBounds.getWidth() * ppu;
		float settingsHeight = settingsBounds.getHeight() * ppu;
		float settingsButtonX = (settingsBounds.left + cameraPosition.x)
				* ppu;
		float settingsButtonY = ((cameraHeight - settingsBounds.bottom + cameraPosition.y) * ppu)
				- settingsHeight;

		shapeRenderer.begin(ShapeType.Filled);
		// draws the setting button
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.rect(settingsButtonX, settingsButtonY, settingsWidth,
				settingsHeight);
		shapeRenderer.end();
	}

	private void drawEditor() {
		// draw overlay
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setColor(EditorAssetLoader.overlayColour);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(cameraPosition.x * ppu, cameraPosition.y * ppu,
				cameraWidth * ppu, cameraHeight * ppu);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		float gridLeft = (float) Math.ceil(cameraPosition.x);
		float gridBottom = (float) Math.ceil(cameraPosition.y);

		// draw grid
		shapeRenderer.setColor(EditorAssetLoader.gridColour);
		shapeRenderer.begin(ShapeType.Line);
		// vertical lines
		for (int x = 0; x < cameraWidth; x++) {
			shapeRenderer.line((gridLeft + x) * ppu, cameraPosition.y * ppu,
					(gridLeft + x) * ppu, (cameraPosition.y + cameraHeight)
							* ppu);
		}
		// horizontal lines
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
			spriteBatch.draw(blockTextures[block.getBlockType().ordinal()],
					block.getTilePosition().x * ppu, block.getTilePosition().y * ppu,
					blockSize, blockSize);
		}
		spriteBatch.end();
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
