package com.handsomegoats.panda7.controller;

public class ControllerDifficulty extends AbstractController {

  public enum Difficulty {
    Easy(7), Medium(5), Hard(3);

    public final int val;

    Difficulty(int value) {
      this.val = value;
    }
    
    int getVal(){
      return val;
    }

  }

  public void update() {
    // TODO Auto-generated method stub

  }

}
