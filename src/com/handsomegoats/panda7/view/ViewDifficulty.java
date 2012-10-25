package com.handsomegoats.panda7.view;

import com.handsomegoats.panda7.Game;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AbstractController;

public class ViewDifficulty extends AbstractView {
  Rectangle        srcEasy          = new Rectangle(0, 0, 410, 80);
  Rectangle        srcMedium        = new Rectangle(410, 0, 410, 80);
  Rectangle        srcHard          = new Rectangle(820, 0, 410, 80);

  public Rectangle dstEasy;
  public Rectangle dstMedium;
  public Rectangle dstHard;

  double           scaleBtnWidth    = 0.85;
  double           scaleSpaceBottom = 0.1;

  Bitmap           spriteSheet;

  public ViewDifficulty() {
    spriteSheet = Game.titlesprites;

    double btnWidth = Game.SCREEN_WIDTH * scaleBtnWidth;
    double btnHeight = srcEasy.h * ((double) btnWidth / srcEasy.w);
    double space = btnHeight / 2;

    double spaceBottom = Game.SCREEN_HEIGHT * scaleSpaceBottom;
    double x = (Game.SCREEN_WIDTH - btnWidth) / 2;
    double y = (Game.SCREEN_HEIGHT - spaceBottom - (btnHeight * 3) - (space * 3));

    dstEasy = new Rectangle(x, y + (btnHeight + space) * 0, btnWidth, btnHeight);
    dstMedium = new Rectangle(x, y + (btnHeight + space) * 1, btnWidth, btnHeight);
    dstHard = new Rectangle(x, y + (btnHeight + space) * 2, btnWidth, btnHeight);
  }

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    // Draw Background
    Paint paint = new Paint();
    paint.setColor(Game.cGreenBack);
    canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);

    // Draw Buttons
    Rect srcEasy = this.srcEasy.getRect();
    Rect srcMedium = this.srcMedium.getRect();
    Rect srcHard = this.srcHard.getRect();

    Rect dstEasy = this.dstEasy.getRect();
    Rect dstMedium = this.dstMedium.getRect();
    Rect dstHard = this.dstHard.getRect();

    canvas.drawBitmap(spriteSheet, srcEasy, dstEasy, null);
    canvas.drawBitmap(spriteSheet, srcMedium, dstMedium, null);
    canvas.drawBitmap(spriteSheet, srcHard, dstHard, null);
  }

}
