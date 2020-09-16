package com.inwi.clubinwi.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class IflixFragment extends Fragment {

    private WebView webView;
    private ProgressDialog progressDialog;


    public IflixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_iflix, container, false);
        webView = view.findViewById(R.id.web_view_iflix);
        progressDialog = new ProgressDialog(getContext());
        String url;

        if ("ar".equalsIgnoreCase(Utils.readFromSharedPreferences(getActivity(), Constants.USER_LANGUE))) {

            url = Utils.readFromSharedPreferences(getContext(), Constants.URL_MODE_CONNECT_AR);
        } else {
            url = Utils.readFromSharedPreferences(getContext(), Constants.URL_MODE_CONNECT);

        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        webView.loadUrl(url);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeTitle(getString(R.string.iflix));
        ((MainActivity) getActivity()).showActionBar();
    }
}
