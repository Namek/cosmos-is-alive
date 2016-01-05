package net.namekdev.cosmos_is_alive.component;

import net.namekdev.cosmos_is_alive.system.OrbitSystem;

import com.artemis.PooledComponent;
import com.artemis.annotations.EntityId;

/**
 * Ship or some scrap that can orbit around planet.
 *
 * @see {@link OrbitSystem}
 */
public class Orbiter extends PooledComponent {
	@EntityId
	public int orbitEntityId = -1;


	@Override
	protected void reset() {
		orbitEntityId = -1;
	}
}
