package com.handsomegoats.panda7.controller;

import com.handsomegoats.panda7.Main;

import android.graphics.Canvas;

public class ControllerTitleScreen extends AbstractController implements InterfaceController {
  private static final String TAG      = ControllerTitleScreen.class.getSimpleName();

  long                        gameTime = 0;
  double                      delta    = 0;
  long                        then     = 0;

  public ControllerTitleScreen() {
    Main.debug(TAG, "TitleScreenController Started");

  }

  public void update() {
    // if (this.slowDown)

    // Game Time
    gameTime++;
    delta = (double) (then - System.currentTimeMillis()) / 1000.0;
    then = System.currentTimeMillis();
  }

  public void draw(Canvas canvas) {
  }

}
