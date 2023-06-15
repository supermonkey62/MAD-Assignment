package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;


public class ProfilePage extends AppCompatActivity {

    TextView profilepagesetting,profilepageback,profileusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profilepagesetting = findViewById(R.id.profilepagesettings);
        profilepageback = findViewById(R.id.profilepageback);
        profileusername = findViewById(R.id.profileusername);


        String username = getIntent().getStringExtra("USERNAME");
        profileusername.setText(username);

        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " Back to Main Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilePage.this, MainPage.class);
                startActivity(intent);
            }
        });

        profilepagesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this,"Going to Profile Settings",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ProfilePage.this, Profile_Setting.class);
                startActivity(intent2);
            }
        });
    }
}
