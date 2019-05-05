package pl.pojava.kerbnot.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * 
 * @author Filip
 * Class which load and saves game preferences.
 *
 */
public class GamePreferences {
	
	private float masterVolume;
	private boolean music;
	
	private boolean fullscreen;
	
	private Preferences prefs;
	
	private static GamePreferences instance = new GamePreferences();
	
	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}
	
	public void load() {
		masterVolume = prefs.getFloat("masterVolume", 1);
		music = prefs.getBoolean("music", true);
		fullscreen = prefs.getBoolean("fullscreen", false);
	}
	
	public void save() {
		prefs.putFloat("masterVolume", masterVolume);
		prefs.putBoolean("music", music);
		prefs.putBoolean("fullscreen", fullscreen);
	}
	
	public float getMasterVolume() {
		return masterVolume;
	}
	
	public void setMasterVolume(float masterVolume) {
		this.masterVolume = masterVolume;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	public boolean isMusicEnabled() {
		return music;
	}
	
	public void setMusic (boolean music) {
		this.music = music;
	}
	
	public static GamePreferences getInstance() {
		return instance;
	}

}
