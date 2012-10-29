package com.handsomegoats.panda7.view;

import java.io.Serializable;

import android.graphics.Canvas;

public abstract class AbstractAnimation implements Serializable {
  private static final long serialVersionUID = 1L;
  public boolean            destroy          = false;

  public void update(long gameTime, double delta) {
    // TODO: Update stuff
  }

  public void draw(Canvas canvas) {
    // TODO Auto-generated method stub

  }
}
