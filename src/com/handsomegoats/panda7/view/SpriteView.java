package com.handsomegoats.panda7.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import android.R;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.handsomegoats.panda7.Controller;
import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.GridController;

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

	// Sprite Dimensions
	int blankOffset = 10;
	int brickOffset = 11;
	int brokenOffset = 12;
	int[] sTile = { 64, 64 };
	int[] sBtn = { 128, 128 };
	int[] sPanda = { 128, 128 };
	int[] sCounter = { 32, 32 };
	int[] sNumber = { 53, 64 };
	double scale = 1;
	Rect bgSourceRect = new Rect(0, 0, 480, 800);
	Rect tileSourceRect = new Rect(0, 0, sTile[0], sTile[1]);
	Rect btnSourceRect = new Rect(0, sTile[1], sBtn[0], sBtn[1] + sTile[1]);
	Rect pandaSourceRect = new Rect(0, sBtn[1] + sTile[1], sPanda[0], sBtn[1]
			+ sTile[1] + sPanda[1]);
	Rect counterSourceRect = new Rect(0, sTile[1] + sPanda[1] + sBtn[1],
			sCounter[0], sTile[1] + sPanda[1] + sBtn[1] + sCounter[1]);
	Rect numberSourceRect = new Rect(0, sTile[1] + sPanda[1] + sBtn[1]
			+ sCounter[1], sNumber[0], sTile[1] + sPanda[1] + sBtn[1]
			+ sCounter[1] + sNumber[1]);

	// Layout Settings
	final double xOffsetPercent = 0.05; // 5%
	final double yOffsetPercent = 0.42; // 42%
	final double spacePercent = 0.01; // 1%
	double tileSizePercent;

	// Calculated dimensions
	double xOffset;
	double yOffset;
	double space;
	double tileSize;

	GridController controller;

	public SpriteView(GridController controller) {
		this.controller = controller;

		// Tile size percent is what ever is leftover from the sum of the above
		double sides = this.xOffsetPercent * 2;
		double spacing = this.spacePercent * (Game.GRID_SIZE - 1);
		this.tileSizePercent = (1 - sides - spacing) / Game.GRID_SIZE;

		this.xOffset = this.xOffsetPercent * Game.SCREEN_WIDTH;
		this.yOffset = this.yOffsetPercent * Game.SCREEN_HEIGHT;
		this.space = this.spacePercent * Game.SCREEN_WIDTH;
		this.tileSize = this.tileSizePercent * Game.SCREEN_WIDTH;

		this.sprites = Game.sprites;
		this.background = Game.background;
		this.scale = tileSize / sTile[0];
	}

	public int getTileSize() {
		return (int) Math.round(tileSize);
	}

	public int getXOffset() {
		return (int) Math.round(xOffset);
	}

	public int getYOffset() {
		return (int) Math.round(yOffset);
	}

	public int getSpace() {
		int spacing = (int) Math.round(this.space);
		int minimumSpace = 1;

		if (spacing < minimumSpace)
			spacing = 1;

		return spacing;
	}

	public void update(double gametime, double delta) {
		// TODO Auto-generated method stub

	}

	public void draw(Canvas canvas) {
		int[][] grid = controller.grid;
		int[] entryGrid = controller.entryGrid;
		ArrayList<AAnimation> particles = controller.animations;

		// Draw Background
		Rect destRect = new Rect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		canvas.drawBitmap(background, bgSourceRect, destRect, null);

		// Draw Active Tile
		drawActiveTile(canvas, entryGrid);

		// Draw Board Tiles
		drawBoardTiles(canvas, grid);

		// Draw Score
		// double scale = 0.8;
		drawScore(canvas, this.scale, controller.score);

		// Draw Combo Points

		// Draw Particles
		for (AAnimation p : particles) {
			p.draw(canvas);
		}
	}

	private void drawScore(Canvas canvas, double scale, int number) {
		ArrayList<Integer> split = new ArrayList<Integer>();

		while (number > 0) {
			split.add(number % 10);
			number = number / 10;
		}

		Collections.reverse(split);

		int width = numberSourceRect.width();
		int height = numberSourceRect.height();
		int destX = 25;
		int destY = 25;
		int destWidth = (int) (numberSourceRect.width() * scale);
		int destHeight = (int) (numberSourceRect.height() * scale);

		for (int i = 0; i < split.size(); i++) {
			int numChar = split.get(i);

			Rect sourceRect = new Rect(numberSourceRect);

			sourceRect.left = width * numChar;
			sourceRect.right = sourceRect.left + width;

			Rect destRect = new Rect(destX + (i * destWidth), destY, destX
					+ (i * destWidth) + destWidth, destY + destHeight);

			canvas.drawBitmap(sprites, sourceRect, destRect, null);
		}
	}

	private void drawBoardTiles(Canvas canvas, int[][] grid) {
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				int[] p = controller.coordsToPixels(x, y);
				int tileValue = grid[y][x];

				if (tileValue == Game.NO_TILE) {
					tileValue = blankOffset;
				} else if (tileValue == Game.BRICK_TILE) {
					tileValue = brickOffset;
				} else if (tileValue == Game.BROKEN_BRICK_TILE) {
					tileValue = brokenOffset;
				}

				int swidth = (int) tileSourceRect.width();
				int sheight = (int) tileSourceRect.height();
				int destX = (int) p[0];
				int destY = (int) p[1];
				int destWidth = (int) (tileSize);
				int destHeight = (int) (tileSize);

				Rect sourceRect = new Rect(tileSourceRect);
				sourceRect.left = swidth * tileValue;
				sourceRect.right = sourceRect.left + swidth;

				Rect destRect = new Rect(destX, destY, destX + destWidth, destY
						+ destHeight);

				canvas.drawBitmap(sprites, sourceRect, destRect, null);
			}
		}
	}

	private void drawActiveTile(Canvas canvas, int[] entryGrid) {
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			if (entryGrid[i] > 0) {
				int[] p = controller.coordsToPixels(i, 0);
				int tileValue = entryGrid[i];
				int x, y, w, h;
				int t = (int) tileSize;
				int s = (int) space;

				x = (int) p[0];
				y = (int) (p[1] - t - s);
				w = (int) (p[0] + t);
				h = (int) (p[1] - s);

				// activeTile Destination
				Rect eTileSource = new Rect(tileSourceRect);
				int width = eTileSource.width();
				eTileSource.left = eTileSource.left + (tileValue * width);
				eTileSource.right = eTileSource.left + width;

				Rect eTileDest = new Rect(x, y, w, h);

				// Panda Destination Rect
				int diff = (pandaSourceRect.width() - tileSourceRect.width()) / 2;

				Rect pandaDest = new Rect(eTileDest.left - diff, eTileDest.top
						- diff - 30, eTileDest.left + pandaSourceRect.width()
						- diff, eTileDest.top + pandaSourceRect.height() - diff
						- 30);
				Rect pandaHands = new Rect(pandaSourceRect);
				pandaHands.left = pandaHands.height();
				pandaHands.right = pandaHands.height() * 2;

				// Draw Panda
				canvas.drawBitmap(sprites, pandaSourceRect, pandaDest, null);

				// Draw Tile
				canvas.drawBitmap(sprites, eTileSource, eTileDest, null);

				// Draw Panda Hands
				canvas.drawBitmap(sprites, pandaHands, pandaDest, null);
			}
		}
	}
}
