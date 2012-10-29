package com.handsomegoats.panda7.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AbstractController;

public class ViewHighScore extends AbstractView {

  int       score;

  Rectangle srcGameOver       = new Rectangle(0, 608, 480, 80);
  Rectangle srcScore          = new Rectangle(0, 688, 480, 80);
  Rectangle srcTopScores      = new Rectangle(0, 768, 480, 80);
  Rectangle srcAllTimeAverage = new Rectangle(0, 848, 480, 80);
  Rectangle srcPlayAgain      = new Rectangle(0, 448, 480, 80);
  Rectangle srcReturnToTitle  = new Rectangle(0, 528, 480, 80);

  Rectangle dstGameOver;
  Rectangle dstScore;
  Rectangle dstTopScores;
  Rectangle dstAllTimeAverage;
  public Rectangle dstPlayAgain;
  public Rectangle dstReturnToTitle;

  double    GAME_OVER         = 0.13;
  double    SCORE             = 0.25;
  double    TOP_SCORES        = 0.35;
  double    AVERAGE           = 0.6;
  double    PLAY_AGAIN        = 0.7;

  double    scaleBtnWidth     = 0.85;
  double    scaleSpaceBottom  = 0.1;

  public ViewHighScore(int score) {
    this.score = score;

    // Add score to database
    Main.insertIntoDB(Game.difficulty.name(), Integer.toString(this.score));

    // Change sizes if HD
    if (Game.hd) {
      srcGameOver.scale(2.0);
      srcScore.scale(2.0);
      srcTopScores.scale(2.0);
      srcAllTimeAverage.scale(2.0);
      srcPlayAgain.scale(2.0);
      srcReturnToTitle.scale(2.0);
    }

    // Work out Destination Rectangles
    double space = 0.05 * Game.SCREEN_WIDTH;
    double gameOverWidth = Game.SCREEN_WIDTH - (2 * space);
    double gameOverHeight = srcPlayAgain.h * ((double) gameOverWidth / srcPlayAgain.w);

    dstGameOver = new Rectangle(space, Game.SCREEN_HEIGHT * GAME_OVER, gameOverWidth, gameOverHeight);
    dstScore = new Rectangle(space, Game.SCREEN_HEIGHT * SCORE, gameOverWidth, gameOverHeight);
    dstTopScores = new Rectangle(space, Game.SCREEN_HEIGHT * TOP_SCORES, gameOverWidth, gameOverHeight);
    dstAllTimeAverage = new Rectangle(space, Game.SCREEN_HEIGHT * AVERAGE, gameOverWidth, gameOverHeight);
    dstPlayAgain = new Rectangle(space, Game.SCREEN_HEIGHT * PLAY_AGAIN, gameOverWidth, gameOverHeight);
    dstReturnToTitle = new Rectangle(space, dstPlayAgain.y + dstPlayAgain.h, gameOverWidth, gameOverHeight);
  }

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    // Draw Background
    Paint paint = new Paint();
    paint.setColor(Game.cGreenBack);
    canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);
    
    Rect srcGameOver = this.srcGameOver.getRect();
    Rect srcScore = this.srcScore.getRect();
    Rect srcTopScores = this.srcTopScores.getRect();
    Rect srcAllTimeAverage = this.srcAllTimeAverage.getRect();
    Rect srcPlayAgain = this.srcPlayAgain.getRect();
    Rect srcReturnToTitle = this.srcReturnToTitle.getRect();
    
    Rect dstGameOver = this.dstGameOver.getRect();
    Rect dstScore = this.dstScore.getRect();
    Rect dstTopScores = this.dstTopScores.getRect();
    Rect dstAllTimeAverage = this.dstAllTimeAverage.getRect();
    Rect dstPlayAgain = this.dstPlayAgain.getRect();
    Rect dstReturnToTitle = this.dstReturnToTitle.getRect();
    
    // Draw Words
    canvas.drawBitmap(Game.titlesprites, srcGameOver, dstGameOver, null);
    canvas.drawBitmap(Game.titlesprites, srcScore, dstScore, null);
    canvas.drawBitmap(Game.titlesprites, srcTopScores, dstTopScores, null);
    canvas.drawBitmap(Game.titlesprites, srcAllTimeAverage, dstAllTimeAverage, null);
    canvas.drawBitmap(Game.titlesprites, srcPlayAgain, dstPlayAgain, null);
    canvas.drawBitmap(Game.titlesprites, srcReturnToTitle, dstReturnToTitle, null);
    
    // TODO Auto-generated method stub
    // Show current score
    // Draw Top 5 scores
    // Highlight current score if it's in the top 5
    // Button New Game
    // Button Title Screen
  }

}
