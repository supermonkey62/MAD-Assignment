package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<WeeklyCalendarViewHolder> {
    private final ArrayList<Date> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<Date> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public WeeklyCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15) { // Month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else { // Week view
            layoutParams.height = parent.getHeight();
        }
        return new WeeklyCalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyCalendarViewHolder holder, int position) {
        final Date date = days.get(position);
        if (date == null) {
            holder.dayOfMonth.setText("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("d", Locale.getDefault());
            holder.dayOfMonth.setText(formatter.format(date));
            if (isSameDate(date, CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.parentView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, Date date);
    }

    private boolean isSameDate(Date date1, Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return formatter.format(date1).equals(formatter.format(date2));
    }
}
