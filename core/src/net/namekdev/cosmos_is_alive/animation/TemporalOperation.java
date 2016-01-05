package net.namekdev.cosmos_is_alive.animation;

import com.badlogic.gdx.math.Interpolation;

public abstract class TemporalOperation extends net.mostlyoriginal.api.operation.common.TemporalOperation {
	public TemporalOperation(Interpolation interpolation) {
		super.interpolation = interpolation;
	}

	public TemporalOperation(float duration) {
		setDuration(duration);
	}

	public TemporalOperation(Interpolation interpolation, float duration) {
		super.interpolation = interpolation;
		setDuration(duration);
	}
}
