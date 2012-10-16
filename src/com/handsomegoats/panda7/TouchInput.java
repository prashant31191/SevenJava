package com.handsomegoats.panda7;

import android.view.MotionEvent;

public class TouchInput implements Input {
  
  /**
   * Drop the active tile
   */
  public boolean drop(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN)
      return true;

    return false;
  }

  /**
   * Move the active tile's position
   */
  public boolean move(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_MOVE)
      return true;
    
    return false;
  }
  

}
