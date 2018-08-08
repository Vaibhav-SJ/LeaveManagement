package com.example.vaibhav.leavemanagement;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaibhav.leavemanagement.apis.HomeAPI;
import com.example.vaibhav.leavemanagement.pojo.SuccessModel;
import com.example.vaibhav.leavemanagement.servicegenerator.ServiceGenerator;
import com.example.vaibhav.leavemanagement.utils.CommonUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class SanctionLeaveActivity extends AppCompatActivity
{


    //To check Internet connection
    ConnectivityManager connMgr;
    android.net.NetworkInfo wifi ;
    android.net.NetworkInfo mobile ;
    Boolean internetConnection = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.daysAns)
    EditText daysAns;

    @BindView(R.id.remarksAns)
    EditText remarksAns;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.checkbox)
    CheckBox checkbox;

    float daysVal = 0;

    HomeAPI homeAPI;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanction_leave);

        ButterKnife.bind(this);

        homeAPI = ServiceGenerator.createService(HomeAPI.class);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.sanctionTxt));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkbox.isChecked())
                {
                    if (daysAns.getText().toString().trim().equals(""))
                    {
                        daysVal = (float) 0.5;
                    }
                    else
                    {
                        daysVal = Float.parseFloat(String.valueOf((Integer.parseInt(daysAns.getText().toString().trim())) + (0.5)));
                    }
                }
                else if(daysAns.getText().toString().trim().equals(""))
                {
                    daysAns.setError(getResources().getString(R.string.mandtory_text));
                }
                else if (!checkbox.isChecked() && !daysAns.getText().toString().trim().equals(""))
                {
                    daysVal = Float.parseFloat((daysAns.getText().toString().trim()));
                }


                internetConnection = checkInternetConnectionFun();

                if(internetConnection)
                {
                    addDetailsFun();
                }
                else
                {
                    showToastMsgFun(getResources().getString(R.string.noInternet));
                }


            }
        });

    }

    private void addDetailsFun()
    {

        CommonUtils.displayProgressDialog(SanctionLeaveActivity.this, "");

        TypedInput input = new TypedByteArray("application/json",RequestJson.addLeaveDetails(getIntent().getStringExtra("eid"),daysVal,remarksAns.getText().toString().trim()).toString().getBytes());
        homeAPI.addLeaveDetails(input, new Callback<SuccessModel>() {
            @Override
            public void success(SuccessModel successModel, Response response) {
                showToastMsgFun("Details Submitted");
                finish();

                CommonUtils.dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Log.d("msgAns",String.valueOf(error));

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





}
