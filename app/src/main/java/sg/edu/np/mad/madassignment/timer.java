package sg.edu.np.mad.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//public class timer extends AppCompatActivity  {
//    public int counter;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_timer);
//        Button button = findViewById(R.id.timerbttn);
//        TextView textView = findViewById(R.id.textView);
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                new CountDownTimer(30000, 1000){
//                    public void onTick(long millisUntilFinished){
//                        textView.setText(String.valueOf(counter));
//                        counter--;
//                    }
//                    public  void onFinish(){
//                        textView.setText("FINISH!!");
//                    }
//                }.start();
//            }
//        });
//    }
//}
public class timer extends AppCompatActivity {
    private long timeLeftInMillis;
    private int counter;
    private boolean isTimerRunning = false;
    private CountDownTimer countDownTimer;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        button = findViewById(R.id.timerbttn);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });
    }

    private void startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            button.setText("Stop Timer");

            if (timeLeftInMillis == 0) {

                timeLeftInMillis = 30000;
            }

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    counter = (int) (millisUntilFinished / 1000);
                    textView.setText(String.valueOf(counter));


                    int halfwayPoint = (int) (timeLeftInMillis / 1000);

                    // Display Toast message when halfway done
                    if (counter == halfwayPoint/2) {
                        Toast.makeText(timer.this, "Halfway done!", Toast.LENGTH_SHORT).show();
                    }

                    // Display Toast message when 5 seconds remaining
                    if (counter == 5) {
                        Toast.makeText(timer.this, "5 seconds remaining!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFinish() {
                    textView.setText("done");
                    isTimerRunning = false;
                    button.setText("Start Timer");
                    timeLeftInMillis = 0;
                }
            }.start();
        }
    }

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            button.setText("Start Timer");
            countDownTimer.cancel();
        }
    }
}


