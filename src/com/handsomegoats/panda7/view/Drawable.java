package com.handsomegoats.panda7.view;

import android.graphics.Canvas;

import com.handsomegoats.panda7.controller.AController;

public interface Drawable {
  public void update(AController controller, double gametime, double delta);

  public void draw(Canvas canvas);
}
