package com.inwi.clubinwi.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class InscriptionStep3 extends BaseFragment {

    protected static final String TAG = "InscriptionStep1";
    private Context mContext;
    private View mView;
    private EditText myEditTextCodeActivation;
    private LinearLayout myLayoutCodeActivation;
    private ProgressDialog progressDialog;
    private ImageView myImageCode1, myImageCode2, myImageCode3, myImageCode4;
    private LinearLayout header;
    private String fromFragment, currentPhoneNumbre = "";
    private ImageView close;
    private MyTextView myTextViewForgetCode;
    private String telephone;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_inscription_step3, container, false);
        mContext = getActivity();
        init(mView);

        Bundle args = getArguments();
        if (args != null) {
            if (args.getString("telephone") != null)
                telephone = args.getString("telephone");
        }

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.activation_code);
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(R.string.connexion);
        header = mView.findViewById(R.id.header);

        Bundle mArgs = getArguments();
        if (mArgs != null)
            fromFragment = mArgs.getString("fromFragment");
        if (fromFragment == null)
            fromFragment = "inscriptionStep2";
        if ("addNewNumber".equals(fromFragment)) {
            header.setVisibility(View.GONE);
            try {
                currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
            } catch (JSONException e1) {
                e1.printStackTrace();
                Toast.makeText(getActivity(), e1.toString(), Toast.LENGTH_SHORT).show();
            }
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((LoginActivity) mContext).onBackPressed();
            }
        });
        myTextViewForgetCode = mView.findViewById(R.id.myTextViewForgetCode);
        myTextViewForgetCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                Bundle args = getArguments();
                if (args != null) {
                    Log.e("telefon", args.getString("telephone"));
                    getCodeSMS(args.getString("telephone"));

                }
            }
        });

        myImageCode1 = mView.findViewById(R.id.myImageCode1);
        myImageCode2 = mView.findViewById(R.id.myImageCode2);
        myImageCode3 = mView.findViewById(R.id.myImageCode3);
        myImageCode4 = mView.findViewById(R.id.myImageCode4);
        myLayoutCodeActivation = mView.findViewById(R.id.myLayoutCodeActivation);
        myEditTextCodeActivation = mView.findViewById(R.id.myEditTextCodeActivation);

        myEditTextCodeActivation.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        myEditTextCodeActivation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0)
                    noNumber();
                else if (s.length() == 1)
                    oneNumber();
                else if (s.length() == 2)
                    twoNumber();
                else if (s.length() == 3)
                    threeNumber();
                else if (s.length() == 4) {
                    progressDialog.show();
                    fourNumber();
                    Bundle args = getArguments();
                    Log.e("telefon", args.getString("telephone") + " + " + s.toString());
                    if (args != null)
                        ActivateAccount(args.getString("telephone"), s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myLayoutCodeActivation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                myEditTextCodeActivation.requestFocus();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });
    }

    private void ActivateAccount(final String phone, final String code) {
        String url = Constants.URL_ACTIVATION;
        token = "";
        if ("addNewNumber".equals(fromFragment)) {
            url = Constants.URL_BASE + "/api/client/telephone-activation";
            Log.e("ADDNUM", url);
            token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        }

        Log.e("token", token + "");
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("header").equals("OK")) {
                        if ("addNewNumber".equals(fromFragment)) {
                            JSONObject js = object.getJSONObject("result");
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS, js.getJSONObject("forfaits").getJSONArray("list").toString());
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT, String.valueOf(js.getJSONObject("forfaits").getInt("count")));
                            ((MainActivity) mContext).updateInfo();
                            Log.i("ssssss", js.getJSONObject("forfaits").getJSONArray("list").toString());
                            getDialogSuccess(phone);
                        } else {
                            Log.e("response", response + "");
//							Utils.saveToSharedPreferences(mContext, Constants.USER_TOKEN, object.getJSONObject("result").getString("token"));
                            JSONObject js = object.getJSONObject("result");
                            Utils.saveToSharedPreferences(mContext, Constants.USER_TOKEN, js.getString("token"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_PHONE, js.getString("telephone"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FULLNAME, js.getString("full_name"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FIRST_NAME, js.getString("first_name"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LAST_NAME, js.getString("last_name"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_CNI, js.getString("cni"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_AVATAR, js.getString("avatar"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_EMAIL, js.getString("email_address"));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS, js.getJSONObject("filleuls").getJSONArray("list").toString());
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS_COUNT, String.valueOf(js.getJSONObject("filleuls").getInt("count")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS, js.getJSONObject("forfaits").getJSONArray("list").toString());
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF, js.getJSONObject("forfaits").getJSONArray("actif").toString());
                            Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT, String.valueOf(js.getJSONObject("forfaits").getInt("count")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL, String.valueOf(js.getJSONObject("level").getInt("num")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_CADEAUX, String.valueOf(js.getInt("cadeaux")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_DATE, String.valueOf(js.getString("created_at")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_BIRTHDAY, String.valueOf(js.getString("birthdate")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_PUSH_STATUS, String.valueOf(js.getBoolean("push_status")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_ZIPCODE, String.valueOf(js.getString("zipcode")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_ADDRESS, String.valueOf(js.getString("address")));
                            Utils.saveToSharedPreferences(mContext, Constants.USER_CITY, String.valueOf(js.getString("city")));
                            if (js.getJSONObject("level").getInt("num") == 7)
                                Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi");
                            else if (js.getJSONObject("level").getInt("num") == 8)
                                Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi Premium");
                            Intent mIntent = new Intent(mContext, MainActivity.class);
                            startActivity(mIntent);
                            ((Activity) mContext).finish();
                        }


                        View view = ((Activity) mContext).getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    } else {
                        myEditTextCodeActivation.setText("");

                        Utils.showToastOnUiTread(mContext, object.getString("message"));
                    }
                } catch (JSONException e) {
                    myEditTextCodeActivation.setText("");
                    Log.e("JSONException", e.getMessage() + "");
                    Utils.showToastOnUiTread(mContext, "Veuillez resséyer");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myEditTextCodeActivation.setText("");
                Toast.makeText(getActivity(), "Veuillez resséyer", Toast.LENGTH_LONG).show();
                MyLog.e("ERROR", "" + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telephone", phone);
                params.put("code", code);
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                if ("addNewNumber".equals(fromFragment))
                    params.put("token", token);
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
        AppController.getInstance().addToRequestQueue(sr, url);
    }


    private void getCodeSMS(final String telephone) {

        String url = Constants.URL_BASE + "/api/client/request-activation-code";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject js = new JSONObject(response);
                    String header = js.getString("header");
                    if (header.equals("OK")) {
                        Utils.showToastOnUiTread(mContext, js.getString("message"));
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR LOGIN", "" + error.getMessage());
                progressDialog.dismiss();
                Utils.showToastOnUiTread(mContext, getResources().getString(R.string.message_erreur));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telephone", telephone);
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
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
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    public AlertDialog getDialogSuccess(String numero) {
        final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        dialogLang.setContentView(R.layout.dialog_success);

        MyTextView myTextViewNumero = dialogLang.findViewById(R.id.myTextViewNumero);
        myTextViewNumero.setText(numero);
        dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
                Fragment fragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            }
        });

        // Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogLang.getWindow();
        lp.copyFrom(window.getAttributes());

        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialogLang.setCancelable(true);
        dialogLang.setCanceledOnTouchOutside(true);

        return dialogLang;
    }

    private void noNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
        } else {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
        }
    }

    private void oneNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
        } else {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
        }

    }

    private void twoNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
        } else {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
        }

    }

    private void threeNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide, mContext.getTheme()));
        } else {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active_vide));
        }

    }

    private void fourNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active, mContext.getTheme()));
        } else {
            myImageCode1.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode2.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode3.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
            myImageCode4.setImageDrawable(getResources().getDrawable(R.drawable.cercle_active));
        }

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