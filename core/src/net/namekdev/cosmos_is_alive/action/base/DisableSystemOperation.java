package net.namekdev.cosmos_is_alive.action.base;

import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.SingleUseOperation;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.annotations.Wire;

public class DisableSystemOperation extends SingleUseOperation {
	public Class<? extends BaseSystem> systemType;

	@Override
	public Class<? extends Executor> executorType() {
		return DisableSystemExecutor.class;
	}

	@Wire
	public static class DisableSystemExecutor extends SingleUseExecutor<DisableSystemOperation> {
		World world;

		public void initialize(World world) {
			this.world = world;
		}

		@Override
		protected void act(DisableSystemOperation op, OperationTree node) {
			world.getSystem(op.systemType).setEnabled(false);
		}
	}
}
