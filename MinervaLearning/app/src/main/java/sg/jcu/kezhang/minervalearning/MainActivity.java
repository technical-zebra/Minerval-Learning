package sg.jcu.kezhang.minervalearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare variables.
    private EditText loginInput;
    private String username;

    /***
     *  This method is called when the activity is first created.
     * @param savedInstanceState Bundle loaded from the previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove action bar.
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        // Set layout.
        setContentView(R.layout.activity_main);

        // Initialise variables.
        loginInput = findViewById(R.id.login_input);
        Button loginButton = findViewById(R.id.login_button);

        /* Button Listener for login input button. */
        loginButton.setOnClickListener(view -> {
            // Get username from input.
            username = loginInput.getText().toString().toLowerCase();

            // Check if username is empty.
            if (username != null && !username.isEmpty()) {
                // Create intent to start game activity.
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                // Display error message.
                Toast.makeText(this, "Please enter a valid username",
                        Toast.LENGTH_SHORT).show();
            }


        });

    }

}