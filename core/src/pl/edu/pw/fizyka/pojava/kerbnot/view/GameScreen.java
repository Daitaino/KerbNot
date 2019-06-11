package pl.edu.pw.fizyka.pojava.kerbnot.view;

/**
 * author Ma³gorzata
 */

//class with game screen: it draws things like trajectory, thrust and velocity rate, pause screen etc.

import static pl.edu.pw.fizyka.pojava.kerbnot.util.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;
import pl.edu.pw.fizyka.pojava.kerbnot.controller.WorldController;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Level;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Playable;
import pl.edu.pw.fizyka.pojava.kerbnot.model.PositionTrigger;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Trigger;
import pl.edu.pw.fizyka.pojava.kerbnot.util.GamePreferences;

public class GameScreen implements Screen {
	
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
	private Level level;
	private Playable cameraTarget;
	private WorldRenderer renderer;
	private WorldController controller;
	private ParticleEffect particleEffect;
	private Stage stage;
	private Skin skin;
	private KerbNot game;
	private float elapsedTime;
	private Minimap minimap;
	private PopupView popupView;
	private BitmapFont timerFont;
	private ShapeRenderer shapeRenderer;
	private Animation<TextureRegion> waypointAnimation;
	private TextureAtlas waypointAtlas;
	private Texture sasTexture;
	private String endLevelText;
	private MainMenuScreen mainMenuScreen;
	
	public GameScreen(MainMenuScreen mainMenuScreen, KerbNot game, Level level, SpriteBatch batch, BitmapFont font) {
		elapsedTime = 0;
		this.game = game;
		this.level = level;
		this.batch = batch;
		this.font = font;
		this.mainMenuScreen = mainMenuScreen;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		debugRenderer.dispose();
		renderer.dispose();
		timerFont.dispose();
		font.dispose();
		particleEffect.dispose();
		timerFont.dispose();
		waypointAtlas.dispose();
		shapeRenderer.dispose();
		skin.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		controller.update(delta);
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		sasTexture = AssetManager.SAS_OFF;
		
		batch.begin();
		renderer.draw(batch);
		
		//particles coming out of racket
		if(cameraTarget.getCurrentThrust() > 0) {
			this.igniteRocketTrail();
		} else {
			this.stopRocketTrail();
		}
		
		//drawing particles
		particleEffect.draw(batch);
		particleEffect.update(delta);
		particleEffect.setPosition(level.getPlayable().getBottomPosition().x * toPixel, level.getPlayable().getBottomPosition().y * toPixel);
		float angle = level.getPlayable().getBody().getAngle() * MathUtils.radiansToDegrees + 270;
		for (int i = 0; i < particleEffect.getEmitters().size; i++) {
			particleEffect.getEmitters().get(i).getAngle().setHigh(angle,angle);
			particleEffect.getEmitters().get(i).getAngle().setLow(angle);
		}
		draw();
		
		batch.end();
		
		if (DEBUG) {
			debugRenderer.render(level.getWorld(), camera.combined.scl(PPM));
		}
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		elapsedTime += delta;
	}

