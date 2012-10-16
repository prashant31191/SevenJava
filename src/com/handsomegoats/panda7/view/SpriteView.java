package com.handsomegoats.panda7.view;

import java.util.ArrayList;

import android.R;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.util.Log;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.GridController;

public class SpriteView implements IView {
	private static final String TAG = SpriteView.class.getSimpleName();

	// Images
	Bitmap sprites;
	Bitmap background;

	// Sprite Dimensions
	int[] sTile = { 64, 64 };
	int[] sBtn = { 128, 128 };
	int[] sPanda = { 128, 128 };
	int[] sCounter = { 32, 32 };
	int[] sNumber = { 53, 64 };
	double scale = 1;
	Rect bgSourceRect = new Rect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
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
	final double yOffsetPercent = 0.4; // 40%
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
		this.tileSizePercent = (1 - ((this.xOffsetPercent * 2) + (this.spacePercent * (Game.GRID_SIZE - 1))))
				/ Game.GRID_SIZE;

		this.xOffset = this.xOffsetPercent * Game.SCREEN_WIDTH;
		this.yOffset = this.yOffsetPercent * Game.SCREEN_HEIGHT;
		this.space = this.spacePercent * Game.SCREEN_WIDTH;
		this.tileSize = this.tileSizePercent * Game.SCREEN_WIDTH;

		sprites = Game.sprites;
		background = Game.background;
		scale = tileSize / sTile[0];

		sprites = Bitmap.createScaledBitmap(Game.sprites,
				(int) (scale * Game.sprites.getWidth()),
				(int) (scale * Game.sprites.getHeight()), true);

		background = Bitmap.createScaledBitmap(Game.background,
				Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, true);

		ArrayList<Rect> sources = new ArrayList<Rect>();
		sources.add(tileSourceRect);
		sources.add(btnSourceRect);
		sources.add(pandaSourceRect);
		sources.add(counterSourceRect);
		sources.add(numberSourceRect);

		for (Rect rect : sources) {
			rect.left = (int) (rect.left * scale);
			rect.top = (int) (rect.top * scale);
			rect.right = (int) (rect.right * scale);
			rect.bottom = (int) (rect.bottom * scale);
		}
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

		// Draw Active Tile
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
				Rect eTileSource = new Rect(tileSourceRect.left
						+ (tileValue * t), tileSourceRect.top,
						tileSourceRect.right + (tileValue * t),
						tileSourceRect.bottom);

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

				// canvas.drawRect(destRect, paint);
			}
		}

		// Draw Board Tiles
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
			}
		}

		// Draw Score
		// canvas.drawRect(numberSourceRect, paint);

		// Draw Combo Points
		// Draw Particles
	}
}
