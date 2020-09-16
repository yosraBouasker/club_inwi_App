package com.inwi.clubinwi.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.inwi.clubinwi.R;

public class HomeLoginPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ImageView annif_image;

    public HomeLoginPagerAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(View collection, int pos) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = null;

        if (pos == 0) {
            view = (RelativeLayout) inflater.inflate(R.layout.pager_login_0, null);
            view.setTag(pos);
        } else if (pos == 1) {
            view = (RelativeLayout) inflater.inflate(R.layout.pager_login_3, null);
            view.setTag(pos);
        } else if (pos == 2) {
            view = (RelativeLayout) inflater.inflate(R.layout.pager_login_1, null);
            view.setTag(pos);
        }

        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((RelativeLayout) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}	
