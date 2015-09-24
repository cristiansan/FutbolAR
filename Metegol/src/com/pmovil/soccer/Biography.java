package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.PlayerFieldComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.Player;

public class Biography {

    public static final String PLAYER = "player";
    private static final String TAG = "FilePlayerActivity";
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private View biographyView;
    private Context context;
    private Player playerData;
    // private TextView nameTitleTextView;
    private TextView nameTextView;
    private TextView countryTextView;
    private TextView ageTextView;
    private TextView birthdayTextView;
    private ImageView teamEmblemImageView;
    private TextView teamNameTextView;
    private TextView positionValueTextView;
    private TextView weight_valueTextView;
    private TextView heightValueTextView;
    private TextView goalsTotalTextView;
    private TextView movesTextView;
    private TextView shotFreeTextView;
    private TextView penalTextView;
    private TextView shotSoccerGoalTextView;
    private TextView deflectedTextView;
    private TextView postTextView;
    private TextView foulsReceivedTextView;
    private TextView foulsCommittedTextView;
    private TextView cardsYellowTextView;
    private TextView cardRedTextView;
    private DisplayImageOptions optionsTeamImage;
    private DisplayImageOptions optionsPlayerImage;
    private RelativeLayout containerDataPlayerFile;
    private RelativeLayout containerPlayerField;

    public Biography(Context context, Player player) {
        this.context = context;
//		Serializable serializable = ResourcesMetegol.getInstance(context).getPlayer();
//		if (serializable instanceof Player) {
//			playerData = (Player) serializable;
//		}
        playerData = player;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        biographyView = layoutInflater.inflate(R.layout.biography_player, null);

        containerPlayerField = (RelativeLayout) biographyView.findViewById(R.id.container_player_field);
        containerDataPlayerFile = (RelativeLayout) biographyView.findViewById(R.id.container_data_player_file_player_activity);
        containerDataPlayerFile.setBackgroundResource(R.drawable.rounded_personal_file_player);
        nameTextView = (TextView) biographyView.findViewById(R.id.tv_name_file_player_activity);

        countryTextView = (TextView) biographyView.findViewById(R.id.tv_country_file_player_activity);
        ageTextView = (TextView) biographyView.findViewById(R.id.tv_age_file_player_activity);
        birthdayTextView = (TextView) biographyView.findViewById(R.id.tv_birthday_file_player_activity);
        teamEmblemImageView = (ImageView) biographyView.findViewById(R.id.iv_team_emblem_file_player_activity);
        teamNameTextView = (TextView) biographyView.findViewById(R.id.tv_team_name_file_player_activity);

        positionValueTextView = (TextView) biographyView.findViewById(R.id.tv_position_value_file_player_activity);

        weight_valueTextView = (TextView) biographyView.findViewById(R.id.tv_weight_value_file_player_activity);

        heightValueTextView = (TextView) biographyView.findViewById(R.id.tv_height_value_file_player_activity);
        goalsTotalTextView = (TextView) biographyView.findViewById(R.id.tv_goals_total_file_player_activity);
        movesTextView = (TextView) biographyView.findViewById(R.id.tv_moves_file_player_activity);
        shotFreeTextView = (TextView) biographyView.findViewById(R.id.tv_shot_free_file_player_activity);
        penalTextView = (TextView) biographyView.findViewById(R.id.tv_penal_file_player_activity);
        shotSoccerGoalTextView = (TextView) biographyView.findViewById(R.id.tv_shot_soccer_goal_file_player_activity);
        deflectedTextView = (TextView) biographyView.findViewById(R.id.tv_deflected_file_player_activity);
        postTextView = (TextView) biographyView.findViewById(R.id.tv_post_file_player_activity);
        foulsReceivedTextView = (TextView) biographyView.findViewById(R.id.tv_fouls_received_file_player_activity);
        foulsCommittedTextView = (TextView) biographyView.findViewById(R.id.tv_fouls_committed_file_player_activity);
        cardsYellowTextView = (TextView) biographyView.findViewById(R.id.tv_cards_yellow_file_player_activity);
        cardRedTextView = (TextView) biographyView.findViewById(R.id.tv_card_red_file_player_activity);

        if (playerData != null)
            loadDataPlayer();
    }


