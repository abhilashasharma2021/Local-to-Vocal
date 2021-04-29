package com.localtovocal;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Runnable {
    Button timePicker1;
    TextView txt;
    String timeFormat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timePicker1 = findViewById(R.id.timePicker1);
        txt = findViewById(R.id.txt);

        MainActivity mainActivity = new MainActivity();
        Thread thread = new Thread(mainActivity);

        MainActivity mainActivity2 = new MainActivity();
        Thread thread2 = new Thread(mainActivity2);


       /* timePicker1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();

            }
        });
*/

        timePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
               int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txt.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();

            }
        });


        thread.start();
        thread2.start();


    }

    @Override
    public void run() {

        for (int i = 1; i < 5; i++) {

            try {
                Log.e("fdsfd", "hello" + i);
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("yyt", Objects.requireNonNull(e.getMessage()));
            }
            Log.e("usydusd", "thread is running..." + i);
        }

    }
}