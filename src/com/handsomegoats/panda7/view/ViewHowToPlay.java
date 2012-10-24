package com.handsomegoats.panda7.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerHowToPlay;
import com.handsomegoats.panda7.controller.ControllerHowToPlay.HowToPage;

public class ViewHowToPlay implements InterfaceView {

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    // TODO Auto-generated method stub
    ControllerHowToPlay c = (ControllerHowToPlay) Game.controller;
    HowToPage currentScreen = c.currentScreen;

    Paint paint = new Paint();
    paint.setColor(Color.WHITE);
    canvas.drawRect(new Rect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT), paint);

    switch (currentScreen) {
    case Release:
      canvas.drawBitmap(Game.howToImages[0], 0, 0, null);
      break;
    case Match0:
      canvas.drawBitmap(Game.howToImages[1], 0, 0, null);
      break;
    case Match1:
      canvas.drawBitmap(Game.howToImages[2], 0, 0, null);
      break;
    case Bricks:
      canvas.drawBitmap(Game.howToImages[3], 0, 0, null);
      break;
    case Counter:
      canvas.drawBitmap(Game.howToImages[4], 0, 0, null);
      break;
    case GameOver:
      canvas.drawBitmap(Game.howToImages[5], 0, 0, null);
      break;
    }
  }

}
