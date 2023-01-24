package sg.jcu.kezhang.minervalearning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    // Declare variables
    private static SharedPreferences.Editor editor;

    FloatingActionButton addWord;
    QuizDatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ArrayList<Term> terms;

    TextView quizTitle, quizDescription;

    CardView flashCard, matchGame;

    String username;
    String quizName;

    MediaPlayer mediaPlayer;

    /***
     * This method is called when the activity is created
     * @param savedInstanceState - the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No shadow on the action bar
        getSupportActionBar().setElevation(0);

        setContentView(R.layout.activity_quiz);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        terms = new ArrayList<>();

        databaseHelper = new QuizDatabaseHelper(QuizActivity.this);

        SharedPreferences dataSource = getSharedPreferences("terms", Context.MODE_PRIVATE);
        editor = dataSource.edit();

        addWord = findViewById(R.id.add_item);
        quizTitle = findViewById(R.id.quiz_title);
        recyclerView = findViewById(R.id.quiz_recycler_view);
        quizDescription = findViewById(R.id.set_description);

        flashCard = findViewById(R.id.flash_card);
        matchGame = findViewById(R.id.match_game);

        username = getIntent().getStringExtra("username");
        quizName = quizTitle.getText().toString();

        /* Listener for add word button. */
        addWord.setOnClickListener(view -> {
            // Create a new intent to open the AddWordActivity
            Intent intent = new Intent(this, AddWordActivity.class);
            startActivity(intent);

        });

        /* Listener for flashcard game card. */
        flashCard.setOnClickListener(view -> {
            // Create a new intent to open the FlashCardActivity
            Intent intent = new Intent(this, FlashCardActivity.class);
            startActivity(intent);

        });

        /* Listener for match game card. */
        matchGame.setOnClickListener(view -> {
            // Create a new intent to open the MatchGameActivity
            Intent intent = new Intent(this, MatchGameActivity.class);
            startActivity(intent);

        });
    }

    // This method convert the list of terms to a JSON string
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    /***
     *  This method is used to set the value and the key in the shared preferences
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        editor.clear();
        editor.putString(key, value);
        editor.commit();
    }

    /***
     * This method is called to update the recycler view
     */
    private void updateRecyclerView() {
        terms  = databaseHelper.populateQuizArray(quizName);

        setList("terms", terms);

        editor.putString("username", username);
        editor.commit();

        System.out.println("terms: " + terms);

        if (terms != null || !terms.isEmpty()) {

            // Set CustomAdapter as the adapter for RecyclerView.
            CustomAdapter adapter = new CustomAdapter(terms, QuizActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(QuizActivity.this));

        }
    }

    /***
     * This method is called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Update RecyclerView.
        updateRecyclerView();

        // Set quiz info.
        quizDescription.setText(username + " | " + terms.size() + " terms");

        // Set music.
        mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer.start();
    }

    /***
     * This method is called when the activity is paused.
     */
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    /***
     * This method is called when the user want to end current activity and go back to previous one.
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }

    /***
     * Create the menu for the activity.
     * @param menu The menu to be created.
     * @return True if the menu is created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     *  This method is called when an item in the options menu is selected.
     * @param item The item that was selected.
     * @return True if the item was selected, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.settings) {
            mediaPlayer = null;
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}