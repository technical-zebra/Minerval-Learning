package sg.jcu.kezhang.minervalearning;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FlashCardActivity extends AppCompatActivity {

    // Declare variables
    int numOfStillLearning, numOfKnow, currentCard = 0;

    TextView stillLearning, know, term;

    MaterialCardView cardForTerm, cardForNotification,backgroundCard;

    Boolean isFront = true;

    ImageView next, previous, info;

    private static SharedPreferences dataSource;

    MediaPlayer mediaPlayer;

    List<Term> terms;

    /***
     * This method is called when the activity is created
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        // Get the data source
        dataSource = getSharedPreferences("terms", Context.MODE_PRIVATE);

        // Declare the variables
        stillLearning = findViewById(R.id.still_learning);
        know = findViewById(R.id.know);
        term = findViewById(R.id.term);

        cardForTerm = findViewById(R.id.card_term);
        cardForNotification = findViewById(R.id.card_notification);
        backgroundCard = findViewById(R.id.card_background);

        next = findViewById(R.id.arrow_forward);
        previous = findViewById(R.id.arrow_back);
        info = findViewById(R.id.info);

        // Update variable from previous state
        if(savedInstanceState != null){
            currentCard = savedInstanceState.getInt("currentCard");
        }

        // Flip the card when the user click on the card
        cardForTerm.setOnClickListener(v -> flipCard());

        // Display information when the user click on the info button
        info.setOnClickListener(v -> {
            cardForNotification.setAlpha(1);
            final Handler handler = new Handler();
            handler.postDelayed(() -> cardForNotification.setAlpha(0), 3000);
        });

        // Go to the next card when the user click on the next button
        next.setOnClickListener(v -> returnNextCard());

        // Go to the previous card when the user click on the previous button
        previous.setOnClickListener(v -> returnPreviousCard());

    }

    /***
     * This method display info of the next card
     */
    private void returnNextCard() {
        if (currentCard < terms.size() - 1){
            currentCard++;
            updateCard(currentCard);
        } else {
            Toast.makeText(this, "All done!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /***
     *  This method display info of the previous card
     */
    private void returnPreviousCard() {
        if (currentCard > 0){
            currentCard--;
            updateCard(currentCard);
        } else {
            Toast.makeText(this, "This is the first term!", Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * This method update the num of still learning and know
     */
    private void updateCard(int current) {

        term.setText(terms.get(current).getWord());

        numOfStillLearning = terms.size() - currentCard;
        numOfKnow = currentCard;

        stillLearning.setText(String.valueOf(numOfStillLearning));
        know.setText(String.valueOf(numOfKnow));

    }

    /***
     * This method is flip the card between word and definition
     */
    private void flipCard() {

        AnimatorSet rotate = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.rotate);

        rotate.setTarget(cardForTerm);
        rotate.start();

        isFront = !isFront;
        if (isFront){
            term.setText(terms.get(currentCard).getWord());
        } else {
            term.setText(terms.get(currentCard).getDefinition());
        }
    }

    /***
     * This method is called when the activity is started
     */
    @Override
    protected void onStart() {
        super.onStart();

        terms = getList();
        updateCard(currentCard);

        mediaPlayer = MediaPlayer.create(this, R.raw.flashcard);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

    }

    /***
     *  This method retrieve quiz terms from datasource that store in json format
     * @return a list of quiz terms
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
     * This method is called when the activity is paused, it stop the music
     */
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    /***
     * This method is called when the activity is onBackPressed(), it stop the music
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }

    /***
     * This method save the current card for screen rotation
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentCard", currentCard);
        super.onSaveInstanceState(outState);

    }
}