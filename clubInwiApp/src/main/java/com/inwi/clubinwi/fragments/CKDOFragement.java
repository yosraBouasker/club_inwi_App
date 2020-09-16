package com.inwi.clubinwi.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.inwi.clubinwi.BuildConfig;
import com.inwi.clubinwi.CardView;
import com.inwi.clubinwi.ExampleAdapter;
import com.inwi.clubinwi.ExampleItem;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.RecyclerViewGifts;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.achoura.AchouraUtils;
import com.inwi.clubinwi.achoura.models.listing.CurrentCadeau;
import com.inwi.clubinwi.achoura.models.listing.Currents;
import com.inwi.clubinwi.achoura.models.listing.Luck;
import com.inwi.clubinwi.achoura.models.listing.Result;
import com.inwi.clubinwi.achoura.rest.RestClient;
import com.inwi.clubinwi.cardGiftAdapter;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("NewApi")
public class CKDOFragement extends BaseFragment implements OnClickListener {

    private View mView;
    private Context mContext;
    private ImageView filter;
    private ProgressDialog progressDialog;
    private LinearLayout layoutParamEmpty;
    private SwipeRefreshLayout pullToRefreshCKDO;
    private boolean emptyData;
    private boolean emptyPastData;
    private MyTextView myTextConnexion, mEncours, mArchiver;
    private LinearLayout llPopup;
    private RelativeLayout rlPopGlobal;
    private MyTextView myTextViewJrs, myTextViewHrs, myTextViewMin, myTextViewSec, myTextViewDateNextCKDO;
    private MyTextView myTxtJrs, myTxtHrs, myTxtMin, myTxtSec;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private String currentPhoneNumbre;
    private String array;
    boolean bool;
    private RecyclerView mRecyclerView, mRecyclerView1;
    private ExampleAdapter mAdapter;
    private cardGiftAdapter  mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager1;
    private Luck luck;
    private CurrentCadeau cadeauToady;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //	super.onCreate(savedInstanceState);
        try {
            super.onCreate(savedInstanceState);
        } catch (final BadParcelableException bpe) {

            if (BuildConfig.DEBUG)
                Log.e("BadParcelableException", "" + bpe);
        }
        mView = inflater.inflate(R.layout.fragment_ckdo, container, false);
        mContext = getActivity();
        init(mView);
        if ("ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))){
            selectlanguage(mContext.getApplicationContext(), "ar");
        }
        return mView;
    }

    public void selectlanguage(Context context, String l) {
        Locale locale = new Locale(l);
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
        //Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, l);
    }

    @Override
    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // ((MainActivity) mContext).changeTitle(getString(R.string.label_cadeau_achoura));
        ((MainActivity) mContext).showActionBar();

        //setTimeHandler();
