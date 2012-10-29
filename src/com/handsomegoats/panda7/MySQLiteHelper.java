package com.handsomegoats.panda7;

import com.handsomegoats.panda7.controller.ControllerDifficulty.Difficulty;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
  public static final String TAG = "MySQLiteHelper";

  public MySQLiteHelper(Context context) {
    super(context, "HighScoresDB", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Create Tables if they don't exist
    try {
      db.execSQL("CREATE TABLE IF NOT EXISTS " + Difficulty.Easy.name() + " (name VARCHAR(20), score INT)");
      db.execSQL("CREATE TABLE IF NOT EXISTS " + Difficulty.Medium.name() + " (name VARCHAR(20), score INT)");
      db.execSQL("CREATE TABLE IF NOT EXISTS " + Difficulty.Hard.name() + " (name VARCHAR(20), score INT)");
    } catch (SQLException e) {
      Main.debug(TAG, "Problem creating tables: " + e.getMessage());
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Main.debug(TAG, "Upgrading DB from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + Difficulty.Easy.name());
    db.execSQL("DROP TABLE IF EXISTS " + Difficulty.Medium.name());
    db.execSQL("DROP TABLE IF EXISTS " + Difficulty.Hard.name());
    onCreate(db);
  }

}
