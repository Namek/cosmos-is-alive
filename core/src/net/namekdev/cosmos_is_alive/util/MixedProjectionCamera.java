package net.namekdev.cosmos_is_alive.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * <p>Camera that makes it possible to smoothly transition between
 * perspective and orthogonal projection.</p>
 *
 * @author Namek
 */
public class MixedProjectionCamera extends Camera {
	/** perspective: the field of view of the height, in degrees **/
	public float fieldOfView = 67;

	/** ortho: the zoom of the camera */
	public float zoom = 1;

	/**
	 * <p>Factor that describes how much perspective/orthogonal the camera is.</p>
	 * <p>Value should be in range {@code [0, 1]} where {@code 0f}
	 * is full ortho and {@code 1.0f} is full perspective.</p>
	 */
	public float perspectiveFactor = 1;


	final Vector3 tmp = new Vector3();
	final Matrix4 projectionPerspective = new Matrix4();
	final Matrix4 projectionOrtho = new Matrix4();


	public MixedProjectionCamera() {
	}

	public MixedProjectionCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
		this.fieldOfView = fieldOfViewY;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.near = 0;
		update();
	}

	@Override
	public void update() {
		update(true);
	}

	@Override
	public void update(boolean updateFrustum) {
		// perspective
		float aspect = viewportWidth / viewportHeight;
		projectionPerspective.setToProjection(Math.abs(near), Math.abs(far), fieldOfView, aspect);

		// ortho
		projectionOrtho.setToOrtho(zoom * -viewportWidth / 2, zoom * (viewportWidth / 2), zoom * -(viewportHeight / 2), zoom
			* viewportHeight / 2, near, far);

		// mix perspective and ortho
		perspectiveFactor = MathUtils.clamp(perspectiveFactor, 0, 1);
		projection.set(projectionOrtho).lerp(projectionPerspective, perspectiveFactor);

		view.setToLookAt(position, tmp.set(position).add(direction), up);
		combined.set(projection);
		Matrix4.mul(combined.val, view.val);

		if (updateFrustum) {
			invProjectionView.set(combined);
			Matrix4.inv(invProjectionView.val);
			frustum.update(invProjectionView);
		}
	}

	/** Moves the camera by the given amount on each axis.
	 * @param x the displacement on the x-axis
	 * @param y the displacement on the y-axis */
	public void translate (float x, float y) {
		translate(x, y, 0);
	}

	/** Moves the camera by the given vector.
	 * @param vec the displacement vector */
	public void translate (Vector2 vec) {
		translate(vec.x, vec.y, 0);
	}
}
