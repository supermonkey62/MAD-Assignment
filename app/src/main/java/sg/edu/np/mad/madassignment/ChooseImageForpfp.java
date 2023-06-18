package sg.edu.np.mad.madassignment;

import android.content.ContentResolver;
import android.content.res.Resources;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseImageForpfp extends AppCompatActivity {

    TextView cancel;

    String ImageURI;
    Button confirm;
    ImageView Image1, Image2, Image3, Image4, Image5;

    DatabaseReference userRef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available);


        cancel = findViewById(R.id.choosepfpback);
        confirm = findViewById(R.id.choosepfpconfirm);
        Image1 = findViewById(R.id.smoke);
        Image2 = findViewById(R.id.tiger);
        Image3 = findViewById(R.id.dog);
        Image4 = findViewById(R.id.mountainsnow);
        Image5 = findViewById(R.id.holiday);


        String password = getIntent().getStringExtra("PASSWORD");
        String username = getIntent().getStringExtra("USERNAME");
        Log.v("Username","+" + username);

        userRef = FirebaseDatabase.getInstance().getReference("Users");





        float scaleFactor = 1.2f;

        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resources resources = getResources();
                int imageResId = R.drawable.smoke;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                String IMAGEURI = imageUri.toString();

                confirm.setEnabled(true);

                ImageURI = imageUri.toString();


                // Apply scale animation
                v.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(200).start();

                // Reset scale after a short delay (optional)
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    }
                }, 500); // Adjust the delay time as desired
            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resources resources = getResources();
                int imageResId = R.drawable.tiger;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                ImageURI = imageUri.toString();

                confirm.setEnabled(true);




                // Apply scale animation
                v.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(200).start();

                // Reset scale after a short delay (optional)
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    }
                }, 500); // Adjust the delay time as desired
            }
        });

        Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resources resources = getResources();
                int imageResId = R.drawable.dog;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                ImageURI = imageUri.toString();

                confirm.setEnabled(true);




                // Apply scale animation
                v.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(200).start();

                // Reset scale after a short delay (optional)
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    }
                }, 500); // Adjust the delay time as desired
            }
        });

        Image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resources resources = getResources();
                int imageResId = R.drawable.mountainsnow;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                ImageURI = imageUri.toString();

                confirm.setEnabled(true);




                // Apply scale animation
                v.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(200).start();

                // Reset scale after a short delay (optional)
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    }
                }, 500); // Adjust the delay time as desired
            }
        });

        Image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Resources resources = getResources();
                int imageResId = R.drawable.holiday;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                ImageURI = imageUri.toString();

                confirm.setEnabled(true);



                // Apply scale animation
                v.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(200).start();

                // Reset scale after a short delay (optional)
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
                    }
                }, 500); // Adjust the delay time as desired
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Confirm","+" + username);




                userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("imageURI").setValue(ImageURI);
                            Log.v("URI","+" + dataSnapshot.child("imageURI").getValue());
                            Toast.makeText(getApplicationContext(), "Profile Picture Successfully Changed", Toast.LENGTH_SHORT).show();


                        }
                        else{
                            Log.d("ChooseImageForpfp", "DataSnapshot: " + username);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
                    }

                });
            }




        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}