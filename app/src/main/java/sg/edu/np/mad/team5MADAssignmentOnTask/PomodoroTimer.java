package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class PomodoroTimer extends AppCompatActivity {
    private TextView timerTextView;
    private ProgressBar progressBar;
    private Button startButton;
    private Button resetButton;

    private Button setButton;

    private Button endButton;

    private Button setrestButton;

    private NumberPicker hourPicker, minutePicker, restpicker;


    private TextView workDurationTextView;

    private TextView restDurationTextView;

    private TextView longRestDurationTextView;

    private DatabaseReference userTask;

    private TextView timertitle;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis;

    private long startTimeInMillis;
    private long POMODORO_DURATION ;
    private long SHORT_BREAK_DURATION;

    private static final int POMODORO_STATE = 0;
    private static final int SHORT_BREAK_STATE = 1;

    private int currentState;
    private int completedPomodoros;
    private float totalTimeInMinutes;
    private float existingTimeSpent;
    private float newTimeSpent;
    private int existingSessions;
    private int totalSessions;
    private boolean taskStatus;
    private float currentSessionTimeSpent;

    private boolean isPaused = false;




    private String tag,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);
        timerTextView = findViewById(R.id.pomotext);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.button);
        resetButton = findViewById(R.id.button2);
        setButton = findViewById(R.id.button_setpomo);
        endButton = findViewById(R.id.pomoendbttn);
        setrestButton = findViewById(R.id.button_setrestpomo);
        timertitle = findViewById(R.id.Ptimertitle);
        minutePicker = findViewById(R.id.pomominpicker);
        hourPicker=findViewById(R.id.pomohourpicker);
        restpicker = findViewById(R.id.restpicker);
        ImageButton back = findViewById(R.id.imageButton2);
        tag = getIntent().getStringExtra("TAG");
        title = getIntent().getStringExtra("TITLE");
        userTask = FirebaseDatabase.getInstance().getReference("Task");
        timertitle.setText(title);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        restpicker.setMinValue(1);
        restpicker.setMaxValue(59);
        restpicker.setVisibility(View.INVISIBLE);
        setrestButton.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        endButton.setEnabled(false);
        currentState = POMODORO_STATE;
        completedPomodoros = 0;
        totalTimeInMinutes = 0;
        taskStatus = false;
        currentSessionTimeSpent = 0;
        timeLeftInMillis = POMODORO_DURATION;
        progressBar.setProgress(100);
        String username = getIntent().getStringExtra("USERNAME");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning == true){
                    pauseTimer();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PomodoroTimer.this);
                    builder.setTitle("Leave Timer");
                    builder.setMessage("Timer is still Running.Leave Without Saving Timer?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            onBackPressedFragment();
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog dialog1 = builder.create();
                    dialog1.show();

                }
                else {
//                    onBackPressedFragment();
                    finish();
                }

            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    if(currentState == POMODORO_STATE)
                    {   long timeSpent = POMODORO_DURATION - timeLeftInMillis;
                        float minutes = (float) (((timeSpent / 1000) % 3600) / 60);
                        float seconds = (float) ((timeSpent / 1000) % 60);
                        totalTimeInMinutes += (minutes + (seconds / 60));
                        startButton.setText("Start Timer");
                        }
                    else{
                        startButton.setText("Start Timer");
                        }

                    }

                userTask = FirebaseDatabase.getInstance().getReference("Task");
                AlertDialog.Builder builder = new AlertDialog.Builder(PomodoroTimer.this);
                builder.setTitle("Task Completion");
                builder.setMessage("Are you done with the task?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hourPicker.setVisibility(View.VISIBLE);
                        minutePicker.setVisibility(View.VISIBLE);
                        setButton.setVisibility(View.VISIBLE);
                        isTimerRunning = false;
                        hourPicker.setVisibility(View.VISIBLE);
                        minutePicker.setVisibility(View.VISIBLE);
                        setButton.setVisibility(View.VISIBLE);
                        progressBar.setProgress(100);
                        timerTextView.setVisibility(View.INVISIBLE);

                        userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Task existingTask = dataSnapshot.getValue(Task.class);
                                    existingTimeSpent = existingTask.getTimespent();
                                    newTimeSpent = existingTimeSpent + totalTimeInMinutes;
                                    existingSessions = existingTask.getSessions();
                                    totalSessions = existingSessions + 1;
                                    taskStatus = true;
                                    Log.v("OldTaskTime", tag + "," + existingTimeSpent);
                                    Log.v("NewTaskTime",tag + "," + totalTimeInMinutes);
                                    Log.v("TaskTime", tag + "," + newTimeSpent);
                                } else {
                                    Log.v("TaskCount", tag + " does not exist.");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hourPicker.setVisibility(View.VISIBLE);
                        minutePicker.setVisibility(View.VISIBLE);
                        setButton.setVisibility(View.VISIBLE);
                        isTimerRunning = false;
                        hourPicker.setVisibility(View.VISIBLE);
                        minutePicker.setVisibility(View.VISIBLE);
                        setButton.setVisibility(View.VISIBLE);
                        timerTextView.setVisibility(View.INVISIBLE);
                        progressBar.setProgress(100);
                        userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Task existingTask = dataSnapshot.getValue(Task.class);
                                    existingTimeSpent = existingTask.getTimespent();
                                    newTimeSpent = existingTimeSpent + totalTimeInMinutes;
                                    existingSessions = existingTask.getSessions();
                                    totalSessions = existingSessions + 1;
                                    taskStatus = false;
                                    Log.v("OldTaskTime", tag + "," + existingTimeSpent);
                                    Log.v("NewTaskTime",tag + "," + totalTimeInMinutes);
                                    Log.v("TaskTime", tag + "," + newTimeSpent);
                                } else {
                                    Log.v("TaskCount", tag + " does not exist.");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });




        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hours = hourPicker.getValue();
                int minutes = minutePicker.getValue();

                if (hours == 0 && minutes == 0) {
                    Toast.makeText(PomodoroTimer.this, "Please set a positive duration", Toast.LENGTH_SHORT).show();
                    return;
                }


                POMODORO_DURATION = (hours * 60 + minutes) * 60000;
                timeLeftInMillis = POMODORO_DURATION;
                currentState = POMODORO_STATE;
                hourPicker.setVisibility(View.INVISIBLE);
                minutePicker.setVisibility(View.INVISIBLE);
                setButton.setVisibility(View.INVISIBLE);
                restpicker.setVisibility(View.VISIBLE);
                setrestButton.setVisibility(View.VISIBLE);
                progressBar.setProgress(100);
            }
        });

        setrestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int restmins = restpicker.getValue();
                SHORT_BREAK_DURATION = restmins*60000;
                restpicker.setVisibility(View.INVISIBLE);
                setrestButton.setVisibility(View.INVISIBLE);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    endButton.setEnabled(false);
                    pauseTimer();
                } else {
                    startTimer();
                    endButton.setEnabled(false);
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
        timerTextView.setVisibility(View.VISIBLE);
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
                    totalTimeInMinutes += currentSessionTimeSpent;
                }

                currentSessionTimeSpent = 0;
                timerTextView.setVisibility(View.INVISIBLE);
                progressBar.setProgress(100);

                if (currentState == POMODORO_STATE) {
                    currentState = SHORT_BREAK_STATE;
                    long timeSpent = POMODORO_DURATION;
                    float minutes = (float) (((timeSpent / 1000) % 3600) / 60);
                    float seconds = (float) ((timeSpent / 1000) % 60);
                    totalTimeInMinutes += minutes + (seconds / 60);
                    userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Task existingTask = dataSnapshot.getValue(Task.class);
                                existingTimeSpent = existingTask.getTimespent();
                                newTimeSpent = existingTimeSpent + totalTimeInMinutes;
                                existingSessions = existingTask.getSessions();
                                Log.v("OldTaskTime", tag + "," + existingTimeSpent);
                                Log.v("NewTaskTime", tag + "," + totalTimeInMinutes);
                                Log.v("TaskTime", tag + "," + newTimeSpent);
                            } else {
                                Log.v("TaskCount", tag + " does not exist.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    timeLeftInMillis = SHORT_BREAK_DURATION;
                    Toast.makeText(PomodoroTimer.this, "Time for a Short Break!", Toast.LENGTH_LONG).show();
                } else if (currentState == SHORT_BREAK_STATE) {
                    currentState = POMODORO_STATE;
                    timeLeftInMillis = POMODORO_DURATION;
                    completedPomodoros++;
                    Toast.makeText(PomodoroTimer.this, "Time to get back to work!", Toast.LENGTH_LONG).show();
                } else {
                    currentState = POMODORO_STATE;
                    timeLeftInMillis = POMODORO_DURATION;
                    Toast.makeText(PomodoroTimer.this, "Back to work!", Toast.LENGTH_LONG).show();
                }
                isTimerRunning = false;
                updateTimerText();
                updateProgressBar();
                startButton.setText("Start");
            }
        };;

        countDownTimer.start();
        isTimerRunning = true;
        startButton.setText("Pause");
        resetButton.setEnabled(false);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        isPaused = true;
        startButton.setText("Start");
        resetButton.setEnabled(true);
        endButton.setEnabled(true);
    }


