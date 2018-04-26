package com.snapxeats.ui.home.fragment.snapnshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.snapxeats.R;
import com.snapxeats.ui.review.ReviewActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.snapxeats.common.constants.UIConstants.CAMERA_REQUEST;
import static com.snapxeats.common.constants.UIConstants.CAMERA_REQUEST_PERMISSION;

/**
 * Created by Snehal Tembare on 18/4/18.
 */
public class CameraActivity extends AppCompatActivity {
    private Uri file;
    private String restId;
    private String restName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        checkPermissions();
        restId=getIntent().getStringExtra(getString(R.string.review_rest_id));
        restName=getIntent().getStringExtra(getString(R.string.review_rest_name));
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_PERMISSION);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_PERMISSION);
        } else {
            takePicture();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePicture();
    }

    /**
     * Open system's camera
     */
    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = Uri.fromFile(getOutputMediaFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }else {
            finish();
        }
    }

    private File getOutputMediaFile() {
        File fileMediaStorDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getString(R.string.app_name));
        if (!fileMediaStorDir.exists()) {
            if (!fileMediaStorDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(getString(R.string.date_time_pattern)).format(new Date());
        return new File(fileMediaStorDir.getPath() + File.separator +
                getString(R.string.img) + timeStamp + getString(R.string.image_extension));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Intent reviewIntent = new Intent(this,ReviewActivity.class);
            reviewIntent.putExtra(getString(R.string.file_path),file.toString());
            reviewIntent.putExtra(getString(R.string.review_rest_id),restId);
            reviewIntent.putExtra(getString(R.string.review_rest_name),restName);
            startActivity(reviewIntent);
            finish();
        }else {
            finish();
        }
    }
}
