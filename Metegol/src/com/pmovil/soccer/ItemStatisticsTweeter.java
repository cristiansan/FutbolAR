package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.ImageUtil;
import com.components.Item;
import com.pmovil.soccer.entities.TopMentionsTwitter;

public class ItemStatisticsTweeter implements Item {

    private static LayoutInflater mInflater = null;
    private TopMentionsTwitter mentionsTwitter;

    @Override
    public Object getData() {
        return mentionsTwitter;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof TopMentionsTwitter) {
            mentionsTwitter = (TopMentionsTwitter) data;
        }
    }

    @Override
    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.statistics_tweeter_row, null);

            viewHolder.tvTwitterName = (TextView) view
                    .findViewById(R.id.tv_twitter_name);
            viewHolder.tvTotalMentions = (TextView) view
                    .findViewById(R.id.tv_total_mentions);
            viewHolder.ivTwitter = (ImageView) view
                    .findViewById(R.id.iv_twitter);
            viewHolder.twitterPercentage = view
                    .findViewById(R.id.view_twitter_percentage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (mentionsTwitter == null) {
            return view;
        }
        try {
//			
            int tamMax = (int) context.getResources().getDimension(R.dimen.container_twitter_percentage_statistics);
            int mentions = Integer.parseInt(mentionsTwitter.getMentions());
            int maxMentions = Integer
                    .parseInt(mentionsTwitter.getMentionsMax());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int) ImageUtil.getPixel(context,
                            (int) (tamMax * (mentions / (maxMentions * 1.0)))),
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            viewHolder.twitterPercentage.setLayoutParams(params);

        } catch (Exception e) {

        }
        viewHolder.tvTwitterName.setText(mentionsTwitter.getHashtag());
        viewHolder.tvTotalMentions.setText(mentionsTwitter.getMentions() + " "
                + context.getString(R.string.mentions));

        return view;
    }

    private static class ViewHolder {
        private ImageView ivTwitter;
        private TextView tvTwitterName;
        private View twitterPercentage;
        private TextView tvTotalMentions;
    }

}
