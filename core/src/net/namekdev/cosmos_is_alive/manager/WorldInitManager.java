package net.namekdev.cosmos_is_alive.manager;

import net.namekdev.cosmos_is_alive.factory.EntityFactory;

import com.artemis.BaseSystem;
import com.artemis.managers.TagManager;

public class WorldInitManager extends BaseSystem {
	EntityFactory factory;
	TagManager tags;

	private boolean initialized = false;
	

	private void init() {
		factory.createPlayer(150, 200);

		// TODO create your stuff
	}

	@Override
	protected void processSystem() {
		if (!initialized) {
			init();
			initialized = true;
		}
	}
}
