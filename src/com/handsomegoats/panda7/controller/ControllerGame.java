package com.handsomegoats.panda7.controller;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Canvas;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Main;
import com.handsomegoats.panda7.view.*;

public class ControllerGame extends AbstractController implements Serializable {
  private static final long    serialVersionUID = 1L;
  private static final String  TAG              = ControllerGame.class.getSimpleName();
  public int[][]               grid;
  public int[][]               matches;
  public int[]                 entryGrid;
  int[]                        weighting        = { 10, 10, 19, 12, 15, 14, 17 };
  public int                   score;
  public int                   multiplier;
  int                          selectedRow;
  int                          selectedColumn;
  public boolean               disableDrop;
  public int                   countTillNewRow;
  long                         gameTime         = 0;
  double                       delta            = 0;
  long                         then             = 0;
  int                          FLAG             = 999;
  public int                   startHeight;
  public int                   difficulty;

  public ArrayList<AbstractAnimation> animations       = new ArrayList<AbstractAnimation>();

  public static boolean        menuOpen         = false;

  int                          toneCounter      = 0;
  int                          toneCounterMax   = 5;

  public ControllerGame() {
    Main.debug(TAG, "Controller Started");
    startHeight = Game.startHeight;
    difficulty = Game.difficulty;

    generateNewGame();
  }

  private void generateNewGame() {
    Main.debug(TAG, "Generating Level...");

    // Set up grid
    grid = newGrid(Game.GRID_SIZE);
    matches = newGrid(Game.GRID_SIZE);
    entryGrid = newGridRow(Game.GRID_SIZE);
    score = 0;
    multiplier = 1;
    selectedRow = 0;
    selectedColumn = 0;
    disableDrop = false;
    countTillNewRow = difficulty;

    // Generate new level
    for (int y = 0; y < Game.GRID_SIZE - startHeight - 1; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        int number = 0;

        if (Game.random.nextDouble() > Game.EMPTY_SPACE_PERCENT)
          number = (int) Math.round(Game.random.nextDouble() * Game.GRID_SIZE);

        this.grid[y][x] = number;
      }
    }

    // Generate this.entryGrid value
    int halfway = (int) (entryGrid.length / 2);
    entryGrid[halfway] = randomWeightedNumber();
    
