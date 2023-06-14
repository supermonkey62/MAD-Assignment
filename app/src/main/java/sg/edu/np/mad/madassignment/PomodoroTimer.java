package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class PomodoroTimer extends AppCompatActivity {
    private TextView timerTextView;
    private ProgressBar progressBar;
    private Button startButton;
    private Button resetButton;

    private SeekBar workSeekBar;
    private TextView workDurationTextView;
    private SeekBar restSeekBar;
    private TextView restDurationTextView;
    private SeekBar longRestSeekBar;
    private TextView longRestDurationTextView;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis;

    private static final long POMODORO_DURATION = 25 * 60 * 1000; // 25 minutes
    private static final long SHORT_BREAK_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final long LONG_BREAK_DURATION = 15 * 60 * 1000; // 15 minutes

    private static final int POMODORO_STATE = 0;
    private static final int SHORT_BREAK_STATE = 1;
    private static final int LONG_BREAK_STATE = 2;

    private int currentState;
    private int completedPomodoros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        timerTextView = findViewById(R.id.pomotext);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.button);
        resetButton = findViewById(R.id.button2);
        workSeekBar = findViewById(R.id.seekBar1);
        workDurationTextView = findViewById(R.id.workDuration);
        restSeekBar = findViewById(R.id.seekBar2);
        restDurationTextView = findViewById(R.id.restDuration);
        longRestSeekBar = findViewById(R.id.seekBar3);
        longRestDurationTextView = findViewById(R.id.longRestDuration);

        workSeekBar.setProgress(24);
        restSeekBar.setProgress(4);
        longRestSeekBar.setProgress(14);


        workSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                workDurationTextView.setText(""+progress+"Mins");
                long workDurationInMillis = (progress + 1) * 60 * 1000; // Convert minutes to milliseconds

                // Update the timer duration if the current state is POMODORO_STATE
                if (currentState == POMODORO_STATE) {
                    timeLeftInMillis = workDurationInMillis;
                    updateTimerText();
                    updateProgressBar();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        restSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                restDurationTextView.setText("" + progress + "Mins");
                long restDurationInMillis = (progress + 1) * 60 * 1000; // Convert minutes to milliseconds

                // Update the rest duration if the current state is SHORT_BREAK_STATE
                if (currentState == SHORT_BREAK_STATE) {
                    timeLeftInMillis = restDurationInMillis;
                    updateTimerText();
                    updateProgressBar();
                }
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        longRestSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longRestDurationTextView.setText("" + progress + "Mins");
                long restDurationInMillis = (progress + 1) * 60 * 1000; // Convert minutes to milliseconds

                if (currentState == SHORT_BREAK_STATE) {
                    timeLeftInMillis = restDurationInMillis;
                    updateTimerText();
                    updateProgressBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        currentState = POMODORO_STATE;
        timeLeftInMillis = POMODORO_DURATION;
        updateTimerText();
        updateProgressBar();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                if (currentState == POMODORO_STATE) {
                    currentState = SHORT_BREAK_STATE;
                    timeLeftInMillis = SHORT_BREAK_DURATION;
                    Toast.makeText(PomodoroTimer.this,"Time for a Short Break!",Toast.LENGTH_LONG).show();
                } else if (currentState == SHORT_BREAK_STATE) {
                    currentState = POMODORO_STATE;
                    timeLeftInMillis = POMODORO_DURATION;
                    completedPomodoros++;
                    Toast.makeText(PomodoroTimer.this,"Time to get back to work!",Toast.LENGTH_LONG).show();
                }
                else if (currentState == POMODORO_STATE && completedPomodoros % 4 == 0) {
                    currentState = LONG_BREAK_STATE;
                    timeLeftInMillis = LONG_BREAK_DURATION;
                    Toast.makeText(PomodoroTimer.this,"Long Break Time!",Toast.LENGTH_LONG).show();
                } else {
                    currentState = POMODORO_STATE;
                    timeLeftInMillis = POMODORO_DURATION;
                    Toast.makeText(PomodoroTimer.this,"Back to work!",Toast.LENGTH_LONG).show();
                }
                isTimerRunning = false;
                updateTimerText();
                updateProgressBar();
                startButton.setText("Start");
            }
        };

        countDownTimer.start();
        isTimerRunning = true;
        startButton.setText("Pause");
        resetButton.setEnabled(false);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        startButton.setText("Start");
        resetButton.setEnabled(true);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isTimerRunning = false;
        startButton.setText("Start");
        resetButton.setEnabled(false);

        if (currentState == POMODORO_STATE) {
            timeLeftInMillis = POMODORO_DURATION;
        } else if (currentState == SHORT_BREAK_STATE) {
            timeLeftInMillis = SHORT_BREAK_DURATION;
        } else {
            timeLeftInMillis = LONG_BREAK_DURATION;
        }

        updateTimerText();
        updateProgressBar();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((timeLeftInMillis / (float) getCurrentTimerDuration()) * 100);
        progressBar.setProgress(progress);
    }

    private long getCurrentTimerDuration() {
        if (currentState == POMODORO_STATE) {
            return POMODORO_DURATION;
        } else if (currentState == SHORT_BREAK_STATE) {
            return SHORT_BREAK_DURATION;
        } else {
            return LONG_BREAK_DURATION;
        }
    }
}







