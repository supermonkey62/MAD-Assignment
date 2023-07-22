package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ProfileFragment extends Fragment implements UserDataHolder.UserDataCallback {

    private TextView  profileusername;
    private ImageView editProfile, pfp;
    private String displayname;

    private String ImageURI;
    private String username;

    private String password;
    private String TITLE = "Profile Page";

    private ArrayList<String> goals;
    private ArrayAdapter<String> goalsAdapter;
    private ListView listView;
    private Button addGoalsButton;

    private DatabaseReference userRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = getActivity().getIntent().getStringExtra("USERNAME");
        password = getActivity().getIntent().getStringExtra("PASSWORD");
        displayname = getActivity().getIntent().getStringExtra("DISPLAYNAME");

        updateUserAchievements(username);

        profileusername = view.findViewById(R.id.profileusername);
        editProfile = view.findViewById(R.id.editUsername);
        pfp = view.findViewById(R.id.image_view);
        View layout = view.findViewById(R.id.layout);

        listView = view.findViewById(R.id.goal_list);
        addGoalsButton = view.findViewById(R.id.goal_button);
        ImageView whitebox = view.findViewById(R.id.whitebox);
        TextView goal = view.findViewById(R.id.goal);

        ;


        Context context = container.getContext();
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            whitebox.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            whitebox.setColorFilter(ContextCompat.getColor(context, R.color.grey), PorterDuff.Mode.SRC_IN);
            goal.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            ImageView more = view.findViewById(R.id.editUsername);
            more.setImageResource(R.drawable.white_more);

        }



        // Display user's previously added goals from database
        DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("goals");
        userGoalsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goals.clear();

                for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                    String goal = goalSnapshot.getValue(String.class);
                    goals.add(goal);
                }

                goalsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ProfilePage", "Error: " + databaseError.getMessage());
            }
        });

        addGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addgoal(view);
            }
        });

        goals = new ArrayList<>();
        goalsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, goals);
        listView.setAdapter(goalsAdapter);
        listViewListener();

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue().toString();
                    Uri uri = Uri.parse(ImageURI);
                    pfp.setImageURI(uri);
                } else {
                    Log.v("ChangeDisplayName", "User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ChangePassword", "Error: " + databaseError.getMessage());
            }
        });

        UserDataHolder.getInstance().fetchUserData(username, this);
        profileusername.setText(displayname);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,username,context);
            }
        });




        return view;
    }

    private void addgoal(View view) {
        EditText user_input = getView().findViewById(R.id.goal_input);
        String input_text = user_input.getText().toString();

        if (!input_text.equals("")) {
            goalsAdapter.add(input_text);
            user_input.setText("");

            // Saving goal to database
            DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("goals");
            String goalId = userGoalsRef.push().getKey();
            userGoalsRef.child(goalId).setValue(input_text);
        } else {
            Toast.makeText(getContext(), "Empty input. Please enter a goal.", Toast.LENGTH_LONG).show();
        }
    }

    private void listViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity().getApplicationContext();
                Toast.makeText(context, "Goal Removed", Toast.LENGTH_LONG).show();

                String deleteGoal = goals.get(i);

                goals.remove(i);
                goalsAdapter.notifyDataSetChanged();

                DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("Users").child(username).child("goals");
                userGoalsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot goalSnapshot : dataSnapshot.getChildren()) {
                            String goal = goalSnapshot.getValue(String.class);
                            if (goal.equals(deleteGoal)) {
                                goalSnapshot.getRef().removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("ProfilePage", "Error: " + databaseError.getMessage());
                    }
                });
                return true;
            }
        });
    }

    @Override
    public void onUserDataFetched(String displayname) {
        this.displayname = displayname;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TITLE, "On Pause!");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fetch user data again when the fragment resumes
        UserDataHolder.getInstance().fetchUserData(username, this);
        profileusername.setText(displayname);
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue().toString();
                    Uri uri = Uri.parse(ImageURI);
                    pfp.setImageURI(uri);
                } else {
                    Log.v("ChangeDisplayName", "User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("ChangePassword", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateUserAchievements(final String username) {

        DatabaseReference usercountRef= FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        usercountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer loginCount = dataSnapshot.child("logincount").getValue(Integer.class);
                Integer taskcount = dataSnapshot.child("taskcount").getValue(Integer.class);
                Integer completedtaskcount = dataSnapshot.child("completedtaskcount").getValue(Integer.class);
                Integer coincount = dataSnapshot.child("coincount").getValue(Integer.class);

                if (loginCount != null) {
                    updateAchievementsWithLoginCount(username, loginCount,taskcount,completedtaskcount,coincount);
                } else {
                    Log.e("Error", "Failed to retrieve login count for the user.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e("Error", "Failed to read login count for the user.", databaseError.toException());
            }
        });
    }

    private void updateAchievementsWithLoginCount(String username, int loginCount,int taskcount,int completedtaskcount,int coincount) {
        DatabaseReference userAchievementRef = FirebaseDatabase.getInstance().getReference("UserAchievement").child(username);

        userAchievementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int claimable = 0;
                for (DataSnapshot achievementSnapshot : dataSnapshot.getChildren()) {
                    String achievementKey = achievementSnapshot.getKey();
                    String title = achievementSnapshot.child("title").getValue(String.class);
                    int progress = achievementSnapshot.child("progress").getValue(Integer.class);
                    int target = achievementSnapshot.child("target").getValue(Integer.class);

                    Log.v("Title", "12" + title);
                    DatabaseReference amountRef = achievementSnapshot.child("progress").getRef();

                    // Check if the title contains "Log In"
                    if (title != null && title.contains("Login")) {
                        // Update the "Amount" value with the loginCount
                        amountRef.setValue(loginCount);

                        if (progress >= target) {
                            claimable += 1;
                        }

                    } else if (title != null && title.contains("Create")) {
                        amountRef.setValue(taskcount);
                        Log.d("Amount:", String.valueOf(taskcount));

                        if (progress >= target) {
                            claimable += 1;
                        }

                    } else if (title != null && title.contains("Complete")) {
                        amountRef.setValue(taskcount);
                        Log.d("Amount:", String.valueOf(taskcount));

                        if (progress >= target) {
                            claimable += 1;
                        }

                    }
                }Log.v("Claimable", String.valueOf(claimable));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.e("Error", "Failed to read achievements for user.", databaseError.toException());
            }
        });
    }

    public void showPopup(View view,String username,Context context) {
        // Inflate the layout for the popup
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup, null);


        // Initialize and set up the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // Find the LinearLayout containers, ImageViews, and TextViews for each icon in the PopupWindow layout
        LinearLayout icon1Layout = popupView.findViewById(R.id.icon1_layout);
        LinearLayout icon2Layout = popupView.findViewById(R.id.icon2_layout);
        LinearLayout icon3Layout = popupView.findViewById(R.id.icon3_layout);
        LinearLayout icon4Layout = popupView.findViewById(R.id.icon4_layout);
        LinearLayout layout = popupView.findViewById(R.id.layout);

        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));

            TextView Achievement = popupView.findViewById(R.id.icon1_text);
            Achievement.setTextColor(Color.WHITE);
            ImageView ImageAchievement = popupView.findViewById(R.id.icon1_image);
            ImageAchievement.setImageResource(R.drawable.white_star);

            TextView Shop = popupView.findViewById(R.id.icon2_text);
            Shop.setTextColor(Color.WHITE);
            ImageView ImageShop = popupView.findViewById(R.id.icon2_image);
            ImageShop.setImageResource(R.drawable.white_shopping);

            TextView Edit = popupView.findViewById(R.id.icon3_text);
            Edit.setTextColor(Color.WHITE);
            ImageView ImageEdit = popupView.findViewById(R.id.icon3_image);
            ImageEdit.setImageResource(R.drawable.white_profile);

            TextView Setting = popupView.findViewById(R.id.icon4_text);
            Setting.setTextColor(Color.WHITE);
            ImageView ImageSettings = popupView.findViewById(R.id.icon4_image);
            ImageSettings.setImageResource(R.drawable.white_settings);



        }

        // Set click listeners for each icon layout to handle the button clicks
        icon1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AchievementPage.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        icon2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShopActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        icon3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        icon4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile_Setting.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        // Show the popup below the editUsername ImageView with a vertical offset of 100 pixels
        popupWindow.showAsDropDown(view, 0, 20);
    }
}




