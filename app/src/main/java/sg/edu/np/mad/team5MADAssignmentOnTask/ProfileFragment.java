package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements UserDataHolder.UserDataCallback   {

    private TextView  profileusername,title;
    private ImageView editProfile, banner;
    private CircleImageView pfp;
    private String displayname;
    private String ImageURI, BannerURI;
    private String username;
    private String TITLE = "Profile Page";
    private ArrayList<String> goals;
    private ArrayAdapter<String> goalsAdapter;
    private ListView listView;
    private Button addGoalsButton;
    private DatabaseReference userRef;
    private int num;
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
        displayname = getActivity().getIntent().getStringExtra("DISPLAYNAME");
        updateUserAchievements(username);
        BannerURI = "";

        profileusername = view.findViewById(R.id.profileusername);
        editProfile = view.findViewById(R.id.editUsername);
        pfp = view.findViewById(R.id.image_view);
        banner = view.findViewById(R.id.banner);
        View layout = view.findViewById(R.id.layout);
        listView = view.findViewById(R.id.goal_list);
        addGoalsButton = view.findViewById(R.id.goal_button);
        title = view.findViewById(R.id.title);
        ImageView whitebox = view.findViewById(R.id.whitebox);
        TextView goal = view.findViewById(R.id.goal);
        TextView goalinput = view.findViewById(R.id.goal_input);

        listviewsetbackground(username,listView,title);

        Context context = container.getContext();
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            whitebox.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            whitebox.setColorFilter(ContextCompat.getColor(context, R.color.grey), PorterDuff.Mode.SRC_IN);
            goal.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            ImageView more = view.findViewById(R.id.editUsername);
            more.setImageResource(R.drawable.white_more);
            goalinput.setTextColor(ContextCompat.getColor(context,R.color.white));
            profileusername.setTextColor(ContextCompat.getColor(context,R.color.white));
        }

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference userGoalsRef = FirebaseDatabase.getInstance().getReference("UserAchievement").child(username);
                userGoalsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> titlelist = new ArrayList<>();
                        titlelist.add("The Starter");
                        for (DataSnapshot titleSnapshot : dataSnapshot.getChildren()) {
                            String key = titleSnapshot.getKey();
                            if (titleSnapshot.child("status").getValue(String.class).equals("Completed") && titleSnapshot.child("rewardtype").getValue(String.class).equals("Title")){

                                titlelist.add(titleSnapshot.child("reward").getValue(String.class));
                            }
                        }showListPopup(titlelist,title);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("ProfilePage", "Error: " + databaseError.getMessage());
                    }
                });

            }
        });


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
        goalsAdapter = new CustomArrayAdapter(getContext(), goals);
        listView.setAdapter(goalsAdapter);
        listViewListener();

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageURI = dataSnapshot.child("imageURI").getValue().toString();
                    if(dataSnapshot.child("bannerURI").exists()){
                        BannerURI = dataSnapshot.child("bannerURI").getValue().toString();
                        if(BannerURI.equals("")){
                            banner.setImageResource(R.drawable.gray_square);
                        }
                        else{
                            Uri uriBAN = Uri.parse(BannerURI);
                            banner.setImageURI(uriBAN);
                        }
                    }
                    else{
                        banner.setImageResource(R.drawable.gray_square);
                    }
                    Uri uriIMG = Uri.parse(ImageURI);
                    pfp.setImageURI(uriIMG);
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
                showPopup(v,username,context,num);
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
                num = 0;
                for (DataSnapshot achievementSnapshot : dataSnapshot.getChildren()) {
                    String achievementKey = achievementSnapshot.getKey();
                    String title = achievementSnapshot.child("title").getValue(String.class);
                    int progress = achievementSnapshot.child("progress").getValue(Integer.class);
                    int target = achievementSnapshot.child("target").getValue(Integer.class);
                    String status = achievementSnapshot.child("status").getValue(String.class);
                    DatabaseReference amountRef = achievementSnapshot.child("progress").getRef();
                    if (progress >= target && status.equals("Incomplete")) {
                        num += 1;
                    }

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
                        amountRef.setValue(completedtaskcount);
                        Log.d("Amount:", String.valueOf(completedtaskcount));

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

    public void showPopup(View view,String username,Context context,int num) {
        // Inflate the layout for the popup
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup, null);


        // Initialize and set up the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // Find the LinearLayout containers, ImageViews, and TextViews for each icon in the PopupWindow layout
        LinearLayout icon1Layout = popupView.findViewById(R.id.icon1_layout);
        LinearLayout icon2Layout = popupView.findViewById(R.id.icon2_layout);
        LinearLayout icon3Layout = popupView.findViewById(R.id.icon3_layout);
        LinearLayout icon4Layout = popupView.findViewById(R.id.icon4_layout);
        LinearLayout icon5Layout = popupView.findViewById(R.id.icon5_layout);
        LinearLayout layout = popupView.findViewById(R.id.layout);
        TextView numtext = popupView.findViewById(R.id.number);

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

            TextView Style   = popupView.findViewById(R.id.icon5_text);
            Style.setTextColor(Color.WHITE);
            ImageView ImageStyle = popupView.findViewById(R.id.icon5_image);
            ImageStyle.setImageResource(R.drawable.white_style);
        }

        if (num > 0){
            String number = String.valueOf(num);
            numtext.setText(number);
        } else{
            numtext.setVisibility(View.GONE);
        }

        // Set click listeners for each icon layout to handle the button clicks
        icon1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AchievementPage.class);
                intent.putExtra("USERNAME", username);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });

        icon2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShopActivity.class);
                intent.putExtra("USERNAME", username);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });

        icon3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtra("USERNAME", username);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });

        icon4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile_Setting.class);
                intent.putExtra("USERNAME", username);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });

        icon5Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Choose_Item_Activity.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("Type","background");
                popupWindow.dismiss();
                startActivity(intent);
            }
        });

        // Show the popup below the editUsername ImageView with a vertical offset of 100 pixels
        popupWindow.showAsDropDown(view, 0, 20);
    }

    public  void listviewsetbackground(String username, ListView listView,TextView textView){
        DatabaseReference userequip = FirebaseDatabase.getInstance().getReference("UserEquip");
        userequip.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String background = dataSnapshot.child("background").getValue(String.class);
                String title = dataSnapshot.child("title").getValue(String.class);
                String outline = dataSnapshot.child("outline").getValue(String.class);

                if (background == null || background.equals("")) {

                    listView.setBackground(null);

                } else {
                    int lastIndex = background.lastIndexOf('/');
                    String lastSegment = background.substring(lastIndex + 1);
                    int drawableResId = getResources().getIdentifier(lastSegment, "drawable", getContext().getPackageName());
                    listView.setBackgroundResource(drawableResId);
                }

                if(title == null){
                    textView.setText("The Starter");
                    Log.v("userequip","=" + title);
                    FirebaseDatabase.getInstance().getReference("UserEquip").child(username).child("title").setValue("The Starter");
                }

                else{
                    Log.v("userequip","=" + title);
                    textView.setText(title);
                }

                if(outline == null || outline.equals("")){
                    textView.setBackgroundResource(R.drawable.rectangle_line);
                }

                else{
                    int lastIndex = outline.lastIndexOf('/');
                    String lastSegment = outline.substring(lastIndex + 1);
                    int drawableResId = getResources().getIdentifier(lastSegment, "drawable", getContext().getPackageName());
                    textView.setBackgroundResource(drawableResId);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void showListPopup(List<String> values,TextView title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Title List");

        // Convert the list to a CharSequence array to display in the dialog
        final CharSequence[] charSequenceArray = values.toArray(new CharSequence[values.size()]);

        builder.setItems(charSequenceArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle item selection if needed, e.g., perform an action when an item is clicked
                String selectedValue = values.get(which);
                title.setText(selectedValue);
                Log.v("select","=" + selectedValue);
                FirebaseDatabase.getInstance().getReference("UserEquip").child(username).child("title").setValue(selectedValue);
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        listviewsetbackground(username,listView,title);
        updateUserAchievements(username);
    }

}




