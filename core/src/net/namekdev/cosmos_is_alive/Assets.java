package net.namekdev.cosmos_is_alive;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	private static final String TEXTURES_ATLAS = "pack.atlas";
	
	public BitmapFont font;
	public AssetManager assets;
	public TextureAtlas textures;
	
	public TextureRegion intro;
	public TextureRegion instruction;
	public TextureRegion playerTex;

	

	public void loadAll() {
		font = new BitmapFont();
		
		assets = new AssetManager();
		assets.load(TEXTURES_ATLAS, TextureAtlas.class);
		assets.finishLoading();

		textures = assets.get(TEXTURES_ATLAS);
		intro = textures.findRegion("intro");
		instruction = textures.findRegion("instruction");
		playerTex = textures.findRegion("star_fish");
	}

}
