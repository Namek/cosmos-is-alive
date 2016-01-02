package net.namekdev.cosmos_is_alive.component;

import com.artemis.PooledComponent;

public class PosChild extends PooledComponent {
	/** Parent entity id. */
	public int parent = -1;
	
	@Override
	protected void reset() {
		parent = -1;
	}
}
