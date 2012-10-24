package com.handsomegoats.panda7.view;

import java.util.ArrayList;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.controller.TitleScreenController;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class TitleView implements IView {
  private static final String TAG             = TitleView.class.getSimpleName();
  Rectangle                   sourceTitle     = new Rectangle(0, 0, 480, 400);
  Rectangle                   sourceAdventure = new Rectangle(0, 208, 480, 80);
  Rectangle                   sourceQuickPlay = new Rectangle(0, 288, 480, 80);
  Rectangle                   sourceHowTo     = new Rectangle(0, 368, 480, 80);

  Bitmap                      titleImage;
  Bitmap                      titleSpriteSheet;

  public TitleView() {
    // Add Images
    this.titleSpriteSheet = Game.titlesprites;
    this.titleImage = Game.title;

    // Scale if HD
    if (Game.hd) {
      ArrayList<Rectangle> sources = new ArrayList<Rectangle>();
      sources.add(sourceTitle);
      sources.add(sourceAdventure);
      sources.add(sourceQuickPlay);
      sources.add(sourceHowTo);

      for (Rectangle r : sources)
        r.scale(2.0);
    }
  }

  public void update(AController controller, double gametime, double delta) {
    // TODO Auto-generated method stub
    TitleScreenController c = (TitleScreenController) controller;
    touchMove(c.touchX, c.touchY);
  }

  public void draw(Canvas canvas) {
    // Draw Background
    Paint paint = new Paint();
    paint.setColor(Game.cGreenBack);
    canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);

    // Draw Menu Items
    canvas.drawBitmap(titleImage, sourceTitle.getRect(), TitleScreenController.destTitle.getRect(), null);
    canvas.drawBitmap(titleSpriteSheet, sourceAdventure.getRect(), TitleScreenController.destAdventure.getRect(), null);
    canvas.drawBitmap(titleSpriteSheet, sourceQuickPlay.getRect(), TitleScreenController.destQuickPlay.getRect(), null);
    canvas.drawBitmap(titleSpriteSheet, sourceHowTo.getRect(), TitleScreenController.destHowToPlay.getRect(), null);
  }

  public void touchMove(float x, float y) {
    // TODO Highlight position if over menu item
    sourceAdventure.x = 0;
    sourceQuickPlay.x = 0;
    sourceHowTo.x = 0;

    if (Rectangle.intersects(TitleScreenController.destAdventure, x, y))
      sourceAdventure.x = sourceAdventure.w * 1;

    if (Rectangle.intersects(TitleScreenController.destQuickPlay, x, y))
      sourceQuickPlay.x = sourceQuickPlay.w * 1;

    if (Rectangle.intersects(TitleScreenController.destHowToPlay, x, y))
      sourceHowTo.x = sourceHowTo.w * 1;
  }

  public int getTileSize() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getXOffset() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getYOffset() {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getGap() {
    // TODO Auto-generated method stub
    return 0;
  }

}
