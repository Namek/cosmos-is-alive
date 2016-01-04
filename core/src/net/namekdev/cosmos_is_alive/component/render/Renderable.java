package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;

/**
 * Determines type of renderer, visibility and lists children.
 *
 * @author Namek
 * @see RenderSystem
 */
public class Renderable extends PooledComponent {
	public static final int NONE = -1;
	public static final int MODEL = 0;
	public static final int DECAL = 1;
	public static final int SPRITE = 2;
	public static final int PARTICLE_2D = 3;
	public static final int PARTICLE_3D = 4;
	
	public boolean visible = true; 
	public int type = NONE;
	public int[] children;
	public boolean debugFrame;



    public Renderable() {
    }

	@Override
	protected void reset() {
		visible = true;
		type = NONE;
		debugFrame = false;
		children = null;
	}
}
