package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAnalysis extends AppCompatActivity implements TaskDataHolder.TaskDataCallback {
    private List<Task> taskList;
    private Map<String, Integer> categoryColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_analysis);
        String username = getIntent().getStringExtra("USERNAME");

        categoryColors = createCategoryColors();

        TaskDataHolder.getInstance().fetchUserTasks(username, this);

    }

    private Map<String, Integer> createCategoryColors() {
        Map<String, Integer> colors = new HashMap<>();
        colors.put("Personal Tasks", Color.parseColor("#FFB6C1"));  // Pastel Pink
        colors.put("School Task", Color.parseColor("#FFD700"));  // Pastel Gold
        colors.put("Assignments", Color.parseColor("#00FFFF"));  // Pastel Cyan
        colors.put("Projects", Color.parseColor("#98FB98"));  // Pastel Green
        colors.put("Errands and Shopping Tasks", Color.parseColor("#FFA07A"));  // Pastel Salmon
        colors.put("Health and Fitness Tasks", Color.parseColor("#87CEFA"));  // Pastel Sky Blue
        return colors;
    }

    private void createBarChart(float personal, float school, float assignment, float projects, float errand, float health) {
        // Create bar entries
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        if (personal > 0) {
            entries.add(new BarEntry(0, personal));
            barColors.add(categoryColors.get("Personal Tasks"));
            categories.add("Personal Tasks");
        }
        if (school > 0) {
            entries.add(new BarEntry(1, school));
            barColors.add(categoryColors.get("School Task"));
            categories.add("School Task");
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
            barColors.add(categoryColors.get("Errands and Shopping Tasks"));
            categories.add("Errands and Shopping Tasks");
        }
        if (health > 0) {
            entries.add(new BarEntry(5, health));
            barColors.add(categoryColors.get("Health and Fitness Tasks"));
            categories.add("Health and Fitness Tasks");
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

        // Hide X-axis labels
        XAxis xAxis = barChart.getXAxis();
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

    private void createPieChart(float personalCount, float schoolCount, float assignmentCount, float projectsCount, float errandCount, float healthCount) {
        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> pieColors = new ArrayList<>();

        if (personalCount > 0) {
            entries.add(new PieEntry(personalCount, "Personal Tasks"));
            pieColors.add(categoryColors.get("Personal Tasks"));
        }
        if (schoolCount > 0) {
            entries.add(new PieEntry(schoolCount, "School Task"));
            pieColors.add(categoryColors.get("School Task"));
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
            pieColors.add(categoryColors.get("Errands and Shopping Tasks"));
        }
        if (healthCount > 0) {
            entries.add(new PieEntry(healthCount, "Health and Fitness"));
            pieColors.add(categoryColors.get("Health and Fitness Tasks"));
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
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelTextSize(0f);
        pieChart.getLegend().setEnabled(true);
        pieChart.setTouchEnabled(false);

        // Refresh the chart
        pieChart.invalidate();
    }

    private void createLineChart(List<Task> tasks) {
        // Map to store the count of completed tasks for each month
        Map<String, Integer> completedTasksByMonth = new HashMap<>();

        // Calculate the count of completed tasks for each month
        for (Task task : tasks) {
            if (task.getStatus() == true) {
                String month = getMonthFromDate(task.getDate());
                if (completedTasksByMonth.containsKey(month)) {
                    int count = completedTasksByMonth.get(month);
                    completedTasksByMonth.put(month, count + 1);
                } else {
                    completedTasksByMonth.put(month, 1);
                }
            }
        }

        // Create line entries
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : completedTasksByMonth.entrySet()) {
            String month = entry.getKey();
            int count = entry.getValue();
            entries.add(new Entry(index, count));
            labels.add(month);
            Log.v("month",month);
            index++;
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

        // Customize x-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        lineChart.getAxisLeft().setEnabled(false);

        xAxis.setLabelCount(labels.size());

        // Refresh the chart
        lineChart.invalidate();
    }



    private String getMonthFromDate(String date) {
        SimpleDateFormat inputFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        SimpleDateFormat outputFormat = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            outputFormat = new SimpleDateFormat("MMM");
        }
        try {
            Date parsedDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                parsedDate = inputFormat.parse(date);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return outputFormat.format(parsedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
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

        // Find the CardView for Average Time
        CardView cardViewAverageTime = findViewById(R.id.cardViewAverageTime);

        // Find the TextView to display the average time
        TextView textViewAverageTime = cardViewAverageTime.findViewById(R.id.textViewAverageTime);

        // Update the text with the calculated average time
        textViewAverageTime.setText("Average Time: " + averageTime + " minutes");
    }
    private void displayAverageTimePerCategory(float personalAvg, float schoolAvg, float assignmentAvg,
                                               float projectsAvg, float errandAvg, float healthAvg) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        if (personalAvg > 0) {
            entries.add(new BarEntry(0, personalAvg));
            barColors.add(categoryColors.get("Personal Tasks"));
            categories.add("Personal Tasks");
        }
        if (schoolAvg > 0) {
            entries.add(new BarEntry(1, schoolAvg));
            barColors.add(categoryColors.get("School Task"));
            categories.add("School Task");
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
            barColors.add(categoryColors.get("Errands and Shopping Tasks"));
            categories.add("Errands and Shopping Tasks");
        }
        if (healthAvg > 0) {
            entries.add(new BarEntry(5, healthAvg));
            barColors.add(categoryColors.get("Health and Fitness Tasks"));
            categories.add("Health and Fitness Tasks");
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

        // Hide X-axis labels
        XAxis xAxis = barChartAverageTimePerCategory.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        // Hide the default legend
        barChartAverageTimePerCategory.getLegend().setEnabled(false);

        // Create a custom legend inside the chart
        LinearLayout legendLayout = findViewById(R.id.legendLayout);

        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            int color = barColors.get(i);

            // Create a TextView for the legend entry
            TextView legendEntry = new TextView(this);
            legendEntry.setText(category);
            legendEntry.setTextColor(color);
            legendEntry.setTextSize(12f);

            // Add some padding between legend entries
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 16, 0);
            legendEntry.setLayoutParams(layoutParams);

            // Add the legend entry to the legend layout
            legendLayout.addView(legendEntry);
        }

        // Refresh the chart
        barChartAverageTimePerCategory.invalidate();
    }


//    private void displayAverageTimePerCategory(float personalAvg, float schoolAvg, float assignmentAvg,
//                                               float projectsAvg, float errandAvg, float healthAvg) {
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<Integer> barColors = new ArrayList<>();
//        ArrayList<String> categories = new ArrayList<>();
//
//        if (personalAvg > 0) {
//            entries.add(new BarEntry(0, personalAvg));
//            barColors.add(categoryColors.get("Personal Tasks"));
//            categories.add("Personal Tasks");
//        }
//        if (schoolAvg > 0) {
//            entries.add(new BarEntry(1, schoolAvg));
//            barColors.add(categoryColors.get("School Task"));
//            categories.add("School Task");
//        }
//        if (assignmentAvg > 0) {
//            entries.add(new BarEntry(2, assignmentAvg));
//            barColors.add(categoryColors.get("Assignments"));
//            categories.add("Assignments");
//        }
//        if (projectsAvg > 0) {
//            entries.add(new BarEntry(3, projectsAvg));
//            barColors.add(categoryColors.get("Projects"));
//            categories.add("Projects");
//        }
//        if (errandAvg > 0) {
//            entries.add(new BarEntry(4, errandAvg));
//            barColors.add(categoryColors.get("Errands and Shopping Tasks"));
//            categories.add("Errands and Shopping Tasks");
//        }
//        if (healthAvg > 0) {
//            entries.add(new BarEntry(5, healthAvg));
//            barColors.add(categoryColors.get("Health and Fitness Tasks"));
//            categories.add("Health and Fitness Tasks");
//        }
//
//        // Create a dataset using the entries
//        BarDataSet dataSet = new BarDataSet(entries, "Average Time Spent (minutes)");
//        dataSet.setColors(barColors);
//        float barWidth = 0.5f;
//
//        // Create a BarData object with the dataset
//        BarData barData = new BarData(dataSet);
//        barData.setBarWidth(barWidth);
//
//        // Get the BarChart reference and set the data
//        BarChart barChartAverageTimePerCategory = findViewById(R.id.barChartAverageTimePerCategory);
//        barChartAverageTimePerCategory.setData(barData);
//        barChartAverageTimePerCategory.getDescription().setEnabled(false);
//        barChartAverageTimePerCategory.setTouchEnabled(false);
//
//        // Hide X-axis labels
//        XAxis xAxis = barChartAverageTimePerCategory.getXAxis();
//        xAxis.setDrawLabels(false);
//        xAxis.setDrawGridLines(false);
//
//        Legend legend = barChartAverageTimePerCategory.getLegend();
//        legend.setEnabled(true);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setXEntrySpace(70f);
//        legend.setYEntrySpace(10f);
//        legend.setTextSize(10f);
//        legend.setWordWrapEnabled(true);
//
//        // Create legend entries for each category
//        List<LegendEntry> legendEntries = new ArrayList<>();
//        for (int i = 0; i < categories.size(); i++) {
//            String category = categories.get(i);
//            LegendEntry entry = new LegendEntry();
//            entry.label = category;
//            entry.formColor = barColors.get(i);
//            legendEntries.add(entry);
//        }
//
//        // Set the legend entries
//        legend.setCustom(legendEntries);
//
//        // Refresh the chart
//        barChartAverageTimePerCategory.invalidate();
//    }







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

        float personalavg = 0;
        float schoolavg = 0;
        float assignmentavg = 0;
        float projectsavg= 0;
        float errandavg = 0;
        float healthavg = 0;



        for (Task task : tasks) {
            String category = task.getCategory();
            float timeSpent = task.getTimespent();

            if (category.equals("Personal Tasks")) {
                personalTime += timeSpent;
                personalCount += 1;
                personalavg = personalTime / personalCount;
            } else if (category.equals("School Task")) {
                schoolTime += timeSpent;
                schoolCount += 1;
                schoolavg = schoolTime/schoolCount;
            } else if (category.equals("Assignments")) {
                assignmentTime += timeSpent;
                assignmentCount += 1;
                assignmentavg = assignmentTime/assignmentCount;
            } else if (category.equals("Projects")) {
                projectsTime += timeSpent;
                projectsCount += 1;
                projectsavg = projectsTime/projectsCount;
            } else if (category.equals("Errands and Shopping Tasks")) {
                errandTime += timeSpent;
                errandCount += 1;
                errandavg = errandTime/errandCount;
            } else if (category.equals("Health and Fitness Tasks")) {
                healthTime += timeSpent;
                healthCount += 1;
                healthavg = healthTime/healthCount;
            }
        }

        createBarChart(personalTime, schoolTime, assignmentTime, projectsTime, errandTime, healthTime);
        createPieChart(personalCount, schoolCount, assignmentCount, projectsCount, errandCount, healthCount);
        createLineChart(tasks);
        displayAverageTime(tasks);
        displayAverageTimePerCategory(personalavg, schoolavg, assignmentavg, projectsavg, errandavg, healthavg);
    }
    }