	private void draw() {
		// TODO Auto-generated method stub
		
		//drawing data
		if (DEBUG) {
			
			drawDebugString(GamePreferences.getInstance().isEnglishActive() ? "Fuel left: " + cameraTarget.getFuelLeft() : "Pozostale paliwo: "  + cameraTarget.getFuelLeft(), 26);
			drawDebugString(GamePreferences.getInstance().isEnglishActive() ? "Thrust: " + cameraTarget.getCurrentThrust() : "Sila ciagu: " + cameraTarget.getCurrentThrust(), 27);
			drawDebugString("Y: " + String.format("%.2f", cameraTarget.getBody().getPosition().y), 28);
			drawDebugString("X: " + String.format("%.2f", cameraTarget.getBody().getPosition().x), 29);
			drawDebugString(GamePreferences.getInstance().isEnglishActive() ? "Linear velocity: " + cameraTarget.getBody().getLinearVelocity().len() * 10 : "Predkosc katowa: " + cameraTarget.getBody().getLinearVelocity().len() * 10, 30);
			drawDebugString(GamePreferences.getInstance().isEnglishActive() ? "Angular velocity: " + cameraTarget.getBody().getAngularVelocity() : "Predkosc katowa: " + cameraTarget.getBody().getAngularVelocity(), 31);
			drawDebugString(GamePreferences.getInstance().isEnglishActive() ? "Gravitational force: " + level.getCurrentGravityForce() : "Sila grawitacji: " + level.getCurrentGravityForce(), 32);
			drawDebugString("SAS" + level.getPlayable().getSASEnabled(), 33);
		}
		
		batch.draw(
				AssetManager.OVERLAY, 
				camera.position.x - camera.viewportWidth / 2f * camera.zoom, 
				camera.position.y - camera.viewportHeight / 2f * camera.zoom,
				0, 
				0,
				camera.viewportWidth, 
				camera.viewportHeight, 
				camera.zoom,
				camera.zoom,
				0, 
				0, 
				0,
				AssetManager.OVERLAY.getWidth(),
				AssetManager.OVERLAY.getHeight(),
				false,
				false
		);
		
		Texture overlayFiller = AssetManager.PROGFILLER;
		overlayFiller.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		
		//drawing velocity rate
		float velocityRate;
		if (cameraTarget.getBody().getLinearVelocity().len() * 10 > 1165) {
			velocityRate = 394;
		} else {
			velocityRate = ((cameraTarget.getBody().getLinearVelocity().len() * 10) * 394) / level.getPlayable().getMaxVelocity();
		}
		
		batch.draw(
				overlayFiller, 
				camera.position.x - (camera.viewportWidth / 2f - 462) * camera.zoom,
				camera.position.y - (camera.viewportHeight / 2f - 677) * camera.zoom, 
				0, 
				0, 
				velocityRate, 
				20,
				camera.zoom,
				camera.zoom,
				0, 
				0, 
				0, 
				overlayFiller.getWidth(),
				overlayFiller.getHeight(),
				false,
				false
		);
		
		//drawing fuel rate
		float fuelRate;
		fuelRate = (level.getPlayable().getFuelLeft() * 394) / level.getPlayable().getStartingFuel();
		
		batch.draw(
				overlayFiller, 
				camera.position.x - (camera.viewportWidth / 2f - 462) * camera.zoom,
				camera.position.y - (camera.viewportHeight / 2f - 635.9f) * camera.zoom, 
				0, 
				0, 
				fuelRate, 
				20,
				camera.zoom, //scaleX
				camera.zoom, //scaleY
				0, //rotation
				0, 
				0, 
				overlayFiller.getWidth(),
				overlayFiller.getHeight(),
				false,//whether to flip the sprite horizontally
				false //whether to flip the sprite vertically
		);
		
		
		//drawing thrust rate
		float thrustRate;
        thrustRate = (level.getPlayable().getCurrentThrust() * 69) / level.getPlayable().getMaxThrust(); //69 When bar is full
        batch.draw(
                overlayFiller,
                camera.position.x - (camera.viewportWidth / 2f - 894) * camera.zoom, //462 Fuel bar's starting pointX
                camera.position.y - (camera.viewportHeight / 2f - 635.9f) * camera.zoom, //635.9f Fuel bar's starting pointY
                0,
                0,
                14,
                thrustRate, //When bar is max
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                overlayFiller.getWidth(),
                overlayFiller.getHeight(),
                false,
                false
        );
		
		String timeText = "" + (int) elapsedTime;
		if((int) elapsedTime >= 60 ) {
			timeText = (int) elapsedTime / 60 + ":" + (int) elapsedTime % 60;
		}
		
		//drawing time
		timerFont.getData().setScale(camera.zoom);
		timerFont.draw(
				batch, 
				timeText, 
				camera.position.x + (camera.viewportWidth / 2f - 140f) * camera.zoom - new GlyphLayout(font, timeText).width / 2f,
				camera.position.y + (camera.viewportHeight / 2f + 320f) * camera.zoom - timerFont.getLineHeight() * 12f
		);
		
		
		
		//
		Texture rocketTexture1 = AssetManager.PLAYER_TEXTURE;
		Texture rocketTexture2 = AssetManager.PLAYER_TEXTURE_2;
		
		
		for ( int i = 0; i < level.getHealth(); i++) {
			if(GameSetupScreen.ship1.isChecked()) {
				batch.draw(
						rocketTexture1,
						camera.position.x -(-120 + (camera.viewportWidth/2f) - (75 * i)) * camera.zoom,
						camera.position.y - (camera.viewportHeight/2f - 635) * camera.zoom,
						0,
						0,
						rocketTexture1.getWidth() / 1.7f,
						rocketTexture1.getHeight() / 1.7f,
						camera.zoom,
						camera.zoom,
						0,
						0,
						0,
						rocketTexture1.getWidth(),
						rocketTexture1.getHeight(),
						false,
						false
				);
			}
			else if(GameSetupScreen.ship2.isChecked()) {
				batch.draw(
						rocketTexture2,
						camera.position.x -(-120 + (camera.viewportWidth/2f) - (75 * i)) * camera.zoom,
						camera.position.y - (camera.viewportHeight/2f - 635) * camera.zoom,
						0,
						0,
						rocketTexture2.getWidth() / 3f,
						rocketTexture2.getHeight() / 3f,
						camera.zoom,
						camera.zoom,
						0,
						0,
						0,
						rocketTexture2.getWidth(),
						rocketTexture2.getHeight(),
						false,
						false
						);
			}
		}
		
		font.draw(
				batch,
				level.getObjectiveWindow().getText(),
				camera.position.x - (camera.viewportWidth / 2f - 20) * camera.zoom,
				camera.position.y + (camera.viewportHeight / 2f - 100) * camera.zoom
		);
		
		
		//drawing warning
		if (renderer.getTrajectorySimulator().isCollided() && (int) (elapsedTime * 2) % 2 == 0) {
			batch.draw(
					AssetManager.WARNING,
					camera.position.x - (camera.viewportWidth / 2f - 1022) * camera.zoom,
					camera.position.y - (camera.viewportHeight / 2f - 235) * camera.zoom,
					0,
					0,
					AssetManager.WARNING.getWidth() / 14f,
					AssetManager.WARNING.getHeight() / 14f,
					camera.zoom,
					camera.zoom,
					0,
					0,
					0,
					AssetManager.WARNING.getWidth(),
                    AssetManager.WARNING.getHeight(),
                    false,
                    false
			);
		}
		
		//drawing waypoint
		if (level.getWaypoint() != null) {
			TextureRegion way = waypointAnimation.getKeyFrame(elapsedTime, true);
			batch.draw(
					way,
					level.getWaypoint().getPosition().x * toPixel,
                    level.getWaypoint().getPosition().y * toPixel,
                    0,
                    0,
                    way.getRegionWidth(),
					way.getRegionHeight(),
					0.25f,
					0.25f,
					level.getWaypoint().getAngle()
			);
		}
		
		//drawing minimap
		if (minimap.isEnabled()) {
			minimap.draw(batch);
		}
		
		//drawing SAS
		if (level.getPlayable().getSASEnabled())
	           sasTexture = AssetManager.SAS_ON;
	       else
	           sasTexture = AssetManager.SAS_OFF;

	       batch.draw(
                sasTexture,
                camera.position.x - (camera.viewportWidth / 2f - 0) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 400) * camera.zoom,
                0,
                0,
                sasTexture.getWidth(),
                sasTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                sasTexture.getWidth(),
                sasTexture.getHeight(),
                false,
                false
        );