    private void loadDataPlayer() {

        PlayerFieldComponent playerFieldComponent = new PlayerFieldComponent(
                context, false);
        playerFieldComponent.setPlayer(playerData);

        containerPlayerField.addView(playerFieldComponent);

        if (playerData.getFilePersonal() != null) {

            if(playerData.getFilePersonal().getCountry()==null){
                countryTextView.setText("");
            }else{
                countryTextView.setText(playerData.getFilePersonal().getCountry()
                        .getValue());
            }

            ageTextView.setText("" + playerData.getFilePersonal().getAge()
                    + " " + context.getString(R.string.years));
            birthdayTextView.setText(playerData.getFilePersonal()
                    .getDateBirth());

            weight_valueTextView.setText(""
                    + playerData.getFilePersonal().getWeight() + " Kg");

            String height = Integer.toString(playerData.getFilePersonal()
                    .getHeight());
            if (height.length() > 1) {
                try {
                    height = height.charAt(0) + "." + height.substring(1);
                } catch (Exception e) {
                }
            }
            heightValueTextView.setText(height + " m");

            nameTextView.setText(playerData.getNameKnown().toUpperCase());

        }

        if (playerData.getFileGame() != null) {
            positionValueTextView.setText(playerData.getFileGame()
                    .getPosition());

        }

        if (playerData.getFileIncidence() != null) {
            loadValue(goalsTotalTextView, ""
                    + playerData.getFileIncidence().getGoalsTotal());
            loadValue(shotFreeTextView, ""
                    + playerData.getFileIncidence().getGoalsFreeKick());
            loadValue(movesTextView, ""
                    + playerData.getFileIncidence().getGoalsPlay());
            loadValue(penalTextView, ""
                    + playerData.getFileIncidence().getGoalsPenal());
            loadValue(shotSoccerGoalTextView, ""
                    + playerData.getFileIncidence().getGoalShot());
            loadValue(postTextView, ""
                    + playerData.getFileIncidence().getGoalpostShot());
            loadValue(deflectedTextView, ""
                    + playerData.getFileIncidence().getDeflectedShot());
            loadValue(cardRedTextView, ""
                    + playerData.getFileIncidence().getRedCards());
            loadValue(cardsYellowTextView, ""
                    + playerData.getFileIncidence().getYellowCards());
            loadValue(foulsCommittedTextView, ""
                    + playerData.getFileIncidence().getFoulsCommitted());
            loadValue(foulsReceivedTextView, ""
                    + playerData.getFileIncidence().getFoulsReceived());
        }

        /**
         * int viewHeight = playerPictureImageView.getLayoutParams().height + 1;
         * int viewWidth = playerPictureImageView.getLayoutParams().width + 1;
         * if (playerData.getPicture().length() > 0) { String urlEmblem =
         * playerData.getPicture().replace(
         * ResourcesMetegol.VALUE_REPLACE_IMG_URL, "" + viewWidth + "x" +
         * viewHeight + ResourcesMetegol.VALUE_SCALE_INSIDE); //
         * imageLoader.displayImage(urlEmblem, playerPictureImageView, //
         * optionsPlayerImage); }
         */
        int viewHeight = teamEmblemImageView.getLayoutParams().height + 1;
        int viewWidth = teamEmblemImageView.getLayoutParams().width + 1;
        if (playerData.getTeam() != null
                && playerData.getTeam().getEmblem() != null
                && playerData.getTeam().getEmblem().length() > 0) {
            String urlEmblem = playerData
                    .getTeam()
                    .getEmblem()
                    .replace(
                            com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                            "" + viewWidth + "x" + viewHeight
                                    + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE
                    );
            imageLoader.displayImage(urlEmblem, teamEmblemImageView,
                    optionsTeamImage);
            teamNameTextView.setText(playerData.getTeam().getName());

        }

    }

    private void loadValue(TextView textView, String value) {

        String text = textView.getText().toString();
        textView.setText(text + ": " + value);
    }

    public View getViewBiography() {
        return biographyView;

    }
}
