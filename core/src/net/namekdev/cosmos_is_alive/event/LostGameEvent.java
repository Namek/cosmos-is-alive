package net.namekdev.cosmos_is_alive.event;

import net.mostlyoriginal.api.event.common.Event;
import net.namekdev.cosmos_is_alive.component.GameState;

public class LostGameEvent implements Event {
	public GameState state;
	
	public LostGameEvent(GameState state) {
		this.state = state;
	}
}
