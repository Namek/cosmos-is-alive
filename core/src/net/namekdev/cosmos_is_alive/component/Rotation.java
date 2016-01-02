package net.namekdev.cosmos_is_alive.component;

import com.artemis.PooledComponent;

public class Rotation extends PooledComponent {
	public float rotation;

	@Override
	protected void reset() {
		rotation = 0;
	}
}
