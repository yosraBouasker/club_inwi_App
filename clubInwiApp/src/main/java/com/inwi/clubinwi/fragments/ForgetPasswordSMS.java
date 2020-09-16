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
import java.util.Map;

public class ForgetPasswordSMS extends BaseFragment {

    protected static final String TAG = "LoginFragment";
    private Context mContext;
    private View mView;
    private TextView valider;
    private EditText codeNewPassword, newPassword, confirmNewPassword;
    private ProgressDialog progressDialog;
    private ImageView close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_forget_password_sms, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.create_new_password);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);

        codeNewPassword = mView.findViewById(R.id.codeNewPassword);
        newPassword = mView.findViewById(R.id.newPassword);
        confirmNewPassword = mView.findViewById(R.id.confirmNewPassword);

        valider = mView.findViewById(R.id.valider);
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(v -> ((LoginActivity) mContext).onBackPressed());

        final String phone = Utils.readFromSharedPreferences(getContext(), Constants.USER_PHONE);

        valider.setOnClickListener(v -> {
            if (newPassword.getText().toString().equalsIgnoreCase(confirmNewPassword.getText().toString())) {

                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                progressDialog.show();
                resetPassword(codeNewPassword.getText().toString(), newPassword.getText().toString(), phone);
            } else {
                Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur_code_no_iden));
            }
        });

    }

    private void resetPassword(final String code, final String newPassword, final String telephone) {

        String url = Constants.URL_BASE + "/api/client/request-password/code";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS REINITIALISATION", response);
                progressDialog.dismiss();
                try {
                    String header = new JSONObject(response).getString("header");
                    if (header.equals("OK")) {
                        Utils.switchFragment((FragmentActivity) mContext, new LoginFragment(), LoginFragment.class.toString(), R.id.content_login, true, true, true);
                    } else
                        Utils.showToastOnUiTread(mContext, new JSONObject(response).getString("message"));
//						Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, getResources().getString(R.string.msg_error_generic));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR REINITIALISATION", "" + error.getMessage());
                progressDialog.dismiss();
                // Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
                Utils.showToastOnUiTread(mContext, getResources().getString(R.string.msg_error_generic));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", newPassword);
                params.put("telephone", telephone);
                params.put("code", code);
                params.put("lang",Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE));
                return params;
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
        AppController.getInstance().addToRequestQueue(sr, url);
    }

}