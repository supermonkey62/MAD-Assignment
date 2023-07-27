package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfile extends AppCompatActivity {

    DatabaseReference userRef;

    TextView changepassword,changeusername,changepfp,changebanner,goback;
    String username;

    String TITLE = "Edit Profile";
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        changepassword = findViewById(R.id.change_password);
        changeusername = findViewById(R.id.change_username);
        changepfp = findViewById(R.id.change_pfp);
        changebanner = findViewById(R.id.change_banner);

        goback = findViewById(R.id.back);


        username = getIntent().getStringExtra("USERNAME");
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

        changebanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditProfile.this).crop(392,142).compress(1024).maxResultSize(1925,1080)
                        .start();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(data != null && data.getData() != null)
            {
                String selectedImageUriBann = data.getData().toString();
                userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            snapshot.getRef().child("bannerURI").setValue(selectedImageUriBann);
                        }
                        else {
                            Log.d("Edit Profile", "DataSnapshot: " + username);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Edit Profile","Error: " + error.getMessage());
                    }
                });
            }
        }

    }

}



