package pl.edu.pw.fizyka.pojava.kerbnot.view;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;
import pl.edu.pw.fizyka.pojava.kerbnot.loaders.AssetLoader;
import pl.edu.pw.fizyka.pojava.kerbnot.model.LevelManager;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Ship;
import pl.edu.pw.fizyka.pojava.kerbnot.util.GamePreferences;

public class GameSetupScreen implements Screen {
	
	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private MainMenuScreen mainMenuScreen;
	private GameSetupScreen thisScreen;
	public Ship ship;
	
	public GameSetupScreen(MainMenuScreen mainMenuScreen, KerbNot game, SpriteBatch batch, BitmapFont font) {
		this.game = game;
		this.batch = batch;
		this.font = font;
		this.mainMenuScreen = mainMenuScreen;
		thisScreen = this;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Creating and centering table
		Table table = new Table();
		table.setFillParent(true);
		
		//Textfields TODO: More settings
		final TextField fuel = new TextField("50", AssetLoader.SKIN);
		final TextField mass = new TextField("50", AssetLoader.SKIN);
		
		//Buttons
		TextButton start = new TextButton("Start", AssetLoader.SKIN);
		TextButton back = new TextButton(GamePreferences.getInstance().isEnglishActive() ? "Back" : "Wroc", AssetLoader.SKIN);
		
		table.center();
		table.add(new Label(GamePreferences.getInstance().isEnglishActive() ? "Game setup" : "Ustawienia gry", AssetLoader.SKIN));
		table.row().padTop(20);
		table.add(new Label(GamePreferences.getInstance().isEnglishActive() ? "Fuel" : "Paliwo", AssetLoader.SKIN)).uniform().fill();
		table.row();
		table.add(fuel).uniform().fill();
		table.row().padTop(20);
		table.add(new Label(GamePreferences.getInstance().isEnglishActive() ? "Mass" : "Masa", AssetLoader.SKIN)).uniform().fill();
		table.row();
		table.add(mass).uniform().fill();
		table.row().padTop(20);
		table.add(start).uniform().fill();
		table.row().padTop(20);
		table.add(back);
		
		//Listeners
		
		//Start
		start.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(mainMenuScreen, game, LevelManager.createLevel(), batch, font));
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
