package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDataHolder {
    private static EventDataHolder instance;
    private List<Event> eventList;

    private DatabaseReference eventRef;

    private EventDataHolder() {
        eventList = new ArrayList<>();
        eventRef = FirebaseDatabase.getInstance().getReference("Event");
    }

    public static synchronized EventDataHolder getInstance() {
        if (instance == null) {
            instance = new EventDataHolder();
        }
        return instance;
    }

    public void fetchUserEvents(String username, final EventDataCallback callback) {
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if(event != null && event.getUsername() != null && event.getUsername().equals(username)) {
                        eventList.add(event);
                    }
                    else{
                        Log.v("EventDataHolderError", event.getUsername());
                    }
                }

                if (eventList.isEmpty()) {
                    Log.v("Event Details", "No items in the list");
                }
                // Invoke the callback method with the retrieved taskList
                callback.onEventDataFetched(eventList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public interface EventDataCallback {
        void onEventDataFetched(List<Event> events);
    }
}
