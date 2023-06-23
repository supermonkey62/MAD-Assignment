package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ImageHolder {
    private static ImageHolder instance;
    private  String ImageURI;

    private DatabaseReference userRef;


    private ImageHolder() {;

        userRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public static synchronized ImageHolder getInstance() {
        if (instance == null) {
            instance = new ImageHolder();
        }
        return instance;
    }

    public void fetchImageTasks(String username, final ImageDataCallback callback) {
        userRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue(String.class);



                }



                // Invoke the callback method with the retrieved taskList
                callback.onImageFetched(ImageURI);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error case if the listener is canceled or fails to retrieve data
                // You can show an error message or handle it as per your requirements
            }
        });
    }

    public void fetchImageTasks(String username, View.OnClickListener onClickListener) {

    }

    public interface ImageDataCallback {
        void onImageFetched(String ImageURI);

    }
}



