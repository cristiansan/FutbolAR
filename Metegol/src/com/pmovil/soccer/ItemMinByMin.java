package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.components.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.Incidence;
import com.pmovil.soccer.entities.MinToMinData;
import com.pmovil.soccer.entities.Team;

import java.util.List;

public class ItemMinByMin implements Item, OnClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemMinToMin";
    private static final int firstTime = 1;
    private static final int secondTime = 2;
    private static final int matchStart = 0;
    private static final int extraTime = 3;
    private static final int penalty = 4;
    private static final int EventTypeYellowCard = 3, EventTypeRedCard = 4,
            EventTypeRedCard2 = 5, EventTypeChange = 6,
            EventTypePlayedGoal = 9, EventTypeHeadGoal = 11,
            EventTypeFreekickGoal = 12, EventTypePenaltyGoal = 13,
            EventTypeStart = 1, EventTypeEnd = 2, EventTypeChange2 = 7,
            EventTypeAgainstGoal = 10, EventTypeEndFirstTime = 17,
            EventTypeStartSecondTime = 18, EventTypeCornerKick = 28,
            EventTypeClear = 29, EventTypeClearToCorner = 30,
            EventTypePenaltyByFoul = 31, EventTypePenaltyByHand = 32,
            EventTypeMissedShoot = 33, EventTypePostShoot = 34,
            EventTypeArcShoot = 35, EventTypeFoul = 36, EventTypeHand = 37,
            EventTypeSaved = 38, EventTypeGoalAttendance = 39,
            EventTypeReceivedFoul = 40, EventTypeSavedPenalty = 43,
            EventTypeDeflectedPenalty = 44, EventTypePostPenalty = 45,
            EventTypeThrow = 46, EventTypeOffside = 47, EventTypeGoalKick = 48,
            EventTypeRegularTimeEnded = 49, EventTypePenaltyTimeStart = 54,
            EventTypePenaltyTimeGoal = 55, EventTypePenaltyTimeSaved = 56;
    private static LayoutInflater mInflater = null;
    private static DisplayImageOptions options;
    private static Drawable iconFlag = null;
    private static Drawable iconCross = null;
    private static Drawable iconBooty = null;
    private static Drawable iconYellow = null;
    private static Drawable iconRed = null;
    private static Drawable iconGloves = null;
    private static Drawable iconWhistle = null;
    private static Drawable iconChange = null;
    private static Drawable iconBall = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Incidence incidence = null;
    private MainActivity ma;
    private Context context;
    private boolean withoutShareAndComment = false;
    private boolean left = false;
    private boolean Comment = true;

    public ItemMinByMin() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
    }

    private void loadDrawable(Context context) {
        if (iconFlag == null)
            iconFlag = context.getResources().getDrawable(
                    R.drawable.icon_bandera);
        iconFlag.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        if (iconCross == null)
            iconCross = context.getResources()
                    .getDrawable(R.drawable.icon_cruz);
        iconCross.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        if (iconBooty == null)
            iconBooty = context.getResources().getDrawable(
                    R.drawable.icon_botin);
        iconBooty.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);

        if (iconYellow == null)
            iconYellow = context.getResources().getDrawable(
                    R.drawable.card_yellow);
        if (iconRed == null)
            iconRed = context.getResources().getDrawable(
                    R.drawable.icon_tarjeta_roja);
        if (iconGloves == null)
            iconGloves = context.getResources().getDrawable(
                    R.drawable.icon_guantes);
        iconGloves.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        if (iconWhistle == null)
            iconWhistle = context.getResources().getDrawable(
                    R.drawable.icon_silbato);
            iconWhistle.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        if (iconChange == null)
            iconChange = context.getResources().getDrawable(
                    R.drawable.icon_cambio);
        iconChange.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);

        if (iconBall == null)
            iconBall = context.getResources().getDrawable(
                    R.drawable.icon_pelota);
        iconBall.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);

    }

    public Object getData() {
        return incidence;
    }

    public void setData(Object data) {
        if (data instanceof Incidence) {
            incidence = (Incidence) data;
        }
    }

    public View getView(View view, Context context) {
        this.context = context;
        loadDrawable(context);
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.min_to_min_row, null);
            viewHolder.ivIconTypeIncidenceAndEmblemLeft = (ImageView) view
                    .findViewById(R.id.iv_icon_type_incidence_left);
           /* viewHolder.tvTimeGameLeft = (TextView) view
                    .findViewById(R.id.tv_time_game_left);*/
            viewHolder.tvMinLeft = (TextView) view
                    .findViewById(R.id.tv_min_left);
            viewHolder.ivIconTypeIncidenceAndEmblemRight = (ImageView) view
                    .findViewById(R.id.iv_icon_type_incidence_right);
            /*viewHolder.tvTimeGameRight = (TextView) view
                    .findViewById(R.id.tv_time_game_right);*/
            viewHolder.tvMinRight = (TextView) view
                    .findViewById(R.id.tv_min_right);
            viewHolder.tvCommentBody = (TextView) view
                    .findViewById(R.id.tv_comment_incidence);
            /*
            viewHolder.viewContainerCommentLeft = view
                    .findViewById(R.id.container_min_to_min_btn_comment_left);
            viewHolder.viewContainerShareLeft = view
                    .findViewById(R.id.container_min_to_min_btn_share_left);
            viewHolder.viewContainerCommentRight = view
                    .findViewById(R.id.container_min_to_min_btn_comment_right);
           viewHolder.viewContainerShareRight = view
                    .findViewById(R.id.container_min_to_min_btn_share_right);*/

            /*
            viewHolder.headerLeft = view
                    .findViewById(R.id.incidence_left_min_by_min);*/
            /*viewHolder.headerRight = view
                    .findViewById(R.id.incidence_right_min_by_min);*/
            viewHolder.viewBackground = view.findViewById(R.id.view_background);
            /*viewHolder.iconSide = (ImageView) view
                    .findViewById(R.id.iv_icon_arrow_left);*/
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        /*
        if (incidence == null)
            return view;
        switch (incidence.getHalf()) {
            case firstTime:
                viewHolder.tvTimeGameLeft.setText(R.string.HalfTimeKey_1);
                break;
            case secondTime:
                viewHolder.tvTimeGameLeft.setText(R.string.HalfTimeKey_2);
                break;
            default:
                break;
        }*/

        /*viewHolder.viewContainerCommentLeft.setOnClickListener(this);
        //viewHolder.viewContainerShareLeft.setOnClickListener(this);
        viewHolder.viewContainerCommentRight.setOnClickListener(this);
       // viewHolder.viewContainerShareRight.setOnClickListener(this);*/

        ImageView ivIncidenceIcon = getCorrectImageIncidenceIconAndLoadEmblem(
                viewHolder, incidence.getTeamId());
        loadIconIncidence(ivIncidenceIcon);

        viewHolder.tvMinRight.setText("" + incidence.getRelativeMinutes() + "'");
        viewHolder.tvMinLeft.setText("" + incidence.getRelativeMinutes() + "'");

        viewHolder.tvCommentBody.setText(generateStringTypeIncidence(context));

        // if (withoutShareAndComment) {
        // viewHolder.containerMinToMinCommentsShare.setVisibility(View.GONE);
        // } else {
        // viewHolder.containerMinToMinCommentsShare
        // .setVisibility(View.VISIBLE);
        // }

        return view;
    }

    public void showDialog(boolean isShare) {

        /*
        final DialogShareComment dialog = new DialogShareComment(context,
                R.style.CustomDialogTheme, R.layout.popup_comment_share);
        dialog.setShare(isShare);

        if (isShare) {
            dialog.setMsgPost(generateStringTypeIncidence(context)
                    + generateTagsComment());
        } else {
            dialog.setIncidence(incidence);
            dialog.setMatchId(ResourcesMetegol.getInstance(context)
                    .getGameSelectedId());

        }
        dialog.setActivity((MainActivity) context);
        dialog.show();*/
    }

    private void loadIconIncidence(ImageView ivIconIncidence) {
        if (ivIconIncidence == null)
            return;
        ivIconIncidence.setImageDrawable(iconWhistle);
        switch (incidence.getTypeId()) {
            case EventTypeAgainstGoal:
            case EventTypeArcShoot:
            case EventTypeGoalAttendance:
            case EventTypeMissedShoot:
            case EventTypePostPenalty:
            case EventTypePostShoot:
            case EventTypeThrow:
                ivIconIncidence.setImageDrawable(iconBooty);
                break;
            case EventTypeChange:
            case EventTypeChange2:
                ivIconIncidence.setImageDrawable(iconChange);
                break;
            case EventTypeClear:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypeClearToCorner:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypeCornerKick:
                ivIconIncidence.setImageDrawable(iconFlag);
                break;
            case EventTypeDeflectedPenalty:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypeEnd:
            case EventTypeEndFirstTime:
            case EventTypeFoul:
            case EventTypeFreekickGoal:
            case EventTypeGoalKick:
            case EventTypeOffside:
            case EventTypePenaltyByFoul:
            case EventTypePenaltyByHand:
            case EventTypePenaltyTimeStart:
            case EventTypeRegularTimeEnded:
            case EventTypeStart:
            case EventTypeStartSecondTime:
                ivIconIncidence.setImageDrawable(iconWhistle);
                break;
            case EventTypeHand:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypePenaltyGoal:
            case EventTypeHeadGoal:

            case EventTypePenaltyTimeGoal:
                ivIconIncidence.setImageDrawable(iconBall);
                break;
            case EventTypePenaltyTimeSaved:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypePlayedGoal:
                ivIconIncidence.setImageDrawable(iconBall);
                break;
            case EventTypeReceivedFoul:
                ivIconIncidence.setImageDrawable(iconCross);
                break;
            case EventTypeRedCard:
                ivIconIncidence.setImageDrawable(iconRed);
                break;
            case EventTypeRedCard2:
                ivIconIncidence.setImageDrawable(iconRed);

                break;
            case EventTypeSaved:
                ivIconIncidence.setImageDrawable(iconGloves);

                break;
            case EventTypeSavedPenalty:
                ivIconIncidence.setImageDrawable(iconGloves);
                break;
            case EventTypeYellowCard:
                ivIconIncidence.setImageDrawable(iconYellow);
                break;
            default:
                ivIconIncidence.setImageDrawable(iconWhistle);
                break;
        }

    }

    private String generateStringTypeIncidence(Context context) {
        String incidenceString = "";
        switch (incidence.getTypeId()) {
            case 1:
                incidenceString = context.getString(R.string.commentKey_1);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TYPE, incidence.getType());
                break;
            case 2:
                incidenceString = context.getString(R.string.commentKey_2);

                break;
            case 3:

                incidenceString = context.getString(R.string.commentKey_3);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            case 4:

                incidenceString = context.getString(R.string.commentKey_4);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            case 5:

                incidenceString = context.getString(R.string.commentKey_5);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            // case 6:
            //
            // incidenceString = context.getString(R.string.commentKey_6);
            // incidenceString = incidenceString.replace(
            // ResourcesMetegol.VALUE_TEAM,
            // getTeamName(incidence.getTeamId()));
            // incidenceString = incidenceString.replace(
            // ResourcesMetegol.VALUE_IN_NAME, incidence.getInName());
            //
            // break;
            case 7:

                incidenceString = context.getString(R.string.commentKey_7);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_KNOWN_IN_NAME,
                        incidence.getKnownInName());
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_KNOWN_OFF_NAME,
                        incidence.getKnownOffName());
                break;
            case 8:

                incidenceString = context.getString(R.string.commentKey_8);
                break;
            case 9:

                incidenceString = context.getString(R.string.commentKey_9);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_ASSISTANCE_BY,
                        incidence.getAssistanceBy());

                break;
            case 10:

                incidenceString = context.getString(R.string.commentKey_10);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            case 11:

                incidenceString = context.getString(R.string.commentKey_11);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_ASSISTANCE_BY,
                        incidence.getAssistanceBy());
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            case 12:

                incidenceString = context.getString(R.string.commentKey_12);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));
                break;
            case 13:

                incidenceString = context.getString(R.string.commentKey_13);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));
                break;

            case 17:

                incidenceString = context.getString(R.string.commentKey_17);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TYPE, incidence.getType());

                break;
            case 18:

                incidenceString = context.getString(R.string.commentKey_18);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TYPE, incidence.getType());

                break;

            case 27:

                incidenceString = context.getString(R.string.commentKey_27);
                break;
            case 28:

                incidenceString = context.getString(R.string.commentKey_28);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );

                break;
            case 29:

                incidenceString = context.getString(R.string.commentKey_29);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );

                break;
            case 30:

                incidenceString = context.getString(R.string.commentKey_30);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );

                break;
            case 31:

                incidenceString = context.getString(R.string.commentKey_31);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_RECEIVED_NAME,
                        incidence.getReceivedName());
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));
                break;
            case 32:

                incidenceString = context.getString(R.string.commentKey_32);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TYPE, incidence.getType());

                break;
            case 33:

                incidenceString = context.getString(R.string.commentKey_33);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 34:

                incidenceString = context.getString(R.string.commentKey_34);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 35:

                incidenceString = context.getString(R.string.commentKey_35);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString
                        .replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_CATCHED_BY,
                                incidence.getCatchedBy());

                break;
            case 36:

                incidenceString = context.getString(R.string.commentKey_36);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_RECEIVED_NAME,
                        incidence.getReceivedName());
                break;
            case 37:

                incidenceString = context.getString(R.string.commentKey_37);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TYPE, incidence.getType());
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 43:

                incidenceString = context.getString(R.string.commentKey_43);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );

                incidenceString = incidenceString
                        .replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_CATCHED_BY,
                                incidence.getCatchedBy());
                break;
            case 44:

                incidenceString = context.getString(R.string.commentKey_44);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 45:

                incidenceString = context.getString(R.string.commentKey_45);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 46:

                incidenceString = context.getString(R.string.commentKey_46);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));

                break;
            case 47:

                incidenceString = context.getString(R.string.commentKey_47);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );

                break;
            case 48:

                incidenceString = context.getString(R.string.commentKey_48);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            case 49:

                incidenceString = context.getString(R.string.commentKey_49);
                break;
            case 50:

                incidenceString = context.getString(R.string.commentKey_50);
                break;
            case 51:

                incidenceString = context.getString(R.string.commentKey_51);
                break;
            case 52:

                incidenceString = context.getString(R.string.commentKey_52);
                break;
            case 53:

                incidenceString = context.getString(R.string.commentKey_53);
                break;

            case 54:

                incidenceString = context.getString(R.string.commentKey_54);
                break;

            case 55:

                incidenceString = context.getString(R.string.commentKey_55);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_TEAM,
                        getTeamName(incidence.getTeamId()));
                break;
            case 56:

                incidenceString = context.getString(R.string.commentKey_56);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                incidenceString = incidenceString
                        .replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_CATCHED_BY,
                                incidence.getCatchedBy());
                break;
            case 57:

                incidenceString = context.getString(R.string.commentKey_57);
                incidenceString = incidenceString.replace(
                        com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_PLAYER_NAME,
                        getNamePlayer(incidence.getPlayerId(),
                                incidence.getTeamId())
                );
                break;
            default:
                break;
        }

        return incidenceString;

    }

    private String generateTagsComment() {
        // #<siglas_equipolocal>vs<siglas_equipovisitante> y #Metegol
        MinToMinData minToMin = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context)
                .getMinToMinData();
        if (minToMin == null)
            return null;
        List<Team> teams = minToMin.getTeams();
        if (teams == null || teams.isEmpty())
            return null;
        String awayAcronym = "";
        String homeAcronym = "";

        for (int indexTeam = 0; indexTeam < teams.size(); indexTeam++) {

            Team team = teams.get(indexTeam);
            if (team.getHomeOrAway().trim().equalsIgnoreCase("away")) {
                awayAcronym = team.getName();
            } else {
                homeAcronym = team.getName();
            }
        }

        return "  #" + homeAcronym.trim().replace(" ", "") + "_vs_"
                + awayAcronym.trim().replace(" ", "") + "  #TOING";
    }

    private ImageView getCorrectImageIncidenceIconAndLoadEmblem(
            ViewHolder viewHolder, int idTeam) {
        if (context == null)
            return null;

        MinToMinData minToMin = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context)
                .getMinToMinData();
        if (minToMin == null)
            return null;
        List<Team> teams = minToMin.getTeams();
        if (teams == null || teams.isEmpty())
            return null;
        Team team = new Team();
        team.setId(idTeam);
        if (teams.contains(team)) {
            team = teams.get(teams.indexOf(team));
            if (team.getHomeOrAway().equalsIgnoreCase("Home") || left) {

                //
                // int width = viewHolder.ivIconTypeIncidenceAndEmblemLeft
                // .getLayoutParams().width;
                // int height = viewHolder.ivIconTypeIncidenceAndEmblemLeft
                // .getLayoutParams().height;
                // String urlEmblem = team.getEmblem().replace(
                // ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                // "" + width + "x" + height);
                // imageLoader.displayImage(urlEmblem,
                // viewHolder.ivIconTypeIncidenceAndEmblemLeft, options,
                // animateFirstListener);
                //viewHolder.headerLeft.setVisibility(View.VISIBLE);
                viewHolder.tvMinLeft.setVisibility(View.VISIBLE);
                viewHolder.ivIconTypeIncidenceAndEmblemLeft.setVisibility(View.VISIBLE);
                //viewHolder.iconSide.setVisibility(View.VISIBLE);

                //viewHolder.headerRight.setVisibility(View.INVISIBLE);
                viewHolder.tvMinRight.setVisibility(View.GONE);
                viewHolder.ivIconTypeIncidenceAndEmblemRight.setVisibility(View.GONE);
                viewHolder.tvMinLeft.setText("" + incidence.getRelativeMinutes() + "'");
                //loadTime(viewHolder.tvTimeGameLeft, viewHolder.tvMinLeft);
                /*viewHolder.viewBackground
                        .setBackgroundResource(R.color.gray_metegol);*/
                return viewHolder.ivIconTypeIncidenceAndEmblemLeft;
            } else {

                // int width = viewHolder.ivIconTypeIncidenceAndEmblemRight
                // .getLayoutParams().width;
                // int height = viewHolder.ivIconTypeIncidenceAndEmblemRight
                // .getLayoutParams().height;
                // String urlEmblem = team.getEmblem().replace(
                // ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                // "" + width + "x" + height);
                // imageLoader.displayImage(urlEmblem,
                // viewHolder.ivIconTypeIncidenceAndEmblemRight, options,
                // animateFirstListener);
                //viewHolder.headerLeft.setVisibility(View.INVISIBLE);
                viewHolder.tvMinLeft.setVisibility(View.GONE);
                viewHolder.ivIconTypeIncidenceAndEmblemLeft.setVisibility(View.GONE);

                //viewHolder.headerRight.setVisibility(View.VISIBLE);
                viewHolder.tvMinRight.setVisibility(View.VISIBLE);
                viewHolder.ivIconTypeIncidenceAndEmblemRight.setVisibility(View.VISIBLE);
                //loadTime(viewHolder.tvTimeGameRight, viewHolder.tvMinRight);
                viewHolder.tvMinRight.setText("" + incidence.getRelativeMinutes() + "'");
                /*viewHolder.viewBackground
                        .setBackgroundResource(R.color.gray_metegol);*/
                return viewHolder.ivIconTypeIncidenceAndEmblemRight;

            }
        } else {
            // viewHolder.tvTimeGameLeft.setVisibility(View.VISIBLE);
            // viewHolder.tvMinLeft.setVisibility(View.VISIBLE);
            // viewHolder.tvTimeGameRight.setVisibility(View.INVISIBLE);
            // viewHolder.tvMinRight.setVisibility(View.INVISIBLE);

            //viewHolder.headerLeft.setVisibility(View.VISIBLE);
            viewHolder.tvMinLeft.setVisibility(View.VISIBLE);
            viewHolder.ivIconTypeIncidenceAndEmblemLeft.setVisibility(View.VISIBLE);

            //viewHolder.headerRight.setVisibility(View.INVISIBLE);
            viewHolder.tvMinRight.setVisibility(View.GONE);
            viewHolder.ivIconTypeIncidenceAndEmblemRight.setVisibility(View.GONE);

            //loadTime(viewHolder.tvTimeGameLeft, viewHolder.tvMinLeft);
            viewHolder.tvMinLeft.setText("" + incidence.getRelativeMinutes() + "'");

            /*viewHolder.viewBackground
                    .setBackgroundResource(R.color.green_blue_incidence_min_by_min);*/
            //viewHolder.iconSide.setVisibility(View.INVISIBLE);

            return viewHolder.ivIconTypeIncidenceAndEmblemLeft;
        }

    }

    /*
    private void loadTime(TextView tvTime, TextView tvMin) {
        tvMin.setText("" + incidence.getRelativeMinutes() + "'");

        switch (incidence.getHalf()) {

            case matchStart:

                tvTime.setText(R.string.HalfTimeKey_0);
                break;
            case firstTime:
                tvTime.setText(R.string.HalfTimeKey_1);
                break;
            case secondTime:
                tvTime.setText(R.string.HalfTimeKey_2);
                break;
            case extraTime:
                tvTime.setText(R.string.HalfTimeKey_3);
                break;
            case penalty:
                tvTime.setText(R.string.HalfTimeKey_4);
                break;
            default:
                tvTime.setText("");
                break;
        }

        String time = tvTime.getText().toString();

        tvTime.setText(context.getString(R.string.min) + " " + time);

    }*/

    private String getNamePlayer(int idPlayer, int idTeam) {
        if (context == null)
            return "";
        return incidence.getKnownPlayerName();
        // MinToMinData minToMin = ResourcesMetegol.getInstance(context)
        // .getMinToMinData();
        // if (minToMin == null)
        // return "";
        // List<Team> teams = minToMin.getTeams();
        // if (teams == null || teams.isEmpty())
        // return "";
        // Team team = new Team();
        // team.setId(idTeam);
        // if (teams.contains(team)) {
        // List<Player> players = teams.get(teams.indexOf(team)).getPlayers();
        // if (players != null) {
        // Player player = new Player();
        // player.setId(idPlayer);
        // if (players.contains(player)) {
        // String nickName = players.get(players.indexOf(player))
        // .getNickName();
        // if (nickName.equalsIgnoreCase("")) {
        // return incidence.getPlayerName();
        // }
        // return players.get(players.indexOf(player)).getNickName();
        // }
        // return "";
        // }
        //
        // return "";
        // }
        //
        // return "";
    }

    private String getTeamName(int idTeam) {
        if (context == null)
            return "";

        MinToMinData minToMin = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context)
                .getMinToMinData();
        if (minToMin == null)
            return "";
        List<Team> teams = minToMin.getTeams();
        if (teams == null || teams.isEmpty())
            return "";
        Team team = new Team();
        team.setId(idTeam);
        if (teams.contains(team)) {
            return teams.get(teams.indexOf(team)).getName();
        }

        return "" + idTeam;
    }

    public boolean isWithoutShareAndComment() {
        return withoutShareAndComment;
    }

    public void setWithoutShareAndComment(boolean withoutShareAndComment) {
        this.withoutShareAndComment = withoutShareAndComment;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void onClick(View v) {

        if (Comment) {

            switch (v.getId()) {

               /* case R.id.container_min_to_min_btn_comment_left:
                    showDialog(false);

                    break;*/
                case R.id.container_min_to_min_btn_share_left:
                    showDialog(true);

                    break;

                default:
                    break;
            }

        }

    }

    public boolean isComment() {
        return Comment;
    }

    public void setComment(boolean comment) {
        Comment = comment;
    }

    private static class ViewHolder {
        private TextView tvMinLeft;
        private TextView tvTimeGameLeft;
        private TextView tvMinRight;
        private TextView tvTimeGameRight;
        private ImageView ivIconTypeIncidenceAndEmblemLeft;
        private ImageView ivIconTypeIncidenceAndEmblemRight;
        private TextView tvCommentBody;
        private View viewContainerCommentLeft;
        private View viewContainerCommentRight;
        private View viewContainerShareRight;
        private View viewBackground;
        private View viewContainerShareLeft;
        // private View containerMinToMinCommentsShare;
        private View headerLeft;
        private View headerRight;
        private ImageView iconSide;

    }

}

// final Handler handler = new Handler(context.getMainLooper());
// handler.postDelayed(new Runnable() {
//
// @Override
// public void run() {
// handler.postDelayed(new Runnable() {
//
// @Override
// public void run() {
// // TODO Auto-generated method stub
//
// }
// }, 1000);
// }
// }, 1000);

// final Handler handler = new Handler()
// handler.postDelayed( new Runnable() {
//
// @Override
// public void run() {
// adapter.notifyDataSetChanged();
// handler.postDelayed( this, 1000 );
// }
// }, 1000 );