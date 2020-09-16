package com.inwi.clubinwi.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faradaj.patternededittext.PatternedEditText;
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

public class InscriptionStep1 extends BaseFragment {

    protected static final String TAG = "InscriptionStep1";
    private Context mContext;
    private View mView;
    private TextView valider;
    private PatternedEditText mPatternedEditText;
    private ProgressDialog progressDialog;
    private ImageView close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_inscription_step1, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.subscribe);

        mPatternedEditText = mView.findViewById(R.id.number_phone);
        valider = mView.findViewById(R.id.valider);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);

        close = mView.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ((Activity) mContext).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                ((LoginActivity) mContext).onBackPressed();
            }
        });

        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPatternedEditText.getRawText() == null || mPatternedEditText.getRawText().length() < 10) {
                    Utils.showToastOnUiTread(mContext, "Vérifier le champs");
                } else {
                    View view = ((Activity) mContext).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if (Utils.isOnline(mContext)) {
                        if (mPatternedEditText.getRawText().toString().length() == 0) {
                            Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
                            return;
                        } else
                            preinscription(mPatternedEditText.getRawText());
                    } else
                        Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
                }

            }
        });
    }

    private void preinscription(final String phone) {
        progressDialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.URL_PREINSCRIPTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS VERIFY PHONE NUMBER", response);
                try {
                    if (response != null) {
                        JSONObject object = new JSONObject(response);
                        String header = object.getString("header");
                        if (header.equals("OK")) {
                            Fragment fragment = new InscriptionStep2();
                            Bundle args = new Bundle();
                            args.putString("result", object.getJSONObject("result").toString());
                            args.putString("telephone", phone);
                            fragment.setArguments(args);
                            Utils.switchFragment((FragmentActivity) mContext, fragment, InscriptionStep2.class.toString(), R.id.content_login, true, true, true);
                        } else if (header.equals("NOK") && object.getInt("status") == 401) {
                            Utils.showToastOnUiTread(mContext, object.getString("message"));
                        } else if (header.equals("NOK") && object.getInt("status") == 403) {
                            AlertDialog.Builder builder;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                                builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth));
                            } else {
                                builder = new AlertDialog.Builder(mContext);
                            }

                            builder.setMessage("ce compte existe déjà mais est en attente d'activation. Voulez-vous l'activer?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Fragment fragment = new InscriptionStep3();
                                    Bundle args = new Bundle();
                                    args.putString("telephone", phone);
                                    fragment.setArguments(args);
                                    Utils.switchFragment((FragmentActivity) mContext, fragment, InscriptionStep3.class.toString(), R.id.content_login, true, true, true);
                                }
                            }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                            builder.show();
                        } else {
                            Utils.showToastOnUiTread(mContext, object.getString("message"));
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, "Veuillez vérifier les champs saisies");
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telephone", phone);
                params.put("lang", Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE));
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
        int socketTimeout = 30000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, Constants.URL_PREINSCRIPTION);
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