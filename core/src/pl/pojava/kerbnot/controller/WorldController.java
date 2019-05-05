package pl.pojava.kerbnot.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;

import pl.pojava.kerbnot.model.ForceDiagram;
import pl.pojava.kerbnot.model.Level;
import pl.pojava.kerbnot.model.Playable;
import pl.pojava.kerbnot.model.TrajectorySimulator;
import pl.pojava.kerbnot.view.GameScreen;
import pl.pojava.kerbnot.view.WorldRenderer;

public class WorldController {
	
	private Level level;
	private GameScreen screen;
	private WorldRenderer renderer;
	
	public static byte controlState = 7;
	
	public WorldController(Level level, GameScreen screen, WorldRenderer renderer) {
		this.level = level;
		this.screen = screen;
		this.renderer = renderer;
	}
	
	public void update(float deltaTime) {
		Playable playable = level.getPlayable();
		Body body = playable.getBody();
		
		if (controlState == -1 && Gdx.input.isKeyPressed(Input.Keys.A)) {
			screen.zoomIn();
		}
		if (controlState == -1 && Gdx.input.isKeyPressed(Input.Keys.S)) {
			screen.zoomOut();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && level.getState() == Level.State.RUNNING) {
			screen.showPauseScreen();
		}
		if (controlState >= 2 && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playable.turnLeft(deltaTime);
		}
		if (controlState >= 2 && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playable.turnRight(deltaTime);
		}
		if (controlState >= 3 && Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
			playable.toggleSAS();
		}
		if (controlState >= 3 && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playable.runSAS(deltaTime);
		}
		if (controlState >= 4 && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (playable.getCurrentThrust() == 0) {
				renderer.stopThrusterGoinger();
				renderer.playThrusterEnder();
				renderer.setThrustStopperActive(false);
			}
			playable.decreaseThrust(deltaTime);
		}
		if (controlState >= 5 && Gdx.input.isKeyPressed(Input.Keys.A)) {
			screen.zoomIn();
		}
		if (controlState >= 5 && Gdx.input.isKeyPressed(Input.Keys.S)) {
			screen.zoomOut();
		}
		if (controlState >= 6 && Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			TrajectorySimulator.enabled = !TrajectorySimulator.enabled;
		}
		
		if (controlState >= 6 && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			ForceDiagram.setEnabled(!ForceDiagram.isEnabled());
		}
		
	}
}
