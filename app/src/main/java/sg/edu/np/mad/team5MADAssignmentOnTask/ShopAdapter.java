package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private Context context;
    private List<Shop> shopList;
    private String username;
    private OnButtonClickListener buttonClickListener;



    public interface OnButtonClickListener {
        void onButtonClick(Shop shop);
    }

    // Step 2: Provide a method to set the click listener from the activity
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public ShopAdapter(Context context, List<Shop> shopList, String username) {
        this.context = context;
        this.shopList = shopList;
        this.username = username;// Store the intent data in the adapter

    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_shop, parent, false);
        return new ShopAdapter.ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        if (shop.isBoughted() == false) {

            holder.buy.setText("Buy");
            String Cost = "    Coins: " + String.valueOf(shop.getCost());
            holder.cost.setText(Cost);
            Uri imageuri = Uri.parse(shop.getCardimage());
            holder.image.setImageURI(imageuri);
            if (shop.getItemtype().equals("background")){
                holder.title.setText("Goal Background");
            }

            else{holder.title.setText("Title Outline");}

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buttonClickListener != null) {
                        buttonClickListener.onButtonClick(shop);
                    }
                }
            });
        }else {
            // If shop.isBoughted() is false, hide the layout elements
            holder.buy.setVisibility(View.GONE);
            holder.cost.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
            holder.layout.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title,cost,cancel,coin;
        Button buy;
        ConstraintLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.squircleBackground);
            title = itemView.findViewById(R.id.textView6);
            cost = itemView.findViewById(R.id.costTextView);
            buy = itemView.findViewById(R.id.button);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    private void getItemKeyFromFirebase(Shop ClaimedItem, ItemKeyCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shop").child(username);

        databaseReference.orderByChild("carduri").equalTo(ClaimedItem.getCardimage()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Found the matching achievement, return its itemkey through the callback
                    String itemKey = snapshot.getKey();
                    callback.onItemKeyObtained(itemKey);
                    return; // Important: stop the loop after finding the matching achievement

                }
                // If the loop completes without finding the achievement, return null
                callback.onItemKeyObtained(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error here, if necessary
                callback.onItemKeyObtained(null);
            }
        });
    }

    public interface ItemKeyCallback {
        void onItemKeyObtained(String itemKey);
    }

    void claimItem(Shop shop) {
        // Get the itemKey from the Firebase database
        getItemKeyFromFirebase(shop, new ItemKeyCallback() {
            @Override
            public void onItemKeyObtained(String itemKey) {
                if (itemKey != null) {
                    DatabaseReference achievementRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(username).child(itemKey);
                    achievementRef.child("boughted").setValue(true);

                    if (context instanceof Activity) {
                        ((Activity) context).recreate();
                    }

                } else {
                    // Handle the case when the itemKey is not found (optional)
                }
            }
        });
    }


    private static final int SORT_BY_TITLE = 1;
    private static final int SORT_BY_BACKGROUND = 2;
    private static final int SORT_BY_UNSORT = 3;

    private int currentSortType = SORT_BY_UNSORT; // Default sorting type

    // Method to handle the sorting
    public void sortData() {
        switch (currentSortType) {
            case SORT_BY_TITLE:
                // Sort data by title using a custom comparator
                Collections.sort(shopList, new TitleComparator());
                currentSortType = SORT_BY_BACKGROUND;
                break;
            case SORT_BY_BACKGROUND:
                // Sort data by background using a custom comparator
                Collections.sort(shopList, new BackgroundComparator());
                currentSortType = SORT_BY_UNSORT;
                break;
            case SORT_BY_UNSORT:
                // Do not sort, revert to original order
                currentSortType = SORT_BY_TITLE;
                break;
        }
        notifyDataSetChanged();
    }

    // Custom comparators for sorting by title and background

    private class TitleComparator implements Comparator<Shop> {
        @Override
        public int compare(Shop shop1, Shop shop2) {
            return shop1.getItemtype().equals("title") ? -1 : 1;
        }
    }

    private class BackgroundComparator implements Comparator<Shop> {
        @Override
        public int compare(Shop shop1, Shop shop2) {
            return shop1.getItemtype().equals("background") ? -1 : 1;
        }
    }


    public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;
        private final int horizontalSpaceWidth;

        public ItemSpacingDecoration(int verticalSpaceHeight, int horizontalSpaceWidth) {
            this.verticalSpaceHeight = verticalSpaceHeight;
            this.horizontalSpaceWidth = horizontalSpaceWidth;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = verticalSpaceHeight;
            outRect.left = horizontalSpaceWidth;
            outRect.right = horizontalSpaceWidth;
        }
    }





}