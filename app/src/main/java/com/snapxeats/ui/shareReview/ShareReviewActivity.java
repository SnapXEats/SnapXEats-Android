package com.snapxeats.ui.shareReview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.model.review.SnapNShareResponse;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareFragment;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.constants.UIConstants.IMAGE_TYPE;
import static com.snapxeats.common.constants.UIConstants.INSTA_PACKAGE_NAME;

/**
 * Created by Prajakta Patil on 23/4/18.
 */
public class ShareReviewActivity extends BaseActivity implements ShareReviewContract.ShareReviewView,
        AppContract.SnapXResults {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    ShareReviewContract.ShareReviewPresenter mPresenter;

    @BindView(R.id.toolbar_review)
    protected Toolbar mToolbar;

    private ShareDialog mShareDialog;

    private SnapNShareResponse mSnapResponse;

    private CallbackManager mCallbackManager;

    @BindView(R.id.img_card_rest_image)
    protected ImageView mImgRest;

    @BindView(R.id.txt_card_rest_name)
    protected TextView mImgRestName;

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
        setUpToolbar();
        mShareDialog = new ShareDialog(this);
        mCallbackManager = CallbackManager.Factory.create();
        mSnapResponse = getIntent().getExtras().getParcelable(getString(R.string.intent_review));
        if (null != mSnapResponse.getRestaurant_name() && null != mSnapResponse.getDish_image_url()) {
            Picasso.with(this).load(mSnapResponse.getDish_image_url()).placeholder(R.drawable.ic_restaurant_placeholder).into(mImgRest);
            mImgRestName.setText(mSnapResponse.getRestaurant_name());
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.toolbar_snap_share));
    }

    @OnClick(R.id.img_share_fb)
    public void imgFb() {
        shareOnFb();
        fbCallback();
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
        mBtnShare.setOnClickListener(v -> {
            Fragment fragment = new SnapShareFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        });
        mTxtDismiss.setOnClickListener(v -> dialog.dismiss());
    }

    @OnClick(R.id.img_share_insta)
    public void imgInsta() {
        shareOnInsta();
    }

    private void shareOnInsta() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(INSTA_PACKAGE_NAME);
        if (null != intent) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(INSTA_PACKAGE_NAME);
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                        mSnapResponse.getDish_image_url(), "", "")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            shareIntent.setType(IMAGE_TYPE);
            startActivity(shareIntent);
        }
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
}