    Main.debug(TAG, "Generating Level finished");
  }

  public void update() {
    // Game Time
    gameTime++;
    delta = (double) (then - System.currentTimeMillis()) / 1000.0;
    then = System.currentTimeMillis();

    // Animations
    if (animations.size() > 0) {
      updateParticles();
    } else {
      // Game Logic
      if (applyGravity()) {
        disableDrop = true;
      } else {
        if (validate()) {
          calculateScore();
          addComboMultiplier();
          destroyValidBlocks();
          playTone();
        } else {
          clearComboMultiplier();
          disableDrop = false;
        }

        // Add a row of bricks
        if (countTillNewRow <= 0) {
          newBrickRow();
          Main.playSound(Main.sndBump);
        }
      }
    }
  }

  private void updateParticles() {
    for (int i = 0; i < animations.size(); i++) {
      if (animations.get(i).destroy) {
        animations.remove(i--);
      } else {
        animations.get(i).update(gameTime, delta);
      }
    }
  }

  private void playTone() {
    switch (toneCounter) {
    case 0:
      Main.playSound(Main.sndTone1);
      break;
    case 1:
      Main.playSound(Main.sndTone2);
      break;
    case 2:
      Main.playSound(Main.sndTone3);
      break;
    case 3:
      Main.playSound(Main.sndTone4);
      break;
    case 4:
      Main.playSound(Main.sndTone5);
      break;
    case 5:
      Main.playSound(Main.sndTone6);
      break;
    }
    
    if (++toneCounter > toneCounterMax)
      toneCounter = 0;
  }

  public void draw(Canvas canvas) {

  }

  private void clearComboMultiplier() {
    this.multiplier = 1;
  }

  private void destroyValidBlocks() {
    // Break Stones
    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        if (isNumber(matches[y][x])) {
          // Check Left
          if (x - 1 >= 0)
            breakTile(x - 1, y);

          // Check Right
          if (x + 1 < Game.GRID_SIZE)
            breakTile(x + 1, y);

          // Check Top
          if (y - 1 >= 0)
            breakTile(x, y - 1);

          // Check Bottom
          if (y + 1 < Game.GRID_SIZE)
            breakTile(x, y + 1);
        }
      }
    }

    // Add Animations & Destroy Tiles
    ArrayList<AbstractAnimation> animationsToAdd = new ArrayList<AbstractAnimation>();

    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        if (isFlagged(matches[y][x])) {
          // Notify event: Block destruction, pass in this tile
          animationsToAdd.add(new ViewParticleAnimation(x, y, grid[y][x]));

          // Destroy Tiles
          grid[y][x] = Game.NO_TILE;
          matches[y][x] = Game.NO_TILE;
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
        if (isNumber(grid[y][x])) {
          validRow(x, y);
          validColumn(x, y);
        }
      }
    }

    // If there are any matches, return true
    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        if (isNumber(matches[y][x])) {
          return true;
        }
      }
    }

    return false;
  }

  private void validRow(int x, int y) {
    int total = 1;
    int value = grid[y][x];

    // Check Left
    if (x - 1 >= 0) {
      for (int i = x - 1; i >= 0; i--) {
        if (grid[y][i] == Game.NO_TILE) {
          break;
        } else {
          total++;
        }
      }
    }

    // Check Right
    for (int i = x + 1; i < Game.GRID_SIZE; i++) {
      if (grid[y][i] == Game.NO_TILE) {
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
    int value = grid[y][x];

    // Check Top
    if (y - 1 >= 0) {
      for (int i = y - 1; i >= 0; i--) {
        if (grid[i][x] == Game.NO_TILE) {
          break;
        } else {
          total++;
        }
      }
    }

    // Check Bottom
    for (int i = y + 1; i < Game.GRID_SIZE; i++) {
      if (grid[i][x] == Game.NO_TILE) {
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
          if (!isEmpty(this.grid[y][x]) && isEmpty(this.grid[y + 1][x])) {
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
   * newGridRow()
   * 
   * @param gridSize
   * @return int[]
   */
  private int[] newGridRow(int gridSize) {
    int[] newArray = new int[gridSize];

    for (int x = 0; x < gridSize; x++)
      newArray[x] = Game.NO_TILE;

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
        newArray[y][x] = Game.NO_TILE;

    return newArray;
  }

  private void newBrickRow() {
    countTillNewRow = this.difficulty;

    boolean gameOver = false;
    int[] newRow = new int[Game.GRID_SIZE];

    // Generate new row to add
    for (int i = 0; i < Game.GRID_SIZE; i++)
      newRow[i] = Game.BRICK_TILE;

    // Shift all tiles up
    for (int y = 0; y < Game.GRID_SIZE; y++) {
      for (int x = 0; x < Game.GRID_SIZE; x++) {
        if (!isEmpty(grid[y][x])) {
          if (y - 1 < 0) {
            // Game Over
            gameOver = true;
          } else {
            grid[y - 1][x] = grid[y][x];
            grid[y][x] = Game.NO_TILE;
          }
        }
      }
    }

    // Replace final row with new row
    grid[Game.GRID_SIZE - 1] = newRow;

    // If gameOver = true, call gameOver
    if (gameOver)
      gameOver();
  }

  private void gameOver() {
    generateNewGame();
  }

  public int randomWeightedNumber() {
    double r = Game.random.nextDouble();
    int total = 0;
    int[] cumulativeWeight = new int[Game.GRID_SIZE];

    cumulativeWeight[0] = weighting[0];

    for (int i = 1; i < Game.GRID_SIZE; i++)
      cumulativeWeight[i] = cumulativeWeight[i - 1] + weighting[i];

    total = cumulativeWeight[Game.GRID_SIZE - 1];

    if (r < (double) cumulativeWeight[0] / total)
      return 1;

    for (int i = 1; i < Game.GRID_SIZE; i++) {
      if (r >= (double) cumulativeWeight[i - 1] / total && r < (double) cumulativeWeight[i] / total) {
        return i + 1;
      }
    }

    return 1;
  }

  private boolean isFlagged(int value) {
    if (value == FLAG)
      return true;

    return false;
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
      this.grid[y][x] = randomWeightedNumber();
    }
  }



  private void queueAnimation(ArrayList<AbstractAnimation> animationsToAdd) {
    for (AbstractAnimation a : animationsToAdd) {
      this.animations.add(a);
    }

  }

  // x,y are pixelsToCoords
  public void moveActive(int x, int y) {
    // Vector2 p = pixelsToCoords((int) x, (int) y);
    boolean found = false;
    int activePosition = 0;
    int activeValue = FLAG;

    // Get position and value of active
    for (int i = 0; i < Game.GRID_SIZE; i++) {
      if (isNumber(entryGrid[i])) {
        activePosition = i;
        activeValue = entryGrid[i];
        found = true;
        break;
      }
    }

    // If a position/value was found, do it
    if (found) {
      entryGrid[activePosition] = Game.NO_TILE;
      entryGrid[x] = activeValue;
    }

    // Highlight Column
    for (int i = 0; i < Game.GRID_SIZE; i++) {
      if (isNumber(entryGrid[i])) {
        selectedColumn = i;
        break;
      }
    }
  }

  public static void restart(ControllerGame controller, int startHeight, int difficulty) {
    controller.startHeight = startHeight;
    controller.difficulty = difficulty;

    controller.generateNewGame();

    Main.debug(TAG, "Game Restarted");

  }

}
