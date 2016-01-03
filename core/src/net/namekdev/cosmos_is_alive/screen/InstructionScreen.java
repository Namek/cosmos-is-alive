package net.namekdev.cosmos_is_alive.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class InstructionScreen extends BaseScreen<InstructionScreen> {
	Runnable exitCallback;
	
	public InstructionScreen(Runnable exitCallback) {
		this.exitCallback = exitCallback;
	}
	
	@Override
	public void render(float delta) {
		darkenBackground();

		sprites.begin();
		float x = (sw() - assets.instruction.getRegionWidth()) / 2;
		float y = (sh() - assets.instruction.getRegionHeight()) / 2;
		sprites.draw(assets.instruction, x, y);
		sprites.end();

		if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			popScreen();
			
			if (exitCallback != null) {
				exitCallback.run();
			}
		}
	}
}
