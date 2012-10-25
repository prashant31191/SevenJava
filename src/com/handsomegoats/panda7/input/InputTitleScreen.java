package com.handsomegoats.panda7.input;

import com.handsomegoats.panda7.*;
import com.handsomegoats.panda7.Game.Screen;
import com.handsomegoats.panda7.controller.AbstractController;
import com.handsomegoats.panda7.view.*;

public class InputTitleScreen implements InterfaceInput {
  public static String TAG = InputTitleScreen.class.getSimpleName();

  public InputTitleScreen() {
    Main.debug(TAG, "InputTitle Started.");
  }

  public void touchDown(AbstractController controller, AbstractView view, float x, float y) {
    view.setTouchCoords(x, y);
  }

  public void touchMove(AbstractController controller, AbstractView view, float x, float y) {
    view.setTouchCoords(x, y);
  }

  public void touchPress(AbstractController controller, AbstractView view, float x, float y) {
    ViewTitle v = (ViewTitle) view;

    Rectangle adventure = v.dstAdventure;
    Rectangle quickPlay = v.dstQuickPlay;
    Rectangle howToPlay = v.dstHowToPlay;

    boolean touchAdventure = Rectangle.intersects(adventure, x, y);
    boolean touchQuickPlay = Rectangle.intersects(quickPlay, x, y);
    boolean touchHowToPlay = Rectangle.intersects(howToPlay, x, y);

    if (touchAdventure) {
      // TODO: Go to adventure screen

    } else if (touchQuickPlay) {
      AbstractController.nextScreen = Screen.DifficultySelect;

    } else if (touchHowToPlay) {
      AbstractController.nextScreen = Screen.HowToPlay;

    }
  }
}
