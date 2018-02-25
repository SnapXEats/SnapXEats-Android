package com.snapxeats.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.RootInstagram;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 11/1/18.
 */
public class InstagramDialog extends Dialog {

    private static final FrameLayout.LayoutParams mFrameLayout = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private static final int PADDING = 20;
    private static final int WEBVIEW_SCALE = 210;
    private String mInstaUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog progressDialog;
    private WebView mWebView;
    private LinearLayout mLinearLayout;
    private Context mContext;

    InstagramDialog(Context mContext, String url, OAuthDialogListener listener) {
        super(mContext);
        this.mContext = mContext;
        mInstaUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(mContext.getString(R.string.loading));

        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        addContentView(mLinearLayout, new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));
        setUpTitle();
        setUpWebView();
    }

    private void setUpTitle() {
        ImageView mTitle = new ImageView(getContext());
        mTitle.setImageDrawable(getContext().getDrawable(R.drawable.ic_insta_cancel));
        mTitle.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mTitle.setPadding(PADDING, PADDING, PADDING, PADDING);
        mLinearLayout.addView(mTitle);

        mTitle.setOnClickListener(v -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            InstagramDialog.this.dismiss();
        });
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.loadUrl(mInstaUrl);
        mWebView.setLayoutParams(mFrameLayout);
        mWebView.setInitialScale(WEBVIEW_SCALE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mLinearLayout.addView(mWebView);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                InstagramDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (NetworkUtility.isNetworkAvailable(mContext)) {
                if (!((Activity) mContext).isFinishing()) {
                    progressDialog.show();
                }
            } else {
                AlertDialog optionDialog = new AlertDialog.Builder(mContext).create();
                optionDialog.setMessage(mContext.getString(R.string.check_network));
                optionDialog.setButton(mContext.getString(R.string.ok), (dialog, which) -> {
                    Snackbar snackbar = Snackbar
                            .make(mWebView, mContext.getString(R.string.check_network), Snackbar.LENGTH_INDEFINITE)
                            .setAction(mContext.getString(R.string.retry), view1 -> mWebView.loadUrl(mInstaUrl));
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();

                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                });
                optionDialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();
        }
    }


    public interface OAuthDialogListener {
        void onComplete(String accessToken);
        void onError(String error);
    }
}
