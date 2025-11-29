package com.example.xo_tournament.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xo_tournament.db";
    private static final int DATABASE_VERSION = 1;

    // Table des résultats
    private static final String TABLE_RESULTS = "results";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE_X = "score_x";
    private static final String COLUMN_SCORE_O = "score_o";
    private static final String COLUMN_DRAWS = "draws";
    private static final String COLUMN_TOTAL_GAMES = "total_games";
    private static final String COLUMN_WINNER = "winner";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SCORE_X + " INTEGER,"
                + COLUMN_SCORE_O + " INTEGER,"
                + COLUMN_DRAWS + " INTEGER,"
                + COLUMN_TOTAL_GAMES + " INTEGER,"
                + COLUMN_WINNER + " TEXT"
                + ")";
        db.execSQL(CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    // Méthode pour insérer un résultat
    public void insertTournament(int scoreX, int scoreO, int draws, int totalGames, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_X, scoreX);
        values.put(COLUMN_SCORE_O, scoreO);
        values.put(COLUMN_DRAWS, draws);
        values.put(COLUMN_TOTAL_GAMES, totalGames);
        values.put(COLUMN_WINNER, winner);
        db.insert(TABLE_RESULTS, null, values);
        db.close();
    }
}
