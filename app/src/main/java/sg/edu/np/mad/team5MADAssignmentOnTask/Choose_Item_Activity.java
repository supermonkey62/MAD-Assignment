package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Choose_Item_Activity extends AppCompatActivity implements CustomListAdapter.OnImageClickListener {

    private ListView listView;
    private List<Shop> shopList;
    private Uri uri;

    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_item_list_view);

        listView = findViewById(R.id.listView);
        ImageButton closebtn = findViewById(R.id.btnClose);
        Button setButton = findViewById(R.id.btnClaim);
        ConstraintLayout layout = findViewById(R.id.layout);
        LinearLayout linear = findViewById(R.id.linear);
        TextView goal = findViewById(R.id.textOn);
        TextView title = findViewById(R.id.textOff);

        String username = getIntent().getStringExtra("USERNAME");
        type = getIntent().getStringExtra("Type");




        Context context = this;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode
            linear.setBackgroundColor(ContextCompat.getColor(context,R.color.grey));
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_background));
            closebtn.setImageResource(R.drawable.white_cross);
        }

        type = "background";
        fetchandsort(username);
        goal.setBackgroundResource(R.drawable.rectangle_line);
        title.setBackground(null);
        setButton.setEnabled(true);



        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "outline";
                fetchandsort(username);
                title.setBackgroundResource(R.drawable.rectangle_line);
                goal.setBackground(null);
                setButton.setBackground(null);
                setButton.setEnabled(false);
            }
;
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "background";
                fetchandsort(username);
                goal.setBackgroundResource(R.drawable.rectangle_line);
                title.setBackground(null);
                title.setText(null);
                setButton.setEnabled(true);
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Choose_Item_Activity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                finish();

            }
        });






        // Initialize Firebase


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("USERNAME");
                FirebaseDatabase.getInstance().getReference("UserEquip").child(username).child(type).setValue("");
                Log.v("+","");
                finish();
            }
        });
    }
    @Override
    public void onImageClick(Uri imageUri) {
        String username = getIntent().getStringExtra("USERNAME");
        uri = imageUri;
        String image = imageUri.toString();
        FirebaseDatabase.getInstance().getReference("UserEquip").child(username).child(type).setValue(image);
        finish();

    }

    private void fetchandsort(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shopReference = firebaseDatabase.getReference("Shop").child(username);


        // Fetch data from Firebase
        shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String key = itemSnapshot.getKey();
                    String carduri =itemSnapshot.child("carduri").getValue(String.class);
                    String itemtpye = itemSnapshot.child("fonttype").getValue(String.class);
                    boolean boughted = itemSnapshot.child("boughted").getValue(boolean.class);
                    if (boughted == true && itemtpye.equals(type)){
                        int cost = itemSnapshot.child("cost").getValue(Integer.class);
                        Shop shop = new Shop(cost,carduri,itemtpye,boughted);

                        shopList.add(shop);
                    }

                }


                // Create the adapter and set it to the ListView
                CustomListAdapter adapter = new CustomListAdapter(Choose_Item_Activity.this, shopList,listView,username);
                adapter.setOnImageClickListener(Choose_Item_Activity.this);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that might occur during data fetching
            }
        });
    }


}

