package entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game;

import static handlers.B2DVars.PPM;
public class Player extends Sprite {
	
	private int numCrystals;
	private int totalCrystals;
	public Player(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("puffle");
		TextureRegion[] sprites = TextureRegion.split(tex, 33, 33)[0];
		
		setAnimation(sprites, 1 / 12f);
		
	}
	public void collectCrystal() { numCrystals++; }

	public int getNumCrystals() { return numCrystals; }
	public void setTotalCrystals(int i) { totalCrystals = i; }
	public int getTotalCyrstals() { return totalCrystals; }
	
}










