package net.namekdev.cosmos_is_alive;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;

public class Assets {
	private static final String BASIC_TEXTURES_ATLAS = "graphics/basic/pack.atlas";
	private static final String PLANET_TEXTURES_ATLAS = "graphics/planets/pack.atlas";
	private static final String BACKGROUND_PATH = "graphics/background/";
	private static final String STARS_BACKGROUND = BACKGROUND_PATH + "stars.jpg";

	public BitmapFont font;
	public AssetManager assets;

	public Model ship;

	public TextureAtlas textures;
	public TextureRegion intro;
	public TextureRegion instruction;
	public TextureRegion playerTex;

	public TextureAtlas planets;
	public TextureRegion planetEarth;
	public TextureRegion planetSaturn;
	public TextureRegion planetNeptune;

	public Texture stars;


	public void loadAll() {
		font = new BitmapFont();

		assets = new AssetManager();
		assets.load("models/ship.g3db", Model.class);
		assets.load(BASIC_TEXTURES_ATLAS, TextureAtlas.class);
		assets.load(PLANET_TEXTURES_ATLAS, TextureAtlas.class);
		assets.load(STARS_BACKGROUND, Texture.class);
		assets.finishLoading();

		ship = assets.get("models/ship.g3db", Model.class);

		textures = assets.get(BASIC_TEXTURES_ATLAS);
		intro = textures.findRegion("intro");
		instruction = textures.findRegion("instruction");
		playerTex = textures.findRegion("star_fish");

		planets = assets.get(PLANET_TEXTURES_ATLAS);
		planetEarth = planets.findRegion("earth_clouds");
		planetSaturn = planets.findRegion("saturn");
		planetNeptune = planets.findRegion("neptune");

		stars = assets.get(STARS_BACKGROUND);
		stars.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

}
