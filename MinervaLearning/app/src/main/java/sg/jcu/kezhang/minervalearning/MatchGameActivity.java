package sg.jcu.kezhang.minervalearning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MatchGameActivity extends AppCompatActivity {

    // Declare variables
    private static SharedPreferences dataSource;

    QuizDatabaseHelper databaseHelper;

    List<Term> terms;

    TextView tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12;

    MaterialCardView card1, card2, card3, card4, card5, card6, card7, card8, card9,
            card10, card11, card12;

    TextView[] tiles;
    MaterialCardView[] cards;

    MediaPlayer mediaPlayer;

    String username;

    String pairSelection;
    MaterialCardView cardSelection;

    MaterialCardView restartNotification;
    Button restart, end;

    int numCards = 0;
    int endGame = 0;
    int second = 0;

    ColorStateList white,gray;

    private Handler handler;
    private boolean isRunning;

    /***
     * This method is called when the activity is created
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_game);

        // Set up the database helper
        databaseHelper = new QuizDatabaseHelper(MatchGameActivity.this);

        // Set up the shared preferences
        dataSource = getSharedPreferences("terms", Context.MODE_PRIVATE);

        // Set up textviews
        tile1 = findViewById(R.id.tile1);
        tile2 = findViewById(R.id.tile2);
        tile3 = findViewById(R.id.tile3);
        tile4 = findViewById(R.id.tile4);
        tile5 = findViewById(R.id.tile5);
        tile6 = findViewById(R.id.tile6);
        tile7 = findViewById(R.id.tile7);
        tile8 = findViewById(R.id.tile8);
        tile9 = findViewById(R.id.tile9);
        tile10 = findViewById(R.id.tile10);
        tile11 = findViewById(R.id.tile11);
        tile12 = findViewById(R.id.tile12);

        // Set up material card views
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card10 = findViewById(R.id.card10);
        card11 = findViewById(R.id.card11);
        card12 = findViewById(R.id.card12);

        // Set up a loop for all the textviews
        tiles = new TextView[]{tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8,
                tile9, tile10, tile11, tile12};

        // Set up a loop for all the material card views
        cards = new MaterialCardView[]{card1, card2, card3, card4, card5, card6, card7,
                card8, card9, card10, card11, card12};

        // Set up other variables
        restartNotification = findViewById(R.id.restart_notification);
        restart = findViewById(R.id.restart_button);
        end = findViewById(R.id.end_button);

        // Default value for checking correct pair of word and definition
        pairSelection = "-1";

        // Set up the color state list
        white = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white));
        gray = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray));

        // Get the username from the datasource
        username = dataSource.getString("username", null);

        // Set up the handlers for material card views
        for (MaterialCardView card : cards) {
            card.setBackgroundTintList(white);
            card.setOnClickListener(v -> {
                String pairId = card.getTag().toString();
                Toast.makeText(this, pairId, Toast.LENGTH_SHORT).show();

                if (card.getBackgroundTintList().equals(white)){
                    if (pairSelection.equals("-1")){
                        pairSelection = pairId;
                        cardSelection = card;
                        card.setBackgroundTintList(gray);
                    } else if (pairSelection.equals(pairId)) {
                        cardSelection.setBackgroundTintList(white);
                        dropCard(card);
                        dropCard(cardSelection);
                        numCards = numCards - 2;
                        pairSelection = "-1";
                        cardSelection = null;

                        if (numCards == 0){
                            disableStopwatch();
                            restartNotification.setAlpha(1);
                            restartNotification.bringToFront();
                            endGame = 1;
                            databaseHelper.insertGameRecord(username, second);
                        }
                    } else {
                        cardSelection.setBackgroundTintList(white);
                        pairSelection = "-1";
                        cardSelection = null;
                    }
                } else {
                    card.setBackgroundTintList(white);
                }

            });

        }

        if(savedInstanceState != null){
            second = savedInstanceState.getInt("second");
        }

        restart.setOnClickListener(v -> {
            if (endGame>0){
                recreate();
            }
        });

        end.setOnClickListener(v -> {
            if (endGame>0){
                Intent intent = new Intent(this, LeaderBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!=null && endGame>0){
                    float x_accl = sensorEvent.values[0];
                    float y_accl = sensorEvent.values[1];
                    float z_accl = sensorEvent.values[2];

                    float floatSum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl);

                    if (floatSum > 14){
                        recreate();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(sensorEventListener, sensorShake, SensorManager.SENSOR_DELAY_NORMAL);

    }

    /***
     * This method is called to drop the card
     * @param card the card to be dropped
     */
    private void dropCard(MaterialCardView card){
        card.setAlpha(0);
        card.setOnClickListener(null);
    }

    /***
     *  This method get the data from the datasourse and convert the json into list of Term objects
     * @return list of Term objects
     */
    public List<Term> getList(){
        List<Term> arrayItems = null;
        String serializedObject = dataSource.getString("terms", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Term>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    /***
     * This method is used to create the menu
     * @param menu the menu
     * @return true if the menu is created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     * This method is called when the user clicks on the menu item
     * @param item the menu item that was clicked
     * @return true if the menu item was successfully handled
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * This method is called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();

        terms = getList();

        int length = terms.size();

        // Set up the position of all cards
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            positions.add(i);
        }

        // Set up the random positions for the words and definitions
        Collections.shuffle(terms, new Random());
        Collections.shuffle(positions, new Random());

        // Set up the cards from the list of terms randomly
        for (int i = 0; i < 6; i++) {
            if (i < length){
                tiles[positions.get(i)].setText(terms.get(i).getWord());
                cards[positions.get(i)].setTag(i);
                tiles[positions.get(i + 6)].setText(terms.get(i).getDefinition());
                cards[positions.get(i + 6)].setTag(i);
            }
        }

        // Make empty card transparent
        for (MaterialCardView card : cards) {
            String trigger = card.getTag().toString();
            if (trigger.equals("-1")) {
                dropCard(card);
            } else {
                numCards++;
            }
        }

        enableStopwatch();

        // Set up the music
        mediaPlayer = MediaPlayer.create(this, R.raw.competition);
        mediaPlayer.start();

    }

    /***
     * This method is used to enable the stopwatch, and increase the second by 1 every second.
     */
    private void enableStopwatch(){
        isRunning = true;
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning){
                    second += 1;
                    handler.postDelayed(this, 1000);
                    Objects.requireNonNull(getSupportActionBar()).setTitle(second + " seconds");
                }
            }
        });
    }

    /***
     * This method is called when the activity is paused, it stops the music.
     */
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    /***
     * This method is called when the activity is on back pressed it stop the music.
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }

    /***
     * This method is called to stop the stopwatch
     */
    private void disableStopwatch(){
        isRunning = false;
    }

    /***
     * This method save the state before rotation
     * @param outState the state of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("second", second);
        super.onSaveInstanceState(outState);


    }
}