package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.DecalComponent;
import net.namekdev.cosmos_is_alive.util.MixedProjectionCamera;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

public class DecalRenderer implements IRenderer {
	private ComponentMapper<DecalComponent> mDecal;
	private ComponentMapper<Transform> mTransform;

	@Wire MixedProjectionCamera camera;
	@Wire DecalBatch batch;


	@Override
	public void initialize() {
	}

	@Override
	public Object getBatch() {
		return batch;
	}

	@Override
	public void begin() {
	}

	@Override
	public void draw(int e) {
		DecalComponent decalComponent = mDecal.get(e);
		Decal decal = decalComponent.decal;
		Transform transform = mTransform.get(e);

		decal.setPosition(transform.currentPos);

		if (decalComponent.lookAtCamera) {
			decal.lookAt(camera.position, camera.up);
		}
		else {
			decal.setRotation(transform.orientation);
		}

		batch.add(decal);
	}

	@Override
	public void end() {
		batch.flush();
	}

}
