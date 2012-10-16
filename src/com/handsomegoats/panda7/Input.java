package com.handsomegoats.panda7;

import android.view.MotionEvent;

public interface Input {

  public boolean drop(MotionEvent event);
  public boolean move(MotionEvent event);
}
