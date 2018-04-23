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
import android.widget.ImageView;
import com.snapxeats.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.snapxeats.common.constants.UIConstants.CAMERA_REQUEST;
import static com.snapxeats.common.constants.UIConstants.CAMERA_REQUEST_PERMISSION;
import static com.snapxeats.common.constants.UIConstants.EXTERNAL_STORAGE_PERMISSION;
import static com.snapxeats.common.constants.UIConstants.ZERO;

public class CameraActivity extends AppCompatActivity {

    private Uri file;
    boolean isPermissionsGranted;
    boolean isCameraPermissionGranted;
    boolean isStoragePermissionGranted;

    @BindView(R.id.image)
    protected ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        checkPermissions();
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
//            isCameraPermissionGranted = true;
//            isStoragePermissionGranted = true;
//            isPermissionsGranted = true;
            takePicture();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_PERMISSION:
                for (int index = ZERO; index < permissions.length; index++) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        isCameraPermissionGranted = true;
                    } else if (!shouldShowRequestPermissionRationale(permissions[index])) {
                        isCameraPermissionGranted = false;
                    } else {
                        isCameraPermissionGranted = false;
                    }
                }
                break;

            case EXTERNAL_STORAGE_PERMISSION:
                for (int index = ZERO; index < permissions.length; index++) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        isStoragePermissionGranted = true;
                    } else if (!shouldShowRequestPermissionRationale(permissions[index])) {
                        isStoragePermissionGranted = false;
                    } else {
                        isStoragePermissionGranted = false;
                    }
                }
                break;

        }
/*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            isPermissionsGranted = true;*/
            takePicture();
       /* }else {
            finish();
        }*/
    }

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
                "SnapXEats");
        if (!fileMediaStorDir.exists()) {
            if (!fileMediaStorDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(fileMediaStorDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            mImageView.setImageURI(file);
        }else {
            finish();
        }
    }
}
