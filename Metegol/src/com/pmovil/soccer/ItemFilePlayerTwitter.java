package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.PlayerTweet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemFilePlayerTwitter implements Item {

    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static LayoutInflater mInflater = null;
    private final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private DisplayImageOptions options;
    private PlayerTweet playerTweet;

    public ItemFilePlayerTwitter() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.twitter_photo_time_line)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnFail(R.drawable.twitter_photo_time_line).cacheInMemory()
                .cacheOnDisc().build();
    }


    @Override
    public Object getData() {
        return playerTweet;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof PlayerTweet) {
            playerTweet = (PlayerTweet) data;
        }

    }

    @Override
    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.file_player_twitter_row, null);
            viewHolder.tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            viewHolder.tvMsg = (TextView) view.findViewById(R.id.tv_comment_body);
            viewHolder.ivUserPicture = (ImageView) view.findViewById(R.id.iv_user_picture);
            viewHolder.progressBarComment = (ProgressBar) view.findViewById(R.id.progressBarComment);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (playerTweet == null)
            return view;

        viewHolder.tvUserName.setText("@" + playerTweet.getUsername());
        viewHolder.tvMsg.setText(playerTweet.getBody());


        if ((playerTweet.getPictureUrl() == null ||
                playerTweet.getPictureUrl().equalsIgnoreCase("null"))) {
            viewHolder.progressBarComment
                    .setVisibility(View.INVISIBLE);
            viewHolder.ivUserPicture.setImageResource(R.drawable.twitter_photo_time_line);
            return view;
        }

        String urlUserPhoto = playerTweet.getPictureUrl();
        imageLoader.displayImage(urlUserPhoto, viewHolder.ivUserPicture,
                options, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        viewHolder.progressBarComment
                                .setVisibility(View.VISIBLE);
                        ((ImageView) view).setImageDrawable(null);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        viewHolder.progressBarComment
                                .setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        viewHolder.progressBarComment
                                .setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        viewHolder.progressBarComment
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
        private TextView tvUserName;
        private TextView tvMsg;
        private ImageView ivUserPicture;
        private ProgressBar progressBarComment;
    }

}
