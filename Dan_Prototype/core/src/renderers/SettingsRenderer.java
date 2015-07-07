package renderers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import entities.impl.World;

public class SettingsRenderer {
	private ShapeRenderer shapeRenderer;
	
	public SettingsRenderer(World world, Object object) {
		// TODO Auto-generated constructor stub
	}

	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		this.shapeRenderer = new ShapeRenderer();
	}
	public void render(){
		// this will render the settings
		
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(1, 1, 0, 1);
		 shapeRenderer.rect(0,0,20,20);
		 shapeRenderer.end();
	}		


}
