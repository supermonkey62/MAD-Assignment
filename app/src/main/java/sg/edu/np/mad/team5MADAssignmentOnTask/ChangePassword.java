package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    EditText changepassword,confirmedpassword,currentpassword;
    TextView cancel,authenticationstatus, profilepageback;
    Button changepasswordbutton,authenticatebutton;
    DatabaseReference userRef;

    String originalpassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);

        changepassword = findViewById(R.id.passwordedit);
        confirmedpassword = findViewById(R.id.confirmpasswordedit);

        changepassword.setTransformationMethod(new PasswordTransformationMethod());
        confirmedpassword.setTransformationMethod(new PasswordTransformationMethod());


        changepasswordbutton = findViewById(R.id.changepasswordbutton);
        authenticatebutton = findViewById(R.id.Authenticate);

        currentpassword = findViewById(R.id.currentpassword);
        currentpassword.setTransformationMethod(new PasswordTransformationMethod());
        cancel = findViewById(R.id.canceltext);
        authenticationstatus = findViewById(R.id.status);

        String username = getIntent().getStringExtra("USERNAME");
        String Password = getIntent().getStringExtra("Password");

        changepassword.setEnabled(false);
        confirmedpassword.setEnabled(false);
        changepasswordbutton.setEnabled(false);
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        Log.v("Password User","+" + Password);

        profilepageback = findViewById(R.id.change_passwordback);

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    originalpassword = dataSnapshot.child("password").getValue().toString();
                    Log.v("Passowrd","+" + originalpassword);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ChangePassword", "Error: " + databaseError.getMessage());
            }
        });

        profilepageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();

                finish();
            }
        });


        authenticatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String current = currentpassword.getText().toString();
                Log.v("Username Password","+"+current);



                if (current.equals(originalpassword)){
                    changepassword.setEnabled(true);
                    confirmedpassword.setEnabled(true);
                    changepasswordbutton.setEnabled(true);
                    authenticatebutton.setEnabled(false);
                    currentpassword.setEnabled(false);
                    authenticationstatus.setText("Account has been Authenticated, You can now change your password");



                    changepasswordbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String newpassword = changepassword.getText().toString();
                            final String confirmnewpassword = confirmedpassword.getText().toString();
                            if(newpassword.equals(confirmnewpassword) ) {
                                if (newpassword != null && !newpassword.equals("")){
                                    userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                dataSnapshot.getRef().child("password").setValue(confirmnewpassword);
                                                Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Log.v("ChangeDisplayName", "User not found");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.v("ChangePassword", "Error: " + databaseError.getMessage());
                                        }
                                    });

                                }else{
                                    confirmedpassword.setError("Password cannot be Empty");
                                    confirmedpassword.requestFocus();
                                }

                            }else{
                                Toast.makeText(getApplicationContext(),"Password do not match",Toast.LENGTH_SHORT).show();
                                confirmedpassword.setError("Password do not match");
                                confirmedpassword.requestFocus();
                            }

                        }
                    });


                } else if (current == null || current.equals("") ) {

                    currentpassword.setError("Please Input a password");
                    currentpassword.requestFocus();

                } else{
                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
                    currentpassword.setError("Incorrect Password");
                    currentpassword.requestFocus();
                }


            }

            });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ChangePassword.this, Profile_Setting.class);
                intent3.putExtra("USERNAME", username);
                intent3.putExtra("Password", Password);
                startActivity(intent3);
            }


        });
    }





}
