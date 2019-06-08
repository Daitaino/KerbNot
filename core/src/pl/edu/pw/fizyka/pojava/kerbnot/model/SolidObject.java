package pl.edu.pw.fizyka.pojava.kerbnot.model;

import com.badlogic.gdx.physics.box2d.Body;


/**
 * 
 * @author Filip
 * Class for all solid objects.
 */
public abstract class SolidObject extends GameObject {
	
	protected boolean orbitPreset = false;
	
	protected Body body;
	
	public Body getBody() {
		return body;
	}
	
	public boolean isOrbitPreset() {
		return orbitPreset;
	}
	
	public void setOrbitPreset(boolean orbitPreset) {
		this.orbitPreset = orbitPreset;
	}

}