//public class TaskAnalysis extends AppCompatActivity implements TaskDataHolder.TaskDataCallback {
//    private List<Task> taskList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//        String username = getIntent().getStringExtra("USERNAME");
//
//        TaskDataHolder.getInstance().fetchUserTasks(username, this);
//    }
//
//    private void createBarChart(float personal, float school, float assignment, float projects, float errand, float health) {
//        // Create bar entries
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        List<Integer> pastelColors = new ArrayList<>();
//        pastelColors.add(Color.parseColor("#FFB6C1"));  // Pastel Pink
//        pastelColors.add(Color.parseColor("#FFD700"));  // Pastel Gold
//        pastelColors.add(Color.parseColor("#00FFFF"));  // Pastel Cyan
//        pastelColors.add(Color.parseColor("#98FB98"));  // Pastel Green
//        pastelColors.add(Color.parseColor("#FFA07A"));  // Pastel Salmon
//        pastelColors.add(Color.parseColor("#87CEFA"));  // Pastel Sky Blue
//
//
//        entries.add(new BarEntry(0, personal));
//        entries.add(new BarEntry(1, school));
//        entries.add(new BarEntry(2, assignment));
//        entries.add(new BarEntry(3, projects));
//        entries.add(new BarEntry(4, errand));
//        entries.add(new BarEntry(5, health));
//
//        // Create a dataset using the entries
//        BarDataSet dataSet = new BarDataSet(entries, "Time Spent");
//        dataSet.setColors(pastelColors);
//        float barWidth = 0.5f;
//
//        // Create a BarData object with the dataset
//        BarData barData = new BarData(dataSet);
//        barData.setBarWidth(barWidth);
//
//
//
//        // Get the BarChart reference and set the data
//        BarChart barChart = findViewById(R.id.Barchart1);
//        barChart.setData(barData);
//        barChart.setTouchEnabled(false);
//
//
//        // Hide X-axis labels
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setDrawLabels(false);
//
//        Legend legend = barChart.getLegend();
//        legend.setEnabled(true);
////        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setXEntrySpace(70f);
//        legend.setYEntrySpace(10f);
//
//        legend.setTextSize(10f);
//        legend.setWordWrapEnabled(true);
//
//        String[] categoryNames = {
//                "Personal Tasks",
//                "School Task",
//                "Assignments",
//                "Projects",
//                "Errands and Shopping",
//                "Health and Fitness"
//        };
//
//// Create legend entries for each category
//        List<LegendEntry> legendEntries = new ArrayList<>();
//        for (int i = 0; i < categoryNames.length; i++) {
//            LegendEntry entry = new LegendEntry();
//            entry.label = categoryNames[i];
//            entry.formColor = pastelColors.get(i); // Use the corresponding color from the pastelColors list
//            legendEntries.add(entry);
//        }
//
//// Set the legend entries
//        legend.setCustom(legendEntries);
//
//        // Refresh the chart
//        barChart.invalidate();
//    }
//
//    private void createPieChart(float personalCount, float schoolCount, float assignmentCount, float projectsCount, float errandCount, float healthCount) {
//        // Create pie entries
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        List<Integer> pastelColors = new ArrayList<>();
//        pastelColors.add(Color.parseColor("#FFB6C1"));  // Pastel Pink
//        pastelColors.add(Color.parseColor("#FFD700"));  // Pastel Gold
//        pastelColors.add(Color.parseColor("#00FFFF"));  // Pastel Cyan
//        pastelColors.add(Color.parseColor("#98FB98"));  // Pastel Green
//        pastelColors.add(Color.parseColor("#FFA07A"));  // Pastel Salmon
//        pastelColors.add(Color.parseColor("#87CEFA"));  // Pastel Sky Blue
//
//        if (personalCount > 0) {
//            entries.add(new PieEntry(personalCount, "Personal Tasks"));
//        }
//        if (schoolCount > 0) {
//            entries.add(new PieEntry(schoolCount, "School Task"));
//        }
//        if (assignmentCount > 0) {
//            entries.add(new PieEntry(assignmentCount, "Assignments"));
//        }
//        if (projectsCount > 0) {
//            entries.add(new PieEntry(projectsCount, "Projects"));
//        }
//        if (errandCount > 0) {
//            entries.add(new PieEntry(errandCount, "Errands and Shopping"));
//        }
//        if (healthCount > 0) {
//            entries.add(new PieEntry(healthCount, "Health and Fitness"));
//        }
//
//        // Create a dataset using the entries
//        PieDataSet dataSet = new PieDataSet(entries, "Task Categories");
//        dataSet.setColors(pastelColors);
//        dataSet.setValueTextColor(Color.BLACK);
//        dataSet.setValueTextSize(12f);
//
//        // Create a PieData object with the dataset
//        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
//        pieData.setValueTextSize(12f);
//        pieData.setValueTextColor(Color.BLACK);
//
//        // Get the PieChart reference and set the data
//        PieChart pieChart = findViewById(R.id.pieChart);
//        pieChart.setData(pieData);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setDrawEntryLabels(false);
//        pieChart.setEntryLabelTextSize(0f);
//        pieChart.getLegend().setEnabled(true);
//        pieChart.setTouchEnabled(false);
//
//        // Refresh the chart
//        pieChart.invalidate();
//    }
//
//
//
//
//    @Override
//    public void onTaskDataFetched(List<Task> tasks) {
//        taskList = tasks;
//        float personalTime = 0;
//        float schoolTime = 0;
//        float assignmentTime = 0;
//        float projectsTime = 0;
//        float errandTime = 0;
//        float healthTime = 0;
//
//        float personalcount = 0;
//        float schoolcount = 0;
//        float assignmentcount = 0;
//        float projectscount = 0;
//        float errandcount = 0;
//        float healthcount = 0;
//
//        for (Task task : tasks) {
//            String category = task.getCategory();
//            float timeSpent = task.getTimespent();
//
//            if (category.equals("Personal Tasks")) {
//                personalTime += timeSpent;
//                personalcount += 1;
//            } else if (category.equals("School Task")) {
//                schoolTime += timeSpent;
//                schoolcount+=1;
//            } else if (category.equals("Assignments")) {
//                assignmentTime += timeSpent;
//                assignmentcount+= 1;
//            } else if (category.equals("Projects")) {
//                projectsTime += timeSpent;
//                personalcount+=1;
//            } else if (category.equals("Errands and Shopping Tasks")) {
//                errandTime += timeSpent;
//                errandcount+=1;
//            } else if (category.equals("Health and Fitness Tasks")) {
//                healthTime += timeSpent;
//                healthcount+= 1;
//            }
//        }
//
//        createBarChart(personalTime, schoolTime, assignmentTime, projectsTime, errandTime, healthTime);
//        createPieChart(personalcount, schoolcount, assignmentcount, projectscount, errandcount, healthcount);
//    }
//
//
//}





