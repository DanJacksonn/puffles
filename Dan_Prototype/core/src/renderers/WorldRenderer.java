package renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType; 
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import entities.Block;
import entities.Editor;
import entities.Inventory;
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
	public static final int MAX_DAMAGE = 2;
	public static final FileHandle TILESET_LOCATION = Gdx.files.internal("images/textures/tileset.pack");	
	public static final FileHandle FONTSET_LOCATION = Gdx.files.internal("images/textures/FontTileset.pack");
	
	private World world;
	private Editor editor;
	// inventory
	private Inventory inventory;

	// allows the camera to only render part of the world
	private OrthographicCamera camera;
	
	private ShapeRenderer shapeRenderer;

	// textures
	private TextureRegion puffleTexture;
	private TextureRegion[] blockTextures;
	private TextureRegion[] damageTextures;
	private SpriteBatch spriteBatch;
	private TextureRegion[] fontTextures;
	
	private float ppu; // pixels per unit
	private float cameraWidth; // units
	private float cameraHeight; // units

	public WorldRenderer(World world, Editor editor) {
		this.world = world;
		this.editor = editor;
		this.inventory = world.getInventory();
		this.camera = new OrthographicCamera();
		this.shapeRenderer = new ShapeRenderer();
		this.spriteBatch = new SpriteBatch();
		this.cameraHeight = CAMERA_HEIGHT;
		
		// textures
		fontTextures = new TextureRegion[11];
		blockTextures = new TextureRegion[2];
		damageTextures = new TextureRegion[MAX_DAMAGE];
		loadTextures();
	}
	
	/** Loads textures from texture atlas */
	private void loadTextures() {
		TextureAtlas atlas = new TextureAtlas(TILESET_LOCATION);
		TextureAtlas fontAtlas = new TextureAtlas(FONTSET_LOCATION);
		puffleTexture = atlas.findRegion("player");
	
		blockTextures[0] = atlas.findRegion("stone");
		blockTextures[1] = atlas.findRegion("grass");
		// load crack textures
		for (int i = 0; i < MAX_DAMAGE; i++ ) {
			damageTextures[i] = atlas.findRegion("crack" + (i + 1));
		}
		//load Fonts
		fontTextures[0] = fontAtlas.findRegion("0");
		fontTextures[1] = fontAtlas.findRegion("1");
		fontTextures[2] = fontAtlas.findRegion("2");
		fontTextures[3] = fontAtlas.findRegion("3");
		fontTextures[4] = fontAtlas.findRegion("4");
		fontTextures[5] = fontAtlas.findRegion("5");
		fontTextures[6] = fontAtlas.findRegion("6");
		fontTextures[7] = fontAtlas.findRegion("6");
		fontTextures[8] = fontAtlas.findRegion("7");
		fontTextures[8] = fontAtlas.findRegion("8");
		fontTextures[9] = fontAtlas.findRegion("9");
		fontTextures[10] = fontAtlas.findRegion("+");
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
		
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}
	
	public void render() {
		spriteBatch.begin();
		drawBlocks();
		drawPuffle();                                                                                          
		spriteBatch.end();
		
		InventoryDisplay();
		if (editor != null) {
			drawEditor();
			spriteBatch.begin();
			drawPlacedBlocks();
			spriteBatch.end();
		}
		
		//debug();
	}

	private void drawBlocks() {
		for (Block block : world.getDrawableBlocks((int)cameraWidth, (int)CAMERA_HEIGHT)) {
			float blockSize = Block.SIZE * ppu;
			// draw block to screen
			spriteBatch.draw(blockTextures[block.getBlockID()], block.getPosition().x * ppu,
					block.getPosition().y * ppu, blockSize, blockSize);
			// draw damage to screen
			int damageValue = block.getDamageValue();
			if (damageValue > 0) {
				spriteBatch.draw(damageTextures[damageValue - 1], block.getPosition().x * ppu,
						block.getPosition().y * ppu, blockSize, blockSize);
			}
		}
		inventory.setSize(Block.SIZE * ppu/2f);
	}
	private void InventoryDisplay() {
		float size = inventory.getBounds().width;
		// getting inventory 
		
			spriteBatch.begin();
			//update inventory location
			Vector2 vector = new Vector2();
			vector.set((cameraWidth/10f)*ppu,((cameraHeight/10)*9.5f)*ppu);
			inventory.setPosition(vector);
			
			// drawing inventory
			spriteBatch.draw(blockTextures[1],vector.x,vector.y,size,size );
			spriteBatch.draw(fontTextures[Inventory.getInventory()],vector.x,vector.y,size,size );
			spriteBatch.end();
		
	}
	
	private void drawPuffle() {
		Puffle puffle = world.getPuffle();
		float puffleSize = (Puffle.RADIUS * 2) * ppu;
		// draw puffle to screen with rotation
		spriteBatch.draw(puffleTexture, (puffle.getPosition().x - Puffle.RADIUS) * ppu,
				(puffle.getPosition().y - Puffle.RADIUS) * ppu, puffleSize / 2, puffleSize / 2,
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
	
	/** Draws bounds of entities in the game */
	private void debug() {
		// draw puffle bounds
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeType.Line);
		Circle bounds = world.getPuffle().getBounds();
		shapeRenderer.circle(bounds.x * ppu, bounds.y * ppu, bounds.radius * ppu);
		shapeRenderer.end();
		
		// draw block bounds
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.begin(ShapeType.Line);
		for (Block block : world.getDrawableBlocks((int)cameraWidth, (int)CAMERA_HEIGHT)) {
			float blockSize = Block.SIZE * ppu;
			shapeRenderer.rect(block.getPosition().x * ppu, block.getPosition().y * ppu, blockSize, blockSize);
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
