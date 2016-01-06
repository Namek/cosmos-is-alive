package net.namekdev.cosmos_is_alive.system;

import static net.namekdev.cosmos_is_alive.system.base.collision.ColliderType.*;
import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.system.base.collision.Collider;
import net.namekdev.cosmos_is_alive.system.base.collision.CollisionDetectionSystem;

public class CollisionSystem extends CollisionDetectionSystem {

	@Override
	public boolean checkOverlap(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		final Transform trans1 = mTransform.get(entity1Id);
		final Transform trans2 = mTransform.get(entity2Id);
		final Dimensions size1 = mDimensions.get(entity1Id);
		final Dimensions size2 = mDimensions.get(entity2Id);

		switch (collider1.colliderType) {
			case CIRCLE: {
				circle1
			}
		}

		switch (collider2.colliderType) {
			case CIRCLE: {

			}
		}

		boolean overlaps = false;

		if (collider1.colliderType == collider2.colliderType) {
			switch (collider1.colliderType) {
				case CIRCLE: overlaps = circle1.overlaps(circle2); break;
			}
		}
		else {
			throw new Error("Those collider types are not supported together.");
		}

		return overlaps;
	}

}
