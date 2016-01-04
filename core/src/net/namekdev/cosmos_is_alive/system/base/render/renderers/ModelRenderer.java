package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.ModelSetComponent;
import net.namekdev.cosmos_is_alive.component.render.Shaders;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

@Wire
public class ModelRenderer implements IRenderer {
	ComponentMapper<ModelSetComponent> mModelSet;
	ComponentMapper<Shaders> mShaders;
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Dimensions> mDimensions;
	
	@Wire ModelBatch modelBatch;
	@Wire PerspectiveCamera camera;
	
	Environment environment;
	DirectionalLight directionalLight;


	@Override
	public void initialize() {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0, 0, 0, 1f));
        directionalLight = new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f);
        environment.add(directionalLight);
	}
	
	@Override
	public Object getBatch() {
		return modelBatch;
	}

	@Override
	public void begin() {
		modelBatch.begin(camera);
	}

	@Override
	public void end() {
		modelBatch.end();
	}

	@Override
	public void draw(int e) {
		ModelSetComponent models = mModelSet.get(e);

		if (models == null) {
			return;
		}

		Transform transform = mTransform.get(e);
		Shaders shaders = mShaders.getSafe(e);
		Dimensions dims = mDimensions.getSafe(e);

		for (int i = 0; i < models.instances.length; ++i) {
			ModelInstance model = models.instances[i];

			transform.toMatrix4(model.transform);

			if (dims != null) {
				model.transform.scale(dims.dimensions.x, dims.dimensions.y, dims.dimensions.z);
			}

			if (shaders == null) {
				modelBatch.render(model, environment);
			}
			else {
				if (shaders.useDefaultShader) {
					modelBatch.render(model, environment);
				}

				for (int j = 0, n = shaders.shaders.length; j < n; ++j) {
					modelBatch.render(model, environment, shaders.shaders[j]);
				}
			}
		}
	}
}
