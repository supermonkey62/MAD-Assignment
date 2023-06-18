package sg.edu.np.mad.madassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity implements UserDataHolder.UserDataCallback {

    TextView profilepagesetting, profilepageback, profileusername, goal;
    ImageView editUsername;
    String displayname;
    String username;

    String password;
    String TITLE = "Profile Page";

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
        goal = findViewById(R.id.goal);



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
    }
}
