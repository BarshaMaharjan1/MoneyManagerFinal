package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;
import com.example.moneymanager.viewmodel.AppViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    private AppViewModel mAppViweModel;
    private HashMap<Integer, List<Records>> recordsByGroup;
    private BarChart barChart;
    private LineChart lineChart;
    private PieChart pieChart;
    private AppCompatTextView titleBar, titleLine, titlePie;
    private AppCompatImageView menuBar, menuPie, menuLine;
    private ArrayList<Manager> managerLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);
        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);

        titleBar = findViewById(R.id.titleBar);
        titleLine = findViewById(R.id.titleLine);
        titlePie = findViewById(R.id.titlePie);

        menuBar = findViewById(R.id.menuBar);
        menuPie = findViewById(R.id.menuPie);
        menuLine = findViewById(R.id.menuLine);

        menuBar.setOnClickListener(this);
        menuPie.setOnClickListener(this);
        menuLine.setOnClickListener(this);
    }

    private void fetchRecordsAndManageGroup() {
        recordsByGroup = new HashMap<>();
        managerLists = new ArrayList<>();
        List<Integer> managerIds = mAppViweModel.fetchDistinctManagerId();
        for (int managerId : managerIds) {
            List<Records> records = mAppViweModel.fetchRecordsByManagerId(managerId);
            recordsByGroup.put(managerId, records);

            Manager manager = mAppViweModel.fetchManagerDataFromManagerId(managerId);
            if (manager != null)
                managerLists.add(manager);
        }

        if(managerIds.size() > 0){
            loadBarChartData(managerIds.get(0));
            loadPieChartData(managerIds.get(0));
            loadLineChartData(managerIds.get(0));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRecordsAndManageGroup();
    }

    @Override
    public void onClick(View view) {
        PopupMenu popupMenu;
        switch (view.getId()) {
            case R.id.menuPie:
                popupMenu = new PopupMenu(this, menuPie);
                addItems(popupMenu, 2);
                break;
            case R.id.menuBar:
                popupMenu = new PopupMenu(this, menuBar);
                addItems(popupMenu, 1);
                break;
            case R.id.menuLine:
                popupMenu = new PopupMenu(this, menuLine);
                addItems(popupMenu, 3);
                break;
        }
    }

    private void addItems(PopupMenu popupMenu, int position) {
        for (Manager manager : managerLists) {
            popupMenu.getMenu().add(manager.getTitle());
            popupMenu.show();
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (position == 1) {
                    titleBar.setText(title);
                    findManagerIdForBar(title);
                } else if (position == 2) {
                    titlePie.setText(title);
                    findManagerIdForPieChart(title);
                } else {
                    titleLine.setText(title);
                    findManagerIdForLineChart(title);
                }
                return false;
            }
        });
    }

    private void findManagerIdForPieChart(String title) {
        for (Manager manager : managerLists) {
            if (manager.getTitle().equalsIgnoreCase(title)) {
                loadPieChartData(manager.getId());
            }
        }
    }

    private void loadPieChartData(int id) {
        List<Records> recordsForPie = recordsByGroup.get(id);
        ArrayList datas = new ArrayList();
        ArrayList dates = new ArrayList();
        for (int i = 0; i < recordsForPie.size(); i++) {
            datas.add(new PieEntry(Float.parseFloat(recordsForPie.get(i).getAmount()), i));
            dates.add(recordsForPie.get(i).getDate());
        }
        PieDataSet pieDataSet = new PieDataSet(datas, "Amounts");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        pieChart.animateXY(1000, 1000);
    }

    private void findManagerIdForLineChart(String title) {
        for (Manager manager : managerLists) {
            if (manager.getTitle().equalsIgnoreCase(title)) {
                loadLineChartData(manager.getId());
            }
        }
    }

    private void loadLineChartData(int id) {
        List<Records> recordsForLine = recordsByGroup.get(id);
        ArrayList datas = new ArrayList();
        ArrayList dates = new ArrayList();
        for (int i = 0; i < recordsForLine.size(); i++) {
            datas.add(new Entry(i, Float.parseFloat(recordsForLine.get(i).getAmount())));
            dates.add(recordsForLine.get(i).getDate());
        }
        XAxis axis = lineChart.getXAxis();
        axis.setPosition(XAxis.XAxisPosition.TOP);
        ValueFormatter formatter = new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                return (String) dates.get((int) value);
            }
        };
        axis.setGranularity(1f); // minimum axis-step (interval) is 1
        axis.setValueFormatter(formatter);
        LineDataSet lineDataSet = new LineDataSet(datas, "Amounts");
        lineChart.animateY(1000);
        lineDataSet.setColor(Color.GREEN);
        LineData lineData = new LineData(lineDataSet);
        if (dates.size() > 1)
            lineChart.setData(lineData);
        else
            Toast.makeText(this, "Insufficient datas", Toast.LENGTH_SHORT).show();
    }

    private void findManagerIdForBar(String title) {
        for (Manager manager : managerLists) {
            if (manager.getTitle().equalsIgnoreCase(title)) {
                loadBarChartData(manager.getId());
            }
        }
    }

    private void loadBarChartData(int id) {
        List<Records> recordsForBar = recordsByGroup.get(id);
        ArrayList datas = new ArrayList();
        ArrayList dates = new ArrayList();
        for (int i = 0; i < recordsForBar.size(); i++) {
            datas.add(new BarEntry(i, Float.parseFloat(recordsForBar.get(i).getAmount())));
            dates.add(recordsForBar.get(i).getDate());
        }
        XAxis axis = barChart.getXAxis();
        axis.setPosition(XAxis.XAxisPosition.TOP);
        ValueFormatter formatter = new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                return (String) dates.get((int) value);
            }
        };
        axis.setGranularity(1f); // minimum axis-step (interval) is 1
        axis.setValueFormatter(formatter);
        BarDataSet bardataset = new BarDataSet(datas, "Amounts");
        barChart.animateY(1000);
        BarData data = new BarData(bardataset);
        data.setBarWidth(0.4f);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(data);
    }
}