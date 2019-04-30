package pl.pojava.kerbnot.model;

import com.badlogic.gdx.math.Vector2;

public class PositionTrigger extends Trigger {
	
	private float x;
	private float y;
	private float radius;
	
	private SolidObject target;
	private SolidObject host;
	
	private boolean reverse;
	
	public PositionTrigger(boolean runOnce, SolidObject host, float x, float y, float radius, SolidObject target, boolean reverse) {
		super(runOnce);
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.target = target;
		this.host = host;
		this.reverse = reverse;
	}
	
	public PositionTrigger(boolean runOnce, float x, float y, float radius, SolidObject target, boolean reverse) {
		this(runOnce, null, x, y, radius, target, reverse);
	}
	
	public PositionTrigger(boolean runOnce, SolidObject host, float x, float y, float radius, SolidObject target) {
		this(runOnce, host, x, y, radius, target, false);
	}
	
	public PositionTrigger(boolean runOnce, float x, float y, float radius, SolidObject target) {
		this(runOnce, null, x, y, radius, target, false);
	}
	
	public PositionTrigger(SolidObject host, float x, float y, float radius, SolidObject target, boolean reverse) {
		this(true, host, x, y, radius, target, reverse);
	}
	
	public PositionTrigger(float x, float y, float radius, SolidObject target, boolean reverse) {
		this(true, null, x, y, radius, target, reverse);
	}
	
	public PositionTrigger(SolidObject host, float x, float y, float radius, SolidObject target) {
		this(true, host, x, y, radius, target, false);
	}
	
	public PositionTrigger(float x, float y, float radius, SolidObject target) {
		this(true, null, x, y, radius, target, false);
	}
	
	@Override
	protected boolean isTriggeredInternal() {
		Vector2 pos = getPosition();
		
		return pos.dst(target.getBody().getPosition()) <= radius ^ reverse;
	}
	
	@Override
	public void triggerAction() {
		
	}
	
	public Vector2 getPosition() {
		if (host == null) {
			return new Vector2(x, y);
		}
		return new Vector2(x + host.getBody().getPosition().x, y + host.getBody().getPosition().y);
	}
	
	public float getRadius() {
		return radius;
	}
}
