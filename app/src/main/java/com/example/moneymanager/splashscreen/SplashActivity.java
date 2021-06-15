package com.example.moneymanager.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.moneymanager.Converter;
import com.example.moneymanager.Dashboard;
import com.example.moneymanager.MainActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.Utils;
import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    List<String> titles;
    List<Integer> images;
    List<String> titles1;
    List<Integer> images1;
    List<Manager> managers;
    AppViewModel mAppViweModel;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        utils = new Utils(this);
        titles = new ArrayList<>();
        images = new ArrayList<>();
        titles1 = new ArrayList<>();
        images1 = new ArrayList<>();
        managers = new ArrayList<>();
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);
        imagesArray();
        imagesIncome();
        if(!utils.isIconInserted()) {

            addDb();
            addIncome();

            utils.iconsInserted(true);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (utils.getUserId().isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                Intent intent = new Intent(SplashActivity.this, Dashboard.class);
                startActivity(intent);
                finish();

            }
        }, 2500);
    }

    public void addDb() {
        for (int i = 0; i < images.size(); i++) {
            Manager manager = new Manager(0, Converter.getBase64Bytes(images.get(i), this), titles.get(i), "expenses");
//            managers.add(manager);
            mAppViweModel.insertManager(manager);

        }

    }

    private void imagesArray() {

        titles.add("food");
        titles.add("bills");
        titles.add("transportation");
        titles.add("home");
        titles.add("car");
        titles.add("entertainment");
        titles.add("shopping");
        titles.add("clothing");
        titles.add("insurance");
        titles.add("tax");
        titles.add("telephone");
        titles.add("cigarette");
        titles.add("health");
        titles.add("sport");
        titles.add("baby");
        titles.add("pet");
        titles.add("beauty");
        titles.add("electronics");
        titles.add("hamburger");
        titles.add("wine");
        titles.add("vegetables");
        titles.add("snacks");
        titles.add("gift");
        titles.add("social");
        titles.add("travel");
        titles.add("education");
        titles.add("fruits");
        titles.add("book");
        titles.add("office");
        titles.add("pizza");
        titles.add("fish");
        titles.add("ac");
        titles.add("others");

        images.add(R.drawable.food);
        images.add(R.drawable.bill);
        images.add(R.drawable.transportation);
        images.add(R.drawable.home);
        images.add(R.drawable.car);
        images.add(R.drawable.entertainment);
        images.add(R.drawable.shopping);
        images.add(R.drawable.clothing);
        images.add(R.drawable.insurance);
        images.add(R.drawable.tax);
        images.add(R.drawable.telephone);
        images.add(R.drawable.cigarette);
        images.add(R.drawable.health);
        images.add(R.drawable.sport);
        images.add(R.drawable.baby);
        images.add(R.drawable.cat);
        images.add(R.drawable.beauty);
        images.add(R.drawable.electronics);
        images.add(R.drawable.hamburger);
        images.add(R.drawable.wine);
        images.add(R.drawable.vegetables);
        images.add(R.drawable.snacks);
        images.add(R.drawable.gift);
        images.add(R.drawable.social);
        images.add(R.drawable.travel);
        images.add(R.drawable.education);
        images.add(R.drawable.fruits);
        images.add(R.drawable.books);
        images.add(R.drawable.office);
        images.add(R.drawable.pizza);
        images.add(R.drawable.fish);
        images.add(R.drawable.ac);
        images.add(R.drawable.others);
    }
    public void addIncome()
    {
        for (int i = 0; i < images1.size(); i++) {
            Manager manager = new Manager(0, Converter.getBase64Bytes(images1.get(i), this), titles1.get(i), "income");
//            managers.add(manager);
            mAppViweModel.insertManager(manager);

        }
    }
    private  void imagesIncome()
    {
        titles1.add("Salary");
        titles1.add("Awards");
        titles1.add("Grants");
        titles1.add("Sale");
        titles1.add("Rental");
        titles1.add("Refunds");
        titles1.add("Coupons");
        titles1.add("Lottery");
        titles1.add("Dividends");
        titles1.add("Investments");
        titles1.add("others");

        images1.add(R.drawable.salary);
        images1.add(R.drawable.awards);
        images1.add(R.drawable.grants);
        images1.add(R.drawable.sale);
        images1.add(R.drawable.rental);
        images1.add(R.drawable.refunds);
        images1.add(R.drawable.coupons);
        images1.add(R.drawable.lottery);
        images1.add(R.drawable.dividends);
        images1.add(R.drawable.investments);
        images1.add(R.drawable.others);

    }



}