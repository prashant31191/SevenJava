package com.handsomegoats.panda7.events;

import com.handsomegoats.panda7.controller.GameController;

public interface IEvent {
  public void callEvent(GameController g);
}
