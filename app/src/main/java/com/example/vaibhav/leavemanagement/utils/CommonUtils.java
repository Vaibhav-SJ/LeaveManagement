package com.example.vaibhav.leavemanagement.utils;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.vaibhav.leavemanagement.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.Objects;

public class CommonUtils
{
    private static Dialog mProgressDialog;


    @SuppressLint("NewApi")
    public static void displayProgressDialog(Context context, String message) {
        dismissProgressDialog();
        @SuppressLint("InflateParams") View loadingView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        mProgressDialog = new Dialog(context);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.getWindow().setDimAmount(0.4f);
        mProgressDialog.setCancelable(false);
        if (message != null) {
            message.trim().length();
        }
        mProgressDialog.setContentView(loadingView);
        mProgressDialog.show();
        ((RotateLoading) loadingView.findViewById(R.id.loading)).start();
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }



}
