package pl.pojava.kerbnot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import pl.edu.pw.fizyka.pojava.kerbnot.KerbNot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1200;
		new LwjglApplication(new KerbNot(), config);
	}
}
