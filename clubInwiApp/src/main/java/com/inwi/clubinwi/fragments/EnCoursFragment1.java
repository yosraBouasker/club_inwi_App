package com.inwi.clubinwi.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Offre;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.OnItemSelectRecyclerView;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.AdapterCurrentOffre;
import com.inwi.clubinwi.adapters.AdapterTombolaAnnifCurrent;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EnCoursFragment1 extends BaseFragment implements OnItemSelectRecyclerView {

    private View mView;
    private Context mContext;
    private JSONArray values = new JSONArray();
    private ArrayList<Offre> currentOffres, currentOffresVerified;
    private ProgressDialog progressDialog;
    private MyTextView myTextViewDateNextCKDO, myTextViewTitleKDO, myTextHasSubscribe, myTextConnexion;
    private LinearLayout myLayoutEmptyCurrentOffre, myLayoutCurrentOffre, layoutParamEmpty, myLayoutButtonsProfite;
    //	private SwipeRefreshLayout	pullToRefreshCKDO;
    private boolean emptyData;
    private boolean emptyPastData;
    private boolean newData = true;
    private String idProduct = "";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private String currentPhoneNumbre = "";
    private AdapterCurrentOffre adapter;
    private RecyclerView mRecyclerView;
    private MyTextView myTextViewJrs, myTextViewMin, myTextViewHrs, myTextViewSec;

    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         mContext = getActivity() ;
     }*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

