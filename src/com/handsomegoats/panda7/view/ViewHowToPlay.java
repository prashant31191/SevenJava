package com.handsomegoats.panda7.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerHowToPlay;
import com.handsomegoats.panda7.controller.ControllerHowToPlay.HowToPage;

public class ViewHowToPlay extends AbstractView {

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    ControllerHowToPlay c = (ControllerHowToPlay) Game.controller;
    HowToPage currentScreen = c.currentScreen;

    Paint paint = new Paint();
    paint.setColor(Color.WHITE);
    canvas.drawRect(new Rect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT), paint);

    String howToPlayRelease = "Release the panda's numbered block by tapping the column where you want it to fall.";
    String howToPlayMatch = "A block disappears when it's number matches the amount of blocks in it's row/column.";
    String howToPlayBricks = "Brick blocks break, then reveal a numbered block when other blocks adjacent to it disappear.";
    String howToPlayCounter = "The counter shows how many drops till a row of stones pushes up all your blocks.";
    String howToPlayGameOver = "The game is over when your blocks are pushed past the top or when there is nowhere to place a block.";

    switch (currentScreen) {

    case Release:
      drawParagraphText(canvas, howToPlayRelease);
      drawHowTo(canvas, Game.howToImages[0]);
      break;
    case Match0:
      drawParagraphText(canvas, howToPlayMatch);
      drawHowTo(canvas, Game.howToImages[1]);
      break;
    case Match1:
      drawParagraphText(canvas, howToPlayMatch);
      drawHowTo(canvas, Game.howToImages[2]);
      break;
    case Bricks:
      drawParagraphText(canvas, howToPlayBricks);
      drawHowTo(canvas, Game.howToImages[3]);
      break;
    case Counter:
      drawParagraphText(canvas, howToPlayCounter);
      drawHowTo(canvas, Game.howToImages[4]);
      break;
    case GameOver:
      drawParagraphText(canvas, howToPlayGameOver);
      drawHowTo(canvas, Game.howToImages[5]);
      break;
    }
  }

  private void drawHowTo(Canvas canvas, Bitmap image) {
    double percentOfScreen = 0.4;

    int w = (int) (Game.SCREEN_WIDTH * percentOfScreen);
    int x = 0; // (Game.SCREEN_WIDTH - w) / 2;
    int h = (int) ((double) image.getHeight() * ((double) w / image.getWidth()));
    int y = Game.SCREEN_HEIGHT - h - 16;
    Rect src = new Rect(0, 0, image.getWidth(), image.getHeight());
    Rectangle dst = new Rectangle(x, y, w, h);
    canvas.drawBitmap(image, src, dst.getRect(), null);
  }

  private void drawParagraphText(Canvas canvas, String howToPlayRelease) {
    double percentOfScreen = 0.6;

    TextPaint mTextPaint = new TextPaint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setTypeface(Game.font);
    mTextPaint.setColor(Color.BLACK);
    mTextPaint.setTextSize(24);

    if (canvas.getWidth() > 480)
      mTextPaint.setTextSize(24 * 1.5f);

    StaticLayout mTextLayout = new StaticLayout(howToPlayRelease, mTextPaint, (int) (canvas.getWidth() * percentOfScreen),
        Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    canvas.save();
    int textX = (int) (Game.SCREEN_WIDTH - (Game.SCREEN_WIDTH * percentOfScreen));
    int textY = 16;

    canvas.translate(textX, textY);
    mTextLayout.draw(canvas);
    canvas.restore();
  }

}
