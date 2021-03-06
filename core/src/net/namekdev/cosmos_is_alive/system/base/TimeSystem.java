package net.namekdev.cosmos_is_alive.system.base;

import net.namekdev.cosmos_is_alive.component.base.TimeUpdate;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Manages both global time (slow motion etc.) and calls time updaters for entities.
 *
 * @author Namek
 * @see TimeUpdate
 */
@Wire
public class TimeSystem extends EntityProcessingSystem {
	ComponentMapper<TimeUpdate> mTimeUpdate;

	public static float MIN_DELTA = 1/15f;
	private float deltaTime, deltaTimeModified;

	public float timeSpeedFactor = 1f;


	public TimeSystem() {
		super(Aspect.all(TimeUpdate.class));
	}

	public float getDeltaTime() {
		return deltaTimeModified;
	}

	public float getRealTimeDelta() {
		return deltaTime;
	}

	public float getDeltaTime(boolean modifiedByTimeFactor) {
		return modifiedByTimeFactor ? deltaTimeModified : deltaTime;
	}

	public float getDeltaTime(Entity e) {
		TimeUpdate time = mTimeUpdate.get(e);
		return getDeltaTime(time != null ? time.dependsOnTimeFactor : false);
	}


	@Override
	protected void begin() {
		deltaTime = Math.min(MIN_DELTA, Gdx.graphics.getDeltaTime());
		deltaTimeModified = deltaTime * timeSpeedFactor;
		world.setDelta(deltaTimeModified);
	}

	@Override
	protected void process(Entity e) {
		TimeUpdate time = mTimeUpdate.get(e);

		float delta = getDeltaTime(time.dependsOnTimeFactor);
		time.updater.update(delta, e);
	}
}
