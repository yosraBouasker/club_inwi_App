package com.inwi.clubinwi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inwi.clubinwi.Beans.Offre;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.Utils.OnItemSelectRecyclerView;
import com.inwi.clubinwi.views.MyTextView;

import java.util.ArrayList;


public class AdapterCurrentOffre extends RecyclerView.Adapter<AdapterCurrentOffre.ItemViewHolder> {

    private ArrayList<Offre> mList;
    private OnItemSelectRecyclerView mOnItemSelectRecyclerView;
    private Context mContext;

    public AdapterCurrentOffre(ArrayList<Offre> mList, Context mContext, OnItemSelectRecyclerView mListner) {
        this.mOnItemSelectRecyclerView = mListner;
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mList.size();
       // return 1 ;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder mHolder, final int position) {
        mHolder.textView_categ_item.setText(mList.get(position).getCategorie());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mHolder.mTextTile.setText(Html.fromHtml(mList.get(position).getTitle(),Html.FROM_HTML_MODE_LEGACY));

        } else {
            mHolder.mTextTile.setText(Html.fromHtml(mList.get(position).getTitle()));
        }

        //mHolder.mTextTile.setText(mList.get(position).getTitle());

        int nbr = Integer.parseInt(mList.get(position).getPoint());
        String mystring = mContext.getResources().getQuantityString(R.plurals.numberOfItems, nbr, mList.get(position).getPoint());

        mHolder.mTextPoint.setText(mList.get(position).getPoint() + mystring);
        Glide.with(mContext).
                load(mList.get(position).getImage()).
                // load(R.drawable.achoura_miniature).
                into(mHolder.mImage);

        if (mList.get(position).isComming()) {
            mHolder.mTextProfitOffrir.setTag("false");
            mHolder.mTextProfit.setTag("false");


        }
        if(!mList.get(position).isHasOffered() && !mList.get(position).isCanOfferAndBenefit()){

            mHolder.mTextProfitOffrir.setEnabled(false);
            mHolder.mTextProfit.setEnabled(false);
        }
        Log.e("NIVEAU", mList.get(position).getNiveau() + "");
        if (Integer.parseInt(mList.get(position).getNiveau()) == 7)
            mHolder.mTextProfitOffrir.setVisibility(View.GONE);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        final View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_kdo_encours, arg0, false);
        return new ItemViewHolder(view, mOnItemSelectRecyclerView);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public ImageView mImage;
        public MyTextView mTextTile;
        public MyTextView mTextPoint;
        public MyTextView mTextProfitOffrir;
        public MyTextView mTextProfit;
        public MyTextView textView_categ_item;
        private OnItemSelectRecyclerView mListner;

        public ItemViewHolder(View itemView, OnItemSelectRecyclerView listner) {
            super(itemView);

            textView_categ_item = itemView.findViewById(R.id.textView_categ_item);
            mImage = itemView.findViewById(R.id.imageView_kdo_item);
            mTextTile = itemView.findViewById(R.id.textView_title_item);
            mTextPoint = itemView.findViewById(R.id.textView_point_item);
            mTextProfitOffrir = itemView.findViewById(R.id.textView_offrir);
            mTextProfit = itemView.findViewById(R.id.textView_profiter);
            mListner = listner;
            mTextProfitOffrir.setOnClickListener(this);
            mTextProfit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.textView_offrir:
                    mListner.onItemClick(getAdapterPosition(), "O");
                    break;
                case R.id.textView_profiter:
                    mListner.onItemClick(getAdapterPosition(), "P");
                    break;
                default:
                    break;
            }
        }
    }
}
