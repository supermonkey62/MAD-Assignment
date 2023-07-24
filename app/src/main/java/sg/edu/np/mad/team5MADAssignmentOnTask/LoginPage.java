package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.Date;

public class LoginPage extends AppCompatActivity {

    EditText usernameEdit, passwordEdit;
    Button loginbutton;

    TextView registertext;
    CheckBox remember_me_checkbox;
    DatabaseReference userRef;


    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    boolean remember;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Load the database
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        // Get layout file stuff
        usernameEdit = findViewById(R.id.usernameedit);
        passwordEdit = findViewById(R.id.passwordedit);
        loginbutton = findViewById(R.id.loginbutton);
        registertext = findViewById(R.id.registertext);
        remember_me_checkbox = findViewById(R.id.checkBox);

        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());

        loginPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        remember = loginPreferences.getBoolean(PREF_REMEMBER, false);
        if (remember) {
            usernameEdit.setText(loginPreferences.getString(PREF_USERNAME, ""));
            passwordEdit.setText(loginPreferences.getString(PREF_PASSWORD, ""));
            remember_me_checkbox.setChecked(true);
        }


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();

                if (username != null && !username.equals("") && password != null && !password.equals("")){

                    userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String savedUsername = dataSnapshot.child("username").getValue(String.class);
                                String savedPassword = dataSnapshot.child("password").getValue(String.class);
                                String displayName = dataSnapshot.child("displayname").getValue(String.class);



                                if (savedUsername.equals(username) && savedPassword.equals(password)) {
                                    Log.v("LoginPage", "Login successful");

                                    if (remember_me_checkbox.isChecked()) {
                                        loginPrefsEditor.putBoolean(PREF_REMEMBER, true);
                                        loginPrefsEditor.putString(PREF_USERNAME, username);
                                        loginPrefsEditor.putString(PREF_PASSWORD, password);
                                    } else {
                                        loginPrefsEditor.clear();
                                    }
                                    loginPrefsEditor.apply();

                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginPage.this, Stage2MainPage.class);
                                    intent.putExtra("USERNAME", username);
                                    Log.v("Login Username", username);
                                    intent.putExtra("PASSWORD",password);
                                    intent.putExtra("DISPLAYNAME", displayName);
                                    UpdateLastLogin(username);

                                    startActivity(intent);
                                } else {
                                    Log.v("LoginPage", "Invalid username or password");
                                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.v("LoginPage", "User not found");
                                usernameEdit.setError("Username Do not Exist");
                                usernameEdit.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.v("LoginPage", "Error: " + databaseError.getMessage());
                        }
                    });

                } else if (username == null || username.equals("")) {
                    usernameEdit.setError("Username is Empty");
                    usernameEdit.requestFocus();
                    if (password == null || password.equals("")){
                        passwordEdit.setError("Password is Empty");
                        passwordEdit.requestFocus();
                    }

                } else{
                    passwordEdit.setError("Password is Empty");
                    passwordEdit.requestFocus();
                }


            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterUser.class);
                startActivity(intent);
            }
        });
    }


    private void UpdateLastLogin(String username){
        DatabaseReference LoginDateRef;
        LoginDateRef = FirebaseDatabase.getInstance().getReference("UserDate").child(username);
        LoginDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {

                    Date date = new Date();
                    Date Pre = dataSnapshot.child("logindate").getValue(Date.class);
                    long oneDayInMillis = 24 * 60 * 60 * 1000;
                    Date dateAfter = new Date(Pre.getTime() + oneDayInMillis);
                    if (date.after(dateAfter) || date.equals(dateAfter)) {
                        LoginDate date1 = new LoginDate(date);
                        LoginDateRef.setValue(date1);
                        UpdateCount(username);
                    }

                } else {
                    Log.v("useless","+");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while fetching the data
                // ...
            }
        });

    }

    private void UpdateCount(String username){
        DatabaseReference CountRef;
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {

                    int first = dataSnapshot.child("logincount").getValue(Integer.class);
                    Log.v("CountRef", "+" + first);
                    CountRef.child("logincount").setValue(first + 1);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur while fetching the data
                // ...
            }
        });


    }
}