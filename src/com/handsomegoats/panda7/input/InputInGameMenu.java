package com.handsomegoats.panda7.input;

import java.util.HashMap;

import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.GameController;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.view.IView;
import com.handsomegoats.panda7.view.InGameMenuView;

public class InputInGameMenu implements IInput {

  public enum Buttons {
    Play(0), Title(1), HowToPlay(2), Sound(3), SoundMuted(4), Settings(5), Music(6), MusicMuted(7);

    public final int val;

    Buttons(int value) {
      this.val = value;
    }
  }

  public void touchDown(AController controller, IView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchMove(AController controller, IView view, float x, float y) {
    // TODO Auto-generated method stub

  }

  public void touchPress(AController controller, IView view, float x, float y) {
    GameController c = (GameController) controller;
    
    HashMap<Buttons, Rectangle> dRects = InGameMenuView.dRects;
    
    // Resume Play
    if (intersects(x, y, dRects, Buttons.Play)){
      GameController.menuOpen = false;
    }
    
    // Restart Game
    if (intersects(x, y, dRects, Buttons.Title)){
      int startHeight = c.startHeight;
      int difficulty = c.difficulty;
      GameController.menuOpen = false;
      GameController.restart(c, startHeight, difficulty);
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
