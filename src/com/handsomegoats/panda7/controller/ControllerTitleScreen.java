package com.handsomegoats.panda7.controller;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.Rectangle;

import android.graphics.Canvas;

public class ControllerTitleScreen extends AbstractController implements InterfaceController {
  private static final String TAG      = ControllerTitleScreen.class.getSimpleName();

  double                      HEADER_H = 0.1;
  double                      TITLE_H  = 0.5;
  double                      MENU_H   = 0.1;

  private Rectangle           destHeader;
  public static Rectangle     destTitle;
  private Rectangle           destMenuItem;

  long                        gameTime = 0;
  double                      delta    = 0;
  long                        then     = 0;

  public static Rectangle     destAdventure;
  public static Rectangle     destQuickPlay;
  public static Rectangle     destHowToPlay;

  public float                touchX   = 0;
  public float                touchY   = 0;

  public ControllerTitleScreen() {
    Main.debug(TAG, "TitleScreenController Started");

    double ratioTitle = 0.83333333333333333333333333333333;
    double rationMenuItem = 0.16666666666666666666666666666667;
    double scale = 1;

    do {
      int x = (int) (Game.SCREEN_WIDTH - ((double) Game.SCREEN_WIDTH * scale)) / 2;

      destHeader = new Rectangle(x, 0, Game.SCREEN_WIDTH, (HEADER_H * Game.SCREEN_HEIGHT));
      destTitle = new Rectangle(x, destHeader.y + destHeader.h, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH * ratioTitle);
      destMenuItem = new Rectangle(x, destTitle.y + destTitle.h, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH * rationMenuItem);

      destHeader.scale(scale);
      destTitle.scale(scale);
      destMenuItem.scale(scale);

      // Create Button
      int buttonStart = (destTitle.y + destTitle.h);

      destAdventure = destMenuItem.clone();
      destAdventure.y = buttonStart + (destMenuItem.h * 0);
      destQuickPlay = destMenuItem.clone();
      destQuickPlay.y = buttonStart + (destMenuItem.h * 1);
      destHowToPlay = destMenuItem.clone();
      destHowToPlay.y = buttonStart + (destMenuItem.h * 2);

      scale -= 0.05;
    } while (destHowToPlay.y + destHowToPlay.h > Game.SCREEN_HEIGHT * 0.9);

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
