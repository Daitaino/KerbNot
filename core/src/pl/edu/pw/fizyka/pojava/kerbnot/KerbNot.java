package pl.edu.pw.fizyka.pojava.kerbnot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.edu.pw.fizyka.pojava.kerbnot.model.Level;
import pl.edu.pw.fizyka.pojava.kerbnot.view.MainMenuScreen;

public class KerbNot extends Game {
	
	private SpriteBatch batch;
	private BitmapFont font;
	private Level level;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		level = new Level();
		this.setScreen(new MainMenuScreen(this, batch, font));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
