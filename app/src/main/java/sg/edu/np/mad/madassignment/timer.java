package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;




public class timer extends AppCompatActivity {
    private long timeLeftInMillis;
    private long startTimeInMillis;
    private boolean isTimerRunning = false;
    private CountDownTimer countDownTimer;
    private Button button;

    private TextView textView;
    private EditText mEditTextInput;
    private Button buttonSet;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        button = findViewById(R.id.timerbttn);
        textView = findViewById(R.id.textView);
        mEditTextInput = findViewById(R.id.settimer);
        buttonSet = findViewById(R.id.button_set);
        progressBar = findViewById(R.id.progressBar);

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(timer.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(timer.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });
    }

    private void setTime(long milliseconds) {
        stopTimer();
        timeLeftInMillis = milliseconds;
        startTimeInMillis = milliseconds;
    }

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            button.setText("Start Timer");
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }


    }
    private void startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            button.setText("Stop Timer");

            if (timeLeftInMillis == 0) {
                timeLeftInMillis = startTimeInMillis;
            }

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;


                    int hours = (int) ((timeLeftInMillis / 1000) / 3600);
                    int minutes = (int) (((timeLeftInMillis / 1000) % 3600) / 60);
                    int seconds = (int) ((timeLeftInMillis / 1000) % 60);


                    String timeLeftFormatted;
                    if (hours > 0) {
                        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    } else {
                        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    }

                    textView.setText(timeLeftFormatted);

//                    int halfwayPoint = (int) (timeLeftInMillis / 2000);
//                    if (minutes == halfwayPoint) {
//                        Toast.makeText(timer.this, "Halfway done!", Toast.LENGTH_SHORT).show();
//                    }

                    if (minutes == 5 && seconds == 0) {
                        Toast.makeText(timer.this, "5 minutes remaining!", Toast.LENGTH_SHORT).show();
                    }
                    if(seconds == 5)
                    {
                        Toast.makeText(timer.this, "5 seconds remaining!", Toast.LENGTH_SHORT).show();
                    }
                    int progress = 100 - (int) (((startTimeInMillis - millisUntilFinished) / (float) startTimeInMillis) * 100);

                    progressBar.setProgress(progress);
                }

                public void onFinish() {
                    textView.setText("FINISH!!");
                    Toast.makeText(timer.this,"Completed!",Toast.LENGTH_LONG).show();
                    isTimerRunning = false;
                    button.setText("Start Timer");
                    timeLeftInMillis = 0;
                    progressBar.setProgress(0);
                }
            }.start();



        }
    }}


