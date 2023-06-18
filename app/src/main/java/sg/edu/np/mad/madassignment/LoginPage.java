package sg.edu.np.mad.madassignment;

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

public class LoginPage extends AppCompatActivity {

    EditText usernameEdit, passwordEdit;
    Button loginbutton;

    TextView registertext;
    CheckBox remember_me_checkbox;

    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
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
                                Intent intent = new Intent(LoginPage.this, MainPage.class);
                                intent.putExtra("USERNAME", username);
                                Log.v("Login Username","+"+username);
                                intent.putExtra("PASSWORD",password);
                                intent.putExtra("DISPLAYNAME", displayName);

                                startActivity(intent);
                            } else {
                                Log.v("LoginPage", "Invalid username or password");
                                Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("LoginPage", "User not found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("LoginPage", "Error: " + databaseError.getMessage());
                    }
                });
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
}