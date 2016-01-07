package net.namekdev.cosmos_is_alive.component.base;

import static java.lang.Math.*;
import net.namekdev.cosmos_is_alive.system.base.PositionSystem;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Defines a transformation of entity:
 * <ol>
 *   <li>desired and current positions</li>
 *   <li>orientation</li>
 *   <li>(optional) graphical {@link #displacement} of position</li>
 * </ol>
 *
 * <p>{@link #desiredPos} defines position which can be calculated by {@link PositionSystem}
 * and {@link currentPos} is the result of collision check and accepting or modifying
 * {@link #desiredPos}. Simply acccepting desired position as current position can be
 * done by copying value of {@link #desiredPos} into {@link #currentPos}.</p>
 *
 * <p><b>Orientation</b> is defined by {@link #orientation} quaternion.</p>
 *
 * @author Namek
 * @see PositionSystem
 */
public class Transform extends PooledComponent {
	/** Default direction when null orientation is given.
	 * Don't modify this, it's rather by OpenGL design. */
	public static final Vector3 DEFAULT_DIRECTION = new Vector3(0, 0, -1);

	/** Default up vector. */
	public static final Vector3 UP_VECTOR = new Vector3(0, 1, 0);

	private static final Vector3 tmpVect = new Vector3();
	private static final Vector3 tmpVect2 = new Vector3();
	private static final Vector3 tmpVect3 = new Vector3();
	private static final Quaternion tmpQuat = new Quaternion();


	/** Position set before collision detection. */
	public final Vector3 desiredPos = new Vector3();

	/** Finally accepted position, result of collision checks and physical forces. */
	public final Vector3 currentPos = new Vector3();

	/** Additional displacement to position. Usually used for graphics puroses, like head bobbing. */
	public final Vector3 displacement = new Vector3();

	/** Defines logical orientation due to 3D vector {@link #DEFAULT_DIRECTION} = {@code (0, 0, -1)}. */
	public final Quaternion orientation = new Quaternion();

	/** Defines visual orientation displacement. Usually used for some movement animation. */
	public final Quaternion orientationDisplacement = new Quaternion();

	/** Defines asset rotation, it's always added to visual orientation.
	 *  Should be a static value, it's a way to fix model orientation in runtime. */
	public final Quaternion assetRotation = new Quaternion();



	@Override
	protected void reset() {
		currentPos.setZero();
		desiredPos.setZero();
		displacement.setZero();
		orientation.idt();
		orientationDisplacement.idt();
	}

	@Override
	public String toString() {
		toDirection(tmpVect2);
		return "pos=" + currentPos.toString() + ", dir=" + tmpVect2.toString();
	}


	/**
	 * Sets both desired and current position.
	 */
	public Transform xyz(float x, float y, float z) {
		desiredPos.set(x, y, z);
		currentPos.set(x, y, z);
		return this;
	}

	/**
	 * Sets both desired and current position.
	 */
	public Transform xyz(Vector3 pos) {
		desiredPos.set(pos);
		currentPos.set(pos);
		return this;
	}

	/**
	 * Sets orientation by comparing it to {@link #DEFAULT_DIRECTION},
	 * roll angle is set to zero.
	 */
	public Transform look(float dirX, float dirY, float dirZ) {
		final Vector3 u = DEFAULT_DIRECTION;
		final Vector3 v = tmpVect.set(dirX, dirY, dirZ).nor();
		final Vector3 w = tmpVect2.set(u).crs(v);

		// Normally, we would finish this algorithm this way:
		//   orientation.set(w.x, w.y, w.z, 1f + u.dot(v)).nor();
		// but direction 0,0,1 is 180 degree to 0,0,-1 and it breaks.

	    float len = w.len2();
	    float real_part = u.dot(v);
	    if (len < 1.6e-12f && real_part < 0)
	    {
	    	if (abs(u.x) > abs(u.z)) {
	    		w.set(-u.y, u.x, 0.f).scl((float)(1.0 / sqrt(u.y*u.y + u.x*u.x)));
	    	}
	    	else {
	    		w.set(0.f, -u.z, u.y).scl((float)(1.0 / sqrt(u.y*u.y + u.z*u.z)));
	    	}

	    	orientation.set(w.x, w.y, w.z, 0);
	    }
	    else {
		    real_part += sqrt(real_part*real_part + len);
		    orientation.set(w.x, w.y, w.z, real_part).mul((float)(1.0 / sqrt(real_part*real_part + len)));
	    }

	    return this;
	}

	/**
	 * Sets orientation by comparing it to {@link #DEFAULT_DIRECTION},
	 * roll angle is set to zero.
	 */
	public Transform look(Vector3 dir) {
		return look(dir.x, dir.y, dir.z);
	}

	/**
	 * Gets {@link #currentPos}, {@link #direction} and {@link #up} into given Matrix.
	 * Ignores {@link #displacement} and @{link #visualOrientation}.
	 */
	public Matrix4 toMatrix4(Matrix4 mat) {
		orientation.toMatrix(mat.val);
		mat.trn(currentPos);
		return mat;
	}

	/**
	 * Sets {@link #desiredPos} and {@link #orientation} from given Matrix.
	 * Don't use scaling in given matrix because quaternion is not normalized.
	 */
	public void fromMatrix4(Matrix4 mat) {
		mat.getTranslation(desiredPos);
		mat.getRotation(orientation);
	}

	public Vector3 toDirection(Vector3 outDir) {
		outDir.set(DEFAULT_DIRECTION);
		outDir.mul(orientation).nor();
		return outDir;
	}

	public Vector3 toUpDir(Vector3 outUp) {
		return outUp.set(UP_VECTOR).mul(orientation).nor();
	}

	public Vector3 toRightDir(Vector3 outRight) {
		return toDirection(outRight).crs(toUpDir(tmpVect2));
	}

	public Vector3 toLeftDir(Vector3 outLeft) {
		return toRightDir(outLeft).scl(-1f);
	}

	/**
	 * Transforms direction vector to up vector.
	 * Base up vector is {@link #UP_VECTOR} = {@code (0, 1, 0)}.
	 *
	 * @return given {@code outUp} reference
	 */
	public static Vector3 directionToUp(Vector3 dir, Vector3 outUp) {
		// right = cross(direction, 0,1,0)
		// up = cross(right, direction)
		return outUp.set(dir).crs(UP_VECTOR).crs(dir);
	}

	public void translate(Vector3 translation) {
		desiredPos.add(translation);
	}

	public void moveForward(float length) {
		translate(toDirection(tmpVect).scl(length));
	}

	public void moveBackward(float length) {
		moveForward(-length);
	}

	public void moveRight(float length) {
		translate(toRightDir(tmpVect3).scl(length));
	}

	public void moveLeft(float length) {
		translate(toRightDir(tmpVect3).scl(-length));
	}

	/**
	 * Rotates by the given angle around the given axis, with the axis attached to given point.
	 *
	 * @param angle the angle in degrees
	 */
	public void rotateAround(Vector3 point, Vector3 axis, float angle) {
		// TODO something wrong is here, probably
		tmpVect.set(point);
		tmpVect.sub(desiredPos);
		translate(tmpVect);
		rotate(axis, angle);
		tmpVect.rotate(axis, angle);
		translate(tmpVect.scl(-1f));
	}

	/**
	 * Rotates by the given angle around the given local axis that
	 * will be attached to origin ({@link #desiredPos}) point.
	 *
	 * @param localAxis axis attached to current orientation
	 * @param angle the angle in degrees
	 */
	public void rotateLocal(Vector3 localAxis, float angle) {
		tmpQuat.set(localAxis, angle);
		orientation.mul(tmpQuat);
	}

	public void rotate(Vector3 globalAxis, float angle) {
		tmpQuat.set(globalAxis, angle);
		orientation.mulLeft(tmpQuat);
	}
}
