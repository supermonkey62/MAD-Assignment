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


//public class timer extends AppCompatActivity {
//    private long timeLeftInMillis;
//    private long StarttimeInMillis;
//    private int counter;
//    private boolean isTimerRunning = false;
//    private CountDownTimer countDownTimer;
//    private Button button;
//
//    private TextView textView;
//    private EditText mEditTextInput;
//    private Button Buttonset;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_timer);
//
//        button = findViewById(R.id.timerbttn);
//        textView = findViewById(R.id.textView);
//        mEditTextInput = findViewById(R.id.settimer);
//        Buttonset = findViewById(R.id.button_set);
//
//        Buttonset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String input = mEditTextInput.getText().toString();
//                if (input.length() == 0) {
//                    Toast.makeText(timer.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                long millisInput = Long.parseLong(input) * 60000;
//                if (millisInput == 0) {
//                    Toast.makeText(timer.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                setTime(millisInput);
//                mEditTextInput.setText("");
//
//
//            }
//        });
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isTimerRunning) {
//                    startTimer();
//                } else {
//                    stopTimer();
//                }
//            }
//        });
//    }
////    private void resetTimer(){
////        timeLeftInMillis =
////    }
//    private void setTime(long milliseconds) {
//        timeLeftInMillis = milliseconds;
//    }
//    private void startTimer() {
//        if (!isTimerRunning) {
//            isTimerRunning = true;
//            button.setText("Stop Timer");
//
//            if (timeLeftInMillis == 0) {
//                // Timer is starting for the first time or after finishing
//                timeLeftInMillis = 30000; // Set the total duration of the timer in milliseconds
//            }
//
//            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
//                public void onTick(long millisUntilFinished) {
//                    timeLeftInMillis = millisUntilFinished;
//                    counter = (int) (millisUntilFinished / 1000)/60;
//                    textView.setText(String.valueOf(counter));
//
//                    // Calculate halfway point dynamically
//                    int halfwayPoint = (int) (timeLeftInMillis / 2000); // Halfway point at 50% of the remaining time
//
//                    // Display Toast message when halfway done
//                    if (counter == halfwayPoint) {
//                        Toast.makeText(timer.this, "Halfway done!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    // Display Toast message when 5 seconds remaining
//                    if (counter == 5) {
//                        Toast.makeText(timer.this, "5 seconds remaining!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                public void onFinish() {
//                    textView.setText("FINISH!!");
//                    isTimerRunning = false;
//                    button.setText("Start Timer");
//                    timeLeftInMillis = 0; // Reset the remaining time
//                }
//            }.start();
//        }
//    }
//
//    private void stopTimer() {
//        if (isTimerRunning) {
//            isTimerRunning = false;
//            button.setText("Start Timer");
//            countDownTimer.cancel();
//        }
//    }
//}

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
                timeLeftInMillis = startTimeInMillis; // Set the initial time from startTimeInMillis variable
            }

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;

                    // Calculate hours, minutes, and seconds remaining
                    int hours = (int) ((timeLeftInMillis / 1000) / 3600);
                    int minutes = (int) (((timeLeftInMillis / 1000) % 3600) / 60);
                    int seconds = (int) ((timeLeftInMillis / 1000) % 60);

                    // Format the time as a string
                    String timeLeftFormatted;
                    if (hours > 0) {
                        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    } else {
                        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    }

                    textView.setText(timeLeftFormatted);

                    int halfwayPoint = (int) (timeLeftInMillis / 2000);
                    if (minutes == halfwayPoint) {
                        Toast.makeText(timer.this, "Halfway done!", Toast.LENGTH_SHORT).show();
                    }

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
                    isTimerRunning = false;
                    button.setText("Start Timer");
                    timeLeftInMillis = 0;
                    progressBar.setProgress(0);
                }
            }.start();



        }
    }}


