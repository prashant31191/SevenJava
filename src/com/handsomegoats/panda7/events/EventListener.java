package com.handsomegoats.panda7.events;

import java.util.ArrayList;

import android.util.Log;

import com.handsomegoats.panda7.*;

public class EventListener {
  private static final String TAG = EventListener.class.getSimpleName();
  private ArrayList<Event>    listeners;
  private GameController g;

  public EventListener(GameController g) {
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