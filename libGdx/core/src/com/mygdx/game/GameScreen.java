package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameScreen implements Screen {
	final MyGdxGame game;
	private OrthographicCamera camera;
	private Texture ball;
	private Texture platform;
	private int x = 100;
	private int y = 100;
	float c = 0 ;
	public GameScreen(MyGdxGame game){
		this.game = game;
		ball = new Texture(Gdx.files.internal("images/ball.png"));
		
		platform = new Texture(Gdx.files.internal("images/platform.png"));
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

		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0.2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(ball,x,y,40,40);
		game.batch.end();
		c = Gdx.input.getAccelerometerY();

		if(c < -3 || c > 3){
		//	double angle = c * 9;
		//	System.out.println("c    : " + c);
		//	System.out.println("angle: " + angle);
		//	double temp = 9.81*Math.sin(angle);
		//	System.out.println("temp : " + temp);
			x += (c);
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
		ball.dispose();
		platform.dispose();
	}

	
}
