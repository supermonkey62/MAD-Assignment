package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

public class timer extends AppCompatActivity {
    private long timeLeftInMillis;
    private long startTimeInMillis;
    private boolean isTimerRunning = false;
    private CountDownTimer countDownTimer;
    private Button startButton, endButton;
    private TextView timerTextView, timerTitleTextView;
    private EditText inputEditText;
    private Button setButton;
    private NumberPicker hourPicker, minutePicker;
    private ProgressBar progressBar;
    private DatabaseReference userTask;
    private float existingTimeSpent;
    private float newTimeSpent;
    private int existingSessions;
    private int totalSessions;
    private float totalTimeInMinutes;
    private boolean taskStatus;

    private boolean isPaused = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        startButton = findViewById(R.id.timerbttn);
        endButton = findViewById(R.id.timerend);
        timerTextView = findViewById(R.id.textView);
        timerTitleTextView = findViewById(R.id.timertitle);
        setButton = findViewById(R.id.button_set);
        progressBar = findViewById(R.id.progressBar);
        hourPicker = findViewById(R.id.hourpicker);
        minutePicker = findViewById(R.id.minpicker);
        ImageButton back = findViewById(R.id.imageButtontimer);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        String tag = getIntent().getStringExtra("TAG");
        String title = getIntent().getStringExtra("TITLE");
        timerTitleTextView.setText(title);
        Intent backtask = new Intent(timer.this,TodolistFragment.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning == true){
                    pauseTimer();
                    AlertDialog.Builder builder = new AlertDialog.Builder(timer.this);
                    builder.setTitle("Leave Timer");
                    builder.setMessage("Timer is still Running.Leave Without Saving Timer?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
                userTask = FirebaseDatabase.getInstance().getReference("Task");
                AlertDialog.Builder builder = new AlertDialog.Builder(timer.this);
                builder.setTitle("Task Completion");
                builder.setMessage("Are you done with the task?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hourPicker.setVisibility(View.VISIBLE);
                        minutePicker.setVisibility(View.VISIBLE);
                        setButton.setVisibility(View.VISIBLE);
                        isTimerRunning = false;
                        handleTaskCompletion(tag, true);
                        finish();





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
                        handleTaskCompletion(tag, false);
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
                    Toast.makeText(timer.this, "Please set a positive duration", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = (hours * 60 + minutes) * 60000;
                setTime(millisInput);
                hourPicker.setVisibility(View.INVISIBLE);
                minutePicker.setVisibility(View.INVISIBLE);
                setButton.setVisibility(View.INVISIBLE);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
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

    private void handleTaskCompletion(String tag, boolean done) {
        if (countDownTimer != null) {
            long timeSpent = startTimeInMillis - timeLeftInMillis;
            float minutes = (float) (((timeSpent / 1000) % 3600) / 60);
            float seconds = (float) ((timeSpent / 1000) % 60);
            totalTimeInMinutes += minutes + (seconds / 60);
            totalSessions += 1;
            countDownTimer.cancel();
            isTimerRunning = false;
            startButton.setText("Start Timer");
            timerTextView.setText("");
            progressBar.setProgress(100);
        }

        userTask.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Task existingTask = dataSnapshot.getValue(Task.class);
                    existingTimeSpent = existingTask.getTimespent();
                    newTimeSpent += (existingTimeSpent + totalTimeInMinutes);
                    existingSessions = existingTask.getSessions();
                    totalSessions = (existingSessions + totalSessions);
                    taskStatus = done;
                    Log.v("OldTaskTime", tag + "," + existingTimeSpent);
                    Log.v("NewTaskTime",tag + "," + totalTimeInMinutes);
                    Log.v("TaskTime", tag + "," + newTimeSpent);
                    Log.v("sessions", tag + ","+ totalSessions);
                } else {
                    Log.v("TaskCount", tag + " does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error case
            }
        });
    }

    private void setTime(long milliseconds) {
        stopTimer();
        timeLeftInMillis = milliseconds;
        startTimeInMillis = milliseconds;
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

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            startButton.setText("Start Timer");
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            endButton.setVisibility(View.VISIBLE);
        }
    }

    private void startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            startButton.setText("Stop Timer");
            endButton.setVisibility(View.GONE);

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
                    timerTextView.setText(timeLeftFormatted);

                    if (hours==0 && minutes == 5 && seconds == 0) {
                        Toast.makeText(timer.this, "5 minutes remaining!", Toast.LENGTH_SHORT).show();
                    }
                    if (hours==0 && minutes == 5 && seconds == 5) {
                        Toast.makeText(timer.this, "5 seconds remaining!", Toast.LENGTH_SHORT).show();
                    }
                    int progress = 100 - (int) (((startTimeInMillis - millisUntilFinished) / (float) startTimeInMillis) * 100);
                    progressBar.setProgress(progress);
                }

                public void onFinish() {
                    Intent backtask = new Intent(timer.this,TodolistFragment.class);
                    String tag = getIntent().getStringExtra("TAG");
                    timerTextView.setText("FINISH!!");
                    Toast.makeText(timer.this, "Completed!", Toast.LENGTH_LONG).show();
                    userTask = FirebaseDatabase.getInstance().getReference("Task");
                    AlertDialog.Builder builder = new AlertDialog.Builder(timer.this);
                    builder.setTitle("Task Completion");
                    builder.setMessage("Are you done with the task?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hourPicker.setVisibility(View.VISIBLE);
                            minutePicker.setVisibility(View.VISIBLE);
                            setButton.setVisibility(View.VISIBLE);
                            handleTaskCompletion(tag, true);
                            dialog.dismiss();
                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hourPicker.setVisibility(View.VISIBLE);
                            minutePicker.setVisibility(View.VISIBLE);
                            setButton.setVisibility(View.VISIBLE);
                            handleTaskCompletion(tag, false);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    isTimerRunning = false;
                    startButton.setText("Start Timer");
                    progressBar.setProgress(0);
                }
            }.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer();
        if(isTimerRunning) {
            Toast.makeText(this, "Timer Paused!", Toast.LENGTH_SHORT).show();
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
        String category = getIntent().getStringExtra("CATEGORY");
        userTask = FirebaseDatabase.getInstance().getReference("Task");
        String collaborators = getIntent().getStringExtra("COLLABORATORS");
        Task newTask = new Task(username, title, selectedDate, tag, taskStatus, newTimeSpent, totalSessions, category, collaborators, false);
        userTask.child(tag).setValue(newTask);
        Log.v("TaskSaving", title + " saved to database"+ collaborators);
        Log.v("TaskCheck",newTimeSpent + "," + totalSessions);

    }
    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        isPaused = true;
        startButton.setText("Start");
        endButton.setEnabled(true);
    }


}

