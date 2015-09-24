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
import com.pmovil.soccer.entities.TimeLine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemTimeLineTwitter implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemPosition";
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static LayoutInflater mInflater = null;
    // private ImageLoadingListener animateFirstListener = new
    // AnimateFirstDisplayListener();
    private final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private DisplayImageOptions options;
    private TimeLine timeLine = null;

    public ItemTimeLineTwitter() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.twitter_photo_time_line)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnFail(R.drawable.twitter_photo_time_line).cacheInMemory()
                .cacheOnDisc().build();
    }


    @Override
    public Object getData() {
        return timeLine;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof TimeLine)
            timeLine = (TimeLine) data;
    }

    @Override
    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.time_line_twitter_row, null);
            viewHolder.tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            viewHolder.tvMsg = (TextView) view.findViewById(R.id.tv_comment_body);
            viewHolder.ivPhoto = (ImageView) view.findViewById(R.id.iv_user_picture);
            viewHolder.progressBarComment = (ProgressBar) view.findViewById(R.id.progressBarComment);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (timeLine == null)
            return view;

        viewHolder.tvUserName.setText("@" + timeLine.getUserUsername());
        viewHolder.tvMsg.setText(timeLine.getBody());

        if (timeLine.getUserPicture() == null ||
                timeLine.getUserPicture().equalsIgnoreCase("null")) {
            viewHolder.progressBarComment
                    .setVisibility(View.INVISIBLE);
            viewHolder.ivPhoto.setImageResource(R.drawable.twitter_photo_time_line);
            return view;
        }

        String urlUserPhoto = timeLine.getUserPicture();
        imageLoader.displayImage(urlUserPhoto, viewHolder.ivPhoto,
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
        private ImageView ivPhoto;
        private ProgressBar progressBarComment;
        private TextView tvUserName;
        private TextView tvMsg;
    }

}
