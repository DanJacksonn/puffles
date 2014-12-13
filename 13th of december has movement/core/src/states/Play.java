package states;

import static handlers.B2DVars.PPM;
import handlers.GameStateManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class Play extends GameState {
	private World world;
	private Box2DDebugRenderer b2dr;
	private BitmapFont font = new BitmapFont();
	private OrthographicCamera b2dCam;
	BodyDef bdef = new BodyDef();
	FixtureDef fdef = new FixtureDef();
	int x = 130;
	int y = 200;
	Body body;
	private Vector2 movement = new Vector2();
	final static float SPEED = (20 / PPM);

	public Play(GameStateManager gsm) {

		super(gsm);
		// make the world

		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
		// making a platform
		bdef.position.set(160 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body body2 = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		fdef.shape = shape;
		body2.createFixture(fdef);
		bdef.position.set(210 / PPM, 170 / PPM);
		bdef.type = BodyType.StaticBody;
		body2 = world.createBody(bdef);
		shape = new PolygonShape();
		shape.setAsBox(5 / PPM, 50 / PPM);
		fdef.shape = shape;
		body2.createFixture(fdef);
		//adding side
		// falling circle
		bdef.position.set(x / PPM, y / PPM);
		bdef.type = BodyType.DynamicBody;
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(5/PPM);
		//PolygonShape boxShape = new PolygonShape();
		//boxShape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = circleShape;
		// create fixture;
		fdef.density = 5;
		fdef.restitution = .1f;
		// done
		body = world.createBody(bdef);
		body.createFixture(fdef);

		// cam set
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_Width / PPM, Game.V_Height / PPM);
	}

	public void handleInput() {
		movement.x = 0;
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
			break;
		}

	}

	public void update(float dt) {
		world.step(dt, 6, 2);

	}

	public void render() {
		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// yay
		// body.applyAngularImpulse(-1/PPM,true);
		handleInput();
		body.applyForceToCenter(movement, true);
		// body.applyForceToCenter(10/PPM,0, true);
		// handle input

		//
		// bdef.position.set(x/PPM,y/PPM);
		// bdef.type = BodyType.DynamicBody;
		// body = world.createBody(bdef);
		// CircleShape circleShape = new CircleShape();
		// circleShape.setRadius(5/PPM);
		// fdef.shape = circleShape;
		// body.createFixture(fdef);

		// draw

		b2dr.render(world, b2dCam.combined);
	}

	public void dispose() {
	}

}
