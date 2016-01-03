package net.namekdev.cosmos_is_alive.component.render;

import net.namekdev.cosmos_is_alive.component.base.Transform;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class DecalComponent extends PooledComponent {
	public Decal decal;
	
	/** Ignore {@link Transform#orientation} by looking at camera. */
	public boolean lookAtCamera = true;
	
	@Override
	protected void reset() {
		decal = null;
		lookAtCamera = true;
	}
}
