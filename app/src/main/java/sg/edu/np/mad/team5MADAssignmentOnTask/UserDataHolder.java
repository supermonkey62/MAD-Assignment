package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataHolder {
    private static UserDataHolder instance;
    private  String Displayname;

    private  String Image_URI;
    private DatabaseReference userRef;

    private UserDataHolder() {
        Displayname = new String();

        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public static synchronized UserDataHolder getInstance() {
        if (instance == null) {
            instance = new UserDataHolder();
        }
        return instance;
    }

    public void fetchUserData(String username, final UserDataCallback callback) {
        userRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Displayname = dataSnapshot.child("displayname").getValue(String.class);
                    Log.v("User Display Name", "Display Updated" + Displayname);


                }
                // Invoke the callback method with the retrieved taskList
                callback.onUserDataFetched(Displayname);

                Log.v("callback Value",Displayname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public void fetchUserTasks(String username, View.OnClickListener onClickListener) {

    }

    public interface UserDataCallback {
        void onUserDataFetched(String displayname);

    }
}



