package net.namekdev.cosmos_is_alive.system.render.renderers;


public interface IRenderer {
	void initialize();
	
	void begin();
	void end();
	
	void draw(int entityId);
}
