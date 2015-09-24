package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.Team;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemScorers implements Item {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemScorers";
    private static LayoutInflater mInflater = null;
    private static ImageLoader imageLoader = null;
    private static DisplayImageOptions options = null;
    private static Drawable drawable = null;
    private Player player;
    private ImageLoadingListener animateFirstListener = null;

    public ItemScorers() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory().cacheOnDisc().build();
        if (animateFirstListener == null)
            animateFirstListener = new AnimateFirstDisplayListener();
        if (imageLoader == null)
            imageLoader = ImageLoader.getInstance();

    }

    public Object getData() {
        return player;

    }

    public void setData(Object data) {
        if (data instanceof Player)
            player = (Player) data;
    }

    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (drawable == null)
            drawable = context.getResources().getDrawable(
                    R.drawable.icon_player_simple);
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.scorers_row, null);
           /* viewHolder.ivPhotoPlayer = (ImageView) view
                    .findViewById(R.id.iv_photo_player);*/
            // viewHolder.tvJu = (TextView) view
            // .findViewById(R.id.tv_TableColumnGP);
            viewHolder.tvName = (TextView) view
                    .findViewById(R.id.tv_name_player);
            // viewHolder.tvCa = (TextView) view
            // .findViewById(R.id.tv_tableColumnHG);
            // viewHolder.tvTl = (TextView) view
            // .findViewById(R.id.tv_tableColumnFK);
            viewHolder.tvTot = (TextView) view
                    .findViewById(R.id.tv_tableColumn_tot);
            // viewHolder.tvPe = (TextView) view
            // .findViewById(R.id.tv_tableColumnMD);
            viewHolder.ivEmblem = (ImageView) view
                    .findViewById(R.id.iv_emblem_team_player);
            viewHolder.background = view
                    .findViewById(R.id.container_scorers_rows);
            /*viewHolder.blackHead = (ImageView) view
                    .findViewById(R.id.iv_photo_player_thumb);*/

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (player == null)
            return view;
        if (player.getPosition() % 2 == 0)
            viewHolder.background
                    .setBackgroundResource(R.color.gray_not_pair_team);

        else
            viewHolder.background.setBackgroundResource(R.color.gray_position);

        // viewHolder.tvJu.setText("" + player.getGames());
        // viewHolder.tvCa.setText("" + player.getHeader());
        viewHolder.tvTot.setText("" + player.getGoals());
        // viewHolder.tvPe.setText("" + player.getPenalty());
        // viewHolder.tvTl.setText("" + player.getFreeKick());
        Team team = player.getTeam();

        viewHolder.tvName.setText(player.getNameKnown().trim() + " ("
                + team.getAbbreviation().trim() + ")");

        /*
        int width = (int) (viewHolder.ivPhotoPlayer.getLayoutParams().width * 1.28);
        int height = (int) (viewHolder.ivPhotoPlayer.getLayoutParams().height * 1.28);
        String urlEmblem = player.getPicture().replace(
                ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height
                        + ResourcesMetegol.VALUE_SCALE_INSIDE
        );

        if (player.getPicture() != null
                && !player.getPicture().equalsIgnoreCase("")) {
            viewHolder.blackHead.setVisibility(View.INVISIBLE);
        }

        imageLoader.displayImage(urlEmblem, viewHolder.ivPhotoPlayer, options,
                animateFirstListener);*/

        if (team.getEmblem() != null) {

            int width = (int) (viewHolder.ivEmblem.getLayoutParams().width * 1.0);
            int height = (int) (viewHolder.ivEmblem.getLayoutParams().height * 1);
            String urlEmblem = team.getEmblem().replace(
                    com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + width + "x" + height
                            + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
            );

            imageLoader.displayImage(urlEmblem, viewHolder.ivEmblem, options,
                    animateFirstListener);
        }

        return view;
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            ImageView imageView = (ImageView) view;

            // imageView.setScaleType(ScaleType.CENTER);
            imageView.refreshDrawableState();
            if (loadedImage != null) {

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private static class ViewHolder {
        private View background;
        private TextView tvName;
        //private ImageView blackHead;
        //private ImageView ivPhotoPlayer;
        private TextView tvJu;
        private ImageView ivEmblem;
        private TextView tvCa;
        private TextView tvTl;
        private TextView tvTot;
        private TextView tvPe;

    }

}
