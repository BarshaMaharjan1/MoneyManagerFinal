package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    Toolbar toolbarA;
    RecyclerView recyclerView;
    List<Integer> images;
    CategoriesAdapter adapter;
    private AppViewModel mAppViweModel;
    private EditText edtCategory;
    private int selectedImageId;
    private ImageView selectedIcon;
    private FloatingActionButton icCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        mAppViweModel = ViewModelProviders.of(this).get(AppViewModel.class);
        toolbarA = findViewById(R.id.Ctoolbar);
        setSupportActionBar(toolbarA);
        TextView toolbarTitle = findViewById(R.id.titletxt);
        TextView toolbarCount = findViewById(R.id.titleCount);
        icCheck = findViewById(R.id.imgClick);
        selectedIcon = findViewById(R.id.selectedIcon);

        edtCategory = findViewById(R.id.edtCategory);
        icCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckClicked();
            }
        });
        //toolbarA.setTitle("");
        toolbarTitle.setText("Add "+getIntent().getStringExtra("type"));
        toolbarCount.setText("");
        setSupportActionBar(toolbarA);

        images = new ArrayList<>();

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
        images.add(R.drawable.salary);
        images.add(R.drawable.awards);
        images.add(R.drawable.grants);
        images.add(R.drawable.sale);
        images.add(R.drawable.rental);
        images.add(R.drawable.refunds);
        images.add(R.drawable.coupons);
        images.add(R.drawable.lottery);
        images.add(R.drawable.dividends);
        images.add(R.drawable.investments);





        recyclerView = findViewById(R.id.recylcerview1);

       adapter = new CategoriesAdapter(this, images, new CategoriesAdapter.CategoryCallback() {
           @Override
           public void onIconClicked(int id) {
               selectedImageId = id;
               icCheck.setVisibility(View.VISIBLE);
               selectedIcon.setImageResource(id);
           }
       });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void onCheckClicked(){
        String category = edtCategory.getText().toString();
        if(!category.isEmpty()) {
            mAppViweModel.insertManager(new Manager(0, Converter.getBase64Bytes(selectedImageId, Categories.this), category, getIntent().getStringExtra("type")));
            onBackPressed();
        }
    }
}