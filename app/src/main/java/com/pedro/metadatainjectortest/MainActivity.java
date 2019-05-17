package com.pedro.metadatainjectortest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import com.pedro.metadatainjectphoto.PhotoInjector;
import com.pedro.metadatavideo.VideoInjector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AskPermissionforMashmellowAndLater();

    VideoInjector videoInjector = new VideoInjector();
    File fileIn = new File(Environment.getExternalStorageDirectory(), "/intput.mp4");
    File fileOut = new File(Environment.getExternalStorageDirectory(), "/output.mp4");
    videoInjector.injectVideo(fileIn.getAbsolutePath(), fileOut.getAbsolutePath());

    //File fileIn2 = new File(Environment.getExternalStorageDirectory(), "testPhoto.jpg");
    //File fileOut2 = new File(Environment.getExternalStorageDirectory(), "resultPhoto.jpg");
    //PhotoInjector photoInjector = new PhotoInjector(this);
    //try {
    //  photoInjector.putMetadata(fileIn2, fileOut2);
    //} catch (IOException e) {
    //  e.printStackTrace();
    //}

    MediaScanner scanner = new MediaScanner(getApplicationContext());
    scanner.startScan(fileOut.getAbsolutePath());
  }

  public void postOnCreate() {
  }

  final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

  private void AskPermissionforMashmellowAndLater() {
    if (Build.VERSION.SDK_INT >= 23) {
      // Marshmallow+
      List<String> permissionsNeeded = new ArrayList<String>();
      final List<String> permissionsList = new ArrayList<String>();

      if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        permissionsNeeded.add("Write External Storage");
      if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
        permissionsNeeded.add("Read External Storage");

      if (permissionsList.size() > 0) {
        if (permissionsNeeded.size() > 0) {
          // Need Rationale
          String message = "You need to grant access to " + permissionsNeeded.get(0);
          for (int i = 1; i < permissionsNeeded.size(); i++)
            message = message + ", " + permissionsNeeded.get(i);

          if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
          }
          return;
        }
        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        return;
      }
    } else {
      // Pre-Marshmallow

      // Do nothing cause all needed permissions are in manifest.
      // pre-M does not need this sort of check
    }

    postOnCreate();
  }

  private boolean addPermission(List<String> permissionsList, String permission) {
    if (Build.VERSION.SDK_INT >= 23) {
      // Marshmallow+
      if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        permissionsList.add(permission);
        // Check for Rationale Option
        if (!shouldShowRequestPermissionRationale(permission))
          return false;
      }
    }
    return true;
  }
}
