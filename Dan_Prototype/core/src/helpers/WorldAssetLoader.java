package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import resources.Bounds;

public class WorldAssetLoader {

	// textures
	public static TextureAtlas atlas;
	public static Texture backgroundTexture;
	public static TextureRegion puffleTexture;
	public static TextureRegion[] blockTextures, damageTextures;
	
	// sounds
	
	// font
	public static BitmapFont font;
	
	// hud locations
	public static Bounds invBounds;
	public static Vector2 invTextPos;
	public static Bounds settingsBounds;

	public static void Load() {
		atlas = new TextureAtlas(
				Gdx.files.internal("images/textures/tileset.pack"));
		blockTextures = new TextureRegion[5];
		damageTextures = new TextureRegion[2];

		// load background
		backgroundTexture = new Texture(
				Gdx.files.internal("images/background.png"));

		// load puffle
		puffleTexture = atlas.findRegion("player");

		// load blocks
		blockTextures[0] = atlas.findRegion("stone");
		blockTextures[1] = atlas.findRegion("grass");
        blockTextures[4] = atlas.findRegion("ice");

		// load cracks
		for (int i = 0; i < damageTextures.length; i++) {
			damageTextures[i] = atlas.findRegion("crack" + (i + 1));
		}

		// load font
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"),
				Gdx.files.internal("data/font.png"), false);
		font.setScale(0.5f);
		font.setColor(Color.WHITE);
		
		// gui positions
		invBounds = new Bounds(0.6f, 0.1f, 0.6f);
		invTextPos = new Vector2(0.08f, 0.08f);
		settingsBounds = new Bounds(13.8f, 0.1f, 0.6f);
	}

	public static void dispose() {
		atlas.dispose();
		font.dispose();
	}

}
