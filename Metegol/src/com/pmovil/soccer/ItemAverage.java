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
import com.pmovil.soccer.entities.Team;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemAverage implements Item {

    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static LayoutInflater mInflater = null;
    private static DisplayImageOptions options;
    private static Double minPoints;
    private final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private Team team;
    private int totalPosition = 0;

    public ItemAverage() {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        }
    }

    public static Double getMinAverage() {
        return minPoints;
    }

    public static void setMinAverage(Double min) {
        minPoints = min;
    }

    public Object getData() {
        return team;
    }

    public void setData(Object data) {
        if (data instanceof Team) {
            team = (Team) data;

        }
    }

    public View getView(View view, Context context) {

        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(context);
            }
            view = mInflater.inflate(R.layout.average_row, null);
            viewHolder = new ViewHolder();
            viewHolder.emblemTeamImageView = (ImageView) view
                    .findViewById(R.id.iv_emblem_team);
            viewHolder.progressBarAverage = (ProgressBar) view
                    .findViewById(R.id.progressBarAverage);
            viewHolder.tableColumnTeamTextView = (TextView) view
                    .findViewById(R.id.tv_tableColumnTEAM);
            viewHolder.tableColumn1TextView = (TextView) view
                    .findViewById(R.id.tv_tableColumn_1);
            viewHolder.tableColumn2TextView = (TextView) view
                    .findViewById(R.id.tv_tableColumn_2);
            viewHolder.tableColumn3TextView = (TextView) view
                    .findViewById(R.id.tv_tableColumn_3);
            viewHolder.tableColumnMPTextView = (TextView) view
                    .findViewById(R.id.tv_tableColumnMP);
            viewHolder.tableColumnMLTextView = (TextView) view
                    .findViewById(R.id.tv_tableColumnML);
            viewHolder.tableColumnPROTextView = (TextView) view
                    .findViewById(R.id.tv_tableColumnPRO);

            viewHolder.background = view
                    .findViewById(R.id.container_average_rows);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (team == null) {
            return view;
        }

        if (team.getPosition() % 2 == 0)
            viewHolder.background.setBackgroundResource(R.color.gray_position);

        else {
            viewHolder.background
                    .setBackgroundResource(R.color.gray_not_pair_team);

        }

        if (team.getAverageRelegation() <= minPoints) {
            viewHolder.tableColumnPROTextView
                    .setBackgroundResource(R.drawable.rounded_pts_position);
            viewHolder.tableColumnPROTextView.setTextColor(context.getResources().getColor(
                    android.R.color.white));
        } else {
            viewHolder.tableColumnPROTextView.setTextColor(context.getResources().getColor(
                    R.color.blue_not_selected));
            viewHolder.tableColumnPROTextView.setBackgroundResource(android.R.color.transparent);
        }

//		if (team.getPosition() > totalPosition - 3) {
//			viewHolder.tableColumnPROTextView
//					.setBackgroundResource(R.drawable.rounded_average);
//			viewHolder.tableColumnPROTextView.setTextColor(context
//					.getResources().getColor(android.R.color.white));
//		} else {
//			viewHolder.tableColumnPROTextView.setTextColor(context
//					.getResources().getColor(R.color.blue_not_selected));
//
//			if (team.getPosition() % 2 == 0)
//				viewHolder.tableColumnPROTextView
//						.setBackgroundResource(R.color.gray_position);
//
//			else {
//				viewHolder.tableColumnPROTextView
//						.setBackgroundResource(R.color.gray_not_pair_team);
//
//			}
//		}

        viewHolder.tableColumnTeamTextView.setText(team.getAbbreviation());
        viewHolder.tableColumn1TextView.setText("" + team.getPointsPrevious2());
        viewHolder.tableColumn2TextView.setText("" + team.getPointsPrevious1());
        viewHolder.tableColumn3TextView.setText("" + team.getPointsCurrent());
        viewHolder.tableColumnMLTextView.setText(""
                + team.getPointsRelegation());
        viewHolder.tableColumnMPTextView.setText(""
                + team.getPlayedRelegation());
        viewHolder.tableColumnPROTextView.setText(""
                + team.getAverageRelegation());
        int width = viewHolder.emblemTeamImageView.getLayoutParams().width;
        int height = viewHolder.emblemTeamImageView.getLayoutParams().height;
        String urlEmblem = team.getEmblem().replace(
                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height);
        imageLoader.displayImage(urlEmblem, viewHolder.emblemTeamImageView,
                options, new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        viewHolder.progressBarAverage
                                .setVisibility(View.VISIBLE);
                        ((ImageView) view).setImageDrawable(null);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                        viewHolder.progressBarAverage
                                .setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        viewHolder.progressBarAverage
                                .setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        viewHolder.progressBarAverage
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

    public int getTotalPosition() {
        return totalPosition;
    }

    public void setTotalPosition(int totalPosition) {
        this.totalPosition = totalPosition;
    }

    private static class ViewHolder {
        private View background;
        private ProgressBar progressBarAverage;
        private ImageView emblemTeamImageView;
        private TextView tableColumnTeamTextView;
        private TextView tableColumn1TextView;
        private TextView tableColumn2TextView;
        private TextView tableColumn3TextView;
        private TextView tableColumnMLTextView;
        private TextView tableColumnMPTextView;
        private TextView tableColumnPROTextView;

    }

}
