package me.johnexists.game1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.johnexists.game1.logic.GameLogic;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920 / 2;
		config.height = 1080 / 2;
		config.title = "Laser Storm II";
		config.fullscreen = true;
		new LwjglApplication(new GameLogic(), config);
	}
}
