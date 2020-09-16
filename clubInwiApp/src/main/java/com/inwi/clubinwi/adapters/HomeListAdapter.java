package com.inwi.clubinwi.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.inwi.clubinwi.R;
import com.inwi.clubinwi.views.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HomeListAdapter extends AnimatedExpandableListAdapter implements ImageGetter {
    private LayoutInflater inflater;
    private Context mContext;
    private JSONArray values = new JSONArray();
    private WebView mTvHtml;

    public HomeListAdapter(Context mContext, JSONArray values) {
        super();
        this.mContext = mContext;
        this.values = values;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return values.length();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        try {
            return values.getJSONObject(groupPosition).get("webview");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        try {
            return values.getJSONObject(childPosition).getString("description");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolderParent holder;
        if (convertView == null) {
            holder = new ViewHolderParent();
            convertView = inflater.inflate(R.layout.adapter_home_list, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderParent) convertView.getTag();
        }

//        holder.icon = convertView.findViewById(R.id.icon);
        holder.webview = convertView.findViewById(R.id.webview);
        mTvHtml = holder.webview;
        try {
            if(values.getJSONObject(groupPosition).has("webview"))
            mTvHtml.loadData(values.getJSONObject(groupPosition).getString("webview"), "text/html", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* if (isExpanded) {
            holder.icon.setImageResource(R.drawable.up);
        } else {
            holder.icon.setImageResource(R.drawable.down);
        }*/

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolderChild holder;
        if (convertView == null) {
            holder = new ViewHolderChild();
            convertView = inflater.inflate(R.layout.adapter_home_list_item, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        holder.webview = convertView.findViewById(R.id.webview);
        try {
            Log.e("URRRAAAAL", values.getJSONObject(groupPosition).getString("description"));
            Spanned l = Html.fromHtml(values.getJSONObject(groupPosition).getString("description"));
            Html.fromHtml(values.getJSONObject(groupPosition).getString("description"), this, null);
            holder.webview.loadData(values.getJSONObject(groupPosition).getString("description"), "text/html", null); // encoding : "UTF-8"
           /* WebSettings webSettings = holder.webview.getSettings();
            webSettings.setDefaultFontSize(16);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);*/

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.e("LoadImage", "getDrawable");
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            empty = mContext.getResources().getDrawable(R.drawable.ic_launcher, mContext.getTheme());
        } else
            empty = mContext.getResources().getDrawable(R.drawable.ic_launcher);

        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);
        Log.e("LoadImage", "getDrawable LoadImage");
        return d;
    }

    static class ViewHolderParent {
        WebView webview;
        ImageView icon;
    }

    static class ViewHolderChild {
        WebView webview;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.e("LoadImage", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                Log.e("LoadImage", "InputStream ");
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                Log.e("LoadImage", e.getMessage());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.e("LoadImage", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
//            	Log.e("LoadImage",e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("LoadImage", "onPostExecute drawable " + mDrawable);
            Log.e("LoadImage", "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                @SuppressWarnings("deprecation")
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
            }
        }
    }
}