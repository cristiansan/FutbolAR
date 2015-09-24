package com.pmovil.soccer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.components.AdapterGeneric;
import com.components.Item;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.pmovil.soccer.net.ManagerConnection.ConnectionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectionNotificationActivity extends Activity {

    private static String TAG = "SelectionNotificationActivity";
    public RelativeLayout containerBanner;
    private ListView championshipTeamsListView;
    private List<Item> itemList = null;
    private List<Team> teamsDataList = null;
    private ConnectionHandlerResponseBody fixtureHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, response);
            try {
                hideLoading();
                Fixture fixture = JsonParsers.ParserFixture(new JSONObject(
                        response));
                Date date = fixture.getDates();
                teamsDataList = getTeams(date.getMatchs());
                initListView(generateItemTeam());
                //Log.i(TAG, "Fixture Successfull");
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.i(TAG, "Fixture Error");
            }

        }

        public void onError(String response) {
            //Log.i(TAG, "error Fixture" + response);
            hideLoading();
        }
    };
    private RelativeLayout progressBarLay;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_notification);
        View view = findViewById(R.id.iv_menu_icon_actionbar);
        if (view != null)
            view.setVisibility(View.INVISIBLE);

        containerBanner = (RelativeLayout) findViewById(R.id.container_banner);

        //ADMOB
        bannerAdView = (AdView) findViewById(R.id.bannerView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);

        progressBarLay = (RelativeLayout) findViewById(R.id.progressbarlay);
        championshipTeamsListView = (ListView) findViewById(R.id.lv_championship_teams_selection_notification_activity);
        com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getApplicationContext())
                .getChampionships();
        initListView(generateItemChampionship());

    }

    private void initListView(List<Item> itemList) {
        if (championshipTeamsListView == null) {
            championshipTeamsListView = (ListView) findViewById(R.id.lv_championship_teams_selection_notification_activity);
        }
        if (itemList == null) {
            return;
        }
        this.itemList = itemList;
        if (championshipTeamsListView.getAdapter() instanceof HeaderViewListAdapter
                && ((HeaderViewListAdapter) championshipTeamsListView
                .getAdapter()).getWrappedAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) championshipTeamsListView
                    .getAdapter()).getWrappedAdapter();
            adapter.setItemsList(itemList);
            adapter.notifyDataSetChanged();

        } else {
            championshipTeamsListView.setAdapter(new AdapterGeneric(
                    getApplicationContext(), itemList));
        }

        championshipTeamsListView
                .setOnItemClickListener(new OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (SelectionNotificationActivity.this.itemList != null
                                && SelectionNotificationActivity.this.itemList
                                .get(position) instanceof ItemChampionship) {

                            com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                                    .getInstance(getApplicationContext());
                            showLoading();
                            resourcesMetegol
                                    .getConnWsFutebol()
                                    .getFixturePost(
                                            fixtureHandler,
                                            ((Championship) SelectionNotificationActivity.this.itemList
                                                    .get(position).getData())
                                                    .getId(),
                                            ((Championship) SelectionNotificationActivity.this.itemList
                                                    .get(position).getData())
                                                    .getDates().get(0).getId()
                                    );
                        }
                    }
                });
    }

    private List<Item> generateItemChampionship() {

        List<Item> itemList = new ArrayList<Item>();
        List<Championship> championshipsList = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                getApplicationContext()).getChampionships();
        if (championshipsList == null)
            return null;
        // for (Championship championship : championshipsList) {
        // ItemChampionship itemChampionship = new ItemChampionship();
        // itemChampionship.setData(championship);
        // itemList.add(itemChampionship);
        // }

        for (int i = 0; i < championshipsList.size(); i++) {
            Championship championship = new Championship();
            championship = championshipsList.get(i);
            championship.setPosition(i);
            ItemChampionship itemChampionship = new ItemChampionship();
            itemChampionship.setData(championship);
            itemList.add(itemChampionship);
        }

        return itemList;
    }

    private List<Item> generateItemTeam() {

        List<Item> itemList = new ArrayList<Item>();
        if (teamsDataList == null)
            return null;
        // for (Team team : teamsDataList) {
        // ItemTeam itemTeam = new ItemTeam();
        // team.setPosition(position);
        // itemTeam.setData(team);
        // itemList.add(itemTeam);
        // }

        for (int i = 0; i < teamsDataList.size(); i++) {
            Team team = new Team();
            team = teamsDataList.get(i);
            team.setPosition(i);
            ItemTeam itemTeam = new ItemTeam();
            itemTeam.setData(team);
            itemList.add(itemTeam);

        }

        return itemList;
    }

    private void showLoading() {
        if (progressBarLay != null) {
            progressBarLay.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if (progressBarLay != null) {
            progressBarLay.setVisibility(View.GONE);
        }
    }

    private void showDialogUpdateNotification() {
        Builder builder = new AlertDialog.Builder(
                SelectionNotificationActivity.this); // contex
        builder.setTitle(R.string.MenuOptNotificationKey);
        builder.setMessage(R.string.MessageFavoriteNotificationKey);
        builder.setPositiveButton(R.string.OKKey,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new UpdateNotification().execute();
                    }
                }
        ).setNegativeButton(R.string.CancelKey,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }
        );

        builder.show();

    }

    @Override
    public void onBackPressed() {
        if (teamsDataList == null) {
            super.onBackPressed();
        } else {
            showDialogUpdateNotification();
        }
    }

    private List<Team> getTeams(List<Game> games) {
        List<Team> teams = new ArrayList<Team>();
        com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(getApplicationContext());
        resourcesMetegol.resetListIdNotification();
        for (Game game : games) {
            Team team = game.getAwayTeam();
            if (team.isSubscribedForNotifications()) {
                resourcesMetegol.addIdTeamExistingOfNotification(team
                        .getId());
            }
            teams.add(team);
            team = game.getHomeTeam();
            if (team.isSubscribedForNotifications()) {
                resourcesMetegol.addIdTeamExistingOfNotification(team
                        .getId());
            }
            teams.add(team);
        }
        return teams;
    }

    private class UpdateNotification extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(
                    SelectionNotificationActivity.this, "",
                    getString(R.string.LoadingKey));
        }

        @Override
        protected Void doInBackground(Void... params) {
            com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                    .getInstance(SelectionNotificationActivity.this
                            .getApplicationContext());
            com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                    .getConnWsFutebol();
            String deviceToken = GoogleCloudMessagingUtilities
                    .getRegistrationId(getApplicationContext());
            if (deviceToken.trim().equalsIgnoreCase("")) {
                return null;
            }
            Set<Integer> updates = resourcesMetegol
                    .getIdTeamsToAddNotification();
            ConnectionResponse response = null;
            if (updates != null) {
                for (Integer teamId : updates) {
                    response = connectionsWSMetegol.setConfigPush(deviceToken,
                            "1", "" + teamId);
                    try {
                        //Log.i(TAG, new String(response.getBodyResponse(), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            updates = resourcesMetegol.getIdTeamsToRemoveNotification();
            if (updates != null) {

                for (Integer teamId : updates) {
                    response = connectionsWSMetegol.setConfigPush(deviceToken,
                            "0", "" + teamId);
                    try {
                        //Log.i(TAG, new String(response.getBodyResponse(), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog != null)
                progressDialog.dismiss();

            finish();
        }
    }

}
