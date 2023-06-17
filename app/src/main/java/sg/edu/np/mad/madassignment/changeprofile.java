package sg.edu.np.mad.madassignment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changeprofile extends AppCompatActivity {
    Button confirmusername,setimage,chooseimage;
    EditText changeusername;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeprofile);
        changeusername = findViewById(R.id.changeusername);
        String username = getIntent().getStringExtra("USERNAME");
        String Password = getIntent().getStringExtra("Password");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        confirmusername = findViewById(R.id.confirm_button);

        confirmusername.setEnabled(false);

        String changedusername = changeusername.getText().toString();

        if (changedusername != null){
            confirmusername.setEnabled(true);
            confirmusername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User newuser = new User(username, Password, changedusername);
                    userRef.setValue(newuser);
                    Toast.makeText(getApplicationContext(), " Display is Successfully Changed ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}










