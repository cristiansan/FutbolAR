package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.AnimateFirstDisplayListener;
import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pmovil.soccer.entities.Video;

public class ItemVideo implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemVideo";
    private static LayoutInflater mInflater = null;
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Video video = null;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemVideo() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(20))
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
    }

    public Object getData() {
        return video;
    }

    public void setData(Object data) {
        if (data instanceof Video) {
            video = (Video) data;
        }
    }

    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.video_item_grid, null);
            viewHolder.tvVideoDesc = (TextView) view
                    .findViewById(R.id.tv_desc_video);
            viewHolder.ivThumbnailVideo = (ImageView) view
                    .findViewById(R.id.iv_thumbnail_video);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (video == null)
            return view;
        int width = viewHolder.ivThumbnailVideo.getLayoutParams().width;
        int height = viewHolder.ivThumbnailVideo.getLayoutParams().height;

        String urlEmblem = video.getThumbnail().replace(
                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height);

        // RetainCache.getInstance(context).getmCache()
        // .loadBitmap(viewHolder.ivEmblemAway, urlEmblem);
        imageLoader.displayImage(urlEmblem, viewHolder.ivThumbnailVideo,
                options, animateFirstListener);
        viewHolder.tvVideoDesc.setText(video.getTitle());

        return view;
    }

    private static class ViewHolder {
        private ImageView ivThumbnailVideo;
        private TextView tvVideoDesc;

    }

}
