package net.namekdev.cosmos_is_alive.factory;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.component.base.Transform;
import net.namekdev.cosmos_is_alive.component.render.*;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.Tags;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class EntityFactory extends PassiveSystem {
	private TagManager tags;

	@Wire public Assets assets;


	@Override
	protected void initialize() {

	}

	public Entity createPlayer(float x, float y) {
		Entity player = world.createEntity();
		EntityEdit e = player.edit();
		tags.register(Tags.Player, player);
		
		e.create(Renderable.class).type = Renderable.DECAL;
		e.create(DecalComponent.class)
			.set(createDecal(assets.playerTex, C.Player.Width, C.Player.Height))
			.lookAtCamera = false;
		e.create(Transform.class).xyz(0, 0, -1);

		return player;
	}
	
	Decal createDecal(TextureRegion texture, float width, float height) {
		Decal decal = new Decal();
		decal.setTextureRegion(texture);
		decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		decal.setDimensions(width, height);
		decal.setColor(1, 1, 1, 1);

		return decal;
	}
}
