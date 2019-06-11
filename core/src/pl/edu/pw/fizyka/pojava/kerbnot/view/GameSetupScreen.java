package pl.edu.pw.fizyka.pojava.kerbnot.view;

/*
 * @author Gosia
 */

//class that shows screen in which user chooses opitons of the game, like mass and fuel quantity or ship appearance

import static pl.edu.pw.fizyka.pojava.kerbnot.util.Constants.DEBUG;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Timer;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;
import pl.edu.pw.fizyka.pojava.kerbnot.loaders.AssetLoader;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Level;
import pl.edu.pw.fizyka.pojava.kerbnot.model.LevelManager;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Playable;
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
	private Skin skin;
	static CheckBox ship1;
	static CheckBox ship2;
	Image imageShip1;
	Image imageShip2;
	private float startingFuel;
	private String startingFuelString;
	private float shipMass;
	private String shipMassString;
	
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
		
		skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
		
		//Creating and centering table
		Table table = new Table();
		table.setFillParent(true);
		
		//Textfields for mass and fuel
		final TextField fuel = new TextField("50", AssetLoader.SKIN);
		final TextField mass = new TextField("50", AssetLoader.SKIN);
		
		imageShip1 = new Image(AssetManager.PLAYER_TEXTURE);
		imageShip2 = new Image (AssetManager.PLAYER_TEXTURE_2);
				
		//checkboxes for choosing ship appearance
		ship1 = new CheckBox(GamePreferences.getInstance().isEnglishActive() ? "Ship 1" : "Statek 1", AssetLoader.SKIN);
		ship1.setChecked(true);
		ship2 = new CheckBox(GamePreferences.getInstance().isEnglishActive() ? "Ship 2" : "Statek 2", AssetLoader.SKIN);
		
		ButtonGroup shipGroup = new ButtonGroup(ship1, ship2);
		shipGroup.setMaxCheckCount(1);
		shipGroup.setMinCheckCount(1);
		
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
		table.add(ship1).uniform().fill();
		table.add(imageShip1).width(imageShip1.getWidth()/2f).height(imageShip1.getWidth()/2f);
		table.row().padTop(20);
		table.add(ship2).uniform().fill();
		table.add(imageShip2).width(imageShip2.getWidth()/2f).height(imageShip2.getWidth()/2f);
		table.row().padTop(20);
		table.add(start).uniform().fill();
		table.row().padTop(20);
		table.add(back);
		
		
		//Listeners
		
		//Start
		start.addListener(new ChangeListener( ) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startingFuelString = fuel.getText();
				startingFuel = Float.parseFloat(startingFuelString);
				shipMassString = mass.getText();
				shipMass = Float.parseFloat(shipMassString);
				//game doesn't start when mass or fuel <= 0
				if (startingFuel <= 0 || shipMass <= 0 ) {
					Dialog dialog = new Dialog("", skin, "dialog") {
						public void result(Object obj) {
							switch ((Integer) obj) {			
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
			            }
					};
					
					
					dialog.text(GamePreferences.getInstance().isEnglishActive() ? "Error" : "Blad");
					dialog.row();
					dialog.button(GamePreferences.getInstance().isEnglishActive() ? "OK" : "OK", -1);
					
					Cell secondCell = dialog.getButtonTable().getCells().get(0);
					secondCell.width(secondCell.getPrefWidth() * 1.5f).height(secondCell.getPrefHeight() * 1.5f);
					for (Cell cell : dialog.getButtonTable().getCells()) {
			            cell.uniform().fill();
			        }
					dialog.getCells().first().padTop(dialog.getPrefHeight() * 0.05f);
			        dialog.padBottom(dialog.getPrefHeight() * 0.05f);
			        
			        dialog.key(Input.Keys.ESCAPE, -1);
			       
			        dialog.show(stage);
					
					
					
				} else {
					setStartingFuel();
					setDryMass();
					game.setScreen(new GameScreen(mainMenuScreen, game, LevelManager.createLevel(), batch, font));
				}
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
	
	public void setStartingFuel() {
		Playable.startingFuel = startingFuel;
	}
	
	public void setDryMass() {
		Playable.dryMass = shipMass;
	}

}
