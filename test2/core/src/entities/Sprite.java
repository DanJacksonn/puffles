package entities;

import handlers.Animation;
import handlers.B2DVars;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Sprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	private float count =360.0f;
	public Sprite(Body body) {
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt) {
		animation.update(dt);
		
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		//sb.draw(animation.getFrame(),body.getPosition().x * B2DVars.PPM *2- width / 2,body.getPosition().y * B2DVars.PPM *2- height / 2);
		if(count < 0.0f)
			count = 360.0f;
			else
			count --;
		sb.draw(animation.getFrame(),body.getPosition().x * B2DVars.PPM *2- width / 2,body.getPosition().y * B2DVars.PPM *2- height / 2,animation.getFrame().getRegionWidth()/2.0f,animation.getFrame().getRegionHeight()/2.0f, animation.getFrame().getRegionWidth(), animation.getFrame().getRegionHeight(), 1f, 1f,count, false);
		sb.end();
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
}