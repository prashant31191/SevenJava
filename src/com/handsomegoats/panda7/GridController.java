package com.handsomegoats.panda7;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;
import com.handsomegoats.panda7.events.*;
import com.handsomegoats.panda7.view.*;

public class GridController {
	private static final String TAG = GridController.class.getSimpleName();
	Game game;
	public int[][] grid;
	int[][] matches;
	int[] entryGrid;
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
	ArrayList<AAnimation> animations;

	public EventListener events;

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
		view = new SpriteView();
		animations = new ArrayList<AAnimation>();

		// Add Inputs

		// Create Event Handler
		this.events = new EventListener(this);

		// Register Events
		registerEvents();
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
	
	public void draw() {
		view.draw();
	}

	private void clearComboMultiplier() {
		// TODO Auto-generated method stub

	}

	private void destroyValidBlocks() {
		// TODO Auto-generated method stub

	}

	private void addComboMultiplier() {
		// TODO Auto-generated method stub

	}

	private void calculateScore() {
		// TODO Auto-generated method stub

	}

	private boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean applyGravity() {
		// TODO Auto-generated method stub
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

}
