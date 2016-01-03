package net.namekdev.cosmos_is_alive;

import java.util.Stack;

import net.namekdev.cosmos_is_alive.screen.BaseScreen;
import net.namekdev.cosmos_is_alive.screen.GameScreen;
import net.namekdev.cosmos_is_alive.screen.InstructionScreen;
import net.namekdev.cosmos_is_alive.screen.IntroScreen;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class MyNGame extends ApplicationAdapter {
	private Stack<BaseScreen<?>> screenStack = new Stack<BaseScreen<?>>();
	private GameScreen gameScreen;
	private Assets assets;

	
	@Override
	public void create() {
		assets = new Assets();
		assets.loadAll();
		gameScreen = new GameScreen().init(this);

		pushScreen(gameScreen);

		gameScreen.render(0f);
		pushScreen(new IntroScreen(new Runnable() {
			public void run() {
				pushScreen(new InstructionScreen(null).init(MyNGame.this));
			}
		}).init(this));
	}

	@Override
	public void render() {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				Gdx.app.exit();
			}
		}
		
		gameScreen.isPaused = screenStack.size() > 1;

		float dt = Math.min(1/15f, Gdx.graphics.getDeltaTime());
		for (int i = 0, n = screenStack.size(); i < n; ++i) {
			screenStack.get(i).render(dt);
		}
	}

	public void popScreen(net.namekdev.cosmos_is_alive.screen.BaseScreen<?> screen) {
		if (screenStack.peek() != screen) {
			throw new RuntimeException("Popping another screen.");
		}
		
		screenStack.pop();
		screen.dispose();
	}
	
	public void pushScreen(BaseScreen<?> screen) {
		screenStack.push(screen);
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
	public Assets getAssets() {
		return assets;
	}
}
