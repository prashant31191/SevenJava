package com.handsomegoats.panda7;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
  private static final String TAG = Main.class.getSimpleName();
  public static int SCREEN_WIDTH;
  public static int SCREEN_HEIGHT;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Requesting to turn the title off
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    // Make it full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Rotate
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // Set MainGamePanel as the view
    setContentView(new Game(this));

    // Get Window Size
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);

    int SCREEN_WIDTH = metrics.widthPixels;
    int SCREEN_HEIGHT = metrics.heightPixels;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
}
