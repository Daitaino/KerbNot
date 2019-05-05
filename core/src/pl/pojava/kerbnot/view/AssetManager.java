package pl.pojava.kerbnot.view;

/**
 * @author Małgorzata
 * Class with all assets
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {

		public static final Texture GAME_OVER = new Texture(Gdx.files.internal("PNG/gameOver.png"));
	 	public static final Texture PLAYER_TEXTURE = new Texture(Gdx.files.internal("PNG/rocket.png"));
	    public static final Texture MAP_TEXTURE = new Texture(Gdx.files.internal("Backgrounds/bq.png"), true);
	    public static final Texture SPLASH = new Texture(Gdx.files.internal("PNG/RocketFoolsSplash.png"));
	    public static final Texture GHOST = new Texture(Gdx.files.internal("PNG/Yellow_icon.png"), true);
	    public static final Texture WARNING = new Texture(Gdx.files.internal("PNG/redWarning.png"), true);
	    public static final Texture OVERLAY = new Texture(Gdx.files.internal("PNG/overlay.png"), true);
	    public static final Texture SAS_ON = new Texture(Gdx.files.internal("PNG/SASgreen.png"), true);
	    public static final Texture SAS_OFF = new Texture(Gdx.files.internal("PNG/SASred.png"), true);
	    public static final Texture MINIMAP_PLANET = new Texture(Gdx.files.internal("PNG/red_dot.png")); //ToDo: increase quality
	    public static final Texture MAPBORDER_DOT = new Texture(Gdx.files.internal("PNG/mapBorderDot.png")); //ToDo: increase quality
	    public static final Texture MINIMAP_PLAYER = new Texture(Gdx.files.internal("PNG/green_dot.png"));
	    public static final Texture PROGFILLER = new Texture(Gdx.files.internal("PNG/progFiller.png"), true);
	    public static final Texture PLANET1 = new Texture(Gdx.files.internal("PNG/Planets/1.png"), true);
	    public static final Texture PLANET2 = new Texture(Gdx.files.internal("PNG/Planets/2.png"), true);
	    public static final Texture PLANET3 = new Texture(Gdx.files.internal("PNG/Planets/3.png"), true);
	    public static final Texture PLANET4 = new Texture(Gdx.files.internal("PNG/Planets/4.png"), true);
	    public static final Texture PLANET5 = new Texture(Gdx.files.internal("PNG/Planets/5.png"), true);
	    public static final Texture PLANET6 = new Texture(Gdx.files.internal("PNG/Planets/6.png"), true);
	    public static final Texture PLANET7 = new Texture(Gdx.files.internal("PNG/Planets/7.png"), true);
	    public static final Texture MARS = new Texture(Gdx.files.internal("PNG/Planets/mars.png"), true); // CASE 10
	    public static final Texture EARTH = new Texture(Gdx.files.internal("PNG/Planets/Earth.png"), true); //CASE 8
	    public static final Texture MOON = new Texture(Gdx.files.internal("PNG/Planets/Moon.png"), true); // CASE 9
	    public static final Texture POPUP_HEAD = new Texture(Gdx.files.internal("PNG/title.png"));
	    public static final Texture POPUP_BODY = new Texture(Gdx.files.internal("PNG/popup.png"));
	    public static final Texture MAP_BORDER = new Texture(Gdx.files.internal("PNG/mapBorder.png"));
	    public static final Texture LEVEL_FINISHED = new Texture(Gdx.files.internal("Backgrounds/levelFinish.png"));
	    public static final Texture CROSS_HERE = new Texture(Gdx.files.internal("PNG/crossHere.png"));
	    public static final Sound THRUSTER_STARTER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterStarter.wav"));
	    public static final Sound THRUSTER_GOINGER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterGoinger.wav"));
	    public static final Sound THRUSTER_ENDER = Gdx.audio.newSound(Gdx.files.internal("SFX/ThrusterEnder.wav"));
	    public static final Sound POPUP_OPENER = Gdx.audio.newSound(Gdx.files.internal("SFX/popupOpening.wav"));
	    public static final Music POPUP_SHUTTER_1 = Gdx.audio.newMusic(Gdx.files.internal("SFX/Transmission_06.wav"));
	    public static final Music BQ_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("SFX/Space_Atmospheric_01.wav"));
	    public static final Music WARNING_SOUND= Gdx.audio.newMusic(Gdx.files.internal("SFX/Emergency_Warning_05.wav"));
	    public static final Sound EXPLOSION = Gdx.audio.newSound(Gdx.files.internal("SFX/Explosion_01.wav"));
	    public static final Sound DEATH_SIGN = Gdx.audio.newSound(Gdx.files.internal("SFX/Medical_EKG_Flat_Line_01.wav"));
}
