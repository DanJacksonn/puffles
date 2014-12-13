package com.mygdx.game;

import handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener {
	SpriteBatch batch;
	Texture img;
	public static final String TITLE  = "Prototype mark2";
	public static final int V_Height  = 240;
	public static final int V_Width  = 320;
	public static final int Scale  = 2;
	public static final float STEP  = 1 /60f;
	private float accum;
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	public SpriteBatch getSpriteBatch(){return sb;}
	public OrthographicCamera getCamera(){return cam;}
	public OrthographicCamera getHudCamera(){return hudCam;}
	private GameStateManager gsm;
	@Override
	public void create () {
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false,V_Width,V_Height);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false,V_Width,V_Height);
		gsm = new GameStateManager(this);
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while(accum >=STEP){
			accum -=STEP;
			gsm.update(STEP);
			gsm.render();
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
