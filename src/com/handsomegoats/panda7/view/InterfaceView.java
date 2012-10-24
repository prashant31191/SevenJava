package com.handsomegoats.panda7.view;

import com.handsomegoats.panda7.controller.AbstractController;

import android.graphics.Canvas;

public interface InterfaceView {
  public void update(AbstractController controller, double gametime, double delta);

  public void draw(Canvas canvas);
}
