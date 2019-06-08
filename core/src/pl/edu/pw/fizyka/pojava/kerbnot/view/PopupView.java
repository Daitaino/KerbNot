package pl.edu.pw.fizyka.pojava.kerbnot.view;

/**
 * @author: Ma³gorzata
 * this class is used to show popup window
 */

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import pl.edu.pw.fizyka.pojava.kerbnot.model.Popup;
import pl.edu.pw.fizyka.pojava.kerbnot.util.GamePreferences;


public class PopupView {

	private Popup popup;
	private OrthographicCamera camera;
	private BitmapFont font;
	private TweenManager tweenManager;
	private float yCoord;
	private float startingYCoord;
	private float elapsedTime;
	private float customDelay;
	private Tween tween;
	private Music popupShutter;
	
	public PopupView (Popup popup, OrthographicCamera camera) {
		this.popup = popup;
		this.camera = camera;
		this.font = new BitmapFont();
		this.startingYCoord = -AssetManager.POPUP_BODY.getHeight();
		this.yCoord = startingYCoord;
		this.elapsedTime = 0;
		this.customDelay = -1;
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(PopupView.class, new PopupAccessor());
		
		this.popupShutter = AssetManager.POPUP_SHUTTER_1;
	}
	
	public PopupView(Popup popup, OrthographicCamera camera, float delay) {
        this(popup, camera);
        this.customDelay = delay;
    }
	
	public void update (float deltaTime) {
		elapsedTime += deltaTime;
		tweenManager.update(deltaTime);
		
		float origY = yCoord;
		
		if (popup.isPropertyChanged()) {
			popup.setPropertyChanged(false);
			elapsedTime = 0;
			
			if (tween != null && !tween.isFinished()) {
				tweenManager.killAll();
			}
			
			Tween.set(this, PopupAccessor.Y_COORD).target(origY).start(tweenManager);
			tween = Tween.to(this, PopupAccessor.Y_COORD, 2).target(0).start(tweenManager);
			playPopupOpener();
			playPopupShutter();
		}
		
		float delay;
		if (customDelay == -1) {
			delay = 2 + popup.getLastText().length() * 0.1f;
		} else {
			delay = customDelay;
		}
		
		if (tween != null && yCoord == 0 && elapsedTime >= delay) {
			Tween.set(this, PopupAccessor.Y_COORD).target(0).start(tweenManager);
			tween = Tween.to(this, PopupAccessor.Y_COORD, 2).target(startingYCoord).start(tweenManager);
			stopPopupShutter();
		}
	}
	
	public void draw (SpriteBatch batch) {
		Texture popupTexture = AssetManager.POPUP_BODY;
		batch.draw(
				popupTexture,
				camera.position.x - (camera.viewportWidth / 2f) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - yCoord) * camera.zoom,
                0,
                0,
                popupTexture.getWidth(),
                popupTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                popupTexture.getWidth(),
                popupTexture.getHeight(),
                false,
                false
		);
		
		Texture headTexture = AssetManager.POPUP_HEAD;
		batch.draw(
				headTexture,
				camera.position.x - (camera.viewportWidth / 2f) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - popupTexture.getHeight() - yCoord) * camera.zoom,
                0,
                0,
                headTexture.getWidth(),
                headTexture.getHeight(),
                camera.zoom,
                camera.zoom,
                0,
                0,
                0,
                headTexture.getWidth(),
                headTexture.getHeight(),
                false,
                false
		);
		
		font.getData().setScale(camera.zoom);
		drawFont(batch);
	}
	
	private void drawFont (SpriteBatch batch) {
		String[] splitted = splitText(popup.getText(), 235 * camera.zoom, 140 * camera.zoom);
		String topText = splitted[0];
		String bottomText = splitted[1];
		
		font.draw(
                batch,
                topText,
                camera.position.x - (camera.viewportWidth / 2f - 160) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 280 - yCoord) * camera.zoom,
                235 * camera.zoom,
                Align.left,
                true
        );
		
		font.draw(
                batch,
                bottomText,
                camera.position.x - (camera.viewportWidth / 2f - 20) * camera.zoom,
                camera.position.y - (camera.viewportHeight / 2f - 280 - yCoord + 140) * camera.zoom,
                355 * camera.zoom,
                Align.left,
                true
        );
	}
	
	private String[] splitText (String text, float width, float height) {
		String topText = text;
		
		GlyphLayout bound = new GlyphLayout(font, text);
		
		while (bound.height > height) {
			topText = topText.substring(0, topText.lastIndexOf(' '));
			bound.setText(font, topText);
		}
		
		String bottomText = text.substring(topText.length());
		
		return new String[] {topText, bottomText};
	}
	
	public void playPopupOpener(){
        AssetManager.POPUP_OPENER.play(GamePreferences.getInstance().getMasterVolume()/2f);
    }
	
	public void playPopupShutter(){
        popupShutter.setVolume(GamePreferences.getInstance().getMasterVolume() /13f );
        popupShutter.setLooping(true);
        popupShutter.play();
    }
	
	public void stopPopupShutter(){
        popupShutter.stop();
    }
	
	public float getYCoord() {
        return yCoord;
    }
	
	public void setYCoord(float yCoord) {
        this.yCoord = yCoord;
    }
	
	
}
