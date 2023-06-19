package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodolistFragmentholder extends AppCompatActivity {
    Switch todoswitch;
    FloatingActionButton backhome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_fragmentholder);

        todoswitch = findViewById(R.id.todotaskswitch);
        backhome = findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(TodolistFragmentholder.this,MainPage.class);
                Toast.makeText(TodolistFragmentholder.this,"Back to HomePage",Toast.LENGTH_SHORT).show();
                startActivity(home);
            }
        });
        todoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = isChecked ? new DoneTasksFragment() : new TodoFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.todofragmentContainerView, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                String switchName = isChecked ? "To-Do Tasks" : "Done Tasks";
                todoswitch.setText(switchName);
            }
        });


        boolean switchState = todoswitch.isChecked();
        Fragment initialFragment = switchState ? new DoneTasksFragment() : new TodoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todofragmentContainerView, initialFragment)
                .commit();
        String initialSwitchName = switchState ? "To-Do Tasks" : "Done Tasks";
        todoswitch.setText(initialSwitchName);
    }

}
