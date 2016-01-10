package net.namekdev.cosmos_is_alive.system;

import static com.badlogic.gdx.Gdx.input;
import static net.namekdev.cosmos_is_alive.action.base.Operations.*;
import net.namekdev.cosmos_is_alive.action.base.TemporalDeltaOperation.DeltaExecutor;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;
import net.namekdev.cosmos_is_alive.util.MixedProjectionCamera;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends BaseSystem {
	ComponentMapper<Transform> mTransform;

	PlayerStateSystem playerSystem;
	TagManager tags;

	public @Wire MixedProjectionCamera camera;

	public boolean freeLookEnabled = false;

	ActionTimer shakeTimer = new ActionTimer();

	private final Vector3 tmpScreenPos = new Vector3();
	private final Vector3 tmpAxis = new Vector3();
	private final Vector3 tmpDir = new Vector3();
	private final Vector3 tmpDist = new Vector3();
	private final Vector3 tmpPos = new Vector3();


	@Override
	protected void initialize() {
		camera.far = 1000;
		camera.near = 0.1f;
		camera.zoom = 0.03f;
		camera.perspectiveFactor = C.Camera.MinPerspective;
		camera.fieldOfView = 30;
		camera.update();
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();

		if (freeLookEnabled) {
			input.setCursorCatched(true);
			camera.rotate(-Gdx.input.getDeltaX()*0.1f, 0, 1, 0);
			camera.rotate(getRightVector(tmpAxis), -Gdx.input.getDeltaY()*0.1f);

			if (input.isKeyPressed(Keys.W)) camera.translate(camera.direction);
			if (input.isKeyPressed(Keys.S)) camera.translate(tmpDir.set(camera.direction).scl(-1));
			if (input.isKeyPressed(Keys.A)) camera.translate(getRightVector(tmpDir).scl(-1));
			if (input.isKeyPressed(Keys.D)) camera.translate(getRightVector(tmpDir));

			// align direction to closest axis
			if (input.isKeyJustPressed(Keys.E)) {
				double angleX = Math.acos(camera.direction.dot(Vector3.X));
				double angleY = Math.acos(camera.direction.dot(Vector3.Y));
				double angleZ = Math.acos(camera.direction.dot(Vector3.Z));

				double smallest = angleX;
				Vector3 axis = Vector3.X;
				if (angleY < smallest) {
					smallest = angleY;
					axis = Vector3.Y;
				}
				if (angleZ < smallest) {
					smallest = angleZ;
					axis = Vector3.Z;
				}

				camera.direction.set(axis);
			}

			camera.update();
		}
		else {
			input.setCursorCatched(false);

			// While player is alive update camera position with the ship.
			if (playerSystem.isPlayerAlive()) {
				playerSystem.getShipUpVector(tmpDir);
				tmpDist.set(tmpDir).scl(C.Camera.DistanceToPlayer);
				camera.zoom = C.Camera.OrthoZoom;
				playerSystem.getShipPosition(tmpPos);
				camera.position.set(tmpPos).add(tmpDist);
				camera.lookAt(tmpPos);;
				camera.update();
				playerSystem.getShipDirection(camera.up);
				camera.update();
			}
		}

		if (input.isKeyPressed(Keys.Z)) {
			camera.perspectiveFactor -= 0.01f;
		}
		else if (input.isKeyPressed(Keys.X)) {
			camera.perspectiveFactor += 0.01f;
		}

		camera.perspectiveFactor = MathUtils.clamp(
			camera.perspectiveFactor, C.Camera.MinPerspective, C.Camera.MaxPerspective
		);

		// shake camera
		if (shakeTimer.update(dt) == TimerState.Active) {
			float w2 = (C.Camera.ShakeWidthMax - C.Camera.ShakeWidthMin)/2;
			float h2 = (C.Camera.ShakeHeightMax - C.Camera.ShakeHeightMin)/2;
			camera.position.x += MathUtils.random(C.Camera.ShakeWidthMin, C.Camera.ShakeWidthMax) - w2;
			camera.position.y += MathUtils.random(C.Camera.ShakeHeightMin, C.Camera.ShakeHeightMax) - h2;
		}

		camera.update();
	}

	public Vector3 getUpVector(Vector3 outUp) {
		camera.update();
		return outUp.set(camera.up);
	}

	public Vector3 getRightVector(Vector3 outRight) {
		camera.update();
		return outRight.set(camera.direction).crs(camera.up);
	}

	public void worldToScreen(Vector2 w) {
		camera.project(tmpScreenPos.set(w.x, w.y, 0));
		w.set(tmpScreenPos.x, tmpScreenPos.y);
	}

	public void worldToScreen(Vector3 w) {
		camera.project(w);
	}

	public void screenToWorld(Vector2 s) {
		camera.unproject(tmpScreenPos.set(s.x, s.y, 0));
		s.set(tmpScreenPos.x, tmpScreenPos.y);
	}

	public void screenToWorld(Vector3 s) {
		camera.unproject(s);
	}

	public void shake(float duration) {
		shakeTimer.start(duration);
	}

	public void animateRotationBy(final float degrees, final Vector3 axis) {
		deltaOperation(C.Camera.RotationDuration, Interpolation.sine, new DeltaExecutor() {
			@Override
			public void act(float deltaTime, float deltaPercent) {
				camera.rotate(axis, deltaPercent * degrees);
			}
		}).register(world);
	}
}
