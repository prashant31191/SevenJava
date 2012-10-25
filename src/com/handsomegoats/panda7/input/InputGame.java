package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerGame;
import com.handsomegoats.panda7.view.AbstractView;
import com.handsomegoats.panda7.view.ViewGame;

public class InputGame implements InterfaceInput {
  public static String TAG = InputGame.class.getSimpleName();

  public InputGame() {
    Main.debug(TAG, "InputGame Started.");
  }

  public void touchDown(AbstractController controller, AbstractView view, float x, float y) {
  }

  public void touchMove(AbstractController controller, AbstractView view, float x, float y) {
    ControllerGame c = (ControllerGame) controller;
    ViewGame v = (ViewGame) view;
    Vector2 p = v.pixelsToCoords(x, y);

    c.moveActive((int) p.x, (int) p.y);

  }

  public void touchPress(AbstractController controller, AbstractView view, float x, float y) {
    ControllerGame c = (ControllerGame) controller;
    ViewGame v = (ViewGame) view;
    Vector2 p = v.pixelsToCoords(x, y);

    c.moveActive((int) p.x, (int) p.y);

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

    // TOD: This needs to move to controller
    c.countTillNewRow--;

  }

}
