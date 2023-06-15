package sg.edu.np.mad.madassignment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView box1View, box2View;

    public ViewHolder(View itemView) {
        super(itemView);
        box1View = itemView.findViewById(R.id.box1);
        box2View = itemView.findViewById(R.id.box2);
    }
}