//public class TaskAnalysis extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//        String username = getIntent().getStringExtra("USERNAME");
//
//        final float[] personal = {0};
//        final float[] school = {0};
//        final float[] assignment = {0};
//        final float[] projects = {0};
//        final float[] errand = {0};
//        final float[] health = {0};
//
//        DatabaseReference userTask = FirebaseDatabase.getInstance().getReference("Task");
//        userTask.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
//                        for (DataSnapshot taskSnapshot : categorySnapshot.getChildren()) {
//                            Task task = taskSnapshot.getValue(Task.class);
//                            String category = task.getCategory();
//                            float timeSpent = task.getTimespent();
//                            if (category.equals("Personal Tasks")) {
//                                personal[0] += timeSpent;
//                            } else if (category.equals("School Task")) {
//                                school[0] += timeSpent;
//                            } else if (category.equals("Assignments")) {
//                                assignment[0] += timeSpent;
//                            } else if (category.equals("Projects")) {
//                                projects[0] += timeSpent;
//                            } else if (category.equals("Errands and Shopping Tasks")) {
//                                errand[0] += timeSpent;
//                            } else if (category.equals("Health and Fitness Tasks")) {
//                                health[0] += timeSpent;
//                            }
//                        }
//                    }
//
//                    // Create bar entries
//                    ArrayList<BarEntry> entries = new ArrayList<>();
//                    entries.add(new BarEntry(0, personal[0]));
//                    entries.add(new BarEntry(1, school[0]));
//                    entries.add(new BarEntry(2, assignment[0]));
//                    entries.add(new BarEntry(3, projects[0]));
//                    entries.add(new BarEntry(4, errand[0]));
//                    entries.add(new BarEntry(5, health[0]));
//                    Log.v("List" , personal[0]+ "");
//
//                    // Create a dataset using the entries
//                    BarDataSet dataSet = new BarDataSet(entries, "Time Spent");
//
//                    // Customize the appearance of the dataset
//                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//                    // Create a BarData object with the dataset
//                    BarData barData = new BarData(dataSet);
//
//                    // Get the BarChart reference and set the data
//                    HorizontalBarChart barChart = findViewById(R.id.Barchart1);
//                    barChart.setData(barData);
//
//                    // Customize the appearance of the X-axis labels
//                    List<String> labels = Arrays.asList(
//                            "Personal Tasks",
//                            "School Task",
//                            "Assignments",
//                            "Projects",
//                            "Errands and Shopping Tasks",
//                            "Health and Fitness Tasks"
//                    );
//                    XAxis xAxis = barChart.getXAxis();
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                    // Refresh the chart
//                    barChart.invalidate();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Handle error case
//            }
//        });
//    }
//}


