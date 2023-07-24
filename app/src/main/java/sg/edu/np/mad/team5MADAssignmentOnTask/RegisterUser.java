package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterUser extends AppCompatActivity {

    EditText usernameEdit, passwordEdit, confirmPasswordEdit;
    Button registerButton;
    TextView cancelButton, back;
    DatabaseReference userRef;
    DatabaseReference taskcountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Load the databases
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        taskcountRef= FirebaseDatabase.getInstance().getReference("TaskCount");

        // Get layout file stuff
        usernameEdit = findViewById(R.id.usernameedit);
        passwordEdit = findViewById(R.id.passwordedit);
        confirmPasswordEdit = findViewById(R.id.confirmpasswordedit);
        registerButton = findViewById(R.id.registerbutton);
        cancelButton = findViewById(R.id.canceltext);
        back = findViewById(R.id.back2);

        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
        confirmPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                String confirmPassword = confirmPasswordEdit.getText().toString();
                Resources resources = getResources();
                int imageResId = R.drawable.dog;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                Log.v("Register","+"+imageUri.toString());
                String IMAGEURI = imageUri.toString();
                if (username != null && !username.equals("") && password != null && !password.equals("") && confirmPassword != null && !confirmPassword.equals("")){

                     if (username.length() > 12) {
                        usernameEdit.setError("Username can only Accept 12 Characters");
                        usernameEdit.requestFocus();
                    } else if (username.contains(" ")) {

                         usernameEdit.setError("Spaces are not allowed");
                         usernameEdit.requestFocus();

                     }else{
                         if (password.equals(confirmPassword)) {
                             userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     if (dataSnapshot.exists()) {
                                         Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                                     } else {
                                         // Create a new user
                                         User newUser = new User(username, password,username,IMAGEURI,0, 0);
                                         userRef.child(username).setValue(newUser);
                                         Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                         finish();
                                     }
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {
                                     Log.v("RegisterPage", "Error: " + databaseError.getMessage());
                                 }
                             });
                         } else {
                             Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                         }
                     }
                     }


                 else if (username == null || username.equals("")) {
                    usernameEdit.setError("Username is Empty");
                    usernameEdit.requestFocus();
                    if (password == null || password.equals("")) {
                        passwordEdit.setError("Password is Empty");
                        passwordEdit.requestFocus();
                        if (confirmPassword == null || confirmPassword.equals("")) {
                            confirmPasswordEdit.setError("Password is Empty");
                            confirmPasswordEdit.requestFocus();
                        }
                    }
                }

                else if (password == null || password.equals("")){
                    passwordEdit.setError("Password is Empty");
                    passwordEdit.requestFocus();
                    if (confirmPassword == null || confirmPassword.equals("")) {
                        confirmPasswordEdit.setError("Confirm Password is Empty");
                        confirmPasswordEdit.requestFocus();

                    }
                }

                else{
                    confirmPasswordEdit.setError("Confirm Password is Empty");
                    confirmPasswordEdit.requestFocus();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

