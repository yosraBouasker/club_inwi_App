package com.inwi.clubinwi.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.faradaj.patternededittext.PatternedEditText;
import com.faradaj.patternededittext.PetSavedState;
import com.google.firebase.iid.FirebaseInstanceId;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.BuildConfig;
import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.achoura.AchouraUtils;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.inwi.clubinwi.fragments.HomeLoginFragment.isAppInstalled;

public class LoginFragment extends BaseFragment {
    protected static final String TAG = "LoginFragment";
    String push_status = "0";
    String app_version, gcm_token, token, device_uuid, model, os, gcm_token_v;
    private Context mContext;
    private View mView;
    private PatternedEditText mPatternedEditText;
    private TextView valider, inscription, forgotPassword;
    private EditText password;
    private CheckBox showPassword;
    private ProgressDialog progressDialog;
    Bundle statesave ;
    private ImageView close;
    private WebView pub;
    private boolean ifAppFound = false;
    private String packageOtherApp = "ma.inwi.selfcaremobile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    @Override
    public void onDestroy() {
        if(progressDialog!= null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.connexion);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);
        password = mView.findViewById(R.id.password);
        mPatternedEditText = mView.findViewById(R.id.number_phone);
        valider = mView.findViewById(R.id.valider);
        inscription = mView.findViewById(R.id.inscription);
        showPassword = mView.findViewById(R.id.show_number);
        forgotPassword = mView.findViewById(R.id.forgot_password);
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(v -> {
            View view = ((Activity) mContext).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            ((LoginActivity) mContext).onBackPressed();
        });

        valider.setOnClickListener(v -> {
            if (mPatternedEditText.getRawText() == null || mPatternedEditText.getRawText().length() < 10 && password.getText().toString() == null) {
                Utils.showToastOnUiTread(mContext, "Veuillez resseyer");
            } else {
                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (Utils.isOnline(mContext)) {
                    progressDialog.show();
                    signIn(mPatternedEditText.getRawText(), password.getText().toString());
                } else
                    Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
            }
        });
        ifAppFound = isAppInstalled(mContext, packageOtherApp);

        inscription.setOnClickListener(v -> {

            if (ifAppFound) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageOtherApp);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(packageOtherApp)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageOtherApp)));
                }
            }
        });

        forgotPassword.setOnClickListener(v -> {
            View view = ((Activity) mContext).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (Utils.isOnline(mContext)) {
                Utils.switchFragment((FragmentActivity) mContext, new ForgotPassword(), ForgotPassword.class.toString(), R.id.content_login, true, true, true);
            } else
                Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
        });

        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            password.setSelection(password.getText().length());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            password.setText("");
        } catch (Exception e) {
            Log.e(TAG, "onResume: ", e);
        }
    }

    public void signIn(final String phone, final String password) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
           //  String url_login = "https://club.inwi.ma/api/client/login";
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.URL_SIGNIN, new Response.Listener<String>() {
        //    StringRequest sr = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
                @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS LOGIN", response);
                    if(progressDialog != null && progressDialog.isShowing() && !getActivity().isFinishing())
                        progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String header = object.getString("header");
                    if (header.equals("OK")) {
                        JSONObject jsonObject = new JSONObject(response).getJSONObject("result");
                        Log.d(TAG, "onResponse: SESSION_USER_TOKEN: " + jsonObject.getString("token"));
                        Log.d(TAG, "onResponse: SESSION_USER_PHONE: " + phone);
                       // System.out.println("rachid reponse :"+jsonObject.toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PHONE, phone);
                        Utils.saveToSharedPreferences(mContext, Constants.USER_TOKEN, jsonObject.getString("token"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FULLNAME, jsonObject.getString("full_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FIRST_NAME, jsonObject.getString("first_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_LAST_NAME, jsonObject.getString("last_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_POINT, jsonObject.getString("points"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_CNI, jsonObject.getString("cni"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_AVATAR, jsonObject.getString("avatar"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_EMAIL, jsonObject.getString("email_address"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS, jsonObject.getJSONObject("filleuls").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS_COUNT, String.valueOf(jsonObject.getJSONObject("filleuls").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS, jsonObject.getJSONObject("forfaits").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF, jsonObject.getJSONObject("forfaits").getJSONArray("actif").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT, String.valueOf(jsonObject.getJSONObject("forfaits").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL, String.valueOf(jsonObject.getJSONObject("level").getInt("num")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_CADEAUX, String.valueOf(jsonObject.getInt("cadeaux")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_DATE, String.valueOf(jsonObject.getString("created_at")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_BIRTHDAY, String.valueOf(jsonObject.getString("birthdate")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PUSH_STATUS, String.valueOf(jsonObject.getBoolean("push_status")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_ZIPCODE, String.valueOf(jsonObject.getString("zipcode")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_ADDRESS, String.valueOf(jsonObject.getString("address")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_CITY, String.valueOf(jsonObject.getString("city")));
                        if (jsonObject.getJSONObject("level").getInt("num") == 7)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi");
                        else if (jsonObject.getJSONObject("level").getInt("num") == 8)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi Premium");
                        Intent mIntent = new Intent(mContext, MainActivity.class);
                        startActivity(mIntent);
                        ((Activity) mContext).finish();
                    } else if (header.equals("NOK") && object.getInt("status") == 403) {
                        Fragment fragment = new InscriptionStep3();
                        Bundle args = new Bundle();
                        args.putString("telephone", phone);
                        fragment.setArguments(args);
                        Utils.switchFragment((FragmentActivity) mContext, fragment, InscriptionStep3.class.toString(), R.id.content_login, true, true, true);
                    } else if (header.equals("NOK") && object.getInt("status") == 201)
                        Utils.showToastOnUiTread(mContext, new JSONObject(response).getString("message"));
                    else
                        Utils.showToastOnUiTread(mContext, new JSONObject(response).getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyLog.e("ERROR JSONException", "" + e.getMessage());

                    Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR LOGIN", "" + error.getMessage());
                if(progressDialog != null && progressDialog.isShowing() && getActivity()!= null && !getActivity().isFinishing())
                progressDialog.dismiss();
                if(mContext!=null && isAdded())
                Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", phone);
                params.put("password", password);
                params.put("device_uid", device_uuid + "");
                params.put("lang", AchouraUtils.getAppLocale(getContext()));
                /*if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))
                        || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))) {
                    params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                } else {
                    params.put("lang", "fr");
                }*/
                 //params.put("lang", "fr");
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

               // params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Content-Type", "application/x-www-form-urlencoded");
              //  params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");
                return params;
            }
        };
        int socketTimeout = 20000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        sr.setShouldCache(false);
        queue.add(sr);
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