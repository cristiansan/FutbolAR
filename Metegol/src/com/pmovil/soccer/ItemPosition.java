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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.Team;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemPosition implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemPosition";
    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static LayoutInflater mInflater = null;
    // private ImageLoadingListener animateFirstListener = new
    // AnimateFirstDisplayListener();
    private final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private DisplayImageOptions options;
    private Team team = null;

    public ItemPosition() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                .cacheOnDisc().build();
    }

    public Object getData() {
        return team;
    }

    public void setData(Object data) {
        if (data instanceof Team)
            team = (Team) data;
    }

    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.position_row, null);
            viewHolder.ivEmblem = (ImageView) view
                    .findViewById(R.id.iv_emblem_team);
            viewHolder.tvTeam = (TextView) view
                    .findViewById(R.id.tv_tableColumnTEAM);
            viewHolder.tvPos = (TextView) view
                    .findViewById(R.id.tv_tableColumnPOS);
            viewHolder.tvPts = (TextView) view
                    .findViewById(R.id.tv_tableColumnPTS);
            viewHolder.tvPj = (TextView) view
                    .findViewById(R.id.tv_tableColumnMP);
            viewHolder.tvPg = (TextView) view
                    .findViewById(R.id.tv_tableColumnMW);
            viewHolder.tvPe = (TextView) view
                    .findViewById(R.id.tv_tableColumnMD);
            viewHolder.tvPp = (TextView) view
                    .findViewById(R.id.tv_tableColumnML);
            viewHolder.tvGf = (TextView) view
                    .findViewById(R.id.tv_tableColumnG);
            viewHolder.tvGc = (TextView) view
                    .findViewById(R.id.tv_tableColumnGA);
            viewHolder.background = view
                    .findViewById(R.id.container_position_rows);
            viewHolder.loding = view.findViewById(R.id.progressBarPosition);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (team == null)
            return view;
        if (team.getPosition() % 2 == 0)
            viewHolder.background.setBackgroundResource(R.color.gray_position);

        else {
            viewHolder.background
                    .setBackgroundResource(R.color.gray_not_pair_team);

        }

        viewHolder.tvTeam.setText(team.getAbbreviation());
        viewHolder.tvPts.setText("" + team.getPoints());
        viewHolder.tvGc.setText("" + team.getGoalsAgainst());
        viewHolder.tvGf.setText("" + team.getGoalsScored());
        viewHolder.tvPg.setText("" + team.getWins());
        viewHolder.tvPe.setText("" + team.getTied());
        viewHolder.tvPj.setText("" + team.getPlayed());
        viewHolder.tvPp.setText("" + team.getLost());
        viewHolder.tvPos.setText("" + team.getPosition());
        int width = viewHolder.ivEmblem.getLayoutParams().width;
        int height = viewHolder.ivEmblem.getLayoutParams().height;
        String urlEmblem = team.getEmblem().replace(
                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height);
        imageLoader.displayImage(urlEmblem, viewHolder.ivEmblem, options,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        viewHolder.loding.setVisibility(View.VISIBLE);
                        ((ImageView) view).setImageDrawable(null);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        viewHolder.loding.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        viewHolder.loding.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        viewHolder.loding.setVisibility(View.INVISIBLE);

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
        private View background;
        private TextView tvPos;
        private ImageView ivEmblem;
        private TextView tvTeam;
        private TextView tvPts;
        private TextView tvPj;
        private TextView tvPg;
        private TextView tvPe;
        private TextView tvPp;
        private TextView tvGf;
        private TextView tvGc;
        private View loding;
    }

}
