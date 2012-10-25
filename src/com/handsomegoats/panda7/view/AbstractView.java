package com.handsomegoats.panda7.view;

public abstract class AbstractView implements InterfaceView {
  float touchX = 0;
  float touchY = 0;

  public void setTouchCoords(float x, float y) {
    touchX = x;
    touchY = y;
  }
}
