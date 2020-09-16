package com.inwi.clubinwi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwi.clubinwi.LoginActivity;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.Utils;

public class ForgotPasswordResult extends BaseFragment {

    protected static final String TAG = "HomeLoginFragment";
    private Context mContext;
    private View mView;
    private TextView valider;
    private ImageView close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_forgot_password_result, container, false);
        mContext = getActivity();
        init(mView);
        return mView;
    }

    private void init(View mView) {
        ((TextView) mView.findViewById(R.id.title)).setText(getString(R.string.mot_de_passe_oublie));
        close = mView.findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((LoginActivity) mContext).onBackPressed();
            }
        });


        valider = mView.findViewById(R.id.valider);

        valider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragment((FragmentActivity) mContext, new ForgetPasswordSMS(), ForgetPasswordSMS.class.toString(), R.id.content_login, true, true, true);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
