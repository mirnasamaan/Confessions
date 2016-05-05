package com.example.marvoot.testingandroid.PublicFunctions;


import android.app.ProgressDialog;
import android.content.Context;


public class AlertMessages {


    ProgressDialog prgDialog;


    public void ShowAlert (Context context, String title, String message, Boolean cancel) {
        prgDialog = new ProgressDialog(context);
        prgDialog.setTitle(title);
        prgDialog.setMessage(message);
        prgDialog.setIndeterminate(false);
        prgDialog.setCancelable(cancel);
        prgDialog.show();
    }


    public void HideAlert () {
        prgDialog.dismiss();
    }

}
