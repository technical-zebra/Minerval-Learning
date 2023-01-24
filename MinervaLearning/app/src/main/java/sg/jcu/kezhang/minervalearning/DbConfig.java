package sg.jcu.kezhang.minervalearning;


public class DbConfig {
    // Database configuration.
    public static final String DATABASE_NAME = "quiz_db";

    // Table configuration.
    public static final String TABLE_QUIZ = "quiz";
    public static final String TABLE_GAME_RECORD = "game_record";

    // Columns configuration for quiz table.
    public static final String COLUMN_QUIZ_ID = "_id";
    public static final String COLUMN_QUIZ_WORD = "word";
    public static final String COLUMN_QUIZ_DEFINITION = "definition";

    // Columns configuration for game record table.
    public static final String COLUMN_GAME_RECORD_USERNAME = "username";
    public static final String COLUMN_GAME_RECORD_SCORE = "score";
}
