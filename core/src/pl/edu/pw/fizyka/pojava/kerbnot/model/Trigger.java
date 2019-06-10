package pl.edu.pw.fizyka.pojava.kerbnot.model;

/** 
 * @author Filip
 */

public abstract class Trigger {
	
	protected boolean runOnce;
	
	protected boolean isTriggeredBefore;
	
	public Trigger(boolean runOnce) {
		this.runOnce = runOnce;
		this.isTriggeredBefore = false;
	}
	
	public Trigger() {
		this.runOnce = true;
		this.isTriggeredBefore = false;
	}
	
	public boolean isTriggered() {
		if (runOnce && isTriggeredBefore) {
			return false;
		}
		
		boolean result = isTriggeredInternal();
		if (result) {
			isTriggeredBefore = true;
		}
		return result;
	}
	
	public boolean isRunOnce() {
		return runOnce;
	}
	
	public boolean isTriggeredBefore() {
		return isTriggeredBefore;
	}
	
	protected abstract boolean isTriggeredInternal();
	
	public abstract void triggerAction();
}
