package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAnalysis extends AppCompatActivity implements TaskDataHolder.TaskDataCallback {
    private List<Task> taskList;
    private Map<String, Integer> categoryColors;

    private ImageButton backbttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_analysis);
        backbttn = findViewById(R.id.btnGoBack);
        String username = getIntent().getStringExtra("USERNAME");

        categoryColors = createCategoryColors();

        TaskDataHolder.getInstance().fetchUserTasks(username, this);

        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }



    private Map<String, Integer> createCategoryColors() {
        Map<String, Integer> colors = new HashMap<>();
        colors.put("Personal", Color.parseColor("#FFB6C1"));  // Pastel Pink
        colors.put("School", Color.parseColor("#FFD700"));  // Pastel Gold
        colors.put("Assignments", Color.parseColor("#00FFFF"));  // Pastel Cyan
        colors.put("Projects", Color.parseColor("#98FB98"));  // Pastel Green
        colors.put("Errands and Shopping", Color.parseColor("#FFA07A"));  // Pastel Salmon
        colors.put("Health and Fitness", Color.parseColor("#87CEFA"));  // Pastel Sky Blue
        return colors;
    }

    private void createBarChart(float personal, float school, float assignment, float projects, float errand, float health) {
        // Create bar entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        if (personal > 0) {
            entries.add(new BarEntry(0, personal));
            barColors.add(categoryColors.get("Personal"));
            categories.add("Personal");
        }
        if (school > 0) {
            entries.add(new BarEntry(1, school));
            barColors.add(categoryColors.get("School"));
            categories.add("School");
        }
        if (assignment > 0) {
            entries.add(new BarEntry(2, assignment));
            barColors.add(categoryColors.get("Assignments"));
            categories.add("Assignments");
        }
        if (projects > 0) {
            entries.add(new BarEntry(3, projects));
            barColors.add(categoryColors.get("Projects"));
            categories.add("Projects");
        }
        if (errand > 0) {
            entries.add(new BarEntry(4, errand));
            barColors.add(categoryColors.get("Errands and Shopping"));
            categories.add("Errands and Shopping");
        }
        if (health > 0) {
            entries.add(new BarEntry(5, health));
            barColors.add(categoryColors.get("Health and Fitness"));
            categories.add("Health and Fitness");
        }

        // Create a dataset using the entries
        BarDataSet dataSet = new BarDataSet(entries, "Time Spent");
        dataSet.setColors(barColors);
        float barWidth = 0.5f;

        // Create a BarData object with the dataset
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(barWidth);


        // Get the BarChart reference and set the data
        BarChart barChart = findViewById(R.id.Barchart1);
        barChart.setData(barData);
        barChart.setTouchEnabled(false);
        barChart.getDescription().setEnabled(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        YAxis yAxis1 = barChart.getAxisRight();
        yAxis1.setAxisMinimum(0f);


        // Hide X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);



        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(70f);
        legend.setYEntrySpace(10f);
        legend.setTextSize(10f);
        legend.setWordWrapEnabled(true);

        // Create legend entries for each category
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            LegendEntry entry = new LegendEntry();
            entry.label = category;
            entry.formColor = barColors.get(i);
            legendEntries.add(entry);
        }

        // Set the legend entries
        legend.setCustom(legendEntries);

        // Refresh the chart
        barChart.invalidate();
    }

    private void displayAverageSessPerCategory(int avgPersonalSessions, int avgSchoolSessions, int avgAssignmentSessions,
                                               int avgProjectsSessions, int avgErrandSessions, int avgHealthSessions) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        if (avgPersonalSessions > 0) {
            entries.add(new BarEntry(0, avgPersonalSessions));
            barColors.add(categoryColors.get("Personal"));
            categories.add("Personal");
        }
        if (avgSchoolSessions > 0) {
            entries.add(new BarEntry(1, avgSchoolSessions));
            barColors.add(categoryColors.get("School"));
            categories.add("School");
        }
        if (avgAssignmentSessions > 0) {
            entries.add(new BarEntry(2, avgAssignmentSessions));
            barColors.add(categoryColors.get("Assignments"));
            categories.add("Assignments");
        }
        if (avgProjectsSessions > 0) {
            entries.add(new BarEntry(3, avgProjectsSessions));
            barColors.add(categoryColors.get("Projects"));
            categories.add("Projects");
        }
        if (avgErrandSessions > 0) {
            entries.add(new BarEntry(4, avgErrandSessions));
            barColors.add(categoryColors.get("Errands and Shopping"));
            categories.add("Errands and Shopping");
        }
        if (avgHealthSessions > 0) {
            entries.add(new BarEntry(5, avgHealthSessions));
            barColors.add(categoryColors.get("Health and Fitness"));
            categories.add("Health and Fitness");
        }

        // Create a dataset using the entries
        BarDataSet dataSet = new BarDataSet(entries, "Average Sessions");
        dataSet.setColors(barColors);
        float barWidth = 0.5f;

        // Create a BarData object with the dataset
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(barWidth);

        // Get the BarChart reference and set the data
        BarChart barChartAverageSessionsPerCategory = findViewById(R.id.barChartAverageSessionsPerCategory);
        barChartAverageSessionsPerCategory.setData(barData);
        barChartAverageSessionsPerCategory.setTouchEnabled(false);
        barChartAverageSessionsPerCategory.getDescription().setText("Average number of Sessions by Category");
        barChartAverageSessionsPerCategory.getDescription().setEnabled(false);

        // Hide X-axis labels
        YAxis yAxis = barChartAverageSessionsPerCategory.getAxisLeft(); // Get the left Y-axis
        yAxis.setAxisMinimum(0f);
        YAxis yAxis1 = barChartAverageSessionsPerCategory.getAxisRight();
        yAxis1.setAxisMinimum(0f);


        // Hide X-axis labels
        XAxis xAxis = barChartAverageSessionsPerCategory.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);


        Legend legend = barChartAverageSessionsPerCategory.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(70f);
        legend.setYEntrySpace(10f);
        legend.setTextSize(10f);
        legend.setWordWrapEnabled(true);

        // Create legend entries for each category
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            LegendEntry entry = new LegendEntry();
            entry.label = category;
            entry.formColor = barColors.get(i);
            legendEntries.add(entry);
        }

        // Set the legend entries
        legend.setCustom(legendEntries);

        // Refresh the chart
        barChartAverageSessionsPerCategory.invalidate();
    }


    private void createPieChart(float personalCount, float schoolCount, float assignmentCount, float projectsCount, float errandCount, float healthCount) {
        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();

        if (personalCount > 0) {
            entries.add(new PieEntry(personalCount, "Personal"));
            pieColors.add(categoryColors.get("Personal"));
        }
        if (schoolCount > 0) {
            entries.add(new PieEntry(schoolCount, "School"));
            pieColors.add(categoryColors.get("School"));
        }
        if (assignmentCount > 0) {
            entries.add(new PieEntry(assignmentCount, "Assignments"));
            pieColors.add(categoryColors.get("Assignments"));
        }
        if (projectsCount > 0) {
            entries.add(new PieEntry(projectsCount, "Projects"));
            pieColors.add(categoryColors.get("Projects"));
        }
        if (errandCount > 0) {
            entries.add(new PieEntry(errandCount, "Errands and Shopping"));
            pieColors.add(categoryColors.get("Errands and Shopping"));
        }
        if (healthCount > 0) {
            entries.add(new PieEntry(healthCount, "Health and Fitness"));
            pieColors.add(categoryColors.get("Health and Fitness"));
        }

        // Create a dataset using the entries
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(pieColors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Create a PieData object with the dataset
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        // Get the PieChart reference and set the data
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.getLegend().setEnabled(true);
        pieChart.setTouchEnabled(false);
        pieChart.getDescription().setEnabled(false);



        // Refresh the chart
        pieChart.invalidate();
    }

    private void createPieChartcomplete(float personalCount, float schoolCount, float assignmentCount, float projectsCount, float errandCount, float healthCount) {
        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();

        if (personalCount > 0) {
            entries.add(new PieEntry(personalCount, "Personal"));
            pieColors.add(categoryColors.get("Personal"));
        }
        if (schoolCount > 0) {
            entries.add(new PieEntry(schoolCount, "School"));
            pieColors.add(categoryColors.get("School"));
        }
        if (assignmentCount > 0) {
            entries.add(new PieEntry(assignmentCount, "Assignments"));
            pieColors.add(categoryColors.get("Assignments"));
        }
        if (projectsCount > 0) {
            entries.add(new PieEntry(projectsCount, "Projects"));
            pieColors.add(categoryColors.get("Projects"));
        }
        if (errandCount > 0) {
            entries.add(new PieEntry(errandCount, "Errands and Shopping"));
            pieColors.add(categoryColors.get("Errands and Shopping"));
        }
        if (healthCount > 0) {
            entries.add(new PieEntry(healthCount, "Health and Fitness"));
            pieColors.add(categoryColors.get("Health and Fitness"));
        }

        // Create a dataset using the entries
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(pieColors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        // Create a PieData object with the dataset
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        // Get the PieChart reference and set the data
        PieChart pieChart = findViewById(R.id.pieChartcomplete);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.getLegend().setEnabled(true);


        pieChart.setTouchEnabled(false);
        pieChart.getDescription().setEnabled(false);




        // Refresh the chart
        pieChart.invalidate();
    }
    private void createLineChart(List<Task> tasks) {
        // Get the current year
        String currentYear = GetYear(getCurrentDate());

        // Map to store the count of completed tasks for each month of the current year
        Map<String, Integer> completedTasksByMonth = new HashMap<>();

        // Calculate the count of completed tasks for each month of the current year
        for (Task task : tasks) {
            if (task.getStatus() && currentYear.equals(GetYear(task.getDate()))) {
                String month = getMonthFromDate(task.getDate());
                Log.v("string month", task.getDate());
                Log.v("month", month);
                if (completedTasksByMonth.containsKey(month)) {
                    int count = completedTasksByMonth.get(month);
                    completedTasksByMonth.put(month, count + 1);
                } else {
                    completedTasksByMonth.put(month, 1);
                }
            }
        }

        // Sort the map entries based on the month's order
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(completedTasksByMonth.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                // Assuming the month names are in the format "Jan", "Feb", ..., "Dec"
                String[] monthOrder = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                return Arrays.asList(monthOrder).indexOf(entry1.getKey()) - Arrays.asList(monthOrder).indexOf(entry2.getKey());
            }
        });

        // Create line entries
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Populate entries and labels in the sorted order
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String month = entry.getKey();
            int count = entry.getValue();
            entries.add(new Entry(labels.size(), count));
            labels.add(month);
            Log.v("month", month);
        }

        // Create a dataset using the entries
        LineDataSet dataSet = new LineDataSet(entries, "Completed Tasks");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(true);

        // Create a LineData object with the dataset
        LineData lineData = new LineData(dataSet);

        // Get the LineChart reference and set the data
        LineChart lineChart = findViewById(R.id.lineChart);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);

        YAxis yAxis =lineChart.getAxisLeft(); // Get the left Y-axis
        yAxis.setAxisMinimum(0f);

        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setAxisMinimum(0f);





        // Customize x-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // Set a minimum interval for displaying values to 1 (one month)
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAxisMinimum(0f);

        lineChart.getAxisLeft().setEnabled(false);

        xAxis.setLabelCount(labels.size());

        // Refresh the chart
        lineChart.invalidate();
    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    private String getMonthFromDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("DateParsing", "Parsing failed: " + e.getMessage());
        }
        return "";
    }

    private String GetYear(String date){
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("YYYY");
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("DateParsing", "Parsing failed: " + e.getMessage());
        }
        return "";

    }
    private void displayAverageTime(List<Task> tasks) {
        float totalSpentTime = 0;
        int totalTasks = tasks.size(); // The total number of tasks

        for (Task task : tasks) {
            totalSpentTime += task.getTimespent();
        }

        float averageTime = totalSpentTime / totalTasks;

        // Format the average time with two decimal places
        String formattedAverageTime = String.format("%.2f", averageTime);

        // Find the CardView for Average Time
        CardView cardViewAverageTime = findViewById(R.id.cardViewAverageTime);

        // Find the TextView to display the average time
        TextView textViewAverageTime = cardViewAverageTime.findViewById(R.id.textViewAverageTime);

        // Update the text with the calculated average time
        textViewAverageTime.setText("Average Time: " + formattedAverageTime + " minutes");


    }



    private void displayAverageTimePerCategory(float personalAvg, float schoolAvg, float assignmentAvg,
                                               float projectsAvg, float errandAvg, float healthAvg) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        if (personalAvg > 0) {
            entries.add(new BarEntry(0, personalAvg));
            barColors.add(categoryColors.get("Personal"));
            categories.add("Personal");
        }
        if (schoolAvg > 0) {
            entries.add(new BarEntry(1, schoolAvg));
            barColors.add(categoryColors.get("School"));
            categories.add("School");
        }
        if (assignmentAvg > 0) {
            entries.add(new BarEntry(2, assignmentAvg));
            barColors.add(categoryColors.get("Assignments"));
            categories.add("Assignments");
        }
        if (projectsAvg > 0) {
            entries.add(new BarEntry(3, projectsAvg));
            barColors.add(categoryColors.get("Projects"));
            categories.add("Projects");
        }
        if (errandAvg > 0) {
            entries.add(new BarEntry(4, errandAvg));
            barColors.add(categoryColors.get("Errands and Shopping"));
            categories.add("Errands and Shopping");
        }
        if (healthAvg > 0) {
            entries.add(new BarEntry(5, healthAvg));
            barColors.add(categoryColors.get("Health and Fitness"));
            categories.add("Health and Fitness");
        }

        // Create a dataset using the entries
        BarDataSet dataSet = new BarDataSet(entries, "Average Time Spent (minutes)");
        dataSet.setColors(barColors);
        float barWidth = 0.5f;

        // Create a BarData object with the dataset
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(barWidth);

        // Get the BarChart reference and set the data
        BarChart barChartAverageTimePerCategory = findViewById(R.id.barChartAverageTimePerCategory);
        barChartAverageTimePerCategory.setData(barData);
        barChartAverageTimePerCategory.getDescription().setEnabled(false);

        barChartAverageTimePerCategory.setTouchEnabled(false);

        YAxis yAxis = barChartAverageTimePerCategory.getAxisLeft(); // Get the left Y-axis
        yAxis.setAxisMinimum(0f);

        YAxis yAxis1 = barChartAverageTimePerCategory.getAxisRight();
        yAxis1.setAxisMinimum(0f);


        // Hide X-axis labels
        XAxis xAxis = barChartAverageTimePerCategory.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);


        // Hide the default legend
        barChartAverageTimePerCategory.getLegend().setEnabled(false);

        Legend legend = barChartAverageTimePerCategory.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(70f);
        legend.setYEntrySpace(10f);
        legend.setTextSize(10f);
        legend.setWordWrapEnabled(true);

        // Create legend entries for each category
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            LegendEntry entry = new LegendEntry();
            entry.label = category;
            entry.formColor = barColors.get(i);
            legendEntries.add(entry);
        }

        // Set the legend entries
        legend.setCustom(legendEntries);



        // Refresh the chart
        barChartAverageTimePerCategory.invalidate();
    }


    @Override
    public void onTaskDataFetched(List<Task> tasks) {
        taskList = tasks;
        float personalTime = 0;
        float schoolTime = 0;
        float assignmentTime = 0;
        float projectsTime = 0;
        float errandTime = 0;
        float healthTime = 0;

        float personalCount = 0;
        float schoolCount = 0;
        float assignmentCount = 0;
        float projectsCount = 0;
        float errandCount = 0;
        float healthCount = 0;

        float personaldoneCount = 0;
        float schooldoneCount = 0;
        float assignmentdoneCount = 0;
        float projectsdoneCount = 0;
        float erranddoneCount = 0;
        float healthdoneCount = 0;

        float personalavg = 0;
        float schoolavg = 0;
        float assignmentavg = 0;
        float projectsavg= 0;
        float errandavg = 0;
        float healthavg = 0;

        int personalsess = 0;
        int schoolsess = 0;
        int assignmentsess = 0;
        int projectsess = 0;
        int errandsess = 0;
        int healthsess = 0;

        int avgpersonalsess = 0;
        int avgschoolsess = 0;
        int avgassignmentsess = 0;
        int avgprojectsess = 0;
        int avgerrandsess = 0;
        int avghealthsess = 0;



        for (Task task : tasks) {
            String category = task.getCategory();
            float timeSpent = task.getTimespent();
            int sessions = task.getSessions();

            if (category.equals("Personal Tasks")) {
                personalTime += timeSpent;
                if (task.getStatus() == true){
                    personaldoneCount += 1;
                }
                personalCount += 1;
                personalavg = personalTime / personalCount;
                personalsess += sessions;
                avgpersonalsess = (int) (personalsess/personalCount);

            } else if (category.equals("School Task")) {
                schoolTime += timeSpent;
                if (task.getStatus() == true){
                    schooldoneCount += 1;
                }
                schoolCount += 1;
                schoolavg = schoolTime/schoolCount;
                schoolsess += sessions;
                avgschoolsess = (int)(schoolsess/schoolCount);
            } else if (category.equals("Assignments")) {
                assignmentTime += timeSpent;
                if (task.getStatus() == true){
                    assignmentdoneCount += 1;
                }
                assignmentCount += 1;
                assignmentavg = assignmentTime/assignmentCount;
                assignmentsess += sessions;
                avgassignmentsess = (int)(assignmentsess/assignmentCount);
            } else if (category.equals("Projects")) {
                projectsTime += timeSpent;
                if (task.getStatus() == true){
                    projectsdoneCount += 1;
                }
                projectsCount += 1;
                projectsavg = projectsTime/projectsCount;
                projectsess += sessions;
                avgprojectsess = (int)(projectsess/projectsCount);
            } else if (category.equals("Errands and Shopping Tasks")) {
                errandTime += timeSpent;
                if (task.getStatus() == true){
                    erranddoneCount += 1;
                }
                errandCount += 1;
                errandavg = errandTime/errandCount;
                errandsess += sessions;
                errandsess = (int)(errandsess/errandCount);
            } else if (category.equals("Health and Fitness Tasks")) {
                healthTime += timeSpent;
                if (task.getStatus() == true){
                    healthdoneCount += 1;
                }
                healthCount += 1;
                healthavg = healthTime/healthCount;
                healthsess += sessions;
                avghealthsess = (int)(healthsess/healthCount);
            }
        }

        createBarChart(personalTime, schoolTime, assignmentTime, projectsTime, errandTime, healthTime);
        createPieChart(personalCount, schoolCount, assignmentCount, projectsCount, errandCount, healthCount);
        createLineChart(tasks);
        displayAverageTime(tasks);
        displayAverageTimePerCategory(personalavg, schoolavg, assignmentavg, projectsavg, errandavg, healthavg);
        displayAverageSessPerCategory(avgpersonalsess,avgschoolsess,avgassignmentsess,avgprojectsess,avgerrandsess,avghealthsess);
        createPieChartcomplete(personaldoneCount, schooldoneCount, assignmentdoneCount, projectsdoneCount, erranddoneCount, healthdoneCount);
    }
    }
