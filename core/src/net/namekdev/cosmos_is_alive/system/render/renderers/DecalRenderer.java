package net.namekdev.cosmos_is_alive.system.render.renderers;

import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.DecalComponent;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;

public class DecalRenderer implements IRenderer  {
	private World world;
	private ComponentMapper<DecalComponent> mDecal;
	private ComponentMapper<Transform> mTransform;
	
	@Wire PerspectiveCamera camera;
	@Wire DecalBatch batch;


	private final Vector3 lookPoint = new Vector3();


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
