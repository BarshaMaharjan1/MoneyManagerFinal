package com.example.moneymanager;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;
import com.example.moneymanager.viewmodel.AppViewModel;

import java.util.Calendar;
import java.util.List;


public class IncomeExpensesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    AppViewModel mAppViweModel;
    LinearLayoutCompat layoutAmount;
    AppCompatTextView txtCal, title, txtCancel, txtConfirm;
    AppCompatEditText edtAmt;
    AppCompatImageView imgIcon;
    AppCompatSpinner spinnerType;

    String[] type = {"Expenses", "Income"};
    private String stringType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        recyclerView = findViewById(R.id.recylcerview);
        layoutAmount = findViewById(R.id.layoutAmount);
        spinnerType = findViewById(R.id.spinnerType);
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);

        spinnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type));
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stringType = adapterView.getSelectedItem().toString();
                fetchItemBasedOnSelection(stringType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void fetchItemBasedOnSelection(String value) {

        mAppViweModel.retrieveManager(value.toLowerCase()).observe(this, new Observer<List<Manager>>() {
            @Override
            public void onChanged(List<Manager> managers) {
                adapter = new RecyclerViewAdapter(IncomeExpensesActivity.this, managers, new RecyclerViewAdapter.RecyclerCallback() {
                    @Override
                    public void onAddClicked(int id, String type) {
                        Intent intent = new Intent(IncomeExpensesActivity.this, Categories.class);
                        intent.putExtra("id", id);
                        intent.putExtra("type", type);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClicked(Manager manager) {
                        openDataEntryPoint(manager);
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(IncomeExpensesActivity.this, 4, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);

            }
        });
    }

    private void openDataEntryPoint(Manager manager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_amount, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        edtAmt = dialog.findViewById(R.id.edtAmount);
        txtCal = dialog.findViewById(R.id.txtCal);
        imgIcon = dialog.findViewById(R.id.imgIcon);
        title = dialog.findViewById(R.id.title);
        txtCancel = dialog.findViewById(R.id.txtCancel);
        txtConfirm = dialog.findViewById(R.id.txtConfirm);
        title.setText("Enter your " + stringType);
        imgIcon.setImageBitmap(Converter.getImageFromBase64Blob(manager.getImage()));

        txtCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(IncomeExpensesActivity.this, (datePicker, y, m, d) -> {
                    m++;
                    String month1 = m < 10 ? "0" + m : m + "";
                    String day1 = d < 10 ? "0" + d : d + "";
                    txtCal.setText(y + "-" + month1 + "-" + day1);
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        txtCancel.setOnClickListener(view1 -> dialog.dismiss());

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = edtAmt.getText().toString();
                String date = txtCal.getText().toString();
                if (!amount.isEmpty() && !date.isEmpty()) {
                    Records records = new Records(0, manager.getId(), amount, date, manager.getType(),date.substring(5,7));
                    mAppViweModel.saveRecords(records);
                    displayNotification(records);
                    Toast.makeText(IncomeExpensesActivity.this, "Amount for " + manager.getType() + " saved.", Toast.LENGTH_SHORT).show();
                    updateCapitalAmount(amount, manager.getType());
                    dialog.dismiss();
                    onBackPressed();
                }
            }
        });
    }

    private void updateCapitalAmount(String amount, String type) {
        Records records = mAppViweModel.fetchInitialCapital();
        double capital = Double.parseDouble(records.getAmount());
        double amt = Double.parseDouble(amount);
        double finalAmt = 0.0;
        if (type.equalsIgnoreCase("income")) {
            finalAmt = capital + amt;
        } else {
            finalAmt = capital - amt;
        }
        mAppViweModel.updateCapitalBudget(new Records(records.getId(), records.getManagerId(), String.valueOf(finalAmt), records.getDate(), records.getType(),records.getMonth()));


    }

    public void add(View view) {
        Intent intent = new Intent(IncomeExpensesActivity.this, Categories.class);
        startActivity(intent);
    }

    int counter = 0;
    public void displayNotification(Records records) {
        counter++;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification_large);

        contentViewBig.setTextViewText(R.id.txtTitle, "Records Created");
        contentViewBig.setTextViewText(R.id.txtDesc, records.getType()+" for Rs."+records.getAmount()+" has been inserted.");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_piggy_bank);
        Intent intent = new Intent(this, IncomeExpensesActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setCustomBigContentView(contentViewBig)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Default channel",
                        NotificationManager.IMPORTANCE_DEFAULT);

                manager.createNotificationChannel(channel);
            }
            manager.notify(counter, builder.build());
        }
    }
}