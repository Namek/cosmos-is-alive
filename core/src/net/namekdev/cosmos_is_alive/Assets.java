package net.namekdev.cosmos_is_alive;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	private static final String BASIC_TEXTURES_ATLAS = "graphics/basic/pack.atlas";
	private static final String PLANET_TEXTURES_ATLAS = "graphics/planets/pack.atlas";

	public BitmapFont font;
	public AssetManager assets;

	public TextureAtlas textures;
	public TextureRegion intro;
	public TextureRegion instruction;
	public TextureRegion playerTex;
	public TextureRegion stars;

	public TextureAtlas planets;
	public TextureRegion planetEarth;
	public TextureRegion planetSaturn;
	public TextureRegion planetNeptune;


	public void loadAll() {
		font = new BitmapFont();

		assets = new AssetManager();
		assets.load(BASIC_TEXTURES_ATLAS, TextureAtlas.class);
		assets.load(PLANET_TEXTURES_ATLAS, TextureAtlas.class);
		assets.finishLoading();

		textures = assets.get(BASIC_TEXTURES_ATLAS);
		intro = textures.findRegion("intro");
		instruction = textures.findRegion("instruction");
		playerTex = textures.findRegion("star_fish");
		stars = textures.findRegion("stars");

		planets = assets.get(PLANET_TEXTURES_ATLAS);
		planetEarth = planets.findRegion("earth_clouds");
		planetSaturn = planets.findRegion("saturn");
		planetNeptune = planets.findRegion("neptune");
	}

}
