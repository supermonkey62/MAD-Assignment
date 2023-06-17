package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class EditProfile extends AppCompatActivity {

    TextView changepassword,changeusername,changepfp,goback,editgoal;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        changepassword = findViewById(R.id.change_password);
        changeusername = findViewById(R.id.change_username);
        changepfp = findViewById(R.id.change_pfp);
        editgoal = findViewById(R.id.edit_goal);
        goback = findViewById(R.id.back);
        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("Password");


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
                intent3.putExtra("USERNAME", username);
                intent3.putExtra("Password",password);
                startActivity(intent3);
            }
        });




    }
}



