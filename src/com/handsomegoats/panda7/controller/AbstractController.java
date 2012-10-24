package com.handsomegoats.panda7.controller;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Game.Screen;

public abstract class AbstractController implements InterfaceController {
  public static boolean     destroy    = false;
  public static Game.Screen nextScreen = Screen.Nothing;
}
