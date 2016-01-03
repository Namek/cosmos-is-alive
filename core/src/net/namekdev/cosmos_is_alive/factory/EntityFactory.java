package net.namekdev.cosmos_is_alive.factory;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.component.render.Renderable;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.Tags;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.managers.TagManager;

public class EntityFactory extends PassiveSystem {
	private TagManager tags;

	public Assets assets;


	@Override
	protected void initialize() {

	}

	public Entity createPlayer(float x, float y) {
		Entity player = world.createEntity();
		EntityEdit e = player.edit();
		tags.register(Tags.Player, player);


		return player;
	}
}
