package net.namekdev.cosmos_is_alive.animation;

import com.artemis.Entity;
import com.badlogic.gdx.math.Interpolation;

import net.mostlyoriginal.api.operation.common.TemporalOperation;

public abstract class TemporalDeltaOperation extends TemporalOperation {
	protected float previousPercentage = 0;
	
	public TemporalDeltaOperation() {
	}
	
	public TemporalDeltaOperation(Interpolation interpolation) {
		super.interpolation = interpolation;
	}
	
	public TemporalDeltaOperation(Interpolation interpolation, float duration) {
		super.interpolation = interpolation;
		setDuration(duration);
	}

	@Override
	public void rewind() {
		previousPercentage = 0;
		super.rewind();
	}

	@Override
	final public void act(float percentage, Entity e) {
		float deltaPercentage = previousPercentage - percentage;
		update(deltaPercentage, e);
		previousPercentage = percentage;
	}

	abstract public void update(float percentageDelta, Entity e);
}
