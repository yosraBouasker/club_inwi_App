package com.inwi.clubinwi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.HomePagerAdapter;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class HomeFragment extends BaseFragment {

	protected static final String	TAG			= "HomeFragment";
	private Context					mContext;
	private View					mView;

	private ViewPager				mViewPager;
	private HomePagerAdapter		mAdapter;
	private JSONArray				values		= new JSONArray();
	TabLayout						tabLayout;
	private int						position	= 0;
	private ProgressDialog			progressDialog;
	private LinearLayout			layoutParamEmpty;
	private boolean					noData;
	private MyTextView			 	myTextConnexion;
    public JSONObject               object;
    boolean bool;
    String lang;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mView = inflater.inflate(R.layout.fragment_home, container, false);
		mContext = getActivity();

		init(mView);
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
		Utils.saveToSharedPreferences(context, Constants.USER_LANGUE, l);
	}

	//@SuppressLint("NewApi")
	private void init(View mView) {
        progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(getResources().getString(R.string.patientez));
		progressDialog.setCancelable(true);
		noData = true;
		//mViewPager = new RtlViewPager(mContext);
		mViewPager =  mView.findViewById(R.id.pager);
		tabLayout = mView.findViewById(R.id.tab_layout);
		layoutParamEmpty = mView.findViewById(R.id.layoutParamEmpty);
		myTextConnexion = mView.findViewById(R.id.myTextConnexion);
		Bundle mArgs = getArguments();
		if (mArgs != null) {
			position = mArgs.getInt("selectedItem", 0);
			bool = mArgs.getBoolean("first");
			//lang = mArgs.getString("lang");
			//selectlanguage(mContext.getApplicationContext(), lang);
		}

		if ("ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE))){
			selectlanguage(mContext.getApplicationContext(), "ar");
		}else {
			selectlanguage(mContext.getApplicationContext(), "fr");
		}


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new TaskGetDataOffline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			new TaskGetDataOffline().execute();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity) mContext).changeTitle("Club inwi");
		((MainActivity) mContext).showActionBar();
	}
	
	class TaskGetDataOffline extends AsyncTask<String, Void, Void> {
		String	valuesOffline;
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			Log.i("get data presentation", "from offline");
		}
		
		@Override
		protected Void doInBackground(String... params) {
			valuesOffline = Utils.readFromSharedPreferences(mContext, Constants.URL_BASE + "/pages");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(isAdded()) {

				try {
					if (valuesOffline != null && valuesOffline.length() > 0 && mViewPager != null && tabLayout != null) {

						noData = false;
						mAdapter = new HomePagerAdapter(getChildFragmentManager(), new JSONArray(valuesOffline));
						//tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
						mViewPager.setAdapter(mAdapter);
						tabLayout.setupWithViewPager(mViewPager);
						//mViewPager.setCurrentItem(position);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if(bool){
					HomeFragment mContent = new HomeFragment();
					Bundle bundle = new Bundle();
					//bundle.putInt("selectedItem", 0);
					bundle.putBoolean("first", false);
					//bundle.putString("lang", "ar");
					mContent.setArguments(bundle);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
				}
				else {
					if(progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				}
				if (Utils.isOnline(mContext))
					getData();
				else if (noData) {
					layoutParamEmpty.setVisibility(View.VISIBLE);
					if(Utils.isOnline(mContext))
						myTextConnexion.setVisibility(View.GONE);
					else
						myTextConnexion.setVisibility(View.VISIBLE);
				}
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	private void getData() {
        progressDialog.show();
        String url = Constants.URL_BASE + "/api/content/pages?token="+Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN) + "&lang=" + Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE);
		//System.out.println("rachid : online data url :"+url);

		// String url = getResources().getString(R.string.URL_HOME);
		RequestQueue queue = Volley.newRequestQueue(mContext);

		StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(progressDialog != null && progressDialog.isShowing() && getActivity()!= null && !getActivity().isFinishing())
					progressDialog.dismiss();
	//			MyLog.e("SUCCESS HOME", response);
				try {
					Log.i("get data presentation", "from online");
                    object = new JSONObject(response);
					values = object.getJSONArray("result");

					//System.out.println("rachid : online data :"+values.toString());
					Utils.saveToSharedPreferences(mContext, Constants.URL_BASE + "/pages", values.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                int status = object.optInt("status");

                if (status == 200) {

				    Log.d("status",object.optString("status"));
                    if (isAdded()) {
                        if (values.length() > 0 && mViewPager != null) {
                            noData = false;
                            mAdapter = new HomePagerAdapter(getChildFragmentManager(), values);
                            //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                            mViewPager.setAdapter(mAdapter);
                            tabLayout.setupWithViewPager(mViewPager);
                            mViewPager.setCurrentItem(position);
                        } else if (noData)
                            layoutParamEmpty.setVisibility(View.VISIBLE);
                        if(Utils.isOnline(mContext))
                            myTextConnexion.setVisibility(View.GONE);
                        else
                            myTextConnexion.setVisibility(View.VISIBLE);
                    }

                } else if(status == 331){
					if(mContext!=null && isAdded())
				    Utils.expiredSession(mContext,"session expired");
                }
            }
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				MyLog.e("ERROR", "" + error.getMessage());
			}
		});
		int socketTimeout = 20000;// 30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		sr.setRetryPolicy(policy);
        sr.setShouldCache(false);
        queue.add(sr);
	}

}