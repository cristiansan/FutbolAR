package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.ImageUtil;
import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.TopTweet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemMatchTwitter implements Item {

    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static LayoutInflater mInflater = null;
    private final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private TopTweet topTweet = null;
    private DisplayImageOptions options;

    public ItemMatchTwitter() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.twitter_photo_time_line)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnFail(R.drawable.twitter_photo_time_line).cacheInMemory()
                .cacheOnDisc().build();
    }

    @Override
    public Object getData() {
        return topTweet;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof TopTweet) {
            topTweet = (TopTweet) data;
        }
    }

    @Override
    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.match_row, null);

            viewHolder.tvTwitterName = (TextView) view
                    .findViewById(R.id.tv_twitter_name);
            viewHolder.tvTotalMentions = (TextView) view
                    .findViewById(R.id.tv_total_mentions);
            viewHolder.ivTwitter = (ImageView) view
                    .findViewById(R.id.iv_twitter);
            viewHolder.twitterPercentage = view
                    .findViewById(R.id.view_twitter_percentage);
            viewHolder.container_twitter_percentage = (RelativeLayout) view.findViewById(R.id.container_twitter_percentage);

            viewHolder.progressBarPhotoPlayer = (ProgressBar) view.findViewById(R.id.progressBarTopTwitter);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (topTweet == null) {
            return view;
        }
        try {

            int tamMax = (int) context.getResources().getDimension(R.dimen.container_twitter_percentage);
            int mentions = Integer.parseInt(topTweet.getMentions());
            int maxMentions = Integer.parseInt(topTweet.getMentionsMax());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int) ImageUtil.getPixel(context,
                            (int) (tamMax * (mentions / (maxMentions * 1.0)))),
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            viewHolder.twitterPercentage.setLayoutParams(params);

        } catch (Exception e) {

        }
        viewHolder.tvTwitterName.setText(topTweet.getHashtag());
        viewHolder.tvTotalMentions.setText(topTweet.getMentions() + " "
                + context.getString(R.string.mentions));


        if (topTweet.getUrlImage() == null ||
                topTweet.getUrlImage().equalsIgnoreCase("null")) {
            viewHolder.progressBarPhotoPlayer
                    .setVisibility(View.INVISIBLE);
            viewHolder.ivTwitter.setImageResource(R.drawable.twitter_photo_time_line);
            return view;
        }

        int width = viewHolder.ivTwitter.getLayoutParams().width;
        int height = viewHolder.ivTwitter.getLayoutParams().height;
        String urlUserPhoto = topTweet.getUrlImage()
                .replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                        "" + width + "x" + height
                                + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
                );

        imageLoader.displayImage(urlUserPhoto, viewHolder.ivTwitter,
                options, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        viewHolder.progressBarPhotoPlayer
                                .setVisibility(View.VISIBLE);
                        ((ImageView) view).setImageDrawable(null);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        viewHolder.progressBarPhotoPlayer
                                .setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        viewHolder.progressBarPhotoPlayer
                                .setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        viewHolder.progressBarPhotoPlayer
                                .setVisibility(View.INVISIBLE);

                        if (loadedImage != null) {
                            ImageView imageView = (ImageView) view;
                            boolean firstDisplay = !displayedImages
                                    .contains(imageUri);
                            if (firstDisplay) {
                                FadeInBitmapDisplayer.animate(imageView, 500);
                                displayedImages.add(imageUri);
                            }
                        }
                    }
                }
        );


        return view;
    }

    private static class ViewHolder {
        private ImageView ivTwitter;
        private TextView tvTwitterName;
        private View twitterPercentage;
        private TextView tvTotalMentions;
        private RelativeLayout container_twitter_percentage;
        private ProgressBar progressBarPhotoPlayer;
    }

}
