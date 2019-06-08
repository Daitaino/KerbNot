package pl.edu.pw.fizyka.pojava.kerbnot.model;

import static pl.edu.pw.fizyka.pojava.kerbnot.util.Constants.toMeter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import pl.edu.pw.fizyka.pojava.kerbnot.util.*;

public class Playable extends SolidObject {
	
	private static final float FUEL_SPECIFIC_IMPULSE = 3500;
	private static final float MAX_VELOCITY = 1200;
	
	public static final float BASE = 1 * 1e3f;
	
	private float currentThrust;
	private float deltaTorque;
	private float deltaThrust;
	
	private float fuelLeft;
	private float startingFuel;
	private float width;
	private float height;
	
	private float maxThrust;
	
	private boolean SASEnabled;
	
	private boolean maximizeThrust;
	private boolean minimizeThrust;
	
	private Vector2 bottomPosition;
	private Vector2 spawnPoint;
	
	public Playable(float x, float y, float width, float height, float dryMass, float deltaTorque, float deltaThrust, float maxThrust, float fuel, World world) {
		
		this.currentThrust = 0;
		this.width = width;
		this.height = height;
		this.deltaTorque = deltaTorque;
		this.deltaThrust = deltaThrust;
		this.fuelLeft = fuel;
		this.maxThrust = maxThrust;
		this.SASEnabled = false;
		this.maximizeThrust = false;
		this.startingFuel = fuel;
		
		this.body = createBody(x, y, (dryMass + fuel), world);
		
		this.spawnPoint = body.getPosition().cpy();
	}
	
	private Body createBody(float x, float y, float mass, World world) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(Gdx.graphics.getWidth() / 2f * toMeter, Gdx.graphics.getHeight() / 2f * toMeter);
		
		Body body = world.createBody(bodyDef);
		
		body.setTransform(x, y, -90 * MathUtils.degreesToRadians);
		
		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(width / 2f * toMeter, height / 2f * toMeter);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = rectangle;
		fixtureDef.density = mass / (width * toMeter * height * toMeter);
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		
		Fixture fixture = body.createFixture(fixtureDef);
		
		fixture.setUserData(Level.ObjectType.PLAYABLE);
		
		rectangle.dispose();
		
		return body;
	}
	
	@Override
	public void update(float deltaTime) {
		move(deltaTime);
		consumeFuel(deltaTime);
	}
	
	private void consumeFuel(float deltaTime) {
		if (fuelLeft > 0) {
			float fuelSpent = currentThrust * deltaTime / FUEL_SPECIFIC_IMPULSE;
			fuelLeft -= fuelSpent;
			
			GameUtils.changeMass(body, body.getMass() - fuelSpent);
		}
	}
	
	private void move(float deltaTime) {
		Vector2 bottomVector = new Vector2(0, -height / 2f * toMeter).rotateRad(body.getAngle());
		bottomPosition = bottomVector.add(body.getPosition());
		
		if (fuelLeft <= 0) 
			currentThrust = 0;
		
		if (minimizeThrust) {
			maximizeThrust = false;
			minimizeThrust(deltaTime);
			if (currentThrust == 0)
				minimizeThrust = false;
		}
		
		if (maximizeThrust) {
			minimizeThrust = false;
			maximizeThrust(deltaTime);
			if (currentThrust >= maxThrust)
				maximizeThrust = false;
		}
		
		Vector2 impulseVector = new Vector2(0, currentThrust * deltaTime).rotateRad(body.getAngle());
		body.applyLinearImpulse(impulseVector.x, impulseVector.y, bottomPosition.x, bottomPosition.y, true);
	}
	
	public void turnLeft(float deltaTime) {
		body.applyAngularImpulse(deltaTorque * deltaTime, true);
	}
	
	public void turnRight(float deltaTime) {
		body.applyAngularImpulse(-deltaTorque * deltaTime, true);
	}
	
	public void toggleSAS() {
		SASEnabled = !SASEnabled;
	}
	
	public void runSAS(float deltaTime) {
		float spin = this.getBody().getAngularVelocity();
		if(SASEnabled) {
			if (spin > 0f) {
				if(spin > 0.0075f)
					turnRight(deltaTime);
				else 
					this.body.setAngularVelocity(0);
			} else if (spin < 0f)
				if (spin < -0.0075f)
					turnLeft(deltaTime);
				else 
					this.body.setAngularVelocity(0);
		}
	}
	
	public void increaseThrust(float deltaTime) {
		if (fuelLeft > 0) {
			currentThrust = Math.min(currentThrust + deltaTime * 2 * deltaThrust, maxThrust);
		}
	}
	
	public void decreaseThrust(float deltaTime) {
		currentThrust = Math.max(0, currentThrust - deltaTime * 2 * deltaThrust);
	}
	
	public void toggleMaximizeThrust() {
		maximizeThrust = !maximizeThrust;
	}
	
	public void toggleMinimizeThrust() {
		minimizeThrust = !minimizeThrust;
	}
	
	public void maximizeThrust(float deltaTime) {
		if (fuelLeft > 0) {
			currentThrust = Math.min(maxThrust, currentThrust + deltaTime * deltaThrust * 10);
		}
	}
	
	public void minimizeThrust(float deltaTime) {
		currentThrust = Math.max(0, currentThrust - deltaTime * deltaThrust * 10);
	}
	
	public float getCurrentThrust() {
		return currentThrust;
	}
	
	public float getDeltaTorque() {
		return deltaTorque;
	}
	
	public float getDeltaThrust() {
		return deltaThrust;
	}
	
	public float getFuelLeft() {
		return fuelLeft;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getMaxThrust() {
		return maxThrust;
	}
	
	public Vector2 getBottomPosition() {
		return bottomPosition;
	}
	
	public boolean getSASEnabled() {
		return SASEnabled;
	}
	
	public Vector2 getSpawnPoint() {
		return spawnPoint;
	}
	
	public void setCurrentThrust(float currentThrust) {
		this.currentThrust = currentThrust;
	}
	
	public void setFuelLeft(float fuel) {
		this.fuelLeft = fuel;
	}
	
	public void setSASEnabled(boolean set) {
		SASEnabled = set;
	}
	
	public float getStartingFuel() {
		return startingFuel;
	}
	;
	public float getMaxVelocity() {
		return MAX_VELOCITY;
	}
}
