package com.example.training;


import android.Manifest;
import android.app.DownloadManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.training.databinding.ActivityMainBinding;
import com.example.training.ui.login.LoginFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



import retrofit2.Retrofit;


@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {
    private static final int INSTALL_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private AppBarConfiguration mAppBarConfiguration;

    CardView cardView;
    CardView cardView_retrieve;
    private ActivityMainBinding binding;

    String TAG = "Permission";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_login,
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        String url = "http://192.168.0.103/test/esoftm.apk";
        //https://dagrs.berkeley.edu/sites/default/files/2020-01/sample.pdf

        String title = URLUtil.guessFileName(url, null, null);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String filename = "esoftm.apk";
        destination += filename;
        final Uri uri = Uri.parse("file://" + destination);

        String finalDestination1 = destination;

        cardView_retrieve = (CardView) findViewById(R.id.cardView_retrive);
        cardView_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchDataTask().execute("http://192.168.0.103/website1/Service.asmx/GetPreferences?company=ECOS&slman=001");
            }
        });


        cardView = (CardView) findViewById(R.id.updatecard);
        cardView.setOnClickListener(arg0 ->{
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(title);
            request.setDescription("Downloading file");
            String cookie = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookie);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


            downloadManager.enqueue(request);

            Toast.makeText(MainActivity.this, "Download started", Toast.LENGTH_LONG).show();
            if (checkPermission()) {
                Toast.makeText(MainActivity.this, "granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something
                    if (isApkDownloadComplete(finalDestination1, 10500)) {
                        installll();
                    }
                }
            }, 5000);



        });
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    public void updateAppifCondition(){
        String url = "http://192.168.0.103/test/esoftm.apk";
        String title = URLUtil.guessFileName(url, null, null);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String filename = "esoftm.apk";
        destination += filename;
        final Uri uri = Uri.parse("file://" + destination);

        String finalDestination1 = destination;

        Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription("Downloading file");
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        downloadManager.enqueue(request);

        Toast.makeText(MainActivity.this, "Download started", Toast.LENGTH_LONG).show();
        if (checkPermission()) {
            Toast.makeText(MainActivity.this, "granted", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something
                if (isApkDownloadComplete(finalDestination1, 10500)) {
                    installll();
                }
            }
        }, 5000);

    }
    public class FetchDataTask extends AsyncTask<String, Void, String> {
        TextView textView = findViewById(R.id.tvInstallVersion);


        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the XML data from the web
                InputStream inputStream = connection.getInputStream();

                // Parse the XML data
                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlFactoryObject.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parseXmlData(parser);

                // Close the input stream
                inputStream.close();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void parseXmlData(XmlPullParser parser) throws XmlPullParserException, IOException {
            int eventType = parser.getEventType();
            StringBuilder dataBuilder = new StringBuilder();
            String flag = "false";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();


                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("tblprf_seq")) {
                            String tblprfSeq = parser.nextText();
                            dataBuilder.append("The Sequence is:").append(tblprfSeq).append("\n");
                            // Do something with tblprf_seq value
                        } else if (tagName.equalsIgnoreCase("tblprf_User")) {
                            String tblprfComp = parser.nextText();
                            if (tblprfComp.equals("ANNIE")){
                                flag="true";
                                //updateAppifCondition();
                            }
                            dataBuilder.append(tblprfComp).append("\n");
                            // Do something with tblprf_Comp value
                        }

                        // Process start tags if needed
                        break;

                    case XmlPullParser.TEXT:
                        // Get text content of the tag

                        // Append the text to the data builder

                        break;

                    case XmlPullParser.END_TAG:
                        // Process end tags if needed
                        break;
                }

                // Move to the next event
                eventType = parser.next();
            }

            // Update the UI with the retrieved data
            runOnUiThread(() -> textView.setText(dataBuilder.toString()));
            if (flag=="false"){
                runOnUiThread(()->updateAppifCondition());
            }

        }
    }

    static Retrofit retrofit;


    public boolean isApkDownloadComplete(String filePath, long expectedSize) {
            File apkFile = new File(filePath);

            while (!apkFile.exists() || apkFile.length() < expectedSize) {
                try {
                    // Wait for a specific interval before checking again
                    Thread.sleep(6000);
                    Toast.makeText(MainActivity.this, "APK file still Downloading", Toast.LENGTH_SHORT).show();
                    // Adjust the interval as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // APK download is complete
            return true;
    }

    private void installll() {
        // Specify the path to your APK file in the downloads directory
        String apkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/esoftm.apk";
        File apkFile = new File(apkPath);

        if (apkFile.exists()) {
            Uri apkUri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".provider", apkFile);
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(installIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Unable to install the APK", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "APK file not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // User granted permission, install the APK
                performApkInstallation();
            } else {
                Toast.makeText(this, "Permission denied to install APK", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void performApkInstallation() {
        String apkFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/esoftm.apk".toString();
        String storage = Environment.getExternalStorageDirectory().toString() + "/esoftm.apk";
        File file = new File(storage);
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse(file.getPath()); // My work-around for SDKs up to 29.
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult( intent, 100);
            } else {
                requestPermissions(new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 100);
            }
        }
        startActivity(intent);
    }
    private ActivityResultLauncher<Intent> request_permission_storage=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        if (Environment.isExternalStorageManager()){
                            Log.d(TAG,"ONactivityResul Granted");
                        }
                    }
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==STORAGE_PERMISSION_CODE){
            if (grantResults.length>0){
                boolean write = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (write){
                    Log.d(TAG,"PERMISSION GRANTED");
                }
            }
        }
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performApkInstallation();
            }
            } else {
                Toast.makeText(this, "Permission denied. Cannot install APK.", Toast.LENGTH_SHORT).show();
            }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        return false;
    }

    private void requestPermission() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
           try{
               Log.d(TAG,"requestPermission:tryt");

               Intent intent = new Intent();
               intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
               Uri uri = Uri.fromParts("package",this.getPackageName(),null);
               intent.setData(uri);
               request_permission_storage.launch(intent);
           }catch (Exception e){
               Log.d(TAG,"request:catch",e);
               Intent intent = new Intent();
               intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
               request_permission_storage.launch(intent);
           }
       }else {

       }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity.this,"Downloading",Toast.LENGTH_LONG).show();
                return false;

            }
        });
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}