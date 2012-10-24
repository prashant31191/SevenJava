package com.handsomegoats.panda7.view;

import android.graphics.Canvas;

import com.handsomegoats.panda7.controller.AbstractController;

public interface InterfaceDrawable {
  public void update(AbstractController controller, double gametime, double delta);

  public void draw(Canvas canvas);
}
