package net.namekdev.cosmos_is_alive.system;

import static se.feomedia.orion.OperationFactory.*;
import static net.namekdev.cosmos_is_alive.action.base.Operations.*;
import static net.namekdev.cosmos_is_alive.system.TweenSystem.EntityTweenAccessor.*;
import static aurelienribon.tweenengine.TweenEquations.*;
import static com.badlogic.gdx.Gdx.input;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.cosmos_is_alive.action.base.TemporalDeltaOperation;
import net.namekdev.cosmos_is_alive.action.base.TemporalOperation;
import net.namekdev.cosmos_is_alive.action.base.TemporalDeltaOperation.DeltaExecutor;
import net.namekdev.cosmos_is_alive.action.base.TemporalOperation.SimpleExecutor;
import net.namekdev.cosmos_is_alive.component.*;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.ZOrder;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.Tags;
import net.namekdev.cosmos_is_alive.manager.AspectHelpers;
import net.namekdev.cosmos_is_alive.system.base.collision.CollisionDetectionSystem;
import net.namekdev.cosmos_is_alive.system.base.render.RenderSystem;
import net.namekdev.cosmos_is_alive.util.ActionTimer;
import net.namekdev.cosmos_is_alive.util.ActionTimer.TimerState;
import net.namekdev.cosmos_is_alive.util.MixedProjectionCamera;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class PlayerStateSystem extends BaseSystem {
	ComponentMapper<Transform> mTransform;

	AspectHelpers aspects;
	CameraSystem cameraSystem;
	CollisionDetectionSystem collisions;
	RenderSystem renderSystem;
	TagManager tags;
	TweenSystem tweens;

	private final Vector3 dir = new Vector3();

	private int nearPlanetId = -1;


	@Override
	protected void initialize() {
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();
		final Entity e = tags.getEntity(Tags.Player);
		Transform transform = mTransform.get(e);


		if (!cameraSystem.freeLookEnabled) {
			if (input.isKeyPressed(Keys.W)) {
				transform.moveForward(C.Player.NormalSpeed * dt);
			}
//			else if (input.isKeyPressed(Keys.A))
		}
	}

	public Entity getPlayer() {
		return tags.getEntity(Tags.Player);
	}

	public Transform getShipTransform() {
		return mTransform.get(getPlayer());
	}

	public Vector3 getShipPosition(Vector3 outPosition) {
		Transform transform = mTransform.get(getPlayer());
		return outPosition.set(transform.currentPos);
	}

	public Vector3 getShipDirection(Vector3 outDir) {
		Transform transform = mTransform.get(getPlayer());
		return transform.toDirection(outDir);
	}

	public Vector3 getShipUpVector(Vector3 outUp) {
		Transform transform = mTransform.get(getPlayer());
		return transform.toUpDir(outUp);
	}

	public boolean isPlayerAlive() {
		return true;
	}

	public boolean tryRotateAroundPlanet(final float byDegrees, final Vector3 axis) {
//		if (nearPlanetId < 0) {
//			return false;
//		}

		animateRotationAroundPlanet(byDegrees, axis);
		return true;
	}


	private void animateRotationAroundPlanet(final float byDegrees, final Vector3 axis) {
		final MixedProjectionCamera camera = cameraSystem.camera;
		final Transform shipTransform = getShipTransform();
		final Transform planetTransform = null;//mTransform.get(nearPlanetId);
		final Vector3 point = shipTransform.toUpDir(new Vector3()).scl(-6).add(shipTransform.currentPos);//planetTransform.currentPos

		sequence(
			parallel(
				deltaOperation(C.Camera.RotationDuration, Interpolation.sine, new DeltaExecutor() {
					@Override
					public void act(float deltaTime, float deltaPercent) {
						final float angle = deltaPercent * byDegrees;
						shipTransform.rotateAround(point, axis, angle);
					}
				}),
				temporal(C.Camera.RotationDuration, Interpolation.sine, new SimpleExecutor() {
					@Override
					public void act(float deltaTime, float percent, float alpha) {
						float p = (percent <= 0.5f ? percent : (1f - percent)) * 2f;
						camera.perspectiveFactor = MathUtils.lerp(
							C.Camera.MinPerspective, C.Camera.MaxPerspective, p
						);
						camera.update();
					}
				})
			)
		).register(world);
	}
}
