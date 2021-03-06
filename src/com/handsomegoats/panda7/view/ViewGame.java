package com.handsomegoats.panda7.view;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerGame;

public class ViewGame extends AbstractView {
  public static int[]         tileColors      = { Color.argb(255, 0, 155, 77), Color.argb(255, 255, 40, 1), Color.argb(255, 3, 22, 254),
      Color.argb(255, 254, 147, 13), Color.argb(255, 0, 128, 156), Color.argb(255, 134, 15, 253), Color.argb(255, 87, 209, 27),
      Color.argb(255, 162, 23, 68), Color.argb(255, 115, 0, 81), };

  private static final String TAG             = ViewGame.class.getSimpleName();

  // Images
  Bitmap                      sprites;
  Bitmap                      background;
  Bitmap                      clouds;

  // Sprite Dimensions
  int                         blankOffset     = 10;
  int                         brickOffset     = 11;
  int                         brokenOffset    = 12;

  Rectangle                   srcBackground   = new Rectangle(0, 0, 480, 1024);
  Rectangle                   srcClouds       = new Rectangle(0, 0, 630, 225);
  Rectangle                   srcTile         = new Rectangle(0, 0, 64, 64);
  Rectangle                   srcBtn          = new Rectangle(0, 64, 128, 128);
  Rectangle                   srcPanda        = new Rectangle(384, 64, 128, 128);
  Rectangle                   srcPandaHands   = new Rectangle(512, 64, 128, 128);
  Rectangle                   srcCounter      = new Rectangle(0, 192, 32, 32);
  Rectangle                   srcNumber       = new Rectangle(0, 224, 53, 64);
  Rectangle                   srcWhiteNumber  = new Rectangle(0, 288, 53, 64);

  double                      scale           = 1;

  // Layout Settings
  double                      PERCENT_SIDES   = 0.1;
  double                      PERCENT_SPACING = 0.01;
  double                      PERCENT_BOTTOM  = 3;
  double                      PERCENT_SCORE   = 0.6;

  // private Rectangle destHeader;
  private Rectangle[]         dstSides        = new Rectangle[2];
  private Rectangle[][]       dstTiles        = new Rectangle[Game.GRID_SIZE][Game.GRID_SIZE];

  // private Rectangle destBottom;
  private Rectangle           dstScore;
  private Rectangle           dstBackground;
  private Rectangle           dstCounter;

  private int                 gap;
  private int                 xOffset;
  private int                 yOffset;
  private int                 tileSize;
  private int                 counterGap      = 5;
  private int                 pandaOffset     = -30;

  public ViewGame() {
    // Scale if HD
    if (Game.hd) {
      ArrayList<Rectangle> sources = new ArrayList<Rectangle>();
      sources.add(srcBackground);
      sources.add(srcClouds);
      sources.add(srcTile);
      sources.add(srcBtn);
      sources.add(srcPanda);
      sources.add(srcPandaHands);
      sources.add(srcCounter);
      sources.add(srcNumber);
      sources.add(srcWhiteNumber);

      for (Rectangle r : sources)
        r.scale(2.0);
    }

    // yOffset

    // Calculate Sizes
    int sidesW = (int) Math.floor(PERCENT_SIDES * Game.SCREEN_WIDTH / 2);
    int gameAreaWH = (int) Math.floor(Game.SCREEN_WIDTH - (sidesW * 2));
    gap = (int) Math.floor(gameAreaWH * PERCENT_SPACING);
    tileSize = (int) Math.floor((gameAreaWH - (gap * (Game.GRID_SIZE - 1))) / Game.GRID_SIZE);

    int bottom = (int) Math.floor(sidesW * PERCENT_BOTTOM);
    int score = (int) Math.floor(bottom * PERCENT_SCORE);
    int header = Game.SCREEN_HEIGHT - gameAreaWH - bottom;

    xOffset = sidesW;
    yOffset = header;

    // Create Destination Rectangles
    createDestinationRects(sidesW, gameAreaWH, gap, tileSize, bottom, score, header);

    // Set image source
    sprites = Game.sprites;
    background = Game.background;
    clouds = Game.clouds;

    Main.debug(TAG, "ViewGame Started");
  }

