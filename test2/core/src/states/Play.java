package states;

import static handlers.B2DVars.PPM;
import handlers.GameStateManager;
import handlers.MyContactListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game;

import entities.Player;
import entities.Puffel;
import entities.WorldGen;

public class Play extends GameState {
	private World world;
	private Box2DDebugRenderer b2dr;
	private BitmapFont font = new BitmapFont();
	private OrthographicCamera b2dCam;
	BodyDef bdef = new BodyDef();
	FixtureDef fdef = new FixtureDef();
	int x = 130;
	int y = 200;
	Player player;
	private Vector2 movement = new Vector2();
	final static float SPEED = (20 / PPM);
	final static float UPWARDSSPEED = (200 / PPM);
	Texture backgroundTexture;
	public Play(GameStateManager gsm) {

		super(gsm);
		// make the world
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(new MyContactListener());
		b2dr = new Box2DDebugRenderer();
		// creates body used for rendering stuff
		Body body;
		// making a platforms
		WorldGen worldCreate = new WorldGen(world);
		//making player
		Puffel puffel = new Puffel(world);
		body = puffel.getBody();
		player = new Player(body);
		// cam set
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_Width / PPM, Game.V_Height / PPM);
		
		
	}

	public void handleInput() {
		movement.y = 0;
		movement.x = 0;
		boolean onGround;
		switch (Gdx.app.getType()) {
		case Android:
			float c;
			c = Gdx.input.getAccelerometerY();

			if(c < -2 || c > 2){
					movement.x = c/30;
			}
			break;
		default:
			if (Gdx.input.isKeyPressed(Keys.A)) {
				movement.x -= SPEED;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				movement.x = SPEED;
			}
			if(Gdx.input.isKeyPressed(Keys.SPACE)){
				onGround = MyContactListener.IfGround();
				System.out.println(onGround);
				if(onGround){
					movement.y = UPWARDSSPEED;
				}
				
			}
			break;
		}

	}

	public void update(float dt) {
		// on tick
		world.step(dt, 6, 2);
		player.update(dt);	
	}

	public void render() {
		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//gets player sprite 
		player.render(sb);
		// handles input
		handleInput();
		player.getBody().applyForceToCenter(movement, true);
		// draw
		b2dr.render(world, b2dCam.combined);
	}

	public void dispose() {
	}

}
