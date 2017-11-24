package com.fairyfalls.kds;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class PermissionChecker {

    public static boolean isPermissionGranted(Activity activity, String permission) {
        int permissionCheck = ActivityCompat.checkSelfPermission(activity, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public static void showPermissionDialog(final Context context) {
        //Log.i("ALERT", "Create dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String title = context.getResources().getString(R.string.app_name);
        builder.setTitle(title);

        builder.setMessage(title + " " + R.string.need_permissions_to_load_and_save_in_storage);

        String positiveText = "Настройки";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        });

        String negativeText = "Выход";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity activity = (Activity) context;
                //activity.finish(); //Пока не будем так суровы.
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //Log.i("ALERT", "Open dialog");
    }
}
