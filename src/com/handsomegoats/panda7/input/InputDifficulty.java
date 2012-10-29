package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerDifficulty.Difficulty;
import com.handsomegoats.panda7.view.AbstractView;
import com.handsomegoats.panda7.view.ViewDifficulty;

public class InputDifficulty implements InterfaceInput {

  public void touchDown(AbstractController controller, AbstractView view, float x, float y) {
    view.setTouchCoords(x, y);
  }

  public void touchMove(AbstractController controller, AbstractView view, float x, float y) {
    view.setTouchCoords(x, y);
  }

  public void touchPress(AbstractController controller, AbstractView view, float x, float y) {
    ViewDifficulty v = (ViewDifficulty) view;

    Rectangle dstEasy = v.dstEasy;
    Rectangle dstMedium = v.dstMedium;
    Rectangle dstHard = v.dstHard;

    boolean touchEasy = Rectangle.intersects(dstEasy, x, y);
    boolean touchMedium = Rectangle.intersects(dstMedium, x, y);
    boolean touchHard = Rectangle.intersects(dstHard, x, y);

    if (touchEasy) {
      Game.difficulty = Difficulty.Easy;
      AbstractController.nextScreen = Screen.Game;
    } else if (touchMedium) {
      Game.difficulty = Difficulty.Medium;
      AbstractController.nextScreen = Screen.Game;
    } else if (touchHard) {
      Game.difficulty = Difficulty.Hard;
      AbstractController.nextScreen = Screen.Game;
    }
  }
}