//public class TaskAnalysis extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//
//
//        HorizontalBarChart barChart = findViewById(R.id.Barchart1);
//
//        DatabaseReference userTask = FirebaseDatabase.getInstance().getReference("Task");
//        userTask.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    HashMap<String, Float> categoryTimeMap = new HashMap<>();
//                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                        for (DataSnapshot taskSnapshot : categorySnapshot.getChildren()) {
//                            Task task = taskSnapshot.getValue(Task.class);
//                            String category = task.getCategory();
//                            float timeSpent = task.getTimespent();
//
//                            if (categoryTimeMap.containsKey(category)) {
//                                float totalTimeSpent = categoryTimeMap.get(category) + timeSpent;
//                                categoryTimeMap.put(category, totalTimeSpent);
//                            } else {
//                                categoryTimeMap.put(category, timeSpent);
//                            }
//                        }
//                    }
//
//                    // Create bar entries from categoryTimeMap
//                    ArrayList<BarEntry> entries = new ArrayList<>();
//                    int index = 0;
//                    for (Map.Entry<String, Float> entry : categoryTimeMap.entrySet()) {
//                        String category = entry.getKey();
//                        float totalTimeSpent = entry.getValue();
//                        entries.add(new BarEntry(index, totalTimeSpent));
//                        index++;
//                    }
//
//                    // Create a dataset using the entries
//                    BarDataSet dataSet = new BarDataSet(entries, "Time Spent");
//
//                    // Customize the appearance of the dataset
//                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//                    // Create a BarData object with the dataset
//                    BarData barData = new BarData(dataSet);
//
//                    // Set the bar chart data
//                    barChart.setData(barData);
//
//                    // Customize the appearance of the X-axis labels
//                    List<String> labels = new ArrayList<>(categoryTimeMap.keySet());
//                    XAxis xAxis = barChart.getXAxis();
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                    // Refresh the chart
//                    barChart.invalidate();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error case
//            }
//        });
//    }
//}




