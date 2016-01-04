package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.render.SpriteComponent;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteRenderer implements IRenderer {
	private ComponentMapper<SpriteComponent> sm;
	
	@Wire private OrthographicCamera camera2d;
	@Wire private SpriteBatch batch;


	@Override
	public void initialize() {
	}
	
	@Override
	public Object getBatch() {
		return batch;
	}

	@Override
	public void begin() {
		camera2d.update();
		batch.setProjectionMatrix(camera2d.combined);
		batch.begin();
	}

	@Override
	public void end() {
		batch.end();
	}

	@Override
	public void draw(int e) {
		SpriteComponent sprite = sm.get(e);

		batch.setBlendFunction(sprite.blendSrcFunc, sprite.blendDestFunc);
		sprite.sprite.draw(batch);
	}
}