package sg.edu.np.mad.team5MADAssignmentOnTask;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements ShopAdapter.OnButtonClickListener {
    private RecyclerView recyclerView;
    private ShopAdapter adapter;
    private List<Shop> shopList;
    private DatabaseReference shopRef;
    private DatabaseReference CountRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_page);

        String username = getIntent().getStringExtra("USERNAME");
        Log.v("Username",username);



        TextView title = findViewById(R.id.title_text);
        ImageView cancel = findViewById(R.id.close_button);
        TextView CoinsCount = findViewById(R.id.coins);

        title.setText("Shop");

        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // The value of CountRef can be accessed here
                int countValue = dataSnapshot.child("coincount").getValue(Integer.class);
                Log.v("Count", "+" + countValue);
                String Coins = "Coins: " + countValue;
                CoinsCount.setText(Coins);
                Log.d("CountValue", "Value: " + countValue);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("CountValue", "Error: " + databaseError.getMessage());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Call finish() when the cancel button is clicked
            }
        });



        recyclerView = findViewById(R.id.recycler_view);
        shopList = new ArrayList<>();

        // Create the adapter and pass the intent data and username
        adapter = new ShopAdapter(this, shopList, username);

        adapter.setOnButtonClickListener(this);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        // Get a reference to the UserAchievement node in Firebase
        shopRef = FirebaseDatabase.getInstance().getReference("Shop").child(username);

        // Attach a ValueEventListener to retrieve the achievement data
        shopRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear(); // Clear the existing list

                // Iterate through the usernames
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                    String shopSnapshotKey = shopSnapshot.getKey();

                    if(shopSnapshot.child("cost").getValue(Integer.class) != null){
                        Log.v("tesy", "+" + shopSnapshot.child("fonttype").getValue(String.class));
                        int cost = shopSnapshot.child("cost").getValue(Integer.class);
                        Log.v("cost", "}" + shopSnapshot.child("carduri").getValue(String.class));
                        String image = shopSnapshot.child("carduri").getValue(String.class);
                        String font = shopSnapshot.child("fonttype").getValue(String.class);
                        boolean status = shopSnapshot.child("boughted").getValue(boolean.class);

                        // Create a Shop object and add it to the list
                        Shop shop = new Shop(cost, image, font, status);

                        shopList.add(shop);
                    }



                }
                adapter.notifyDataSetChanged();
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

    public void onButtonClick(Shop shop) {

        String username = getIntent().getStringExtra("USERNAME");
        Log.v("button usser", "+" + username);
        CountRef = FirebaseDatabase.getInstance().getReference("UserCount").child(username);

        CountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // The value of CountRef can be accessed here
                int countValue = dataSnapshot.child("coincount").getValue(Integer.class);

                if (countValue >= shop.getCost()) {
                    new AlertDialog.Builder(ShopActivity.this)
                            .setMessage("Confirm Purchase")
                            .setCancelable(false)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(ShopActivity.this, ShopActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    FirebaseDatabase.getInstance().getReference("UserCount").child(username).child("coincount").setValue(countValue-shop.getCost());
                                    claimItem(shop);
                                    intent.putExtra("USERNAME", username);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton("Back", null)
                            .show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("CountValue", "Error: " + databaseError.getMessage());
            }
        });
        Toast.makeText(this, "Button clicked at position " + shop.getFonttype(), Toast.LENGTH_SHORT).show();
    }
    private void claimItem(Shop shop) {
        // Call the claimAchievement method in the adapter
        adapter.claimItem(shop);
    }


}

