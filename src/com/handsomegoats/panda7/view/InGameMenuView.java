package com.handsomegoats.panda7.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Canvas;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AController;
import com.handsomegoats.panda7.input.InputInGameMenu;
import com.handsomegoats.panda7.input.InputInGameMenu.Buttons;

public class InGameMenuView implements Drawable {

  HashMap<Buttons, Rectangle>               sRects      = new HashMap<InputInGameMenu.Buttons, Rectangle>();
  public static HashMap<Buttons, Rectangle> dRects      = new HashMap<InputInGameMenu.Buttons, Rectangle>();
  ArrayList<Rectangle>                      sourceRects = new ArrayList<Rectangle>();
  public static ArrayList<Rectangle>        destRects   = new ArrayList<Rectangle>();

  int                                       buttonSize;
  int                                       bottomRowSpacing;

  public InGameMenuView() {
    sRects.put(Buttons.Play, new Rectangle(640, 64, 64, 64));
    sRects.put(Buttons.Title, new Rectangle(704, 64, 64, 64));
    sRects.put(Buttons.HowToPlay, new Rectangle(768, 64, 64, 64));
    sRects.put(Buttons.Sound, new Rectangle(640, 128, 64, 64));
    sRects.put(Buttons.SoundMuted, new Rectangle(704, 128, 64, 64));
    sRects.put(Buttons.Music, new Rectangle(640, 192, 64, 64));
    sRects.put(Buttons.MusicMuted, new Rectangle(704, 192, 64, 64));
    sRects.put(Buttons.Settings, new Rectangle(640, 192, 64, 64));

    // 1/5 Width of screen
    int screenWidth = Game.SCREEN_WIDTH;
    int screenHeight = Game.SCREEN_HEIGHT;

    int btn = screenWidth / 5;
    int space = btn / 2;

    dRects.put(Buttons.Play, new Rectangle(btn, btn * 3, btn, btn));
    dRects.put(Buttons.Title, new Rectangle(btn * 3, btn * 3, btn, btn));
    dRects.put(Buttons.HowToPlay, new Rectangle(space, screenHeight - btn, btn, btn));
    dRects.put(Buttons.Sound, new Rectangle(screenWidth - space, screenHeight - btn, btn, btn));
    dRects.put(Buttons.SoundMuted, new Rectangle(screenWidth - space, screenHeight - btn, btn, btn));
    dRects.put(Buttons.Music, new Rectangle(btn * 2, screenHeight - btn, btn, btn));
    dRects.put(Buttons.MusicMuted, new Rectangle(btn * 2, screenHeight - btn, btn, btn));
    dRects.put(Buttons.Settings, new Rectangle(0, 0, 0, 0));

    sourceRects.addAll(sRects.values());
    destRects.addAll(sRects.values());
  }

  public void update(AController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    for (int i = 0; i < destRects.size(); i++) {
      canvas.drawBitmap(Game.sprites, sourceRects.get(i).getRect(), destRects.get(i).getRect(), null);
    }
  }

}
