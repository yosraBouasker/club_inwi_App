package com.inwi.clubinwi.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.inwi.clubinwi.fragments.HomePagerFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private JSONArray values = new JSONArray();

    public HomePagerAdapter(FragmentManager fm, JSONArray values) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.values = values;
    }

    @Override
    public Fragment getItem(int i) {
        // TODO Auto-generated method stub
        System.out.println("OMAR!!");
        Fragment fragment = new HomePagerFragment();
        Bundle args = new Bundle();
        try {
            args.putString("value", values.getJSONObject(i).toString());
           // System.out.println("rachid data : "+values.getJSONArray(i).toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        try {

           // System.out.println("rachid getPageTitle : "+ values.getJSONObject(position).getString("title"));
            return values.getJSONObject(position).getString("title");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "null";
    }

}
