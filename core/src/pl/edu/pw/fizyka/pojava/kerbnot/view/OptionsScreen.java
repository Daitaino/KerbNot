package pl.edu.pw.fizyka.pojava.kerbnot.view;

import java.awt.Checkbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;
import pl.edu.pw.fizyka.pojava.kerbnot.loaders.AssetLoader;
import pl.edu.pw.fizyka.pojava.kerbnot.util.GamePreferences;

public class OptionsScreen implements Screen {
	
	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private MainMenuScreen mainMenuScreen;
	private boolean englishStatus;
	private boolean polishStatus;

	public OptionsScreen(MainMenuScreen mainMenuScreen, KerbNot game, SpriteBatch batch, BitmapFont font) {
		this.game = game;
		this.batch = batch;
		this.font = font;
		this.mainMenuScreen = mainMenuScreen;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Creating and centering table
		Table table = new Table();
		table.setFillParent(true);
		
		//Volume slider
		final Slider volumeSlider = new Slider(0, 100, 10, false, AssetLoader.SKIN);
		volumeSlider.setValue(GamePreferences.getInstance().getMasterVolume());
		
		//Music checkbox
		final CheckBox musicCheck = new CheckBox(GamePreferences.getInstance().isEnglishActive() ? "Music" : "Muzyka", AssetLoader.SKIN);
		musicCheck.setChecked(GamePreferences.getInstance().isMusicEnabled());
		
		englishStatus = GamePreferences.getInstance().isEnglishActive();
		polishStatus = GamePreferences.getInstance().isPolishActive();
		
		final CheckBox englishLanguage = new CheckBox(GamePreferences.getInstance().isEnglishActive() ? "English" : "Angielski", AssetLoader.SKIN);
		englishLanguage.setChecked(englishStatus);
		
		final CheckBox polishLanguage = new CheckBox(GamePreferences.getInstance().isEnglishActive() ? "Polish" : "Polski", AssetLoader.SKIN);
		polishLanguage.setChecked(!englishStatus);
		
		ButtonGroup languageGroup = new ButtonGroup(englishLanguage, polishLanguage);
		languageGroup.setMaxCheckCount(1);
		languageGroup.setMinCheckCount(1);
		
		//Buttons
		final TextButton back = new TextButton(GamePreferences.getInstance().isEnglishActive() ? "Back" : "Wroc", AssetLoader.SKIN);
		
		table.center();
		
		final Label optionsLabel = new Label(GamePreferences.getInstance().isEnglishActive() ? "Options" : "Opcje", AssetLoader.SKIN);
		final Label volumeLabel = new Label(GamePreferences.getInstance().isEnglishActive() ? "Volume" : "Dzwiek", AssetLoader.SKIN);
		
		table.add(optionsLabel);
		table.row().padTop(20);
		table.add(volumeLabel).uniform().fill();
		table.row();
		table.add(volumeSlider).uniform().fill();
		table.row().padTop(20);
		table.add(musicCheck).uniform().fill();
		table.row().padTop(20);
		table.add(englishLanguage).uniform().fill();
		table.row().padTop(20);
		table.add(polishLanguage).uniform().fill();
		table.row().padTop(20);
		table.add(back);
		
		//Listeners
		
		//Volume
		back.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GamePreferences.getInstance().setMasterVolume(volumeSlider.getValue());
			}
		});
		
		//Music
		musicCheck.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GamePreferences.getInstance().setMusic(!GamePreferences.getInstance().isMusicEnabled());
			}
		});
		
		englishLanguage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if(!GamePreferences.getInstance().isEnglishActive()) {
                    GamePreferences.getInstance().setEnglishLanguage(true);
                   // GamePreferences.getInstance().setPolishLanguage(false);
                }
                else
                {
                	GamePreferences.getInstance().setEnglishLanguage(false);
                   // GamePreferences.getInstance().setPolishLanguage(true);
                }
				
				englishLanguage.setText(GamePreferences.getInstance().isEnglishActive() ? "English" : "Angielski");
				polishLanguage.setText(GamePreferences.getInstance().isEnglishActive() ? "Polish" : "Polski");
				optionsLabel.setText(GamePreferences.getInstance().isEnglishActive() ? "Options" : "Opcje");
				volumeLabel.setText(GamePreferences.getInstance().isEnglishActive() ? "Volume" : "Dzwiek");
				musicCheck.setText(GamePreferences.getInstance().isEnglishActive() ? "Music" : "Muzyka");
				back.setText(GamePreferences.getInstance().isEnglishActive() ? "Back" : "Wroc");
				GamePreferences.getInstance().save();
			}
			
		});
		
		polishLanguage.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if(!GamePreferences.getInstance().isPolishActive()) {
					//GamePreferences.getInstance().setPolishLanguage(true);
                    GamePreferences.getInstance().setEnglishLanguage(false);    
                }
                else
                {
                	//GamePreferences.getInstance().setPolishLanguage(false);
                	GamePreferences.getInstance().setEnglishLanguage(true);
                   
                }
				
				englishLanguage.setText(GamePreferences.getInstance().isEnglishActive() ? "English" : "Angielski");
				polishLanguage.setText(GamePreferences.getInstance().isEnglishActive() ? "Polish" : "Polski");
				optionsLabel.setText(GamePreferences.getInstance().isEnglishActive() ? "Options" : "Opcje");
				volumeLabel.setText(GamePreferences.getInstance().isEnglishActive() ? "Volume" : "Dzwiek");
				back.setText(GamePreferences.getInstance().isEnglishActive() ? "Back" : "Wroc");
				GamePreferences.getInstance().save();
			}
			
		});
		
		
		//Back
		back.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(mainMenuScreen);
			}
		});
		
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(delta);
		batch.begin();
		AssetLoader.BACKGROUND_SPRITE.draw(batch);
		batch.end();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

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
		stage.dispose();

	}

}
