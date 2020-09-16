package com.inwi.clubinwi.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ForgotPassword extends BaseFragment {

    protected static final String TAG = "ForgotPassword";
    private Context mContext;
    private View mView;
    private TextView valider;
    private EditText email;
    private ImageView close;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(getString(R.string.mot_de_passe_oublie));
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);

        email = mView.findViewById(R.id.et_phone);
        valider = mView.findViewById(R.id.valider);
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(v -> ((LoginActivity) mContext).onBackPressed());

        valider.setOnClickListener(v -> {
            if (email.getText().toString() != null || email.getText().toString().length() > 0/* || Utils.isMaileValid(email.getText().toString())*/) {
                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                progressDialog.show();
                forgotPassword(email.getText().toString());
                Utils.saveToSharedPreferences(getActivity(), Constants.USER_PHONE, email.getText().toString());

            } else {

            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void forgotPassword(final String email) {

        StringRequest sr = new StringRequest(Request.Method.POST, Constants.URL_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                MyLog.e("SUCCESS FORGOT PASSWORD", response);
                try {
                    String header = new JSONObject(response).getString("header");
                    if (header.equals("OK"))
                        Utils.switchFragment((FragmentActivity) mContext, new ForgotPasswordResult(), ForgotPasswordResult.class.toString(), R.id.content_login, true, true, true);
                    else
                        Utils.showToastOnUiTread(mContext, new JSONObject(response).getString("message"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, "Veuillez réessayer ultérieurement ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                progressDialog.dismiss();
                Utils.showToastOnUiTread(mContext, "Veuillez réessayer ultérieurement ");
                // forgotPassword(email);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telephone", email);
                params.put("lang",Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE));
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, Constants.URL_FORGOT_PASSWORD);
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
