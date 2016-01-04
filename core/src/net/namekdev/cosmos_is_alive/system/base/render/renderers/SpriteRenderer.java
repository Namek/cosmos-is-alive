package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.render.Renderable;
import net.namekdev.cosmos_is_alive.component.render.SpriteComponent;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteRenderer implements IRenderer {
	private World world;
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
		
		/*
		Pos pos = mPos.get(e);
		Scale scale = mScale.getSafe(e);
		Origin origin = mOrigin.getSafe(e);
		Rotation rot = mRotation.getSafe(e);

		float scaleX = scale != null ? scale.x : 1f;
		float scaleY = scale != null ? scale.y : 1f;
		float originX = origin != null ? origin.x : Origin.DEFAULT_X;
		float originY = origin != null ? origin.y : Origin.DEFAULT_Y;
		float rotation = rot != null ? rot.rotation : 0;

		if (renderable.type == Renderable.Type.Sprite) {
			TextureRegion img = renderable.sprite;
			float w = img.getRegionWidth();
			float h = img.getRegionHeight();

			float x = pos.x, y = pos.y;
			if (mPosChild.has(e)) {
				Pos parentPos = mPos.get(world.getEntity(mPosChild.get(e).parent));
				x += parentPos.x;
				y += parentPos.y;
			}

			if (mColored.has(e)) {
				Colored col = mColored.get(e);
				sprites.setColor(col.color);
			}
			else {
				sprites.setColor(Color.WHITE);
			}

			float ox = originX*w;
			float oy = originY*h;

			sprites.draw(img, x - ox, y - oy, ox, oy, w, h, scaleX, scaleY, rotation);

//			if (renderable.debugFrame && mCollider.has(e)) {
//				collisions.getRect(e, tmpRect);
//				shapes.rect(tmpRect.x, tmpRect.y, tmpRect.width, tmpRect.height);
//				renderable.debugFrame = false;
//			}
			*/
	}
}