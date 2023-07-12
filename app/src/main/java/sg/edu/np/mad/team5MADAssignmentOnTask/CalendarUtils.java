package sg.edu.np.mad.team5MADAssignmentOnTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
    public static Date selectedDate;

    public static String formattedDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static String formattedTime(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return formatter.format(time);
    }

    public static String monthYearFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public static ArrayList<Date> daysInMonthArray(Date date) {
        ArrayList<Date> daysInMonthArray = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar firstOfMonth = (Calendar) calendar.clone();
        firstOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(null);
            } else {
                Calendar day = (Calendar) calendar.clone();
                day.set(Calendar.DAY_OF_MONTH, i - dayOfWeek + 1);
                daysInMonthArray.add(day.getTime());
            }
        }

        return daysInMonthArray;
    }

    public static ArrayList<Date> daysInWeekArray(Date selectedDate) {
        ArrayList<Date> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        Calendar current = sundayForDate(calendar);
        Calendar endDate = (Calendar) current.clone();
        endDate.add(Calendar.WEEK_OF_YEAR, 1);

        while (current.before(endDate)) {
            days.add(current.getTime());
            current.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    private static Calendar sundayForDate(Calendar calendar) {
        Calendar current = (Calendar) calendar.clone();
        Calendar oneWeekAgo = (Calendar) calendar.clone();
        oneWeekAgo.add(Calendar.WEEK_OF_YEAR, -1);

        while (current.after(oneWeekAgo)) {
            if (current.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                return current;

            current.add(Calendar.DAY_OF_MONTH, -1);
        }

        return null;
    }
}
