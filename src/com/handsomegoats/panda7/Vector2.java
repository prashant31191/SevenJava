package com.handsomegoats.panda7;

import java.io.Serializable;

public class Vector2 implements Serializable {
  private static final long serialVersionUID = 1L;
  public float x;
  public float y;

  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector2(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
