package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import org.w3c.dom.Text;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Shop> {

    private Context context;
    private List<Shop> shop;
    private int expandedPosition = -1;
    private ListView listView;
    private String username;
    private String status;

    public interface OnImageClickListener {
        void onImageClick(Uri imageUri);

    }



    private OnImageClickListener onImageClickListener;


    // Method to set the listener
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }


    public CustomListAdapter(Context context, List<Shop> shop, ListView listView,String username) {
        super(context, 0, shop);
        this.context = context;
        this.shop = shop;
        this.listView = listView;
        this.username = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.background_example_item, parent, false);
        }

        Shop currentItem = shop.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);
        RelativeLayout relative = convertView.findViewById(R.id.relative);
        Uri image = Uri.parse(currentItem.getCardimage());
        Log.e("Image", "+" + currentItem.getCardimage());
        imageView.setImageURI(image);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the onImageClick method of the interface and pass the imageUri
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(image);

                    notifyDataSetChanged();

                }
            }
        });

        return convertView;






    }

}


