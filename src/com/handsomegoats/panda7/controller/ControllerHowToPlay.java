package com.handsomegoats.panda7.controller;

import com.handsomegoats.panda7.Game.Screen;

import android.graphics.Canvas;

public class ControllerHowToPlay extends AbstractController {

  public enum HowToPage {
    Release, Match0, Match1, Bricks, Counter, GameOver
  }

  public HowToPage currentScreen;

  public ControllerHowToPlay() {
    currentScreen = HowToPage.Release;
  }

  public void update() {
    // TODO Auto-generated method stub

  }

  public void draw(Canvas canvas) {
    // TODO Auto-generated method stub

  }

  public void nextScreen() {
    switch (currentScreen) {
    case Release:
      currentScreen = HowToPage.Match0;
      break;
    case Match0:
      currentScreen = HowToPage.Match1;
      break;
    case Match1:
      currentScreen = HowToPage.Bricks;
      break;
    case Bricks:
      currentScreen = HowToPage.Counter;
      break;
    case Counter:
      currentScreen = HowToPage.GameOver;
      break;
    case GameOver:
      nextScreen = Screen.Title;
      break;
    }
  }

}
