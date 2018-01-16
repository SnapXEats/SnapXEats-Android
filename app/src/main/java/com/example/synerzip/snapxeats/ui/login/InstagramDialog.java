package com.example.synerzip.snapxeats.ui.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;

/**
 * Created by Prajakta Patil on 11/1/18.
 */
public class InstagramDialog extends Dialog {

    private static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    private static final int MARGIN = 4;
    private static final int PADDING = 4;
    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog progressDialog;
    private WebView mWebView;
    private LinearLayout mLinearLayout;
    private TextView mTitle;
    private Context context;

    public InstagramDialog(Context context, String url,
                           OAuthDialogListener listener) {
        super(context);
        this.context = context;
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage(context.getString(R.string.loading));

        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

        setUpTitle();
        setUpWebView();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        addContentView(mLinearLayout, new FrameLayout.LayoutParams(display.getWidth(), display.getHeight()));

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTitle = new TextView(getContext());
        mTitle.setText(context.getString(R.string.instagram));
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(Color.BLACK);
        mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
        mLinearLayout.addView(mTitle);
        mLinearLayout.setPadding(0, 0, 0, 0);
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mLinearLayout.addView(mWebView);
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
            if (NetworkUtility.isNetworkAvailable(context)) {
                progressDialog.show();
            } else {
                AlertDialog optionDialog = new AlertDialog.Builder(context).create();
                optionDialog.setMessage(context.getString(R.string.check_network));
                optionDialog.setButton(context.getString(R.string.ok), (dialog, which) -> {
                    Snackbar snackbar = Snackbar
                            .make(mWebView, context.getString(R.string.check_network), Snackbar.LENGTH_INDEFINITE)
                            .setAction(context.getString(R.string.retry), view1 -> mWebView.loadUrl(mUrl));
                    snackbar.setActionTextColor(Color.RED);
                    Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

                    layout.setPadding(0, 0, 0, 0);
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
            String title = mWebView.getTitle();
            if (title != null && title.length() > 0) {
                mTitle.setText(title);
            }
            progressDialog.dismiss();
        }
    }

    public interface OAuthDialogListener {
        void onComplete(String accessToken);
        void onError(String error);
    }
}
