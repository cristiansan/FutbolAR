package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.components.Item;

public class ItemStatisticsHeaderTweeter implements Item {

    private static LayoutInflater mInflater;

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public void setData(Object data) {
        // TODO Auto-generated method stub

    }

    @Override
    public View getView(View view, Context context) {

        if (mInflater == null) {
            mInflater = LayoutInflater.from(context);

        }

        return mInflater.inflate(R.layout.statistic_tweeter_header, null);
    }

}
