package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.components.Item;

public class ItemUpcomingHeader implements Item {

    private String headerTitle = new String();

    public Object getData() {
        return headerTitle;
    }

    public void setData(Object data) {
        if (data instanceof String)
            headerTitle = (String) data;
    }

    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.upcoming_header, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view
                    .findViewById(R.id.week_header_upcoming_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (headerTitle != null) {
            viewHolder.textView.setText(headerTitle.toUpperCase());
        }
        return view;
    }

    private static class ViewHolder {
        private TextView textView;
    }

}