//
//public class TaskAnalysis extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//        float personal =0;
//        float school =0;
//        float assignement = 0;
//        float projects =0;
//        float errand =0;
//        float tasks =0;
//        DatabaseReference userTask = FirebaseDatabase.getInstance().getReference("Task");
//        userTask.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot snapshot) {
//                if (snapshot.exists()){
////                    float personal =0;
////                    float school =0;
////                    float assignement = 0;
////                    float projects =0;
////                    float errand =0;
////                    float health =0;
//                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
//                        for (DataSnapshot taskSnapshot : categorySnapshot.getChildren()) {
//                            Task task = taskSnapshot.getValue(Task.class);
//                            String category = task.getCategory();
//                            float timeSpent = task.getTimespent();
//
//                            if (category == "Personal Tasks"){
//                                personal += timeSpent;
//                            }
//
//                            else if(category == "School Task"){
//                                school+= timeSpent;
//                            }
//                            else if(category == "Assignments"){
//                                assignement += timeSpent;
//                            }
//                            else if(category =="Projects"){
//                                projects += timeSpent;
//                            }
//                            else if(category == "Errands and Shopping Tasks"){
//                                errand+= timeSpent;
//                            }
//                            else if(category == "Health and Fitness Tasks"){
//                                health += timeSpent;
//                            }
//                        }}
//
//                }
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//        HorizontalBarChart barChart = findViewById(R.id.Barchart1);
//
//        userTask = FirebaseDatabase.getInstance().getReference("Task");
//
//
//        // Create bar entries
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0, 20));
//        entries.add(new BarEntry(1, 30));
//        entries.add(new BarEntry(2, 10));
//        entries.add(new BarEntry(3, 50));
//
//        // Create a dataset using the entries
//        BarDataSet dataSet = new BarDataSet(entries, "Bar Dataset");
//
//        // Customize the appearance of the dataset
//        dataSet.setColor(Color.BLUE);
//
//        // Create a BarData object with the dataset
//        BarData barData = new BarData(dataSet);
//
//        // Set the bar chart data
//        barChart.setData(barData);
//
//        // Customize the appearance of the X-axis labels
//        List<String> labels = Arrays.asList("Label 1", "Label 2", "Label 3", "Label 4");
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        // Refresh the chart
//        barChart.invalidate();
//    }
//}




