package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;
import com.example.moneymanager.viewmodel.AppViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    NavigationView nav_view;
    Toolbar toolbar;
    TextView info;
    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private RecyclerView recyclerview;
    private AppViewModel mAppViweModel;
    private Utils utils;
    private AppCompatTextView amtCapital, amtDate;
    private RecordsAdapter adapter;
    private String[] months = {"Default", "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private List<Records> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        utils = new Utils(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("529122432788-s4mg646qqhrhtkv6jd98osrmjatpgp9k.apps.googleusercontent.com")
                .build();

        //google sign client where client is passing this for configure the request "gso".
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, IncomeExpensesActivity.class);
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        info = findViewById(R.id.info);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerview = findViewById(R.id.recyclerview);
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        amtCapital = findViewById(R.id.amtCapital);
        amtDate = findViewById(R.id.amtDate);
    }

    private void fetchRecords() {
        mAppViweModel.fetchAllRecords().observe(this, new Observer<List<Records>>() {
            @Override
            public void onChanged(List<Records> records) {
                recordsList = records;
                adapter = new RecordsAdapter(records, new RecordsAdapter.RecordsCallback() {
                    @Override
                    public void updateItemData(AppCompatTextView title, AppCompatImageView icon, Records records, int pos) {
                        Manager manager = mAppViweModel.fetchManagerDetails(records.getManagerId());
                        title.setText(manager.getTitle());
                        icon.setImageBitmap(Converter.getImageFromBase64Blob(manager.getImage()));
                        if (manager.getType().equalsIgnoreCase("income")) {
                            title.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark));
                        } else {
                            title.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
                        }
                    }

                    @Override
                    public void onMenuSelected(Records records, RecyclerView.ViewHolder menu) {
                        PopupMenu popupMenu = new PopupMenu(Dashboard.this, menu.itemView);
                        popupMenu.getMenu().add("Update");
                        popupMenu.getMenu().add("Delete");
                        popupMenu.show();

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getTitle().equals("Update")) {
                                    showUpdateDialog(records);
                                } else {
                                    float finalAmt = 0;
                                    Records records1 = mAppViweModel.fetchInitialCapital();
                                    if (records.getType().equalsIgnoreCase("Income"))
                                        finalAmt = Float.parseFloat(records1.getAmount()) - Float.parseFloat(records.getAmount());
                                    else
                                        finalAmt = Float.parseFloat(records1.getAmount()) + Float.parseFloat(records.getAmount());

                                    mAppViweModel.updateCapitalBudget(new Records(records1.getId(), records1.getManagerId(), String.valueOf(finalAmt), records1.getDate(), records1.getType(), records1.getMonth()));
                                    mAppViweModel.deleteRecords(records);
                                    displayNotification(records);
                                }
                                return false;
                            }
                        });
                    }

                });
                recyclerview.setAdapter(adapter);
            }
        });
    }

    private void showUpdateDialog(Records records) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.update_record_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        AppCompatEditText edtAmt = dialog.findViewById(R.id.edtAmount);
        AppCompatTextView txtCal = dialog.findViewById(R.id.txtCal);
        AppCompatTextView txtConfirm = dialog.findViewById(R.id.txtConfirm);
        AppCompatTextView txtCancel = dialog.findViewById(R.id.txtCancel);
        AppCompatTextView title = dialog.findViewById(R.id.title);
        AppCompatImageView imgIcon = dialog.findViewById(R.id.imgIcon);
        title.setText("Update your " + records.getType());

        imgIcon.setImageBitmap(Converter.getImageFromBase64Blob(mAppViweModel.fetchImageByte(records.getManagerId())));
        edtAmt.setText(Editable.Factory.getInstance().newEditable(records.getAmount() + ""));
        txtCal.setText(Editable.Factory.getInstance().newEditable(records.getDate()));

        txtCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Dashboard.this, (datePicker, y, m, d) -> {
                    m++;
                    String month1 = m < 10 ? "0" + m : m + "";
                    String day1 = d < 10 ? "0" + d : d + "";
                    txtCal.setText(y + "-" + month1 + "-" + day1);
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        txtConfirm.setOnClickListener(view1 -> {
            mAppViweModel.updateRecords(new Records(records.getId(), records.getManagerId(), edtAmt.getText().toString(), txtCal.getText().toString(), records.getType(), records.getMonth()));
            dialog.dismiss();
        });

        txtCancel.setOnClickListener(view12 -> dialog.dismiss());

    }

    private void fetchInitialBudget() {
        mAppViweModel.fetchInitialBudget().observe(this, new Observer<Records>() {
            @Override
            public void onChanged(Records records) {
                if (records != null) {
                    amtCapital.setText(records.getAmount());
                    amtDate.setText(records.getDate());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (utils.isLoggedInFirstTime()) {
            openInitialBudgetDialog();
        }
        fetchInitialBudget();
        fetchRecords();
    }

    private void openInitialBudgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.initial_budget_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        AppCompatEditText edtBudget = dialog.findViewById(R.id.edtBudget);
        AppCompatTextView confirm = dialog.findViewById(R.id.confirm);
        AppCompatTextView skip = dialog.findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = R.drawable.ic_piggy_bank;
                mAppViweModel.saveRecords(new Records(0, 0,
                        "0.0", "", "initial", ""));
                dialog.dismiss();
                utils.saveFirstLogin(false);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtBudget.getText().toString().isEmpty()) {
                    Toast.makeText(Dashboard.this, "No amount inserted.", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int m = calendar.get(Calendar.MONTH) + 1;
                    int d = calendar.get(Calendar.DAY_OF_MONTH);
                    String month = m < 10 ? "0" + m : "" + m;
                    String day = d < 10 ? "0" + d : "" + d;
                    mAppViweModel.saveRecords(new Records(0, 0,
                            edtBudget.getText().toString(), year + "-" + month + "-" + day, "initial", month));
                    Toast.makeText(Dashboard.this, "Initial Budget Established", Toast.LENGTH_SHORT).show();
                    utils.saveFirstLogin(false);
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calendar) {
            //displayCalendar();
            displayMonths();
            return true;

        } else if (id == R.id.logout) {

            signOut();
            Intent intent = new Intent(Dashboard.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void displayMonths() {
        PopupMenu popupMenu = new PopupMenu(this, toolbar);
        popupMenu.setGravity(GravityCompat.END);
        for (String val : months) {
            popupMenu.getMenu().add(val);
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String value = (String) menuItem.getTitle();
                for (int i = 0; i < months.length; i++) {
                    if (months[i].equalsIgnoreCase(value)) {
                        String month = i < 10 ? "0" + i : i + "";
                        fetchRecordsFromPos(month);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void fetchRecordsFromPos(String month) {
        if (recordsList != null) {
            if (month.equalsIgnoreCase("00")) {
                adapter.setFilteredValue(recordsList);
            } else {
                filterByDate(month);
            }

        }
    }
    String updateDate;
    private void displayCalendar() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, y, m, d) -> {
            m++;
            String month1 = m < 10 ? "0" + m : m + "";
            String day1 = d < 10 ? "0" + d : d + "";
            updateDate =  y + "-" + month1 + "-" + day1;
        }, year, month, day);
        datePickerDialog.show();
    }


    private void filterByDate(String date) {
        mAppViweModel.filterRecordsByDate(date).observe(this, new Observer<List<Records>>() {
            @Override
            public void onChanged(List<Records> records) {
                adapter.setFilteredValue(records);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void signOut() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Dashboard.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawer();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.chart:
                intent = new Intent(Dashboard.this, GraphActivity.class);
                startActivity(intent);
                break;
            case R.id.categories:
                intent = new Intent(Dashboard.this, Categories.class);
                startActivity(intent);
                break;


        }
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        }
        super.onBackPressed();
    }

    int counter = 0;

    public void displayNotification(Records records) {
        counter++;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification_large);

        contentViewBig.setTextViewText(R.id.txtTitle, "Records deleted");
        contentViewBig.setTextViewText(R.id.txtDesc, records.getType() + " for Rs." + records.getAmount() + " has been deleted.");

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