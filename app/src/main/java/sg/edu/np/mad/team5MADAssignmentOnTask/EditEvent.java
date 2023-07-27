package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditEvent extends AppCompatActivity {

    private TextInputEditText eventTitleEditText, eventDescriptionEditText;
    private Button dateButton, timeButton, editEventButton, deleteEventButton;

    TextView cancelText;
    private java.util.Calendar startCalendar;
    private java.util.Calendar endCalendar;

    private String eventId, username;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());

    private boolean selectingStartDate = true; // Flag to track whether we are selecting start date or end date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Find the TextInputEditText views for title and description
        eventTitleEditText = findViewById(R.id.eventTitleEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);

        // Find the Button views for date and time
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        editEventButton = findViewById(R.id.editEventButton);

        cancelText = findViewById(R.id.cancelText);

        startCalendar = java.util.Calendar.getInstance();
        endCalendar = java.util.Calendar.getInstance();


        // Retrieve the intent data and pre-populate the input fields
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("TITLE");
            String description = intent.getStringExtra("DESCRIPTION");
            String startDate = intent.getStringExtra("START_DATE");
            String startTime = intent.getStringExtra("START_TIME");
            String endDate = intent.getStringExtra("END_DATE");
            String endTime = intent.getStringExtra("END_TIME");
            eventId = intent.getStringExtra("ID");
            username = intent.getStringExtra("USERNAME");

            eventTitleEditText.setText(title);
            eventDescriptionEditText.setText(description);

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            try {
                startCalendar.setTime(dateTimeFormat.parse(startDate + " " + startTime));
                endCalendar.setTime(dateTimeFormat.parse(endDate + " " + endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Update the date and time buttons with the pre-filled start and end dates/times
            updateDateButton();
            updateTimeButton();
        }

        // Add click listeners to the date and time buttons to show the date and time pickers
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectingStartDate) {
                    showDatePicker();
                } else {
                    showEndDatePicker();
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        cancelText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the action is UP, which indicates that the touch is released
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Finish the activity when the touch is released (click event)
                    finish();
                }
                return true; // Return true to indicate that the touch event is handled
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();

            }
        });

        deleteEventButton = findViewById(R.id.deleteEventButton);
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });


    }

    private void updateDateButton() {
        String formattedStartDate = dateFormat.format(startCalendar.getTime());
        String formattedEndDate = dateFormat.format(endCalendar.getTime());
        String dateRange = formattedStartDate + " - " + formattedEndDate;
        dateButton.setText(dateRange);
    }

    private void updateTimeButton() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

        String startTime = timeFormat.format(startCalendar.getTime());
        String endTime = timeFormat.format(endCalendar.getTime());

        String formattedTime = startTime + " - " + endTime;
        timeButton.setText(formattedTime);
    }


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startCalendar.set(Calendar.YEAR, year);
                        startCalendar.set(Calendar.MONTH, month);
                        startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectingStartDate = false; // Next selection will be for end date
                        showEndDatePicker();
                    }
                },
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showEndDatePicker() {
        DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endCalendar.set(Calendar.YEAR, year);
                        endCalendar.set(Calendar.MONTH, month);
                        endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Check if the selected end date is before the start date
                        if (endCalendar.getTimeInMillis() < startCalendar.getTimeInMillis()) {
                            // Show toast message when end date is earlier than start date
                            Toast.makeText(EditEvent.this, "Ending date cannot be earlier than the starting date", Toast.LENGTH_SHORT).show();
                            // Reshow the end date picker
                            showEndDatePicker();
                        } else {
                            selectingStartDate = true; // Next selection will be for start date
                            updateDateButton();
                        }
                    }
                },
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the previously selected start date as the initial selected date for the end date picker
        endDatePickerDialog.getDatePicker().updateDate(
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
        );

        endDatePickerDialog.show();
    }



    private void showTimePicker() {
        TimePickerDialog startTimePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        startCalendar.set(Calendar.MINUTE, minute);
                        showEndTimePicker();
                    }
                },
                startCalendar.get(Calendar.HOUR_OF_DAY),
                startCalendar.get(Calendar.MINUTE),
                false
        );
        startTimePickerDialog.show();
    }

    private void showEndTimePicker() {
        TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endCalendar.set(Calendar.MINUTE, minute);

                        // Check if the selected end time is the same as the start time or earlier
                        if (endCalendar.getTimeInMillis() <= startCalendar.getTimeInMillis()) {
                            // Show toast message when end time is the same as or earlier than start time
                            Toast.makeText(EditEvent.this, "End time cannot be the same as or earlier than the start time", Toast.LENGTH_SHORT).show();
                            // Reshow the end time picker
                            showEndTimePicker();
                        } else {
                            updateTimeButton();
                        }
                    }
                },
                endCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.MINUTE),
                false
        );

        // Show the end time picker dialog
        endTimePickerDialog.show();
    }


    private void createEvent() {
        String title = eventTitleEditText.getText().toString().trim();
        String description = eventDescriptionEditText.getText().toString().trim();
        String startDate = dateFormat.format(startCalendar.getTime());
        String startTime = getTimeAsString(startCalendar);
        String endDate = dateFormat.format(endCalendar.getTime());
        String endTime = getTimeAsString(endCalendar);
        String startDateFormatted = convertToFormattedDate(startCalendar);
        String endDateFormatted = convertToFormattedDate(endCalendar);

        // Perform entry validation for title and description
        if (TextUtils.isEmpty(title)) {
            eventTitleEditText.setError("Title cannot be empty");
        }

        if (TextUtils.isEmpty(description)) {
            eventDescriptionEditText.setError("Description cannot be empty");
        }

        // Check if there are any validation errors
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {

            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Event");
            eventRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Event event = new Event(username,eventId, title, startDateFormatted, startTime, endDateFormatted, endTime, description);
                        eventRef.child(eventId).setValue(event);
                        finish();
                    } else {
                        Log.v(title, eventId + " does not exists");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.v(title, "Error: " + databaseError.getMessage());
                }
            });


        }
    }

    private String getTimeAsString(java.util.Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    private String convertToFormattedDate(java.util.Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void deleteEvent() {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Event");
        eventRef.child(eventId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditEvent.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after successful deletion
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditEvent.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

