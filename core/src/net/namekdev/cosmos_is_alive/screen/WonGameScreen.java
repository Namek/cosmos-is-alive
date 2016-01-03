package net.namekdev.cosmos_is_alive.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class WonGameScreen extends BaseScreen<WonGameScreen> {
	private Runnable exitCallback;

	public WonGameScreen(Runnable exitCallback) {
		this.exitCallback = exitCallback;
	}
	
	@Override
	public void render(float delta) {
		darkenBackground();
		
		sprites.begin();
		// TODO
//		float x = (sw() - w) / 2;
//		float y = (sh() - h) / 2;
//		batch.draw(whatever, x, y)
		sprites.end();
		
		if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			popScreen();

			if (exitCallback != null) {
				exitCallback.run();
			}
		}
	}
}
