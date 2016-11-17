package com.truemdhq.projectx.helper;

/**
 * Created by visheshagarwal on 26/09/16.
 */

import android.app.Activity;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.truemdhq.projectx.activity.AppController;


public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

//        Intent intent = new Intent(myContext, AnotherActivity.class);
//        intent.putExtra("error", errorReport.toString());
//        myContext.startActivity(intent);

        new SweetAlertDialog(myContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Thats Strange!")
                .setContentText("Closing the app now.\nPlease try again.")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        AppController.getInstance().clearApplicationData();
                        myContext.finishAffinity();


                    }
                })

                .show();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}