package com.handsomegoats.panda7.view;

import java.util.ArrayList;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AbstractController;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ViewTitle extends AbstractView {
  private static final String TAG          = ViewTitle.class.getSimpleName();

  Rectangle                   srcTitle     = new Rectangle(0, 0, 480, 400);
  Rectangle                   srcAdventure = new Rectangle(0, 208, 480, 80);
  Rectangle                   srcQuickPlay = new Rectangle(0, 288, 480, 80);
  Rectangle                   srcHowToPlay = new Rectangle(0, 368, 480, 80);

  public Rectangle            dstAdventure;
  public Rectangle            dstQuickPlay;
  public Rectangle            dstHowToPlay;
  Rectangle                   destHeader;
  Rectangle                   dstTitle;
  Rectangle                   dstMenuItem;

  double                      HEADER_H     = 0.1;
  double                      TITLE_H      = 0.5;
  double                      MENU_H       = 0.1;

  public ViewTitle() {
    Main.debug(TAG, "ViewTitle");
    double ratioTitle = 0.833;
    double rationMenuItem = 0.167;
    double scale = 1;

    do {
      int x = (int) (Game.SCREEN_WIDTH - ((double) Game.SCREEN_WIDTH * scale)) / 2;

      destHeader = new Rectangle(x, 0, Game.SCREEN_WIDTH, (HEADER_H * Game.SCREEN_HEIGHT));
      dstTitle = new Rectangle(x, destHeader.y + destHeader.h, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH * ratioTitle);
      dstMenuItem = new Rectangle(x, dstTitle.y + dstTitle.h, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH * rationMenuItem);

      destHeader.scale(scale);
      dstTitle.scale(scale);
      dstMenuItem.scale(scale);

      // Create Button
      int buttonStart = (dstTitle.y + dstTitle.h);

      dstAdventure = dstMenuItem.clone();
      dstAdventure.y = buttonStart + (dstMenuItem.h * 0);
      dstQuickPlay = dstMenuItem.clone();
      dstQuickPlay.y = buttonStart + (dstMenuItem.h * 1);
      dstHowToPlay = dstMenuItem.clone();
      dstHowToPlay.y = buttonStart + (dstMenuItem.h * 2);

      scale -= 0.05;
    } while (dstHowToPlay.y + dstHowToPlay.h > Game.SCREEN_HEIGHT * 0.9);

    // Scale if HD
    if (Game.hd) {
      ArrayList<Rectangle> sources = new ArrayList<Rectangle>();
      sources.add(srcTitle);
      sources.add(srcAdventure);
      sources.add(srcQuickPlay);
      sources.add(srcHowToPlay);

      for (Rectangle r : sources)
        r.scale(2.0);
    }
  }

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub
    touchMove(touchX, touchY);
  }

  public void draw(Canvas canvas) {
    // Draw Background
    Paint paint = new Paint();
    paint.setColor(Game.cGreenBack);
    canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);

    // Source Rects
    Rect srcTitle = this.srcTitle.getRect();
    Rect srcQuickPlay = this.srcQuickPlay.getRect();
    Rect srcHowToPlay = this.srcHowToPlay.getRect();

    // Destination Rects
    Rect dstTitle = this.dstTitle.getRect();
    Rect dstQuickPlay = this.dstQuickPlay.getRect();
    Rect dstHowToPlay = this.dstHowToPlay.getRect();

    // Draw Menu Items
    canvas.drawBitmap(Game.title, srcTitle, dstTitle, null);
    canvas.drawBitmap(Game.titlesprites, srcQuickPlay, dstQuickPlay, null);
    canvas.drawBitmap(Game.titlesprites, srcHowToPlay, dstHowToPlay, null);

  }

  public void touchMove(float x, float y) {
    srcAdventure.x = 0;
    srcQuickPlay.x = 0;
    srcHowToPlay.x = 0;

    if (Rectangle.intersects(dstAdventure, x, y))
      srcAdventure.x = srcAdventure.w * 1;

    if (Rectangle.intersects(dstQuickPlay, x, y))
      srcQuickPlay.x = srcQuickPlay.w * 1;

    if (Rectangle.intersects(dstHowToPlay, x, y))
      srcHowToPlay.x = srcHowToPlay.w * 1;
  }

}
