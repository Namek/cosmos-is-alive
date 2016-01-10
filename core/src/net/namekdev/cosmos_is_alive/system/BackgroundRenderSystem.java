package net.namekdev.cosmos_is_alive.system;

import static com.badlogic.gdx.Gdx.graphics;

import net.namekdev.cosmos_is_alive.Assets;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundRenderSystem extends BaseSystem {
	@Wire Assets assets;
	@Wire SpriteBatch sprites;

	@Override
	protected void processSystem() {
		Gdx.gl.glClearColor(0, 0, 1, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		sprites.begin();
		sprites.draw(assets.stars, 0, 0, graphics.getWidth(), graphics.getHeight());
		sprites.end();
	}

}
