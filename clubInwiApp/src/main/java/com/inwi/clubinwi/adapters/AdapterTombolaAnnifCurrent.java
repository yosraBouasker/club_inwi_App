package com.inwi.clubinwi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Offre;
import com.inwi.clubinwi.PdfActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.OnItemSelectRecyclerView;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.fragments.AchouraCadeauFragment;
import com.inwi.clubinwi.fragments.EnCoursFragment;
import com.inwi.clubinwi.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class AdapterTombolaAnnifCurrent extends RecyclerView.Adapter<AdapterTombolaAnnifCurrent.TombolaAnnifViewHolder> {

    private ArrayList<Offre> mList;
    private Context mContext;
    private OnItemSelectRecyclerView mOnItemSelectRecyclerView;



    public AdapterTombolaAnnifCurrent(Context context, ArrayList<Offre> tombolas, OnItemSelectRecyclerView mListner) {
        this.mList = tombolas;
        this.mContext = context;
        this.mOnItemSelectRecyclerView = mListner;

    }

    @Override
    public TombolaAnnifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kdo_encours_annif, parent, false);
        return new TombolaAnnifViewHolder(view,mOnItemSelectRecyclerView);
    }

    @Override
    public void onBindViewHolder(AdapterTombolaAnnifCurrent.TombolaAnnifViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTombolas(ArrayList<Offre> tombolas) {
        this.mList = tombolas;
        notifyDataSetChanged();
    }

    public void participate(final Offre tombola) {

        final String phone = Utils.readFromSharedPreferences(mContext, Constants.USER_PHONE);
        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);

        Log.i("url participate", Constants.URL_TOMBOLAS_ANNIF);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOMBOLAS_ANNIF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("header").equals("OK")) {
                        TombolaAnnifViewHolder holder ;
                        Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                        // ft.replace(R.id.content, new EnCoursFragment());
                        ft.replace(R.id.content, AchouraCadeauFragment.newInstance());
                        ft.commitAllowingStateLoss();

                    }


                } catch (JSONException e) {
                    MyLog.e("ERROR JSON", "error" + e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToastOnUiTread(mContext, "Veuillez resséyer");
                MyLog.e("ERROR", "" + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("cadeau_id", tombola.getId());
                params.put("niveau_id", tombola.getNiveau());
                params.put("points", tombola.getPoint());


                params.put("compagnie_id", tombola.getCompagnie_id());
                params.put("token", Token);
                //params.put("lang","fr");
                params.put("lang", Utils.readFromSharedPreferences(mContext, Constants.USER_LANGUE));



                Log.d("params", phone + " " + tombola.getId() + " " + tombola.getCompagnie_id() + " " + Token);
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
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(request, Constants.URL_TOMBOLAS_ANNIF);

    }

    private void undoLastCheck() {
        for (int i = 0; i < mList.size(); i++) {
            Offre tombola = mList.get(i);
           /* if (tombola.isChecked()) {
                tombola.setChecked(false);
            }*/
        }
    }

public class TombolaAnnifViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    public ImageView mImageTombola;
    public MyTextView mTextTile;
    public MyTextView mTextDescription;
    public MyTextView mTextReglement_accept;
    public ImageView mShareApp;
    public CheckBox check_reglement;
    public Button mButtonParticipe;
    private OnItemSelectRecyclerView mListner;


    public TombolaAnnifViewHolder(View itemView, OnItemSelectRecyclerView listner) {
        super(itemView);


        mImageTombola = itemView.findViewById(R.id.image_tombola);
        mTextTile = itemView.findViewById(R.id.annif_tombola_titre);
        mTextDescription = itemView.findViewById(R.id.annif_tombola_content);
        mTextReglement_accept = itemView.findViewById(R.id.accept_reglement);
        check_reglement = itemView.findViewById(R.id.accept_chexbox_annif);
        mButtonParticipe = itemView.findViewById(R.id.participate_annif);
        mShareApp = itemView.findViewById(R.id.icon_share);
        mTextReglement_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // PdfActivity.start((Activity) mContext, "https://club.inwi.ma/themes/club/imgs/anniversaire/reglement_jeu_tembola.pdf");
                Intent mIntent = new Intent(mContext, PdfActivity.class);
                mIntent.putExtra("url","https://club.inwi.ma/themes/club/imgs/anniversaire/reglement_jeu_tembola.pdf");
                mContext.startActivity(mIntent);
            }
        });

        mShareApp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String shareBody = "https://play.google.com/store/apps/details?id=com.inwi.clubinwi";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Partage Club Inwi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mContext.startActivity(Intent.createChooser(sharingIntent, "Partager"));
            }
        });

        mListner = listner;

        mButtonParticipe.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.participate_annif:
                mListner.onItemClick(getAdapterPosition(), "P");
                break;
            default:
                break;
        }
    }

    public void bind(final Offre tombola) {

        if(!tombola.isHasOffered()) {
            check_reglement.setChecked(false);
            check_reglement.setEnabled(false);
        }else {

            check_reglement.setEnabled(true);

        }
        mTextDescription.setText(tombola.getDescription());

        // if (benefit == 0 && status == 1) {

            //check_reglement.setClickable(!check_reglement.isChecked());
            check_reglement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //checkboxParticipate.setChecked(checkboxParticipate.isChecked());

                    if (check_reglement.isChecked()) {
                        check_reglement.setChecked(true);
                        check_reglement.setEnabled(true);

                        mButtonParticipe.setEnabled(true);
                        // uncheck privious checked item
                      //  undoLastCheck();

                        //Check current


                    }else {
                        check_reglement.setChecked(false);
                        mButtonParticipe.setEnabled(false);
                    }
                    //btnParticipate.setEnabled(checkboxParticipate.isChecked());
                    // Notify adapter so bind will be called for all items
                    notifyDataSetChanged();
                }
            });


       /* } else {
            checkboxParticipate.setClickable(false);

            checkboxParticipate.setChecked(false);
            btnParticipate.setActivated(false);
        }*/

            /*if (tombola.isChecked()) {

            } else {
                checkboxParticipate.setChecked(false);
                btnParticipate.setEnabled(false);
            }*/

        mTextTile.setText(tombola.getTitle());


        Glide.with(mContext).load(tombola.getImage()).into(mImageTombola);


       /* mButtonParticipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                participate(tombola);

            }
        });*/


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