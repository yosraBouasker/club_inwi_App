package com.inwi.clubinwi.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.BuildConfig;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.HomeLoginPagerAdapter;
import com.inwi.clubinwi.views.MyTextView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class HomeLoginFragment extends BaseFragment {

	protected static final String TAG = "HomeLoginFragment";
	private Context mContext;
	private View mView;
	private TextView inscription, connexion;
	private ViewPager dashViewPager;
	private CirclePageIndicator circlePageIndicator;
	private HomeLoginPagerAdapter mAdapter;
	private ImageView annif_image;
	private boolean ifAppFound = false;
	private String packageOtherApp = "ma.inwi.selfcaremobile";


	String app_version, gcm_token, token, device_uuid, model, os, gcm_token_v;
	String push_status = "0";
	private ProgressDialog progressDialog;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mView = inflater.inflate(R.layout.fragment_home_login, container, false);
		mContext = getActivity();
		init(mView);
		controlVersion();
		return mView;
	}

	private void init(View mView) {

		progressDialog = new ProgressDialog(mContext);

		progressDialog.setMessage(getResources().getString(R.string.patientez));
		progressDialog.setCancelable(false);



		inscription = (TextView) mView.findViewById(R.id.inscription);
		connexion = (TextView) mView.findViewById(R.id.connexion);
		circlePageIndicator = (CirclePageIndicator) mView.findViewById(R.id.pager_indicator);
		dashViewPager = (ViewPager) mView.findViewById(R.id.viewpager);

		mAdapter = new HomeLoginPagerAdapter(mContext);

		dashViewPager.setAdapter(mAdapter);
		try {
			circlePageIndicator.setViewPager(dashViewPager);
		} catch (Exception ex) {
			ex.printStackTrace();
		}






		ifAppFound = isAppInstalled(mContext, packageOtherApp);

		inscription.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ifAppFound) {
					Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageOtherApp);
					if (intent != null) {
						startActivity(intent);
					}
				} else {

					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(packageOtherApp)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageOtherApp)));
					}

				}
			}
		});

		connexion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.switchFragment((FragmentActivity) mContext, new LoginFragment(), LoginFragment.class.toString(), R.id.content_login, true, true, true);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		ifAppFound = isAppInstalled(mContext, packageOtherApp);
	}

	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}



	@Override
	public void onDestroy() {
		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		super.onDestroy();
	}



	void controlVersion() {
		PackageInfo pInfo = null;
		try {
			pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		app_version = pInfo.versionName;
		push_status = "1";
		token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
		os = "android";
		model = Build.MODEL;

		try {
			TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.

				device_uuid = tManager.getDeviceId();

				return;
			}
			device_uuid = tManager.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gcm_token = FirebaseInstanceId.getInstance().getToken();

		Utils.readFromSharedPreferences(mContext, Constants.SHARED_PREFERENCES_GCM_TOKEN);
		gcm_token_v = Utils.readFromSharedPreferences(mContext, Constants.SHARED_PREFERENCES_GCM_TOKEN_VERSION);
		//  String url = "https://club.inwi.ma/api/add/device";
		StringRequest sr = new StringRequest(Request.Method.POST, Constants.URL_CONTROL_VERSION, new Response.Listener<String>() {
			// StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject object = new JSONObject(response);
					int status = object.getInt("status");
					if (status == 201) {
						if (object.getJSONObject("result").getString("url") != "")
							showDialog(getActivity(), object.getString("message"), object.getJSONObject("result").getString("url"), 0);
						if (object.getJSONObject("result").getInt("cache") == 1) {
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/ckdo");
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/pages");
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/historique");
							Utils.removeFromSharedPreferences(mContext, Constants.USER_TOKEN);
							//	startActivity(new Intent(mContext,SplashActivity.class));
							//getActivity().finish();
						}
					} else if (status == 202) {
						if (object.getJSONObject("result").getString("url") != "")
							showDialog(getActivity(), object.getString("message"), object.getJSONObject("result").getString("url"), 1);
						if (object.getJSONObject("result").getInt("cache") == 1) {
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/ckdo");
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/pages");
							Utils.removeFromSharedPreferences(mContext, Constants.URL_BASE + "/historique");
							Utils.removeFromSharedPreferences(mContext, Constants.USER_TOKEN);
							//		startActivity(new Intent(mContext,SplashActivity.class));
							//getActivity().finish();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Utils.showToastOnUiTread(mContext, getString(R.string.probleme_connexion));
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(progressDialog != null && progressDialog.isShowing() && !getActivity().isFinishing())
					progressDialog.dismiss();
				MyLog.e("ERROR LOGIN", "" + error.getMessage());
				Utils.showToastOnUiTread(mContext, mContext.getResources().getString(R.string.probleme_connexion));
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token + "");
				params.put("app_version", BuildConfig.VERSION_NAME + "");
				params.put("app_version_code", BuildConfig.VERSION_CODE + "");
				params.put("os", os + "");
				params.put("push_status", push_status + "");
				params.put("app_token", gcm_token + "");
				params.put("model", model + "");
				params.put("device_uid", device_uuid + "");

				if ("fr".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)) || "ar".equalsIgnoreCase(Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE)))
					params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));
				//else params.put("lang", "fr");


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
		int socketTimeout = 50000;// 20 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		sr.setRetryPolicy(policy);
		AppController.getInstance().addToRequestQueue(sr, Constants.URL_CONTROL_VERSION);

	}



	public void showDialog(Activity activity, String msg, final String url, int status) {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_control_version);
		Log.e("URL", url);
		MyTextView text = dialog.findViewById(R.id.myTextViewMessageControlVersion);
		text.setText(msg);
		MyTextView annuler = dialog.findViewById(R.id.myTextViewAnnulerControlVersion);
		MyTextView confirmer = dialog.findViewById(R.id.myTextViewConfirmerControlVersion);
		if (status == 0)
			annuler.setVisibility(View.GONE);

		annuler.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		confirmer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageOtherApp)));
			}
		});

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.show();

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
	