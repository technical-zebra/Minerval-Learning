package sg.jcu.kezhang.minervalearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class QuizDatabaseHelper extends SQLiteOpenHelper {

    // Initialise database version.
    private static final int DATABASE_VERSION = 1;

    /**
     * Creates a quiz SQLite database for the first time.
     *
     * @param context A context that database created for.
     */
    public QuizDatabaseHelper(Context context) {
        super(context, DbConfig.DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the SQL tables in the database.
     *
     * @param sqLiteDatabase The database create table in.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /* Create table for quiz terms */
        // SQL Query for create a Table.
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + DbConfig.TABLE_QUIZ + "("
                + DbConfig.COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbConfig.COLUMN_QUIZ_WORD + " TEXT NOT NULL, "
                + DbConfig.COLUMN_QUIZ_DEFINITION + " TEXT NOT NULL "
                + ")";

        // SQL execution to create the table.
        sqLiteDatabase.execSQL(CREATE_QUIZ_TABLE);

        /* Create table for player ranking */
        // SQL Query for create a Table.
        String CREATE_GAME_RECORD_TABLE = "CREATE TABLE " + DbConfig.TABLE_GAME_RECORD + "("
                + DbConfig.COLUMN_GAME_RECORD_USERNAME + " TEXT PRIMARY KEY UNIQUE, "
                + DbConfig.COLUMN_GAME_RECORD_SCORE + " INTEGER "
                + ")";

        // SQL execution to create the table.
        sqLiteDatabase.execSQL(CREATE_GAME_RECORD_TABLE);

    }

    /**
     * Database is not upgradable.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     *  Add a new term to the database.
     * @param word The word and definition of the term.
     * @return A integer count all inserted records.
     */
    public int insertWord(String[] word) {

        // This variable will count how many records been inserted.
        long insert = 0;

        // Get the writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Initialise a ContentValues that store a set of values to be inserted.
        ContentValues cv = new ContentValues();

        // A set of values to be inserted.
        cv.put(DbConfig.COLUMN_QUIZ_WORD,
                word[0]);

        cv.put(DbConfig.COLUMN_QUIZ_DEFINITION,
                word[1]);

        db.delete(DbConfig.TABLE_QUIZ, DbConfig.COLUMN_QUIZ_WORD + " = ?",
                new String[]{word[0]});

        // Execute the SQL insertion and return how many records are inserted.
        insert = insert + db.insert(DbConfig.TABLE_QUIZ, null, cv);

        // Return number of record inserted.
        return (int) insert;

    }

    /***
     *  Insert a new record into the game record table if score is higher than the previous record.
     * @param user The username of the player.
     * @param score The score of the player.
     * @return A integer count all inserted records.
     */
    public int insertGameRecord(String user, int score) {

        user = user.toLowerCase();

        // This variable will count how many records been inserted.
        long insert = 0;

        // Get the writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Initialise a ContentValues that store a set of values to be inserted.
        ContentValues cv = new ContentValues();

        // A set of values to be inserted.
        cv.put(DbConfig.COLUMN_GAME_RECORD_USERNAME,
                user);

        cv.put(DbConfig.COLUMN_GAME_RECORD_SCORE,
                score);

        // Find possible records of the user.
        String query = String.format("SELECT * FROM %s WHERE username=\""
                        + user + "\" LIMIT 1",
                DbConfig.TABLE_GAME_RECORD);
        try (Cursor result = db.rawQuery(query, null)) {
            // If there is more than one result.¬
            if (result.getCount() != 0) {

                // Get the first result.
                result.moveToFirst();
                int prevScore = result.getInt(1);
                System.out.println("prevScore: " + prevScore);
                System.out.println("score: " + score);

                // Check if the score is higher than the previous score.
                if (score > prevScore) {
                    db.close();
                    return (int) insert;
                }

                // updating row
                db.update(DbConfig.TABLE_GAME_RECORD, cv,
                        " username= ?",
                        new String[]{user});
                db.close();
                return 1;

            } else {
                insert = insert + db.insert(DbConfig.TABLE_GAME_RECORD, null, cv);

                // Return number of record inserted.
                return (int) insert;

            }

        }

    }

    public void removeWord(String word) {

        // Get the writable database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the record.
        db.delete(DbConfig.TABLE_QUIZ, DbConfig.COLUMN_QUIZ_WORD + " = ?",
                new String[]{word});

        // Close the database.
        db.close();

    }

    /**
     * Query records match given keyword using SQL statements and return data.
     *
     * @param quizName A string represent the selection of agency.
     * @return An ArrayList of Term objects contain words and definitions needed to be display.
     */
    public ArrayList<Term> populateQuizArray(String quizName) {

        ArrayList<Term> terms = new ArrayList<>();

        String query = String.format("SELECT * FROM %s ",
                DbConfig.TABLE_QUIZ, quizName);

        // Get the readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Store results in Term ArrayList.
        try (Cursor result = db.rawQuery(query, null)) {

            // If there is more than one result.
            if (result.getCount() != 0) {

                // A Loop to go through all records.
                while (result.moveToNext()) {

                    // Create a Term object with one record.
                    String word = result.getString(0);
                    String definition = result.getString(1);

                    terms.add(new Term(word, definition));

                }
            } else {
                return terms;
            }

        }

        // return an ArrayList of Term objects.
        return terms;

    }

    /**
     * Query records match given keyword using SQL statements and return data.
     *
     * @return An Array of arrays which contain the ranking information.
     */
    public String[][] populateGameScoreArray() {

        String[][] populateGameScoreArray;

        String query = String.format("SELECT * FROM %s ",
                DbConfig.TABLE_GAME_RECORD) + " ORDER BY score, username ASC LIMIT 10";

        // Get the readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Store results in the String array.
        try (Cursor result = db.rawQuery(query, null)) {

            int n = 0;

            // If there is more than one result.
            if (result.getCount() != 0) {

                populateGameScoreArray = new String[result.getCount()][3];

                // A Loop to go through all records.
                while (result.moveToNext()) {

                    // Get the result from the database.
                    String username = result.getString(0);
                    String score = String.valueOf(result.getInt(1));

                    // Store the result in the array.
                    populateGameScoreArray[n][0] = username;
                    populateGameScoreArray[n][1] = score;

                    n++;

                }
            } else {
                return null;
            }

        }

        // return an ArrayList of players and their shortest times.
        return populateGameScoreArray;

    }


}