//        getCadeaux();

    }

    private void init(View mView) {
        progressDialog = new ProgressDialog(mContext);
        llPopup = mView.findViewById(R.id.popup_kdo);
        rlPopGlobal = mView.findViewById(R.id.bg_popup_kdo);
        myTextViewJrs = mView.findViewById(R.id.myTextViewJrs);
        myTextViewHrs = mView.findViewById(R.id.myTextViewHrs);
        myTextViewMin = mView.findViewById(R.id.myTextViewMin);
        myTextViewSec = mView.findViewById(R.id.myTextViewSec);
        myTxtJrs = mView.findViewById(R.id.txt_jours);
        myTxtHrs = mView.findViewById(R.id.txt_hrs);
        myTxtMin = mView.findViewById(R.id.txt_min);
        myTxtSec = mView.findViewById(R.id.txt_sec);
        myTextViewDateNextCKDO = mView.findViewById(R.id.myTextViewDateNextCKDO);
        layoutParamEmpty = mView.findViewById(R.id.layoutParamEmpty);
        mArchiver = mView.findViewById(R.id.archiver);
        mEncours = mView.findViewById(R.id.encours);
        myTextConnexion = mView.findViewById(R.id.myTextConnexion);
        filter = mView.findViewById(R.id.img_filter);

        mArchiver.setOnClickListener(this);
        mEncours.setOnClickListener(this);

        setHeader();
        filter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //setHeader();
                if (View.GONE == rlPopGlobal.getVisibility()) {
                    filter.setImageResource(R.drawable.filtre_active);
                    showShadow();
                } else if (View.INVISIBLE == rlPopGlobal.getVisibility()) {
                    filter.setImageResource(R.drawable.filtre_active);
                    showShadow();
                } else if (View.VISIBLE == rlPopGlobal.getVisibility()) {
                    hidePopup();
                    filter.setImageResource(R.drawable.filtre_normal);
                }

            }
        });

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            bool = mArgs.getBoolean("first");
            bool = mArgs.getBoolean("first");
            if(bool){
                CKDOFragement mContent = new CKDOFragement();
                Bundle bundle = new Bundle();
                bundle.putBoolean("first", false);
                mContent.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
            }
        }

        // Charge First Fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mArchiver.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_noactive, null));
            mEncours.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_active, null));
        }

        getCadeaux();
        ArrayList<ExampleItem> exampleList = new ArrayList<>();

        exampleList.add(new ExampleItem(R.drawable.appels_vers_inwi, "Appels vers inwi"));
        exampleList.add(new ExampleItem(R.drawable.appels_vers_le_national, "Appels vers le natonal"));
        exampleList.add(new ExampleItem(R.drawable.appels_vers_inwi, "Appels vers inwi"));
        exampleList.add(new ExampleItem(R.drawable.appels_vers_le_national, "Appels vers le national"));
        exampleList.add(new ExampleItem(R.drawable.appels_vers_inwi, "Appels vers inwi"));
        exampleList.add(new ExampleItem(R.drawable.appels_vers_le_national, "Appels vers le national"));

        mRecyclerView = mView.findViewById(R.id.InitialRecyclerView);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                  Utils.showToastOnUiTread(mContext, "button clicked");
                  mRecyclerView.setVisibility(View.GONE);

                FragmentManager childFragCkd = getChildFragmentManager();
                FragmentTransaction childFragTrans_ = childFragCkd.beginTransaction();
                RecyclerViewGifts frag = new RecyclerViewGifts();
                childFragTrans_.replace(R.id.InitialRecyclerView, frag);
                childFragTrans_.addToBackStack(null).commit();

                ArrayList<CardView> giftsList = new ArrayList<>();

                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "30 min on-net", "30 min on-net"));
                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "90 min on-net", "90 min on-net"));
                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "30 min on-net", "30 min on-net"));
                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "30 min on-net", "30 min on-net"));
                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "30 min on-net", "30 min on-net"));
                giftsList.add(new CardView(R.drawable.appels_vers_inwi, "30 min on-net", "30 min on-net"));

                mRecyclerView1 = mView.findViewById(R.id.recyclerView2);
                mRecyclerView1.setVisibility(View.VISIBLE);
                mRecyclerView1.setHasFixedSize(true);
                mLayoutManager1 = new LinearLayoutManager(mContext);
                mAdapter1 = new cardGiftAdapter(mContext, giftsList);

                mRecyclerView1.setLayoutManager(mLayoutManager1);
                mRecyclerView1.setAdapter(mAdapter1);
            }
        });

