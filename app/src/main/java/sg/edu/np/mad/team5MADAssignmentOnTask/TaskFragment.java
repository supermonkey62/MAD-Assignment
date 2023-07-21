package sg.edu.np.mad.team5MADAssignmentOnTask;

        import static sg.edu.np.mad.team5MADAssignmentOnTask.CalendarUtils.daysInWeekArray;

        import android.icu.util.Calendar;
        import android.os.Bundle;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.GridLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;

public class TaskFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        CalendarUtils.selectedDate = calendar.getTime();

        initWidgets(view);
        setWeekView();
        return view;
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    private void setWeekView() {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(monthYearFormat.format(CalendarUtils.selectedDate));
        ArrayList<Date> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousWeekAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CalendarUtils.selectedDate);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        CalendarUtils.selectedDate = calendar.getTime();
        setWeekView();
    }

    public void nextWeekAction(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CalendarUtils.selectedDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        CalendarUtils.selectedDate = calendar.getTime();
        setWeekView();
    }

    @Override
    public void onItemClick(int position, Date date) {
        CalendarUtils.selectedDate = date;
        setWeekView();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String selectedDateText = dateFormat.format(date);
        Toast.makeText(getActivity(), "Selected date: " + selectedDateText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWeekView(); // Refresh the view on resume if needed
    }
}