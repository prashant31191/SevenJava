package com.handsomegoats.panda7;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;
import android.util.Log;
import com.handsomegoats.panda7.events.*;
import com.handsomegoats.panda7.view.*;

public class GridController implements Controller {
	private static final String TAG = GridController.class.getSimpleName();
	Game game;
	public int[][] grid;
	public int[][] matches;
	public int[] entryGrid;
	int[] weighting = { 10, 10, 19, 12, 15, 14, 17 };
	int score;
	int multiplier;
	int selectedRow;
	int selectedColumn;
	boolean disableDrop;
	int newRowCounter;
	IView view;
	long gameTime = 0;
	double delta = 0;
	long then = 0;
	int FLAG = 999;
	public ArrayList<AAnimation> animations;

	public EventListener events;
	public int X_OFFSET;
	public int Y_OFFSET;
	public int TILE_SIZE;
	public int SPACE;

	/**
	 * Constructor
	 * 
	 * @param gamePanel
	 */
	public GridController(Game gamePanel) {
		this.game = gamePanel;
		// Set up grid
		this.grid = newGrid(Game.GRID_SIZE);
		this.matches = newGrid(Game.GRID_SIZE);
		this.entryGrid = newGridRow(Game.GRID_SIZE);
		this.score = 0;
		this.multiplier = 1;
		this.selectedRow = 0;
		this.selectedColumn = 0;
		this.disableDrop = false;
		this.newRowCounter = Game.DROPS_TILL_NEW_ROW;

		// Generate new level
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				int number = 0;

				if (Game.random.nextDouble() > Game.EMPTY_SPACE_PERCENT)
					number = (int) Math.round(Game.random.nextDouble()
							* Game.GRID_SIZE);

				this.grid[y][x] = number;
			}
		}

		// Generate this.entryGrid value
		int halfway = (int) (this.entryGrid.length / 2);
		this.entryGrid[halfway] = randomNewNumber();

		// Add View
		view = new SpriteView(this);
		animations = new ArrayList<AAnimation>();
		
		TILE_SIZE = view.getTileSize();
		X_OFFSET = view.getXOffset();
		Y_OFFSET = view.getYOffset();
		SPACE = view.getSpace();
	}

	public void update() {
		// Game Time
		this.gameTime++;
		this.delta = (double) (this.then - System.currentTimeMillis()) / 1000.0;
		this.then = System.currentTimeMillis();

		// Animations
		if (animations.size() > 0) {
			for (int i = 0; i < animations.size(); i++) {
				if (animations.get(i).destroy) {
					animations.get(i).update(this.gameTime, this.delta);
				} else {
					animations.remove(i--);
				}
			}
		} else {
			// Game Logic
			if (this.applyGravity()) {
				this.disableDrop = true;
			} else {
				if (this.validate()) {
					this.calculateScore();
					this.addComboMultiplier();
					this.destroyValidBlocks();
				} else {
					this.clearComboMultiplier();
					this.disableDrop = false;
				}

				if (this.newRowCounter <= 0) {
					this.events.notify("newRow");
				}
			}
		}

		// View Updates
		view.update(this.gameTime, this.delta);
	}

	public void draw(Canvas canvas) {
		view.draw(canvas);
	}

	private void clearComboMultiplier() {
		this.multiplier = 1;
	}

	private void destroyValidBlocks() {
		// Break Stones
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (isNumber(this.matches[y][x])) {
					// Check Left
					if (x - 1 >= 0)
						this.breakTile(x - 1, y);

					// Check Right
					if (x + 1 < Game.GRID_SIZE)
						this.breakTile(x + 1, y);

					// Check Top
					if (y - 1 >= 0)
						this.breakTile(x, y - 1);

					// Check Bottom
					if (y + 1 < Game.GRID_SIZE)
						this.breakTile(x, y + 1);
				}
			}
		}

		// Add Animations
		ArrayList<AAnimation> animationsToAdd = new ArrayList<AAnimation>();

		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (isNumber(this.matches[y][x])) {
					// Notify event: Block destruction, pass in this tile
					int[] p = this.coordsToPixels(x, y);
					animationsToAdd.add(new ParticleAnimation(p[0], p[1],
							this.matches[y][x]));

					// Destroy Tiles
					this.grid[y][x] = 0;
					this.matches[y][x] = 0;
				}
			}
		}

		// Notify Animation Queue
		queueAnimation(animationsToAdd);
	}

	private void addComboMultiplier() {
		this.multiplier++;
	}

	private void calculateScore() {
		int units = 0;
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (isNumber(this.matches[y][x])) {
					units++;
				}
			}
		}

		this.score += units * Game.CHAIN[this.multiplier];
	}

	private boolean validate() {
		// Validate each block, if one is found, flag it in this.matches
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (isNumber(this.grid[y][x])) {
					validRow(x, y);
					validColumn(x, y);
				}
			}
		}

		// If there are any matches, return true
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (isNumber(this.matches[y][x])) {
					return true;
				}
			}
		}
		return false;
	}

	private void validRow(int x, int y) {
		int total = 1;
		int value = this.grid[y][x];

		// Check Left
		for (int i = x - 1; i >= 0; i--) {
			if (this.grid[y][x] == 0) {
				break;
			} else {
				total++;
			}
		}

		// Check Right
		for (int i = x + 1; i < Game.GRID_SIZE; i++) {
			if (this.grid[y][i] == 0) {
				break;
			} else {
				total++;
			}
		}

		// Flag that tile it found the correct amount of tiles
		if (total == value)
			this.matches[y][x] = this.FLAG;

	}

	private void validColumn(int x, int y) {
		int total = 1;
		int value = this.grid[y][x];

		// Check Top
		for (int i = y - 1; i >= 0; i--) {
			if (this.grid[i][x] == 0) {
				break;
			} else {
				total++;
			}
		}

		// Check Bottom
		for (int i = y + 1; i < Game.GRID_SIZE; i++) {
			if (this.grid[i][x] == 0) {
				break;
			} else {
				total++;
			}
		}

		// Flag that tile it found the correct amount of tiles
		if (total == value)
			this.matches[y][x] = this.FLAG;

	}

	private boolean applyGravity() {
		for (int y = 0; y < Game.GRID_SIZE; y++) {
			for (int x = 0; x < Game.GRID_SIZE; x++) {
				if (y + 1 < Game.GRID_SIZE) {
					if (!isEmpty(this.grid[y][x])
							&& isEmpty(this.grid[y + 1][x])) {
						this.grid[y + 1][x] = this.grid[y][x];
						this.grid[y][x] = 0;

						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Any Events that get called need to go in here
	 */
	private void registerEvents() {
		// Drop Event
		this.events.register("drop", new Event("drop") {
			@Override
			public void callEvent(GridController g) {
				Log.i(TAG, "Drop Event WORKING!!!");
			}
		});

		// Move Event
		this.events.register("move", new Event("move") {
			@Override
			public void callEvent(GridController g) {
				Log.i(TAG, "Move Event WORKING!!!");
			}
		});

		// New Row Event
		this.events.register("newRow", new Event("newRow") {
			@Override
			public void callEvent(GridController g) {
				Log.i(TAG, "newRow Event WORKING!!!");
			}
		});

		// queueAnimation Event
		this.events.register("queueAnimation", new Event("queueAnimation") {
			@Override
			public void callEvent(GridController g) {
				Log.i(TAG, "queueAnimation Event WORKING!!!");
			}
		});

		// this.events.notify("drop");
		// this.events.notify("move");
		// this.events.notify("newRow");
		// this.events.notify("queueAnimation");
	}

	/**
	 * newGridRow()
	 * 
	 * @param gridSize
	 * @return int[]
	 */
	private int[] newGridRow(int gridSize) {
		int[] newArray = new int[gridSize];

		for (int x = 0; x < gridSize; x++)
			newArray[x] = 0;

		return newArray;
	}

	/**
	 * newGrid()
	 * 
	 * @param gridSize
	 * @return
	 */
	private int[][] newGrid(int gridSize) {
		int[][] newArray = new int[gridSize][gridSize];

		for (int y = 0; y < gridSize; y++)
			for (int x = 0; x < gridSize; x++)
				newArray[y][x] = 0;

		return newArray;
	}

	private int randomNewNumber() {
		double r = Game.random.nextDouble();
		int total = 0;
		int[] cumulativeWeight = new int[weighting.length];

		cumulativeWeight[0] = weighting[0];

		for (int i = 1; i < weighting.length; i++)
			cumulativeWeight[i] = cumulativeWeight[i - 1] + weighting[i];

		total = cumulativeWeight[cumulativeWeight.length - 1];

		if (r >= 0 && r < cumulativeWeight[0] / total)
			return 1;

		for (int i = 1; i < weighting.length; i++) {
			if (r >= cumulativeWeight[i - 1] / total
					&& r < cumulativeWeight[i] / total) {
				return i + 1;
			}
		}

		return 1;
	}

	private boolean isNumber(int value) {
		if (value > Game.NO_TILE)
			return true;

		return false;
	}

	private boolean isEmpty(int value) {
		if (value == Game.NO_TILE)
			return true;

		return false;
	}

	private boolean isBroken(int value) {
		if (value == Game.BROKEN_BRICK_TILE)
			return true;

		return false;
	}

	private boolean isBrick(int value) {
		if (value == Game.BRICK_TILE)
			return true;

		return false;
	}

	private void breakTile(int x, int y) {
		if (isBrick(this.grid[y][x])) {
			this.grid[y][x]++;
		} else if (isBroken(this.grid[y][x])) {
			this.grid[y][x] = randomNewNumber();
		}
	}

	public int[] coordsToPixels(int x, int y) {
		int px = X_OFFSET + (x * (TILE_SIZE + SPACE));
		int py = Y_OFFSET + (y * (TILE_SIZE + SPACE));

		int[] coords = { px, py };

		return coords;
	}

	public int[] pixelsToCoords(int x, int y) {
		int cx = (int) Math.round((x - X_OFFSET) / (TILE_SIZE + SPACE));
		int cy = (int) Math.round((y - Y_OFFSET) / (TILE_SIZE + SPACE));

		if (cx > Game.GRID_SIZE - 1)
			cx = Game.GRID_SIZE - 1;

		if (cy > Game.GRID_SIZE - 1)
			cy = Game.GRID_SIZE - 1;

		if (cx < 0)
			cx = 0;

		if (cy < 0)
			cy = 0;

		int[] coords = { cx, cy };

		return coords;
	}

	private void queueAnimation(ArrayList<AAnimation> animationsToAdd) {
		for (AAnimation a : animationsToAdd) {
			this.animations.add(a);
		}

	}

	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub

	}

	public void touchMove(float x, float y) {
		int[] p = pixelsToCoords((int) x, (int) y);
		boolean found = false;
		int activePosition = 0;
		int activeValue = FLAG;

		// Get position and value of active
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			if (isNumber(this.entryGrid[i])) {
				activePosition = i;
				activeValue = this.entryGrid[i];
				found = true;
				break;
			}
		}

		// If a position/value was found, do it
		if (found) {
			this.entryGrid[activePosition] = Game.NO_TILE;
			this.entryGrid[p[0]] = activeValue;
		}

		// Highlight Column
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			if (isNumber(this.entryGrid[i])) {
				this.selectedColumn = i;
				break;
			}
		}

	}

	public void touchPress(float x, float y) {
		if (this.disableDrop)
			return;

		for (int i = 0; i < Game.GRID_SIZE; i++) {
			if (this.entryGrid[i] > 0) {
				// Drop into grid
				this.grid[0][i] = this.entryGrid[i];
				this.entryGrid[i] = randomNewNumber();
			}
		}

		this.newRowCounter--;
	}

}
