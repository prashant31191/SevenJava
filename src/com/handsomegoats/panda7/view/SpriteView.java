package com.handsomegoats.panda7.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Space;

import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.GameController;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.Vector2;
import com.handsomegoats.panda7.*;

public class SpriteView implements IView {
	public static int[] tileColors = { Color.argb(255, 0, 155, 77),
			Color.argb(255, 255, 40, 1), Color.argb(255, 3, 22, 254),
			Color.argb(255, 254, 147, 13), Color.argb(255, 0, 128, 156),
			Color.argb(255, 134, 15, 253), Color.argb(255, 87, 209, 27),
			Color.argb(255, 162, 23, 68), Color.argb(255, 115, 0, 81), };

	private static final String TAG = SpriteView.class.getSimpleName();

	// Images
	Bitmap sprites;
	Bitmap background;
	Bitmap clouds;

	// Sprite Dimensions
	int blankOffset = 10;
	int brickOffset = 11;
	int brokenOffset = 12;

	Rectangle sourceBackground = new Rectangle(0, 0, 480, 1024);
	Rectangle sourceClouds = new Rectangle(0, 0, 630, 225);
	Rectangle sourceTile = new Rectangle(0, 0, 64, 64);
	Rectangle sourceBtn = new Rectangle(0, 64, 128, 128);
	Rectangle sourcePanda = new Rectangle(384, 64, 128, 128);
	Rectangle sourcePandaHands = new Rectangle(512, 64, 128, 128);
	Rectangle sourceCounter = new Rectangle(0, 192, 32, 32);
	Rectangle sourceNumber = new Rectangle(0, 224, 53, 64);
	Rectangle sourceWhiteNumber = new Rectangle(0, 288, 53, 64);

	double scale = 1;

	// Layout Settings
	double PERCENT_SIDES = 0.1;
	double PERCENT_SPACING = 0.01;
	double PERCENT_BOTTOM = 3;
	double PERCENT_SCORE = 0.6;

	// Calculated dimensions
	private Rectangle destHeader;
	private Rectangle[] destSides = new Rectangle[2];
	private Rectangle destGameArea;
	private Rectangle[][] destTiles = new Rectangle[Game.GRID_SIZE][Game.GRID_SIZE];
	private Rectangle destBottom;
	private Rectangle destScore;
	private Rectangle destBackground;
	private Rectangle destCounter;

	private int gap;
	private int tileSize;
	private int counterGap = 5;
	private int pandaOffset = -30;

	public SpriteView() {
		// Scale if HD
		if (Game.hd) {
			ArrayList<Rectangle> sources = new ArrayList<Rectangle>();
			sources.add(sourceBackground);
			sources.add(sourceClouds);
			sources.add(sourceTile);
			sources.add(sourceBtn);
			sources.add(sourcePanda);
			sources.add(sourcePandaHands);
			sources.add(sourceCounter);
			sources.add(sourceNumber);
			sources.add(sourceWhiteNumber);

			for (Rectangle r : sources)
				r.scale(2.0);
		}

		// Calculate Sizes
		int sides = (int) Math.floor(PERCENT_SIDES * Game.SCREEN_WIDTH / 2);
		int gameArea = (int) Math.floor(Game.SCREEN_WIDTH - (sides * 2));
		int gap = (int) Math.floor(gameArea * PERCENT_SPACING);
		int tileSize = (int) Math
				.floor((gameArea - (gap * (Game.GRID_SIZE - 1)))
						/ Game.GRID_SIZE);
		int bottom = (int) Math.floor(sides * PERCENT_BOTTOM);
		int score = (int) Math.floor(bottom * PERCENT_SCORE);
		int header = Game.SCREEN_HEIGHT - gameArea - bottom;

		// Create Destination Rectangles
		createDestinationRects(sides, gameArea, gap, tileSize, bottom, score,
				header);

		// Set image source
		this.sprites = Game.sprites;
		this.background = Game.background;
		this.clouds = Game.clouds;

		Main.debug(TAG, "SpriteView Started");
	}

