package pl.pojava.kerbnot.model;

import java.nio.file.Watchable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import pl.pojava.kerbnot.objects.*;
import pl.pojava.kerbnot.util.Constants;

/**
 * 
 * @author Filip
 * The core model of the game. Updates and manages all entities.
 */
public class Level {

	public enum ObjectType {
		PLANET, OBSTACLE, PLAYABLE
	}
	
	public enum State {
		RUNNING, PAUSED, GAME_OVER, HEALTH_LOST, FINISHED
	}
	
	public static final float G = 6.67408f * 1e-20f;
	
	protected World world;
	protected Playable playable;
	protected GameScreen screen;
	protected Map map;
	
	protected Array<Trigger> triggers;
	protected Waypoint waypoint;
	protected Array<Planet> planets;
	protected Array<SolidObject> solidObjects;
	
	protected float timePassedReal;
	protected float timePassedFixed;
	
	protected float currentGravityForce;
	
	protected State state;
	
	protected int health = 3;
	protected Popup popup;
	protected Timer timer;
	protected ObjectiveWindow objectiveWindow;
	
	public Level() {
		this.state = State.PAUSED;
		if(Timer.instance() != null)
			Timer.instance().stop();
		
		this.world = new World(new Vector2(0, 0), true);
		
		//Default values
		this.triggers = new Array<Trigger>();
		this.planets = new Array<Planet>();
		this.solidObjects = new Array<SolidObject>();
		this.timePassedReal = 0;
		this.timePassedFixed = 0;
		this.popup = new Popup();
		this.objectiveWindow = new ObjectiveWindow();
		
		//Register collisions
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				ObjectType typeA = (ObjectType) contact.getFixtureA().getUserData();
				ObjectType typeB = (ObjectType) contact.getFixtureB().getUserData();
				
				if((typeA == ObjectType.PLANET && typeB == ObjectType.PLAYABLE) || (typeB == ObjectType.PLANET && typeA == ObjectType.PLAYABLE)) {
					planetCollision(contact);
				} else if((typeA == ObjectType.OBSTACLE && typeB == ObjectType.PLAYABLE) || (typeB == ObjectType.OBSTACLE && typeA == ObjectType.PLAYABLE)) {
					obstacleCollision(contact);
				}
				
			}
		});
	}
	
	public void resetLevel() {
		Level newWorld = LevelManager.createLevel();
		
		newWorld.setScreenReference(screen);
		
		this.world = newWorld.world;
		this.playable = newWorld.playable;
		this.map = newWorld.map;
		this.triggers = newWorld.triggers;
		this.waypoint = newWorld.waypoint;
		this.planets = newWorld.planets;
		this.timePassedReal = newWorld.timePassedReal;
		this.timePassedFixed = newWorld.timePassedFixed;
		this.currentGravityForce = newWorld.currentGravityForce;
		this.solidObjects = newWorld.solidObjects;
		this.popup = newWorld.popup;
		this.timer = newWorld.timer;
		
		screen.lookAt(playable);
		playable.update(0);
	}
	
	public void update(float deltaTime) {
		if(state == State.RUNNING) {
			timePassedReal += deltaTime;
			deltaTime = FRAME_RATE;
			timePassedFixed += deltaTime;
			
			playable.update(deltaTime);
			updateGravity();
			updateTriggers();
			updateVisualObjects(deltaTime);
			if(waypoint != null)
				waypoint.update(deltaTime);
			updatePresetOrbits();
			
			world.step(deltaTime, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
		}
	}
	
	private void updateTriggers() {
		for(Trigger trigger : triggers) {
			if(trigger.isTriggered()) {
				trigger.triggerAction();
			}
		}
	}
	
	private void updateGravity() {
		for(Planet planet : planets) {
			Body spaceship = playable.getBody();
			
			Vector2 directionVector = planet.getBody().getPosition().sub(spaceship.getPosition());
			
			float forceScalar = G * spaceship.getMass() * planet.getMass() / directionVector.len2();
			currentGravityForce = forceScalar;
			
			Vector2 forceVector = directionVector.setLength(forceScalar);
			
			spaceship.applyForceToCenter(forceVector, true);
		}
	}
	
	private void updateVisualObjects(float deltaTime) {
		for(GameObjects go : solidObjects) {
			go.update(deltaTime);
		}
	}
	
	public void updatePresetOrbits() {
		for(GameObject obj : solidObjects) {
			if ((obj instanceof SolidObject) && (((SolidObject) obj).isOrbitPreset()))
				quickPresetOrbits((SolidObject) obj, timePassedFixed);
		}
		for(GameObject obj : planets) {
			if ((obj instanceof SolidObject) && (((SolidObject) obj).isOrbitPreset()))
				quickPresetOrbits((SolidObject) obj, timePassedFixed);
		}
	}
	
	private void planetCollision(Contact contact) {
		System.out.println("planet collision");
		if(!Constants.DEBUG) 
			setState(State.HEALTH_LOST);
	}
	
	public static void presetOrbit(SolidObject orbiter, SolidObject focus, float orbitRadius, float period, float timePassed, float phase) {
		float fx = focus.getBody().getPosition().x;
		float fy = focus.getBody().getPosition().y;
		double w = 2 * Math.PI / period;
		double t = (double) (timePassed);
		float x = fx + (float) (orbitRadius * Math.cos(w * t + phase));
		float y = fy + (float) (orbitRadius * Math.sin(w * t + phase));
		orbiter.getBody().setTransform(x, y, 0f);
	}
	
	public static void quickPresetOrbits(SolidObject orbiter, float timePassed) {
		float x = ((Planet) orbiter).getPrimary().getBody().getPosition().x;
		float y = ((Planet) orbiter).getPrimary().getBody().getPosition().y;
		float w = (float) (2 * Math.PI / 3000);
		float phase = (float) (Math.PI * 2f / 3f);
		orbiter.getBody().setTransform(x + (float) (7615 * Math.cos(w * timePassed + phase)), y + (float) (7615 * Math.sin(w * timePassed + phase)), 0f);
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
		if (state == State.HEALTH_LOST) {
			healthLost();
		} else if(state == State.GAME_OVER) {
			Timer.instance().stop();
		} else if(state == State.RUNNING) {
			Timer.instance().start();
		} else if(state == State.PAUSED) {
			Timer.instance().stop();
		}
	}
	
	public void healthLost() {
		health -= 1;
		if(health == 0) {
			setState(State.GAME_OVER);
		} else {
			resetLevel();
			setState(State.RUNNING);
		}
	}
	
	public Array<Planet> getPlanets() {
		return planets;
	}
	
	public float getCurrentGravityForce() {
		return currentGravityForce;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Playable getPlayable() {
		return playable;
	}
	
	public Map getMap() {
		return map;
	}
	
	public float getTimePassedReal() {
		return timePassedReal;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setScreenReference(GameScreen screen) {
		this.screen = screen;
	}
	
	public Popup getPopup() {
		return popup;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public Waypoint getWaypoint() {
		return waypoint;
	}
	
	public void setWaypoint(Waypoint waypoint) {
		this.waypoint = waypoint;
	}
	
	public Array<Trigger> getTriggers() {
		return triggers;
	}
	
	public Array<SolidObject> getSolidObjects() {
		return solidObjects;
	}
	
	public ObjectiveWindow getObjectiveWindow() {
		return objectiveWindow;
	}
}
