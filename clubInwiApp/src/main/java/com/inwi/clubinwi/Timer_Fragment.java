package com.inwi.clubinwi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inwi.clubinwi.fragments.CKDOFragement;
import com.inwi.clubinwi.fragments.EnCoursFragment;

public class Timer_Fragment extends Fragment {

    private View mView;
    private Context mContext;
    public Timer_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_timer_, container, false);

        mView = inflater.inflate(R.layout.fragment_ckdo, container, false);
        mContext = getActivity();
        init(mView);
        return null;
    }

    private void init(View mView) {
        Bundle mArgs = getArguments();
        if (mArgs != null) {
            Boolean bool = mArgs.getBoolean("first");
            if(bool){
                CKDOFragement mContent = new CKDOFragement();
                Bundle bundle = new Bundle();
                bundle.putBoolean("first", false);
                mContent.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, mContent).commit();
            }
        }
    }


}