package com.inwi.clubinwi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

    protected static final String TAG = "BaseFragment";
    private View retourImageView;
//	protected View mView;


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
//		this.mView = view;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    protected void retour(View view) {

        if (retourImageView != null) {

            //Utils.backToParent(getActivity(), this);
            getActivity().onBackPressed();
        }

    }


}
