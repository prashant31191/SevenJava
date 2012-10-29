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

    c.release = true;

  }

}
