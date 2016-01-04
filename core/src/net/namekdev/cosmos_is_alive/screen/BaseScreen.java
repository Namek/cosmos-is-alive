package net.namekdev.cosmos_is_alive.screen;

import net.namekdev.cosmos_is_alive.Assets;
import net.namekdev.cosmos_is_alive.MyNGame;
import net.namekdev.cosmos_is_alive.util.DefaultShaderWatchableProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public abstract class BaseScreen<T extends BaseScreen<T>> extends ScreenAdapter {
	protected MyNGame game;
	protected PerspectiveCamera camera;
	protected OrthographicCamera camera2d;
	protected SpriteBatch sprites;
	protected DecalBatch decals;
	protected ModelBatch models;
	protected ShapeRenderer shapes;
	protected DefaultShaderWatchableProvider shaderProvider;
	protected Assets assets;
	
	public T init(MyNGame game) {
		this.game = game;
		camera = new PerspectiveCamera(67, sw(), sh());
		camera2d = new OrthographicCamera(sw(), sh());
		sprites = new SpriteBatch();
		decals = new DecalBatch(new CameraGroupStrategy(camera));
		shapes = new ShapeRenderer();

		Config shaderConfig = new Config();
		shaderConfig.defaultCullFace = 0;
		shaderProvider = new DefaultShaderWatchableProvider(
			shaderConfig,
			Gdx.files.internal("shaders/basic.vertex.glsl"),
			Gdx.files.internal("shaders/basic.fragment.glsl")
		);
		models = new ModelBatch(shaderProvider);

		assets = game.getAssets();
		
		return (T) this;
	}

	@Override
	public void dispose() {
		sprites.dispose();
		decals.dispose();
		models.dispose();
		shapes.dispose();
		shaderProvider.dispose();
	}

	public void popScreen() {
		game.popScreen(this);
	}
	
	protected void darkenBackground() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(0, 0, 0, 0.8f);
		shapes.rect(0, 0, sw(), sh());
		shapes.end();
	}
	
	protected float sw() {
		return Gdx.graphics.getWidth();
	}
	
	protected float sh() {
		return Gdx.graphics.getHeight();
	}
}
