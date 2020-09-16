package com.inwi.clubinwi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;

import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Constants;
import com.inwi.clubinwi.Utils.Utils;
import com.inwi.clubinwi.adapters.HomeListAdapter;
import com.inwi.clubinwi.views.AnimatedExpandableListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePagerFragment extends Fragment {

    protected static final String TAG = "HomePagerFragment";
    JSONObject value;
    private Context mContext;
    private View mView;
    private ImageView image;
    private AnimatedExpandableListView listView;
    private HomeListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_home_pager, container, false);
        mContext = getActivity();
        init(mView);

        return mView;
    }

    private void init(View mView) {
        listView = mView.findViewById(R.id.list);
        image = mView.findViewById(R.id.image);
        listView = mView.findViewById(R.id.list);
        String url = "";
        JSONArray values = null;

        Bundle args = getArguments();
        try {
            value = new JSONObject(args.getString("value"));
            url = value.getString("image");
            values = value.getJSONArray("details");
           // System.out.println("rachid : "+" url image : "+url+" , details "+values.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.default_cadeau).showImageOnFail(R.drawable.default_cadeau).build();
        if (Constants.imageLoader == null)
            Utils.initImageLoader(mContext);
        if (url != null) {
            Constants.imageLoader.displayImage(url, image, options);
        } else
            image.setImageResource(R.drawable.default_cadeau);

        adapter = new HomeListAdapter(mContext, values);
        listView.setAdapter(adapter);
        listView.expandGroupWithAnimation(0);
        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
