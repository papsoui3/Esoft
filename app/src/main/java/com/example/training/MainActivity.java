package com.example.training;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.training.databinding.ActivityMainBinding;
import com.example.training.databinding.CustomTablerowLayoutBinding;
import com.example.training.databinding.PdfviewBinding;
import com.example.training.ui.fragmentcompany;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;


@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {
    private static final int INSTALL_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private AppBarConfiguration mAppBarConfiguration;

    CardView cardView;
    CardView cardView_retrieve1;
    private ActivityMainBinding binding;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dataList;

    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;



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


        cardView_retrieve1 = findViewById(R.id.cardView_retrive);
        cardView_retrieve1.setOnClickListener(v ->
                new FetchDataTask().execute("http://192.168.0.103/security/Service.asmx/GetSecurityInfo"));


        cardView = findViewById(R.id.updatecard);
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


        Button btn = findViewById(R.id.printpdfbtn);


        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //createPdf();
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "samp.pdf";
            createPdfFromXmlLayout();

        }
    });





        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    // Inside your activity or fragment

    private void createPdfFromXmlLayout() {
        PdfDocument document = new PdfDocument();
        Log.d("PDFGeneration", "Starting PDF generation");

// 2. Start a new page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size in points (72 points per inch)
        PdfDocument.Page page = document.startPage(pageInfo);

// 3. Inflate the XML layout and draw it on the page's canvas
        PdfviewBinding binding = PdfviewBinding.inflate(getLayoutInflater());
        binding.setPdfTitle("My PDF Title");
        // Set the variable directly in the binding object
        TableLayout stk = binding.tableLayout;

// Create a list of data for the new row
        List<String> newRowDataList = Arrays.asList("43242Row 1", "hfdkjshlkjfdhlkjs2", "234");

// Create a new TableRow for the new data

// Loop through the data and create TextViews for each cell
            TextView item = binding.item;
        TextView qty = binding.qty;
        TextView desc = binding.desc;


            item.setText(newRowDataList.get(0));
            desc.setText(newRowDataList.get(1));
            qty.setText(newRowDataList.get(2));


      //  stk.addView(newRow);


        TextView textView12 = binding.telephone;
        TextView Doc_num = binding.number;
        // Replace 'yourTextViewId' with the actual ID of the TextView in your layout
        textView12.setText("My PDF Title");
        Doc_num.setText(": TSAL80001");

        View view = binding.getRoot();
        view.measure(View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY));
        view.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

// Verify if the TextView is successfully bound


        // ... (Previous co

        view.draw(page.getCanvas());

// ... Rest of the code ...


        // 4. Finish the page
        document.finishPage(page);

        // 5. Save the document to a file
        String pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/tes2t1.pdf";
        try {
            FileOutputStream outputStream = new FileOutputStream(pdfFilePath);
            document.writeTo(outputStream);
            Log.d("PDFGeneration", "PDF generation completed");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF creation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // 6. Close the document
        document.close();

    }

    private void createPdf() {
        // Create a new Document
        Document document = new Document();

        // Get the path to the external storage directory
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "samp.pdf";
        File outputFile = new File(Environment.getExternalStorageDirectory(), "samp.pdf");
        try {
            // Create a PdfWriter instance
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document for writing
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.UNDERLINE);

            // Create a Font for regular text
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Add the title to the PDF
            Paragraph title = new Paragraph("CASH INVOICE", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Add content to the PDF
            Paragraph page = new Paragraph("page 1 of 1",normalFont);
            page.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(page);
            Paragraph space = new Paragraph("");
            document.add(space);

            PdfPTable table1 = new PdfPTable(8);
            table1.setTotalWidth(new float[]{50, 220, 50, 50,50,50,50,50});
            table1.setLockedWidth(true);

            // Add cells to the table
            PdfPCell cell1 = new PdfPCell(new Paragraph("Item",normalFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph("Description",normalFont));
            PdfPCell cell3 = new PdfPCell(new Paragraph("Qty",normalFont));
            PdfPCell cell4 = new PdfPCell(new Paragraph("Price",normalFont));
            PdfPCell cell5 = new PdfPCell(new Paragraph("Disc",normalFont));
            PdfPCell cell6 = new PdfPCell(new Paragraph("Net Price",normalFont));
            PdfPCell cell7 = new PdfPCell(new Paragraph("Vat%",normalFont));
            PdfPCell cell8 = new PdfPCell(new Paragraph("Amount",normalFont));


            table1.addCell(cell1);
            table1.addCell(cell2);
            table1.addCell(cell3);
            table1.addCell(cell4);
            table1.addCell(cell5);
            table1.addCell(cell6);
            table1.addCell(cell7);
            table1.addCell(cell8);

            PdfPTable table2 = new PdfPTable(8);
            table1.setTotalWidth(new float[]{50, 220, 50, 50,50,50,50,50});
            table1.setLockedWidth(true);



            // Add the table to the document
            document.add(table1);



            // Close the document
            document.close();


            // Show a success message to the user
            Toast.makeText(this, "PDF created successfully at " + filePath, Toast.LENGTH_LONG).show();

                // ... (Your existing code to write the PDF content to the file)

                // Get the content URI using FileProvider
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        TextView listView = findViewById(R.id.tvInstallVersion);



        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

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
            ArrayList<String> company = new ArrayList<>();
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> version = new ArrayList<>();
            ArrayList<String> version_date = new ArrayList<>();
            ArrayList<String> sales_names = new ArrayList<>();

            ArrayList<String> Users = new ArrayList<>();
            HashMap<String,ArrayList<String>> company_users = new HashMap<>();
            String flag = "false";
            String comp = null;

            Integer counter=0;
            Integer line= 0;
            Boolean exist= false;
            Integer pombos=0;


            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("salesperson_name")) {
                            String sales_name = parser.nextText();
                            sales_names.add(sales_name);
                            counter+=1;
                        }
                        if (tagName.equalsIgnoreCase("company")) {
                            comp = parser.nextText();
                            company.add(comp);

                            //dataBuilder.append("The Sequence is:").append(tblprfSeq).append("\n");
                        }else if (tagName.equalsIgnoreCase("company_name")) {
                            String name_com = parser.nextText();
                            name.add(name_com);

                        }else if (tagName.equalsIgnoreCase("version")) {
                            String versioning = parser.nextText();
                            version.add(versioning);
                        }else if (tagName.equalsIgnoreCase("synchronised_date")) {
                            String version_date2 = parser.nextText();
                            version_date.add(version_date2);
                        }
                        // Process start tags if needed
                        break;
                }
                pombos +=1;
                // Move to the next event
                eventType = parser.next();
            }


            Intent intent = new Intent(MainActivity.this, fragmentcompany.class);
            intent.putStringArrayListExtra("dataList", company);
            intent.putStringArrayListExtra("dataList_names", name);
            intent.putStringArrayListExtra("version",version);
            intent.putStringArrayListExtra("version_date",version_date);

            intent.putStringArrayListExtra("sales",sales_names);

            intent.putExtra("counter",counter);
            startActivity(intent);
               // runOnUiThread(()->updateAppifCondition());

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
               // performApkInstallation();
            } else {
                Toast.makeText(this, "Permission denied to install APK", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void performApkInstallation() {
        String apkFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/esoftm.apk";
        String storage = Environment.getExternalStorageDirectory().toString() + "/esoftm.apk";
        File file = new File(apkFilePath);
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
    private final ActivityResultLauncher<Intent> request_permission_storage=
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
                //performApkInstallation();
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