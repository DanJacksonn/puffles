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

public class Puffel {
	public BodyDef bdef = new BodyDef();
	public FixtureDef fdef = new FixtureDef();
	Body body;
	int xPos = 120;
	int yPos = 200;
	public Puffel(World world){
		PolygonShape shape = new PolygonShape();
		body = world.createBody(bdef);
		bdef.position.set(xPos/ PPM, yPos / PPM);
		bdef.type = BodyType.DynamicBody;
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(8/PPM);
		fdef.shape = circleShape;
		fdef.density = 1;
		fdef.restitution = .1f;
		body = world.createBody(bdef);
		body.createFixture(fdef).setUserData("Player");
		
		// done
		shape.setAsBox(2 / PPM, 2 / PPM,new Vector2(0,-5/PPM),0);
		fdef.shape = shape;
		fdef.density = 0;
		fdef.isSensor = true;
		body.setFixedRotation(true);
		body.createFixture(fdef).setUserData("Foot");
	}
	public BodyDef getBodyDef(){ return bdef; }
	public Body getBody(){ return body; }
	public FixtureDef getFixture(){ return fdef;}
}
