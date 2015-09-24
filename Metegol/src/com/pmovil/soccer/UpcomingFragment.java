package com.pmovil.soccer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pmovil.soccer.DownloadUpcoming.DownloadOperationResonseHandler;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UpcomingFragment extends FragmentBase {

    private static final String TAG = "UpcomingFragment";
    private View view = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = null;
    private PullToRefreshListView upcomingList;
    ConnectionHandlerResponseBody handlerResponseBody = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {

            //Log.i(TAG, response);

            Fixture fixture;
            if (upcomingList != null) {
                upcomingList.onRefreshComplete();
            }
            try {
                fixture = JsonParsers.ParserFixture(new JSONObject(response));
                Date date = fixture.getDates();
                if ((date == null || date.getMatchs() == null)
                        && fixture.getDatesList() == null) {
                    hideLoading();

                    return;
                }
                if (date != null) {
                    matchs.addAll(date.getMatchs());
                } else if (fixture.getDatesList() != null) {
                    for (Date dates : fixture.getDatesList()) {
                        matchs.addAll(dates.getMatchs());
                    }
                }

                loadDataListAdapter();
                hideLoading();

            } catch (Exception e) {
                e.printStackTrace();
                hideLoading();

            }

        }

        public void onError(String response) {
            hideLoading();
            if (upcomingList != null) {
                upcomingList.onRefreshComplete();
            }
            Activity act = getActivity();

            if (act != null && act instanceof MainActivity) {
                ((MainActivity) act).noConnection();
            }
        }
    };
    private DownloadOperationResonseHandler downloadOperationResonseHandler = new DownloadOperationResonseHandler() {

        public void onSuccess() {
            loadDataListAdapter();
            if (upcomingList != null) {
                upcomingList.onRefreshComplete();
            }
            hideLoading();
        }

        public void onError(int error) {
            hideLoading();
            if (upcomingList != null) {
                upcomingList.onRefreshComplete();
            }
            Activity act = getActivity();

            if (act != null && act instanceof MainActivity) {
                if (error == DownloadUpcoming.NO_CONNECTION) {
                    ((MainActivity) act).noConnection();
                } else {
                    ((MainActivity) act).noData();

                }
            }
        }
    };
    private List<Item> itemList;
    private int page = 1;
    private List<Game> matchs = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = 1;
        hideLoading();
        if (matchs != null) {
            matchs.clear();
        }
        matchs = new ArrayList<Game>();

        if (resourcesMetegol == null) {
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());
        }
        if (connectionsWSMetegol == null) {
            connectionsWSMetegol = resourcesMetegol.getConnWsFutebol();
        }
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.upcoming_fragment, null);
        upcomingList = (PullToRefreshListView) view
                .findViewById(R.id.upcoming_list);
        upcomingList.setMode(Mode.PULL_FROM_END);

        upcomingList.setOnRefreshListener(new OnRefreshListener<ListView>() {

            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                showLoading();
                page++;
                connectionsWSMetegol.getUpcomingPost(handlerResponseBody,
                        resourcesMetegol.getIdChampionshipSelected(),
                        resourcesMetegol.getGameSelectedId(), page);

            }
        });
        upcomingList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                Object gameObject = parent.getAdapter().getItem(pos);
                if (gameObject instanceof Game) {
                    Game game = (Game) gameObject;
                    if (game.getState() != null
                            && game.getState().getId() == 100

                            ) {
                        resourcesMetegol.clearGame();
                        resourcesMetegol.setGameSelected(game);
                        resourcesMetegol.setGameSelectedId(game.getId());
                        /*
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).refreshMenu();
                            ((MainActivity) getActivity()).menuFragment
                                    .changeItemFromButton(Options.TEAM);
                        }*/
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (matchs != null && matchs.isEmpty()) {
            showLoading();
            connectionsWSMetegol.getUpcomingPost(handlerResponseBody,
                    resourcesMetegol.getIdChampionshipSelected(),
                    resourcesMetegol.getGameSelectedId(), page);
        }

    }

    private List<Item> generateListItems() throws Exception {
        Context context = getActivity();

        if (context == null) {
            return null;
        }

        List<Item> itemList = new ArrayList<Item>();
        List<Game> games = new ArrayList<Game>();

        for (Game game : matchs) {

            if (game != null) {

                // Especificar indicando año, mes, dia, horas, minutos,
                // segundos y milisegundos
                DateTime dateOfPlay = new DateTime((game.getDate() / 10000),
                        (game.getDate() % 10000) / 100,
                        ((game.getDate() % 10000) % 100), 0, 0, 0, 0);
                game.setDateOfPlay(dateOfPlay);
                games.add(game);
            }
        }

        Collections.sort(games, new Comparator<Game>() {
            public int compare(Game m1, Game m2) {
                return m1.getDateOfPlay().compareTo(m2.getDateOfPlay());
            }
        });
        if (games.isEmpty()) {
            return null;
        }

        DateTime dateTime = games.get(0).getDateOfPlay();
        String dateName = ""
                + getString(R.string.Semana)
                + " "
                + dateTime.withDayOfWeek(DateTimeConstants.MONDAY).minusDays(1)
                .toString("dd-MM-yyyy")

                + " | "
                + dateTime.withDayOfWeek(DateTimeConstants.SUNDAY).toString(
                "dd-MM-yyyy");

        Item itemHeader = new ItemUpcomingHeader();
        itemHeader.setData(dateName);
        itemList.add(itemHeader);
        int prevWeek = games.get(0).getDateOfPlay().getWeekOfWeekyear();
        for (int i = 0; i < games.size(); i++) {
            dateTime = games.get(i).getDateOfPlay();
            if (prevWeek != dateTime.getWeekOfWeekyear()) {
                prevWeek = dateTime.getWeekOfWeekyear();
                dateName = ""
                        + getString(R.string.Semana)
                        + " "
                        + dateTime.withDayOfWeek(DateTimeConstants.MONDAY)
                        .minusDays(1).toString("dd-MM-yyyy")
                        + " | "
                        + dateTime.withDayOfWeek(DateTimeConstants.SUNDAY)
                        .toString("dd-MM-yyyy");
                itemHeader = new ItemUpcomingHeader();
                itemHeader.setData(dateName);
                itemList.add(itemHeader);

            }
            Item item = new ItemFixtureGame();
            item.setData(games.get(i));
            itemList.add(item);
        }

        return itemList;

    }

    private void noDataMessage() {
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).noData();
    }

    private void loadDataListAdapter() {

        try {
            itemList = generateListItems();
        } catch (Exception e) {
            e.printStackTrace();
            noDataMessage();
            return;
        }
        if (upcomingList == null || upcomingList.getRefreshableView() == null) {
            noDataMessage();

            return;
        }
        ListView listView = upcomingList.getRefreshableView();

        if (listView.getAdapter() instanceof HeaderViewListAdapter) {
            if (((HeaderViewListAdapter) listView.getAdapter())
                    .getWrappedAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) listView
                        .getAdapter()).getWrappedAdapter();
                adapter.setItemsList(itemList);
                adapter.notifyDataSetChanged();
            } else {
                if (getActivity() != null)
                    upcomingList.setAdapter(new AdapterGeneric(getActivity(),
                            itemList));
            }
        } else {
            if (getActivity() != null)
                upcomingList.setAdapter(new AdapterGeneric(getActivity(),
                        itemList));
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (matchs != null) {
            matchs.clear();
            matchs = null;
        }
        //Log.i(TAG, "DestroyView");
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

}
