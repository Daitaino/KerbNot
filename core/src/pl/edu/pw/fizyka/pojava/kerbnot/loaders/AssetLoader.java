package pl.edu.pw.fizyka.pojava.kerbnot.loaders;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class AssetLoader {

	public static final Skin SKIN= new Skin(Gdx.files.internal("skin/tracer-ui.json"));
	public static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("background/menuBackground.jpg"));
	public static final Sprite BACKGROUND_SPRITE = new Sprite(BACKGROUND_TEXTURE);
	


}
