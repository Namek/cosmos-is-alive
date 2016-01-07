package net.namekdev.cosmos_is_alive.action.base;

import com.artemis.annotations.Wire;

import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.SingleUseOperation;

public class CallOperation extends SingleUseOperation {
	public Runnable callback;


	@Override
	public Class<? extends Executor> executorType() {
		return CallExecutor.class;
	}

	@Wire
	public static class CallExecutor extends SingleUseExecutor<CallOperation> {
		@Override
		protected void act(CallOperation op, OperationTree node) {
			op.callback.run();
		}
	}
}
