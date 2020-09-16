package com.inwi.clubinwi.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwi.clubinwi.R;

import org.json.JSONArray;
import org.json.JSONException;

public class NumberListAdapter extends BaseAdapter {

    public JSONArray values;
    private Context activity;
    private ProgressDialog progressDialog;

    public NumberListAdapter(Context context, JSONArray values) {
        this.activity = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_number, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.phoneNumber = convertView.findViewById(R.id.myPhoneNumber);
        holder.deleteButton = convertView.findViewById(R.id.deleteButton);
        holder.deleteImage = convertView.findViewById(R.id.deleteImage);
        try {
            holder.phoneNumber.setText(values.getJSONObject(position).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView phoneNumber, deleteButton;
        ImageView deleteImage;

    }
}
