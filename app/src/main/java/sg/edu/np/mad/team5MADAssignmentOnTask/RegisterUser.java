package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    EditText usernameEdit, passwordEdit, confirmPasswordEdit;
    Button registerButton;
    TextView cancelButton, back;
    DatabaseReference userRef;
    DatabaseReference achievementRef;
    DatabaseReference shopRef;
    DatabaseReference usercountRef;
    DatabaseReference userDateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Load the databases
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        usercountRef = FirebaseDatabase.getInstance().getReference("UserCount");
        userDateRef = FirebaseDatabase.getInstance().getReference("UserDate");



        // Get layout file stuff
        usernameEdit = findViewById(R.id.usernameedit);
        passwordEdit = findViewById(R.id.passwordedit);
        confirmPasswordEdit = findViewById(R.id.confirmpasswordedit);
        registerButton = findViewById(R.id.registerbutton);
        cancelButton = findViewById(R.id.canceltext);
        back = findViewById(R.id.back2);

        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
        confirmPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back to Main Page", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                String confirmPassword = confirmPasswordEdit.getText().toString();
                Resources resources = getResources();
                int imageResId = R.drawable.dog;  // Replace with your image resource ID
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(imageResId)
                        + '/' + resources.getResourceTypeName(imageResId)
                        + '/' + resources.getResourceEntryName(imageResId));
                String IMAGEURI = imageUri.toString();
                if (username != null && !username.equals("") && password != null && !password.equals("") && confirmPassword != null && !confirmPassword.equals("")){

                     if (username.length() > 12) {
                        usernameEdit.setError("Username can only Accept 12 Characters");
                        usernameEdit.requestFocus();
                    } else if (username.contains(" ")) {

                         usernameEdit.setError("Spaces are not allowed");
                         usernameEdit.requestFocus();

                     }else{
                         if (password.equals(confirmPassword)) {
                             userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     if (dataSnapshot.exists()) {
                                         Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                                     } else {
                                         // Create a new user

                                         User newUser = new User(username, password,username,IMAGEURI,0, 0, "NIL");
                                         UserCount newUserCount = new UserCount(100,0,0,0,0);
                                         List<String> newFriendList = new ArrayList<>();
                                         newUser.setFriendList(newFriendList);
                                         Date date = new Date();
                                         Calendar calendar = Calendar.getInstance();
                                         calendar.setTime(date);
                                         // Subtract one day from the current date
                                         calendar.add(Calendar.DAY_OF_MONTH, -1);
                                         // Get the date one day before the current date
                                         Date oneDayBefore = calendar.getTime();
                                         LoginDate newLoginDate = new LoginDate(oneDayBefore);
                                         Log.v("Date", String.valueOf(oneDayBefore));

                                         usercountRef.child(username).setValue(newUserCount);
                                         userRef.child(username).setValue(newUser);
                                         userDateRef.child(username).setValue(newLoginDate);
                                         Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                         creatingachievements(null,username);
                                         creatingshop(null,username);
                                         finish(); // Finish the activity and go back to the login page
                                     }
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {
                                     Log.v("RegisterPage", "Error: " + databaseError.getMessage());
                                 }
                             });
                         } else {
                             Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                         }
                     }
                     }


                 else if (username == null || username.equals("")) {
                    usernameEdit.setError("Username is Empty");
                    usernameEdit.requestFocus();
                    if (password == null || password.equals("")) {
                        passwordEdit.setError("Password is Empty");
                        passwordEdit.requestFocus();
                        if (confirmPassword == null || confirmPassword.equals("")) {
                            confirmPasswordEdit.setError("Password is Empty");
                            confirmPasswordEdit.requestFocus();
                        }
                    }
                }

                else if (password == null || password.equals("")){
                    passwordEdit.setError("Password is Empty");
                    passwordEdit.requestFocus();
                    if (confirmPassword == null || confirmPassword.equals("")) {
                        confirmPasswordEdit.setError("Confirm Password is Empty");
                        confirmPasswordEdit.requestFocus();

                    }
                }

                else{
                    confirmPasswordEdit.setError("Confirm Password is Empty");
                    confirmPasswordEdit.requestFocus();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void creatingachievements(String[] args,String username) {
        List<Achievement> achievementList = new ArrayList<>();
        achievementRef = FirebaseDatabase.getInstance().getReference("UserAchievement").child(username);

        Achievement maketask5 = new Achievement("Create 5 task", 0, 5, "5","Coins", "Incomplete");
        Achievement maketask10 = new Achievement("Create 10 task", 0, 10, "10","Coins", "Incomplete");
        Achievement maketask20 = new Achievement("Create 20 task", 0, 20, "20","Coins", "Incomplete");
        Achievement maketask100 = new Achievement("Create 100 task",0,100,"Dedicted Planner","Title","Incomplete");

        Achievement login1 = new Achievement("Login for 1 day", 1, 1, "10","Coins", "Incomplete");
        Achievement login1day = new Achievement("Login for 1 day",0,1,"Novice User","Title","Incomplete");
        Achievement login5 = new Achievement("Login for 5 days", 0, 5, "10","Coins", "Incomplete");
        Achievement login10 = new Achievement("Login for 10 days", 0, 10, "20","Coins", "Incomplete");
        Achievement login30 = new Achievement("Login for 30 days", 0, 30, "50","Coins", "Incomplete");
        Achievement login100 = new Achievement("Login for 100 days",0,100,"Devoted User","Title","Incomplete");

        Achievement complete3 = new Achievement("Complete 3 task", 0, 3, "30","Coins", "Incomplete");
        Achievement complete5 = new Achievement("Complete 5 task", 0, 5, "20","Coins", "Incomplete");
        Achievement complete10 = new Achievement("Complete 10 task", 0, 10, "30","Coins", "Incomplete");
        Achievement complete100 = new Achievement("Complete 100 task",0,100,"Productive Achiever","Title","Incomplete");

        achievementList.add(maketask5);
        achievementList.add(maketask10);
        achievementList.add(maketask20);
        achievementList.add(maketask100);
        achievementList.add(login1);
        achievementList.add(login1day);
        achievementList.add(login5);
        achievementList.add(login10);
        achievementList.add(login30);
        achievementList.add(login100);
        achievementList.add(complete3);
        achievementList.add(complete5);
        achievementList.add(complete10);
        achievementList.add(complete100);

        // Sort the achievements based on their names
        Collections.sort(achievementList, (a1, a2) -> a1.getTitle().compareTo(a2.getTitle()));

        for (Achievement achievement : achievementList) {
            // Convert the achievement to a map that can be stored in Firebase
            Map<String, Object> achievementMap = new HashMap<>();
            achievementMap.put("title", achievement.getTitle());
            achievementMap.put("progress", achievement.getProgress());
            achievementMap.put("target", achievement.getMaxProgress());
            achievementMap.put("reward", achievement.getReward());
            achievementMap.put("status", achievement.getStatus());
            achievementMap.put("rewardtype",achievement.getRewardtype());

            // Generate a unique key for each achievement in Firebase
            String achievementKey = achievementRef.push().getKey();

            // Store the achievement under the generated key
            achievementRef.child(achievementKey).setValue(achievementMap);
            Log.v("}","]");
        }


    }

    public void creatingshop(String[] args,String username) {
        List<Shop> shopList= new ArrayList<>();


        int skyId = R.drawable.cardbeautyfulsky;  // Replace with your image resource ID
        String skyuri = getImageUriString(skyId);
        Shop item1 = new Shop(100,skyuri,"background",false);

        int forestId = R.drawable.forest;  // Replace with your image resource ID
        String foresturi = getImageUriString(forestId);
        Shop item2 = new Shop(100,foresturi,"background",false);

        int koreanId = R.drawable.korean;  // Replace with your image resource ID
        String koreanuri = getImageUriString(koreanId);
        Shop item3 = new Shop(100,koreanuri,"background",false);

        int pixelId = R.drawable.pixel;  // Replace with your image resource ID
        String pixeluri = getImageUriString(pixelId);
        Shop item4 = new Shop(100,pixeluri,"background",false);

        int blueId = R.drawable.blue; //Change it back to blue later
        String blueuri = getImageUriString(blueId);
        Shop item5 = new Shop(100,blueuri,"background",false);

        int outlineId = R.drawable.rectangle_line;
        String outlineuri = getImageUriString(outlineId);
        Shop item6 = new Shop(100,outlineuri,"outline",true);

        int dotId = R.drawable.dot_outline;
        String doturi = getImageUriString(dotId);
        Shop item7 = new Shop(100,doturi,"outline",false);

        int doubleId = R.drawable.doubleline_outline;
        String doubleuri = getImageUriString(doubleId);
        Shop item8 = new Shop(100,doubleuri,"outline",false);




        shopList.add(item1);
        shopList.add(item2);
        shopList.add(item3);
        shopList.add(item4);
        shopList.add(item5);
        shopList.add(item6);
        shopList.add(item7);
        shopList.add(item8);


        for (Shop shop : shopList) {
            // Convert the achievement to a map that can be stored in Firebase
            Map<String, Object> shopMap = new HashMap<>();
            shopMap.put("cost", shop.getCost());
            shopMap.put("carduri", shop.getCardimage());
            shopMap.put("fonttype", shop.getItemtype());
            shopMap.put("boughted", shop.isBoughted());

            shopRef = FirebaseDatabase.getInstance().getReference("Shop").child(username);

            String shopKey = shopRef.push().getKey();

            // Store the achievement under the generated key
            shopRef.child(shopKey).setValue(shopMap);

        }
    }




    private String getImageUriString(int imageResId) {
        Resources resources = getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(imageResId)
                + '/' + resources.getResourceTypeName(imageResId)
                + '/' + resources.getResourceEntryName(imageResId));
        return imageUri.toString();
    }
}

