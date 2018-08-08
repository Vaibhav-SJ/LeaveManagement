package com.example.vaibhav.leavemanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.vaibhav.leavemanagement.adapters.MyAdapter;
import com.example.vaibhav.leavemanagement.apis.HomeAPI;
import com.example.vaibhav.leavemanagement.pojo.GetEmployeeDetailsModule;
import com.example.vaibhav.leavemanagement.servicegenerator.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class ViewEmployeesActivity extends AppCompatActivity
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loadTxt)
    TextView loadTxt;

    @BindView(R.id.searchOrders)
    SearchView searchView;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeAPI homeAPI;
    MyAdapter adapter;

    List<GetEmployeeDetailsModule.EmployeeDetailsBean> detailsList;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        ButterKnife.bind(this);
        homeAPI = ServiceGenerator.createService(HomeAPI.class);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.employees_list_txt));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        
        getItems();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                List<GetEmployeeDetailsModule.EmployeeDetailsBean> filteredItems = filterItems(s,detailsList);
                adapter = new MyAdapter(ViewEmployeesActivity.this,filteredItems);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });

    }

    private List<GetEmployeeDetailsModule.EmployeeDetailsBean> filterItems(String s, List<GetEmployeeDetailsModule.EmployeeDetailsBean> detailsList)
    {
        List<GetEmployeeDetailsModule.EmployeeDetailsBean> filteredItem = new ArrayList<>();
        for (GetEmployeeDetailsModule.EmployeeDetailsBean fa : detailsList)
        {
            if (fa.getName().toLowerCase().contains(s.toLowerCase()) || fa.getDesignation().toLowerCase().contains(s.toLowerCase()) || fa.getPhone().toLowerCase().contains(s.toLowerCase()) )
            {
                filteredItem.add(fa);
            }
        }

        return filteredItem;
    }


    private void getItems()
    {
        TypedInput input = new TypedByteArray("application/json",RequestJson.getEmployeeDetails().toString().getBytes());
        homeAPI.getEmployeeDetails(input, new Callback<GetEmployeeDetailsModule>()
        {
            @Override
            public void success(GetEmployeeDetailsModule getEmployeeDetailsModule, Response response)
            {
                detailsList = new ArrayList<>();
                detailsList.addAll(getEmployeeDetailsModule.getEmployeeDetails());

                adapter = new MyAdapter(ViewEmployeesActivity.this,detailsList);
                recyclerView.setAdapter(adapter);
                loadTxt.setVisibility(View.GONE);
            }

            @Override
            public void failure(RetrofitError error)
            {


            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // on back button press function
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(new Intent(ViewEmployeesActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ViewEmployeesActivity.this, MainActivity.class));
        finish();
    }















}
