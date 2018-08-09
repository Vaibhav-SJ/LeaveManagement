package com.example.vaibhav.leavemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaibhav.leavemanagement.apis.HomeAPI;
import com.example.vaibhav.leavemanagement.pojo.SuccessModel;
import com.example.vaibhav.leavemanagement.servicegenerator.ServiceGenerator;
import com.example.vaibhav.leavemanagement.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static java.security.AccessController.getContext;

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

    @BindView(R.id.buttonChooseImg)
    Button buttonChooseImg;

    @BindView(R.id.profileImg)
    ImageView profileImg; 

    @BindView(R.id.submitBtn)
    Button submitBtn;
    HomeAPI homeAPI;

    //image upload
    public static final int mYPermisionsRequestCamera = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private static final int CAMERA_REQUEST_PROFILE = 1888;
    private static final int REQUEST_GAllERY_PICTURE_PROFILE = 1;
    public static final String UPLOAD_URL = "http://appmomos.com/sportsAcademy/profile_pic_upload.php";
    public static final String UPLOAD_KEY = "userimage";

    Boolean isProfilePicSelected = false;
    String profilePicUrl= "http://emodularkitchen.com/img/uploads/posts/404%20.jpg";
    Bitmap bitmap;
    Bitmap profilePicBitMap;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        ButterKnife.bind(this);
        homeAPI = ServiceGenerator.createService(HomeAPI.class);

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


        isProfilePicSelected = false;
        profilePicUrl= "http://emodularkitchen.com/img/uploads/posts/404%20.jpg";
        profileImg.setVisibility(View.GONE);

        buttonChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfileImage();
            }
        });

    }
    
    /* Image upload */



    //Funtion to upload user image to server and get url
    private void uploadProfileImage(final Bitmap bitmap)
    {
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,String,String>
        {
            private RequestHandler rh = new RequestHandler();
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                CommonUtils.displayProgressDialog(AddEmployeeActivity.this, "");
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                CommonUtils.dismissProgressDialog();
                profilePicUrl = s;
                Log.d("profilePicUrl ",profilePicUrl);

                finalApiCallFun();
            }

            @Override
            protected String doInBackground(Bitmap... params)
            {
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                return rh.sendPostRequest(UPLOAD_URL,data);
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(this.bitmap);
    }



    private void selectProfileImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddEmployeeActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (options[item].equals("Take Photo"))
                {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(AddEmployeeActivity.this), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (getFromPref(Objects.requireNonNull(AddEmployeeActivity.this), ALLOW_KEY))
                        {
                            showSettingsAlert();
                        }
                        else if (ContextCompat.checkSelfPermission(Objects.requireNonNull(AddEmployeeActivity.this),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(AddEmployeeActivity.this),Manifest.permission.CAMERA))
                            {
                                showAlert();
                            }
                            else
                            {
                                ActivityCompat.requestPermissions(Objects.requireNonNull(AddEmployeeActivity.this),new String[]{Manifest.permission.CAMERA}, mYPermisionsRequestCamera);
                            }
                        }
                    }
                    else
                    {
                        openCameraForPic();
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_GAllERY_PICTURE_PROFILE);
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_PROFILE)
        {
            try
            {
                @SuppressLint({"NewApi", "LocalSuppress"}) Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                profileImg.setImageBitmap(photo);
                profilePicUrl= getStringImage(photo);
                profilePicBitMap = StringToBitMap(profilePicUrl);
                isProfilePicSelected = true;
                profileImg.setVisibility(View.VISIBLE);
            }
            catch (NullPointerException e )
            {
                showToastMsgFun(getResources().getString(R.string.imageNotSelectedTxt));
            }

        }
        else if (requestCode == REQUEST_GAllERY_PICTURE_PROFILE )
        {
            try
            {
                Uri imageUri = data.getData();
                InputStream imageStream = null;
                if (imageUri != null) {
                    imageStream = Objects.requireNonNull(AddEmployeeActivity.this).getContentResolver().openInputStream(imageUri);
                }
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImage = getResizedBitmap(selectedImage, 400);
                profileImg.setImageBitmap(selectedImage);
                profilePicUrl= getStringImage(selectedImage);
                profilePicBitMap = StringToBitMap(profilePicUrl);
                isProfilePicSelected = true;
                profileImg.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                showToastMsgFun(getResources().getString(R.string.imageNotSelectedTxt));
            }

        }

    }


    public Bitmap StringToBitMap(String encodedString)
    {
        try
        {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e)
        {
            e.getMessage();
            return null;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1)
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }
        else
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void openCameraForPic()
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROFILE);
    }

    public static Boolean getFromPref(Context context, String key)
    {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert()
    {
        @SuppressLint({"NewApi", "LocalSuppress"}) AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(AddEmployeeActivity.this)).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        AddEmployeeActivity.this.finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener()
                {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(Objects.requireNonNull(AddEmployeeActivity.this),new String[]{Manifest.permission.CAMERA}, mYPermisionsRequestCamera);
                        openCameraForPic();
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert()
    {
        @SuppressLint({"NewApi", "LocalSuppress"}) AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(AddEmployeeActivity.this)).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener()
                {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(Objects.requireNonNull(AddEmployeeActivity.this));
                    }
                });

        alertDialog.show();
    }


    public static void startInstalledAppDetailsActivity(final Activity context)
    {
        if (context == null)
        {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        if (requestCode == mYPermisionsRequestCamera) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(AddEmployeeActivity.this), permission);
                    if (showRationale) {
                        showAlert();
                    } else {
                        saveToPreferences(Objects.requireNonNull(AddEmployeeActivity.this), ALLOW_KEY);
                    }
                }
            }
        }
    }

    public static void saveToPreferences(Context context, String key)
    {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, true);
        prefsEditor.apply();
    }


    /* Image upload */

    @SuppressLint("NewApi")
    private void validateFun()
    {
        if(username.getText().toString().trim().equals(""))
        {
            username.setError(getResources().getString(R.string.mandtory_text));
            showToastMsgFun("Enter Name to proceed");
        }
        else if(userEmail.getText().toString().trim().equals(""))
        {
            userEmail.setError(getResources().getString(R.string.mandtory_text));
            showToastMsgFun("Enter correct Email to proceed");
        }
        else if (!userEmail.getText().toString().trim().matches(getResources().getString(R.string.emailPattern)))
        {
            userEmail.setError(getResources().getString(R.string.invalidEmailError));
        }
        else if(userPhone.getText().toString().trim().length() != 10)
        {
            userPhone.setError(getResources().getString(R.string.mobileNumError));
            showToastMsgFun("Enter correct Phone number to proceed");
        }
        else if (!userPhone.getText().toString().trim().matches(getResources().getString(R.string.onlydigitsPattern)))
        {
            userPhone.setError(getResources().getString(R.string.errorOnlyDigits));
            showToastMsgFun("Enter correct Phone number to proceed");
        }
        else if(age.getText().toString().trim().equals(""))
        {
            age.setError(getResources().getString(R.string.mandtory_text));
            showToastMsgFun("Enter age to proceed");
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

                if(isProfilePicSelected)
                {
                    uploadProfileImage(profilePicBitMap);
                }
                else
                {
                    finalApiCallFun();

                }


            }
            else
            {
                showToastMsgFun(getResources().getString(R.string.noInternet));
            }
        }
    }


    private void finalApiCallFun()
    {
        CommonUtils.displayProgressDialog(AddEmployeeActivity.this, "");

        TypedInput input = new TypedByteArray("application/json",RequestJson.addEmployeeDetails(username.getText().toString(),
                userPhone.getText().toString(),age.getText().toString(),genderAns,profilePicUrl,designation.getText().toString(),
                userEmail.getText().toString()).toString().getBytes());
        homeAPI.addEmployeeDetails(input, new Callback<SuccessModel>() {
            @Override
            public void success(SuccessModel successModel, Response response) {
                showToastMsgFun("Employee Details Submitted");
                finish();

                CommonUtils.dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Log.d("msgAns",String.valueOf(error));

            }
        });

        Log.d("allDetails","Name "+username.getText().toString()+"\nemail "+userEmail.getText().toString()+
                "\nphone "+userPhone.getText().toString()+"\nage "+age.getText().toString()+
                "\ngender : "+genderAns+"\ndesignation: "+designation.getText().toString()+"\nimage "+profilePicUrl);
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
