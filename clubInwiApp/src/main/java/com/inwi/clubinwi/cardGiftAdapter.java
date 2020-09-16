package com.inwi.clubinwi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inwi.clubinwi.Utils.Utils;

import java.util.ArrayList;

public class cardGiftAdapter extends RecyclerView.Adapter<cardGiftAdapter.ExampleViewHolder>{
    Context mContext;
    Dialog my_dialog;
    ArrayList<CardView> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout popup_item;
        public ImageView mImageView;
        public Button mButton;
        public TextView mTextView1, mTextView2;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            popup_item = (RelativeLayout) itemView.findViewById(R.id.popupView);
            mImageView = itemView.findViewById(R.id.cardIcon);
            mTextView1 = itemView.findViewById(R.id.text1);
            mTextView2 = itemView.findViewById(R.id.textCard);
            mButton = itemView.findViewById(R.id.ButtonProfiter);
        }
    }
    public cardGiftAdapter(Context mContext, ArrayList<CardView> exampleList) {

        mExampleList = exampleList;
        this.mContext = mContext;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_view_gift, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        CardView currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());

        my_dialog = new Dialog(mContext);
        my_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        my_dialog.setContentView(R.layout.popup_dialog);

        Button closeButton = (Button) my_dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utils.showToastOnUiTread(mContext, "test click 2");
                my_dialog.dismiss();
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ImageView image_view_popup = (ImageView) my_dialog.findViewById(R.id.image_view_popup);
                    ImageView image_popup_gone = (ImageView) my_dialog.findViewById(R.id.image_view_popup1);

                    TextView first_text_view = (TextView) my_dialog.findViewById(R.id.first_text_view_popup);
                    TextView first_text_view_gone = (TextView) my_dialog.findViewById(R.id.first_text_view_popup1);

                    TextView second_text_view1 = (TextView) my_dialog.findViewById(R.id.second_text_view_popup);

                    Button mButtonProfiter = (Button) my_dialog.findViewById(R.id.ButtonProfiterPopup);
                    TextView mCongratsText = (TextView) my_dialog.findViewById(R.id.congratsText);

                    image_view_popup.setImageResource(mExampleList.get(holder.getAdapterPosition()).getImage());
                    image_popup_gone.setImageResource(mExampleList.get(holder.getAdapterPosition()).getImage());

                    first_text_view.setText(mExampleList.get(holder.getAdapterPosition()).getText1());
                    first_text_view_gone.setText(mExampleList.get(holder.getAdapterPosition()).getText1());

                mButtonProfiter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            image_view_popup.setVisibility(View.INVISIBLE);
                            image_popup_gone.setVisibility(View.VISIBLE);

                            first_text_view.setVisibility(View.GONE);
                            first_text_view_gone.setVisibility(View.VISIBLE);

                            second_text_view1.setVisibility(View.INVISIBLE);

                            mButtonProfiter.setVisibility(View.GONE);

                            mCongratsText.setVisibility(View.VISIBLE);
//                            notifyItemChanged(position);
                        }

                    });
                    my_dialog.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
