package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerHighScore;
import com.handsomegoats.panda7.view.AbstractView;
import com.handsomegoats.panda7.view.ViewHighScore;

public class InputHighScore implements InterfaceInput {

  public void touchDown(AbstractController controller, AbstractView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchMove(AbstractController controller, AbstractView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchPress(AbstractController controller, AbstractView view, float x, float y) {
    // TODO Auto-generated method stub
    ViewHighScore v = (ViewHighScore) view;

    if (Rectangle.intersects(v.dstPlayAgain, x, y))
      AbstractController.nextScreen = Screen.Game;
    
    if (Rectangle.intersects(v.dstReturnToTitle, x, y))
      AbstractController.nextScreen = Screen.Title;
  }

}
