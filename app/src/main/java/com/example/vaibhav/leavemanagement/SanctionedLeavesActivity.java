package com.example.vaibhav.leavemanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaibhav.leavemanagement.adapters.LeavesAdapter;
import com.example.vaibhav.leavemanagement.adapters.MyAdapter;
import com.example.vaibhav.leavemanagement.apis.HomeAPI;
import com.example.vaibhav.leavemanagement.pojo.GetEmployeeDetailsModule;
import com.example.vaibhav.leavemanagement.pojo.LeaveDetailsModel;
import com.example.vaibhav.leavemanagement.servicegenerator.ServiceGenerator;
import com.example.vaibhav.leavemanagement.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class SanctionedLeavesActivity extends AppCompatActivity
{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeAPI homeAPI;

    LeavesAdapter adapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanctioned_leaves);

        ButterKnife.bind(this);

        homeAPI = ServiceGenerator.createService(HomeAPI.class);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.sanctionedTxt));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        getItems();

    }


    private void getItems()
    {
        CommonUtils.displayProgressDialog(SanctionedLeavesActivity.this,"");
        TypedInput input = new TypedByteArray("application/json",RequestJson.getLeaveDetails(getIntent().getStringExtra("eid")).toString().getBytes());
        homeAPI.getLeaveDetails(input, new Callback<LeaveDetailsModel>() {
            @Override
            public void success(LeaveDetailsModel leaveDetailsModel, Response response) {
                if (leaveDetailsModel.isSuccess())
                {
                    adapter = new LeavesAdapter(SanctionedLeavesActivity.this,leaveDetailsModel.getLeaveDetails());
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    showToastMsgFun("No records found.");
                }
                CommonUtils.dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {

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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    //Toast Message Print Function
    private void showToastMsgFun(String s)
    {
        Context context=getApplicationContext();
        LayoutInflater inflater=getLayoutInflater();
        @SuppressLint("InflateParams") View customToastroot =inflater.inflate(R.layout.mycustom_toast, null);
        TextView toastMsg = customToastroot.findViewById(R.id.textView1);
        toastMsg.setText(s);

        Toast customtoast=new Toast(context);
        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.BOTTOM,0,200);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }
}