	private void createDestinationRects(int sidesW, int gameAreaWH, int gapWH,
			int tileSizeWH, int bottomH, int scoreH, int headerH) {
		// Header
		this.destHeader = new Rectangle(0, 0, Game.SCREEN_WIDTH, headerH);

		// Sides
		this.destSides[0] = new Rectangle(0, headerH, sidesW, gameAreaWH);
		this.destSides[1] = new Rectangle(Game.SCREEN_WIDTH - sidesW, headerH,
				sidesW, gameAreaWH);

		// Game Area
		this.destGameArea = new Rectangle(sidesW, headerH, gameAreaWH,
				gameAreaWH);

		// Tiles
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				this.destTiles[y][x] = new Rectangle(sidesW
						+ ((tileSizeWH + gapWH) * x), headerH
						+ ((tileSizeWH + gapWH) * y), tileSizeWH, tileSizeWH);
			}
		}

		// Bottom
		this.destBottom = new Rectangle(0, headerH + gameAreaWH,
				Game.SCREEN_WIDTH, bottomH);

		// Score
		double scoreScale = (double) scoreH / sourceNumber.h;
		int numberWidth = (int) ((double) sourceNumber.w * scoreScale);
		int numberHeight = (int) ((double) sourceNumber.h * scoreScale);

		this.destScore = new Rectangle(sidesW, Game.SCREEN_HEIGHT - bottomH
				+ gapWH, numberWidth, numberHeight);

		// New Row Counter
		this.destCounter = new Rectangle(sidesW, sidesW, sidesW, sidesW);

		// Set destBackground
		this.destBackground = new Rectangle(0, 0, Game.SCREEN_WIDTH,
				sourceBackground.h);

		// Set tile size for reference
		this.tileSize = tileSizeWH;

		// Set gap size for reference
		this.gap = gapWH;

	}

	public int getTileSize() {
		return tileSize;
	}

	public int getXOffset() {
		return destGameArea.x;
	}

	public int getYOffset() {
		return destGameArea.y;
	}

	public int getGap() {
		return this.gap;
	}

	public void update(AController controller, double gametime, double delta) {
		// TODO Auto-generated method stub

	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawRect(100, 100, 150, 250, paint);

		GameController controller = (GameController) Game.controller;
		int[][] grid = controller.grid;
		int[] entryGrid = controller.entryGrid;
		ArrayList<AAnimation> particles = controller.animations;

		// Font Stuff
		// Paint p = new Paint();
		// p.setAntiAlias(true);
		// p.setTypeface(Game.font);

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
		for (AAnimation p : particles)
			p.draw(canvas);

		// Draw Counter
		drawCounters(controller, canvas);
	}

	private void drawBackground(Canvas canvas) {
		// Clouds
		Paint paint = new Paint();
		paint.setColor(Game.cSkyBlue);
		canvas.drawRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, paint);

		double scale = (double) Game.SCREEN_WIDTH / sourceClouds.w;
		Rectangle destClouds = sourceClouds.clone();
		destClouds.w = Game.SCREEN_WIDTH;
		destClouds.h = (int) (scale * destClouds.h);

		canvas.drawBitmap(clouds, sourceClouds.getRect(), destClouds.getRect(),
				null);

		// Foreground
		scale = (double) Game.SCREEN_WIDTH / sourceBackground.w;

		Rectangle destBG = destBackground.clone();
		destBG.h *= scale;
		destBG.y = -(destBG.h - Game.SCREEN_HEIGHT);

		canvas.drawBitmap(background, sourceBackground.getRect(),
				destBG.getRect(), null);
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

				Rectangle sourceRect = sourceTile.clone();
				sourceRect.x = sourceRect.w * tileValue;

				canvas.drawBitmap(sprites, sourceRect.getRect(),
						destTiles[y][x].getRect(), null);
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

			Rectangle sourceRect = sourceNumber.clone();
			sourceRect.x = sourceRect.w * numChar;

			Rectangle destRect = destScore.clone();
			destRect.x += (i * destRect.w);

			canvas.drawBitmap(sprites, sourceRect.getRect(),
					destRect.getRect(), null);
		}
	}

	private void drawActiveTile(Canvas canvas, int[] entryGrid) {
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			if (entryGrid[i] > 0) {
				int tileValue = entryGrid[i];

				Rectangle destPanda = destTiles[0][i].clone();
				destPanda.x = destPanda.x - ((destPanda.w * 2) - destPanda.w)
						/ 2;
				destPanda.y = destPanda.y - tileSize - gap
						- ((destPanda.h * 2) - destPanda.h) / 2 + pandaOffset;
				destPanda.w *= 2;
				destPanda.h *= 2;

				Rectangle destActiveTile = destTiles[0][i].clone();
				destActiveTile.y = destActiveTile.y - tileSize - gap;

				Rectangle sourceActiveTile = sourceTile.clone();
				sourceActiveTile.x = sourceActiveTile.w * tileValue;

				// Draw Panda
				canvas.drawBitmap(sprites, sourcePanda.getRect(),
						destPanda.getRect(), null);

				// Draw Tile
				canvas.drawBitmap(sprites, sourceActiveTile.getRect(),
						destActiveTile.getRect(), null);

				// Draw Panda Hands
				canvas.drawBitmap(sprites, sourcePandaHands.getRect(),
						destPanda.getRect(), null);
			}
		}
	}

	private void drawCounters(GameController controller, Canvas canvas) {
		for (int i = 0; i < controller.difficulty; i++) {
			Rectangle sourceRect = sourceCounter.clone();
			if (i < controller.countTillNewRow)
				sourceRect.x = sourceRect.w;

			Rectangle destRect = destCounter.clone();
			destRect.x = destCounter.x + (destRect.w + counterGap) * i;

			canvas.drawBitmap(sprites, sourceRect.getRect(),
					destRect.getRect(), null);
		}
	}

}
