package net.namekdev.cosmos_is_alive.system;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.cosmos_is_alive.component.Colored;
import net.namekdev.cosmos_is_alive.component.Pos;
import net.namekdev.cosmos_is_alive.component.Rotation;
import net.namekdev.cosmos_is_alive.component.Scale;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

import com.artemis.BaseSystem;
import com.artemis.Entity;

public class TweenSystem extends BaseSystem {
	M<Pos> mPos;
	M<Rotation> mRotation;
	M<Scale> mScale;
	M<Colored> mColored;

	public TweenManager tweenManager;


	public TweenSystem() {
	}

	@Override
	protected void initialize() {
		tweenManager = new TweenManager();
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
	}

	@Override
	protected void processSystem() {
		final float dt = world.getDelta();
		tweenManager.update(dt);
	}

	public void sequence(TimelineInitializer initializer) {
		Timeline timeline = Timeline.createSequence();
		initializer.init(timeline);
		timeline.start(tweenManager);
	}
	
	public void parallel(TimelineInitializer initializer) {
		Timeline timeline = Timeline.createParallel();
		initializer.init(timeline);
		timeline.start(tweenManager);
	}

	public static interface TimelineInitializer {
		void init(Timeline timeline);
	}
	
	public class EntityTweenAccessor implements TweenAccessor<Entity> {
		public static final int POS = 1;
		public static final int POS_X = 2;
		public static final int POS_Y = 3;
		public static final int ROTATION = 4;
		public static final int SCALE = 5;
		public static final int SCALE_X = 6;
		public static final int SCALE_Y = 7;
		public static final int COLOR = 8;
		public static final int COLOR_WITH_ALPHA = 9;
		public static final int COLOR_R = 10;
		public static final int COLOR_G = 11;
		public static final int COLOR_B = 12;
		public static final int ALPHA = 13;


		@Override
		public int getValues(Entity entity, int tweenType, float[] returnValues) {
			Pos pos;
			Rotation rot;
			Scale scale;
			Colored col;

			switch (tweenType) {
			case POS:
				pos = mPos.get(entity);
				returnValues[0] = pos.x;
				returnValues[1] = pos.y;
				return 2;

			case POS_X:
				pos = mPos.get(entity);
				returnValues[0] = pos.x;
				return 1;

			case POS_Y:
				pos = mPos.get(entity);
				returnValues[0] = pos.y;
				return 1;

			case ROTATION:
				rot = mRotation.get(entity);
				returnValues[0] = rot.rotation;
				return 1;

			case SCALE:
				scale = mScale.get(entity);
				returnValues[0] = scale.x;
				returnValues[1] = scale.y;
				return 2;

			case SCALE_X:
				scale = mScale.get(entity);
				returnValues[0] = scale.x;
				return 1;

			case SCALE_Y:
				scale = mScale.get(entity);
				returnValues[0] = scale.y;
				return 1;

			case COLOR:
				col = mColored.get(entity);
				returnValues[0] = col.color.r;
				returnValues[1] = col.color.g;
				returnValues[2] = col.color.b;
				return 3;

			case COLOR_WITH_ALPHA:
				col = mColored.get(entity);
				returnValues[0] = col.color.r;
				returnValues[1] = col.color.g;
				returnValues[2] = col.color.b;
				returnValues[3] = col.color.a;
				return 4;

			case COLOR_R:
				col = mColored.get(entity);
				returnValues[0] = col.color.r;
				return 1;

			case COLOR_G:
				col = mColored.get(entity);
				returnValues[0] = col.color.g;
				return 1;

			case COLOR_B:
				col = mColored.get(entity);
				returnValues[0] = col.color.b;
				return 1;

			case ALPHA:
				col = mColored.get(entity);
				returnValues[0] = col.color.a;
				return 1;
			}

			return 0;
		}

		@Override
		public void setValues(Entity entity, int tweenType, float[] newValues) {
			Pos pos;
			Rotation rot;
			Scale scale;
			Colored col;

			switch (tweenType) {
			case POS:
				pos = mPos.get(entity);
				pos.x = newValues[0];
				pos.y = newValues[1];
				break;

			case POS_X:
				pos = mPos.get(entity);
				pos.x = newValues[0];
				break;

			case POS_Y:
				pos = mPos.get(entity);
				pos.y = newValues[0];
				break;

			case ROTATION:
				rot = mRotation.get(entity);
				rot.rotation = newValues[0];
				break;

			case SCALE:
				scale = mScale.get(entity);
				scale.x = newValues[0];
				scale.y = newValues[1];
				break;

			case SCALE_X:
				scale = mScale.get(entity);
				scale.x = newValues[0];
				break;

			case SCALE_Y:
				scale = mScale.get(entity);
				scale.y = newValues[0];
				break;

			case COLOR:
				col = mColored.get(entity);
				col.color.r = newValues[0];
				col.color.g = newValues[1];
				col.color.b = newValues[2];
				break;

			case COLOR_WITH_ALPHA:
				col = mColored.get(entity);
				col.color.r = newValues[0];
				col.color.g = newValues[1];
				col.color.b = newValues[2];
				col.color.a = newValues[3];
				break;

			case COLOR_R:
				col = mColored.get(entity);
				col.color.r = newValues[0];
				break;

			case COLOR_G:
				col = mColored.get(entity);
				col.color.g = newValues[0];
				break;

			case COLOR_B:
				col = mColored.get(entity);
				col.color.b = newValues[0];
				break;

			case ALPHA:
				col = mColored.get(entity);
				col.color.a = newValues[0];
				break;
			}
		}
	}
}
