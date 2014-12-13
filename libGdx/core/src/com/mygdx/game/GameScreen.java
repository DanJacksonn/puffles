package com.mygdx.game;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	final MyGdxGame game;
	private OrthographicCamera camera;
	private Sprite ball;
	private Sprite platform;
	private int ballx = 200;
	private int bally = 200;
	float c = 0 ;
	private Rectangle ballRect;
	private Rectangle platformRect ;
	private World world;
	public GameScreen(MyGdxGame game){
		this.game = game;
		//world = new World(new Vector2(0,-9.81f),true);
		ball = new Sprite(new Texture(Gdx.files.internal("images/ball.png")));
		platform = new Sprite(new Texture(Gdx.files.internal("images/platform.png")));
		ballRect = new Rectangle(ball.getX(),ball.getY(),ball.getWidth(),ball.getHeight());
		platformRect = new Rectangle(platform.getX(),platform.getY(),platform.getWidth(),platform.getHeight());
		camera = new OrthographicCamera();
		
		camera.setToOrtho(false,800,480);
		//float delta = 0;
		//this.render(delta);
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render(float delta) {
		boolean overlap = false;
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0.2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(ball,ballx,bally,40,40);
		game.batch.draw(platform,0,0);
		game.batch.end();
		c = Gdx.input.getAccelerometerY();

		if(c < -3 || c > 3){
			if(!overlap){
				ballx += (c);
				ballRect.setPosition(ball.getX(),ball.getY());
			}
		//	double angle = c * 9;
		//	System.out.println("c    : " + c);
		//	System.out.println("angle: " + angle);
		//	double temp = 9.81*Math.sin(angle);
		//	System.out.println("temp : " + temp);
			
		}

		
		
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	
}
