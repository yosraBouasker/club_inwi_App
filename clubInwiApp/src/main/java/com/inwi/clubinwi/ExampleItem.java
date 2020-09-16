package com.inwi.clubinwi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inwi.clubinwi.fragments.CKDOFragement;
import com.inwi.clubinwi.fragments.HistoriqueMesCadeauxFragment;
import com.inwi.clubinwi.fragments.cardViewGift;

import static android.app.PendingIntent.getActivity;

public class ExampleItem {
    private int mImageResource;
    private String mText;
    private RecyclerView mRec;
//    private String mButton;

    public ExampleItem(int imageResource, String text1) {
        mImageResource = imageResource;
        mText = text1;
    }
//
    public void changeText (RecyclerView rec, View mView){
      // mText = text;
        AppCompatActivity activity = (AppCompatActivity) mView.getContext();
        RecyclerViewGifts rec1 = new RecyclerViewGifts();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.recyclerView, rec1).addToBackStack(null).commit();

    }
    public int getImageResource() {
        return mImageResource;
    }
    public String getText1() {
        return mText;
    }
//    public String getText2() {
//        return mButton;
//    }
}

