package com.pmovil.soccer;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.components.ImageUtil;
import com.components.Item;
import com.pmovil.soccer.entities.TopTweet;

public class ItemMatchRowTwitter implements Item {

    private TopTweet topTweets[];

    @Override
    public Object getData() {
        return topTweets;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof TopTweet[]) {
            topTweets = (TopTweet[]) data;
        }

    }

    @Override
    public View getView(View view, Context context) {
        LinearLayout container = null;
        ViewHolder viewHolder = null;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins((int) ImageUtil.getPixel(context, 5), 0,
                (int) ImageUtil.getPixel(context, 5),
                (int) ImageUtil.getPixel(context, 3));
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            container = new LinearLayout(context);
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setWeightSum(2);
            viewHolder.itemMatchTwitterLeft = new ItemMatchTwitter();
            viewHolder.itemMatchTwitterRight = new ItemMatchTwitter();
            view = container;
            view.setTag(viewHolder);

        } else {
            container = (LinearLayout) view;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (topTweets.length > 0) {
            viewHolder.itemMatchTwitterLeft.setData(topTweets[0]);
        }
        if (topTweets.length > 1) {
            viewHolder.itemMatchTwitterRight.setData(topTweets[1]);
        }

        if (container.getChildAt(0) == null) {
            View viewItemLeft = viewHolder.itemMatchTwitterLeft.getView(null,
                    context);

            viewItemLeft.setLayoutParams(params);
            container.addView(viewItemLeft);
        } else {
            viewHolder.itemMatchTwitterLeft.getView(container.getChildAt(0),
                    context);
        }
        if (container.getChildAt(1) == null) {
            View viewItemLeft = viewHolder.itemMatchTwitterRight.getView(null,
                    context);
            viewItemLeft.setLayoutParams(params);
            container.addView(viewItemLeft);
        } else {
            viewHolder.itemMatchTwitterRight.getView(container.getChildAt(1),
                    context);
        }

        // View viewItemRight = viewHolder.itemMatchTwitterRight.getView(
        // container.getChildAt(1), context);
        // viewItemRight.setLayoutParams(params);
        // container.removeAllViews();
        // container.addView(viewItemLeft, 0);
        // container.addView(viewItemRight, 1);
        return view;
    }

    private static class ViewHolder {
        private ItemMatchTwitter itemMatchTwitterLeft;
        private ItemMatchTwitter itemMatchTwitterRight;

    }

}
