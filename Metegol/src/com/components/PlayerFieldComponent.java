package com.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pmovil.soccer.R;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.Team;

public class PlayerFieldComponent extends RelativeLayout {

    protected static ImageLoader imageLoader = ImageLoader.getInstance();
    private static DisplayImageOptions options = null;
    private static DisplayImageOptions optionsShirt = null;
    public int viewWidth = 0;
    public int viewHeight = 0;
    private ImageView ivPlayerPicture;
    private TextView tvPlayerField;
    private Player player;
    private ImageView ivPlayerIcon[] = new ImageView[3];
    private ImageView ivGoalsIcon[] = new ImageView[4];
    private TextView tvNumberOfGoals;
    private ImageView ivPlayerField;
    private ImageView ivShirtPlayerField;
    private ImageView ivPlayerNewShirt;
    private TextViewPlus tvPlayerNumber;
    private boolean showExtras;

    public PlayerFieldComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.playerfield, this);
        setupViewItems();
    }

    public PlayerFieldComponent(Context context, boolean showExtras) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.playerfield, this);
        this.showExtras = showExtras;
        setupViewItems();
    }

    private void setupViewItems() {

        tvPlayerField = (TextView) findViewById(R.id.tv_player_name_field);
        ivPlayerIcon[0] = (ImageView) findViewById(R.id.iv_player_icon_1);
        ivPlayerIcon[1] = (ImageView) findViewById(R.id.iv_player_icon_2);
        ivPlayerIcon[2] = (ImageView) findViewById(R.id.iv_player_icon_3);
        ivGoalsIcon[0] = (ImageView) findViewById(R.id.iv_goal_icon_1);
        ivGoalsIcon[1] = (ImageView) findViewById(R.id.iv_goal_icon_2);
        ivGoalsIcon[2] = (ImageView) findViewById(R.id.iv_goal_icon_3);
        ivGoalsIcon[3] = (ImageView) findViewById(R.id.iv_goal_icon_4);
        ivPlayerPicture = (ImageView) findViewById(R.id.iv_player_picture_field);
        ivPlayerField = (ImageView) findViewById(R.id.iv_player_field);
        ivShirtPlayerField = (ImageView) findViewById(R.id.iv_shirt_player_picture_field);
        tvNumberOfGoals = (TextView) findViewById(R.id.tv_number_of_goals);
        ivPlayerNewShirt = (ImageView) findViewById(R.id.iv_player_new_shirt);
        tvPlayerNumber = (TextViewPlus) findViewById(R.id.iv_player_number);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        updatePlayerUI();
    }

    private void updatePlayerUI() {

        if (player == null)
            return;

        if (showExtras == true){

            tvPlayerField.setVisibility(View.VISIBLE);
            tvPlayerField.setText(player.getNameKnown().toUpperCase());
            if (player.getRedCard() > 0) {
                ivPlayerIcon[0]
                        .setImageResource(R.drawable.icon_tarjeta_roja_team_2);
                ivPlayerIcon[0].setVisibility(View.VISIBLE);
            } else if (player.getYellowCard() > 0) {
                ivPlayerIcon[0]
                        .setImageResource(R.drawable.icon_tarjeta_amarilla_team_2);
                ivPlayerIcon[0].setVisibility(View.VISIBLE);
            }
            if (player.isChange()) {
                ivPlayerIcon[2].setImageResource(R.drawable.icon_change_team_2);
                ivPlayerIcon[2].setVisibility(View.VISIBLE);
            }

            if (player.getGoals() > 0) {
                switch (player.getGoals()) {
                    case 1:
                        ivGoalsIcon[0].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[0].setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        ivGoalsIcon[0].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[0].setVisibility(View.VISIBLE);
                        ivGoalsIcon[1].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[1].setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        ivGoalsIcon[0].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[0].setVisibility(View.VISIBLE);
                        ivGoalsIcon[1].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[1].setVisibility(View.VISIBLE);
                        ivGoalsIcon[2].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[2].setVisibility(View.VISIBLE);
                        break;
                    case 4:
                    default:
                        ivGoalsIcon[0].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[0].setVisibility(View.VISIBLE);
                        ivGoalsIcon[1].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[1].setVisibility(View.VISIBLE);
                        ivGoalsIcon[2].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[2].setVisibility(View.VISIBLE);
                        ivGoalsIcon[3].setImageResource(R.drawable.icon_pelota_team_2);
                        ivGoalsIcon[3].setVisibility(View.VISIBLE);
                        break;
                }

                if (player.getGoals() > 4) {
                    tvNumberOfGoals.setVisibility(View.VISIBLE);
                    tvNumberOfGoals.setText(String.valueOf(player.getGoals()));
                }
            }
        } else{

        }

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        if (player == null)
            return;
        viewHeight = ivPlayerPicture.getLayoutParams().height + 1;
        viewWidth = ivPlayerPicture.getLayoutParams().width + 1;

        //////Log.i("onSizeplayerField", "width = " + xNew + " heigth = " + yNew);
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(10))
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory().cacheOnDisc().build();
        if (optionsShirt == null)
            optionsShirt = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory().cacheOnDisc().build();

        if (player.getTeam().getShirt().length() > 0){

            String urlEmblem = player.getTeam().getShirt().replace(
                    com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + viewWidth + "x" + viewHeight
                            + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
            );

            imageLoader.displayImage(urlEmblem, ivPlayerNewShirt, options,
                    new ImageLoadingListener() {

                        public void onLoadingStarted(String imageUri, View view) {
                            if (ivPlayerNewShirt != null) {

                                String number = player.getSquadNumber().toString();

                                if(number.length()>1){

                                    tvPlayerNumber.setPadding(7,0,0,0);
                                }else{

                                    tvPlayerNumber.setPadding(12,0,0,0);
                                }

                                ivPlayerNewShirt.setVisibility(View.VISIBLE);
                                tvPlayerNumber.setText(number);
                                ivPlayerField.setVisibility(View.INVISIBLE);
                            }
                        }

                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            if (ivPlayerNewShirt != null) {
                                ivPlayerNewShirt.setVisibility(View.VISIBLE);
                                ivPlayerField.setVisibility(View.INVISIBLE);
                            }
                        }

                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            if (ivPlayerNewShirt != null) {
                                //ivPlayerNewShirt.setVisibility(View.INVISIBLE);
                                ivPlayerField.setVisibility(View.INVISIBLE);
                            }
                        }

                        public void onLoadingCancelled(String imageUri,
                                                       View view) {
                            if (ivPlayerNewShirt != null) {

                                String number = player.getSquadNumber().toString();

                                if(number.length()>1){

                                    tvPlayerNumber.setPadding(7,0,0,0);
                                }else{

                                    tvPlayerNumber.setPadding(12,0,0,0);
                                }

                                ivPlayerNewShirt.setVisibility(View.VISIBLE);
                                tvPlayerNumber.setText(number);
                                ivPlayerField.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
            );

        } else{

            if (player.getPicture().length() > 0) {
                String urlEmblem = player.getPicture().replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                        "" + viewWidth + "x" + viewHeight
                                + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
                );

                imageLoader.displayImage(urlEmblem, ivPlayerPicture, options,
                        new ImageLoadingListener() {

                            public void onLoadingStarted(String imageUri, View view) {
                                if (ivPlayerField != null) {
                                    ivPlayerField.setVisibility(View.VISIBLE);
                                    ivPlayerNewShirt.setVisibility(View.INVISIBLE);
                                }
                            }

                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {
                                if (ivPlayerField != null) {
                                    ivPlayerField.setVisibility(View.VISIBLE);
                                    ivPlayerNewShirt.setVisibility(View.INVISIBLE);
                                }
                            }

                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                if (ivPlayerField != null) {
                                    ivPlayerField.setVisibility(View.INVISIBLE);
                                    ivPlayerNewShirt.setVisibility(View.INVISIBLE);
                                }
                            }

                            public void onLoadingCancelled(String imageUri,
                                                           View view) {
                                if (ivPlayerField != null) {
                                    ivPlayerField.setVisibility(View.VISIBLE);
                                    ivPlayerNewShirt.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                );

            }
            if (player.getTeam() != null) {

                String urlEmblem;
                Team playerTeam = player.getTeam();
                if (player.getRolId() == 1) {
                    urlEmblem = playerTeam.getShirtGoalKeeper();
                } else {
                    urlEmblem = playerTeam.getShirt();
                }
                urlEmblem = urlEmblem.replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL, "" + viewWidth + "x" + viewHeight + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);
                imageLoader.displayImage(urlEmblem, ivShirtPlayerField, optionsShirt);
            }

        }

    }
}
