package pl.pojava.kerbnot.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.pojava.kerbnot.KerbNot;

public class CreditsScreen implements Screen {
	
	private KerbNot game;
	private SpriteBatch batch;
	private BitmapFont font;
	private Stage stage;
	private MainMenuScreen mainMenuScreen;
	private Texture backgroundTexture;
	private Sprite backgroundSprite;

	public CreditsScreen(MainMenuScreen mainMenuScreen,KerbNot game, SpriteBatch batch, BitmapFont font) {
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
		
		//Background
		backgroundTexture = new Texture(Gdx.files.internal("background/menuBackground.jpg"));
		backgroundSprite = new Sprite(backgroundTexture);
		
		//Skin setup
		Skin skin = new Skin(Gdx.files.internal("skin/tracer-ui.json"));
		
		//Authors
		String author1 = "Filip Jakubczak";
		String author2 = "Malgorzata Korona";
		
		//Buttons
		TextButton back = new TextButton("Back", skin);
		
		table.center();
		table.add(new Label("Authors", skin));
		table.row().padTop(20);
		table.add(new Label(author1, skin)).uniform().fill();
		table.row();
		table.add(new Label(author2, skin)).uniform().fill();
		table.row().padTop(20);
		table.add(back);
		
		//Listeners
		back.addListener(new ClickListener( ) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(mainMenuScreen);
			}
		});
		
		stage.addActor(table);;
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
