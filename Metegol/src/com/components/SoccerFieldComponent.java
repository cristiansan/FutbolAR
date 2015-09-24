package com.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.FilePlayerActivity;
import com.pmovil.soccer.FragmentBase;
import com.pmovil.soccer.R;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class SoccerFieldComponent extends AbsoluteLayout implements
        OnClickListener {

    private static final String TAG = "SoccerFieldComponent";
    private static final float SCALESIZE = 480;
    float scale = 1;
    private ImageView ivSoccerField;
    private AbsoluteLayout rlSoccerField;
    private Context context;
    private FragmentBase fragmentParent;
    // players lists
    private Player goalKeeper;
    private List<Player> backfielderPlayers = new ArrayList<Player>();
    private List<Player> midfielderPlayers = new ArrayList<Player>();
    private List<Player> frontfielderPlayers = new ArrayList<Player>();
    private PlayerFieldComponent playerField;
    // pos arrays
    // private int[] posX;
    private int[] posY;

    public SoccerFieldComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.soccerfield, this);

        scale = getScaleFactor();

        setupViewItems();
    }

    @SuppressLint("NewApi")
    private float getScaleFactor() {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        int width;
        try {
            display.getSize(size);
            width = size.x;
        } catch (NoSuchMethodError e) {
            width = display.getWidth();
        }

        float scaleFactor = width / SCALESIZE;
        ////Log.i("scalefactor", "scalefactor is = " + scaleFactor);
        return scaleFactor;
    }

    private void setupViewItems() {
        ivSoccerField = (ImageView) findViewById(R.id.iv_soccer_field);
        rlSoccerField = (AbsoluteLayout) findViewById(R.id.lay_soccer_field);

    }

    public void createPlayersInFieldWithPlayersArray(List<Player> playersList) {
        ImageLoader.getInstance().clearMemoryCache();
        createPlayerLists(playersList);
        addPlayersToField();
    }

    private void addPlayersToField() {
        rlSoccerField.removeAllViews();
        rlSoccerField.addView(ivSoccerField, 0);
        // ivSoccerField.setBackgroundResource(R.drawable.soccer_field);
        posY = getResources().getIntArray(R.array.yPosForPlayers);

        addGoalKeeperToField();
        addBackFieldersToField();
        addMidFieldersToField();
        addFrontFieldersToField();

    }

    private void addGoalKeeperToField() {
        int[] posX = getResources().getIntArray(R.array.xPosForOnePlayer);
        Point pos = new Point();
        pos.x = (int) (posX[0] * scale);
        pos.y = (int) (posY[0] * scale);
        addPlayerToField(goalKeeper, pos);
    }

    private void addFrontFieldersToField() {
        int[] posX = getPosXVectorWithPlayerQuantity(frontfielderPlayers.size());
        int posY = (int) (this.posY[3] * scale);

        int counter = 0;
        for (Player player : frontfielderPlayers) {
            if (posX.length <= counter)
                break;
            Point pos = new Point();
            pos.x = (int) (posX[counter] * scale);
            pos.y = posY;

            addPlayerToField(player, pos);

            counter++;
        }
    }

    private void addMidFieldersToField() {
        int[] posX = getPosXVectorWithPlayerQuantity(midfielderPlayers.size());
        int[] posYDisplace = getPosYVectorWithPlayerQuantity(
                midfielderPlayers.size(), 3);
        int posY = (int) (this.posY[2] * scale);

        int counter = 0;
        for (Player player : midfielderPlayers) {
            if (posX.length <= counter)
                break;
            Point pos = new Point();
            pos.x = (int) (posX[counter] * scale);
            pos.y = (int) (posY - (posYDisplace[counter] * scale));

            addPlayerToField(player, pos);

            counter++;
        }
    }

    private void addBackFieldersToField() {
        int[] posX = getPosXVectorWithPlayerQuantity(backfielderPlayers.size());
        int[] posYDisplace = getPosYVectorWithPlayerQuantity(
                backfielderPlayers.size(), 2);
        int posY = (int) (this.posY[1] * scale);

        int counter = 0;
        for (Player player : backfielderPlayers) {
            if (posX.length <= counter)
                break;
            Point pos = new Point();
            pos.x = (int) (posX[counter] * scale);
            pos.y = (int) (posY - (posYDisplace[counter] * scale));

            addPlayerToField(player, pos);

            counter++;
        }
    }

    private int[] getPosXVectorWithPlayerQuantity(int playersQuant) {
        int[] posX = null;
        switch (playersQuant) {
            case 1:
                posX = getResources().getIntArray(R.array.xPosForOnePlayer);
                break;
            case 2:
                posX = getResources().getIntArray(R.array.xPosForTwoPlayers);
                break;
            case 3:
                posX = getResources().getIntArray(R.array.xPosForThreePlayers);
                break;
            case 4:
                posX = getResources().getIntArray(R.array.xPosForFourPlayers);
                break;
            case 5:
                posX = getResources().getIntArray(R.array.xPosForFivePlayers);
                break;
            case 6:
                posX = getResources().getIntArray(R.array.xPosForSixPlayers);
                break;
            case 7:
            default:
                posX = getResources().getIntArray(R.array.xPosForSevenPlayers);
                break;
        }
        return posX;
    }

    private int[] getPosYVectorWithPlayerQuantity(int playersQuant, int type) {
        int[] posY = null;
        if (type == 2)
            switch (playersQuant) {
                case 1:
                    posY = getResources().getIntArray(
                            R.array.yPosForOnePlayerDefense);
                    break;
                case 2:
                    posY = getResources().getIntArray(
                            R.array.yPosForTwoPlayersDefense);
                    break;
                case 3:
                    posY = getResources().getIntArray(
                            R.array.yPosForThreePlayersDefense);
                    break;
                case 4:
                    posY = getResources().getIntArray(
                            R.array.yPosForFourPlayersDefense);
                    break;
                case 5:
                    posY = getResources().getIntArray(
                            R.array.yPosForFivePlayersDefense);
                    break;
                case 6:
                    posY = getResources().getIntArray(
                            R.array.yPosForSixPlayersDefense);
                    break;
                case 7:
                    posY = getResources().getIntArray(
                            R.array.yPosForSevenPlayersDefense);
                    break;
            }
        else
            switch (playersQuant) {
                case 1:
                    posY = getResources().getIntArray(
                            R.array.yPosForOnePlayerMiddle);
                    break;
                case 2:
                    posY = getResources().getIntArray(
                            R.array.yPosForTwoPlayersMiddle);
                    break;
                case 3:
                    posY = getResources().getIntArray(
                            R.array.yPosForThreePlayersMiddle);
                    break;
                case 4:
                    posY = getResources().getIntArray(
                            R.array.yPosForFourPlayersMiddle);
                    break;
                case 5:
                    posY = getResources().getIntArray(
                            R.array.yPosForFivePlayersMiddle);
                    break;
                case 6:
                    posY = getResources().getIntArray(
                            R.array.yPosForSixPlayersMiddle);
                    break;
                case 7:
                    posY = getResources().getIntArray(
                            R.array.yPosForSevenPlayersMiddle);
                    break;
            }
        return posY;
    }

    private void addPlayerToField(Player player, Point pos) {
        playerField = new PlayerFieldComponent(context, true);
        playerField.setOnClickListener(this);
        playerField.setPlayer(player);
        rlSoccerField.addView(playerField);
        //Log.i("playerField", "width = " + playerField.getWidth() + " heigth = " + playerField.getHeight());
        //Log.i("playerFieldmesured", "width = " + playerField.viewWidth + " heigth = " + playerField.viewHeight);
        AbsoluteLayout.LayoutParams params = ((AbsoluteLayout.LayoutParams) playerField
                .getLayoutParams());
        params.x = (int) (pos.x - ((68 / 2) * scale));
        params.y = pos.y;
        playerField.setLayoutParams(params);
    }

    private void createPlayerLists(List<Player> playersList) {
        if (playersList == null)
            return;
        backfielderPlayers.clear();
        midfielderPlayers.clear();
        frontfielderPlayers.clear();
        Collections.reverse(playersList);

        for (Player player : playersList) {
            switch (player.getRolId()) {
                case 1:
                    goalKeeper = player;
                    break;
                case 2:
                    backfielderPlayers.add(player);
                    break;
                case 3:
                    midfielderPlayers.add(player);
                    break;
                case 4:
                    frontfielderPlayers.add(player);
                    break;
            }
        }
        // sort arrays
        Collections.sort(backfielderPlayers, new Player());
        Collections.sort(midfielderPlayers, new Player());
        Collections.sort(frontfielderPlayers, new Player());
        Collections.reverse(backfielderPlayers);
        Collections.reverse(midfielderPlayers);
        Collections.reverse(frontfielderPlayers);

    }

    public void onClick(View v) {
        if (v instanceof PlayerFieldComponent) {
            Player player = ((PlayerFieldComponent) v).getPlayer();
            if (player != null) {
                if (fragmentParent != null) {
                    fragmentParent.showLoading();
                }
                com.pmovil.soccer.entities.parser.ResourcesMetegol resources = com.pmovil.soccer.entities.parser.ResourcesMetegol
                        .getInstance(context);
                com.pmovil.soccer.net.ConnectionsWSMetegol conWsFutebol = resources
                        .getConnWsFutebol();
                conWsFutebol.getPlayerDataPost(new ConnectionHandler(player),
                        resources.getChampionshipByIdSelected().getId(),
                        resources.getGameSelectedId(), player.getId());
            }
        }

    }

    public FragmentBase getFragmentParent() {
        return fragmentParent;
    }

    public void setFragmentParent(FragmentBase parent) {
        this.fragmentParent = parent;
    }

    private class ConnectionHandler implements ConnectionHandlerResponseBody {

        private Player player;

        public ConnectionHandler(Player player) {
            this.player = player;
        }

        public void onSuccess(String response) {
            try {
                JsonParsers.loadFilePlayer(player, new JSONObject(response));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            ////Log.i(TAG, response);

            Intent intent = new Intent(context, FilePlayerActivity.class);
            intent.putExtra(FilePlayerActivity.PLAYER, player);
            context.startActivity(intent);
            if (fragmentParent != null) {
                fragmentParent.hideLoading();
            }
        }

        public void onError(String response) {
            ////Log.i(TAG, response);
            if (fragmentParent != null) {
                fragmentParent.hideLoading();
            }
        }

    }
}
