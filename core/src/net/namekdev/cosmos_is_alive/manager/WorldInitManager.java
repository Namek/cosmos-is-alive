package net.namekdev.cosmos_is_alive.manager;

import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.component.base.*;
import net.namekdev.cosmos_is_alive.factory.EntityFactory;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;

public class WorldInitManager extends BaseSystem {
	EntityFactory factory;
	TagManager tags;

	@Wire Assets assets;

	private boolean initialized = false;


	private void init() {
		factory.createStars();

		Entity planet = factory.createPlanet(4, 0, -10, 10, 2, assets.planetSaturn);
		factory.createScrapOrbitingAroundPlanet(planet, 500, 0.1f, 0.6f, 0.5f);

		planet = factory.createPlanet(-24, 5, -20, 8, 1, assets.planetNeptune);
		factory.createScrapOrbitingAroundPlanet(planet, 100, 0.05f, 0.2f, 2f);

		planet = factory.createPlanet(-10, -20, -40, 14, 0.78f, assets.planetEarth);

		factory.createPlayerShip(150, 200);

	}

	@Override
	protected void processSystem() {
		if (!initialized) {
			init();
			initialized = true;
		}
	}
}
