package pl.pojava.kerbnot.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class ForceDiagram extends GameObject {
	
	public static final float FRAME_RATE = 1f / 60;
	private Level level;
	private Array<Planet> planets;
	private Array<Vector2> vectors;
	private Vector2 impulseVector;
	private Vector2 resultant;
	private static boolean enabled = false;
	
	public ForceDiagram(Level level) {
		this.level = level;
	}
	
	@Override
	public void update(float deltaTime) {
		doGravity();
		doThrust();
		doResultant();
	}
	
	private void doGravity() {
		for (Planet planet : level.getPlanets()) {
			Body spaceship = level.getPlayable().getBody();
			
			Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());
			
			float forceScalar = level.G * spaceship.getMass() * planet.getMass() / directionVector.len2();
			
			Vector2 forceVector = directionVector.setLength(forceScalar);
			vectors.add(forceVector);
		}
	}
	
	private void doThrust() {
		impulseVector = new Vector2(0, level.getPlayable().getCurrentThrust()).rotateRad(level.getPlayable().getBody().getAngle());
	}
	
	private void doResultant() {
		resultant = impulseVector;
		for (Vector2 vector : vectors) {
			resultant = resultant.add(vector);
		}
	}
	
	public Vector2 getResultant() {
		return resultant;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static void setEnabled(boolean set) {
		enabled = set;
	}
}
