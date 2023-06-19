package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfile extends AppCompatActivity {

    DatabaseReference userRef;

    TextView changepassword,changeusername,changepfp,goback,editgoal;

    String TITLE = "Edit Profile";
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        changepassword = findViewById(R.id.change_password);
        changeusername = findViewById(R.id.change_username);
        changepfp = findViewById(R.id.change_pfp);
        
        goback = findViewById(R.id.back);


        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("Password");

        userRef = FirebaseDatabase.getInstance().getReference("Users");


        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, ChangePassword.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("Password",password);
                Log.v("Edit","+"+username);
                startActivity(intent);
            }
        });

        changeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EditProfile.this, ChangeDisplayName.class);
                intent2.putExtra("USERNAME", username);
                intent2.putExtra("Password",password);
                startActivity(intent2);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(EditProfile.this, Profile_Setting.class);
                finish();
            }
        });

        changepfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EditProfile.this, ChooseImageForpfp.class);
                intent4.putExtra("USERNAME", username);
                intent4.putExtra("Password",password);
                startActivity(intent4);
            }
        });







    }

}



