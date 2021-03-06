package de.zcience;

import java.util.HashMap;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.zcience.z1.screens.MainMenuScreen;
import de.zcience.z1.screens.game.GameScreen;

public class ZApplication extends Game {
	private SpriteBatch batch;

	private AssetManager assetManager;

	private HashMap<String, Screen> screens = new HashMap<String, Screen>();
	
	private FPSLogger fpsLogger = new FPSLogger();

	@Override
	public void create() {
		// Setting LogLevel. For now: Log everything
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// set clear color
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		this.batch = new SpriteBatch();
		this.assetManager = new AssetManager();
		// add Inputprocessor

		// TODO: Create an AssetManager, create a Loader Class for Screens to
		// make everything Data-Driven

		// add Main Menu
		MainMenuScreen mainMenu = new MainMenuScreen(this);
		screens.put("MenuScreen", mainMenu);

		// add Game Screen
		GameScreen gameScreen = new GameScreen(this);
		screens.put("GameScreen", gameScreen);

		this.setScreen(mainMenu);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(); // Important! Needed, because we're extending the gdx
						// Game class
		fpsLogger.log();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	public void switchToScreen(String screenName) {
		Screen toSwitch = screens.get(screenName);
		if (null == toSwitch) {
			Gdx.app.debug("Error", "Tried to switch to a screen which wasn't added to the ZApplication");
		} else {
			Gdx.app.log("Screen Switch", "Switched to screen: " + screenName);
			this.setScreen(screens.get(screenName));
		}
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

}
