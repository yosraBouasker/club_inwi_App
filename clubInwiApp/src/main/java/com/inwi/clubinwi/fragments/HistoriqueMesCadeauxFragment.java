package com.inwi.clubinwi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Offre;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.historiqueListAdapter;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoriqueMesCadeauxFragment extends BaseFragment {
    Context context;
    private View mView;
    private ListView myListHistorique;
    private ArrayList<Offre> arrayHistoriqueclub;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout pullToRefreshHistorique;
    private boolean emptyData;
    private LinearLayout layoutParamEmpty;
    private MyTextView myTextConnexion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mView = inflater.inflate(R.layout.fragment_historique_club, container, false);
        getActivity().getWindow().setFormat(PixelFormat.RGBA_8888);
        init(mView);

        return mView;
    }

    @Override
    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        context = mcontext ;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).changeTitle(getString(R.string.user_histoque));
        ((MainActivity) context).showActionBar();
    }


    private void init(View mView) {
        emptyData = true;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);
        layoutParamEmpty = mView.findViewById(R.id.layoutParamEmpty);
        myTextConnexion = mView.findViewById(R.id.myTextConnexion);
        myListHistorique = mView.findViewById(R.id.myListHistoriqueClub);
        pullToRefreshHistorique = mView.findViewById(R.id.pullToRefreshHistorique);
        pullToRefreshHistorique.setColorSchemeColors(getResources().getColor(R.color.pink));
        pullToRefreshHistorique.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (Utils.isOnline(getActivity()))
                    getData();
                else
                    pullToRefreshHistorique.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new TaskGetHistoriqueOffline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new TaskGetHistoriqueOffline().execute();
    }

    private void getData() {

       // String url = Constants.URL_BASE + "/api/cadeaux/birthday?token=" + Utils.readFromSharedPreferences(context, Constants.USER_TOKEN) + "&lang=" + Utils.readFromSharedPreferences(context,Constants.USER_LANGUE);
       // String url = Constants.URL_BASE + "/api/cadeaux/birthday?"+"phone=" + Utils.readFromSharedPreferences(context,Constants.USER_PHONE) +"?token="+ Utils.readFromSharedPreferences(context, Constants.USER_TOKEN) +  "&lang=" + Utils.readFromSharedPreferences(context,Constants.USER_LANGUE);
        String url = Constants.URL_BASE + "/api/cadeaux/historique?token=" + Utils.readFromSharedPreferences(context, Constants.USER_TOKEN) + "&lang=" + Utils.readFromSharedPreferences(context,Constants.USER_LANGUE);


        Log.i("url cadeaux :", url);
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS ELIGIBILITE", response);

                try {
                    JSONObject object = new JSONObject(response);
                    Utils.saveToSharedPreferences(context, Constants.URL_BASE + "/historique", response);
                    if (object.getString("header") != null && object.getString("header").equals("OK")) {
                        JSONArray values = object.getJSONArray("result");
                        arrayHistoriqueclub = Offre.parseOffres(values);
                        if (arrayHistoriqueclub.size() > 0) {
                            historiqueListAdapter adapter = new historiqueListAdapter(context, arrayHistoriqueclub);
                            myListHistorique.setAdapter(adapter);
                        } else if (emptyData) {
                            layoutParamEmpty.setVisibility(View.VISIBLE);
                            if (Utils.isOnline(context))
                                myTextConnexion.setVisibility(View.GONE);
                            else
                                myTextConnexion.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutParamEmpty.setVisibility(View.VISIBLE);
                        if (Utils.isOnline(context))
                            myTextConnexion.setVisibility(View.GONE);
                        else
                            myTextConnexion.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    MyLog.e("ERROR JSON", "error" + e.toString());
                }
                pullToRefreshHistorique.setRefreshing(false);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                pullToRefreshHistorique.setRefreshing(false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    class TaskGetHistoriqueOffline extends AsyncTask<String, Void, Void> {
        String valuesOffline;

        @Override
        protected void onPreExecute() {

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            valuesOffline = Utils.readFromSharedPreferences(context, Constants.URL_BASE + "/historique");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (valuesOffline != null && valuesOffline.length() > 0) {
                try {
                    JSONObject object = new JSONObject(valuesOffline);
                    String header = object.getString("header");
                    if (header.equals("OK")) {
                        JSONArray values = object.getJSONArray("result");
                        arrayHistoriqueclub = Offre.parseOffres(values);
                        if (arrayHistoriqueclub.size() > 0) {
                            emptyData = false;
                            historiqueListAdapter adapter = new historiqueListAdapter(context, arrayHistoriqueclub);
                            myListHistorique.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    MyLog.e("ERROR JSON", "error" + e.toString());
                }
            }

            if (Utils.isOnline(context))
                getData();
            else if (emptyData) {
                layoutParamEmpty.setVisibility(View.VISIBLE);
                if (Utils.isOnline(context))
                    myTextConnexion.setVisibility(View.GONE);
                else
                    myTextConnexion.setVisibility(View.VISIBLE);
            }

            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }
    }

}
