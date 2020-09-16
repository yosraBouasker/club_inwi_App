package com.inwi.clubinwi.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Tombola;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.TombolaAdapter;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class TombolaAnnifFragment extends Fragment {

    RecyclerView recyclerView;
    private Context mContext;
    private Handler mTimerHandler = new Handler();
    private Runnable mTimerRunnable;
    private TombolaAdapter adapter;
    private ProgressDialog progressDialog;

    private MyTextView days, hours, minuts, seconds, tv_next_ticket_msg;


    public TombolaAnnifFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TombolaAnnifFragment.
     */
    public static TombolaAnnifFragment newInstance() {
        TombolaAnnifFragment fragment = new TombolaAnnifFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        days = view.findViewById(R.id.days);
        hours = view.findViewById(R.id.hours);
        minuts = view.findViewById(R.id.minuts);
        seconds = view.findViewById(R.id.second);
        tv_next_ticket_msg = view.findViewById(R.id.tv_next_ticket_msg);
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tombola_annif, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        init(view);
        setTimeHandler();
        return view;
    }

    @Override
    public void onPause() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TombolaAdapter(getActivity(), new ArrayList<Tombola>());
        recyclerView.setAdapter(adapter);
        setTimeHandler();
        loadTombolas();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainActivity) getActivity()).changeTitle(getString(R.string.tombola));
        ((MainActivity) getActivity()).showActionBar();
        mTimerHandler.removeCallbacks(mTimerRunnable);
        setTimeHandler();
    }

    private void loadTombolas() {
        progressDialog.show();
        final String phone = Utils.readFromSharedPreferences(getContext(), Constants.USER_PHONE);
        final String token = Utils.readFromSharedPreferences(getContext(), Constants.USER_TOKEN);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOMBOLAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                MyLog.e("SUCCESS", response);
                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        String header = jsonObject.getString("header");
                        if (header.equals("OK")) {

                            JSONArray jsonArray = jsonObject.optJSONObject("result").optJSONArray("tickets");
                            List<Tombola> tombolas = Tombola.tombolasFromJson(jsonArray);
                            adapter.setTombolas(tombolas);

                            final String message = jsonObject.optJSONObject("result").optString("message");
                            tv_next_ticket_msg.setText(message);
//                            Log.e("token", FirebaseInstanceId.getInstance().getToken());
                        }
                    }
                } catch (JSONException e) {
                    // e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("phone", phone);
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
               // params.put("lang", "fr");
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
        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(request, Constants.URL_TOMBOLAS);
    }

    void setTimeHandler() {
        final String phone = Utils.readFromSharedPreferences(getContext(), Constants.USER_PHONE);
        final String Token = Utils.readFromSharedPreferences(getContext(), Constants.USER_TOKEN);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOMBOLAS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Utils.saveToSharedPreferences(mContext, Constants.URL_BASE + "/ckdo", response);
                    String header = jsonObject.getString("header");
                    if (header.equals("OK") && isAdded()) {
                        final String startDate = jsonObject.optJSONObject("result").optString("startDate");
                        final String endDate = jsonObject.optJSONObject("result").optString("dateExp");

                        mTimerHandler.removeCallbacks(mTimerRunnable);
                        mTimerRunnable = new Runnable() {
                            @Override
                            public void run() {
                                setTime(startDate, endDate);
                                mTimerHandler.postDelayed(this, 1000);
                            }
                        };
                        mTimerHandler.postDelayed(mTimerRunnable, 1000);
                    }

                } catch (JSONException e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", Token);
                params.put("phone", phone);
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
                //params.put("lang", "fr");
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
        request.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(request, Constants.URL_TOMBOLAS);
    }

    public void setTime(String startDate, String endDate) {

        Date dateStart;
        Date dateEnd;

        try {

            String dateStartString = startDate;
            String dateEndString = endDate;
            Date curDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            //	Date DateNow = dateFormat.format(curDate);
            dateStart = dateFormat.parse(dateStartString);
            dateEnd = dateFormat.parse(dateEndString);

            long diff = dateStart.getTime() - System.currentTimeMillis();
            if (diff < 0)
                diff = dateEnd.getTime() - System.currentTimeMillis();


            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays >= 10)
                days.setText(diffDays + "  ");
            else
                days.setText("0" + diffDays + "  ");

            if (diffHours >= 10)
                hours.setText(diffHours + " : ");
            else
                hours.setText("0" + diffHours + " : ");

            if (diffMinutes >= 10)
                minuts.setText(diffMinutes + " : ");
            else
                minuts.setText("0" + diffMinutes + " : ");

            if (diffSeconds >= 10)
                seconds.setText(diffSeconds + "");
            else
                seconds.setText("0" + diffSeconds);
        } catch (Exception e) {
            e.printStackTrace();
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
