package net.namekdev.cosmos_is_alive.system.base.events;

import net.mostlyoriginal.api.event.common.Event;

public final class Signal implements Event {
	public int code;
	
	public Signal(int code) {
		this.code = code;
	}
}
