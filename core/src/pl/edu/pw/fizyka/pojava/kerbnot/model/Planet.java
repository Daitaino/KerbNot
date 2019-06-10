package pl.edu.pw.fizyka.pojava.kerbnot.model;

/**
 * @author Filip
 */

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Planet extends CelestialObject {
	
	private Planet primary;
	
	private int planetType;
	
	public Planet(float x, float y, float mass, float radius, Planet primary, World world, int planetType) {
		super(mass, radius);
		this.primary = primary;
		this.planetType = planetType;
		this.body = createBody(x, y, mass, radius, world);
	}
	
	private Body createBody(float x, float y, float mass, float radius, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(x, y);
		
		Body body = world.createBody(bodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.restitution = 0.0f;
		fixtureDef.friction = 10000f;
		
		Fixture fixture = body.createFixture(fixtureDef);
		
		fixture.setUserData(Level.ObjectType.PLANET);
		
		circle.dispose();
		
		return body;
	}
	
	@Override
	public void update(float dt) {
	}
	
	public int getPlanetType() {
		return planetType;
	}
	
	public void setPlanetType(int planetType) {
		this.planetType = planetType;
	}
	
	public Planet getPrimary() {
		return primary;
	}
	
	public void setPrimary(Planet primary) {
		this.primary = primary;
	}
}

