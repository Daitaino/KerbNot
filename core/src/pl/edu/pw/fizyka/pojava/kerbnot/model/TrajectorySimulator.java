package pl.edu.pw.fizyka.pojava.kerbnot.model;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import pl.edu.pw.fizyka.pojava.kerbnot.util.GameUtils;

import static pl.edu.pw.fizyka.pojava.kerbnot.util.Constants.*;

import com.badlogic.gdx.math.Vector2;

public class TrajectorySimulator extends GameObject {

	public static final int SIMULATION_STEPS = 3600;

	public static final float SKIP_MULTIPLIER = 20;

	public static final int MIN_SKIP = 2;

	public static final float MAX_VELOCITY = 120;

	public static final int IGNORE_THRESHOLD = 15;

	public static boolean enabled = true;

	private World world;
	private Level level;

	private Array<Vector2> currentEstimationPath;

	private Array<Planet> planets;

	private Playable playable;

	private boolean collided = false;

	private Vector2 collisionPoint;

	public TrajectorySimulator(Level level) {
		this.level = level;

		planets = new Array<Planet>();
		currentEstimationPath = new Array<Vector2>();

		world = new World(new Vector2(0, 0), true);

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA().getUserData() == Level.ObjectType.PLAYABLE) {
					collisionPoint = contact.getFixtureA().getBody().getPosition();
				} else if (contact.getFixtureB().getUserData() == Level.ObjectType.PLAYABLE) {
					collisionPoint = contact.getFixtureB().getBody().getPosition();
				}

				collided = true;

			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}
		});

		for (Planet planet : level.getPlanets()) {
			Planet newPlanet = new Planet(
					planet.getBody().getPosition().x,
					planet.getBody().getPosition().y,
					planet.getMass(),
					planet.getRadius(),
					planet.getPrimary(),
					world,
					planet.getPlanetType()
			);
			planets.add(newPlanet);
		}
		
		playable = new Playable(
				level.getPlayable().getBody().getPosition().x,
				level.getPlayable().getBody().getPosition().y,
				level.getPlayable().getWidth(),
				level.getPlayable().getHeight(),
				level.getPlayable().getBody().getMass() - level.getPlayable().getFuelLeft(),
				level.getPlayable().getDeltaTorque(),
				level.getPlayable().getDeltaThrust(),
				level.getPlayable().getMaxThrust(),
				level.getPlayable().getFuelLeft(),
				world
				);
		
		resetSimulation();
	}
	
	private void resetSimulation() {
		
		currentEstimationPath.clear();
		
		world.clearForces();
		
		Body simulatedPlayable = playable.getBody();
		Body realPlayable = level.getPlayable().getBody();
		
		playable.setCurrentThrust(level.getPlayable().getCurrentThrust());
		playable.setFuelLeft(level.getPlayable().getFuelLeft());
		playable.setSASEnabled(level.getPlayable().getSASEnabled());
		
		if (level.getPlayable().getSASEnabled())
			simulatedPlayable.setAngularVelocity(0);
		else
			simulatedPlayable.setAngularVelocity(realPlayable.getAngularVelocity());
		
		simulatedPlayable.setLinearVelocity(realPlayable.getLinearVelocity());
		simulatedPlayable.setTransform(realPlayable.getPosition(), realPlayable.getAngle());
		simulatedPlayable.getTransform().setOrientation(realPlayable.getTransform().getOrientation());
		simulatedPlayable.getTransform().setRotation(realPlayable.getTransform().getRotation());
		
		GameUtils.changeMass(simulatedPlayable, realPlayable.getMass());
				
	}
	
	@Override
	public void update(float deltaTime) {
		collided = false;
		
		if (level.getPlayable().getBody().getLinearVelocity().len() + level.getPlayable().getCurrentThrust() < IGNORE_THRESHOLD) {
			currentEstimationPath.clear();
			return;
		}
		
		int skipCount = (int) ((1 - (level.getPlayable().getBody().getLinearVelocity().len() / MAX_VELOCITY)) * SKIP_MULTIPLIER);
		skipCount = Math.max(MIN_SKIP, skipCount);
		
		resetSimulation();
		
		for (int i = 1; i <= SIMULATION_STEPS; i++) {
			if (collided)
				break;
			
			doGravity();
			updatePlanets();
			playable.update(FRAME_RATE);
			world.step(FRAME_RATE, 8, 3);
			
			if (i % skipCount == 0)
				currentEstimationPath.add(playable.getBody().getPosition().cpy());
		}
	}
	
	private void updatePlanets() {
		for (int i = 0; i < planets.size; i++) {
			Planet myPlanet = planets.get(i);
			Planet otherPlanet = level.getPlanets().get(i);
			
			myPlanet.getBody().setTransform(otherPlanet.getBody().getPosition(), 0);
		}
	}
	
	private void doGravity() {
		for (Planet planet : planets) {
			Body spaceship = playable.getBody();
			
			Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());
			
			float forceScalar = level.G * spaceship.getMass() * planet.getMass() / directionVector.len2();
			
			Vector2 forceVector = directionVector.setLength(forceScalar);
			
			spaceship.applyForceToCenter(forceVector, true);
		}
	}
	
	public Array<Vector2> getEstimationPath() {
		return currentEstimationPath;
	}
	
	public Vector2 getCollisionPoint() {
		return collisionPoint;
	}
	
	public boolean isCollided() {
		return collided;
	}
}

