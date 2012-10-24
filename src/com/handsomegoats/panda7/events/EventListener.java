package com.handsomegoats.panda7.events;

import java.io.Serializable;
import java.util.ArrayList;

import com.handsomegoats.panda7.controller.ControllerGame;

public class EventListener implements Serializable {
  private static final long serialVersionUID = 1L;
  // private static final String TAG = EventListener.class.getSimpleName();
  private ArrayList<Event>    listeners;
  private ControllerGame g;

  public EventListener(ControllerGame g) {
    this.g = g;
    this.listeners = new ArrayList<Event>();
  }

  public void register(String name, Event listener) {
    listeners.add(listener);
  }

  public void notify(String name) {
    for (Event e : listeners) {
      if (name.equals(e.name)) {
        e.callEvent(g);
      }
    }
  }
}