package pl.pojava.kerbnot.view;

/**
 * @author Ma³gorzata
 * Class that draws objects (map, stars, planets etc.)
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import pl.pojava.kerbnot.model.Level;
import pl.pojava.kerbnot.model.Planet;
import pl.pojava.kerbnot.model.TrajectorySimulator;
import pl.pojava.kerbnot.util.GamePreferences;

import static pl.pojava.kerbnot.util.Constants.*;

public class WorldRenderer implements Disposable{
	
	public static final float MIN_ZOOM = 1f;
	public static final float MAX_ZOOM = 500f;
	public static final int MIN_ALPHA = 255;
	public static final int MAX_ALPHA = 10;
	public static final float STAR_FREQ = 3f;
	
	private Level level;
	private TextureAtlas textureMeteor;
	private TextureAtlas textureStar;
	private Animation animationStar;
	private Animation animationMeteor;
	private float elapsedTime = 0f;
	private TrajectorySimulator trajectorySimulator;
	private OrthographicCamera camera;
	private Sound thrusterGoinger;
	private Music warningSound;
	private Music backMusic;
	private boolean isGoingerPlaying;
	private boolean isThrustStopperActive;
	private boolean isBackMusicPlaying;
	
	public WorldRenderer (Level level, OrthographicCamera camera) {
		this.level = level;
		this.camera = camera;
		
		textureMeteor = new TextureAtlas(Gdx.files.internal("Backgrounds/meteorSheets/meteors.atlas"));
		animationMeteor = new Animation(1f/80f, textureMeteor.getRegions());
		
		textureStar = new TextureAtlas(Gdx.files.internal("Backgrounds/starSheets/stars.atlas"));
		for (Texture texture : textureStar.getTextures()) {
			texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		}
		animationStar = new Animation(1f/80f, textureStar.getRegions());
		
		trajectorySimulator = new TrajectorySimulator(level);
		
		thrusterGoinger = AssetManager.THRUSTER_GOINGER;
		warningSound = AssetManager.WARNING_SOUND;
		backMusic = AssetManager.BQ_MUSIC;
		isGoingerPlaying = false;
		isThrustStopperActive = false;
		isBackMusicPlaying = false;
		
		registerCollision();
	}
	
	private void registerCollision() {
		level.getWorld().setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA().getUserData() == Level.ObjectType.PLAYABLE || (contact.getFixtureB().getUserData() == Level.ObjectType.PLAYABLE)) {
					onCollision();
				}
			}

			@Override
			public void endContact(Contact arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
		private void onCollision() {
			stopWarningSound();
			stopBackgroundMusic();
			stopThrusterGoinger();
			
			AssetManager.EXPLOSION.play(GamePreferences.getInstance().getMasterVolume() / 5f);
			
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					
				}
			}, 3.0f);
			AssetManager.DEATH_SIGN.play(GamePreferences.getInstance().getMasterVolume());
			
			level.setState(Level.State.HEALTH_LOST);
			
			/*if (level.getState() == Level.State.GAME_OVER) {
				
			}*/
			registerCollision();
		}
		
		public void draw(SpriteBatch batch) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			drawMap(batch);
			drawStars(batch);
			drawPlanets(batch);
			drawPlayers(batch);
			drawTrajectory(batch);
			drawWarningSign(batch);
			drawMapBorder(batch);
			
			if(level.getPlayable().getCurrentThrust() > 0) {
				if (!isGoingerPlaying) {
					playThrusterGoinger();
					isGoingerPlaying = true;
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							isGoingerPlaying = false;
						}
					}, 4.0f);
				}
			}
			
			playBackgroundMusic();
			if(trajectorySimulator.isCollided()) {
				playWarningSound();
			} else {
				stopWarningSound();
			}
		}
		
		private void drawMap(SpriteBatch batch) {
			Texture mapTexture = AssetManager.MAP_TEXTURE;
			mapTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
			mapTexture.setFilter(Texture.TextureFilter.MipMapNearestLinear, Texture.TextureFilter.MipMapNearestLinear);
			
			
			float minX = -camera.viewportWidth * 660 / 2f;
			float minY = -camera.viewportHeight * 660 / 2f;
			float maxX = level.getMap().getWidth() - minX;
			float maxY = level.getMap().getHeight() - minY;
			
			batch.draw(
					mapTexture,
					minX,
					minY,
					0,
					0,
					(int) (maxX - minX),
					(int) (maxY - minY)
			);
			 
			batch.setColor(1, 1, 1, 1);
		}
		
		private void drawStars (SpriteBatch batch) {
			Texture starTexture =  animationStar.getKeyFrame(elapsedTime, true).getTexture();
			starTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			
			int alpha = (int) ((MIN_ALPHA - MAX_ALPHA) * (Math.min(10f, camera.zoom) - MIN_ZOOM) / (10f - MIN_ZOOM)) + MAX_ALPHA;
			
			batch.setColor(1, 1, 1, alpha);

	        batch.draw(
	                starTexture,
	                0,
	                0,
	                0,
	                0,
	                level.getMap().getWidth(),
	                level.getMap().getHeight()
	        );


	        batch.setColor(1, 1, 1, 1);
		}
		
		
		
		private void drawPlanets(SpriteBatch batch) {
			Texture planetTexture = AssetManager.PLANET1;
			
			for (Planet planet : level.getPlanets()) {
				switch (planet.getPlanetType()) {
					case 1:
						planetTexture = AssetManager.PLANET1;
	                    break;
	                case 2:
	                	planetTexture = AssetManager.PLANET2;
	                    break;
	                case 3:
	                	planetTexture = AssetManager.PLANET3;
	                    break;
	                case 4:
	                	planetTexture = AssetManager.PLANET4;
	                    break;
	                case 5:
	                	planetTexture = AssetManager.PLANET5;
	                    break;
	                case 6:
	                	planetTexture = AssetManager.PLANET6;
	                    break;
	                case 7:
	                	planetTexture = AssetManager.PLANET7;
	                    break;
	                case 8:
	                	planetTexture = AssetManager.EARTH;
	                    break;
	                case 9:
	                	planetTexture = AssetManager.MOON;
	                    break;
	                case 10:
	                	planetTexture = AssetManager.MARS;
				}
				
				planetTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				
				batch.draw(
						planetTexture,
						planet.getBody().getPosition().x * (toPixel) - (planet.getRadius() * toPixel),
	                    planet.getBody().getPosition().y * (toPixel) - (planet.getRadius() * toPixel),
	                    planet.getRadius() * toPixel * 2,
	                    planet.getRadius() * toPixel * 2
				);
			}
		}
		
		private void drawPlayers(SpriteBatch batch) {
			Texture playerTexture = AssetManager.PLAYER_TEXTURE;
			
			playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			Body playerBody = level.getPlayable().getBody();
			
			batch.draw(
				playerTexture,
				playerBody.getPosition().x * toPixel - playerTexture.getWidth() / 2f,
				playerBody.getPosition().y * toPixel - playerTexture.getHeight() / 2f,
				playerTexture.getWidth() / 2f,
				playerTexture.getHeight() / 2f,
				playerTexture.getWidth(),
				playerTexture.getHeight(),
				1, //scaleX
				1, //scaleY
				playerBody.getAngle() * MathUtils.radiansToDegrees, //rotation
				0,
				0,
				playerTexture.getWidth(),
				playerTexture.getHeight(),
				false, //whether to flip the sprite horizontally
				false //whether to flip the sprite vertically
			);
		}
		
		private void drawTrajectory (SpriteBatch batch) {
			if (TrajectorySimulator.enabled) {
				trajectorySimulator.update(Gdx.graphics.getDeltaTime());
				
				Texture trajectoryTexture = AssetManager.GHOST;
				trajectoryTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
				for (Vector2 pos : trajectorySimulator.getEstimationPath()) {
					batch.draw(
							trajectoryTexture,
							pos.x * toPixel,
							pos.y * toPixel
					);
				}
			}
		}
		
		private void drawWarningSign(SpriteBatch batch) {
			if(trajectorySimulator.isCollided()) {
				float randMultiplier = MathUtils.random(0.7f, 1.0f);
				float myWidth = AssetManager.WARNING.getWidth() * randMultiplier;
				float myHeight = AssetManager.WARNING.getHeight() * randMultiplier;
				
				batch.draw(
						AssetManager.WARNING,
						trajectorySimulator.getCollisionPoint().x * toPixel - myWidth / 2f,
	                    trajectorySimulator.getCollisionPoint().y * toPixel - myHeight / 2f,
	                    myWidth,
	                    myHeight
	            );
			}
		}
		
		private void drawMapBorder(SpriteBatch batch) {
			float radius = level.getMap().getRadius();
			float scale = 10;
			
			Vector2 mapCenter = level.getMap().getCenter();
			Texture mapBorderTexture = AssetManager.MAPBORDER_DOT;
			
			for (float angle = 0; angle < 360; angle += 1.5f) {
				Vector2 dotPos = new Vector2(radius,0).rotate(angle).add(mapCenter);
				
				batch.draw(
						mapBorderTexture,
						dotPos.x - mapBorderTexture.getWidth() * scale / 2f,
	                    dotPos.y - mapBorderTexture.getHeight() * scale / 2f,
	                    mapBorderTexture.getWidth() * scale,
	                    mapBorderTexture.getHeight() * scale
				);
			}
		}
		
		public static float getMinZoom() {
			return MIN_ZOOM;
		}
		
		public static float getMaxZoom() {
			return MAX_ZOOM;
		}
		
		public TrajectorySimulator getTrajectorySimulator() {
			return trajectorySimulator;
		}
		
		public void playThrustStarter() {
	        thrusterGoinger.play(GamePreferences.getInstance().getMasterVolume() / 10f);
	    }
		
		public void playThrusterEnder() {
	        if (isThrustStopperActive)
	            AssetManager.THRUSTER_ENDER.play(GamePreferences.getInstance().getMasterVolume() / 10f);
	    }
		
		public void playThrusterGoinger() {
	        thrusterGoinger.play(GamePreferences.getInstance().getMasterVolume() / 10f);
	    }
		
		public void stopThrusterGoinger() {
	        thrusterGoinger.stop();
	    }
		
		public void playThrusterStarter() {
	        AssetManager.THRUSTER_STARTER.play(GamePreferences.getInstance().getMasterVolume() / 10f);
	    }
		
		public void setThrustStopperActive(boolean thrustStopperActive) {
	        isThrustStopperActive = thrustStopperActive;
	    }
		
		public void playBackgroundMusic() {
	        backMusic.setVolume(GamePreferences.getInstance().getMasterVolume() / 4f);
	        backMusic.play();
	    }
		
		public void stopBackgroundMusic() {
	        backMusic.stop();
	    }
		
		public void playWarningSound() {
	        warningSound.setVolume(GamePreferences.getInstance().getMasterVolume() / 3f);
	        warningSound.play();
	    }
		
		public void stopWarningSound() {
	        warningSound.stop();
	    }
	
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			textureMeteor.dispose();
			textureStar.dispose();
		}

}
