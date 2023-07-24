package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_item_list_view);

        listView = findViewById(R.id.listView);
        ImageButton closebtn = findViewById(R.id.btnClose);
        Button setButton = findViewById(R.id.btnClaim);

        String username = getIntent().getStringExtra("USERNAME");




        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference shopReference = firebaseDatabase.getReference("Shop").child(username);


        // Fetch data from Firebase
        shopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String key = itemSnapshot.getKey();
                    if (itemSnapshot.child("boughted").getValue(boolean.class) == true){
                        int cost = itemSnapshot.child("cost").getValue(Integer.class);
                        String carduri =itemSnapshot.child("carduri").getValue(String.class);
                        String font = itemSnapshot.child("fonttype").getValue(String.class);
                        boolean boughted = itemSnapshot.child("boughted").getValue(boolean.class);
                        Shop shop = new Shop(cost,carduri,font,boughted);
                        shopList.add(shop);
                    }

                }

                // Create the adapter and set it to the ListView
                CustomListAdapter adapter = new CustomListAdapter(Choose_Item_Activity.this, shopList,listView);
                adapter.setOnImageClickListener(Choose_Item_Activity.this);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that might occur during data fetching
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onImageClick(Uri imageUri) {
        String username = getIntent().getStringExtra("USERNAME");
        uri = imageUri;
        String image = imageUri.toString();
        FirebaseDatabase.getInstance().getReference("UserEquip").child(username).child("background").setValue(image);
        finish();

    }

}

