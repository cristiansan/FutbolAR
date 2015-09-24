package com.pmovil.soccer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.components.Item;
import com.components.TextViewPlus;

public class ItemHeader implements Item {

    private String headerTitle = new String();

    public Object getData() {
        return headerTitle;
    }

    public void setData(Object data) {
        if (data instanceof String)
            headerTitle = (String) data;
    }

    @SuppressLint("DefaultLocale")
    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            LayoutInflater mInflater = LayoutInflater.from(context);

            view = mInflater.inflate(R.layout.header_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextViewPlus) view
                    .findViewById(R.id.header_title);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(headerTitle.toUpperCase());

        return view;
    }

    private static class ViewHolder {
        private TextViewPlus textView;
    }

}
