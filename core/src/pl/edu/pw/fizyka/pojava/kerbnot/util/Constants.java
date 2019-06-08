package pl.edu.pw.fizyka.pojava.kerbnot.util;


/**
 * 
 * @author Filip
 * List of global constants.
 *
 */
public final class Constants {
	
	// Pixels per meter
	public static final float PPM = 16;
	
	// Conversion aliases
	public static final float toPixel = PPM;
	public static final float toMeter = 1f / PPM;
	
	// DEBUG mode
	public static boolean DEBUG = false;
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	public static final float FRAME_RATE = 1 / 60f;
	
	// Preferences file
	public static final String PREFERENCES = "game.prefs";
	
	// Box2D constants
	public static final int VELOCITY_ITERATIONS = 8;
	public static final int POSITION_ITERATIONS = 3;
	
}
