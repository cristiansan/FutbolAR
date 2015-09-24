package com.pmovil.soccer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import com.pmovil.soccer.Adapters.GridViewAdapter;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.CacheManager;
import com.pmovil.soccer.net.ManagerConnection;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SelectTeamActivity extends Activity {

    private GridView gv_teams;
    private List<Team> teamsList = new ArrayList<Team>();
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol;
    private ManagerConnection.ConnectionHandlerResponseBody fixtureHandler = new ManagerConnection.ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            processSatisfactoryProcess(response);
        }

        public void onError(String response) {
            Log.d("SelectTeamActivity", response);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);

        gv_teams = (GridView) findViewById(R.id.gv_select_team);

        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getBaseContext());
        Championship currentChampionship = resourcesMetegol.getChampionshipByIdSelected();
        List<Date> dates = currentChampionship.getDates();
        Date date = dates.get(currentChampionship.getCurrentDateIndex());

        if (date.getMatchs()!= null && date.getMatchs().size() > 0) {

            getTeamsFromDate(date);

        } else {

            connWsFutebol = resourcesMetegol.getConnWsFutebol();
            connWsFutebol.getFixturePost(fixtureHandler,
                    resourcesMetegol.getIdChampionshipSelected(),
                    date.getId());

        }

    }

    private void getTeamsFromDate(Date date) {
        List<Game> gamesList = date.getMatchs();

        for (int i = 0; i < gamesList.size(); i++) {

            Team homeTeam = gamesList.get(i).getHomeTeam();
            Team awayTeam = gamesList.get(i).getAwayTeam();

            if (teamsList == null || !teamsList.contains(homeTeam)) {

                teamsList.add(homeTeam);
            }
            if (teamsList == null || !teamsList.contains(awayTeam)) {

                teamsList.add(awayTeam);
            }
        }

        GridViewAdapter gvAdapter = new GridViewAdapter(this, teamsList);
        gv_teams.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();
    }

    private void processSatisfactoryProcess(String response) {
        //Log.i(TAG, response);
        try {
            Fixture fixture = JsonParsers
                    .ParserFixture(new JSONObject(response));
            CacheManager.getInstance(getBaseContext()).setResponseFixture(getBaseContext(), response);
            Date date = fixture.getDates();
            getTeamsFromDate(date);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SelectTeamActivity", e.getMessage());
        }
    }
}
