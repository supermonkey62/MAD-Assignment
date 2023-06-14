package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    Button pomodorotimer;
    Button normaltimer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        pomodorotimer = findViewById(R.id.pomobutton);
        normaltimer = findViewById(R.id.NormalTimerBttn);

        pomodorotimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " Entering Pomodoro Timer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, PomodoroTimer.class);
                startActivity(intent);
            }
        });

        normaltimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this,"Entering Timer",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainPage.this, timer.class);
                startActivity(intent2);
            }
        });
    }
}