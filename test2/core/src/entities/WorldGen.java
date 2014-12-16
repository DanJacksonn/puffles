package entities;

import static handlers.B2DVars.PPM;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
public class WorldGen {
	public BodyDef bdef = new BodyDef();
	public FixtureDef fdef = new FixtureDef();
	Body body;
	int xPos = 120;
	int yPos = 200;
	public WorldGen(World world) {
		bdef.position.set(160 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("Ground");
		//platform one 
		bdef.position.set(210 / PPM, 170 / PPM);
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);
		shape = new PolygonShape();
		shape.setAsBox(5 / PPM, 50 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("Ground");
	}

}
