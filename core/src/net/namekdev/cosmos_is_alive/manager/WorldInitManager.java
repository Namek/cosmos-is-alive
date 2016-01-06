package net.namekdev.cosmos_is_alive.manager;

import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.factory.EntityFactory;
import net.namekdev.cosmos_is_alive.factory.PlanetLayoutGenerator;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldInitManager extends BaseSystem {
	EntityFactory factory;
	TagManager tags;

	@Wire Assets assets;

	private boolean initialized = false;


	private void init() {
		factory.createStars(C.World.BoxSize, C.World.BoxSize, C.World.BoxSize);

		/*
		Entity planet = factory.createPlanet(4, 0, -10, 10, 2, assets.planetSaturn);
		factory.createScrapOrbitingAroundPlanet(planet, 500, 0.1f, 0.6f, 0.5f);

		planet = factory.createPlanet(-24, 5, -20, 8, 1, assets.planetNeptune);
		factory.createScrapOrbitingAroundPlanet(planet, 100, 0.05f, 0.2f, 2f);

		planet = factory.createPlanet(-10, -20, -40, 14, 0.78f, assets.planetEarth);

		planet = factory.createPlanet(-4, -25, 10, 14, 0.78f, assets.planetNeptune);
		planet = factory.createPlanet(-18, 10, 22, 14, 0.78f, assets.planetNeptune);*/

		PlanetLayoutGenerator gen = new PlanetLayoutGenerator();
		int width = 5, height = 5, depth = 4;
		int[][][] layout = gen.generate(width, height, depth, 10);
		final float gap = 10;
		final float size = 4;

		TextureRegion textures[] = new TextureRegion[] {
			assets.planetNeptune,
			assets.planetSaturn,
			assets.planetEarth,
		};
		float x = -10, y = -10, z = -10;

		for (int ix = 0; ix < width; ++ix) {
			for (int iy = 0; iy < height; ++iy) {
				for (int iz = 0; iz < depth; ++iz) {
					if (layout[ix][iy][iz] == 1) {
						TextureRegion tex = textures[(ix+iy+iz)%3];
						factory.createPlanet(x+ix*gap, y+iy*gap, z+iz*gap, size, 0, tex);
					}
				}
			}
		}

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
