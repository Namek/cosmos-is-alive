package net.namekdev.cosmos_is_alive.system;

import static net.namekdev.cosmos_is_alive.system.TweenSystem.EntityTweenAccessor.*;
import static aurelienribon.tweenengine.TweenEquations.*;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.cosmos_is_alive.component.*;
import net.namekdev.cosmos_is_alive.component.render.ZOrder;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.Tags;
import net.namekdev.cosmos_is_alive.manager.AspectHelpers;
import net.namekdev.cosmos_is_alive.system.TweenSystem.EntityTweenAccessor;
import net.namekdev.cosmos_is_alive.system.TweenSystem.TimelineInitializer;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class PlayerStateSystem extends BaseSystem {
	M<Pos> mPos;
	M<Rotation> mRotation;
	M<Scale> mScale;
	M<ZOrder> mZOrder;

	AspectHelpers aspects;
	CollisionSystem collisions;
//	DepthSystem depthSystem;
	RenderSystem renderSystem;
	TagManager tags;
	TweenSystem tweens;

	Input input;

	ActionTimer walkTimer = new ActionTimer(C.Player.WalkStepDuration);


	@Override
	protected void initialize() {
		input = Gdx.app.getInput();
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();

		final Entity e = tags.getEntity(Tags.Player);
		Pos pos = mPos.get(e);
		Rotation rot = mRotation.get(e);
		Scale scale = mScale.get(e);
		ZOrder z = mZOrder.get(e);

		int horzDir = input.isKeyPressed(Keys.LEFT) || input.isKeyPressed(Keys.A) ? -1
			: input.isKeyPressed(Keys.RIGHT) || input.isKeyPressed(Keys.D) ? 1 : 0;
		int vertDir = input.isKeyPressed(Keys.DOWN) || input.isKeyPressed(Keys.S) ? -1
				: input.isKeyPressed(Keys.UP) || input.isKeyPressed(Keys.W) ? 1 : 0;

		boolean isMoving = horzDir != 0 || vertDir != 0;

		pos.x += C.Player.NormalMoveSpeed * horzDir;
		pos.y += C.Player.NormalMoveSpeed * vertDir;


		if (scale.x * horzDir > 0) {
			scale.x *= -1f;
		}

		if (isMoving) {
			if (walkTimer.update(dt) != TimerState.Active) {
				walkTimer.start();
			}

			rot.rotation = C.Player.WalkRotation * (walkTimer.isForward(0.5f) ? 1 : -1);
			z.z = (int) pos.y;
			renderSystem.dirtyOrder = true;
		}
		else {
			rot.rotation = 0;
		}

		if (input.isKeyJustPressed(Keys.SPACE)) {
			// TODO jump or whatever

			tweens.parallel(new TimelineInitializer() {
				@Override
				public void init(Timeline timeline) {
					timeline.beginSequence()
						.push(Tween.to(e, COLOR_R, 0.4f).target(0.4f).ease(easeOutSine).delay(0.2f))
						.push(Tween.to(e, COLOR_R, 0.4f).target(1f).ease(easeOutSine))
					.end()
					.beginSequence()
						.push(Tween.to(e, SCALE, 0.4f).target(0.8f, 0.8f).ease(easeOutSine).delay(0.2f))
						.push(Tween.to(e, SCALE, 0.4f).target(1f, 1f).ease(easeOutSine))
					.end();
				}
			});
		}
	}
}
