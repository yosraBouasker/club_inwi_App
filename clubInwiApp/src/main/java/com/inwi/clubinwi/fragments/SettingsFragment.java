package com.inwi.clubinwi.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SettingsFragment extends BaseFragment implements OnClickListener {

    protected static final String TAG = "HomeFragment";
    public String userLangue;
    TabLayout tabLayout;
    private ImageView buttonPushNotification;
    private ImageView switchLangue;
    private RelativeLayout layoutAide, layoutAPropos, layoutCondition, layoutInviterAmi;
    private TextView textLayoutAPropos, textView_version;
    private Context mContext;
    private View mView;
    private ProgressDialog progressDialog;
    private Fragment mFragment = null;
    private String mFragmentTag = "";
    private Bundle bundle;
    private TextView valider;
    private String app_version, gcm_token, token, device_uuid, model, os, gcm_token_v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getActivity();

        ViewCompat.setLayoutDirection(mView, ViewCompat.LAYOUT_DIRECTION_LOCALE);
        init(mView);
        return mView;
    }

    @SuppressLint("NewApi")
    private void init(final View mView) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);

        userLangue = Utils.readFromSharedPreferences(getActivity(), Constants.USER_LANGUE);

        textView_version = mView.findViewById(R.id.textView_version);
        layoutAide = mView.findViewById(R.id.layoutAide);
        layoutCondition = mView.findViewById(R.id.layoutCondition);
        buttonPushNotification = mView.findViewById(R.id.buttonPushNotification);
        switchLangue = mView.findViewById(R.id.buttonSwitchLangue);
        layoutInviterAmi = mView.findViewById(R.id.layoutInviterAmi);
        valider = mView.findViewById(R.id.valider);

        if ("fr".equalsIgnoreCase(userLangue)) {
            switchLangue.setImageResource(R.drawable.switchon_ar);
        } else if ("ar".equalsIgnoreCase(userLangue)){
            switchLangue.setImageResource(R.drawable.switchon);
        }
        buttonPushNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("true".equals(Utils.readFromSharedPreferences(mContext, Constants.USER_PUSH_STATUS)))
                    buttonPushNotification.setImageResource(R.drawable.switchon);
                else
                    buttonPushNotification.setImageResource(R.drawable.switchoff);
            }
        });
        switchLangue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("fr".equalsIgnoreCase(userLangue)) {
                    switchLangue.setImageResource(R.drawable.switchon_ar);
                    getFragmentManager().beginTransaction().detach(SettingsFragment.this).attach(SettingsFragment.this).commitAllowingStateLoss();
                    userLangue = "ar";
                    selectlanguage(mContext, userLangue);
                    Utils.saveToSharedPreferences(mContext, Constants.USER_LANGUE, userLangue);
                    ((MainActivity) mContext).updateInfo();
                    getActivity().recreate();
                } else if ("ar".equalsIgnoreCase(userLangue))  {
                    switchLangue.setImageResource(R.drawable.switchon);
                    getFragmentManager().beginTransaction().detach(SettingsFragment.this).attach(SettingsFragment.this).commitAllowingStateLoss();
                    userLangue = "fr";
                    selectlanguage(mContext, userLangue);
                    Utils.saveToSharedPreferences(mContext, Constants.USER_LANGUE, userLangue);
                    ((MainActivity) mContext).updateInfo();
                    getActivity().recreate();
                }
            }
        });

        layoutAide.setOnClickListener(this);
        layoutCondition.setOnClickListener(this);
        layoutInviterAmi.setOnClickListener(this);
        valider.setOnClickListener(this);


        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null)
            textView_version.setText("version " + pInfo.versionName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainActivity) mContext).changeTitle(getString(R.string.settings));
        ((MainActivity) mContext).showActionBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.valider:
                Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/ckdo");
                Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/pages");
                Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/historique");
                Utils.showToastOnUiTread(mContext, "L'opération a été effectuée avec succès");
                break;
            case R.id.layoutAide:
                mFragment = new AideFragment();
                mFragmentTag = AideFragment.class.toString();

                bundle = new Bundle();
                bundle.putString("subParam", "aide");
                mFragment.setArguments(bundle);
                Utils.switchContent(mContext, ((MainActivity) mContext).getSupportFragmentManager(), mFragment, mFragmentTag, R.id.content, true);

                break;
            case R.id.layoutCondition:
                mFragment = new AideFragment();
                mFragmentTag = AideFragment.class.toString();

                bundle = new Bundle();
                bundle.putString("subParam", "condition");
                mFragment.setArguments(bundle);
                Utils.switchContent(mContext, ((MainActivity) mContext).getSupportFragmentManager(), mFragment, mFragmentTag, R.id.content, true);
                break;
            case R.id.buttonPushNotification:
                Log.i("value push", "" + Utils.readFromSharedPreferences(mContext, Constants.USER_PUSH_STATUS));
                if (Utils.readFromSharedPreferences(mContext, Constants.USER_PUSH_STATUS).equals("true")) {
                    controlVersion("0");
                } else {
                    controlVersion("1");

                }
                break;
            case R.id.layoutInviterAmi:

                String shareBody = "https://play.google.com/store/apps/details?id=com.inwi.clubinwi";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Partage Club Inwi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Partager"));
                break;
            default:
                break;
        }

    }

    void controlVersion(final String push) {
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        app_version = pInfo.versionName;

        token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        os = "android";
        model = Build.MODEL;
        try {
            TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            device_uuid = tManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gcm_token = FirebaseInstanceId.getInstance().getToken();

        StringRequest sr = new StringRequest(Request.Method.POST, Constants.URL_CONTROL_VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MyLog.e("SUCCESS LOGIN", response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    int pushStatut = object.getJSONObject("result").getInt("push_status");
                    if (pushStatut == 0) {
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PUSH_STATUS, "false");
                        buttonPushNotification.setImageResource(R.drawable.switchoff);
                    } else {
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PUSH_STATUS, "true");
                        buttonPushNotification.setImageResource(R.drawable.switchon);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR LOGIN", "" + error.getMessage());
                progressDialog.dismiss();
                Utils.showToastOnUiTread(mContext, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token + "");
                params.put("app_version", app_version + "");
                params.put("os", os + "");
                params.put("push_status", push + "");
                params.put("app_token", gcm_token + "");
                params.put("model", model + "");
                params.put("device_uid", device_uuid + "");
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
               // params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, Constants.URL_CONTROL_VERSION);

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
        res.updateConfiguration(config, res.getDisplayMetrics());
        Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, langue);

    }


    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

}