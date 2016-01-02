package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;

public class Layer extends PooledComponent {
	public int layer;

	@Override
	protected void reset() {
		layer = 0;
	}
}
