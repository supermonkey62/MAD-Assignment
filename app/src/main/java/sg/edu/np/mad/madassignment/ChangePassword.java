package sg.edu.np.mad.madassignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {

    EditText changepassword,confirmedpassword,currentpassword;
    TextView cancel,authenticationstatus;
    Button changepasswordbutton,authenticatebutton;
    DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);
        changepassword = findViewById(R.id.passwordedit);
        confirmedpassword = findViewById(R.id.confirmpasswordedit);

        changepasswordbutton = findViewById(R.id.changepasswordbutton);
        authenticatebutton = findViewById(R.id.Authenticate);

        currentpassword = findViewById(R.id.currentpassword);
        cancel = findViewById(R.id.canceltext);
        authenticationstatus = findViewById(R.id.status);
        String username = getIntent().getStringExtra("USERNAME");
        String Password = getIntent().getStringExtra("Password");

        changepassword.setEnabled(false);
        confirmedpassword.setEnabled(false);
        changepasswordbutton.setEnabled(false);
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);




        authenticatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String current = currentpassword.getText().toString();

                if (Password.equals(current)){
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
                            if (newpassword.equals(confirmnewpassword)){
                                User newuser = new User(username,confirmnewpassword,username);
                                userRef.setValue(newuser);
                                Toast.makeText(getApplicationContext(), " Password is Successfully Changed ", Toast.LENGTH_SHORT).show();
                                finish();



                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Password do not match",Toast.LENGTH_SHORT).show();
                                confirmedpassword.setError("Password do not match");
                                confirmedpassword.requestFocus();


                            }
                        }
                    });


                }
                else{
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
