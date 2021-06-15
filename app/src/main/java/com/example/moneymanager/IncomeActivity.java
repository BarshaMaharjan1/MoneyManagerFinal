package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;
import com.example.moneymanager.viewmodel.AppViewModel;

import java.util.Calendar;
import java.util.List;

public class IncomeActivity extends AppCompatActivity {
    Toolbar toolbarA;
    RecyclerView recyclerView;
    AppViewModel mAppViweModel;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        recyclerView = findViewById(R.id.recylcerview);
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);
        mAppViweModel.retrieveManager("income").observe(this, new Observer<List<Manager>>() {
            @Override
            public void onChanged(List<Manager> managers) {
                adapter = new RecyclerViewAdapter(IncomeActivity.this, managers, new RecyclerViewAdapter.RecyclerCallback() {
                    @Override
                    public void onAddClicked(int id, String type) {
                        Intent intent = new Intent(IncomeActivity.this,Categories.class);
                        intent.putExtra("id",id);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClicked(Manager manager) {
                        openDataEntryPoint(manager);
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(IncomeActivity.this, 4, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });


        toolbarA = findViewById(R.id.Ctoolbar);
        TextView toolbarTitle = findViewById(R.id.titletxt);
        TextView toolbarCount = findViewById(R.id.titleCount);

        toolbarTitle.setText("Income");
        toolbarCount.setText("");
        setSupportActionBar(toolbarA);
    }

    AppCompatTextView txtCal, title, txtCancel, txtConfirm;
    AppCompatEditText edtAmt;
    AppCompatImageView imgIcon;
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
        title.setText("Enter your Expenses.");
        imgIcon.setImageBitmap(Converter.getImageFromBase64Blob(manager.getImage()));

        txtCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(IncomeActivity.this, (datePicker, y, m, d) -> {
                    String month1 = m < 10 ? "0"+m : m+"";
                    String day1 = d < 10 ? "0"+d : d+"";
                    txtCal.setText(y+"-"+ month1 +"-"+ day1);
                },year,month,day);
                datePickerDialog.show();
            }
        });

        txtCancel.setOnClickListener(view1 -> dialog.dismiss());

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = edtAmt.getText().toString();
                String date = txtCal.getText().toString();
                if(!amount.isEmpty() && !date.isEmpty()){
                    Records records = new Records(0,manager.getId(),amount,date,manager.getType(),date.substring(5,7));
                    long saved = mAppViweModel.saveRecords(records);
                    Log.e("records",saved+"");
                    if(saved == 1){
                        Toast.makeText(IncomeActivity.this, "Income for "+manager.getType()+" saved.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.categories_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expenses:
                Intent intent = new Intent(this, IncomeExpensesActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Expenses", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.income:
                Intent intent1 = new Intent(this, IncomeActivity.class);
                startActivity(intent1);

                Toast.makeText(this, "Income", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}