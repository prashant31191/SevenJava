package com.handsomegoats.panda7;

import android.view.MotionEvent;

public class TouchInput implements Input {
  boolean pressed = false;

  public boolean down(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      pressed = true;
      return true;
    }

    return false;
  }

  public boolean press(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP && pressed) {
      pressed = false;
      return true;
    }

    return false;
  }

  public boolean move(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_MOVE)
      return true;

    return false;
  }

}
