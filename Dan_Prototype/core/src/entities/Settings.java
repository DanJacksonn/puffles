package entities;

import com.badlogic.gdx.math.Rectangle;

public class Settings {
	//display infomation
	public static final float BUTTON_SIZE = 0.6f;
	public static final float BUTTON_X_POSITION = 13.8f;
	public static final float BUTTON_Y_POSITION = 0.1f;
	private Rectangle bounds;
	// used to check if settings are on
	private boolean settingsOn = false;
	public Settings(){
		//WARNING
		// this class will be holding all the infomation used in storing and the creatation of the settings 
		// at the minuite this should not be coming from the main class but to get things moving is doing
		// to avoid this we may use a config file 
		//WARNING
		this.bounds = new Rectangle(BUTTON_X_POSITION, BUTTON_Y_POSITION , BUTTON_SIZE, BUTTON_SIZE);
		
	}
	public Rectangle getButtonBounds(){
		return bounds;
	}
	// these two are used in the displaying of the button , these will not be needed when textures are available 
	public boolean getIfSettingsOn(){
		// used to return if the settings should being renderd
		return settingsOn;
		
	}
	public void setIfSettingsOn(boolean ifOn){
		// sets if the settings display should be being renderd
		this.settingsOn = ifOn;
	}

}
