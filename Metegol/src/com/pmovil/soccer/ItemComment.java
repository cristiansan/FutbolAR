package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.Comment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemComment implements Item {

    private static final String TAG = "ItemComment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static LayoutInflater mInflater = null;
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Comment comment = null;

    public ItemComment() {
        if (options == null)
            options = new DisplayImageOptions.Builder().cacheInMemory()
                    .cacheOnDisc().build();
    }

    public Object getData() {
        return comment;
    }

    public void setData(Object data) {
        if (data instanceof Comment)
            comment = (Comment) data;
    }

    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.comment_row, null);
            viewHolder.ivUserPicture = (ImageView) view
                    .findViewById(R.id.iv_user_picture);
            viewHolder.tvCommentBody = (TextView) view
                    .findViewById(R.id.tv_comment_body);
            /*viewHolder.tvUserName = (TextView) view
                    .findViewById(R.id.tv_user_name);*/
            viewHolder.loading = view.findViewById(R.id.progressBarComment);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!comment.getUser_photo_url().equalsIgnoreCase("")
                && !comment.getUser_photo_url().equalsIgnoreCase("null")) {
            imageLoader.displayImage(comment.getUser_photo_url(),
                    viewHolder.ivUserPicture, options,
                    new AnimateFirstDisplayListener(viewHolder.loading));
            viewHolder.ivUserPicture.setVisibility(View.VISIBLE);
        } else
            viewHolder.ivUserPicture.setVisibility(View.INVISIBLE);

        viewHolder.tvCommentBody.setText(comment.getComment());
        //viewHolder.tvUserName.setText(comment.getUserName());
        return view;

    }

    private static class ViewHolder {
        ImageView ivUserPicture;
        //TextView tvUserName;
        TextView tvCommentBody;
        View loading;
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {
        private View loading;

        public AnimateFirstDisplayListener(View loading) {
            this.loading = loading;
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            super.onLoadingCancelled(imageUri, view);
            loading.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            super.onLoadingFailed(imageUri, view, failReason);
            loading.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            super.onLoadingStarted(imageUri, view);
            ((ImageView) view).setImageDrawable(null);
            loading.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            loading.setVisibility(View.INVISIBLE);

            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }

    }

}
