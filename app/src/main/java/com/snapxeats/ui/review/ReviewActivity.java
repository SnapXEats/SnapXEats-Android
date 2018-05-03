package com.snapxeats.ui.review;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.shareReview.ShareReviewActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.snapxeats.common.constants.UIConstants.INT_TEN;
import static com.snapxeats.common.constants.UIConstants.TIME_HOUR;
import static com.snapxeats.common.constants.UIConstants.TIME_MINUTE;
import static com.snapxeats.common.constants.UIConstants.TIME_SECONDS;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 12/4/18.
 */

public class ReviewActivity extends BaseActivity implements ReviewContract.ReviewView,
        AppContract.SnapXResults {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    ReviewContract.ReviewPresenter mPresenter;

    @BindView(R.id.toolbar_review)
    protected Toolbar mToolbar;

    @BindView(R.id.img_rest_photo)
    protected ImageView mImgRestPhoto;

    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private Chronometer mChronometer, mChronometerPlay;
    private File savedAudioPath = null;
    public static final int RequestPermissionCode = 1;

    private Button mBtnStartAudio;
    private Button mBtnStopReview;
    private long seconds;

    @BindView(R.id.rating_review)
    protected RatingBar mRatingBar;

    @BindView(R.id.edt_txt_review)
    protected EditText mEditTxtReview;

    @BindView(R.id.txt_length_error)
    protected TextView mTxtLengthError;

    @BindView(R.id.timer_audio_time)
    protected Chronometer mAudioTime;

    @BindView(R.id.img_play_review)
    protected ImageView mImgPlayAudio;

    @BindView(R.id.img_audio_review)
    protected ImageView mImgAddAudio;

    private Uri fileImageUri;
    private String restId;
    private String restName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        setUpToolbar();
        //get file path
        Intent intent = getIntent();
        String image_path = intent.getStringExtra(getString(R.string.file_path));
        restId = intent.getStringExtra(getString(R.string.review_rest_id));
        restName = intent.getStringExtra(getString(R.string.review_rest_name));
        fileImageUri = Uri.parse(image_path);
        mImgRestPhoto.setImageURI(fileImageUri);
    }

      /*TODO-Restrict review text length
    private void limitTextReview() {

       mEditTxtReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (UIConstants.REVIEW_LENGTH_LIMIT == s.length()) {
                    mTxtLengthError.setVisibility(View.VISIBLE);
                    utility.hideKeyboard();
                } else {
                    mTxtLengthError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
} */


    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.toolbar_snap_share));
    }


    /*webservice call to send review*/
    @OnClick(R.id.img_share_review)
    public void imgSendReview() {
        int rating = (int) mRatingBar.getRating();
        if (ZERO != rating) {
            dialogShareReview();
        } else {
            dialogSetRating();
        }
    }

    private void dialogSetRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.msg_set_rating))
                .setNeutralButton(getString(R.string.ok), (dialog, which) -> {
                })
                .show();
    }

    private void dialogShareReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.msg_share_review))
                .setPositiveButton(getString(R.string.review_continue), (dialog, which) -> {
                    callApiReview();
                })
                .setNegativeButton(getString(R.string.review_discard), (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void callApiReview() {
        String textReview = mEditTxtReview.getText().toString();
        int rating = (int) mRatingBar.getRating();
        if (null != restId && null != fileImageUri && 0 != rating) {
            showProgressDialog();
            mPresenter.sendReview(restId, fileImageUri, Uri.fromFile(savedAudioPath), textReview, rating);
        }
    }

    /*Play recorded audio review*/
    @OnClick(R.id.img_play_review)
    public void imgPlayAudio() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_play_audio, null);
        Button mBtnDismissAudio = alertLayout.findViewById(R.id.btn_play_dismiss);
        ImageView mImgPlayRecAudio = alertLayout.findViewById(R.id.img_play_audio);
        TextView mTxtDeleteAudio = alertLayout.findViewById(R.id.txt_delete_audio);
        mChronometerPlay = alertLayout.findViewById(R.id.timer_play);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
        mBtnDismissAudio.setOnClickListener(v -> {
            mChronometerPlay.stop();
            dialog.dismiss();
            if (null != mPlayer) {
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.release();
                setMediaRecorder();
            }
        });
        mImgPlayRecAudio.setOnClickListener(v -> {
            mChronometerPlay.setBase(SystemClock.elapsedRealtime());
            mChronometerPlay.start();
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(savedAudioPath.toString());
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        });
        mTxtDeleteAudio.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(getString(R.string.msg_delete_review))
                    .setPositiveButton(getString(R.string.yes), (dialog1, which) -> {
                        mPlayer = new MediaPlayer();
                        mPlayer.stop();
                        mPlayer.release();
                        boolean deleted = savedAudioPath.delete();
                        if (deleted) {
                            SnapXToast.showToast(this, getString(R.string.review_del));
                        }
                        dialog.dismiss();
                        mImgAddAudio.setVisibility(View.VISIBLE);
                        mImgPlayAudio.setVisibility(View.GONE);
                        mAudioTime.setVisibility(View.GONE);
                    })
                    .setNegativeButton(getString(R.string.not_now), (dialog1, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    /*Add audio review*/
    @OnClick(R.id.img_audio_review)
    public void imgAddReview() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_record_review, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();

        mBtnStartAudio = alertLayout.findViewById(R.id.btn_record_review);
        mBtnStopReview = alertLayout.findViewById(R.id.btn_stop_review);
        TextView mTxtCancel = alertLayout.findViewById(R.id.txt_review_cancel);
        mChronometer = alertLayout.findViewById(R.id.chronometer_timer);

        mRecorder = new MediaRecorder();

        mBtnStartAudio.setOnClickListener(v -> {
            mBtnStartAudio.setVisibility(View.GONE);
            mBtnStopReview.setVisibility(View.VISIBLE);
            startRecording();
        });

        mBtnStopReview.setOnClickListener(v -> {
            stopRecording();
            dialog.dismiss();
            mImgAddAudio.setVisibility(View.GONE);
            mImgPlayAudio.setVisibility(View.VISIBLE);
            mAudioTime.setVisibility(View.VISIBLE);

            setAudioTime();
        });
        mTxtCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void setAudioTime() {
        long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
        int hour = (int) (time / TIME_HOUR);
        int min = (int) (time - hour * TIME_HOUR) / TIME_MINUTE;
        int seconds = (int) (time - hour * TIME_HOUR - min * TIME_MINUTE) / TIME_SECONDS;
        String audioTime = (min < INT_TEN ? getString(R.string.int_zero) + min : min) + ":"
                + (seconds < INT_TEN ? getString(R.string.int_zero) + seconds : seconds);
        mAudioTime.setText(audioTime);
    }

    private void stopRecording() {
        mChronometer.stop();
        mRecorder.stop();
        mRecorder.release();
    }

    private void startRecording() {

        if (checkPermission()) {
            savedAudioPath = getOutputMediaFile();
            setMediaRecorder();
            try {
                mRecorder.prepare();
                mRecorder.start();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mChronometer.setOnChronometerTickListener(chronometer -> {
                    String currentTime = mChronometer.getText().toString();
                    if (currentTime.equals(getString(R.string.timer_audio_review))) {
                        mChronometer.stop();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(getString(R.string.msg_share_review))
                                .setNeutralButton(getString(R.string.ok), (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }
                });
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        } else {
            requestPermission();
        }
    }

    public void setMediaRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(savedAudioPath.toString());
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
                getString(R.string.aud) + timeStamp + getString(R.string.audio_extension));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ReviewActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > ZERO) {

                    boolean StoragePermission = grantResults[ZERO] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(ReviewActivity.this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ReviewActivity.this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int resultAudio = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        return resultStorage == PackageManager.PERMISSION_GRANTED && resultAudio == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        SnapNShareResponse mSnapResponse = (SnapNShareResponse) value;
        Intent intent = new Intent(ReviewActivity.this, ShareReviewActivity.class);
        intent.putExtra(getString(R.string.intent_review), mSnapResponse);
        startActivity(intent);
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {
    }

    @Override
    public void networkError(Object value) {
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        dialogExitReview();
    }

    private void dialogExitReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.msg_review_back_pressed));
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> finish());
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}