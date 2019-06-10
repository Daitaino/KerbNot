package pl.edu.pw.fizyka.pojava.kerbnot.model;

/**
 * @author Filip
 */

import com.badlogic.gdx.math.Vector2;

public class Map {
	
	private int width;
	private int height;
	
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getRadius() {
		return (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / 2f;
	}
	
	public Vector2 getCenter() {
		return new Vector2(width / 2f, height / 2f);
	}
}

