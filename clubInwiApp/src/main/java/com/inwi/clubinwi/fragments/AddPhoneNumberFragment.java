package com.inwi.clubinwi.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faradaj.patternededittext.PatternedEditText;
import com.inwi.clubinwi.AppController;
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

public class AddPhoneNumberFragment extends BaseFragment {

    protected static final String TAG = "InscriptionStep1";
    private Context mContext;
    private View mView;
    private TextView valider;
    private PatternedEditText mPatternedEditText;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog progressDialog;
    private MyTextView myTextViewMessage;
    private boolean offrirFragment = false;
    private String idProduct, token, phone, cadeau_id, compagnie_id, niveau_id, points;
    private ImageView myImageViewContact;
    private String currentPhoneNumbre = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_add_phone_number, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainActivity) mContext).showActionBar();
    }

    private void init(View mView) {
        mPatternedEditText = mView.findViewById(R.id.number_phone);
        valider = mView.findViewById(R.id.valider);
        myTextViewMessage = mView.findViewById(R.id.myTextViewMessage);
        myImageViewContact = mView.findViewById(R.id.myImageViewContact);

        myImageViewContact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            String fromFragment = mArgs.getString("fromFragment");
            token = mArgs.getString("token");
            cadeau_id = mArgs.getString("cadeau_id");
            compagnie_id = mArgs.getString("compagnie_id");
            niveau_id = mArgs.getString("niveau_id");
            points = mArgs.getString("points");
            idProduct = mArgs.getString("idProduct");

            if (fromFragment.equals("offrirCKDO")) {
                ((MainActivity) mContext).changeTitle("Offrir votre CKDO");
                myTextViewMessage.setVisibility(View.GONE);
                valider.setText(R.string.offrir);
                offrirFragment = true;
            } else {
                ((MainActivity) mContext).changeTitle("Ajouter un numéro");
                myTextViewMessage.setVisibility(View.VISIBLE);
                valider.setText(R.string.add);
                offrirFragment = false;
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);

        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPatternedEditText.getRawText() == null || mPatternedEditText.getRawText().length() < 10) {
                    Utils.showToastOnUiTread(mContext, "error");
                } else {
                    if (Utils.isOnline(mContext)) {
                        View view = ((Activity) mContext).getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        if (offrirFragment)
                            offrirCKDO(idProduct, mPatternedEditText.getRawText());
                        else
                            AddPhoneNumber(mPatternedEditText.getRawText());
                    } else
                        Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
                }

            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = mContext.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE}, null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        number = number.replace("+212", "0");
                        number = number.replace("-", "");
                        number.replace(" ", "");
                        Log.i("phone", number);
                        mPatternedEditText.setText(number);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }

    private void AddPhoneNumber(final String phone) {
        progressDialog.show();
        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        final String token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        String url = Constants.URL_BASE + "/api/client/telephone-association";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS", response);
                try {
                    if (response != null) {
                        JSONObject object = new JSONObject(response);
                        String header = object.getString("header");
                        if (header.equals("OK")) {

                            Fragment fragment = new InscriptionStep3();
                            Bundle args = new Bundle();
                            args.putString("telephone", phone);
                            args.putString("fromFragment", "addNewNumber");
                            fragment.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

                        } else
                            Utils.showToastOnUiTread(mContext, object.getString("message"));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    Utils.showToastOnUiTread(mContext, "Veuillez resséyer");
                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Veuillez resséyer", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telephone", phone);//
                params.put("token", token);
                params.put("current_telephone", currentPhoneNumbre);
                params.put("lang",Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE));
                return checkParams(params);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
             //   params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    private void offrirCKDO(String idProduct, final String phoneReceiver) {
        progressDialog.show();

        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Constants.URL_BASE + "/api/cadeaux/offriretbeneficier";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS", response);
                try {
                    if (response != null) {
                        JSONObject object = new JSONObject(response);
                        String header = object.getString("header");
                        if (header.equals("OK")) {
                            getDialogSuccess();
                        }
                        Utils.showToastOnUiTread(mContext, object.getString("message"));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, "Veuillez vérifier votre saisie");
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
                params.put("benficiant_tel", phoneReceiver);
                params.put("phone", currentPhoneNumbre);
                params.put("cadeau_id", cadeau_id);
                params.put("compagnie_id", compagnie_id);
                params.put("niveau_id", niveau_id);
                params.put("points", points);
                params.put("token", token);
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
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    public AlertDialog getDialogSuccess() {
        final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        dialogLang.setContentView(R.layout.dialog_success_offrir);

        dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
                Fragment fragment = new CKDOFragement();
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