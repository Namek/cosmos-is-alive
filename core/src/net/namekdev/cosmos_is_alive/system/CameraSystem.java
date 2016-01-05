package net.namekdev.cosmos_is_alive.system;

import net.mostlyoriginal.api.operation.common.TemporalOperation;
import net.namekdev.cosmos_is_alive.animation.TemporalDeltaOperation;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends BaseSystem {
	SchedulerSystem scheduler;
	TagManager tags;

	public @Wire PerspectiveCamera camera;

	public boolean freeLookEnabled = false;

	ActionTimer shakeTimer = new ActionTimer();

	private final Vector3 tmpScreenPos = new Vector3();
	private final Vector3 tmpAxis = new Vector3();


	@Override
	protected void initialize() {
		camera.position.set(0, 0, 5f);
		camera.far = 1000;
		camera.near = 0.1f;
		camera.update(true);
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();

		if (freeLookEnabled) {
			Gdx.input.setCursorCatched(true);
			camera.rotate(-Gdx.input.getDeltaX()*0.1f, 0, 1, 0);
			camera.rotate(getRightVector(tmpAxis), -Gdx.input.getDeltaY()*0.1f);
			camera.update();
		}
		else {
			Gdx.input.setCursorCatched(false);
		}

//		camera.position.set(offset.x, offset.y, offset.z);

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

	public void screenToWorld(Vector2 s) {
		camera.unproject(tmpScreenPos.set(s.x, s.y, 0));
		s.set(tmpScreenPos.x, tmpScreenPos.y);
	}

	public void shake(float duration) {
		shakeTimer.start(duration);
	}

	public void animateRotationBy(final float degrees, final Vector3 axis) {
		scheduler.schedule(
			new TemporalDeltaOperation(Interpolation.sine, C.Camera.RotationDuration) {
				@Override
				public void update(float percentageDelta, Entity e) {
					camera.rotate(axis, percentageDelta * degrees);
				}
			}
		);
	}

	public void animateRotationAroundBy(final Vector3 point, final float byDegrees, final Vector3 axis) {
		scheduler.schedule(
			new TemporalDeltaOperation(Interpolation.sine, C.Camera.RotationDuration) {
				final Vector3 tmpVec = new Vector3();

				@Override
				public void update(float percentageDelta, Entity e) {
					camera.rotateAround(point, axis, percentageDelta * byDegrees);
					camera.update();
				}
			}
		);
	}
}
