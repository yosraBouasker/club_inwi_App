package com.inwi.clubinwi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.inwi.clubinwi.Beans.Filleul;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.views.MyTextView;

import java.util.ArrayList;

public class FilleulsListAdapter extends BaseAdapter {

    public ArrayList<Filleul> values;
    private Context activity;

    public FilleulsListAdapter(Context context, ArrayList<Filleul> values) {
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
            convertView = inflater.inflate(R.layout.adapter_filleuls, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fullName = convertView.findViewById(R.id.myTextViewFullName);
        holder.phoneNumber = convertView.findViewById(R.id.myTextViewPhoneNumber);
        holder.status = convertView.findViewById(R.id.myTextViewStatus);
        holder.avatar = convertView.findViewById(R.id.avatarFilleul);

        holder.fullName.setText(values.get(position).getFull_name().toUpperCase());
        holder.phoneNumber.setText(values.get(position).getMsisdn());
        holder.status.setText(values.get(position).getStatus());
        if (values.get(position).getStatus().equals("Actif"))
            holder.status.setBackgroundResource(R.drawable.round_corners_filleul_accepte);
        else
            holder.status.setBackgroundResource(R.drawable.round_corners_filleul_encours);


        return convertView;
    }

    static class ViewHolder {
        MyTextView fullName, phoneNumber, status;
        ImageView avatar;
    }
}
