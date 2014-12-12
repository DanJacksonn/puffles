package states;

import static handlers.B2DVars.PPM;
import handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game;

public class Play extends GameState{
	private World world;
	private Box2DDebugRenderer b2dr;
	private BitmapFont font  = new BitmapFont();
	private OrthographicCamera b2dCam;
	float c;
	int boxX;
	public Play(GameStateManager gsm) {
	
		super(gsm);
		//make the world
		
		world  = new World(new Vector2(0,-9.81f),true);
		b2dr = new Box2DDebugRenderer();
		// making a platform
		BodyDef bdef  = new BodyDef();
		bdef.position.set(160/PPM,120/PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50/PPM,5/PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape= shape;
		body.createFixture(fdef);
		
		// falling box
		bdef.position.set(105/PPM,200/PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		shape.setAsBox(5/PPM,5/PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		// cam set 
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false,Game.V_Width/PPM,Game.V_Height/PPM);
	}

	public void handleInput() {
	}

	public void update(float dt) {	
		world.step(dt,6,2);
	}

	public void render() {
		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//draw

		b2dr.render(world,b2dCam.combined);
	}
	
	public void dispose() {
	}

}