/*    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_en_cours, container, false);

        init(mView);
        return mView;
    }



    @Override
    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
        super.onPause();
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainActivity) mContext).changeTitle(getString(R.string.label_cadeau_achoura));
        ((MainActivity) mContext).showActionBar();
        getData();
//        new TaskGetDataOffline().execute();
    }



    private void init(View mView) {
        emptyData = true;
        emptyPastData = true;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);
        layoutParamEmpty = mView.findViewById(R.id.myLayoutEmptyCurrentOffre);
        myTextConnexion = mView.findViewById(R.id.myTextConnexion);
        myTextViewJrs = mView.findViewById(R.id.myTextViewJrs);
        myTextViewHrs = mView.findViewById(R.id.myTextViewHrs);
        myTextViewMin = mView.findViewById(R.id.myTextViewMin);
        myTextViewSec = mView.findViewById(R.id.myTextViewSec);
        myTextViewDateNextCKDO = mView.findViewById(R.id.myTextViewDateNextCKDO);
        myLayoutEmptyCurrentOffre = mView.findViewById(R.id.myLayoutEmptyCurrentOffre);
        myLayoutCurrentOffre = mView.findViewById(R.id.myLayoutCurrentOffre);
        mRecyclerView = mView.findViewById(R.id.recyclerView_en_cours);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void getData() {
        if(progressDialog!=null)
            progressDialog.show();
        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(getContext(), Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String langue ;
        if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))
                || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)))
            langue =  Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE);
        else langue = "fr";
        String url = Constants.URL_BASE + "/api/cadeaux?token=" + Token + "&phone=" + currentPhoneNumbre + "&lang=" + langue;


        // String url = Constants.URL_BASE + "/api/cadeaux/birthday?token=" + Token + "&phone=" + currentPhoneNumbre + "&lang=" + langue;
        Log.e("url 2 ","url 2 "+url);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        Log.i("urlKDO","En Cours Fragment : urlKDO"+ url);
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //progressDialog.dismiss(); before
                MyLog.e("SUCCESS CKDO", response);
                try {
                    JSONObject object = new JSONObject(response);
                    Utils.saveToSharedPreferences(mContext, Constants.URL_BASE + "/ckdo", response);
                    int status = object.optInt("status");
                    if (status == 200) {

                        currentOffresVerified = new ArrayList<>();
                        currentOffres = new ArrayList<>();

                        currentOffres = Offre.parseOffres(object.getJSONObject("result").getJSONObject("currents").getJSONArray("list"));

                        for (Offre offre : currentOffres)
                            if (offre.getImage() != null && !offre.getImage().equals("") && !oldOffre(offre)) {
                                currentOffresVerified.add(offre);
                                emptyData = false;
                            }

                        if (!emptyData) {
                            Log.e("emptyData", "emptyData");
                            newData = false;
                            mRecyclerView.removeAllViews();
                        }

                        OnItemSelectRecyclerView mOnItemSelectRecyclerView = new OnItemSelectRecyclerView() {

                            @Override
                            public void onItemClick(int position, String type) {
                                Offre offre = currentOffresVerified.get(position);
                                int pointKdo = Integer.parseInt(offre.getPoint());
                                int pointUser;
                                if (Utils.readFromSharedPreferences(mContext, Constants.USER_POINT) == null)
                                    pointUser = 0;
                                else
                                    pointUser = Integer.parseInt(Utils.readFromSharedPreferences(mContext, Constants.USER_POINT));

                                if (Utils.isOnline(mContext)) {
                                    if (type.equals("P")) {
                                        profiter(offre);
                                    } else if (type.equals("O")) {
                                        if (pointKdo > pointUser) {
                                            Toast.makeText(mContext, "Vous n'avez pas assez de points", Toast.LENGTH_SHORT).show();
                                        } else if (!offre.isComming()) {

                                            if (offre.isCanOfferAndBenefit() && !offre.isHasSubscribe() && offre.isCredential()) {
                                                Fragment fragment = new AddPhoneNumberFragment();

                                                Bundle args = new Bundle();
                                                args.putString("fromFragment", "offrirCKDO");
                                                args.putString("token", "" + Token);
                                                args.putString("cadeau_id", "" + offre.getId());
                                                args.putString("compagnie_id", "" + offre.getCompagnie_id());
                                                args.putString("niveau_id", "" + offre.getNiveau());
                                                args.putString("points", "" + offre.getPoint());


                                                fragment.setArguments(args);
                                                Utils.switchFragment((FragmentActivity) mContext, fragment, AddPhoneNumberFragment.class.toString(), R.id.content, true, true, true);
                                            }
                                        } else
                                            Utils.showToastOnUiTread(mContext, "Vous n'etes pas éligible à cette offre");
                                    }
                                } else
                                    Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));

                            }
                        };
                        mRecyclerView.removeAllViews();

                        for (int i = 0; i < currentOffresVerified.size(); i++) {
                            if (showOffre(currentOffresVerified.get(i))) {
                                Log.e("Show", currentOffresVerified.get(i).getTitle());
                                emptyData = false;
                                newData = true;
                                adapter = new AdapterCurrentOffre(currentOffresVerified, mContext, mOnItemSelectRecyclerView);
                                // adapter = new AdapterTombolaAnnifCurrent( mContext,currentOffresVerified, mOnItemSelectRecyclerView); // Added for tombolaAnnif
                                mRecyclerView.setAdapter(adapter);
                            }
                        }

                        if (emptyData && emptyPastData) {
                            layoutParamEmpty.setVisibility(View.VISIBLE);
                            if (Utils.isOnline(mContext))
                                myTextConnexion.setVisibility(View.GONE);
                            else
                                myTextConnexion.setVisibility(View.VISIBLE);
                        } else if (emptyData) {
                            myLayoutEmptyCurrentOffre.setVisibility(View.VISIBLE);
                            myLayoutCurrentOffre.setVisibility(View.GONE);
                        } else {
                            myLayoutEmptyCurrentOffre.setVisibility(View.GONE);
                            myLayoutCurrentOffre.setVisibility(View.VISIBLE);

                        }

                    } else if (status == 331) {
                        if(mContext != null)
                            Utils.expiredSession(mContext, object.getString("message")); // crashlytics
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if(mContext!=null)
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.probleme_connexion), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                if(mContext!=null)
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.probleme_connexion), Toast.LENGTH_SHORT).show();
                //			pullToRefreshCKDO.setRefreshing(false);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                // params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");
                return params;
            }

        };

        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        sr.setShouldCache(false);
        queue.add(sr);
    }

    public boolean showOffre(Offre offre) {

        Date dateEnd;
        try {
            Log.e("showOffre", "showOffre");
            String dateEndString = offre.getStop_date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dateEnd = dateFormat.parse(dateEndString);
            long diff = dateEnd.getTime() - System.currentTimeMillis();
            Log.e("showOffre", diff + "");
            if (diff > 0)
                return true;

        } catch (Exception e) {
            Log.e("showOffre", e.toString());
            e.printStackTrace();

        }
        return false;

    }

    public boolean oldOffre(Offre offre) {

        Date dateEnd;
        try {

            String dateEndString = offre.getStop_date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dateEnd = dateFormat.parse(dateEndString);
            long diff = dateEnd.getTime() - System.currentTimeMillis();
            if (diff < 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void onItemClick(int position, String type) {
        // TODO Auto-generated method stub
        Toast.makeText(mContext, currentOffresVerified.get(position).getTitle(), Toast.LENGTH_SHORT).show();

    }

    private void profiter(final Offre offre) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(mContext.getResources().getString(R.string.patientez));
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Constants.URL_BASE + "/api/cadeaux/reserver";
        // String url = Constants.URL_BASE + "/api/cadeaux/birthday/reserver";
        Log.i("url profiter", url);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MyLog.e("SUCCESS profiter", response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("header").equals("OK")) {
                        getData();
                        getDialogSuccess(object.optString("message"),object.optString("cadeau_title"));

                    }else     getDialogSuccessNOK(object.optString("message"),object.optString("cadeau_title"));


                    //  Toast.makeText(mContext, object.optString("message")+object.optString("cadeau_title"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    MyLog.e("ERROR JSON", "error" + e.toString());
                }
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //  progressDialog.dismiss(); before


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToastOnUiTread(mContext, "Veuillez resséyer");
                MyLog.e("ERROR", "" + error.getMessage());
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", currentPhoneNumbre);
                params.put("cadeau_id", offre.getId());
                params.put("compagnie_id", offre.getCompagnie_id());
                params.put("niveau_id", offre.getNiveau());
                params.put("points", offre.getPoint());
                params.put("token", Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN));
                if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))
                        || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)))
                    params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                else params.put("lang", "fr");

                return checkParams(params);
            }
    /*    // added in dev
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");
                return params;
            }*/
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);

    }
    public AlertDialog getDialogSuccessNOK(String msg,String cadeau) {
        final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        // final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        // dialogLang.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialogLang.setContentView(R.layout.dialog_success_annif);


        dialogLang.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogLang.getWindow().setBackgroundDrawableResource(android.R.color.transparent);







        //dialogLang.getWindow().setDimAmount(0);

        MyTextView message_succes ;
        MyTextView cdo_title ;
        MyTextView felicMsg ;

        Button dismiss_button;
        ImageView check_success ;
        ImageView ribbons ;

        check_success =  dialogLang.findViewById(R.id.check_success) ;
        felicMsg =  dialogLang.findViewById(R.id.felicitation_msg) ;
        ribbons =  dialogLang.findViewById(R.id.ribbons) ;

        felicMsg.setVisibility(View.GONE);
        //check_success.setVisibility(View.INVISIBLE);
        ribbons.setVisibility(View.INVISIBLE);


        message_succes =  dialogLang.findViewById(R.id.message_succes_annif) ;
        dismiss_button =  dialogLang.findViewById(R.id.dismiss_button) ;
        dismiss_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogLang.dismiss();
            }
        });
        message_succes.setText(msg);
        cdo_title =  dialogLang.findViewById(R.id.cadeau_annif) ;
        cdo_title.setText(cadeau);



    /*    dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
                Fragment fragment = new CKDOFragement();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

            }
        });*/

        // Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogLang.getWindow();
        ((FrameLayout) window.getDecorView().findViewById(android.R.id.content)).setForeground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // lp.copyFrom(window.getAttributes());

        // This makes the dialog take up the full width
       /* lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);*/

        dialogLang.setCancelable(true);
        //dialogLang.setCanceledOnTouchOutside(true);

        return dialogLang;
    }
    public AlertDialog getDialogSuccess(String msg,String cadeau) {
        final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        // final AlertDialog dialogLang = new AlertDialog.Builder(mContext).show();
        // dialogLang.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialogLang.setContentView(R.layout.dialog_success_annif);


        dialogLang.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogLang.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //dialogLang.getWindow().setDimAmount(0);

        TextView message_succes ;
        TextView cdo_title ;
        Button dismiss_button;


        message_succes =  dialogLang.findViewById(R.id.message_succes_annif) ;
        dismiss_button =  dialogLang.findViewById(R.id.dismiss_button) ;
        dismiss_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogLang.dismiss();
            }
        });
        message_succes.setText(msg);
        cdo_title =  dialogLang.findViewById(R.id.cadeau_annif) ;
        cdo_title.setText(cadeau);



    /*    dialogLang.findViewById(R.id.myTextViewOk).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
                Fragment fragment = new CKDOFragement();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

            }
        });*/

        // Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogLang.getWindow();
        ((FrameLayout) window.getDecorView().findViewById(android.R.id.content)).setForeground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // lp.copyFrom(window.getAttributes());

        // This makes the dialog take up the full width
       /* lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);*/

        dialogLang.setCancelable(true);
        //dialogLang.setCanceledOnTouchOutside(true);

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
