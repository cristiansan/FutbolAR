package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.AnimateFirstDisplayListener;
import com.components.ImageUtil;
import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pmovil.soccer.entities.News;

public class ItemNews implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemNews";
    private static LayoutInflater mInflater = null;
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private News news = null;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ItemNews() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageForEmptyUri(R.drawable.logo_3)
                    .showImageOnFail(R.drawable.logo_3).cacheInMemory()
                    .cacheOnDisc().build();
    }

    public Object getData() {
        return news;
    }

    public void setData(Object data) {
        if (data instanceof News) {
            news = (News) data;
        }
    }

    public View getView(View view, Context context) {
        if (context == null)
            return null;
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.news_row, null);
            viewHolder.ivThumbnailNews = (ImageView) view
                    .findViewById(R.id.iv_news);
            viewHolder.tvTitleNews = (TextView) view
                    .findViewById(R.id.tv_title_news);
            viewHolder.tvDescriptionNews = (TextView) view
                    .findViewById(R.id.tv_desc_news);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (news == null)
            return view;
        int width = viewHolder.ivThumbnailNews.getLayoutParams().width;
        int height = (int) (viewHolder.ivThumbnailNews.getLayoutParams().height - ImageUtil
                .getPixel(context, 3));

        String urlEmblem = news.getImage().replace(
                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);

        // RetainCache.getInstance(context).getmCache()
        // .loadBitmap(viewHolder.ivEmblemAway, urlEmblem);
        imageLoader.displayImage(urlEmblem, viewHolder.ivThumbnailNews,
                options, animateFirstListener);
        viewHolder.tvTitleNews.setText(news.getTitle());
        if (news.getDescription() != null && !news.getDescription().equalsIgnoreCase("")) {
            viewHolder.tvDescriptionNews.setText(news.getDescription());
        } else {
            viewHolder.tvDescriptionNews.setText("");
        }

        return view;
    }

    private static class ViewHolder {
        private ImageView ivThumbnailNews;
        private TextView tvTitleNews;
        private TextView tvDescriptionNews;

    }
}
