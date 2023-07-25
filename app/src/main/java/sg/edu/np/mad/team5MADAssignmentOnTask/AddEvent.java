package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEvent extends AppCompatActivity {

    private TextInputLayout eventTitleInputLayout, eventDescriptionInputLayout;
    private Button dateButton, timeButton;
    private Calendar startCalendar, endCalendar;

    TextView cancelText;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());

    private boolean selectingStartDate = true; // Flag to track whether we are selecting start date or end date
    String username;

    String title = "AddEvent";
    DatabaseReference eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        username = getIntent().getStringExtra("USERNAME");

        eventTitleInputLayout = findViewById(R.id.eventTitleInputLayout);
        eventDescriptionInputLayout = findViewById(R.id.eventDescriptionInputLayout);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        cancelText = findViewById(R.id.cancelText);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        updateDateButton();
        updateTimeButton();

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

        Button addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
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
                            Toast.makeText(AddEvent.this, "Ending date cannot be earlier than the starting date", Toast.LENGTH_SHORT).show();
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
        // Custom TimePickerDialog to handle the case when start and end times are the same
        TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endCalendar.set(Calendar.MINUTE, minute);

                        // Check if the selected end time is the same as the start time
                        if (startCalendar.getTimeInMillis() == endCalendar.getTimeInMillis()) {
                            // Show toast message when start and end times are the same
                            Toast.makeText(AddEvent.this, "End time cannot be the same as the start time", Toast.LENGTH_SHORT).show();
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

    private void createEvent() {
        String title = eventTitleInputLayout.getEditText().getText().toString().trim();
        String description = eventDescriptionInputLayout.getEditText().getText().toString().trim();
        String startDate = dateFormat.format(startCalendar.getTime());
        String startTime = getTimeAsString(startCalendar);
        String endDate = dateFormat.format(endCalendar.getTime());
        String endTime = getTimeAsString(endCalendar);
        String startDateFormatted = convertToFormattedDate(startCalendar);
        String endDateFormatted = convertToFormattedDate(endCalendar);

        // Perform entry validation for title and description
        if (TextUtils.isEmpty(title)) {
            eventTitleInputLayout.setError("Title cannot be empty");
        }

        if (TextUtils.isEmpty(description)) {
            eventDescriptionInputLayout.setError("Description cannot be empty");
        }

        // Check if there are any validation errors
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
            generateEventId(username, new EventIdCallback() {
                @Override
                public void onEventIdGenerated(String eventId) {
                    DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Event");
                    eventRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.v(title, eventId + " already exists");
                            } else {
                                Event event = new Event(username,eventId, title, startDateFormatted, startTime, endDateFormatted, endTime, description);
                                eventRef.child(eventId).setValue(event);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.v(title, "Error: " + databaseError.getMessage());
                        }
                    });
                }
            });
        }
    }

    // Helper method to format time as a string (e.g., "6:00 AM")
    private String getTimeAsString(Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    private void generateEventId(final String username, final EventIdCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("eventCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int eventCount = dataSnapshot.getValue(Integer.class); // Assuming eventCount is stored as an integer in Firebase
                    String eventId = username + eventCount;
                    // Call the callback with the generated eventId
                    callback.onEventIdGenerated(eventId);

                    // Increment the eventCount and update it back in the database
                    int newEventCount = eventCount + 1;
                    userRef.setValue(newEventCount);
                } else {
                    // User does not exist in Firebase, handle this case if needed
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AddEvent", "Error retrieving event count: " + databaseError.getMessage());
                // Handle the error if needed
            }
        });
    }

    private String convertToFormattedDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    public interface EventIdCallback {
        void onEventIdGenerated(String eventId);
    }

}



