package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AchievementPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AchievementAdapter adapter;
    private List<Achievement> achievementList;
    private DatabaseReference userAchievementRef;
    private DatabaseReference CountRef;// Change this to your actual Firebase reference

    int done,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_page);

        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("PASSWORD");
        String displayname = getIntent().getStringExtra("DISPLAYNAME");


        ImageView cancel = findViewById(R.id.close_button);
        TextView achievementCount = findViewById(R.id.coins);
        RelativeLayout layout = findViewById(R.id.layout);


        achievementCount.setText(String.format("%d/%d", done, total));
        Context context = this;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            cancel.setImageResource(R.drawable.white_cross);
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Call finish() when the cancel button is clicked
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        achievementList = new ArrayList<>();

            // Create the adapter and pass the intent data and username
        adapter = new AchievementAdapter(this, achievementList, username);

        adapter.setOnClaimButtonClickListener(new AchievementAdapter.OnClaimButtonClickListener() {
            @Override
            public void onClaimButtonClick(Achievement achievement) {


                showClaimConfirmationDialog(achievement);
            }
        });

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        // Get a reference to the UserAchievement node in Firebase
        userAchievementRef = FirebaseDatabase.getInstance().getReference("UserAchievement").child(username);
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);
        done = 0;
        total = 0;
        // Attach a ValueEventListener to retrieve the achievement data
        userAchievementRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                achievementList.clear(); // Clear the existing list

                // Iterate through the usernames
                for (DataSnapshot achievementSnapshot : dataSnapshot.getChildren()) {
                    String achievementKey = achievementSnapshot.getKey();
                    String title = achievementSnapshot.child("title").getValue(String.class);
                    int progress = achievementSnapshot.child("progress").getValue(Integer.class);
                    int maxProgress = achievementSnapshot.child("target").getValue(Integer.class);
                    String reward = achievementSnapshot.child("reward").getValue(String.class);
                    String status = achievementSnapshot.child("status").getValue(String.class);
                    String rewardtype = achievementSnapshot.child("rewardtype").getValue(String.class);

                    total +=1;
                    if (status.equals("Completed")){
                        done+=1;
                    }
                    achievementCount.setText(String.format("%d/%d", done, total));
                    CountRef.child("achievementcomplete").setValue(done);


                    // Create an Achievement object and add it to the list
                    Achievement achievement = new Achievement(title, progress, maxProgress, reward, rewardtype, status);
                    achievementList.add(achievement);
                }

                Collections.sort(achievementList, new Comparator<Achievement>() {
                    @Override
                    public int compare(Achievement a1, Achievement a2) {
                        boolean isClaimed1 = a1.getProgress() >= a1.getMaxProgress();
                        boolean isClaimed2 = a2.getProgress() >= a2.getMaxProgress();
                        if (isClaimed1 != isClaimed2) {
                            return Boolean.compare(isClaimed2, isClaimed1);
                        } else {
                            // If statuses are the same, sort by maxProgress in ascending order
                            return Integer.compare(a1.getMaxProgress(), a2.getMaxProgress());
                        }
                    }
                });



                adapter.notifyDataSetChanged(); // Notify the adapter of data changes
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if any
            }
        });
    }
    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = verticalSpaceHeight;
            // If you want to add spacing at the bottom as well, you can set outRect.bottom = verticalSpaceHeight;
        }
    }

    private void showClaimConfirmationDialog(Achievement achievement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Claim Achievement");
        builder.setMessage("Are you sure you want to claim this achievement?");
        builder.setPositiveButton("Claim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the method to claim the achievement in the adapter
                claimAchievement(achievement);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to claim the achievement in the adapter
    private void claimAchievement(Achievement achievement) {
        // Call the claimAchievement method in the adapter
        adapter.claimAchievement(achievement);
    }






}