	    //drawing popup window
        popupView.draw(batch);
		
		//what happens when we lose or win
		if(level.getState() == Level.State.FINISHED) {
			
			 Timer.schedule(new Timer.Task() {
	                @Override
	                public void run() {
						if (level.getHealth() != 0) {
							boolean isGameOver = true;
							endLevelText = "You made it back safely! That was close!";
				            
							renderer.stopThrusterGoinger();
							renderer.stopWarningSound();
			                renderer.stopBackgroundMusic();
			                popupView.stopPopupShutter();
			                

			                game.setScreen(new MainMenuScreen(game, batch, font));
						} else {
							boolean isGameOver = true;
							endLevelText = "You didn't make it....";
				            
							renderer.stopBackgroundMusic();
			                renderer.stopThrusterGoinger();
			                renderer.stopWarningSound();
			                popupView.stopPopupShutter();
			                renderer.dispose();
			                game.setScreen(new MainMenuScreen(game, batch, font));
						}
					}
			 }, 5.0f);
		}
		
		if (DEBUG) {
			for (Trigger trigger: level.getTriggers()) {
				if (trigger instanceof PositionTrigger) {
					PositionTrigger positionTrigger = (PositionTrigger) trigger;
					batch.end();
					shapeRenderer.setProjectionMatrix(camera.combined);
					shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
					
					if (positionTrigger. isTriggeredBefore()) {
						shapeRenderer.setColor(Color.GREEN);
					} else {
						shapeRenderer.setColor(Color.RED);
					}
					
					shapeRenderer.circle(positionTrigger.getPosition().x * toPixel, positionTrigger.getPosition().y * toPixel, positionTrigger.getRadius() * toPixel);
					shapeRenderer.end();
					batch.begin();
				}
			}
		}
		
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		viewport.update(arg0, arg1);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // 1. argument - whether y should be pointing down
		
