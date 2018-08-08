package com.example.vaibhav.leavemanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEmployeeActivity extends AppCompatActivity
{
    //To check Internet connection
    ConnectivityManager connMgr;
    android.net.NetworkInfo wifi ;
    android.net.NetworkInfo mobile ;
    Boolean internetConnection = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.userEmail)
    EditText userEmail;

    @BindView(R.id.userPhone)
    EditText userPhone;

    @BindView(R.id.age)
    EditText age;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    String genderAns = "";

    @BindView(R.id.designation)
    EditText designation;

    @BindView(R.id.submitBtn)
    Button submitBtn;

    String finalImageUrl = "http://www.bsmc.net.au/wp-content/uploads/No-image-available.jpg";


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.add_employee));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                genderAns = String.valueOf(radioButton.getText());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFun();
            }
        });

    }

    @SuppressLint("NewApi")
    private void validateFun()
    {
        if(username.getText().toString().trim().equals(""))
        {
            username.setError(getResources().getString(R.string.mandtory_text));
        }
        else if(userEmail.getText().toString().trim().equals(""))
        {
            userEmail.setError(getResources().getString(R.string.mandtory_text));
        }
        else if (!userEmail.getText().toString().trim().matches(getResources().getString(R.string.emailPattern)))
        {
            userEmail.setError(getResources().getString(R.string.invalidEmailError));
        }
        else if(userPhone.getText().toString().trim().length() != 10)
        {
            userPhone.setError(getResources().getString(R.string.mobileNumError));
        }
        else if (!userPhone.getText().toString().trim().matches(getResources().getString(R.string.onlydigitsPattern)))
        {
            userPhone.setError(getResources().getString(R.string.errorOnlyDigits));
        }
        else if(age.getText().toString().trim().equals(""))
        {
            age.setError(getResources().getString(R.string.mandtory_text));
        }
        else if(genderAns.trim().equals(""))
        {
            showToastMsgFun("Choose Gender to proceed");
        }
        else if(designation.getText().toString().trim().equals(""))
        {
            designation.setError(getResources().getString(R.string.mandtory_text));
        }
        else
        {
            internetConnection = checkInternetConnectionFun();

            if(internetConnection)
            {
                 Log.d("allDetails","Name "+username.getText().toString()+"\nemail "+userEmail.getText().toString()+
                         "\nphone "+userPhone.getText().toString()+"\nage "+age.getText().toString()+
                         "\ngender : "+genderAns+"\ndesignation: "+designation.getText().toString());
            }
            else
            {
                showToastMsgFun(getResources().getString(R.string.noInternet));
            }
        }
    }


    //Function to check internet connection
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Boolean checkInternetConnectionFun()
    {
        //To check Internet connection
        connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            wifi = Objects.requireNonNull(connMgr).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        mobile = Objects.requireNonNull(connMgr).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnected() || mobile.isConnected();
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
            startActivity(new Intent(AddEmployeeActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AddEmployeeActivity.this, MainActivity.class));
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
