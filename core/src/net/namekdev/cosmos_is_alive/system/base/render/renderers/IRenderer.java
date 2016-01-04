package net.namekdev.cosmos_is_alive.system.base.render.renderers;


public interface IRenderer {
	void initialize();
	
	/**
	 * Used to distinguish whether previous renderer needs to be flushed.
	 */
	Object getBatch();
	
	void begin();
	void end();
	
	void draw(int entityId);
}
