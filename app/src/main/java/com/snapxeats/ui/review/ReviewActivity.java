package com.snapxeats.ui.review;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.LoginUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.login.InstagramApp;
import com.snapxeats.ui.login.InstagramDialog;
import com.snapxeats.ui.shareReview.ShareReviewActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.snapxeats.common.constants.UIConstants.INT_TEN;
import static com.snapxeats.common.constants.UIConstants.MILLI_TO_SEC;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.REQUEST_PERMISSION_CODE;
import static com.snapxeats.common.constants.UIConstants.SX_BEARER;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.TIME_HOUR;
import static com.snapxeats.common.constants.UIConstants.TIME_MINUTE;
import static com.snapxeats.common.constants.UIConstants.TIME_SECONDS;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 12/4/18.
 */

public class ReviewActivity extends BaseActivity implements ReviewContract.ReviewView,
        AppContract.SnapXResults, InstagramDialog.InstagramDialogListener,
        MediaPlayer.OnCompletionListener {

    @BindView(R.id.toolbar_review)
    protected Toolbar mToolbar;

    @BindView(R.id.img_rest_photo)
    protected ImageView mImgRestPhoto;

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

    @BindView(R.id.layout_main_review)
    protected LinearLayout mParentLayout;

    @Inject
    SnapXDialog snapXDialog;
    @Inject
    AppUtility utility;
    @Inject
    DbHelper dbHelper;
    @Inject
    ReviewContract.ReviewPresenter mPresenter;
    @Inject
    ReviewDbHelper reviewDbHelper;
    @Inject
    LoginUtility loginUtility;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;
    private Chronometer mChronometer;
    private File savedAudioPath = null;
    private Button mBtnStartAudio;
    private Button mBtnStopReview;
    private Uri fileImageUri;
    private String restId;
    private String image_path;
    private int resumePosition;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    private int seconds;
    private String audioTime;
    private AlertDialog dialog;
    private long timeRemaining = ZERO;
    private TextView mTimer;
    private ImageView mImgPlayRecAudio, mImgPauseRecAudio;
    private RootRestaurantInfo mRootRestaurantInfo;
    private Uri audioFile;
    private int rating;
    private String textReview;
    private CallbackManager mCallbackManager;
    private SnapXUserRequest snapXUserRequest;
    private InstagramApp mApp;
    private String mToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        initView();
    }

    /*hide keyboard when tabbed on screen*/
    @OnClick(R.id.layout_main_review)
    public void layoutReview() {
        utility.hideKeyboard();
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        setUpToolbar();
        dbHelper.setContext(this);
        reviewDbHelper.setContext(this);
        dbHelper.getDraftPhotoDao();
        getReviewData();
        loginUtility.setContext(this);

        /** initialize facebook and instagram login **/
        mCallbackManager = CallbackManager.Factory.create();
        initInstagram();
    }

    private void getReviewData() {
        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getExtras().getString(getString(R.string.file_path));
            fileImageUri = Uri.parse(image_path);

            mRootRestaurantInfo = intent.getExtras().getParcelable(getString(R.string.restaurant_info_object));
            if (null != mRootRestaurantInfo.getRestaurantDetails()) {
                restId = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_info_id();
            }
        }

        Glide.with(this)
                .load(image_path)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_rest_info_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL)
                .into(mImgRestPhoto);

        mToolbar.setNavigationOnClickListener(v -> dialogExitReview());
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.toolbar_snap_share));
        mToolbar.setNavigationOnClickListener(v -> dialogExitReview());
    }

    /**
     * show login dialog if user is not logged in
     **/
    private void showLoginDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_dialog_login, null);
        initAlertDialog(alertLayout);
        TextView txtShareLater = alertLayout.findViewById(R.id.txt_login_share_later);
        Button btnCustomFb = alertLayout.findViewById(R.id.btn_fb_custom);
        LoginButton btnFb = alertLayout.findViewById(R.id.btn_facebook_login);
        Button btnInsta = alertLayout.findViewById(R.id.btn_instagram_login);

        loginWithFacebook();

        btnCustomFb.setOnClickListener(v -> {
            if (v == btnCustomFb) {
                if (NetworkUtility.isNetworkAvailable(this)) {
                    btnFb.performClick();
                } else {
                    showNetworkErrorDialog((dialog, which) -> {
                        if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                            AppContract.DialogListenerAction click = () -> {
                                showProgressDialog();
                                btnFb.performClick();
                            };
                            showSnackBar(mParentLayout, setClickListener(click));
                        }
                    });
                }
            }
        });

        btnInsta.setOnClickListener(v -> showInstaWebView());

        txtShareLater.setOnClickListener(v -> dialogSaveToDraft());
    }

    private void showInstaWebView() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            mApp.authorize();

        } else {
            showNetworkErrorDialog((dialog, which) -> {
                if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                    AppContract.DialogListenerAction click = () -> {
                        showProgressDialog();
                        mApp.authorize();
                    };
                    showSnackBar(mParentLayout, setClickListener(click));
                }
            });
        }
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        snapXUserRequest = new SnapXUserRequest(AccessToken.getCurrentAccessToken().getToken(),
                                getString(R.string.platform_facebook), AccessToken.getCurrentAccessToken().getUserId());
                        showProgressDialog();
                        mPresenter.getUserdata(snapXUserRequest);
                    }

                    @Override
                    public void onCancel() {
                        mPresenter.response(SnapXResult.NETWORKERROR, null);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        mPresenter.response(SnapXResult.ERROR, null);
                    }
                });
    }

    private void initInstagram() {
        mApp = new InstagramApp(this, WebConstants.INSTA_CLIENT_ID, WebConstants.INSTA_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                //TODO data passed null for now
                mPresenter.response(SnapXResult.SUCCESS, null);
            }

            @Override
            public void onFail(String error) {
                mPresenter.response(SnapXResult.NETWORKERROR, null);
            }
        });
    }

    @Override
    public void onReturnValue(String token) {
        showProgressDialog();
        mPresenter.getInstaInfo(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*save to draft dialog on 'share later' action*/
    private void dialogSaveToDraft() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.draft));
        builder.setMessage(getString(R.string.msg_save_to_draft));
        builder.setNeutralButton(getString(R.string.ok), (dialog, which) -> finish());
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*webservice call to send review*/
    @OnClick(R.id.img_share_review)
    public void imgSendReview() {
        //Save share data in local db for Smart photo 'Draft'
        List<String> restaurantAminities = null;
        String restName = null;
        String restAddress = null;
        if (null != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities()
                && ZERO != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities().size())
            restaurantAminities = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_amenities();

        if (null != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name() &&
                !mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name().isEmpty())
            restName = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_name();

        if (null != mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address() &&
                !mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address().isEmpty())
            restAddress = mRootRestaurantInfo.getRestaurantDetails().getRestaurant_address();

        String audioFile = null;
        if (null != savedAudioPath) audioFile = Uri.fromFile(savedAudioPath).getPath();
        textReview = mEditTxtReview.getText().toString();
        int rating = (int) mRatingBar.getRating();

        reviewDbHelper.saveSnapDataInDb(restId,
                restName
                , restAddress
                , Uri.parse(image_path).getPath()
                , audioFile,
                textReview,
                rating,
                restaurantAminities);

        if (ZERO != rating) {
            if (utility.isLoggedIn()) {
                dialogShareReview();
            } else {
                showLoginDialog();
            }
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
                .setPositiveButton(getString(R.string.review_continue), (dialog, which) -> callApiReview())
                .setNegativeButton(getString(R.string.review_discard), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void callApiReview() {
        textReview = mEditTxtReview.getText().toString();
        rating = (int) mRatingBar.getRating();
        if (null != restId && null != fileImageUri && ZERO != rating) {
            showProgressDialog();
            audioFile = null;
            if (null != savedAudioPath) {
                audioFile = Uri.fromFile(savedAudioPath);
            }
            if (mToken == null) {
                mPresenter.sendReview(utility.getAuthToken(this), restId, fileImageUri, audioFile, textReview, rating);
            } else {
                mPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
            }
        }
    }

    /*initialize components for alert dialog*/
    public void initAlertDialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();
    }

    /*Play recorded audio review*/
    @OnClick(R.id.img_play_review)
    public void imgPlayAudio() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_play_audio, null);
        Button mBtnDoneAudio = alertLayout.findViewById(R.id.btn_play_done);
        mImgPlayRecAudio = alertLayout.findViewById(R.id.img_play_audio);
        mImgPauseRecAudio = alertLayout.findViewById(R.id.img_pause_audio);
        TextView mTxtDeleteAudio = alertLayout.findViewById(R.id.txt_delete_audio);
        mTimer = alertLayout.findViewById(R.id.timer_play);

        initAlertDialog(alertLayout);

        mPlayer = new MediaPlayer();
        mTimer.setText(audioTime);

        /*Pause audio review*/
        mImgPauseRecAudio.setOnClickListener(v -> {
            mImgPlayRecAudio.setVisibility(View.VISIBLE);
            mImgPauseRecAudio.setVisibility(View.GONE);
            isPaused = true;
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
            resumePosition = mPlayer.getCurrentPosition();
        });

        /*Play audio review*/
        mImgPlayRecAudio.setOnClickListener(v -> {
            mImgPlayRecAudio.setVisibility(View.GONE);
            mImgPauseRecAudio.setVisibility(View.VISIBLE);
            setAudioTimer((long) seconds * MILLI_TO_SEC);
            initMediaPlayer();
            if (ZERO != resumePosition) {
                setAudioTimer(timeRemaining);
                mPlayer.seekTo(resumePosition);
                mPlayer.start();
            } else {
                if (null != mPlayer && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.reset();
                }
            }
        });

        /*Finish audio review*/
        mBtnDoneAudio.setOnClickListener(v -> {
            isCanceled = true;
            if (null != mPlayer && mPlayer.isPlaying()) {
                isPaused = false;
                resumePosition = ZERO;
                mPlayer.stop();
                mPlayer.reset();
            }
            dialog.dismiss();
        });

        /*Delete audio review*/
        mTxtDeleteAudio.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getString(R.string.msg_delete_review))
                    .setPositiveButton(getString(R.string.yes), (dialog1, which) -> {
                        mPlayer = new MediaPlayer();
                        mPlayer.stop();
                        mPlayer.reset();
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
                    .setNegativeButton(getString(R.string.not_now), (dialog1, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(savedAudioPath.toString());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAudioTimer(long millisInFuture) {
        isPaused = false;
        isCanceled = false;
        new CountDownTimer(millisInFuture, MILLI_TO_SEC) {
            public void onTick(long millisUntilFinished) {
                if (isPaused || isCanceled) {
                    cancel();
                } else {
                    long sec = millisUntilFinished / MILLI_TO_SEC;
                    mTimer.setText(getString(R.string.str_timer) + (sec < INT_TEN ? getString(R.string.int_zero) + sec : sec));
                    timeRemaining = millisUntilFinished;
                }
            }

            public void onFinish() {
                resumePosition = ZERO;
                mImgPlayRecAudio.setVisibility(View.VISIBLE);
                mImgPauseRecAudio.setVisibility(View.GONE);
                mTimer.setText(audioTime);
            }
        }.start();
    }

    /*Add audio review*/
    @OnClick(R.id.img_audio_review)
    public void imgAddReview() {
        if (checkPermission()) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.layout_record_review, null);
            initAlertDialog(alertLayout);

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
        } else {
            requestPermission();
        }
    }

    private void setAudioTime() {
        long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
        int hour = (int) (time / TIME_HOUR);
        int min = (int) (time - hour * TIME_HOUR) / TIME_MINUTE;
        seconds = (int) (time - hour * TIME_HOUR - min * TIME_MINUTE) / TIME_SECONDS;
        audioTime = (min < INT_TEN ? getString(R.string.int_zero) + min : min) + ":"
                + (seconds < INT_TEN ? getString(R.string.int_zero) + seconds : seconds);
        mAudioTime.setText(audioTime);
    }

    private void startRecording() {
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
                    builder.setMessage(getString(R.string.audio_review_limit_msg))
                            .setNeutralButton(getString(R.string.ok), (dialog, which) -> {
                                stopRecording();
                                setAudioTime();
                                dialog.dismiss();
                            })
                            .show();
                }
            });
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setMediaRecorder() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(savedAudioPath.toString());
    }

    private void stopRecording() {
        mChronometer.stop();
        mRecorder.stop();
        mRecorder.release();
    }

    private File getOutputMediaFile() {
        File saveFilePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getString(R.string.app_name));
        if (!saveFilePath.exists()) {
            if (!saveFilePath.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(getString(R.string.date_time_pattern)).format(new Date());
        return new File(saveFilePath.getPath() + File.separator +
                getString(R.string.aud) + timeStamp + getString(R.string.audio_extension));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ReviewActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (ZERO < grantResults.length) {

                    boolean StoragePermission = grantResults[ZERO] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[ONE] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(ReviewActivity.this, getString(R.string.permission_granted), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ReviewActivity.this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int resultAudio = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        return resultStorage == PackageManager.PERMISSION_GRANTED && resultAudio == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof SnapNShareResponse) {
            SnapNShareResponse mSnapResponse = (SnapNShareResponse) value;
            Intent intent = new Intent(ReviewActivity.this, ShareReviewActivity.class);
            intent.putExtra(getString(R.string.intent_review), mSnapResponse);
            intent.putExtra(getString(R.string.image_path), image_path);
            intent.putExtra(getString(R.string.review_rest_id), restId);
            startActivity(intent);
        }
        if (value instanceof SnapXUserResponse) {
            SnapXUserResponse snapXUserResponse = (SnapXUserResponse) value;
            mToken = snapXUserResponse.getUserInfo().getToken();
            dialog.dismiss();
            callApiReview();
        }
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    if (null == mToken) {
                        mPresenter.sendReview(utility.getAuthToken(this), restId, fileImageUri, audioFile, textReview, rating);
                    } else {
                        mPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
                    }
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                if (null == mToken) {
                    mPresenter.sendReview(utility.getAuthToken(this), restId, fileImageUri, audioFile, textReview, rating);
                } else {
                    mPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
                }
            }
        });
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
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPlayer && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.reset();
        }
    }

    /**
     * Function not used as not implemented in iOS
     */
    private void limitTextReview() {
        mEditTxtReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (UIConstants.REVIEW_LENGTH_LIMIT == s.length()) {
                    mTxtLengthError.setVisibility(View.VISIBLE);
                    hideTheKeyboard(ReviewActivity.this, mEditTxtReview);
                } else {
                    mTxtLengthError.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * hide keyboard when text review limit exceeds
     **/
    public void hideTheKeyboard(Context context, EditText editText) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert null != manager;
        manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mPlayer && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
        try {
            mPlayer.setDataSource(savedAudioPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnCompletionListener(this);
        mTimer.setText(utility.milliSecondsToTimer(mPlayer.getCurrentPosition()));
    }
}