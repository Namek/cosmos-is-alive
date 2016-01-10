package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.ModelSetComponent;
import net.namekdev.cosmos_is_alive.component.render.Renderable;
import net.namekdev.cosmos_is_alive.component.render.Scale;
import net.namekdev.cosmos_is_alive.component.render.Shaders;
import net.namekdev.cosmos_is_alive.util.MixedProjectionCamera;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class ModelRenderer implements IRenderer {
	ComponentMapper<Dimensions> mDimensions;
	ComponentMapper<ModelSetComponent> mModelSet;
	ComponentMapper<Shaders> mShaders;
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Renderable> mRenderable;
	ComponentMapper<Scale> mScale;

	@Wire ModelBatch modelBatch;
	@Wire MixedProjectionCamera camera;

	Environment environment;
	DirectionalLight directionalLight;

	private Matrix4 tmpMat4 = new Matrix4();

	ModelInstance debugBoundingBox;


	@Override
	public void initialize() {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
//        environment.set(new ColorAttribute(ColorAttribute.Fog, 0, 0, 0, 1f));
        directionalLight = new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f);
        environment.add(directionalLight);

        createDebugBoundingBox();
	}

	private void createDebugBoundingBox() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(1, 0, 1, 1));
		Model model = builder.createBox(1, 1, 1, GL20.GL_LINES, material, Usage.Position | Usage.ColorUnpacked);

		debugBoundingBox = new ModelInstance(model);
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

		Renderable renderable = mRenderable.get(e);
		Transform transform = mTransform.get(e);
		Shaders shaders = mShaders.getSafe(e);
		Scale scale = mScale.getSafe(e);

		for (int i = 0; i < models.instances.length; ++i) {
			ModelInstance model = models.instances[i];

			// scale
			if (scale != null) {
				model.transform.setToScaling(scale.scale);
			}
			else {
				model.transform.idt();
			}

			// orientation
			model.transform.rotate(transform.orientation);
			model.transform.rotate(transform.orientationDisplacement);
			model.transform.rotate(transform.assetRotation);

			// translation
			model.transform.trn(transform.currentPos);


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

			if (renderable.debugFrame) {
				Dimensions dimensions = mDimensions.getSafe(e);

				if (dimensions == null) {
					continue;
				}

				final Matrix4 trans = debugBoundingBox.transform;
				final Vector3 dims = dimensions.dimensions;

				trans.setToScaling(dims);
				trans.rotate(transform.orientation);
				trans.trn(transform.currentPos);

				modelBatch.render(debugBoundingBox, environment);
			}
		}
	}
}
