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
import com.pmovil.soccer.entities.Game;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ItemFixtureGame implements Item {

    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static LayoutInflater mInflater = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("ccc d MMM",
            Locale.getDefault());
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private Game game = null;

    public ItemFixtureGame() {
        // imageLoader.isInited();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                .cacheOnDisc().build();
    }

    public Object getData() {
        return game;
    }

    public void setData(Object data) {
        if (data instanceof Game)
            game = (Game) data;
    }

    public View getView(View view, Context context) {
        final ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.fixture_row, null);
            viewHolder.tvAcronymAway = (TextView) view
                    .findViewById(R.id.tv_acronym_away);
            viewHolder.tvAcronymHome = (TextView) view
                    .findViewById(R.id.tv_acronym_home);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.tvGoalsAway = (TextView) view
                    .findViewById(R.id.tv_goals_away);
            viewHolder.tvGoalsHome = (TextView) view
                    .findViewById(R.id.tv_goals_home);
            viewHolder.tvTimeStart = (TextView) view
                    .findViewById(R.id.tv_time_start);
            viewHolder.ivEmblemAway = (ImageView) view
                    .findViewById(R.id.iv_emblem_away);
            viewHolder.ivTime = (ImageView) view.findViewById(R.id.iv_Time);
            viewHolder.ivEmblemHome = (ImageView) view
                    .findViewById(R.id.iv_emblem_home);
            viewHolder.loadingHome = view
                    .findViewById(R.id.progressBarEmblemHome);
            viewHolder.loadingAway = view
                    .findViewById(R.id.progressBarEmblemAway);
            viewHolder.containerStateMatch = view
                    .findViewById(R.id.container_state_match);
            viewHolder.containerStateMatch_2 = view
                    .findViewById(R.id.container_game_data);
            viewHolder.tvStatus = (TextView) view.findViewById(R.id.tv_state);
            viewHolder.tvStatus_2 = (TextView) view
                    .findViewById(R.id.tv_state_2);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
            //Log.i("Item", "Reused");
        }
        if (game == null)
            return view;
        try {
            //TODO item del listview del fixture.
            viewHolder.tvAcronymAway.setText(game.getAwayTeam()
                    .getAbbreviation().toUpperCase());
            viewHolder.tvAcronymHome.setText(game.getHomeTeam()
                    .getAbbreviation().toUpperCase());

            setStateMatch(game.getState().getId(),
                    viewHolder.containerStateMatch, viewHolder.tvStatus,
                    context, viewHolder.containerStateMatch_2,
                    viewHolder.tvStatus_2);

            String dateString = Integer.toString(game.getDate());

            try {
                @SuppressWarnings("deprecation")
                Date date = new Date((int) ((game.getDate() / 10000) - 1900),
                        Integer.parseInt(dateString.substring(4, 6)) - 1,
                        Integer.parseInt(dateString.substring(6)));
                viewHolder.tvDate.setText(sdf.format(date).toUpperCase());
            } catch (Exception e) {
                viewHolder.tvDate.setText("" + game.getDate());

            }
            if (game.getGoalAway() != -1)
                viewHolder.tvGoalsAway.setText("" + game.getGoalAway());
            else {
                viewHolder.tvGoalsAway.setText("");
            }
            if (game.getGoalAway() != -1)
                viewHolder.tvGoalsHome.setText("" + game.getGoalHome());
            else {
                viewHolder.tvGoalsHome.setText("");
            }
            String time = game.getTime();
            if (time.equalsIgnoreCase("")) {
                viewHolder.ivTime.setVisibility(View.INVISIBLE);
            } else if (time.length() >= 8) {
                viewHolder.ivTime.setVisibility(View.VISIBLE);

                time = time.substring(0, 5);
            }
            viewHolder.tvTimeStart.setText("" + time);
            int width = viewHolder.ivEmblemAway.getLayoutParams().width;
            int height = viewHolder.ivEmblemAway.getLayoutParams().height;
            String urlEmblem = game
                    .getAwayTeam()
                    .getEmblem()
                    .replace(
                            com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                            "" + width + "x" + height
                                    + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
                    );

            // RetainCache.getInstance(context).getmCache()
            // .loadBitmap(viewHolder.ivEmblemAway, urlEmblem);
            imageLoader.displayImage(urlEmblem, viewHolder.ivEmblemAway,
                    options, new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            super.onLoadingStarted(imageUri, view);
                            viewHolder.loadingAway.setVisibility(View.VISIBLE);
                            ((ImageView) view).setImageDrawable(null);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                                       View view) {
                            super.onLoadingCancelled(imageUri, view);
                            viewHolder.loadingAway
                                    .setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            super.onLoadingFailed(imageUri, view, failReason);
                            viewHolder.loadingAway
                                    .setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            viewHolder.loadingAway
                                    .setVisibility(View.INVISIBLE);
                            if (loadedImage != null) {
                                ImageView imageView = (ImageView) view;
                                boolean firstDisplay = !displayedImages
                                        .contains(imageUri);
                                if (firstDisplay) {
                                    FadeInBitmapDisplayer.animate(imageView,
                                            500);
                                    displayedImages.add(imageUri);
                                }
                            }
                        }
                    }
            );

            width = viewHolder.ivEmblemHome.getLayoutParams().width;
            height = viewHolder.ivEmblemHome.getLayoutParams().height;

            urlEmblem = game
                    .getHomeTeam()
                    .getEmblem()
                    .replace(
                            com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                            "" + width + "x" + height
                                    + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
                    );
            imageLoader.displayImage(urlEmblem, viewHolder.ivEmblemHome,
                    options, new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            super.onLoadingStarted(imageUri, view);
                            viewHolder.loadingHome.setVisibility(View.VISIBLE);
                            ((ImageView) view).setImageDrawable(null);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                                       View view) {
                            super.onLoadingCancelled(imageUri, view);
                            viewHolder.loadingHome
                                    .setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            super.onLoadingFailed(imageUri, view, failReason);
                            viewHolder.loadingHome
                                    .setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            viewHolder.loadingHome
                                    .setVisibility(View.INVISIBLE);
                            if (loadedImage != null) {
                                ImageView imageView = (ImageView) view;
                                boolean firstDisplay = !displayedImages
                                        .contains(imageUri);
                                if (firstDisplay) {
                                    FadeInBitmapDisplayer.animate(imageView,
                                            500);
                                    displayedImages.add(imageUri);
                                }
                            }
                        }
                    }
            );
            // RetainCache.getInstance(context).getmCache()
            // .loadBitmap(viewHolder.ivEmblemHome, urlEmblem);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("error", "ERROR ");
        }
        return view;
    }

    private void setStateMatch(int statusId, View container,
                               TextView statusTextView, Context context, View container_2,
                               TextView statusTextView_2) {
        container.setBackgroundColor(context.getResources().getColor(
                R.color.green_see_formation));
        statusTextView_2.setVisibility(View.INVISIBLE);
        statusTextView.setVisibility(View.VISIBLE);
        container_2.setBackgroundColor(context.getResources().getColor(
                R.color.gray_metegol));
//		statusId=1;
        switch (statusId) {

            case 1:
                if (!game.getTimeOfGame().equalsIgnoreCase("")) {
                    statusTextView.setText(game.getTimeOfGame());

                } else {
                    statusTextView.setText(context
                            .getString(R.string.StatusMatch_1).toUpperCase());

                }
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.green_see_formation));
                container.setVisibility(View.VISIBLE);
                break;
            case 2:
                statusTextView.setText(context.getString(R.string.StatusMatch_2)
                        .toUpperCase());
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.blue_sky_finalized));
                container.setVisibility(View.VISIBLE);

                break;
            case 3:
                statusTextView.setText(context.getString(R.string.StatusMatch_3)
                        .toUpperCase());
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.blue_sky_finalized));
                container.setVisibility(View.VISIBLE);

                break;
            case 4:
                statusTextView.setText(context.getString(R.string.StatusMatch_4)
                        .toUpperCase());
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.blue_sky_finalized));
                container.setVisibility(View.VISIBLE);

                break;
            case 5:
                statusTextView.setText(context.getString(R.string.StatusMatch_5)
                        .toUpperCase());
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.orange_halftime));
                container.setVisibility(View.VISIBLE);

                break;
            case 6:
                if (!game.getTimeOfGame().equalsIgnoreCase("")) {
                    statusTextView.setText(game.getTimeOfGame());

                } else {
                    statusTextView.setText(context
                            .getString(R.string.StatusMatch_6).toUpperCase());

                }
                container.setVisibility(View.VISIBLE);
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.green_see_formation));

                break;
            case 7:
                statusTextView.setText(context.getString(R.string.StatusMatch_7)
                        .toUpperCase());
                container.setBackgroundColor(context.getResources().getColor(
                        R.color.orange_halftime));
                container.setVisibility(View.VISIBLE);

                break;
            case 8:
                statusTextView.setText(context.getString(R.string.StatusMatch_8)
                        .toUpperCase());
                container.setVisibility(View.VISIBLE);

                break;
            case 9:
                statusTextView.setText(context.getString(R.string.StatusMatch_9)
                        .toUpperCase());
                container.setVisibility(View.VISIBLE);

                break;
            case 10:
                statusTextView.setText(context.getString(R.string.StatusMatch_10)
                        .toUpperCase());
                container.setVisibility(View.VISIBLE);

                break;
            case 11:
                statusTextView.setText(context.getString(R.string.StatusMatch_11)
                        .toUpperCase());
                container.setVisibility(View.VISIBLE);

                break;
            case 12:
                statusTextView.setText(context.getString(R.string.StatusMatch_12)
                        .toUpperCase());
                container.setVisibility(View.VISIBLE);

                break;
            case 100:
                statusTextView.setText(context.getString(R.string.StatusMatch_100)
                        .toUpperCase());
                statusTextView.setVisibility(View.GONE);
                statusTextView_2.setText(context
                        .getString(R.string.StatusMatch_100).toUpperCase());
                container_2.setBackgroundColor(context.getResources().getColor(
                        R.color.green_see_formation));
                statusTextView_2.setVisibility(View.VISIBLE);
                container.setVisibility(View.INVISIBLE);

                break;
            default:
                container.setVisibility(View.INVISIBLE);

                break;
        }

    }

    private static class ViewHolder {
        TextView tvAcronymHome = null;
        TextView tvAcronymAway = null;
        TextView tvGoalsHome = null;
        TextView tvGoalsAway = null;
        TextView tvTimeStart = null;
        TextView tvDate = null;
        ImageView ivEmblemHome = null;
        ImageView ivEmblemAway = null;
        ImageView ivTime;
        View loadingHome;
        View loadingAway;
        View containerStateMatch;
        View containerStateMatch_2;

        TextView tvStatus;
        TextView tvStatus_2;

    }
}
