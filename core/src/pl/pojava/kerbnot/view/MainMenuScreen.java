package pl.pojava.kerbnot.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.pojava.kerbnot.KerbNot;

//A standard main menu screen

public class MainMenuScreen implements Screen {
	
	private Stage stage;
	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Texture backgroundTexture;
	private Sprite backgroundSprite;
	private OrthographicCamera camera;
	private Vector3 camTarget;
	private Vector3 rocketPos;
	private boolean focusRocket;
	private float elapsedTime;
	private MainMenuScreen thisScreen = this;
	
	public MainMenuScreen(KerbNot game, SpriteBatch batch, BitmapFont font) {
		this.game = game;
		this.batch = batch;
		this.font = font;
	}

	@Override
	public void show() {
		
		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		
		focusRocket = false;
		elapsedTime = 0;
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Create and center table
		Table table = new Table();
		table.setFillParent(true);
		
		Skin skin = new Skin(Gdx.files.internal("skin/tracer-ui.json"));
		
		backgroundTexture = new Texture(Gdx.files.internal("background/menuBackground.jpg"));
		backgroundSprite = new Sprite(backgroundTexture);
		
		
		TextButton newGame = new TextButton("New Game", skin);
		TextButton options = new TextButton("Options", skin);
		TextButton credits = new TextButton("Credits", skin);
		TextButton exit = new TextButton("Exit", skin);
		
		//Align buttons
		table.right().padRight(75);
		table.add(newGame).width(newGame.getPrefWidth() * 1.5f).height(newGame.getPrefHeight() * 1.5f).uniform();
		table.row().padTop(20);
		table.add(options).uniform().fill();
		table.row().padTop(20);
		table.add(credits).uniform().fill();
		table.row().padTop(20);
		table.add(exit).uniform().fill();
		
		//Listeners
		
		//New Game
		newGame.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameSetupScreen(thisScreen, game, batch, font));
			}
		});
		
		//Options
		options.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new OptionsScreen(thisScreen, game, batch, font));
			}
		});
		
		//Credits
		credits.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new CreditsScreen(thisScreen, game, batch, font));
			}
		});
		
		//Exit
		exit.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		stage.act(delta);
		batch.begin();
		backgroundSprite.draw(batch);
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