  private void createDestinationRects(int sidesW, int gameAreaWH, int gapWH, int tileSizeWH, int bottomH, int scoreH, int headerH) {
    // Sides
    this.dstSides[0] = new Rectangle(0, headerH, sidesW, gameAreaWH);
    this.dstSides[1] = new Rectangle(Game.SCREEN_WIDTH - sidesW, headerH, sidesW, gameAreaWH);

    // Tiles
    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        this.dstTiles[y][x] = new Rectangle(sidesW + ((tileSizeWH + gapWH) * x), headerH + ((tileSizeWH + gapWH) * y), tileSizeWH,
            tileSizeWH);
      }
    }

    // Score
    double scoreScale = (double) scoreH / srcNumber.h;
    int numberWidth = (int) ((double) srcNumber.w * scoreScale);
    int numberHeight = (int) ((double) srcNumber.h * scoreScale);

    this.dstScore = new Rectangle(sidesW, Game.SCREEN_HEIGHT - bottomH + gapWH, numberWidth, numberHeight);

    // New Row Counter
    this.dstCounter = new Rectangle(sidesW, sidesW, sidesW, sidesW);

    // Set destBackground
    this.dstBackground = new Rectangle(0, 0, Game.SCREEN_WIDTH, srcBackground.h);

    // Set tile size for reference
    this.tileSize = tileSizeWH;

    // Set gap size for reference
    this.gap = gapWH;

  }

  public Vector2 coordsToPixels(int x, int y) {
    int px = xOffset + (x * (tileSize + gap));
    int py = yOffset + (y * (tileSize + gap));

    Vector2 coords = new Vector2(px, py);

    return coords;
  }

  public Vector2 pixelsToCoords(float x, float y) {
    int cx = (int) Math.floor(((double) x - xOffset) / (tileSize + gap));
    int cy = (int) Math.floor(((double) y - yOffset) / (tileSize + gap));

    if (cx > Game.GRID_SIZE - 1)
      cx = Game.GRID_SIZE - 1;

    if (cy > Game.GRID_SIZE - 1)
      cy = Game.GRID_SIZE - 1;

    if (cx < 0)
      cx = 0;

    if (cy < 0)
      cy = 0;

    Vector2 coords = new Vector2(cx, cy);

    return coords;
  }

  public void update(AbstractController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    ControllerGame controller = (ControllerGame) Game.controller;

    int[][] grid = controller.grid;
    int[] entryGrid = controller.entryGrid;
    ArrayList<AbstractAnimation> particles = controller.updateLockAnimations;
    ArrayList<AbstractAnimation> animations = controller.animations;

    // Draw Background
    drawBackground(canvas);

    // Draw Board Tiles
    drawBoardTiles(canvas, grid);

    // Draw Active Tile
    drawActiveTile(canvas, entryGrid);

    // Draw Score
    drawScore(canvas, controller.score);

    // Draw Combo Points

    // Draw Particles
    for (AbstractAnimation p : particles)
      p.draw(canvas);

    // Draw Animations
    for (AbstractAnimation a : animations)
      a.draw(canvas);

    // Draw Counter
    drawCounters(controller, canvas);
  }

  private void drawBackground(Canvas canvas) {
    // Clouds
    Paint paint = new Paint();
    paint.setColor(Game.cSkyBlue);
    canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);

    double scale = (double) Game.SCREEN_WIDTH / srcClouds.w;
    Rectangle destClouds = srcClouds.clone();
    destClouds.w = Game.SCREEN_WIDTH;
    destClouds.h = (int) (scale * destClouds.h);

    canvas.drawBitmap(clouds, srcClouds.getRect(), destClouds.getRect(), null);

    // Foreground
    scale = (double) Game.SCREEN_WIDTH / srcBackground.w;

    Rectangle destBG = dstBackground.clone();
    destBG.h *= scale;
    destBG.y = -(destBG.h - Game.SCREEN_HEIGHT);

    canvas.drawBitmap(background, srcBackground.getRect(), destBG.getRect(), null);
  }

  private void drawBoardTiles(Canvas canvas, int[][] grid) {
    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        int tileValue = grid[y][x];

        if (tileValue == Game.NO_TILE)
          tileValue = blankOffset;
        else if (tileValue == Game.BRICK_TILE)
          tileValue = brickOffset;
        else if (tileValue == Game.BROKEN_BRICK_TILE)
          tileValue = brokenOffset;

        Rectangle sourceRect = srcTile.clone();
        sourceRect.x = sourceRect.w * tileValue;

        canvas.drawBitmap(sprites, sourceRect.getRect(), dstTiles[y][x].getRect(), null);
      }
    }
  }

  private void drawScore(Canvas canvas, int number) {
    ArrayList<Integer> split = new ArrayList<Integer>();

    while (number > 0) {
      split.add(number % 10);
      number = number / 10;
    }

    Collections.reverse(split);

    for (int i = 0; i < split.size(); i++) {
      int numChar = split.get(i);

      Rectangle sourceRect = srcNumber.clone();
      sourceRect.x = sourceRect.w * numChar;

      Rectangle destRect = dstScore.clone();
      destRect.x += (i * destRect.w);

      canvas.drawBitmap(sprites, sourceRect.getRect(), destRect.getRect(), null);
    }
  }

  private void drawActiveTile(Canvas canvas, int[] entryGrid) {
    for (int i = 0; i < Game.GRID_SIZE; i++) {
      if (entryGrid[i] > 0) {
        int tileValue = entryGrid[i];

        Rectangle destPanda = dstTiles[0][i].clone();
        destPanda.x = destPanda.x - ((destPanda.w * 2) - destPanda.w) / 2;
        destPanda.y = destPanda.y - tileSize - gap - ((destPanda.h * 2) - destPanda.h) / 2 + pandaOffset;
        destPanda.w *= 2;
        destPanda.h *= 2;

        Rectangle destActiveTile = dstTiles[0][i].clone();
        destActiveTile.y = destActiveTile.y - tileSize - gap;

        Rectangle sourceActiveTile = srcTile.clone();
        sourceActiveTile.x = sourceActiveTile.w * tileValue;

        // Draw Panda
        canvas.drawBitmap(sprites, srcPanda.getRect(), destPanda.getRect(), null);

        // Draw Tile
        canvas.drawBitmap(sprites, sourceActiveTile.getRect(), destActiveTile.getRect(), null);

        // Draw Panda Hands
        canvas.drawBitmap(sprites, srcPandaHands.getRect(), destPanda.getRect(), null);
      }
    }
  }

  private void drawCounters(ControllerGame controller, Canvas canvas) {
    for (int i = 0; i < controller.difficulty.val; i++) {
      Rectangle sourceRect = srcCounter.clone();
      if (i < controller.countTillNewRow)
        sourceRect.x = sourceRect.w;

      Rectangle destRect = dstCounter.clone();
      destRect.x = dstCounter.x + (destRect.w + counterGap) * i;

      canvas.drawBitmap(sprites, sourceRect.getRect(), destRect.getRect(), null);
    }
  }

}
