package pl.edu.pw.fizyka.pojava.kerbnot.model;


/**
 * 
 * @author Filip
 * Class for all celestials objects (planets etc.)
 * 
 *
 */
public class CelestialObject extends SolidObject{

	protected float mass;
	protected float radius;
	
	public CelestialObject(float mass, float radius) {
		this.mass = mass;
		this.radius = radius;
	}
	
	public float getMass() {
		return mass;
	}
	
	public float getRadius() {
		return radius;
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

}
