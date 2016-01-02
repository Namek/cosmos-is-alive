package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Renderable extends PooledComponent {
	public static final int FRAME_ANIM = 2;
	public static final int SPRITE = 4;
	public static final int CUSTOM_RENDERER = 16;


	public boolean visible = true;

	/** bitmask of types */
	public int type = 0;

	public int[] children = null;

	public TextureRegion sprite = null;
	public CustomRenderer renderer = null;


	@Override
	protected void reset() {
		visible = true;
		type = 0;
		children = null;
		sprite = null;
		renderer = null;
	}

	public void setToSprite(TextureRegion texture) {
		this.sprite = texture;
		this.type = SPRITE;
	}


	public static interface CustomRenderer {
		void render(int entityId, SpriteBatch sprites, ShapeRenderer shapes);
	}
}
