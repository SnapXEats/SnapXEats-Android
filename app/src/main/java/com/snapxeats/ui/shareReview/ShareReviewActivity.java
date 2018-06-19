package com.snapxeats.ui.shareReview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.login.InstagramApp;
import com.snapxeats.ui.login.InstagramDialog;
import com.snapxeats.ui.review.ReviewDbHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.constants.UIConstants.IMAGE_TYPE;
import static com.snapxeats.common.constants.UIConstants.INSTA_PACKAGE_NAME;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewActivity extends BaseActivity implements ShareReviewContract.ShareReviewView,
        AppContract.SnapXResults, InstagramDialog.InstagramDialogListener {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    ShareReviewContract.ShareReviewPresenter mPresenter;

    @Inject
    ReviewDbHelper reviewDbHelper;

    @BindView(R.id.toolbar_review)
    protected Toolbar mToolbar;

    private ShareDialog mShareDialog;

    private SnapNShareResponse mSnapResponse;

    private CallbackManager mCallbackManager;

    @BindView(R.id.img_card_rest_image)
    protected ImageView mImgRest;

    @BindView(R.id.txt_card_rest_name)
    protected TextView mImgRestName;

    @BindView(R.id.txt_share_url)
    protected TextView mTxtMessage;

    private String image_path;
    private String photoId;
    private InstagramApp mApp;

    @BindView(R.id.layout_share_review)
    protected LinearLayout mParentLayout;

    @Inject
    DbHelper dbHelper;

    private String instaToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_review);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        dbHelper.setContext(this);
        reviewDbHelper.setContext(this);

        setUpToolbar();
        mShareDialog = new ShareDialog(this);
        mCallbackManager = CallbackManager.Factory.create();
        mSnapResponse = getIntent().getExtras().getParcelable(getString(R.string.intent_review));
        image_path = getIntent().getExtras().getString(getString(R.string.image_path));
        photoId = getIntent().getExtras().getString(getString(R.string.photo_id));

        if (null != mSnapResponse) {

            Glide.with(this)
                    .load(mSnapResponse.getDish_image_url())
                    .placeholder(R.drawable.ic_rest_info_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).thumbnail(THUMBNAIL)
                    .into(mImgRest);
            mImgRestName.setText(mSnapResponse.getRestaurant_name());
            mTxtMessage.setText(mSnapResponse.getMessage());
        }
        initInstagram();
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

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.img_share_fb)
    public void imgFb() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            shareOnFb();
            fbCallback();
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
    }

    private void shareOnFb() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote(mSnapResponse.getMessage())
                    .setContentUrl(Uri.parse(mSnapResponse.getDish_image_url()))
                    .build();
            mShareDialog.show(linkContent);
        }
    }

    private void fbCallback() {
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                reviewDbHelper.deleteDraftData(photoId);
                dialogShareCallback();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void dialogShareCallback() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_success_review, null);
        Button mBtnShare = alertLayout.findViewById(R.id.btn_share_another);
        TextView mTxtDismiss = alertLayout.findViewById(R.id.txt_not_now);
        final AlertDialog.Builder alert = new AlertDialog.Builder(ShareReviewActivity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();

        Intent shareAnotherIntent = new Intent(this, HomeActivity.class);
        shareAnotherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mBtnShare.setOnClickListener(v -> {

            String restId = getIntent().getExtras().getString(getString(R.string.review_rest_id));
            shareAnotherIntent.putExtra(getString(R.string.intent_restaurant_id), restId);
            shareAnotherIntent.putExtra(getString(R.string.share_another), true);
            dialog.dismiss();
            startActivity(shareAnotherIntent);
        });

        mTxtDismiss.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(shareAnotherIntent);
        });
    }

    @OnClick(R.id.img_share_insta)
    public void imgInsta() {
        if (NetworkUtility.isNetworkAvailable(this)) {
            if (!dbHelper.getSnapxDataDao().loadAll().get(ZERO).socialPlatform.equalsIgnoreCase(getString(R.string.platform_instagram))) {
                showInstaWebView();
            } else {
                shareOnInsta();
            }
        } else {
            showNetworkErrorDialog((dialog, which) -> {
            });
        }
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

    private void shareOnInsta() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(INSTA_PACKAGE_NAME);
        if (null != intent) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(INSTA_PACKAGE_NAME);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(image_path));
            shareIntent.setType(IMAGE_TYPE);
            reviewDbHelper.deleteDraftData(photoId);
            startActivity(shareIntent);
        }
    }

    @Override
    public void onReturnValue(String token) {
        showProgressDialog();
        instaToken = token;
        mPresenter.getInstaInfo(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
    public void success(Object value) {
        dismissProgressDialog();
        if (value instanceof SnapXUserResponse) {
            SnapXUserResponse snapXUserResponse = (SnapXUserResponse) value;
            shareOnInsta();
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
                    mPresenter.getInstaInfo(instaToken);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            } else {
                showProgressDialog();
                mPresenter.getInstaInfo(instaToken);
            }
        });
    }

    @Override
    public void networkError(Object value) {
    }
}
