package com.inwi.clubinwi.Utils;

import android.content.Context;

import com.inwi.clubinwi.Beans.Menu;
import com.inwi.clubinwi.Beans.SousMenu;
import com.inwi.clubinwi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Services {

    public static ArrayList<Menu> getMenu(Context mContext, String listForfaits) {
        ArrayList<Menu> values = new ArrayList<Menu>();
        //menu mes numeros
        ArrayList<SousMenu> menuNumbers = new ArrayList<SousMenu>();
        int count = 0;
        try {
            count = Integer.parseInt(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_COUNT));
            String currentPhoneNumbre = new JSONArray(Utils.readFromSharedPreferences(mContext, Constants.USER_FORFAITS_ACTIF)).getJSONObject(0).getString("id");
            if (currentPhoneNumbre != null && currentPhoneNumbre != "")
                menuNumbers.add(new SousMenu(R.drawable.check, currentPhoneNumbre, true, true));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (listForfaits != null) {
            try {
                JSONArray listNum = new JSONArray(listForfaits);
                for (int i = 0; i < listNum.length(); i++) {
                    if (listNum.getJSONObject(i).getString("status").equals("1"))
                        menuNumbers.add(new SousMenu(R.drawable.check, listNum.getJSONObject(i).getString("id"), true, true));
                    else
                        menuNumbers.add(new SousMenu(0, listNum.getJSONObject(i).getString("id"), false, true));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        menuNumbers.add(new SousMenu(0, mContext.getString(R.string.ajouter_numero), false, false));
        menuNumbers.add(new SousMenu(0, mContext.getString(R.string.historique_comte), false, false));
        values.add(new Menu((mContext.getString(R.string.mes_forfaits)), R.drawable.forfaits, count, menuNumbers));
        // menu cadeaux
        ArrayList<SousMenu> menuCadeaux = new ArrayList<SousMenu>();
        values.add(new Menu((mContext.getString(R.string.cadeaux)), R.drawable.cadeau, 0, menuCadeaux));
        // menu iflix
        ArrayList<SousMenu> iflixMenu = new ArrayList<SousMenu>();
        /*if ("fr".equals(Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE))){
            values.add(new Menu((mContext.getString(R.string.iflix)), R.drawable.ic_iflix, 0, iflixMenu));
        } else {
            values.add(new Menu(("\u200F"+" "+ mContext.getString(R.string.iflix)), R.drawable.ic_iflix, 0, iflixMenu));
        }*/
        //menu tombola
       /* ArrayList<SousMenu> menuTombola = new ArrayList<SousMenu>();
        if ("fr".equals(Utils.readFromSharedPreferences(mContext,Constants.USER_LANGUE))){
            values.add(new Menu((mContext.getString(R.string.tombola)), R.drawable.ic_tombola, 0, menuTombola));
        } else {
            values.add(new Menu(("\u200F"+" "+ mContext.getString(R.string.tombola)), R.drawable.ic_tombola, 0, menuTombola));
        }*/
        // menu settings
        ArrayList<SousMenu> menuSettings = new ArrayList<SousMenu>();
        values.add(new Menu((mContext.getString(R.string.settings)), R.drawable.settings, 0, menuSettings));
        //logout
        ArrayList<SousMenu> logout = new ArrayList<SousMenu>();
        values.add(new Menu((mContext.getString(R.string.logout)), R.drawable.logout, 0, logout));
        return values;
    }

}
