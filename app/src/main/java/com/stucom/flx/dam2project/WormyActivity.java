package com.stucom.flx.dam2project;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

    public class WormyActivity extends AppCompatActivity
            implements WormyView.WormyListener, SensorEventListener {

        private WormyView wormyView;
        private TextView tvScore;
        // Sensors' related code
        private SensorManager sensorManager;
        private Button btnNewGame;
        boolean isActive = false;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_play);
            wormyView = findViewById(R.id.wormyView);

            btnNewGame = findViewById(R.id.btnNewGame);
            btnNewGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    wormyView.newGame();
                    btnNewGame.setVisibility(View.INVISIBLE);

                    if (isActive == false) {
                        isActive = true;

                    } else {


                    }

                }

            });
            wormyView.setWormyListener(this);
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        }




        @Override
        public void onResume() {
            super.onResume();
            btnNewGame.setVisibility(View.VISIBLE);
            // Connect the sensor's listener to the view
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            switch(event.getKeyCode()) {
                case KeyEvent.KEYCODE_A: wormyView.update(0, +10); break;
                case KeyEvent.KEYCODE_Q: wormyView.update(0, -10); break;
                case KeyEvent.KEYCODE_O: wormyView.update(-10, 0); break;
                case KeyEvent.KEYCODE_P: wormyView.update(+10, 0); break;
            }
            return super.dispatchKeyEvent(event);
        }



        @Override
        public void scoreUpdated(View view, int score) {


        }

        @Override
        public void gameLost(View view) {
            Toast.makeText(this, getString(R.string.you_lost), Toast.LENGTH_LONG).show();

        }


        @Override
        public void onSensorChanged(SensorEvent event) {
            float ax = event.values[0];
            float ay = event.values[1];
            wormyView.update(ax * -1, ay);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


