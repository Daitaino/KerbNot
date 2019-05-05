package pl.pojava.kerbnot.view;

/**
 * @author: Ma³gorzata
 * 
 */

import aurelienribon.tweenengine.TweenAccessor;

public class PopupAccessor implements TweenAccessor<PopupView>{
	
	public static final int Y_COORD = 0;

	@Override
	public int getValues(PopupView target, int tweenType, float[] returnValues) {
		// TODO Auto-generated method stub
		switch (tweenType) {
			case Y_COORD:
				returnValues[0] = target.getYCoord();
				return 1;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(PopupView target, int tweenType, float[] newValues) {
		// TODO Auto-generated method stub
		switch(tweenType) {
			case Y_COORD:
				target.setYCoord(newValues[0]);
				break;
			default:
				assert false;
		}
		
	}

}
