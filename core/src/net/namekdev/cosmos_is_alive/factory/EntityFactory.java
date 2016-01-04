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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class EntityFactory extends PassiveSystem {
	private TagManager tags;

	@Wire public Assets assets;


	@Override
	protected void initialize() {
	}

	public static Decal createDecal(TextureRegion texture, float width, float height) {
		Decal decal = new Decal();
		decal.setTextureRegion(texture);
		decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		decal.setDimensions(width, height);
		decal.setColor(1, 1, 1, 1);

		return decal;
	}

	public static DecalComponent setDecal(EntityEdit e, TextureRegion texture, float width, float height) {
		e.create(Renderable.class).type = Renderable.DECAL;

		return e.create(DecalComponent.class)
			.set(createDecal(texture, width, height));
	}

	public static ModelSetComponent setModel(EntityEdit e, ModelInstance model) {
		e.create(Renderable.class).type = Renderable.MODEL;
		return e.create(ModelSetComponent.class).set(model);
	}

	public static Transform xyz(EntityEdit e, float x, float y, float z) {
		return e.create(Transform.class)
			.xyz(x, y, z);
	}

	public Entity createPlayer(float x, float y) {
		Entity player = world.createEntity();
		EntityEdit e = player.edit();
		tags.register(Tags.Player, player);

		setDecal(e, assets.playerTex, C.Player.Width, C.Player.Height)
			.lookAtCamera = true;
		e.create(Transform.class).xyz(0, 0, -1);

		return player;
	}

	public void createStars() {
		Entity stars = null;
		EntityEdit e = null;

		final float mapWidth = C.World.BoxSize;
		final float mapHeight = C.World.BoxSize;
		final float mapDepth = C.World.BoxSize;

		// front
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapWidth, mapHeight);
		xyz(e, 0, 0, -mapDepth/2);

		// left
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapDepth, mapHeight);
		xyz(e, -mapWidth/2, 0, 0).look(1, 0, 0);

		// right
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapDepth, mapHeight);
		xyz(e, mapWidth/2, 0, 0).look(-1, 0, 0);

		// back
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapWidth, mapHeight);
		xyz(e, 0, 0, mapDepth/2).look(0, 0, 1);

		// bottom
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapWidth, mapDepth);
		xyz(e, 0, -mapWidth/2, 0).look(0, 1, 0);

		// up
		stars = world.createEntity();
		e = stars.edit();
		setDecal(e, assets.stars, mapWidth, mapDepth);
		xyz(e, 0, mapWidth/2, 0).look(0, -1, 0);
	}

	public void createPlanet(float x, float y, float z, float size) {
		Entity planet = world.createEntity();
		EntityEdit e = planet.edit();

		ModelBuilder mb = new ModelBuilder();
		Material material = new Material(TextureAttribute.createDiffuse(assets.planetEarth));
		Model model = mb.createSphere(size, size, size, (int)size*4, (int)size*4, material, Usage.Position|Usage.TextureCoordinates|Usage.Normal);

		setModel(e, new ModelInstance(model));
		e.create(Transform.class).xyz(x, y, z);
	}
}
