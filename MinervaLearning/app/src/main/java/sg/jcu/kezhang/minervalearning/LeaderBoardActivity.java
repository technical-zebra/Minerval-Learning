package sg.jcu.kezhang.minervalearning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderBoardActivity extends AppCompatActivity {

    // Declare variables
    QuizDatabaseHelper databaseHelper;

    MediaPlayer mediaPlayer;

    Button finish;

    String username;

    /***
     * This method is called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        // Initialize new database helper
        databaseHelper = new QuizDatabaseHelper(LeaderBoardActivity.this);

        // Get the players and their ranking from the shared preferences
        String[][] records = databaseHelper.populateGameScoreArray();

        // Set up the list view
        ListView playerRank = findViewById(R.id.ranking_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.player);
        playerRank.setAdapter(adapter);

        // Declare shared preferences
        SharedPreferences dataSource = getSharedPreferences("terms", Context.MODE_PRIVATE);

        // Get the username from the shared preferences
        username = dataSource.getString("username", null);

        // Produce the ranking list
        int i = 1;
        for (String[] record: records){
            adapter.add("  " + i+"|    " + record[0] + "    " + record[1]+" seconds");
            i++;
        }

        finish = findViewById(R.id.finish_button);
        TextView sharing = findViewById(R.id.sharing);

        // Set up the finish button
        finish.setOnClickListener(v -> finish());

        // Set up the sharing text view, which will share the app to the user's social media
        sharing.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I, " + username + " have complete the " +
                    "match game in Minerva Learning! Come and challenge me!");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        // Set up the background music
        mediaPlayer = MediaPlayer.create(this, R.raw.winner);
        mediaPlayer.start();

    }

    /***
     * This method is to stop the music when the user leaves the activity
     */
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    /***
     * This method is called when the activity is on back pressed.
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }




}