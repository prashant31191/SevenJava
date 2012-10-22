package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.controller.GameController;
import com.handsomegoats.panda7.view.IView;

public class InputGame implements IInput {
  public static String TAG = InputGame.class.getSimpleName();

  public InputGame() {
    Main.debug(TAG, "InputGame Started.");  
  }

  public void touchDown(AController controller, IView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchMove(AController controller, IView view, float x, float y) {
    GameController c = (GameController) controller;
    c.moveActive(x, y);

  }

  public void touchPress(AController controller, IView view, float x, float y) {
    GameController c = (GameController) controller;
    c.moveActive(x, y);

    if (c.disableDrop)
      return;

    for (int i = 0; i < Game.GRID_SIZE; i++) {
      if (c.entryGrid[i] > 0) {
        // Drop into grid
        if (c.grid[0][i] == 0) {
          c.grid[0][i] = c.entryGrid[i];
          c.entryGrid[i] = c.randomWeightedNumber();
        }
      }
    }

    c.countTillNewRow--;

  }

}
