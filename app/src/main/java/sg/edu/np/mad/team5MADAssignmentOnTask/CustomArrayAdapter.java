package sg.edu.np.mad.team5MADAssignmentOnTask;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;

    public CustomArrayAdapter(Context context, List<String> data) {
        super(context, android.R.layout.simple_list_item_1, data);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Context context = parent.getContext();
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {}

        else {
            // Set the text color to black
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
        }


        return view;
    }
}



