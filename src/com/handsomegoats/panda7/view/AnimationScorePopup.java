package com.handsomegoats.panda7.view;

import java.util.ArrayList;
import java.util.Collections;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.Vector2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class AnimationScorePopup extends AbstractAnimation {
  private static final long serialVersionUID = 1L;
  int                       bonusScore;
  Rectangle                 dstScore;
  static Bitmap             sprites          = Game.sprites;
  Rectangle                 srcNumber        = new Rectangle(0, 224+64, 53, 64);
  Rectangle                 srcWhiteNumber   = new Rectangle(0, 288, 53, 64);
  long                      startTime;
  long                      duration         = 3000;                         // Milliseconds
  int                       alpha            = 255;
  int                       aDecrement       = 255 / (1000 / 60);

  public AnimationScorePopup(int x, int y, int clearScore) {
    ViewGame v = (ViewGame) Game.view;
    Vector2 p = v.coordsToPixels(x, y);

    // Value
    bonusScore = clearScore;

    // Sprite
    sprites = Game.sprites;

    if (Game.hd) {
      srcNumber.scale(2.0);
      srcWhiteNumber.scale(2.0);
    }

    int width = srcNumber.w / 2;
    int height = srcNumber.h / 2;

    // dstRect of First Char
    dstScore = new Rectangle(p.x, p.y, width, height);

    startTime = System.currentTimeMillis();
  }

  @Override
  public void update(long gameTime, double delta) {
    if (System.currentTimeMillis() - startTime > duration)
      destroy = true;

    dstScore.y -= 0.5f;

    alpha -= aDecrement;

    if (alpha < 0)
      alpha = 0;
  }

  @Override
  public void draw(Canvas canvas) {
    ArrayList<Integer> split = new ArrayList<Integer>();

    Paint paint = new Paint();
    paint.setAlpha(alpha);

    int value = bonusScore;

    while (value > 0) {
      split.add(value % 10);
      value = value / 10;
    }

    Collections.reverse(split);

    for (int i = 0; i < split.size(); i++) {
      int numChar = split.get(i);

      Rectangle srcRect = srcNumber.clone();
      Rectangle dstRect = dstScore.clone();

      srcRect.x = srcRect.w * numChar;
      dstRect.x += (i * dstRect.w);

      canvas.drawBitmap(sprites, srcRect.getRect(), dstRect.getRect(), paint);
    }
  }
}
