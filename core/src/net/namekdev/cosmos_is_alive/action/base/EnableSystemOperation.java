package net.namekdev.cosmos_is_alive.action.base;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.annotations.Wire;

import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.SingleUseOperation;

public class EnableSystemOperation extends SingleUseOperation {
	public Class<? extends BaseSystem> systemType;

	@Override
	public Class<? extends Executor> executorType() {
		return EnableSystemExecutor.class;
	}

	@Wire
	public static class EnableSystemExecutor extends SingleUseExecutor<EnableSystemOperation> {
		World world;

		public void initialize(World world) {
			this.world = world;
		}

		@Override
		protected void act(EnableSystemOperation op, OperationTree node) {
			world.getSystem(op.systemType).setEnabled(true);
		}
	}
}
