package net.namekdev.cosmos_is_alive.component.base;

import net.namekdev.cosmos_is_alive.component.render.Scale;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

/**
 * 3 dimensions describes "3D size": width, height and depth.
 * Used for collisions. To determine scale of renderer object
 * use {@link Scale}.
 *
 * @author Namek
 */
public class Dimensions extends PooledComponent {
	/** XYZ values represent: width, height, depth */
	public final Vector3 dimensions = new Vector3(1, 1, 1);


	public float getWidth() {
		return dimensions.x;
	}

	public float getHeight() {
		return dimensions.y;
	}

	public float getDepth() {
		return dimensions.z;
	}

	public Dimensions width(float w) {
		dimensions.x = w;
		return this;
	}

	public Dimensions height(float h) {
		dimensions.y = h;
		return this;
	}

	public Dimensions depth(float d) {
		dimensions.z = d;
		return this;
	}

	public Dimensions set(float width, float height, float depth) {
		dimensions.set(width, height, depth);
		return this;
	}


	@Override
	protected void reset() {
		dimensions.set(1, 1, 1);
	}
}
