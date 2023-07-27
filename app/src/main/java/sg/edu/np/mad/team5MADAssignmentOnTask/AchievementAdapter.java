package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    private Context context;
    private List<Achievement> achievementList;
    private String username;

    private OnClaimButtonClickListener claimButtonClickListener;

    public void setOnClaimButtonClickListener(OnClaimButtonClickListener listener) {
        this.claimButtonClickListener = listener;
    }


    public interface OnClaimButtonClickListener {
        void onClaimButtonClick(Achievement achievement);
    }



    public AchievementAdapter(Context context, List<Achievement> achievementList, String username) {
        this.context = context;
        this.achievementList = achievementList;
        this.username = username;// Store the intent data in the adapter

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_achievements, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = achievementList.get(position);

        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            holder.progressText.setTextColor(Color.BLACK);
        }



        if (achievement.getStatus().equals("Incomplete")) { // Only show achievements with status "Incomplete"
            holder.achievementTitle.setText(achievement.getTitle());
            holder.progressBar.setMax(achievement.getMaxProgress());
            holder.progressBar.setProgress(achievement.getProgress());
            holder.progressText.setText(String.format("Progress: %d/%d", achievement.getProgress(), achievement.getMaxProgress()));
            holder.progressBar.setEnabled(false);
            if (achievement.getRewardtype().equals("Coins")) {
                holder.reward.setText("Coins: " + achievement.getReward());
            } else if (achievement.getRewardtype().equals("Title")) {
                String name = "'" + achievement.getReward() + "'";
                holder.reward.setText(name);
            }

            // Set click listener for claimButton here
            if (achievement.getProgress() >= achievement.getMaxProgress()) {
                holder.claimButton.setText("Claim");
                holder.claimButton.setEnabled(true);
                holder.claimButton.setBackgroundColor(ContextCompat.getColor(context,R.color.app_theme));



                holder.claimButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (claimButtonClickListener != null) {
                            claimButtonClickListener.onClaimButtonClick(achievement);
                        }
                    }
                });
            } else {
                holder.claimButton.setText("Incomplete");
                holder.claimButton.setEnabled(false);
                holder.claimButton.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            }
        }
    }


    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView achievementTitle,reward;
        ProgressBar progressBar;
        TextView progressText;
        Button claimButton;

        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementTitle = itemView.findViewById(R.id.achievement_title);
            progressBar = itemView.findViewById(R.id.progress_bar);
            progressText = itemView.findViewById(R.id.progress_text);
            claimButton = itemView.findViewById(R.id.claim_button);
            reward = itemView.findViewById(R.id.reward);
            constraintLayout = itemView.findViewById(R.id.constraint);
        }
    }
    private void getAchievementKeyFromFirebase(Achievement claimedAchievement, AchievementKeyCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserAchievement").child(username);

        // Query the database to find the achievement with the matching title and progress
        databaseReference.orderByChild("title").equalTo(claimedAchievement.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Achievement achievement = snapshot.getValue(Achievement.class);
                    if (achievement != null && achievement.getTitle() == claimedAchievement.getTitle()) {
                        // Found the matching achievement, return its key through the callback
                        String achievementKey = snapshot.getKey();
                        callback.onAchievementKeyObtained(achievementKey);
                        return; // Important: stop the loop after finding the matching achievement
                    }
                }
                // If the loop completes without finding the achievement, return null
                callback.onAchievementKeyObtained(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error here, if necessary
                callback.onAchievementKeyObtained(null);
            }
        });
    }

    public interface AchievementKeyCallback {
        void onAchievementKeyObtained(String achievementKey);
    }

    void claimAchievement(Achievement achievement) {
        // Get the AchievementKey from the Firebase database
        getAchievementKeyFromFirebase(achievement, new AchievementKeyCallback() {
            @Override
            public void onAchievementKeyObtained(String achievementKey) {
                if (achievementKey != null) {
                    // Update the status of the achievement to "Claimed" in the UserAchievement node
                    DatabaseReference achievementRef = FirebaseDatabase.getInstance().getReference().child("UserAchievement").child(username).child(achievementKey);
                    achievementRef.child("status").setValue("Completed");

                    Log.v("Claimed",achievement.getRewardtype());
                    if (achievement.getRewardtype().equals("Coins")){
                        int coin = Integer.parseInt(achievement.getReward());
                        Log.v("Coins","+" + coin);
                        UpdateCount(username,coin);
                    }


                    if (context instanceof Activity) {
                        ((Activity) context).recreate();
                    }

                } else {
                    // Handle the case when the achievement key is not found (optional)
                }
            }
        });
    }


    private void UpdateCount(String username,int Reward){
        DatabaseReference CountRef;
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {

                    int first = dataSnapshot.child("coincount").getValue(Integer.class);
                    CountRef.child("coincount").setValue(first + Reward);

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
