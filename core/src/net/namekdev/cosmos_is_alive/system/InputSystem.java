package net.namekdev.cosmos_is_alive.system;

import static com.badlogic.gdx.Gdx.input;
import net.namekdev.cosmos_is_alive.component.base.Transform;
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

	private ActionTimer movementBlocker = new ActionTimer(C.Camera.RotationDuration);


	@Override
	protected void processSystem() {
		final float dt = world.getDelta();

		if (input.isKeyJustPressed(Keys.C)) {
			cameraSystem.freeLookEnabled = !cameraSystem.freeLookEnabled;
		}

		if (movementBlocker.update(dt) != TimerState.Active) {
			float angle = 0;
			boolean performRotation = true;

			Transform playerTransform = playerSystem.getShipTransform();

			if (input.isKeyJustPressed(Keys.UP)) {
				angle = -90;
				playerTransform.toRightDir(tmpAxis);
			}
			else if (input.isKeyJustPressed(Keys.DOWN)) {
				angle = 90;
				playerTransform.toRightDir(tmpAxis);
			}
			else if (input.isKeyJustPressed(Keys.LEFT)) {
				angle = -90;
				playerTransform.toDirection(tmpAxis);
			}
			else if (input.isKeyJustPressed(Keys.RIGHT)) {
				angle = 90;
				playerTransform.toDirection(tmpAxis);
			}
			else performRotation = false;

			if (performRotation) {
				if (playerSystem.tryRotateAroundPlanet(angle, tmpAxis)) {
					movementBlocker.start();
				}
			}
		}
	}
}
