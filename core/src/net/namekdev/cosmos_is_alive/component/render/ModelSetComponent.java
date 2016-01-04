package net.namekdev.cosmos_is_alive.component.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelSetComponent extends Component {
	public ModelInstance[] instances;

	public ModelSetComponent set(ModelInstance model) {
		instances = new ModelInstance[] { model };
		return this;
	}
}