//public class TaskAnalysis extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//        BarChart barChart = findViewById(R.id.Barchart1);
//        DatabaseReference userTaskRef = FirebaseDatabase.getInstance().getReference("Task");
//        userTaskRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    HashMap<String, Float> categoryTimeMap = new HashMap<>();
//                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
//                        Task task = taskSnapshot.getValue(Task.class);
//                        String category = task.getCategory();
//                        float timeSpent = task.getTimespent();
//                        float totalTimespent = 0;
//
//                        if (categoryTimeMap.containsKey(category)) {
//                            totalTimespent += timeSpent;
//                            categoryTimeMap.put(category, totalTimespent);
//                        } else {
//                            categoryTimeMap.put(category, timeSpent);
//                        }
//                    }
//
//                    // Process the categoryTimeMap and create the bar chart
//                    ArrayList<BarEntry> entries = new ArrayList<>();
//                    ArrayList<String> labels = new ArrayList<>();
//                    int index = 0;
//                    for (Map.Entry<String, Float> entry : categoryTimeMap.entrySet()) {
//                        String category = entry.getKey();
//                        float totalTimeSpent = entry.getValue();
//
//                        entries.add(new BarEntry(index, totalTimeSpent));
//                        labels.add(category);
//
//                        index++;
//                    }
//
//                    BarDataSet dataSet = new BarDataSet(entries, "Total Time Spent");
//                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//                    BarData barData = new BarData(dataSet);
//
//                    // Customize the appearance of the bar graph
//                    barData.setBarWidth(0.5f);
//                    barData.setValueTextSize(12f);
//                    barChart.setFitBars(true);
//                    barChart.setData(barData);
//                    barChart.getDescription().setEnabled(false);
//
//                    // Customize the x-axis labels
//                    XAxis xAxis = barChart.getXAxis();
//                    xAxis.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            int intValue = (int) value;
//                            if (intValue >= 0 && intValue < labels.size()) {
//                                return labels.get(intValue);
//                            }
//                            return "";
//                        }
//                    });
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setGranularity(1f);
//                    xAxis.setGranularityEnabled(true);
//                    xAxis.setLabelCount(labels.size());
//
//                    // Customize the y-axis labels
//                    YAxis yAxisLeft = barChart.getAxisLeft();
//                    YAxis yAxisRight = barChart.getAxisRight();
//                    yAxisLeft.setGranularity(1f);
//                    yAxisRight.setGranularity(1f);
//
//                    // Animate and refresh the bar graph
//                    barChart.animateY(1000);
//                    barChart.invalidate();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error case
//            }
//        });
//    }
//}




