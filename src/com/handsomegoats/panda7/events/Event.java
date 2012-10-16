package com.handsomegoats.panda7.events;

import android.util.Log;

import com.handsomegoats.panda7.GridController;

public class Event implements IEvent {
	private static final String TAG = Event.class.getSimpleName();

	public String name;

	// Create the event notifier and pass ourself to it.
	public Event(String name) {
		super();
		this.name = name;
	}

	// Define the actual handler for the event.
	public void callEvent(GridController g) {
		Log.i("Event", name + "Event Called " + g.grid[0][0]);
	}
}