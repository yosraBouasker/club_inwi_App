package com.inwi.clubinwi.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Tombola;
import com.inwi.clubinwi.PdfActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.fragments.TombolaFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 2/26/18.
 * TombolaAdapter
 */

public class TombolaAdapter extends RecyclerView.Adapter<TombolaAdapter.TombolaViewHolder> {

    private List<Tombola> tombolas;
    private Context mContext;

    public TombolaAdapter(Context context, ArrayList<Tombola> tombolas) {
        this.tombolas = tombolas;
        this.mContext = context;
    }

    @Override
    public TombolaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tombola, parent, false);
        return new TombolaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TombolaViewHolder holder, int position) {
        holder.bind(tombolas.get(position));
    }

    @Override
    public int getItemCount() {
        return tombolas.size();
    }

    public void setTombolas(List<Tombola> tombolas) {
        this.tombolas = tombolas;
        notifyDataSetChanged();
    }

    public void participate(final Tombola tombola) {

        final String phone = Utils.readFromSharedPreferences(mContext, Constants.USER_PHONE);
        final String Token = Utils.readFromSharedPreferences(mContext, Constants.USER_TOKEN);

        Log.i("url participate", Constants.URL_TOMBOLAS_BENEFIT);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_TOMBOLAS_BENEFIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("header").equals("OK")) {

                        Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content, new TombolaFragment());
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
                params.put("ticket_id", tombola.getTicketId());
                params.put("compagnie_id", tombola.getCompagnieId());
                params.put("token", Token);

                Log.d("params", phone + " " + tombola.getTicketId() + " " + tombola.getCompagnieId() + " " + Token);
                return params;
            }
        };
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(request, Constants.URL_TOMBOLAS_BENEFIT);

    }

    private void undoLastCheck() {
        for (int i = 0; i < tombolas.size(); i++) {
            Tombola tombola = tombolas.get(i);
            if (tombola.isChecked()) {
                tombola.setChecked(false);
            }
        }
    }

    public class TombolaViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public CheckBox checkboxParticipate;
        public Button btnParticipate;
        public TextView mTextTile;
        public TextView txtCity;
        public TextView pdf;

        public TombolaViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_tombola);
            txtCity = itemView.findViewById(R.id.tv_city);
            mTextTile = itemView.findViewById(R.id.tv_date_place);
            checkboxParticipate = itemView.findViewById(R.id.accept_chexbox);
            btnParticipate = itemView.findViewById(R.id.participate);
            pdf = itemView.findViewById(R.id.accept);
        }

        public void bind(final Tombola tombola) {

            int benefit = tombola.getBenefit();
            int status = tombola.getStatus();
            if (benefit == 0 && status == 1) {

                checkboxParticipate.setClickable(!checkboxParticipate.isChecked());
                checkboxParticipate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //checkboxParticipate.setChecked(checkboxParticipate.isChecked());

                        if (checkboxParticipate.isChecked()) {
                            checkboxParticipate.setChecked(true);
                            btnParticipate.setEnabled(true);
                            // uncheck privious checked item
                            undoLastCheck();

                            //Check current
                            tombola.setChecked(true);

                        }else {
                            checkboxParticipate.setChecked(false);
                            btnParticipate.setEnabled(false);
                        }
                        //btnParticipate.setEnabled(checkboxParticipate.isChecked());
                        // Notify adapter so bind will be called for all items
                        notifyDataSetChanged();
                    }
                });


            } else {
                checkboxParticipate.setClickable(false);

                checkboxParticipate.setChecked(false);
                btnParticipate.setActivated(false);
            }

            /*if (tombola.isChecked()) {

            } else {
                checkboxParticipate.setChecked(false);
                btnParticipate.setEnabled(false);
            }*/

            mTextTile.setText(tombola.getTitle());
            String ville = tombola.getVille();
            if (ville != null && !ville.isEmpty()) {
                txtCity.setVisibility(View.VISIBLE);
                txtCity.setText(ville);
            } else {
                txtCity.setVisibility(View.GONE);
            }

            Glide.with(mContext).load(tombola.getImage()).into(mImage);


            btnParticipate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    participate(tombola);

                }
            });

            pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PdfActivity.start((Activity) mContext, tombola.getPdf());

                }
            });
        }
    }
}
