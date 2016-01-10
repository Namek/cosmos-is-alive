package net.namekdev.cosmos_is_alive.factory;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.component.*;
import net.namekdev.cosmos_is_alive.component.base.*;
import net.namekdev.cosmos_is_alive.component.render.*;
import net.namekdev.cosmos_is_alive.enums.C;
import net.namekdev.cosmos_is_alive.enums.CollisionGroups;
import net.namekdev.cosmos_is_alive.enums.Tags;
import net.namekdev.cosmos_is_alive.system.base.collision.Collider;
import net.namekdev.cosmos_is_alive.system.base.collision.ColliderType;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class EntityFactory extends PassiveSystem {
	private TagManager tags;

	@Wire public Assets assets;

	private final Vector3 pos = new Vector3();


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

	public Entity createPlayerShip(float x, float y, float z) {
		Entity player = world.createEntity();
		EntityEdit e = player.edit();
		tags.register(Tags.Player, player);

		setModel(e, new ModelInstance(assets.ship));
		e.create(Dimensions.class).set(C.Player.Width, C.Player.Height, 1);
		e.create(Transform.class)
			.xyz(x, y, z)
			.look(0, 1, 0)
			.assetRotation.setEulerAngles(0, 180, 180);
		e.create(Speed.class);
		e.create(Scale.class).set(2f);
		e.create(Collider.class).setup(CollisionGroups.PLAYER_2D, ColliderType.CIRCLE);

		return player;
	}

	public void createStars(float mapWidth, float mapHeight, float mapDepth) {
		Entity stars = null;
		EntityEdit e = null;
/*
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
		xyz(e, 0, mapWidth/2, 0).look(0, -1, 0);*/

		stars = world.createEntity();
		e = stars.edit();

		ModelBuilder mb = new ModelBuilder();

		TextureAttribute texAttr = TextureAttribute.createDiffuse(assets.stars);
		Material material = new Material(texAttr);
		Model model = mb.createSphere(mapWidth, mapHeight, mapDepth, 50, 50, material, Usage.Position|Usage.TextureCoordinates|Usage.Normal);
		model.materials.get(0).set(new IntAttribute(IntAttribute.CullFace, 0));
		setModel(e, new ModelInstance(model));
		xyz(e, 0, 0, 0);
	}

	public Entity createPlanet(float x, float y, float z, float size, float orbitAltitude, TextureRegion texture) {
		Entity planet = world.createEntity();
		EntityEdit e = planet.edit();

		ModelBuilder mb = new ModelBuilder();
		Material material = new Material(
			TextureAttribute.createDiffuse(texture)
		);
		Model model = mb.createSphere(size, size, size, (int)size*4, (int)size*4, material, Usage.Position|Usage.TextureCoordinates|Usage.Normal);

		setModel(e, new ModelInstance(model));
		e.create(Dimensions.class).set(size, size, size);
		e.create(Transform.class).xyz(x, y, z);
		e.create(Orbit.class).set(orbitAltitude, Vector3.X, Vector3.Y);
		e.create(Collider.class).setup(CollisionGroups.PLANETS_2D, ColliderType.CIRCLE);

		return planet;
	}

	public void createScrapOrbitingAroundPlanet(Entity planet, int scrapAroundCount, float minSize, float maxSize, float orbitSpeed) {
		Orbit orbit = planet.getComponent(Orbit.class);
		Vector3 planetPos = planet.getComponent(Transform.class).currentPos;
		float planetSize = planet.getComponent(Dimensions.class).getWidth();
		float orbitAltitude = planetSize/2 + orbit.altitude;

		ModelBuilder mb = new ModelBuilder();
		Vector3 startPos = new Vector3(orbitAltitude, 0, 0);

		for (int i = 0; i < scrapAroundCount; ++i) {
			Entity scrap = world.createEntity();
			EntityEdit e = scrap.edit();

			pos.set(startPos).scl(MathUtils.random(0.9f, 1.1f));
			pos.rotate(orbit.orbitRotationAxis, MathUtils.random(360));
			pos.rotate(orbit.orbitAxis, MathUtils.random(15)-7.5f);
			pos.add(planetPos);
			e.create(Transform.class).xyz(pos);

			e.create(Orbiter.class).orbitEntityId = planet.getId();
			e.create(Speed.class).speed = orbitSpeed + MathUtils.random(-0.7f, 0.7f) * orbitSpeed;

			float size = MathUtils.random(minSize, maxSize);
			e.create(Dimensions.class).set(size, size, size);

			Material material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
			Model model = mb.createSphere(size, size, size, 4, 4, material, Usage.Position|Usage.TextureCoordinates|Usage.Normal);
			setModel(e, new ModelInstance(model));
		}
	}
}
