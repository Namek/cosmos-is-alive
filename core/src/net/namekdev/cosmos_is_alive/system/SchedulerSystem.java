package net.namekdev.cosmos_is_alive.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import net.mostlyoriginal.api.component.Schedule;
import net.mostlyoriginal.api.operation.common.Operation;
import net.mostlyoriginal.api.operation.flow.ParallelOperation;
import net.mostlyoriginal.api.operation.flow.SequenceOperation;

public class SchedulerSystem extends EntityProcessingSystem {
	protected ComponentMapper<Schedule> mSchedule;

	public SchedulerSystem() {
		super(Aspect.all(Schedule.class));
    }

    @Override
    protected void process(Entity e) {
        ParallelOperation operation = mSchedule.get(e).operation;
        if (operation.process(world.delta, e)) {
            // Done. return schedule to pool.
            mSchedule.remove(e);
        }
    }

	public void schedule(Entity e, Operation operation) {
		mSchedule.create(e).operation.add(operation);
	}

	public ParallelOperation schedule(Operation operation) {
		Entity e = world.createEntity();
		ParallelOperation parallel = mSchedule.create(e).operation;
		parallel.add(operation);
		return parallel;
	}

	public void parallel(Operation... operations) {
		Entity e = world.createEntity();
		ParallelOperation parallel = mSchedule.create(e).operation;

		for (int i = 0; i < operations.length; ++i) {
			parallel.add(operations[i]);
		}
	}

	public void sequential(Operation... operations) {
		SequenceOperation sequence = new SequenceOperation();
		for (int i = 0; i < operations.length; ++i) {
			sequence.operations.add(operations[i]);
		}

		schedule(sequence);
	}
}
