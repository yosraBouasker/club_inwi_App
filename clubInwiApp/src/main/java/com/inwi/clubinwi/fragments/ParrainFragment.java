package com.inwi.clubinwi.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.NumberListAdapter;
import com.inwi.clubinwi.views.MyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParrainFragment extends BaseFragment {

    Context context;
    private View mView;
    private ImageView myParrainImageBack, myParrainImage, myImageViewLevel, imagePhone;
    private MyTextView myTextViewName, myTextViewFilleuls, myTextViewNumeros, myTextViewCadeaux, myTextViewLevelString, myTextViewDate, myTextViewNumero;
    private String selectedTab;
    private ListView myListNumber;
    private NumberListAdapter adapter;
    private String urlAvatarProfil = "";
    private String urlAvatarParrain = "";
    private LinearLayout myLayoutLevel, myLayoutCadeauxLevel;
    private boolean isListNumberVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mView = inflater.inflate(R.layout.fragment_parrain, container, false);
        init(mView);

        return mView;
    }

    private void init(View mView) {
        Bundle mArgs = getArguments();
        if (mArgs != null)
            selectedTab = mArgs.getString("selectedItem");
        else
            selectedTab = "profil";

        myParrainImageBack = mView.findViewById(R.id.myParrainImageBack);
        myParrainImage = mView.findViewById(R.id.myParrainImage);
        myImageViewLevel = mView.findViewById(R.id.myImageViewLevel);
        myTextViewName = mView.findViewById(R.id.myTextViewName);
        myTextViewFilleuls = mView.findViewById(R.id.myTextViewFilleuls);
        myTextViewNumeros = mView.findViewById(R.id.myTextViewNumeros);
        myTextViewCadeaux = mView.findViewById(R.id.myTextViewCadeaux);
        myTextViewLevelString = mView.findViewById(R.id.myTextViewLevelString);
        myTextViewDate = mView.findViewById(R.id.myTextViewDate);
        myListNumber = mView.findViewById(R.id.myListNumber);
        myLayoutLevel = mView.findViewById(R.id.myLayoutLevel);
        myLayoutCadeauxLevel = mView.findViewById(R.id.myLayoutCadeauxLevel);
        imagePhone = mView.findViewById(R.id.imagePhone);
        myTextViewNumero = mView.findViewById(R.id.myTextViewNumero);

        populateListeNumber();

        Bitmap image = Bitmap.createBitmap(95, 95, Bitmap.Config.ARGB_8888);
        image.eraseColor(android.graphics.Color.WHITE);
        myParrainImageBack.setImageBitmap(Utils.getCroppedBitmap(image));

        myParrainImage.setImageDrawable(this.getResources().getDrawable(R.drawable.profil));
        if (selectedTab == "profil")
            showProfilData();
        else
            showParrainData();
    }

    public void populateListeNumber() {
        final String listForfaits = Utils.readFromSharedPreferences(context, Constants.USER_FORFAITS);
        try {
            JSONArray listNum = new JSONArray();
            if (listForfaits != null && !listForfaits.equals("[]") && !listForfaits.equals("")) {
                listNum = new JSONArray(listForfaits);
            }
            JSONObject firstNumber = new JSONArray(Utils.readFromSharedPreferences(context, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0);
            listNum.put(firstNumber);
            Log.i("listNum", "" + listNum);
            adapter = new NumberListAdapter(context, listNum);
            myListNumber.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showProfilData() {

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.profil).showImageOnFail(R.drawable.profil).build();
        if (Constants.imageLoader == null)
            Utils.initImageLoader(context);
        if (Utils.readFromSharedPreferences(context, Constants.USER_AVATAR) != null) {
            urlAvatarProfil = Utils.readFromSharedPreferences(context, Constants.USER_AVATAR);
            Constants.imageLoader.displayImage(urlAvatarProfil, myParrainImage, options);
        } else
            myParrainImage.setImageResource(R.drawable.profil);

        String fullName = Utils.readFromSharedPreferences(context, Constants.USER_FULLNAME);
        String filleulsCount = Utils.readFromSharedPreferences(context, Constants.USER_FILLEULS_COUNT);
        String forfaitsCount = Utils.readFromSharedPreferences(context, Constants.USER_FORFAITS_COUNT);
        String cadeaux = Utils.readFromSharedPreferences(context, Constants.USER_CADEAUX);
        String date = Utils.readFromSharedPreferences(context, Constants.USER_DATE);
        int level = Integer.parseInt(Utils.readFromSharedPreferences(context, Constants.USER_LEVEL));
        String type = Utils.readFromSharedPreferences(context, Constants.USER_LEVEL_TYPE);

        if (fullName != null)
            myTextViewName.setText(fullName);
        if (filleulsCount != null)
            myTextViewFilleuls.setText(filleulsCount);
        if (forfaitsCount != null)
            myTextViewNumeros.setText(forfaitsCount);
        if (cadeaux != null)
            myTextViewCadeaux.setText(cadeaux);
        if (level != 0) {
            if (level == 7) {
                myImageViewLevel.setImageDrawable(this.getResources().getDrawable(R.drawable.niveau1));
                myTextViewLevelString.setText(type);
            } else if (level == 8) {
                myImageViewLevel.setImageDrawable(this.getResources().getDrawable(R.drawable.niveau3));
                myTextViewLevelString.setText(type);
            }
        }
        myTextViewDate.setText(date);
    }

    private void showParrainData() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.profil).showImageOnFail(R.drawable.profil).build();
        if (Constants.imageLoader == null)
            Utils.initImageLoader(context);
        if (Utils.readFromSharedPreferences(context, Constants.PARRAIN_AVATAR) != null) {
            urlAvatarProfil = Utils.readFromSharedPreferences(context, Constants.PARRAIN_AVATAR);
            Constants.imageLoader.displayImage(urlAvatarProfil, myParrainImage, options);
        } else
            myParrainImage.setImageResource(R.drawable.profil);

        String fullName = Utils.readFromSharedPreferences(context, Constants.PARRAIN_FULLNAME);
        String filleulsCount = Utils.readFromSharedPreferences(context, Constants.PARRAIN_FILLEULS_COUNT);
        String forfaitsCount = Utils.readFromSharedPreferences(context, Constants.PARRAIN_FORFAITS_COUNT);
        String cadeaux = Utils.readFromSharedPreferences(context, Constants.PARRAIN_CADEAUX);
        String date = Utils.readFromSharedPreferences(context, Constants.PARRAIN_DATE);
        int level = Utils.readFromSharedPreferences(context, Constants.PARRAIN_AVATAR) != null ?
                Integer.parseInt(Utils.readFromSharedPreferences(context, Constants.PARRAIN_LEVEL)) : 0;
        if (fullName != null)
            myTextViewName.setText(fullName);
        if (filleulsCount != null)
            myTextViewFilleuls.setText(filleulsCount);
        if (forfaitsCount != null)
            myTextViewNumeros.setText(forfaitsCount);
        if (cadeaux != null)
            myTextViewCadeaux.setText(cadeaux);
        if (level != 0) {
            if (level == 7) {
                myTextViewLevelString.setText(R.string.club_inwi);
                myImageViewLevel.setImageDrawable(this.getResources().getDrawable(R.drawable.niveau1));
            } else if (level == 8) {
                myTextViewLevelString.setText(R.string.club_inwi_premium);
                myImageViewLevel.setImageDrawable(this.getResources().getDrawable(R.drawable.niveau3));
            }
        }
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
            try {
                Date date0 = dateFormat.parse(date);
                myTextViewDate.setText((dateFormat2.format(date0)).toUpperCase(Locale.FRANCE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}	
