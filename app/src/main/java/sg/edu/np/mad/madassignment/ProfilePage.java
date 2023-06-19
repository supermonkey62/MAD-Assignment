package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity implements UserDataHolder.UserDataCallback {

    TextView profilepagesetting, profilepageback, profileusername;
    ImageView editUsername,pfp;
    String displayname;

    String ImageURI;
    String username;

    String password;
    String TITLE = "Profile Page";

    private ArrayList<String> goals;
    private ArrayAdapter<String> goalsAdapter;
    private ListView listView;
    private Button addGoalsButton;
    @SuppressLint("WrongViewCast")

    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        username = getIntent().getStringExtra("USERNAME");
        displayname = getIntent().getStringExtra("DISPLAYNAME");
        password = getIntent().getStringExtra("Password");

        profilepagesetting = findViewById(R.id.profilepagesettings);
        profilepageback = findViewById(R.id.profilepageback);
        profileusername = findViewById(R.id.profileusername);
        editUsername = findViewById(R.id.editUsername);
        pfp = findViewById(R.id.image_view);

        listView = findViewById(R.id.goal_list);
        addGoalsButton = findViewById(R.id.goal_button);


        //Display user's previously added goals from database
        DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("goals");
        userGoalsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goals.clear();

                for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                    String goal = goalSnapshot.getValue(String.class);
                    goals.add(goal);
                }

                goalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ProfilePage", "Error: " + databaseError.getMessage());
            }
        });
        addGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addgoal(view);
            }
        });

        goals = new ArrayList<>();
        goalsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goals);
        listView.setAdapter(goalsAdapter);
        listViewListener();

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue().toString();
                    Uri uri = Uri.parse(ImageURI);
                    pfp.setImageURI(uri);


                } else {
                    Log.v("ChangeDisplayName", "User not found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ChangePassword", "Error: " + databaseError.getMessage());
            }
        });



        UserDataHolder.getInstance().fetchUserData(username, this);
        profileusername.setText(displayname);
        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChangeDisplayName = new Intent(ProfilePage.this, ChangeDisplayName.class);
                toChangeDisplayName.putExtra("DISPLAYNAME", displayname);
                toChangeDisplayName.putExtra("USERNAME", username);
                startActivity(toChangeDisplayName);
            }
        });

        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        profilepagesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this, "Going to Profile Settings", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ProfilePage.this, Profile_Setting.class);
                intent2.putExtra("displayname", profileusername.getText().toString());
                intent2.putExtra("Password",password);
                intent2.putExtra("USERNAME",username);
                startActivity(intent2);
            }
        });
    }

    private void addgoal(View view){
        EditText user_input = findViewById(R.id.goal_input);
        String input_text = user_input.getText().toString();

        if(!(input_text.equals(""))){
            goalsAdapter.add(input_text);
            user_input.setText("");

            //Saving goal to database
            DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("goals");
            String goalId = userGoalsRef.push().getKey();
            userGoalsRef.child(goalId).setValue(input_text);
        }
        else{
            Toast.makeText(getApplicationContext(), "Empty input. Please enter a goal.", Toast.LENGTH_LONG).show();
        }
    }

    private void listViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Goal Removed", Toast.LENGTH_LONG).show();

                goals.remove(i);
                goalsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void onUserDataFetched(String displayname) {
        this.displayname = displayname;

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TITLE, "On Pause!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch user data again when the activity resumes
        UserDataHolder.getInstance().fetchUserData(username, this);
        profileusername.setText(displayname);
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue().toString();
                    Uri uri = Uri.parse(ImageURI);
                    pfp.setImageURI(uri);


                } else {
                    Log.v("ChangeDisplayName", "User not found");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ChangePassword", "Error: " + databaseError.getMessage());
            }
        });
    }
}
