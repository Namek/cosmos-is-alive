package net.namekdev.cosmos_is_alive.manager;

import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.CollisionGroups;
import net.namekdev.cosmos_is_alive.factory.EntityFactory;
import net.namekdev.cosmos_is_alive.factory.PlanetLayoutGenerator;
import net.namekdev.cosmos_is_alive.system.CollisionSystem;
import net.namekdev.cosmos_is_alive.system.PlayerStateSystem;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class WorldInitManager extends BaseSystem {
	CollisionSystem collisions;
	EntityFactory factory;
	PlayerStateSystem playerSystem;
	TagManager tags;

	@Wire Assets assets;

	private boolean initialized = false;


	private void init() {
//		factory.createStars(C.World.StarBoxSize, C.World.StarBoxSize, C.World.StarBoxSize);

		/*
		Entity planet = factory.createPlanet(4, 0, -10, 10, 2, assets.planetSaturn);
		factory.createScrapOrbitingAroundPlanet(planet, 500, 0.1f, 0.6f, 0.5f);

		planet = factory.createPlanet(-24, 5, -20, 8, 1, assets.planetNeptune);
		factory.createScrapOrbitingAroundPlanet(planet, 100, 0.05f, 0.2f, 2f);

		planet = factory.createPlanet(-10, -20, -40, 14, 0.78f, assets.planetEarth);

		planet = factory.createPlanet(-4, -25, 10, 14, 0.78f, assets.planetNeptune);
		planet = factory.createPlanet(-18, 10, 22, 14, 0.78f, assets.planetNeptune);*/

		PlanetLayoutGenerator gen = new PlanetLayoutGenerator();
		int planetCount = 15;
		int width = 5, height = 5, depth = 5;
		int[][][] layout = gen.generate(width, height, depth, planetCount);
		final float minSize = 4, maxSize = 10;
		final float gap = (C.World.PlanetBoxSize - maxSize*width)/(width-1);

		TextureRegion textures[] = new TextureRegion[] {
			assets.planetNeptune,
			assets.planetSaturn,
			assets.planetEarth,
		};
		float x = -C.World.PlanetBoxSize/2 + maxSize/2;
		float y = -C.World.PlanetBoxSize/2 + maxSize/2;
		float z = -C.World.PlanetBoxSize/2 + maxSize/2;

		for (int ix = 0; ix < width; ++ix) {
			for (int iy = 0; iy < height; ++iy) {
				for (int iz = 0; iz < depth; ++iz) {
					if (layout[ix][iy][iz] == 1) {
						TextureRegion tex = textures[(ix+iy+iz)%3];
						float size = MathUtils.random(minSize, maxSize);

						factory.createPlanet(x+ix*(maxSize+gap), y+iy*(maxSize+gap), z+iz*(maxSize+gap), size, 0, tex);
					}
				}
			}
		}

		Entity player = factory.createPlayerShip(0, 0, 5);
		playerSystem.setPlayer(player);

		collisions.relations.connectGroups(CollisionGroups.PLAYER_2D, CollisionGroups.PLANETS_2D);
	}

	@Override
	protected void processSystem() {
		if (!initialized) {
			init();
			initialized = true;
		}
	}
}
