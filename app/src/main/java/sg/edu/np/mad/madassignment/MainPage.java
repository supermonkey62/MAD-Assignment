package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {
    String title = "MainPage";
    Button pomodorotimer, normaltimer;

    Button pomodorotimer, normaltimer,Profile;

    TextView usernametext;
    ImageView calendarexpand;
    List<Event> eventList;
    DatabaseReference eventRef;

    public void fetchUserEvents(String username) {
        eventRef = FirebaseDatabase.getInstance().getReference("Event");

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList = new ArrayList<>();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null && event.getUsername().equals(username)) {
                        eventList.add(new Event(event.getUsername(), event.getTitle(), event.getType(), event.getDate()));
                        Log.v("Event Details", "Title: " + event.getTitle() + ", Date: " + event.getDate());
                    }
                }

                if (eventList.isEmpty()) {
                    Log.v("Event Details", "No items in the list");
                } else {
                    for (Event event : eventList) {
                        Log.v("Event Details", "Title: " + event.getTitle() + ", Date: " + event.getDate());
                    }
                }

                // Update the RecyclerView adapter with the populated eventList
                RecyclerView recyclerView = findViewById(R.id.calenderrecycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainPage.this));
                recyclerView.setAdapter(new Adapter(MainPage.this, eventList));

                int numEntities = eventList.size();
                Log.v("Event Details", "Number of entities: " + numEntities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        pomodorotimer = findViewById(R.id.pomobutton);
        normaltimer = findViewById(R.id.NormalTimerBttn);
        usernametext = findViewById(R.id.usernametext);
        Profile = findViewById(R.id.profilepageBttn);
        calendarexpand = findViewById(R.id.calendarexpand);

        String username = getIntent().getStringExtra("USERNAME");
        usernametext.setText(username);

        fetchUserEvents(username);

        pomodorotimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " Entering Pomodoro Timer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, PomodoroTimer.class);
                startActivity(intent);
            }
        });

        normaltimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this,"Entering Timer",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainPage.this, timer.class);
                startActivity(intent2);
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, " Entering Profile Page", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(MainPage.this, ProfilePage.class);
                startActivity(intent3);
            }
        });

        calendarexpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Entering Calendar", Toast.LENGTH_SHORT).show();
                Intent toCalendar = new Intent(MainPage.this, TaskCalendar.class);
                toCalendar.putExtra("EVENTLIST", new ArrayList<>(eventList));
                startActivity(toCalendar);
            }
        });
    }
}