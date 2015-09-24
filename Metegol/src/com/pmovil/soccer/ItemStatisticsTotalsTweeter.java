package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.components.Item;
import com.pmovil.soccer.entities.TotalesTweets;

public class ItemStatisticsTotalsTweeter implements Item {

    private static LayoutInflater mInflater;
    private TotalesTweets totalesTweets;

    @Override
    public Object getData() {
        return totalesTweets;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof TotalesTweets) {
            totalesTweets = (TotalesTweets) data;
        }
    }

    @Override
    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(context);
            }
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.statistics_total_tweeter, null);
            viewHolder.tvReTweetAway = (TextView) view
                    .findViewById(R.id.tv_retweets_away);
            viewHolder.tvReTweetLocal = (TextView) view
                    .findViewById(R.id.tv_retweets_local);
            viewHolder.tvTweetAway = (TextView) view
                    .findViewById(R.id.tv_tweets_away);
            viewHolder.tvTweetLocal = (TextView) view
                    .findViewById(R.id.tv_tweets_local);
            viewHolder.tvTotalAway = (TextView) view
                    .findViewById(R.id.tv_totals_away);
            viewHolder.tvTotalLocal = (TextView) view
                    .findViewById(R.id.tv_totals_local);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTweetLocal.setText(totalesTweets.getTweets().getLocal());
        viewHolder.tvTweetAway.setText(totalesTweets.getTweets().getVisitor());
        viewHolder.tvReTweetLocal.setText(totalesTweets.getRetweets()
                .getLocal());
        viewHolder.tvReTweetAway.setText(totalesTweets.getRetweets()
                .getVisitor());
        viewHolder.tvTotalLocal.setText(totalesTweets.getTotal().getLocal());
        viewHolder.tvTotalAway.setText(totalesTweets.getTotal().getVisitor());
        return view;
    }

    private class ViewHolder {
        private TextView tvTweetLocal;
        private TextView tvTweetAway;
        private TextView tvReTweetLocal;
        private TextView tvReTweetAway;
        private TextView tvTotalLocal;
        private TextView tvTotalAway;
    }

}