		debugRenderer = new Box2DDebugRenderer();
		
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("effects/trail.p"), Gdx.files.internal("PNG"));
		
		waypointAtlas = new TextureAtlas(Gdx.files.internal("waypointSheets/waypoint.atlas"));
		waypointAnimation = new Animation<TextureRegion>(1f / 60f, waypointAtlas.getRegions());
		
		cameraTarget = level.getPlayable();
		renderer = new WorldRenderer(level, camera);
		controller = new WorldController(level, this, renderer);
		
		skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		minimap = new Minimap(1064, 48, 116, level, camera, renderer.getTrajectorySimulator());
		
		level.setScreenReference(this);
		
		viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        
        popupView = new PopupView(level.getPopup(), camera);
        
        timerFont = new BitmapFont(Gdx.files.internal("fonts/contrax.fnt"));
        
        shapeRenderer = new ShapeRenderer();
        
        level.setState(Level.State.RUNNING);
	}
	
	private void update (float delta) {
		level.update(delta);
		camera.position.set(cameraTarget.getBody().getPosition().x * toPixel, cameraTarget.getBody().getPosition().y * toPixel, 0);
		camera.update();
		popupView.update(delta);
		font.getData().setScale(camera.zoom);
	}
	
	public void lookAt (Playable target) {
		cameraTarget = target;
	}
	
	public void zoomIn() {
		camera.zoom = Math.max(renderer.getMinZoom(), camera.zoom / 1.04f);
	}
	
	public void zoomOut() {
		camera.zoom = Math.min(camera.zoom * 1.04f, renderer.getMaxZoom());
	}
	
	public void setZoom(float zoom) {
		camera.zoom = zoom;
	}
	
	private void drawDebugString (String s, int row) {
		font.draw(batch, s, camera.position.x - camera.viewportWidth/2f * camera.zoom, camera.position.y - camera.viewportHeight/2f * camera.zoom + font.getLineHeight() * row);
	}
	
	//drawing pause screen
	public void showPauseScreen() {
		Dialog dialog = new Dialog("Paused", skin, "dialog") {
			public void result(Object obj) {
				switch ((Integer) obj) {
					case 0:
						DEBUG = !DEBUG;
						break;
					case 1:
						level.resetLevel();
						level.setHealth(3);
						break;
					case 2:
						renderer.stopBackgroundMusic();
                        renderer.stopThrusterGoinger();
                        renderer.stopWarningSound();
                        popupView.stopPopupShutter();
                        renderer.dispose();
                        game.setScreen(mainMenuScreen);
                        break;
                    default:
                    	break;
				}
			}
			
			@Override
            public float getPrefWidth() {
                return super.getPrefWidth() * 1.5f;
            }
			
			@Override
            public void hide() {
                super.hide();

                new Timer().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        level.setState(Level.State.RUNNING);
                    }
                }, 0.150f);
            }
		};
		
		dialog.button(GamePreferences.getInstance().isEnglishActive() ? "Toggle Debug" : "Wlacz debugowanie", 0);
		dialog.getButtonTable().row();
		dialog.button(GamePreferences.getInstance().isEnglishActive() ? "Reset Level" : "Zrestartuj poziom", 1);
		dialog.getButtonTable().row();
		dialog.button(GamePreferences.getInstance().isEnglishActive() ? "Exit" : "Wyjscie", 2);
		dialog.getButtonTable().row().padTop(
                dialog.getButtonTable().getCells().first().getPrefHeight()
        );
		dialog.button(GamePreferences.getInstance().isEnglishActive() ? "Continue" : "Kontynuuj", -1);
		
		Cell secondCell = dialog.getButtonTable().getCells().get(1);
		secondCell.width(secondCell.getPrefWidth() * 1.5f).height(secondCell.getPrefHeight() * 1.5f);
		for (Cell cell : dialog.getButtonTable().getCells()) {
            cell.uniform().fill();
        }
		dialog.getCells().first().padTop(dialog.getPrefHeight() * 0.05f);
        dialog.padBottom(dialog.getPrefHeight() * 0.05f);
        
        dialog.key(Input.Keys.ESCAPE, -1);
        level.setState(Level.State.PAUSED);
        dialog.show(stage);
	}
	
	//particles are coming out of rocket when thrust > 0
	public void igniteRocketTrail() {
		if (particleEffect.isComplete()) {
			particleEffect.reset();
		}
		for (int i = 0; i < particleEffect.getEmitters().size; i++) {
			particleEffect.getEmitters().get(i).setContinuous(true);
		}
	}
	
	//particles stop coming out of rocket when thrust = 0
	public void stopRocketTrail() {
		for (int i = 0; i < particleEffect.getEmitters().size; i++) {
			particleEffect.getEmitters().get(i).setContinuous(false);
			particleEffect.getEmitters().get(i).duration = 0;
		}
	}
	
	public Minimap getMinimap() {
        return minimap;
    }

}
