package com.pmovil.soccer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.components.Item;
import com.pmovil.soccer.entities.Statistic;

public class ItemStatistics implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemStatistics";
    private static LayoutInflater mInflater = null;
    private Statistic dataStatistics = null;
    private int value_int;
    private int value_int2;

    public Object getData() {
        return dataStatistics;
    }

    public void setData(Object data) {
        if (data instanceof Statistic)
            dataStatistics = (Statistic) data;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.statistics_row, null);
            viewHolder.tvValueAway = (TextView) view
                    .findViewById(R.id.tv_value_away);
            viewHolder.tvValueHome = (TextView) view
                    .findViewById(R.id.tv_value_home);
            viewHolder.tvValueTitle = (TextView) view
                    .findViewById(R.id.tv_value_title);
            viewHolder.tvImgAway = (ImageView) view.findViewById(R.id.tv_img_away);
            viewHolder.tvImgHome = (ImageView) view.findViewById(R.id.tv_img_home);
            viewHolder.seekbar_statistic_der = (SeekBar) view.findViewById(R.id.seekbar_statistics_der);
            viewHolder.seekbar_statistic_izq = (SeekBar) view.findViewById(R.id.seekbar_statistics_izq);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (dataStatistics == null)
            return view;

        if (dataStatistics.getIsSummary()) {
            viewHolder.seekbar_statistic_der.setVisibility(View.GONE);
            viewHolder.seekbar_statistic_izq.setVisibility(View.GONE);
            viewHolder.tvValueAway.setText(dataStatistics.getValueAway());
            viewHolder.tvValueAway.setTextSize(14);
            viewHolder.tvValueAway.setTextColor(context.getResources().getColor(R.color.gray_black_font));
            viewHolder.tvValueHome.setText(dataStatistics.getValueHome());
            viewHolder.tvValueHome.setTextSize(14);
            viewHolder.tvValueHome.setTextColor(context.getResources().getColor(R.color.gray_black_font));
            viewHolder.tvValueTitle.setText(dataStatistics.getValueTitle());
            if (dataStatistics.getSummaryImage() > -1) {
                if (!dataStatistics.getValueHome().equals("")) {
                    viewHolder.tvImgHome.setVisibility(View.VISIBLE);
                    viewHolder.tvImgHome.setImageResource(dataStatistics.getSummaryImage());
                }
                if (!dataStatistics.getValueAway().equals("")) {
                    viewHolder.tvImgAway.setVisibility(View.VISIBLE);
                    viewHolder.tvImgAway.setImageResource(dataStatistics.getSummaryImage());
                }
            }
        } else {

            String value = dataStatistics.getValueAway().equalsIgnoreCase("-1") ? "0" : dataStatistics.getValueAway();
            viewHolder.tvValueAway.setText(value);
            value_int = Integer.parseInt(value);

            value = dataStatistics.getValueHome().equalsIgnoreCase("-1") ? "0" : dataStatistics.getValueHome();
            viewHolder.tvValueHome.setText(value);
            value_int2 = Integer.parseInt(value);

            viewHolder.tvValueTitle.setText(dataStatistics.getValueTitle().toUpperCase());

            ColorDrawable cd = new ColorDrawable(0xffff6666);

            int totalValue=(value_int+value_int2);

            viewHolder.seekbar_statistic_der.setMax(totalValue);
            viewHolder.seekbar_statistic_der.setEnabled(false);
            viewHolder.seekbar_statistic_der.setThumb(cd);

            viewHolder.seekbar_statistic_izq.setMax(totalValue);
            viewHolder.seekbar_statistic_izq.setEnabled(false);
            viewHolder.seekbar_statistic_izq.setThumb(cd);
            viewHolder.seekbar_statistic_izq.setRotation(180);

            viewHolder.seekbar_statistic_der.setProgress(value_int);
            viewHolder.seekbar_statistic_izq.setProgress(value_int2);
        }

        return view;
    }

    private static class ViewHolder {
        private TextView tvValueHome;
        private TextView tvValueAway;
        private TextView tvValueTitle;
        private ImageView tvImgHome;
        private ImageView tvImgAway;
        private SeekBar seekbar_statistic_der;
        private SeekBar seekbar_statistic_izq;

    }

}
