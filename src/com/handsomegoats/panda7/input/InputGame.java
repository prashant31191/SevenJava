package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerGame;
import com.handsomegoats.panda7.view.InterfaceView;

public class InputGame implements InterfaceInput {
  public static String TAG = InputGame.class.getSimpleName();

  public InputGame() {
    Main.debug(TAG, "InputGame Started.");  
  }

  public void touchDown(AbstractController controller, InterfaceView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchMove(AbstractController controller, InterfaceView view, float x, float y) {
    ControllerGame c = (ControllerGame) controller;
    c.moveActive(x, y);

  }

  public void touchPress(AbstractController controller, InterfaceView view, float x, float y) {
    ControllerGame c = (ControllerGame) controller;
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
