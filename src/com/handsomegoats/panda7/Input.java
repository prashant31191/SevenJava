package com.handsomegoats.panda7;

import android.view.MotionEvent;

public interface Input {

  public boolean down(MotionEvent event);

  public boolean move(MotionEvent event);

  public boolean press(MotionEvent event);
}
