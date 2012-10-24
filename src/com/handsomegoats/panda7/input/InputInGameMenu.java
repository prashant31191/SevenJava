package com.handsomegoats.panda7.input;

import java.util.HashMap;

import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.ControllerGame;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.view.InterfaceView;
import com.handsomegoats.panda7.view.ViewInGameMenu;

public class InputInGameMenu implements InterfaceInput {

  public enum Buttons {
    Play(0), Title(1), HowToPlay(2), Sound(3), SoundMuted(4), Settings(5), Music(6), MusicMuted(7);

    public final int val;

    Buttons(int value) {
      this.val = value;
    }
  }

  public void touchDown(AbstractController controller, InterfaceView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchMove(AbstractController controller, InterfaceView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchPress(AbstractController controller, InterfaceView view, float x, float y) {
    ControllerGame c = (ControllerGame) controller;
    
    HashMap<Buttons, Rectangle> dRects = ViewInGameMenu.dRects;
    
    // Resume Play
    if (intersects(x, y, dRects, Buttons.Play)){
      ControllerGame.menuOpen = false;
    }
    
    // Restart Game
    if (intersects(x, y, dRects, Buttons.Title)){
      int startHeight = c.startHeight;
      int difficulty = c.difficulty;
      ControllerGame.menuOpen = false;
      ControllerGame.restart(c, startHeight, difficulty);
    }
    
    if (intersects(x, y, dRects, Buttons.HowToPlay)){
      // Pop up how to play window
    }
    
    // Toggle Sound
    if (intersects(x, y, dRects, Buttons.Sound)){}
    
    // Toggle Music
    if (intersects(x, y, dRects, Buttons.Music)){}
    
  }

  private boolean intersects(float x, float y, HashMap<Buttons, Rectangle> dRects, Buttons play) {
    return Rectangle.intersects(dRects.get(play), x, y);
  }
}
