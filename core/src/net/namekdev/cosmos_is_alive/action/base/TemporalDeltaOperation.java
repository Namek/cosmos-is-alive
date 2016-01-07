package net.namekdev.cosmos_is_alive.action.base;

import com.artemis.annotations.Wire;

import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.TemporalOperation;

public class TemporalDeltaOperation extends TemporalOperation {
	public DeltaExecutor executor;
	protected float previousPercentage;

	public static interface DeltaExecutor {
		abstract void act(float deltaTime, float deltaPercent);
	}


	@Override
	public void reset() {
		executor = null;
		super.reset();
	}

	@Override
	public Class<? extends Executor> executorType() {
		return TemporalDeltaOperationExecutor.class;
	}

	@Wire
	public static class TemporalDeltaOperationExecutor extends TemporalExecutor<TemporalDeltaOperation> {
		@Override
		protected void begin(TemporalDeltaOperation op, OperationTree node) {
			super.begin(op, node);
			op.previousPercentage = 0;
		}

		@Override
		protected void act(float deltaTime, float alpha, TemporalDeltaOperation operation, OperationTree node) {
			float percent = operation.percent();
			float deltaPercentage = percent - operation.previousPercentage;
			operation.executor.act(deltaTime, deltaPercentage);
			operation.previousPercentage = percent;
		}
	}
}
