package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;

public class ZOrder extends PooledComponent {
	public int z = 0;

	@Override
	protected void reset() {
		z = 0;
	}

}
