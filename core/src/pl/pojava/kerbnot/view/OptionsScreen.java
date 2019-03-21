package pl.pojava.kerbnot.view;

import java.awt.Checkbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.pojava.kerbnot.KerbNot;
import pl.pojava.kerbnot.Preferences;
import pl.pojava.kerbnot.loaders.AssetLoader;

public class OptionsScreen implements Screen {
	
	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private MainMenuScreen mainMenuScreen;

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
		volumeSlider.setValue(Preferences.volume);
		
		//Music checkbox
		CheckBox musicCheck = new CheckBox("Music", AssetLoader.SKIN);
		musicCheck.setChecked(Preferences.music);
		
		//Buttons
		TextButton back = new TextButton("Back", AssetLoader.SKIN);
		
		table.center();
		table.add(new Label("Options", AssetLoader.SKIN));
		table.row().padTop(20);
		table.add(new Label("Volume", AssetLoader.SKIN)).uniform().fill();
		table.row();
		table.add(volumeSlider).uniform().fill();
		table.row().padTop(20);
		table.add(musicCheck).uniform().fill();
		table.row().padTop(20);
		table.add(back);
		
		//Listeners
		
		//Volume
		back.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Preferences.volume = volumeSlider.getValue();
			}
		});
		
		//Music
		musicCheck.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Preferences.music = !Preferences.music;
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
