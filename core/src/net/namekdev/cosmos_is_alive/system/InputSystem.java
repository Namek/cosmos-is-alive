package net.namekdev.cosmos_is_alive.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class InputSystem extends BaseSystem {
	CameraSystem cameraSystem;

	@Override
	protected void processSystem() {
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			cameraSystem.freeLookEnabled = !cameraSystem.freeLookEnabled;
		}
	}
}
