package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    TextView profilepagesetting,profilepageback,profileusername,goal;
    DatabaseReference userRef;
    String goal1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profilepagesetting = findViewById(R.id.profilepagesettings);
        profilepageback = findViewById(R.id.profilepageback);
        profileusername = findViewById(R.id.profileusername);
        goal = findViewById(R.id.goal);
        String username = getIntent().getStringExtra("USERNAME");
        profileusername.setText(username);
        String Password = getIntent().getStringExtra("Password");






        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " Back to Main Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilePage.this, MainPage.class);
                intent.putExtra("USERNAME", profileusername.getText().toString());
                intent.putExtra("Password", Password);
                startActivity(intent);
            }
        });

        profilepagesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this,"Going to Profile Settings",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(ProfilePage.this, Profile_Setting.class);
                intent2.putExtra("USERNAME", profileusername.getText().toString());
                intent2.putExtra("Password", Password);
                startActivity(intent2);
            }
        });
    }
}
