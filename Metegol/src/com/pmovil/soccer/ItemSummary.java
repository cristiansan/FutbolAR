package com.pmovil.soccer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.components.Item;
import com.components.TextViewPlus;
import com.pmovil.soccer.entities.Statistic;

public class ItemSummary implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemSummary";
    private static LayoutInflater mInflater = null;
    private Statistic dataSummary = null;

    public Object getData() {
        return dataSummary;
    }

    public void setData(Object data) {
        if (data instanceof Statistic)
            dataSummary = (Statistic) data;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.summary_row, null);

            viewHolder.tvValueAway = (TextView) view.findViewById(R.id.tv_value_away);
            viewHolder.tvValueHome = (TextView) view.findViewById(R.id.tv_value_home);
            viewHolder.tvImgAway = (ImageView) view.findViewById(R.id.tv_img_away);
            viewHolder.tvImgHome = (ImageView) view.findViewById(R.id.tv_img_home);
            viewHolder.iv_time = (ImageView) view.findViewById(R.id.imgTime);
            viewHolder.tvTime = (TextViewPlus) view.findViewById(R.id.tv_time_row);
            viewHolder.ltHome = (LinearLayout) view.findViewById(R.id.layout_values_home);
            viewHolder.ltAway = (LinearLayout) view.findViewById(R.id.layout_values_away);
            viewHolder.ltMain = (LinearLayout) view.findViewById(R.id.layout_principal);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (dataSummary == null)
            return view;

        if (dataSummary.getIsSummary()) {

            if(dataSummary.getValueAway().equals("") && dataSummary.getValueHome().equals("")){
                viewHolder.ltMain.setBackgroundColor(context.getResources().getColor(R.color.color_title_menu));
                viewHolder.ltMain.setPadding(0,0,3,3);
                viewHolder.tvTime.setVisibility(View.VISIBLE);
                viewHolder.iv_time.setVisibility(View.GONE);
                viewHolder.ltAway.setVisibility(View.GONE);
                viewHolder.ltHome.setVisibility(View.GONE);
                viewHolder.tvTime.setText(dataSummary.getValueTitle());

            }else{

                if(!dataSummary.getValueAway().equals("")){

                    viewHolder.tvValueAway.setText(dataSummary.getValueAway());
                    viewHolder.tvTime.setText(dataSummary.getMinutes()+"'");
                    viewHolder.tvValueAway.setTextColor(context.getResources().getColor(R.color.gray_black_font));
                    viewHolder.ltAway.setVisibility(View.VISIBLE);
                    //viewHolder.tvValueAway.setBackgroundColor(context.getResources().getColor(R.color.gray_summary_row));
                    //viewHolder.tvImgAway.setBackgroundColor(context.getResources().getColor(R.color.gray_summary_row));

                }else if(dataSummary.getValueHome().equals("")){

                    viewHolder.tvValueAway.setVisibility(View.GONE);
                    viewHolder.tvTime.setVisibility(View.GONE);
                    viewHolder.iv_time.setVisibility(View.GONE);
                }

                if(!dataSummary.getValueHome().equals("")){

                    viewHolder.tvValueHome.setText(dataSummary.getValueHome());
                    viewHolder.tvTime.setText(dataSummary.getMinutes()+"'");
                    viewHolder.tvValueHome.setTextColor(context.getResources().getColor(R.color.gray_black_font));
                    viewHolder.ltHome.setVisibility(View.VISIBLE);
                    //viewHolder.tvValueHome.setBackgroundColor(context.getResources().getColor(R.color.gray_summary_row));
                    //viewHolder.tvImgHome.setBackgroundColor(context.getResources().getColor(R.color.gray_summary_row));

                }else if(dataSummary.getValueAway().equals(""))
                {
                    viewHolder.tvValueHome.setVisibility(View.GONE);
                    viewHolder.tvTime.setVisibility(View.GONE);
                    viewHolder.iv_time.setVisibility(View.GONE);

                }

                if (dataSummary.getSummaryImage() > -1) {
                    if (!dataSummary.getValueHome().equals("")) {
                        viewHolder.tvImgHome.setVisibility(View.VISIBLE);
                        viewHolder.tvImgHome.setImageResource(dataSummary.getSummaryImage());
                        viewHolder.tvTime.setText(dataSummary.getMinutes()+"'");
                    }
                    if (!dataSummary.getValueAway().equals("")) {
                        viewHolder.tvImgAway.setVisibility(View.VISIBLE);
                        viewHolder.tvImgAway.setImageResource(dataSummary.getSummaryImage());
                        viewHolder.tvTime.setText(dataSummary.getMinutes()+"'");
                    }
                }
            }

        }

        return view;
    }

    private static class ViewHolder {
        private TextView tvValueHome;
        private TextView tvValueAway;
        private ImageView tvImgHome;
        private ImageView tvImgAway;
        private ImageView iv_time;
        private TextViewPlus tvTime;
        private LinearLayout ltHome;
        private LinearLayout ltAway;
        private LinearLayout ltMain;


    }

}
