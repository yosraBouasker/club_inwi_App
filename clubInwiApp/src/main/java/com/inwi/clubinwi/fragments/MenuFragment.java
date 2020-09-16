package com.inwi.clubinwi.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Menu;
import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.MainActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Services;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.MenuListAdapter;
import com.inwi.clubinwi.views.AnimatedExpandableListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends BaseFragment {

    protected static final String TAG = "MenuFragment";
    String url = "";
    Handler handler = new Handler();
    private Context mContext;
    private Fragment mContent;
    private View mView;
    private ImageView image;
    private TextView fullname;
    private AnimatedExpandableListView listView;
    private MenuListAdapter adapter;
    private ArrayList<Menu> values = new ArrayList<Menu>();
    private ProgressDialog progressDialog;
    private RelativeLayout partager_application;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_menu, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {

        fullname = mView.findViewById(R.id.fullname);
        listView = mView.findViewById(R.id.list);
        image = mView.findViewById(R.id.avatar);
        partager_application = mView.findViewById(R.id.partager_application);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getResources().getString(R.string.patientez));
        progressDialog.setCancelable(true);

        addMenuImageName();
        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);

        values = Services.getMenu(mContext, listForfaits);
        System.out.println("OMAR menus " + values.toString());

        adapter = new MenuListAdapter(mContext, values);
        listView.setAdapter(adapter);

        loadListNumber();

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.

                if (groupPosition == 1) { //3
                    ((MainActivity) mContext).closeDrawer();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            ((MainActivity) mContext).changeContent(R.id.rl_ckdo, true);
                            mContent = new CKDOFragement();
                            //mContent = new TombolaAnnifFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
                        }
                    }, 300);

                }

                /*if (groupPosition == 2) { //3
                    ((MainActivity) mContext).closeDrawer();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            openWebPage("https://myinwi.ma/login");
                        }
                    }, 300);



                }*/

              /*  if (groupPosition == 2) {
                    ((MainActivity) mContext).closeDrawer();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            ((MainActivity) mContext).changeContent(R.id.rl_ckdo, true);
                            mContent = new IflixFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
                        }
                    }, 300);

                }*/

               /* if (groupPosition == 2) {
                    ((MainActivity) mContext).closeDrawer();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            ((MainActivity) mContext).changeContent(R.id.rl_ckdo, true);
                            mContent = new TombolaFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
                        }
                    }, 300);

                }*/

                if (groupPosition == 2) {
                    ((MainActivity) mContext).closeDrawer();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            ((MainActivity) mContext).changeContent(R.id.rl_ckdo, true);
                            mContent = new SettingsFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
                        }
                    }, 300);

                }
                if (groupPosition == 3) {

                    SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
                    if (mSharedPreferences != null)

                    // Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE);

                    mSharedPreferences.edit().remove(Constants.USER_TOKEN).commit();

                   // mSharedPreferences.edit().apply();


                    Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE);

                        Intent mIntent = new Intent(getContext(), LoginActivity.class);
                        startActivity(mIntent);
                        ((Activity) mContext).finish();


                }

                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);

                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        listView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                ((MainActivity) mContext).closeDrawer();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (groupPosition == 0) {
                            ((MainActivity) mContext).changeContent(0, true);
                            if (childPosition < Integer.parseInt(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT))) {

                                Log.i("phone number", "" + values.get(groupPosition).getmSubmenu().get(childPosition).getName());
                                Log.i("phone number is active", "" + values.get(groupPosition).getmSubmenu().get(childPosition).getIsActive());

                                changeCurrentPhoneNumber(childPosition);

                            } else if (childPosition == (Integer.parseInt(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT)))) {
                                Utils.switchFragment((FragmentActivity) mContext, new AddPhoneNumberFragment(), AddPhoneNumberFragment.class.toString(), R.id.content, true, true, true);
                                // ((MainActivity) mContext).closeDrawer();
                            } else if (childPosition == (Integer.parseInt(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT)) + 1)) {
                                Utils.switchFragment((FragmentActivity) mContext, new HistoriqueMesCadeauxFragment(), HistoriqueMesCadeauxFragment.class.toString(), R.id.content, true, true, true);

                            }
                        }

                    }


                }, 300);

                return false;
            }
        });

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) mContext).closeDrawer();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        ((MainActivity) mContext).changeContent(R.id.rl_profil, true);
//						((MainActivity) mContext).inProfil();
                        ((MainActivity) mContext).showProfil();
                        mContent = new ParrainFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("selectedItem", "profil");
                        mContent.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
                    }
                }, 300);

            }
        });
        partager_application.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String shareBody = "https://play.google.com/store/apps/details?id=com.inwi.clubinwi";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Partage Club Inwi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Partager"));
            }
        });
    }

    public void changeCurrentPhoneNumber(final int childPosition) {
        final String phone = values.get(0).getmSubmenu().get(childPosition).getName();

        if (values.get(0).getmSubmenu().get(childPosition).getIsActive()) {
            progressDialog.show();
            signIn(phone);
            ((MainActivity) mContext).changePhoneTitle();
        } else {
            AlertDialog.Builder builder;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth));
            } else {
                builder = new AlertDialog.Builder(mContext);
            }

            builder.setMessage("ce compte existe déjà mais est en attente d'activation. Voulez-vous l'activer?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // ((MainActivity)
                    // mContext).closeDrawer();
                    Log.i("phone number", phone);
                    Fragment fragment = new InscriptionStep3();
                    Bundle args = new Bundle();
                    args.putString("telephone", phone);
                    args.putString("fromFragment", "addNewNumber");
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

                }
            }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.show();
        }
    }

    public void addMenuImageName() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.profil).showImageOnFail(R.drawable.profil).build();
        if (Constants.imageLoader == null)
            Utils.initImageLoader(mContext);
        if (Utils.readFromSharedPreferences(mContext, Constants.USER_AVATAR) != null) {
            url = Utils.readFromSharedPreferences(mContext, Constants.USER_AVATAR);
            // imageLoader.get(url, ImageLoader.getImageListener(image,
            // R.drawable.profil, R.drawable.profil));

            Constants.imageLoader.displayImage(url, image, options);
        } else {
            image.setImageResource(R.drawable.profil);
        }

        if (Utils.readFromSharedPreferences(mContext, Constants.USER_FULLNAME) != null) {
            fullname.setText(Utils.readFromSharedPreferences(mContext, Constants.USER_FULLNAME));
        }
    }

    public void updateMenu() {
        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);
        values = Services.getMenu(mContext, listForfaits);
        System.out.println("OMAR " + values.toString());

        adapter = new MenuListAdapter(mContext, values);
        listView.setAdapter(adapter);
    }

    public void loadListNumber() {
        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);
        values = Services.getMenu(mContext, listForfaits);
        System.out.println("OMAR " + values.toString());

        adapter = new MenuListAdapter(mContext, values);
        listView.setAdapter(adapter);

    }

    private void signIn(final String phone) {
        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);
        String lng = Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE);
        String url = Constants.URL_BASE + "/api/client/telephone/info?telephone=" + phone + "&token=" + Token + "&lang=" + lng ;
        Log.i("url", url);
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyLog.e("SUCCESS LOGIN", response);
                progressDialog.dismiss();
                try {
                    String header = new JSONObject(response).getString("header");
                    if (header.equals("OK")) {
                        JSONObject js = new JSONObject(response).getJSONObject("result");
                        Utils.saveToSharedPreferences(mContext, Constants.USER_PHONE, phone);
                        Utils.saveToSharedPreferences(mContext, Constants.USER_TOKEN, js.getString("token"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FULLNAME, js.getString("full_name"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_AVATAR, js.getString("avatar"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_EMAIL, js.getString("email_address"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_POINT, js.getString("points"));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS, js.getJSONObject("filleuls").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FILLEULS_COUNT, String.valueOf(js.getJSONObject("filleuls").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS, js.getJSONObject("forfaits").getJSONArray("list").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF, js.getJSONObject("forfaits").getJSONArray("actif").toString());
                        Utils.saveToSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT, String.valueOf(js.getJSONObject("forfaits").getInt("count")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL, String.valueOf(js.getJSONObject("level").getInt("num")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_CADEAUX, String.valueOf(js.getInt("cadeaux")));
                        Utils.saveToSharedPreferences(mContext, Constants.USER_DATE, String.valueOf(js.getString("created_at")));
                        if (js.getJSONObject("level").getInt("num") == 7)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi");
                        else if (js.getJSONObject("level").getInt("num") == 8)
                            Utils.saveToSharedPreferences(mContext, Constants.USER_LEVEL_TYPE, "Club inwi Premium");
                        ((MainActivity) mContext).closeDrawer();
                        ((MainActivity) mContext).changeContent(R.id.rl_ckdo, true);
                        mContent = new CKDOFragement();
                        //mContent = new TombolaAnnifFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();

                        String listForfaits = Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS);
                        values = Services.getMenu(mContext, listForfaits);
                        adapter = new MenuListAdapter(mContext, values);
                        listView.setAdapter(adapter);
                        ((MainActivity) mContext).changePhoneTitle();
                        ((MainActivity) mContext).setHeader();

                    } else
                        Utils.showToastOnUiTread(mContext, new JSONObject(response).getString("message"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Utils.showToastOnUiTread(mContext, "Veuillez réessayer ultérieurement ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e("ERROR", "" + error.getMessage());
                Log.e("method update", "return error" + error);
                progressDialog.dismiss();
            }
        });
        int socketTimeout = 20000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(sr, url);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

}