package net.namekdev.cosmos_is_alive.system.base.collision;

import static net.namekdev.cosmos_is_alive.system.base.collision.ColliderType.*;

import java.util.Map;
import java.util.TreeMap;

import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.system.base.collision.messaging.CollisionEvent;
import net.namekdev.cosmos_is_alive.system.base.events.EventSystem;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Collision detector working on top of groups and group relations.
 *
 * @todo optimize system to be processed in configurable intervals
 * @todo optimize by caching entities belongingness to groups, see {@link #processEntities(IntBag)} comment.
 */
@Wire
public class CollisionDetectionSystem extends EntitySystem {
	public static final int NONE = 1;
	public static final int ENTERED = 2;
	public static final int EXISTING = 3;

	protected ComponentMapper<Collider> mCollider;
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<Dimensions> mDimensions;

	protected EventSystem events;

	public final CollisionGroupsRelations relations;
	protected final CollisionPhases phases = new CollisionPhases();
	protected final Vector3 min = new Vector3(), max = new Vector3();
	protected final BoundingBox box1 = new BoundingBox(), box2 = new BoundingBox();
	protected final Circle circle1 = new Circle(), circle2 = new Circle();
	protected final Matrix4 tmpMat = new Matrix4();

	public boolean eventDispatchingEnabled;


	public CollisionDetectionSystem() {
		this(new CollisionGroupsRelations());
	}

	public CollisionDetectionSystem(boolean eventDispatchingEnabled) {
		this(new CollisionGroupsRelations(), eventDispatchingEnabled);
	}

	public CollisionDetectionSystem(CollisionGroupsRelations relations) {
		this(relations, false);
	}

	public CollisionDetectionSystem(CollisionGroupsRelations relations, boolean eventDispatchingEnabled) {
		super(Aspect.all(Collider.class, Transform.class, Dimensions.class));

		this.relations = relations;
		this.eventDispatchingEnabled = eventDispatchingEnabled;
	}

	@Override
	protected void processSystem() {
		processEntities(subscription.getEntities());
	}

	/**
	 * <b>TODO</b>: optimize by caching entities belongingness to groups.
	 *	 Now all entities are checked against themselves needlessly,
	 *	 e.g. thanks to groups relations Bullets will never collide with Bullets
	 *	 so it doesn't make much performance sense to check those relations between those entities.
	 */
	protected void processEntities(IntBag entities) {
		int[] ids = entities.getData();

		for (int i = 0, n = entities.size(); i < n; ++i) {
			int entity1Id = ids[i];
			Collider collider1 = mCollider.get(entity1Id);

			for (int j = i + 1; j < n; ++j) {
				int entity2Id = ids[j];
				Collider collider2 = mCollider.get(entity2Id);

				processPair(entity1Id, collider1, entity2Id, collider2);
			}
		}
	}

	protected void processPair(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		int phase = phases.get(entity1Id, entity2Id);

		if (phase == EXISTING) {
			if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
				phases.set(entity1Id, entity2Id, NONE);
				onCollisionExit(entity1Id, collider1, entity2Id, collider2);
			}
		}
		else if (phase == ENTERED) {
			if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
				phases.set(entity1Id, entity2Id, NONE);
				onCollisionExit(entity1Id, collider1, entity2Id, collider2);
			}
			else {
				phases.set(entity1Id, entity2Id, EXISTING);
			}
		}
		else if (phase == NONE) {
			if (relations.anyRelationExists(collider1.groups, collider2.groups)) {
				if (checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
					if (onCollisionEnter(entity1Id, collider1, entity2Id, collider2)) {
						phases.set(entity1Id, entity2Id, ENTERED);
					}
				}
			}
		}
	}

	public boolean checkOverlap(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		final Transform trans1 = mTransform.get(entity1Id);
		final Transform trans2 = mTransform.get(entity2Id);
		final Dimensions size1 = mDimensions.get(entity1Id);
		final Dimensions size2 = mDimensions.get(entity2Id);

		switch (collider1.colliderType) {
			case BOUNDING_BOX: {
				trans1.orientation.toMatrix(tmpMat.val);
				min.set(size1.dimensions).scl(-0.5f).mul(tmpMat).add(trans1.currentPos);
				max.set(size1.dimensions).scl(0.5f).mul(tmpMat).add(trans1.currentPos);
				box1.set(min, max);
			}
		}
		switch (collider2.colliderType) {
			case BOUNDING_BOX: {
				trans2.orientation.toMatrix(tmpMat.val);
				min.set(size2.dimensions).scl(-0.5f).mul(tmpMat).add(trans2.currentPos);
				max.set(size2.dimensions).scl(0.5f).mul(tmpMat).add(trans2.currentPos);
				box2.set(min, max);
			}
		}

		boolean overlaps = false;

		if (collider1.colliderType == collider2.colliderType) {
			switch (collider1.colliderType) {
				case BOUNDING_BOX: overlaps = box1.intersects(box2);	break;
			}
		}
		else {
			throw new Error("Those collider types are not supported together.");
		}

		return overlaps;
	}

	public boolean checkOverlap(int entity1Id, int entity2Id) {
		Collider collider1 = mCollider.get(entity1Id);
		Collider collider2 = mCollider.get(entity2Id);
		return checkOverlap(entity1Id, collider1, entity2Id, collider2);
	}

	@Override
	public void removed(Entity entity) {
		phases.clear(entity.getId());
	}

	public boolean onCollisionEnter(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		if (collider1.enterListener != null) {
			collider1.enterListener.onCollisionEnter(entity1Id, entity2Id);
		}

		if (collider2.enterListener != null) {
			collider2.enterListener.onCollisionEnter(entity2Id, entity1Id);
		}

		if (eventDispatchingEnabled) {
			events.dispatch(CollisionEvent.class)
				.setup(entity1Id, entity2Id, CollisionEvent.ENTER);
		}

		return true;
	}

	public void onCollisionExit(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		if (collider1.exitListener != null) {
			collider1.exitListener.onCollisionExit(entity1Id, entity2Id);
		}

		if (collider2.exitListener != null) {
			collider2.exitListener.onCollisionExit(entity2Id, entity1Id);
		}

		if (eventDispatchingEnabled) {
			events.dispatch(CollisionEvent.class)
				.setup(entity1Id, entity2Id, CollisionEvent.EXIT);
		}
	}


	/**
	 *
	 * @author Namek
	 * @todo <b>Pleease</b>, optimizee meee!
	 */
	public static class CollisionPhases {
		/** Maps entity1Id to entity2d which maps to phase */
		private final Map<Integer, Map<Integer, Integer>> collisionPhases = new TreeMap<Integer, Map<Integer, Integer>>();


		public void set(int entity1Id, int entity2Id, int phase) {
			if (entity2Id < entity1Id) {
				int tmp = entity1Id;
				entity1Id = entity2Id;
				entity2Id = tmp;
			}

			Map<Integer, Integer> relations = collisionPhases.get(entity1Id);

			if (relations == null) {
				relations = new TreeMap<Integer, Integer>();
				collisionPhases.put(entity1Id, relations);
			}

			relations.put(entity2Id, phase);
		}

		public int get(int entity1Id, int entity2Id) {
			if (entity2Id < entity1Id) {
				int tmp = entity1Id;
				entity1Id = entity2Id;
				entity2Id = tmp;
			}

			Map<Integer, Integer> relations = collisionPhases.get(entity1Id);
			if (relations != null) {
				Integer phase = relations.get(entity2Id);

				if (phase != null) {
					return phase;
				}
			}

			return NONE;
		}

		public void clear(int entityId) {
			Map<Integer, Integer> relations = collisionPhases.get(entityId);

			if (relations != null) {
				relations.clear();
			}

			for (Map<Integer, Integer> entry : collisionPhases.values()) {
				entry.remove(entityId);
			}
		}
	}
}