package sg.jcu.kezhang.minervalearning;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddWordActivity extends AppCompatActivity {

    // Declare variables
    private EditText wordInput;
    private EditText definitionInput;
    QuizDatabaseHelper databaseHelper;

    /***
     * onCreate method
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        databaseHelper = new QuizDatabaseHelper(AddWordActivity.this);

        // Initialize variables
        wordInput = findViewById(R.id.word_input);
        definitionInput = findViewById(R.id.definition_input);
        Button saveButton = findViewById(R.id.save_button);

        // Save button
        saveButton.setOnClickListener(view -> {
            // Get input
            String word = wordInput.getText().toString();
            String definition = definitionInput.getText().toString();

            // Check if input is empty
            if (word.isEmpty() || definition.isEmpty()){
                Toast.makeText(this, "Word or definition is missing",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Add word to database
                databaseHelper.insertWord(new String[] { word, definition });
                finish();
            }

        });
    }






}