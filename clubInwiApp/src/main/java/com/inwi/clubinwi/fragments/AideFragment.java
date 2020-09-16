package com.inwi.clubinwi.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;

public class AideFragment extends BaseFragment {

    private Context mContext;
    private View mView;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private LinearLayout emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_aide, container, false);
        mContext = getActivity();
        init(mView);

        return mView;
    }

    @SuppressLint("NewApi")
    private void init(View mView) {

        String subParam = getArguments().getString("subParam");
        String url = "";
        switch (subParam) {
            case "aide":
                url = "http://rct.club.inwi.ma/sites/default/files/faq2.html";
                break;
            case "apropos":
                url = "http://dev2.clubinwi.mobiblanc.com/sites/default/files/aboutus2.html";
                break;
            case "condition":
                url = "http://rct.club.inwi.ma/sites/default/files/cgu2.html";
                break;
        }

        mWebView = mView.findViewById(R.id.myWebView);
        mProgressBar = mView.findViewById(R.id.myProgressBarAide);
        emptyLayout = mView.findViewById(R.id.layoutParamEmpty);
        mWebView.setWebViewClient(new myWebClient());
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainActivity) mContext).changeTitle("Club inwi");
        ((MainActivity) mContext).showActionBar();
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            emptyLayout.setVisibility(View.VISIBLE);
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            message += " Do you want to continue anyway?";

            builder.setTitle("SSL Certificate Error");
            builder.setMessage(message);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}