package com.example.training;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.io.File;

public class ApkInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString().replace("package:", "");

        // Specify the file path of the APK you want to delete
        String apkFilePath = "/data/app/~~Mif8N9Hvm2w0jcJKPDJF5w==/com.example.training-McgjIBKlvN6Ih8kbWt4CnA==/base.apk";

        if (packageName.equals(context.getPackageName())) {
            // The installation of your own app is complete
            File apkFile = new File(apkFilePath);
            if (apkFile.exists() && apkFile.delete()) {
                // APK file deleted successfully
                System.out.println("APK file deleted.");
            } else {
                // Failed to delete the APK file
                System.out.println("Failed to delete the APK file.");
            }
        }
    }
}
