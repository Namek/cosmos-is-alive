package net.namekdev.cosmos_is_alive.system;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
//import net.namekdev.cosmos_is_alive.component.Pos;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.Tags;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraSystem extends BaseSystem {
//	M<Pos> mPos;
	TagManager tags;
	
	@Wire PerspectiveCamera camera;

	ActionTimer shakeTimer = new ActionTimer();
	
	private final Vector2 worldCenterPos = new Vector2();
//	private final Vector2 worldSize = new Vector2(C.World.Width, C.World.Height);
	private final Vector2 offset = new Vector2();

	private final Vector2 playerScreenPos = new Vector2();
	private final Vector3 tmpScreenPos = new Vector3();
	private final Vector2 tmpScreenPos2 = new Vector2();
	

	@Override
	protected void initialize() {
		worldCenterPos.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f);
		offset.set(worldCenterPos);
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();
		
		// position camera by player
		Entity e = tags.getEntity(Tags.Player);
//		Pos pos = mPos.get(e);
		/*
		worldToScreen(playerScreenPos.set(pos.x, pos.y));
		float thresholdLeft = C.Camera.MoveThresholdScreenHorzSizePercent * camera.viewportWidth;
		float thresholdRight = (1f - C.Camera.MoveThresholdScreenHorzSizePercent) * camera.viewportWidth;
		float thresholdTop = (1f - C.Camera.MoveThresholdScreenVertSizePercent) * camera.viewportHeight;
		float thresholdBottom = C.Camera.MoveThresholdScreenVertSizePercent * camera.viewportHeight;

		if (playerScreenPos.x < thresholdLeft) {
			offset.x -= (thresholdLeft - playerScreenPos.x);
		}
		else if (playerScreenPos.x > thresholdRight) {
			offset.x -= (thresholdRight - playerScreenPos.x);
		}
		
		if (playerScreenPos.y < thresholdBottom) {
			offset.y -= (thresholdBottom - playerScreenPos.y);
		}
		else if (playerScreenPos.y > thresholdTop) {
			offset.y -= (thresholdTop - playerScreenPos.y);
		}

		camera.position.set(offset.x, offset.y, 0);
		camera.update();
		worldToScreen(tmpScreenPos2.set(worldSize));

		if (tmpScreenPos2.x < camera.viewportWidth) {
			offset.x += (tmpScreenPos2.x - camera.viewportWidth);
		}
		if (tmpScreenPos2.y < camera.viewportHeight) {
			offset.y += (tmpScreenPos2.y - camera.viewportHeight);
		}

		camera.position.set(offset.x, offset.y, 0);
		camera.update();
		worldToScreen(tmpScreenPos2.set(0, 0));

		if (tmpScreenPos2.x > 0) {
			offset.x += tmpScreenPos2.x;
		}
		if (tmpScreenPos2.y > 0) {
			offset.y += tmpScreenPos2.y;
		}
*/

		camera.position.set(offset.x, offset.y, 0);

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
