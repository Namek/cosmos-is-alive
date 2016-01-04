package net.namekdev.cosmos_is_alive.system;

import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends BaseSystem {
	TagManager tags;
	
	@Wire PerspectiveCamera camera;
	
	public boolean freeLookEnabled = false;

	ActionTimer shakeTimer = new ActionTimer();

	private final Vector3 offset = new Vector3();
	private final Vector3 tmpScreenPos = new Vector3();
	

	@Override
	protected void initialize() {
		Gdx.input.setCursorCatched(true);
		offset.set(0, 0, 3);
		camera.far = 100;
		camera.near = 0.1f;
		camera.update(true);
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();

		
		if (freeLookEnabled) {
			camera.rotate(Gdx.input.getDeltaX()*0.1f, 0, 1, 0);
			camera.rotate(Gdx.input.getDeltaY()*0.1f, 1, 0, 0);
			camera.update();
		}

		camera.position.set(offset.x, offset.y, offset.z);

		// shake camera
		if (shakeTimer.update(dt) == TimerState.Active) {
			float w2 = (C.Camera.ShakeWidthMax - C.Camera.ShakeWidthMin)/2;
			float h2 = (C.Camera.ShakeHeightMax - C.Camera.ShakeHeightMin)/2;
			camera.position.x += MathUtils.random(C.Camera.ShakeWidthMin, C.Camera.ShakeWidthMax) - w2;
			camera.position.y += MathUtils.random(C.Camera.ShakeHeightMin, C.Camera.ShakeHeightMax) - h2;
		}
		
		camera.update();
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
}
