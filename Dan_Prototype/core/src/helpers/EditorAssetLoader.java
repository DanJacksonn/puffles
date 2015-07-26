package helpers;

import resources.Bounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EditorAssetLoader {

	// colours
	public static Color overlayColour;
	public static Color gridColour;
	
	// button locations
	public static Bounds invBounds;
	public static Vector2 invTextPos;
	public static Bounds doneButtonBounds;
	
	public static void Load() {
		overlayColour = new Color(0.5f, 0.5f, 0.7f, 0.4f);
		gridColour = new Color(0.35f, 0.35f, 0.5f, 0);
		
		// gui positions
		invBounds = new Bounds(0.6f, 0.1f, 0.6f);
		invTextPos = new Vector2(0.08f, 0.08f);
		doneButtonBounds = new Bounds(13.8f, 0.1f, 0.6f);
	}
}
