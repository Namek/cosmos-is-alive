package net.namekdev.cosmos_is_alive.system;

import net.namekdev.cosmos_is_alive.component.Orbit;
import net.namekdev.cosmos_is_alive.component.Orbiter;
import net.namekdev.cosmos_is_alive.component.Speed;
import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * {@link Orbiter} will orbit around {@link Orbit}.
 */
public class OrbitSystem extends EntityProcessingSystem {
	private ComponentMapper<Dimensions> mDimensions;
	private ComponentMapper<Orbit> mOrbit;
	private ComponentMapper<Orbiter> mOrbiter;
	private ComponentMapper<Speed> mSpeed;
	private ComponentMapper<Transform> mTransform;


	private final Vector3 pos = new Vector3();


	public OrbitSystem() {
		super(Aspect.all(Orbiter.class, Speed.class, Transform.class));
	}

	@Override
	protected void process(Entity e) {
		final float dt = world.getDelta();

		Orbiter orbiter = mOrbiter.get(e);
		Transform transform = mTransform.get(e);
		Speed speedC = mSpeed.get(e);
		Orbit orbit = mOrbit.get(orbiter.orbitEntityId);
		Dimensions orbiterSize = mDimensions.get(orbiter.orbitEntityId);
		Transform orbiterTransform = mTransform.get(orbiter.orbitEntityId);

		float speed = speedC.speed;
		float orbitRadius = orbiterSize.getWidth() + orbit.altitude;

		float degreesDelta = speed*dt / (MathUtils.degRad * orbitRadius);
		pos.set(transform.currentPos)
			.sub(orbiterTransform.currentPos)
			.rotate(orbit.orbitRotationAxis, degreesDelta)
			.add(orbiterTransform.currentPos);
		transform.xyz(pos);
	}
}
