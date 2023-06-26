package com.example.training;/*
 *  Code Prepared by **Muhammad Mubashir**.
 *  Analyst Software Engineer.
    Email Id : muhammad.mubashir.bscs@gmail.com
    Skype Id : muhammad.mubashir.ansari
    Code: **August, 2011.**

    Description: **Get Updates(means New .Apk File) from IIS Server and Download it on Device SD Card,
                 and Uninstall Previous (means OLD .apk) and Install New One.
                 and also get Installed App Version Code & Version Name.**

    All Rights Reserved.
*/


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelfInstall01Activity extends Activity
{



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        //Text= "Old".toString();


    }// On Create END.


}