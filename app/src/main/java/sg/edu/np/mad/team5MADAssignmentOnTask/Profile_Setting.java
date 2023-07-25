package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Setting extends AppCompatActivity {
    TextView editProfile,logout,deleteuser,back;

    DatabaseReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("Password");

        logout = (TextView)findViewById(R.id.logout);
        editProfile = (TextView)findViewById(R.id.edit_profile);
        deleteuser = findViewById(R.id.deleteuser);
        back = findViewById(R.id.back);


        userRef = FirebaseDatabase.getInstance().getReference("Users");

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Setting.this,EditProfile.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("Password",password);
                Log.v("settings","+"+username);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Profile_Setting.this)
                        .setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(Profile_Setting.this, LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        deleteuser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                new AlertDialog.Builder(Profile_Setting.this)
                        .setMessage("Warning, Are You Sure You want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                userRef.child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("Users").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("UserCount").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("TaskCount").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("UserDate").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("UserAchievement").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("Shop").child(username).removeValue();
                                FirebaseDatabase.getInstance().getReference("UserEquip").child(username).removeValue();

                                Intent intent5 = new Intent(Profile_Setting.this,LoginPage.class);
                                startActivity(intent5);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}