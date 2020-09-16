package com.inwi.clubinwi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PdfActivity extends AppCompatActivity {

    private WebView webview;

    /**
     * Starter method
     *
     * @param activity the caller activity
     * @param url      the pdf's url
     */
    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, PdfActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent mintent = getIntent();
        String url = mintent.getExtras().getString("url");

        webview = findViewById(R.id.webview);

        // webview.getSettings().setJavaScriptEnabled(true);
        // webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new WebClient());
        //  String url = getIntent().getStringExtra("url");
        Log.e("test url pdf", url);
        if (url != null && !url.isEmpty()) {
            webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        }
    }

    public class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            //handler.proceed();

            final AlertDialog.Builder builder = new AlertDialog.Builder(PdfActivity.this);
            builder.setMessage("Invalide");
            builder.setPositiveButton("Continuer", (dialog, which) -> handler.proceed());
            builder.setNegativeButton("Annuler", (dialog, which) -> handler.cancel());
            final AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

}
