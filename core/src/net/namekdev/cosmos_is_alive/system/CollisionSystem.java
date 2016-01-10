package net.namekdev.cosmos_is_alive.system;

import static java.lang.Math.*;
import static net.namekdev.cosmos_is_alive.system.base.collision.ColliderType.*;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector3;

import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.system.base.collision.Collider;
import net.namekdev.cosmos_is_alive.system.base.collision.CollisionDetectionSystem;

@Wire(injectInherited=true)
public class CollisionSystem extends CollisionDetectionSystem {
	private CameraSystem cameraSystem;

	private final Vector3 tmpPos1 = new Vector3(), tmpPos2 = new Vector3();
	private final Vector3 tmpSize1 = new Vector3(), tmpSize2 = new Vector3();


	@Override
	public boolean checkOverlap(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		final Transform trans1 = mTransform.get(entity1Id);
		final Transform trans2 = mTransform.get(entity2Id);
		final Dimensions size1 = mDimensions.get(entity1Id);
		final Dimensions size2 = mDimensions.get(entity2Id);

		cameraSystem.worldToScreen(tmpPos1.set(trans1.currentPos));
		cameraSystem.worldToScreen(tmpPos2.set(trans2.currentPos));

		tmpSize1.set(size1.dimensions).add(trans1.currentPos);
		tmpSize2.set(size2.dimensions).add(trans2.currentPos);

		cameraSystem.camera.project(tmpSize1);
		cameraSystem.camera.project(tmpSize2);

		tmpSize1.sub(tmpPos1);
		tmpSize2.sub(tmpPos2);

		switch (collider1.colliderType) {
			case CIRCLE: {
				circle1.set(tmpPos1.x, tmpPos1.y, Math.min(abs(tmpSize1.x), abs(tmpSize1.y))/2);
			}
		}

		switch (collider2.colliderType) {
			case CIRCLE: {
				circle2.set(tmpPos2.x, tmpPos2.y, Math.min(abs(tmpSize2.x), abs(tmpSize2.y))/2);
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
