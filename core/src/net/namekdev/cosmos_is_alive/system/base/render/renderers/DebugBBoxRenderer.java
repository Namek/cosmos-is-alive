package net.namekdev.cosmos_is_alive.system.base.render.renderers;

import net.namekdev.cosmos_is_alive.component.base.Dimensions;
import net.namekdev.cosmos_is_alive.component.base.Transform;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class DebugBBoxRenderer implements IRenderer {
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Dimensions> mDimensions;
	
	@Wire Camera camera;
	@Wire ModelBatch models;
	
	public boolean enableDebugBoundingBoxes = false;
	
	ModelInstance debugBoundingBox;


	@Override
	public void initialize() {
		createDebugBoundingBox();
	}
	
	@Override
	public Object getBatch() {
		return models;
	}

	@Override
	public void begin() {
		models.begin(camera);
	}

	@Override
	public void end() {
		models.end();
	}

	@Override
	public void draw(int entity) {
		// Draw debug bounding box
		if (enableDebugBoundingBoxes) {
			Transform transform = mTransform.get(entity);
			Dimensions dimensions = mDimensions.get(entity);

			if (dimensions == null || transform == null) {
				return;
			}

			final Matrix4 trans = debugBoundingBox.transform;
			final Vector3 dims = dimensions.dimensions;

			transform.toMatrix4(trans);
			trans.scale(dims.x, dims.y, dims.z);

			// TODO
//			models.render(debugBoundingBox, environment);
		}
	}

	private void createDebugBoundingBox() {
		ModelBuilder builder = new ModelBuilder();
		Material material = new Material(ColorAttribute.createDiffuse(1, 0, 1, 1));
		Model model = builder.createBox(1, 1, 1, GL20.GL_LINES, material, Usage.Position | Usage.ColorUnpacked);

		debugBoundingBox = new ModelInstance(model);
	}


}
