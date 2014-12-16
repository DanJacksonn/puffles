package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
	public static boolean playerOnGround;

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture firstFixture = contact.getFixtureA();
		Fixture secondFixture = contact.getFixtureB();
		System.out.println(firstFixture.getUserData()+ "+" + secondFixture.getUserData());
		if(firstFixture.getUserData() != null && firstFixture.getUserData().equals("Ground")){
			if(secondFixture.getUserData().equals("Foot")){
				playerOnGround = true;
			}
		}
		if(secondFixture.getUserData() != null && firstFixture.getUserData().equals("Ground")){
			if(firstFixture.getUserData().equals("Foot")){
				playerOnGround = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture firstFixture = contact.getFixtureA();
		Fixture secondFixture = contact.getFixtureB();
		if(firstFixture.getUserData() != null && firstFixture.getUserData().equals("Ground")){
			if(secondFixture.getUserData().equals("Foot")){
				playerOnGround = false;
			}
		}
		if(secondFixture.getUserData() != null && firstFixture.getUserData().equals("Ground")){
			if(firstFixture.getUserData().equals("Foot")){
				playerOnGround = false;
			}
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	public static boolean IfGround(){
		return playerOnGround;
	}

}
