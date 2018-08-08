package com.example.vaibhav.leavemanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeDetailsActivity extends AppCompatActivity
{


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.emailAns)
    TextView emailAns;

    @BindView(R.id.phoneAns)
    TextView phoneAns;

    @BindView(R.id.ageAns)
    TextView ageAns;

    @BindView(R.id.genderAns)
    TextView genderAns;

    @BindView(R.id.designationAns)
    TextView designationAns;

    @BindView(R.id.sanctionLeaveBtn)
    Button sanctionLeaveBtn;

    @BindView(R.id.sanctionedLeaveBtn)
    Button sanctionedLeaveBtn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name")+"'s Details");

        initialistItems();

    }

    private void initialistItems()
    {
        Glide.with(EmployeeDetailsActivity.this).load(getIntent().getStringExtra("img_url")).into(image);
        name.setText(getIntent().getStringExtra("name"));
        emailAns.setText(Html.fromHtml("<u>"+getIntent().getStringExtra("email")+"</u>"));
        phoneAns.setText(Html.fromHtml("<u>"+getIntent().getStringExtra("phone")+"</u>"));
        ageAns.setText(getIntent().getStringExtra("age"));
        genderAns.setText(getIntent().getStringExtra("gender"));
        designationAns.setText(getIntent().getStringExtra("designation"));

        phoneAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getIntent().getStringExtra("phone")));
                startActivity(intent);
            }
        });

        emailAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getIntent().getStringExtra("email")});
                i.putExtra(Intent.EXTRA_SUBJECT, "This is Subject");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try
                {
                    startActivity(Intent.createChooser(i, "Send mail From..."));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    showToastMsgFun("There are no email clients installed.");
                }
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
