package net.namekdev.cosmos_is_alive.component;

import net.namekdev.cosmos_is_alive.system.OrbitSystem;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

/**
 * Entities can orbit around planets tagged with this component.
 *
 * @see {@link OrbitSystem}
 */
public class Orbit extends PooledComponent {
	/**
	 * Altitude measured from the surface.
	 */
	public float altitude = 0;

	public final Vector3 orbitAxis = new Vector3(Vector3.X);
	public final Vector3 orbitRotationAxis = new Vector3(Vector3.Y);


	public Orbit set(float altitude, Vector3 orbitAxis, Vector3 orbitRotationAxis) {
		this.altitude = altitude;
		this.orbitAxis.set(orbitAxis);
		this.orbitRotationAxis.set(orbitRotationAxis);
		return this;
	}


	@Override
	protected void reset() {
		altitude = 0;
		orbitAxis.set(Vector3.X);
		orbitRotationAxis.set(Vector3.Y);
	}
}