//public class TaskAnalysis extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_analysis);
//        BarChart barChart = findViewById(R.id.Barchart1);
//        // Step 1: Retrieve tasks from Firebase Realtime Database
//        DatabaseReference userTaskRef;
//        userTaskRef = FirebaseDatabase.getInstance().getReference("Task");
//        userTaskRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Step 2: Calculate total time spent for each category
//                    HashMap<String, Float> categoryTimeMap = new HashMap<>();
//                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
//                        Task task = taskSnapshot.getValue(Task.class);
//                        String category = task.getCategory();
//                        float timeSpent = task.getTimespent();
//
//                        if (categoryTimeMap.containsKey(category)) {
//                            float totalTimeSpent = categoryTimeMap.get(category) + timeSpent;
//                            categoryTimeMap.put(category, totalTimeSpent);
//                        } else {
//                            categoryTimeMap.put(category, timeSpent);
//                        }
//                    }
//
//                    // Step 3: Use the data structure to plot a bar graph
//                    // Here, you can use the categoryTimeMap to plot the bar graph
//                    // You can access the category names and their respective total time spent values
//
//                    // Example usage:
//                    for (Map.Entry<String, Float> entry : categoryTimeMap.entrySet()) {
//                        String category = entry.getKey();
//                        float totalTimeSpent = entry.getValue();
//                        // Use the category and totalTimeSpent to plot the bar graph
//                        // ...
//                        Log.v("CategoryTime", category + ": " + totalTimeSpent);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error case
//            }
//        });
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<String> labels = new ArrayList<>();
//        int index = 0;
//
//        for (Map.Entry<String, Float> entry : categoryTimeMap.entrySet()) {
//            String category = entry.getKey();
//            float totalTimeSpent = entry.getValue();
//
//            entries.add(new BarEntry(index, totalTimeSpent));
//            labels.add(category);
//
//            index++;
//        }
//
//        BarDataSet dataSet = new BarDataSet(entries, "Total Time Spent");
//        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        BarData barData = new BarData(dataSet);
//
//// Customize the appearance of the bar graph
//        barData.setBarWidth(0.5f);
//        barData.setValueTextSize(12f);
//        barChart.setFitBars(true);
//        barChart.setData(barData);
//        barChart.getDescription().setEnabled(false);
//
//// Customize the x-axis labels
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int intValue = (int) value;
//                if (intValue >= 0 && intValue < labels.size()) {
//                    return labels.get(intValue);
//                }
//                return "";
//            }
//        });
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setLabelCount(labels.size());
//
//// Customize the y-axis labels
//        YAxis yAxisLeft = barChart.getAxisLeft();
//        YAxis yAxisRight = barChart.getAxisRight();
//        yAxisLeft.setGranularity(1f);
//        yAxisRight.setGranularity(1f);
//
//// Animate and refresh the bar graph
//        barChart.animateY(1000);
//        barChart.invalidate();
//
//
//
//    }
//}