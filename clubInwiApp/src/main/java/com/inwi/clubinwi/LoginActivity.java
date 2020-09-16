package com.inwi.clubinwi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.fragments.HomeLoginFragment;
import com.inwi.clubinwi.fragments.InscriptionStep1;

import java.util.Locale;

@SuppressLint("NewApi")
public class LoginActivity extends FragmentActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Fragment mContent;
    private Context mContext;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        mContext = this;

        if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)) || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))) {
            selectlanguage(mContext, Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
        }

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                Log.d(TAG, "onFragmentViewCreated: " + f.getClass().getSimpleName());
            }
        }, true);

        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        if (getIntent().hasExtra("OPEN_FRAGMENT_B")) {
            mContent = new InscriptionStep1();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_login, mContent).commit();
        } else {
            mContent = new HomeLoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_login, mContent).commit();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_gray));
        }
///////////////////////


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(progressDialog != null && progressDialog.isShowing() )
            progressDialog.dismiss();
        super.onDestroy();
    }




    public void selectlanguage(Context context, String langue) {

        Locale locale = new Locale(langue);
        Locale.setDefault(locale);
        Resources res = context.getResources();

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
        } else {
            config.locale = locale;
        }
        Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, langue);
        res.updateConfiguration(config, res.getDisplayMetrics());


    }

}
