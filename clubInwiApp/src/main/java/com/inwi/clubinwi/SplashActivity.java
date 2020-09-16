package com.inwi.clubinwi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.achoura.AchouraUtils;
import com.inwi.clubinwi.fragments.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2000;
    private Context mContext;
    private long mRequestStartTime;
    private RelativeLayout layoutInterstitial;
    private ImageView imageInterstitial;
    private ImageView closeInterstitial;
    private Button buttonNotMemberInwi;
    private Button buttonMemberInwi;

    public String userLangue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


         //selectlanguage(mContext, Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
        //selectlanguage(mContext);
       // selectlanguage(mContext, Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));

     //    SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
      // mSharedPreferences.edit().clear().apply();


        if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)) || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))) {

            new Handler().postDelayed(new Runnable() {
                public void run() {
                     selectlanguage(mContext, Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                     startApp();
                    //LoadPub();
                }
            }, SPLASH_DISPLAY_TIME);

        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if(!isFinishing())
                    showDialog(SplashActivity.this);
                    //   startApp();
                }
            }, SPLASH_DISPLAY_TIME);
        }
        init();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_gray));
        }
        new SplashFakeLoading().execute();
    }

    void init() {
        layoutInterstitial = findViewById(R.id.layout_interstitial);
        imageInterstitial = findViewById(R.id.image_interstitial);
        closeInterstitial = findViewById(R.id.close_interstitial);
        buttonMemberInwi = findViewById(R.id.member_inwi);
        buttonNotMemberInwi = findViewById(R.id.not_member_inwi);

        buttonNotMemberInwi.setOnClickListener(view -> {
            Intent openFragmentInscription = new Intent(SplashActivity.this, LoginActivity.class);
            openFragmentInscription.putExtra("OPEN_FRAGMENT_B", "SOME_VALUE");
            startActivity(openFragmentInscription);
        });

        buttonMemberInwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openWebURL("https://iflix.onelink.me/QRBE/6a9203bc", SplashActivity.this);
            }
        });

        closeInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutInterstitial.setVisibility(View.GONE);
                startApp();
                if (Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN) != null) {
                    Intent startMain = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(startMain);
                } else {
                    Intent openFragmentHome = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(openFragmentHome);
                }
            }
        });
    }

    private void LoadPub() {
        String url = Constants.URL_BASE + "/api/get/interstitiel?lang=" + Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE);
        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS", response);
                try {
                    if (response != null) {
                        JSONObject object = new JSONObject(response);
                        String header = object.getJSONObject("header").getString("status");
                        if (header.equals("OK")) {

                            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                            JSONObject result = new JSONObject(response).getJSONObject("results");
                            String URL = result.getString("image");
                            String urlDejaMembre = result.getString("url_deja_membre");
                            String urlModeConnecte = result.getString("url_mode_connecte");
                            String urlModeConnecteAr = result.getString("url_mode_connecte_ar");

                            Utils.saveToSharedPreferences(mContext, Constants.URL_DEJA_MEMBER, urlDejaMembre);
                            Utils.saveToSharedPreferences(mContext, Constants.URL_MODE_CONNECT, urlModeConnecte);
                            Utils.saveToSharedPreferences(mContext, Constants.URL_MODE_CONNECT_AR, urlModeConnecteAr);

                            buttonMemberInwi.setText(R.string.member_inwi);
                            buttonNotMemberInwi.setText(R.string.not_member_inwi);

                            imageLoader.get(URL, new ImageLoader.ImageListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                    if (response.getBitmap() != null) {
                                        imageInterstitial.setImageBitmap(response.getBitmap());
                                        layoutInterstitial.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    layoutInterstitial.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                Log.e("error intersitiele", "test" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
              //  params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };

        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        sr.setShouldCache(false);
        queue.add(sr);
    }

    public void startApp() {
        if (!isFinishing()) {
            Intent mIntent;
            if (Utils.readFromSharedPreferences(this, Constants.USER_TOKEN) != null) {
                mIntent = new Intent(this, MainActivity.class);
            } else {
                mIntent = new Intent(this, LoginActivity.class);
            }
            startActivity(mIntent);
            finish();
        }
    }

    public void showDialog(Activity activity) {

        final String[] langue = {"fr"};
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_langue);

        TextView validate = dialog.findViewById(R.id.validate_langue);
        final ImageView switchLangue = dialog.findViewById(R.id.buttonSwitchLangue);

        if (AchouraUtils.getAppLocale(this).equalsIgnoreCase("fr")) {
            switchLangue.setImageResource(R.drawable.switchon);
            langue[0] = "fr";
        } else  if (AchouraUtils.getAppLocale(this).equalsIgnoreCase("ar")) {
            switchLangue.setImageResource(R.drawable.switchon_ar);
            langue[0] = "ar";
        }

        switchLangue.setOnClickListener(view -> {
            if (langue[0].equals("fr")) {
                switchLangue.setImageResource(R.drawable.switchon_ar);
                langue[0] = "ar";
            } else  if (langue[0].equals("ar")){
                switchLangue.setImageResource(R.drawable.switchon);
                langue[0] = "fr";
            }
        });


        validate.setOnClickListener(view -> {
            selectlanguage(mContext, langue[0]);
            startApp();
            dialog.dismiss();
            //LoadPub();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(mContext!=null && !isFinishing())
        dialog.show();

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

    public void selectlanguage(Context context) {

        Locale locale = new Locale("fr");
        Locale.setDefault(locale);
        Resources res = context.getResources();

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
        Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, "fr");

    }

    private class SplashFakeLoading extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRequestStartTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

}	
