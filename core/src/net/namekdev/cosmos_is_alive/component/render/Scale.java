package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class Scale extends PooledComponent {
	public final Vector3 scale = new Vector3(1, 1, 1);

	@Override
	protected void reset() {
		scale.set(1, 1, 1);
	}
}
