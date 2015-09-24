package com.pmovil.soccer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.components.AdapterGeneric;
import com.components.Item;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pmovil.soccer.MenuFragment.Options;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.CacheManager;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FixtureListView extends RelativeLayout {

    public static final String EXTRA_DATEINDEX = "EXTRA_IMAGE";
    private static final String TAG = "ListFragment";
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = null;
    private PullToRefreshListView lvFixtureDate = null;
    private List<Item> itemsList;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol;
    private List<Date> dates = null;
    private int indexCurrentDates = 0;
    private FixtureListListener mFixtureListener;
    private Context context;
    private boolean inFocus = false;
    private boolean inPullToRefresh = false;
    private ConnectionHandlerResponseBody fixtureAutoRefreshHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            if (inPullToRefresh) {
                return;
            }
            processSatisfactoryProcess(response);
        }

        public void onError(String response) {
            if (inPullToRefresh) {
                return;
            }
            lvFixtureDate.onRefreshComplete();
            //Log.i(TAG, "error Fixture" + response);
        }
    };
    private ConnectionHandlerResponseBody fixturePullRefreshHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            processSatisfactoryProcess(response);
            inPullToRefresh = false;

        }

        public void onError(String response) {

            inPullToRefresh = false;
            lvFixtureDate.onRefreshComplete();
            //Log.i(TAG, "error Fixture" + response);
            reportDataLoadedChanged(false);

        }
    };

    public FixtureListView(Context context, AttributeSet attrs, int dateIndex) {
        super(context, attrs);
        this.context = context;
        indexCurrentDates = dateIndex;
        init();
    }

    public FixtureListView(Context context, int dateIndex,
                           FixtureListListener listener) {
        super(context);
        this.context = context;
        indexCurrentDates = dateIndex;
        this.mFixtureListener = listener;
        init();
    }

    public FixtureListView(Context context, int dateIndex) {
        super(context);
        this.context = context;
        indexCurrentDates = dateIndex;
        init();
    }

    private void init() {

        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context);
        connWsFutebol = resourcesMetegol.getConnWsFutebol();
        itemsList = new ArrayList<Item>();

        lvFixtureDate = new PullToRefreshListView(getContext());
        ListView listView = lvFixtureDate.getRefreshableView();

        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.requestFocus(0);
        listView.setDividerHeight(0);

        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        addView(lvFixtureDate, params);
        lvFixtureDate.setOnRefreshListener(new OnRefreshListener<ListView>() {

            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        initList();
        Championship championshipByIdSelected = resourcesMetegol
                .getChampionshipByIdSelected();
        if (championshipByIdSelected != null) {
            dates = resourcesMetegol.getChampionshipByIdSelected()
                    .getDates();
            if (indexCurrentDates == 0)
                checkDate();
        }
    }

    private void initList() {

        ListView listView = lvFixtureDate.getRefreshableView();
        if (listView == null) {
            return;
        }
        if (listView.getAdapter() instanceof HeaderViewListAdapter) {
            if (((HeaderViewListAdapter) listView.getAdapter())
                    .getWrappedAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) listView
                        .getAdapter()).getWrappedAdapter();
                adapter.setItemsList(itemsList);
                adapter.notifyDataSetChanged();
            } else {
                if (context != null)
                    listView.setAdapter(new AdapterGeneric(context, itemsList));
            }
        } else {
            if (context != null)
                listView.setAdapter(new AdapterGeneric(context, itemsList));
        }

        lvFixtureDate.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {

                Object gameObject = parent.getAdapter().getItem(pos);
                if (gameObject instanceof Game) {
                    Game game = (Game) gameObject;
                    if (game.getState().getId() > 0
                            && game.getState().getId() != 4
//							&& game.getState().getId() != 100
                            && game.getState().getId() != 3) {
                        resourcesMetegol.clearGame();
                        resourcesMetegol.setGameSelected(game);
                        resourcesMetegol.setGameSelectedId(game.getId());

                        if(game.getDetails()==1){

                            if (context instanceof MainActivity) {
                                //((MainActivity) context).refreshMenu();
                                ((MainActivity) context).menuFragment
                                        .changeItemFromButton(Options.GAME);
                            }
                        }else{
                            Toast.makeText(context, getResources().getString(R.string.NoDataKey), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    public void initAutoRefreshProcess() {
        inFocus = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                if (inFocus) {
                    loadDataSimpleAutoRefresh();
                    handler.postDelayed(this, 1000 * 60);
                }
            }
        }, 1000 * 60);
    }

    private void loadDataSimpleAutoRefresh() {
        loadDataSimple(fixtureAutoRefreshHandler);
    }

    private void loadDataSimple() {
        inPullToRefresh = true;
        loadDataSimple(fixturePullRefreshHandler);
    }

    private void loadDataSimple(ConnectionHandlerResponseBody fixtureHandler) {
        connWsFutebol.getFixturePost(fixtureHandler,
                resourcesMetegol.getIdChampionshipSelected(),
                dates.get(indexCurrentDates).getId());
    }

    private void loadData() {

        if (mFixtureListener != null)
            mFixtureListener.onDataStartToDownload();
        lvFixtureDate.setVisibility(View.GONE);
        loadDataSimple();
    }

    public void checkDate() {
        if (dates != null && dates.get(indexCurrentDates).getMatchs() == null) {
            loadData(); // primera vez por pag
        } else if (dates != null) {
            loadDataListAdapter();
        }
    }

    private void processSatisfactoryProcess(String response) {
        //Log.i(TAG, response);
        try {
            Fixture fixture = JsonParsers
                    .ParserFixture(new JSONObject(response));
            CacheManager.getInstance(FixtureListView.this.getContext())
                    .setResponseFixture(FixtureListView.this.getContext(),
                            response);
            Date date = fixture.getDates();
            dates.get(indexCurrentDates).setMatchs(date.getMatchs());
            loadDataListAdapter();
            //Log.i(TAG, "Fixture Successfull");
            reportDataLoadedChanged(true);

        } catch (Exception e) {
            e.printStackTrace();
            //Log.i(TAG, "Fixture Error");
            reportDataLoadedChanged(false);
        }
        lvFixtureDate.onRefreshComplete();
    }

    private void loadDataListAdapter() {
        if (dates == null || lvFixtureDate == null)
            return;
        itemsList.clear();
        for (int i = 0; dates.get(indexCurrentDates).getMatchs() != null
                && i < dates.get(indexCurrentDates).getMatchs().size(); i++) {
            Game game = dates.get(indexCurrentDates).getMatchs().get(i);
            Item item = new ItemFixtureGame();
            item.setData(game);
            itemsList.add(item);

        }
        ListView listView = lvFixtureDate.getRefreshableView();
        if (listView.getAdapter() instanceof HeaderViewListAdapter) {
            if (((HeaderViewListAdapter) listView.getAdapter())
                    .getWrappedAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) listView
                        .getAdapter()).getWrappedAdapter();
                adapter.setItemsList(itemsList);
                adapter.notifyDataSetChanged();
            } else {
                if (context != null)
                    listView.setAdapter(new AdapterGeneric(context, itemsList));
            }
        } else {
            if (context != null)
                listView.setAdapter(new AdapterGeneric(context, itemsList));
        }
    }

    public void setFixtureListListener(FixtureListListener listen) {
        mFixtureListener = listen;
    }

    private void reportDataLoadedChanged(Boolean isOk) {
        if (mFixtureListener != null) {
            if (isOk) {
                mFixtureListener.onDataLoaded();
            } else {
                mFixtureListener.onDataFailed();
            }
            lvFixtureDate.setVisibility(View.VISIBLE);

        }
    }

    public boolean isInFocus() {
        return inFocus;
    }

    public void setInFocus(boolean inFocus) {
        this.inFocus = inFocus;
    }

    public interface FixtureListListener {
        public void onDataLoaded();

        public void onDataStartToDownload();

        public void onDataFailed();
    }

}