//    private void pauseTimer() {
//        countDownTimer.cancel();
//        isTimerRunning = false;
//        isPaused = true;
//        startButton.setText("Start");
//        resetButton.setEnabled(true);
//        endButton.setEnabled(true);
//    }


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
        }

        updateTimerText();
        updateProgressBar();
    }

    private void updateTimerText() {
//
        int hours = (int) ((timeLeftInMillis / 1000) / 3600);
        int minutes = (int) (((timeLeftInMillis / 1000) % 3600) / 60);
        int seconds = (int) ((timeLeftInMillis / 1000) % 60);


        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        timerTextView.setText(timeLeftFormatted);
    }



    private void updateProgressBar() {
        int progress = (int) ((timeLeftInMillis / (float) getCurrentTimerDuration()) * 100);
        progressBar.setProgress(progress);

    }


    private long getCurrentTimerDuration() {
        if (currentState == POMODORO_STATE) {
            return POMODORO_DURATION;
        } else {
            return SHORT_BREAK_DURATION;
        }
    }
    private void onBackPressedFragment() {
        // Check if there are fragments in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer();
        if(isTimerRunning == true) {
            Toast.makeText(this, "Timer Paused!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected  void onResume(){
        super.onResume();
        super.onResume();

        if (isPaused) {
            Toast.makeText(this, "Timer is Paused. Unpause to continue", Toast.LENGTH_SHORT).show();
        }
        isPaused = false;

    }


    @Override
    protected void onStop() {
        super.onStop();
        String selectedDate = getIntent().getStringExtra("DATE");
        String username = getIntent().getStringExtra("USERNAME");
        String tag = getIntent().getStringExtra("TAG");
        String title = getIntent().getStringExtra("TITLE");
        String type = getIntent().getStringExtra("TYPE");
        String category = getIntent().getStringExtra("CATEGORY");
        userTask = FirebaseDatabase.getInstance().getReference("Task");
        Task newTask = new Task(username, title, selectedDate, tag, taskStatus, newTimeSpent, totalSessions, category,false);
        userTask.child(tag).setValue(newTask);
        Log.v("TaskSaving", title + " saved to database" + "," + newTimeSpent + "," + totalSessions);
        Log.v("TaskCheck",newTimeSpent + "," + totalSessions);

    }
}







