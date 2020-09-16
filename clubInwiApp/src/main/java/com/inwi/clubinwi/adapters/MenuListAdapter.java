package com.inwi.clubinwi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inwi.clubinwi.Beans.Menu;
import com.inwi.clubinwi.R;
import com.inwi.clubinwi.views.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import java.util.ArrayList;

public class MenuListAdapter extends AnimatedExpandableListAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Menu> values = new ArrayList<Menu>();

    public MenuListAdapter(Context mContext, ArrayList<Menu> values) {
        super();
        this.mContext = mContext;
        this.values = values;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return values.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return values.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return values.get(groupPosition).getmSubmenu().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderParent holder;
        if (convertView == null) {
            holder = new ViewHolderParent();
            convertView = inflater.inflate(R.layout.adapter_menu, parent, false);
            holder.title = convertView.findViewById(R.id.textTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderParent) convertView.getTag();
        }
        holder.title = convertView.findViewById(R.id.title);
        holder.title.setText(values.get(groupPosition).getName());

        holder.count = convertView.findViewById(R.id.countMenu);
        if (values.get(groupPosition).getCount() != 0)
            holder.count.setText("" + values.get(groupPosition).getCount());
        else
            holder.count.setText("");

        holder.icon = convertView.findViewById(R.id.icon);
        holder.icon.setImageResource(values.get(groupPosition).getIcon());

        holder.iconUpDown = convertView.findViewById(R.id.icon2);
        if (groupPosition == 1 || groupPosition == 3 || groupPosition == 4 || groupPosition == 2)// (groupPosition == 2 || groupPosition == 3)
            holder.iconUpDown.setVisibility(View.GONE);
        else
            holder.iconUpDown.setVisibility(View.VISIBLE);

        if (isExpanded) {
            holder.iconUpDown.setImageResource(R.drawable.up);
        } else {
            holder.iconUpDown.setImageResource(R.drawable.down);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolderChild holder;
        if (convertView == null) {
            holder = new ViewHolderChild();
            convertView = inflater.inflate(R.layout.adapter_sous_menu, parent, false);
            holder.title = convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        holder.title = convertView.findViewById(R.id.title);
        holder.title.setText(values.get(groupPosition).getmSubmenu().get(childPosition).getName());

        holder.icon = convertView.findViewById(R.id.icon);
        if (values.get(groupPosition).getmSubmenu().get(childPosition).getIcon() != 0) {
            holder.icon.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.icon.setImageDrawable(mContext.getResources().getDrawable(values.get(groupPosition).getmSubmenu().get(childPosition).getIcon(), mContext.getApplicationContext().getTheme()));
            } else {
                holder.icon.setImageDrawable(mContext.getResources().getDrawable(values.get(groupPosition).getmSubmenu().get(childPosition).getIcon()));
            }
        } else
            holder.icon.setVisibility(View.INVISIBLE);

        if (groupPosition == 0 && childPosition != 0 && values.get(groupPosition).getmSubmenu().get(childPosition).getIsPhoneNumber())
            holder.title.setTextColor(mContext.getResources().getColor(R.color.grayColor));
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return values.get(groupPosition).getmSubmenu().size();
    }

    static class ViewHolderParent {
        TextView title, count;
        ImageView icon, iconUpDown;
    }

    static class ViewHolderChild {
        TextView title;
        ImageView icon;
    }
}