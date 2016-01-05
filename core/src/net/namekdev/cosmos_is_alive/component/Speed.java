package net.namekdev.cosmos_is_alive.component;

import com.artemis.PooledComponent;

public class Speed extends PooledComponent {
	public float speed = 0;

	@Override
	protected void reset() {
		speed = 0;
	}

}
