package pl.edu.pw.fizyka.pojava.kerbnot.view;

//class for main screen

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
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;
import pl.edu.pw.fizyka.pojava.kerbnot.loaders.AssetLoader;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Ship;

public class MainScreen implements Screen {

	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private MainMenuScreen mainMenuScreen;
	private Ship ship;
	private float time;
	private Label timeLabel;
	
	public MainScreen(Ship ship, MainMenuScreen mainMenuScreen, KerbNot game, SpriteBatch batch, BitmapFont font) {
		this.game = game;
		this.batch = batch;
		this.font = font;
		this.mainMenuScreen = mainMenuScreen;
		this.ship = ship;
		this.time = 0.0f;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Creating and centering table
		Table buttons = new Table();
		buttons.setFillParent(true);
		Table stats = new Table();
		stats.setFillParent(true);
		Table timeContainer = new Table();
		timeContainer.setFillParent(true);
		
		//Buttons
		TextButton menu = new TextButton("Menu", AssetLoader.SKIN);
		
		//Table setup
		buttons.top().right().padTop(20).padRight(20);
		buttons.add(menu).uniform().fill();
		
		//Labels
		timeLabel = new Label(String.valueOf(time), AssetLoader.SKIN);
		
		stats.bottom().left().padBottom(100).padLeft(20);
		stats.add(new Label("Fuel", AssetLoader.SKIN)).fill().uniform();
		stats.row();
		stats.add(new Label("0", AssetLoader.SKIN)); // TODO display current fuel
		
		timeContainer.top().left().padTop(20).padLeft(20);
		timeContainer.add(new Label("Time", AssetLoader.SKIN)).fill().uniform();
		timeContainer.row();
		timeContainer.add(timeLabel).fill().uniform();
		
		//Listeners
		
		//Menu
		menu.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(mainMenuScreen);
			}
		});
		
		stage.addActor(buttons);
		stage.addActor(stats);
		stage.addActor(timeContainer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		time += delta;
		timeLabel.setText(Float.toString(time));
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
