package sg.jcu.kezhang.minervalearning;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingActivity extends AppCompatActivity {

    // Declare UI components.
    RadioButton black_and_white, dark_blue_and_white;
    Button apply;

    /***
     * This method is called when the activity is first created.
     * @param savedInstanceState A bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Assign UI components.
        apply = findViewById(R.id.apply_btn);
        black_and_white = findViewById(R.id.black_and_white_rb);
        dark_blue_and_white = findViewById(R.id.dark_blue_and_white_rb);

        // Setup the sensor manager, the light sensor, and the sensor listener.
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener sensorEventListener = new SensorEventListener() {

            /***
             * This method is called when the sensor value changes.
             * @param sensorEvent The sensor event.
             */
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                System.out.println(sensorEvent.values[0]);
                if (sensorEvent.values[0] < 40) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    finish();
                }
            }

            /***
             * This method is called when the sensor accuracy changes.
             * @param sensor The sensor.
             * @param i The accuracy.
             */
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register the listener with the light sensor.
        if(lightSensor != null){
            System.out.println("Sensor.TYPE_LIGHT Available");
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            System.out.println("Sensor.TYPE_LIGHT NOT Available");
        }



        // Set up the listener for the apply button, which change the theme of the app.
        apply.setOnClickListener(view -> {

            /*Check which radio button is selected, and enable night mode (black_and_white theme)
            if condition is meet. */
            if (black_and_white.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // End the activity
            finish();

        });
    }


}