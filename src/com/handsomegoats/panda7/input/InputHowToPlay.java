package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.controller.ControllerHowToPlay;
import com.handsomegoats.panda7.view.InterfaceView;

public class InputHowToPlay implements InterfaceInput {

  public void touchDown(AbstractController controller, InterfaceView view, float x, float y) {
    // TODO Auto-generated method stub
    
  }

  public void touchMove(AbstractController controller, InterfaceView view, float x, float y) {
    // TODO Auto-generated method stub
    
  }

  public void touchPress(AbstractController controller, InterfaceView view, float x, float y) {
    ControllerHowToPlay c = (ControllerHowToPlay) controller;
    
    c.nextScreen();
  }

}
