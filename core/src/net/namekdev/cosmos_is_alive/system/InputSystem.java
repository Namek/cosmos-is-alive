package net.namekdev.cosmos_is_alive.system;

import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

public class InputSystem extends BaseSystem {
	CameraSystem cameraSystem;
	PlayerStateSystem playerSystem;

	private final Vector3 tmpAxis = new Vector3();
	private final Vector3 playerPosition = new Vector3();

	private ActionTimer rotationBlocker = new ActionTimer(C.Camera.RotationDuration);


	@Override
	protected void processSystem() {
		final float dt = world.getDelta();

		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			cameraSystem.freeLookEnabled = !cameraSystem.freeLookEnabled;
		}

		if (rotationBlocker.update(dt) != TimerState.Active) {
			float angle = 0;
			boolean performRotation = true;

			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				angle = 90;
				cameraSystem.getRightVector(tmpAxis);
			}
			else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
				angle = -90;
				cameraSystem.getRightVector(tmpAxis);
			}
			else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
				angle = -90;
				cameraSystem.getUpVector(tmpAxis);
			}
			else performRotation = false;

			if (performRotation) {
				playerSystem.getPlayerPosition(playerPosition);
				cameraSystem.animateRotationAroundBy(playerPosition, angle, tmpAxis);
				rotationBlocker.start();
			}
		}
	}
}
