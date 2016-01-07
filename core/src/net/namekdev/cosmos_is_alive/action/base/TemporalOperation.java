package net.namekdev.cosmos_is_alive.action.base;

import com.artemis.annotations.Wire;

import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

public class TemporalOperation extends se.feomedia.orion.operation.TemporalOperation {
	public SimpleExecutor executor;

	public static interface SimpleExecutor {
		abstract void act(float deltaTime, float percent, float alpha);
	}


	@Override
	public void reset() {
		executor = null;
		super.reset();
	}

	@Override
	public Class<? extends Executor> executorType() {
		return CustomTemporalExecutor.class;
	}

	@Wire
	public static class CustomTemporalExecutor extends TemporalExecutor<TemporalOperation> {
		@Override
		protected void act(float deltaTime, float alpha, TemporalOperation operation, OperationTree node) {
			operation.executor.act(deltaTime, operation.percent(), operation.alpha());
		}
	}
}