//        FragmentManager childFragCkd = getChildFragmentManager();
//        FragmentTransaction childFragTrans_ = childFragCkd.beginTransaction();
        EnCoursFragment frag_ = new EnCoursFragment();
        // AchouraCadeauFragment frag_ = AchouraCadeauFragment.newInstance();
        //comment here
        // childFragTrans_.replace(R.id.frameLayout_ckdo, frag_);
        //      childFragTrans_.addToBackStack("E");
        //childFragTrans_.commit();

    }

    public void setHeader() {
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        llPopup.removeAllViews();

        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);
        String currentPhoneNumbre = "";
        try {
            JSONArray jsonArray = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF));
            if (jsonArray.length() > 0 && jsonArray.getJSONObject(0) != null && jsonArray.getJSONObject(0).has("id")) {
                currentPhoneNumbre = jsonArray.getJSONObject(0).getString("id");
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        View itemFiltre1 = mInflater.inflate(R.layout.popup_item_kdo, null, false);
        ((TextView) itemFiltre1.findViewById(R.id.title)).setText(currentPhoneNumbre);
        itemFiltre1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToastOnUiTread(mContext, "Ce numéro de téléphone est déjà sélectionné");
            }
        });
        llPopup.addView(itemFiltre1);
        if (listForfaits != null && !listForfaits.equals("[]") && !listForfaits.equals("")) {
            try {
                final JSONArray listNum = new JSONArray(listForfaits);
                for (int i = 0; i < listNum.length(); i++) {
                    View itemFiltre = mInflater.inflate(R.layout.popup_item_kdo, null, false);
                    ((TextView) itemFiltre.findViewById(R.id.title)).setText(listNum.getJSONObject(i).getString("id"));
                    final int j = i + 1;
                    itemFiltre.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hidePopup();
                            setHeader();
                        }
                    });
                    llPopup.addView(itemFiltre);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    private void hidePopup() {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            rlPopGlobal.animate()

                    .alpha(0).setDuration(150).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rlPopGlobal.setVisibility(View.GONE);
                }
            });
        } else {
            rlPopGlobal.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void showShadow() {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            rlPopGlobal.setVisibility(View.VISIBLE);
            rlPopGlobal.setAlpha(0);
            rlPopGlobal.animate().alpha(1).setDuration(150).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                }
            });
        } else {
            rlPopGlobal.setVisibility(View.VISIBLE);
        }
    }

    public void getCadeaux(){
        Log.d(TAG, "getCadeauxList: SESSION_USER_TOKEN: " + Utils.readFromSharedPreferences(getActivity(), Constants.USER_TOKEN));
        Log.d(TAG, "getCadeauxList: SESSION_USER_PHONE: " + Utils.readFromSharedPreferences(getActivity(), Constants.USER_PHONE));

        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        final String phone = Utils.readFromSharedPreferences(mContext, Constants.USER_PHONE);

        // Call<Luck> call = RestClient.newInstance().api().listCadeaux("0638523600", "03688070483370828377", "fr");
            Call<Luck> call = RestClient.newInstance().api().getCadeauxList(Token, phone);
            call.enqueue(new Callback <Luck>() {

                @Override
                public void onResponse(Call<Luck> call, retrofit2.Response<Luck> response) {
                    if (!response.isSuccessful()){
                        parseResponse(response);
                        Log.d("responseTag", "Reponse is not successful");
                        return;
                    }
                    Log.d("all gifts", response.body().toString());
                }

                @Override
                public void onFailure(Call<Luck> call, Throwable t) {
//                    Toast.makeText(getActivity(), "Network call failed!", Toast.LENGTH_LONG).show();
                    snackRetryListing(getString(R.string.label_generic_failure));

                }
            });
        }

    private void parseResponse(retrofit2.Response<Luck> response) {
        Log.d(TAG, "parseResponse: Handling listing response");
        luck = response.body();
        if (luck == null) {
            snackRetryListing(getString(R.string.label_generic_failure));
        } else {
            try {
                long status = luck.getStatus();
                if (status != 200) {
                    Log.d(TAG, "parseResponse: msg: " + luck.getMessage());
                    Toast.makeText(getContext(), luck.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Result result = luck.getResult();
                    if (result != null) {
//                        giftEndDate = result.getEndDate();
//                        setupAndStartCountdown();

                        Currents currents = result.getCurrents();
                        if (currents != null) {
                            List<CurrentCadeau> list = currents.getCurrentCadeau();
                            if (list != null && !list.isEmpty()) {
                                cadeauToady = list.get(0);
                                if (cadeauToady != null) {
                                    System.out.println("TodayGift: " + cadeauToady.toString());
//                                    Glide.with(getContext()).load(cadeauToady.getImage()).into(civCadeau);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "parseResponse: ", e);
            }

        }
    }

    private void snackRetryListing(String message) {
//       Snackbar.make(btnPlay, message, Snackbar.LENGTH_INDEFINITE)
//                .setAction(R.string.action_retry, v -> getCadeauxList())
//                .setActionTextColor(Color.WHITE)
//                .show();
    }

    void setTimeHandler() {

        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        try {
            currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        //String url = Constants.URL_BASE + "/api/cadeaux?token=" + Token + "&phone=" + currentPhoneNumbre + "&lang=" + Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE);
        String langue ;
        //String url = Constants.URL_BASE + "/api/cadeaux?token=" + Token + "&phone=" + currentPhoneNumbre + "&lang=" + Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE);
        if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))
                || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)))
            langue =  Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE);
        else langue = "fr";
        String url = Constants.URL_BASE + "/api/cadeaux?token="+ Token + "&phone=" + currentPhoneNumbre+ "&lang=" + langue;
       // String url = "http://rct.club.inwi.ma/api/cadeaux?token=37621929139774919948&phone=0638523600&lang=fr" ;
        Log.e("url 1","CKDO FRAGMENT -> url 1"+url) ;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.getCache().clear();
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject object = new JSONObject(response);
                                if(mContext != null && isAdded())
                                    Utils.saveToSharedPreferences(mContext, Constants.URL_BASE + "/ckdo", response);
                                    String header = object.getString("header");
                                if (header.equals("OK") && isAdded()) {
                                   final String startDate = object.getJSONObject("result").getString("start_date");
                                   // final String startDate = "2018-11-29 23:00:00";
                                    //final String endDate =  "2018-12-04 23:00:00";
                                    final String endDate = object.getJSONObject("result").getString("end_date");
                                    timerHandler.removeCallbacks(timerRunnable);
                                    timerRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            setTime(startDate, endDate);
                                            timerHandler.postDelayed(this, 1000);
                                        } 
                                    };
                                    timerHandler.postDelayed(timerRunnable, 1000);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                if(mContext != null && isAdded() )
                                Toast.makeText(mContext, getResources().getString(R.string.probleme_connexion), Toast.LENGTH_SHORT).show();
                            }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                if(mContext != null && isAdded() )
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.probleme_connexion), Toast.LENGTH_SHORT).show();
            }
        })

        {
            // added in dev

/*            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
               // params.put("Authorization", "Basic cHJvZC1kaWdpdGFsZTpHc3I1OFJzTDE4");

                return params;
            }*/
        };
        int socketTimeout = 30000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        sr.setShouldCache(false);
        queue.add(sr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.archiver:
                mArchiver.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_active));
                mEncours.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_noactive));
                /**********************************************/
                FragmentManager childFragArch = getChildFragmentManager();
                FragmentTransaction childFragTrans = childFragArch.beginTransaction();
                HistoriqueMesCadeauxFragment fragB = new HistoriqueMesCadeauxFragment();
                //comment here
                //childFragTrans.replace(R.id.frameLayout_ckdo, fragB);
                //   childFragTrans.addToBackStack("A");
                childFragTrans.commit();
                break;
            case R.id.encours:
                mArchiver.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_noactive));
                mEncours.setBackground(getResources().getDrawable(R.drawable.round_corners_grey_active));
                FragmentManager childFragCkd = getChildFragmentManager();
                FragmentTransaction childFragTrans_ = childFragCkd.beginTransaction();

                EnCoursFragment frag_ = new EnCoursFragment();
                // AchouraCadeauFragment frag_ = AchouraCadeauFragment.newInstance();
                //comment here
                //childFragTrans_.replace(R.id.frameLayout_ckdo, frag_);
                //      childFragTrans_.addToBackStack("E");
                childFragTrans_.commit();
                break;
        }

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
            if (dateStart.getTime() - curDate.getTime() > 0) {
                myTextViewDateNextCKDO.setText(R.string.next_gift_at);
            } else if (dateStart.getTime() - curDate.getTime() <= 0 && dateEnd.getTime() - curDate.getTime() > 0) {
                myTextViewDateNextCKDO.setText(R.string.before_expiration_of_gift);
            } else {
                myTextViewDateNextCKDO.setText(R.string.no_gift_this_moment);
            }

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            int sec = (int)diffSeconds;
            String mysec = getResources().getQuantityString(R.plurals.secOfItems, sec, "");
            myTxtSec.setText(""+mysec);
            int min = (int)diffMinutes;
            String mymin = getResources().getQuantityString(R.plurals.minOfItems, min, "");
            myTxtMin.setText(""+mymin);
            int hrs = (int)diffHours;
            String myhrs = getResources().getQuantityString(R.plurals.hourOfItems, hrs, "");
            myTxtHrs.setText(""+myhrs);
            int day = (int)diffDays;
            String myday = getResources().getQuantityString(R.plurals.dayOfItems, day, "");
            myTxtJrs.setText(""+myday);

            if (diffDays >= 10)
                myTextViewJrs.setText(diffDays + " : ");
            else
                myTextViewJrs.setText("0" + diffDays + " : ");

            if (diffHours >= 10)
                myTextViewHrs.setText(diffHours + " : ");
            else
                myTextViewHrs.setText("0" + diffHours + " : ");

            if (diffMinutes >= 10)
                myTextViewMin.setText(diffMinutes + " : ");
            else
                myTextViewMin.setText("0" + diffMinutes + " : ");

            if (diffSeconds >= 10)
                myTextViewSec.setText(diffSeconds + "");
            else
                myTextViewSec.setText("0" + diffSeconds);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
