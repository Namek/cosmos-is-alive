package net.namekdev.cosmos_is_alive.component.render;

import net.namekdev.cosmos_is_alive.system.base.render.renderers.IRenderer;

import com.artemis.PooledComponent;

public class CustomRenderer extends PooledComponent {
	public IRenderer renderer;
	
	@Override
	protected void reset() {
		renderer = null;
	}
}
