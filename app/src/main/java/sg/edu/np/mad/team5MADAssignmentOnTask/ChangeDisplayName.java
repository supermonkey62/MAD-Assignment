package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeDisplayName extends AppCompatActivity {
    Button confirmDisplayName;
    EditText changeDisplayName;
    DatabaseReference userRef;

    String username;
    String password;

    TextView profilepageback;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_display_name);

        changeDisplayName = findViewById(R.id.changeDisplayName);
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        confirmDisplayName = findViewById(R.id.confirm_button);
        profilepageback = findViewById(R.id.back);

        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        confirmDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String displayName = changeDisplayName.getText().toString();
                if(displayName == null) {

                    Log.v("DisplayName", "displayName is null");

                } else if (displayName.length() > 12) {
                    changeDisplayName.setError("Display Name can only Accept 12 Characters");
                    changeDisplayName.requestFocus();
                } else if (displayName.contains(" ")) {

                    changeDisplayName.setError("Spaces are not allowed");
                    changeDisplayName.requestFocus();
                    
                } else{
                    
                        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    dataSnapshot.getRef().child("displayname").setValue(displayName);
                                    Toast.makeText(getApplicationContext(), "Display Name Successfully Changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Log.v("ChangeDisplayName", "User not found");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.v("ChangeDisplayName", "Error: " + databaseError.getMessage());
                            }
                        });
                    }
                }


        });

        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}








