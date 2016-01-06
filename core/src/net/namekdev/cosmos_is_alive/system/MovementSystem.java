package net.namekdev.cosmos_is_alive.system;

import net.namekdev.cosmos_is_alive.component.base.Transform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

public class MovementSystem extends EntityProcessingSystem {
	private ComponentMapper<Transform> mTransform;

	public MovementSystem() {
		super(Aspect.all(Transform.class));
	}

	@Override
	protected void process(Entity e) {
		Transform transform = mTransform.get(e);

		transform.currentPos.set(transform.desiredPos);
	}

}
