package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;

public class RenderableChild extends PooledComponent {
	public int parentId = 0;

	@Override
	protected void reset() {
		parentId = 0;
	}
}
