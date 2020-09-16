package com.inwi.clubinwi.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.inwi.clubinwi.AppController;
import com.inwi.clubinwi.Beans.Offre;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.views.MyTextView;

import java.util.ArrayList;

public class historiqueListAdapter extends BaseAdapter {

    public ArrayList<Offre> values;
    private Context activity;

    public historiqueListAdapter(Context context, ArrayList<Offre> values) {
        this.activity = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_historique, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleHistorique = convertView.findViewById(R.id.myTextViewTitleHistorique);


        holder.imageHistorique = convertView.findViewById(R.id.myImageViewHistorique);
        holder.textView_point_item = convertView.findViewById(R.id.textView_point_item);
        holder.textView_categ_item = convertView.findViewById(R.id.textView_categ_item);

        holder.textView_categ_item.setText(values.get(position).getCategorie());

        int nbr = Integer.parseInt(values.get(position).getPoint());
        String mystring = activity.getResources().getQuantityString(R.plurals.numberOfItems, nbr, values.get(position).getPoint());
        holder.textView_point_item.setText(values.get(position).getPoint()+" "+mystring);
        //holder.titleHistorique.setText(values.get(position).getTitle());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.titleHistorique.setText(Html.fromHtml(values.get(position).getTitle(),Html.FROM_HTML_MODE_LEGACY));

        } else {
            //holder.titleHistorique.setText(Html.fromHtml("<p dir=\"rtl\">Mo 50 من الانترنت</p>"));
            holder.titleHistorique.setText(Html.fromHtml(values.get(position).getTitle()));
        }


        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        String url = values.get(position).getImage();
        imageLoader.get(url, ImageLoader.getImageListener(holder.imageHistorique, R.drawable.cadeau_historique, R.drawable.cadeau_historique));

        return convertView;
    }

    static class ViewHolder {
        MyTextView titleHistorique, textView_point_item, textView_categ_item;
        ImageView imageHistorique;
    }
}
