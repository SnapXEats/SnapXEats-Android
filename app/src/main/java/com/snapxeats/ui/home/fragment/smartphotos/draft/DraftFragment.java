package com.snapxeats.ui.home.fragment.smartphotos.draft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.widget.LoginButton;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.model.smartphotos.RestaurantAminities;
import com.snapxeats.common.model.smartphotos.RestaurantAminitiesDao;
import com.snapxeats.common.model.smartphotos.SnapXDraftPhoto;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.home.fragment.smartphotos.AminityAdapter;
import com.snapxeats.ui.login.InstagramApp;
import com.snapxeats.ui.review.ReviewDbHelper;
import com.snapxeats.ui.shareReview.ShareReviewActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.SX_BEARER;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 15/5/18.
 */

public class DraftFragment extends BaseFragment implements View.OnClickListener,
        DraftContract.DraftView,
        AppContract.SnapXResults,
        MediaPlayer.OnCompletionListener {

    @BindView(R.id.listview)
    protected RecyclerView mRecyclerview;

    @BindView(R.id.parent_layout)
    protected LinearLayout mParentLayout;

    @Inject
    ReviewDbHelper reviewDbHelper;

    @Inject
    DbHelper dbHelper;

    @Inject
    AppUtility utility;

    @Inject
    DraftContract.DraftPresenter mDraftPresenter;

    @Inject
    HomeActivity homeActivity;

    private LinearLayout mLayoutControls;
    private LinearLayout mLayoutDescription;
    private LinearLayout mLayoutInfo;
    private LinearLayout mLayoutReview;
    private LinearLayout mLayoutAudio;
    private ListView mListAminities;
    private ImageView mImg;
    private ImageView mImgClose;
    private ImageView mImgInfo;
    private ImageView mImgShare;
    private ImageView mImgAudioReview;
    private ImageView mImgTextReview;
    private ImageView mImgPlayAudio;
    private TextView mTxtRestName;
    private TextView mTxtTimeOfAudio;
    private TextView mTxtRestAddress;
    private TextView mTxtRestReviewContents;
    private SnapXDraftPhoto mSnapXDraftPhoto;
    private List<String> mAminitiesList;
    private boolean isImageTap;
    private boolean isInfoTap;
    private boolean isReviewTap;
    private boolean isAudioViewTap;
    private boolean isAudioPlayTap;
    private InstagramApp mApp;
    private Uri fileImageUri;
    private Uri audioFile;
    private String restId;
    private String textReview;
    private int rating;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private Dialog mDialog;
    private String mToken;
    private AlertDialog dialog;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (null != mMediaPlayer) {
                long currentDuration = mMediaPlayer.getCurrentPosition();

                // Displaying time completed playing
                mTxtTimeOfAudio.setText("" + utility.milliSecondsToTimer(currentDuration));

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, UIConstants.PERCENTAGE);
            }
        }
    };

    @Inject
    public DraftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_draft, container, false);
            ButterKnife.bind(this, view);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        ((HomeActivity) getActivity()).setInstaValue(value -> {
            mToken = value;
            dialog.dismiss();
            callApiReview();
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        List<SnapXDraftPhoto> mDraftPhotoList = reviewDbHelper.getDraftData();
        if (null != mDraftPhotoList && ZERO != mDraftPhotoList.size()) {
            DraftAdapter mDraftAdapter = new DraftAdapter(getActivity(), mDraftPhotoList, (snapXDraftPhoto, viewShare) -> {
                mSnapXDraftPhoto = snapXDraftPhoto;

                QueryBuilder<RestaurantAminities> queryBuilder = dbHelper.getRestaurantAminitiesDao().queryBuilder();
                List<RestaurantAminities> aminitiesList = queryBuilder.where(RestaurantAminitiesDao.Properties.PhotoIdFk.eq(snapXDraftPhoto.getSmartPhoto_Draft_Stored_id())).list();
                mAminitiesList = new ArrayList<>();
                for (RestaurantAminities aminity : aminitiesList) {
                    mAminitiesList.add(aminity.getAminity());
                }
                showSmartPhotoDialog();
                homeActivity.loginWithFacebook();
                ImageView imgShare = viewShare.findViewById(R.id.img_share);
                imgShare.setOnClickListener(v -> {
                    if (utility.isLoggedIn()) {
                        callApiReview();
                    } else {
                        showLoginDialog();
                    }
                });
            });
            mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerview.setAdapter(mDraftAdapter);
        }
    }

    /**
     * show login dialog if user is not logged in
     **/
    private void showLoginDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_dialog_login, null);
        initAlertDialog(alertLayout);
        TextView txtShareLater = alertLayout.findViewById(R.id.txt_login_share_later);
        Button btnCustomFb = alertLayout.findViewById(R.id.btn_fb_custom);
        LoginButton btnFb = alertLayout.findViewById(R.id.btn_facebook_login);
        Button btnInsta = alertLayout.findViewById(R.id.btn_instagram_login);

        btnCustomFb.setOnClickListener(v -> {
            if (v == btnCustomFb) {
                if (NetworkUtility.isNetworkAvailable(getActivity())) {
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

        txtShareLater.setOnClickListener(v -> dialog.dismiss());
    }

    private void showInstaWebView() {
        if (NetworkUtility.isNetworkAvailable(getActivity())) {
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

    private void initInstagram() {
        mApp = new InstagramApp(getActivity(), WebConstants.INSTA_CLIENT_ID, WebConstants.INSTA_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mDraftPresenter.response(SnapXResult.SUCCESS, null);
            }

            @Override
            public void onFail(String error) {
                mDraftPresenter.response(SnapXResult.NETWORKERROR, null);
            }
        });
    }

    /*initialize components for alert dialog*/
    public void initAlertDialog(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();
    }

    /**
     * Show smart photo Dialog
     **/
    private void showSmartPhotoDialog() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.draft_dialog_layout);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.START);
        }

        initViewsForSmartPhotoDialog();
        if (null == mSnapXDraftPhoto.getTextReview() || mSnapXDraftPhoto.getTextReview().isEmpty())
            mImgTextReview.setVisibility(View.GONE);

        if (null == mSnapXDraftPhoto.getAudioURL() || mSnapXDraftPhoto.getAudioURL().isEmpty())
            mImgAudioReview.setVisibility(View.GONE);

        Glide.with(getActivity())
                .load(mSnapXDraftPhoto.getImageURL())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_rest_info_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL)
                .into(mImg);

        //Register listeners
        registerListeners();
        mDialog.show();
    }

    private void initViewsForSmartPhotoDialog() {
        mLayoutDescription = mDialog.findViewById(R.id.layout_description);
        mLayoutControls = mDialog.findViewById(R.id.layout_controls);
        mLayoutInfo = mDialog.findViewById(R.id.layout_info);
        mLayoutReview = mDialog.findViewById(R.id.layout_review);
        mLayoutAudio = mDialog.findViewById(R.id.layout_audio);

        mTxtRestName = mDialog.findViewById(R.id.txt_rest_name);
        mTxtRestAddress = mDialog.findViewById(R.id.txt_rest_address);
        mTxtRestReviewContents = mDialog.findViewById(R.id.txt_review_contents);
        mTxtTimeOfAudio = mDialog.findViewById(R.id.timer_play);

        mImg = mDialog.findViewById(R.id.image_view);
        mImgClose = mDialog.findViewById(R.id.img_close);
        mImgInfo = mDialog.findViewById(R.id.img_info);
        mImgTextReview = mDialog.findViewById(R.id.img_text_review);
        mImgAudioReview = mDialog.findViewById(R.id.img_audio);
        mImgShare = mDialog.findViewById(R.id.img_share);
        mImgShare.setVisibility(View.VISIBLE);
        mImgPlayAudio = mDialog.findViewById(R.id.img_play_audio);
        mListAminities = mDialog.findViewById(R.id.list_aminities);
    }

    private void registerListeners() {
        mImg.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mImgInfo.setOnClickListener(this);
        mImgTextReview.setOnClickListener(this);
        mImgAudioReview.setOnClickListener(this);
        mImgShare.setOnClickListener(this);
        mImgPlayAudio.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        utility.resetMediaPlayer(mMediaPlayer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utility.resetMediaPlayer(mMediaPlayer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view:
                isImageTap = !isImageTap;
                if (isImageTap) {
                    mLayoutControls.setVisibility(View.VISIBLE);
                } else {
                    mLayoutControls.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_close:
                utility.resetMediaPlayer(mMediaPlayer);
                if (null != mMediaPlayer) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                mDialog.dismiss();
                break;

            case R.id.img_info:
                isInfoTap = !isInfoTap;
                if (isInfoTap) {
                    isAudioViewTap = false;
                    isReviewTap = false;

                    utility.resetMediaPlayer(mMediaPlayer);
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info_selected));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);
                    mListAminities.setAdapter(new AminityAdapter(getActivity(), mAminitiesList));

                    mTxtRestName.setText(mSnapXDraftPhoto.getRestaurantName());
                    mTxtRestAddress.setText(mSnapXDraftPhoto.getRestaurantAddress());
                } else {
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    mLayoutDescription.setVisibility(View.GONE);
                }
                break;

            case R.id.img_text_review:
                isReviewTap = !isReviewTap;
                if (isReviewTap) {
                    isAudioViewTap = false;
                    isInfoTap = false;

                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review_selected));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutReview.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mTxtRestReviewContents.setText(mSnapXDraftPhoto.getTextReview());
                    utility.resetMediaPlayer(mMediaPlayer);

                } else {
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mLayoutDescription.setVisibility(View.GONE);
                }
                break;

            case R.id.img_audio:
                isAudioViewTap = !isAudioViewTap;
                if (isAudioViewTap) {
                    isReviewTap = false;
                    isInfoTap = false;

                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker_selected));
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);

                    mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSnapXDraftPhoto.getAudioURL()));
                    mMediaPlayer.setOnCompletionListener(this);
                    mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));

                } else {
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mLayoutDescription.setVisibility(View.GONE);
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_play_audio:
                isAudioPlayTap = !isAudioPlayTap;
                if (isAudioPlayTap) {
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_pause));
                    mMediaPlayer.start();
                    mUpdateTimeTask.run();
                } else {
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
                break;

            case R.id.img_share:
                if (utility.isLoggedIn()) {
                    callApiReview();
                } else {
                    showLoginDialog();
                }
                break;
        }
    }

    private void callApiReview() {
        textReview = mSnapXDraftPhoto.getTextReview();
        rating = mSnapXDraftPhoto.getRating();
        fileImageUri = Uri.parse(mSnapXDraftPhoto.getImageURL());
        restId = mSnapXDraftPhoto.getRestId();

        if (null != restId && null != fileImageUri && ZERO != rating) {
            audioFile = null;
            if (null != mSnapXDraftPhoto.getAudioURL()) {
                File file = new File(mSnapXDraftPhoto.getAudioURL());
                audioFile = Uri.fromFile(file);
            }
            showProgressDialog();
            if (null == mToken) {
                mDraftPresenter.sendReview(utility.getAuthToken(getActivity()), restId, fileImageUri, audioFile, textReview, rating);
            } else {
                mDraftPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
            }
        }
    }

    @Override
    public void initView() {
        reviewDbHelper.setContext(getActivity());
        dbHelper.setContext(getActivity());
        mDraftPresenter.addView(this);
        initInstagram();
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    @Override
    public void success(Object value) {
        mDialog.dismiss();
        dismissProgressDialog();
        if (value instanceof SnapNShareResponse) {
            SnapNShareResponse mSnapResponse = (SnapNShareResponse) value;
            Intent intent = new Intent(getActivity(), ShareReviewActivity.class);
            intent.putExtra(getString(R.string.photo_id), mSnapXDraftPhoto.getSmartPhoto_Draft_Stored_id());
            intent.putExtra(getString(R.string.intent_review), mSnapResponse);
            intent.putExtra(getString(R.string.image_path), mSnapXDraftPhoto.getImageURL());
            intent.putExtra(getString(R.string.review_rest_id), mSnapXDraftPhoto.getRestId());
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
                        mDraftPresenter.sendReview(utility.getAuthToken(getActivity()), restId, fileImageUri, audioFile,
                                textReview, rating);
                    } else {
                        mDraftPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
                    }
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                if (null == mToken) {
                    mDraftPresenter.sendReview(utility.getAuthToken(getActivity()), restId, fileImageUri, audioFile,
                            textReview, rating);
                } else {
                    mDraftPresenter.sendReview(SX_BEARER + mToken, restId, fileImageUri, audioFile, textReview, rating);
                }
            }
        });
    }

    @Override
    public void networkError(Object value) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSnapXDraftPhoto.getAudioURL()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
    }
}
