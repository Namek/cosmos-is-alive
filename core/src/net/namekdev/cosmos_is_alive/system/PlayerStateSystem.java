package net.namekdev.cosmos_is_alive.system;

import static net.namekdev.cosmos_is_alive.system.TweenSystem.EntityTweenAccessor.*;
import static aurelienribon.tweenengine.TweenEquations.*;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
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
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class PlayerStateSystem extends BaseSystem {
	ComponentMapper<Transform> mTransform;

	AspectHelpers aspects;
	CollisionDetectionSystem collisions;
	RenderSystem renderSystem;
	TagManager tags;
	TweenSystem tweens;


	@Override
	protected void initialize() {
	}

	@Override
	protected void processSystem() {
		float dt = world.getDelta();
		final Entity e = tags.getEntity(Tags.Player);

		// TODO update movement
	}

	public Entity getPlayer() {
		return tags.getEntity(Tags.Player);
	}

	public Vector3 getShipPosition(Vector3 outPosition) {
		Transform transform = mTransform.get(getPlayer());
		return outPosition.set(transform.currentPos);
	}

	public Vector3 getShipDirection(Vector3 outDir) {
		Transform transform = mTransform.get(getPlayer());
		return transform.toRightDir(outDir).scl(-1);
	}
}
