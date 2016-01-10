package net.namekdev.cosmos_is_alive.action.base;

import static se.feomedia.orion.OperationFactory.*;
import net.namekdev.cosmos_is_alive.action.base.TemporalDeltaOperation.DeltaExecutor;
import net.namekdev.cosmos_is_alive.action.base.TemporalOperation.SimpleExecutor;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Interpolation;

public final class Operations {
	public static TemporalOperation temporal(float duration, SimpleExecutor executor) {
		TemporalOperation action = operation(TemporalOperation.class);
		action.duration = duration;
		action.executor = executor;

		return action;
	}

	public static TemporalOperation temporal(float duration, Interpolation interpolation, SimpleExecutor executor) {
		TemporalOperation action = temporal(duration, executor);
		action.interpolation = interpolation;

		return action;
	}

	public static TemporalDeltaOperation deltaTemporal(float duration, DeltaExecutor executor) {
		TemporalDeltaOperation action = operation(TemporalDeltaOperation.class);
		action.duration = duration;
		action.executor = executor;

		return action;
	}

	public static TemporalDeltaOperation deltaOperation(float duration, Interpolation interpolation, DeltaExecutor executor) {
		TemporalDeltaOperation action = deltaTemporal(duration, executor);
		action.interpolation = interpolation;

		return action;
	}

	public static CallOperation call(Runnable callback) {
		CallOperation action = operation(CallOperation.class);
		action.callback = callback;

		return action;
	}

	public static DisableSystemOperation disableSystem(Class<? extends BaseSystem> systemType) {
		DisableSystemOperation action = operation(DisableSystemOperation.class);
		action.systemType = systemType;

		return action;
	}

	public static EnableSystemOperation enableSystem(Class<? extends BaseSystem> systemType) {
		EnableSystemOperation action = operation(EnableSystemOperation.class);
		action.systemType = systemType;

		return action;
	}
}
