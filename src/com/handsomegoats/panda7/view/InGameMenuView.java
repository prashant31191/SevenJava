package com.handsomegoats.panda7.view;

import android.graphics.Canvas;

import com.handsomegoats.panda7.Game;
import com.handsomegoats.panda7.Rectangle;
import com.handsomegoats.panda7.controller.AController;

public class InGameMenuView implements Drawable {

  private enum MenuItems {
    Play(0), Restart(1), HowToPlay(2), Sound(3), SoundMuted(4), Settings(5), Music(6), MusicMuted(7);

    private final int val;

    MenuItems(int value) {
      this.val = value;
    }
  }

  Rectangle[] sourceRects;
  Rectangle[] destRects;

  int         buttonSize;
  int         bottomRowSpacing;

  public InGameMenuView() {
    sourceRects = new Rectangle[8];
    sourceRects[MenuItems.Play.val] = new Rectangle(640, 64, 64, 64);
    sourceRects[MenuItems.Restart.val] = new Rectangle(704, 64, 64, 64);
    sourceRects[MenuItems.HowToPlay.val] = new Rectangle(768, 64, 64, 64);
    sourceRects[MenuItems.Sound.val] = new Rectangle(640, 128, 64, 64);
    sourceRects[MenuItems.SoundMuted.val] = new Rectangle(704, 128, 64, 64);
    // sourceRects[MenuItems.Settings.val] = new Rectangle(768, 128, 64, 64);
    sourceRects[MenuItems.Music.val] = new Rectangle(640, 192, 64, 64);
    sourceRects[MenuItems.MusicMuted.val] = new Rectangle(704, 192, 64, 64);

    // 1/5 Width of screen
    int screenWidth = Game.SCREEN_WIDTH;
    int screenHeight = Game.SCREEN_HEIGHT;

    int btn = screenWidth / 5;
    int space = btn / 2;

    destRects[MenuItems.Play.val] = new Rectangle(btn, btn * 3, btn, btn);
    destRects[MenuItems.Restart.val] = new Rectangle(btn * 3, btn * 3, btn, btn);
    destRects[MenuItems.HowToPlay.val] = new Rectangle(space, screenHeight - btn, btn, btn);
    destRects[MenuItems.Sound.val] = new Rectangle(screenWidth - space, screenHeight - btn, btn, btn);
    destRects[MenuItems.SoundMuted.val] = new Rectangle(screenWidth - space, screenHeight - btn, btn, btn);
    // destRects[MenuItems.Settings.val] = new Rectangle(0, 0, 0, 0);
    destRects[MenuItems.Music.val] = new Rectangle(btn * 2, screenHeight - btn, btn, btn);
    destRects[MenuItems.MusicMuted.val] = new Rectangle(btn * 2, screenHeight - btn, btn, btn);

  }

  public void update(AController controller, double gametime, double delta) {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    for (int i = 0; i < destRects.length; i++) {
      canvas.drawBitmap(Game.sprites, sourceRects[i].getRect(), destRects[i].getRect(), null);
    }
  }

}
