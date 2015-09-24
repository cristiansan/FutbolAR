package com.pmovil.soccer.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.FragmentBase;
import com.pmovil.soccer.ItemSummary;
import com.pmovil.soccer.MainActivity;
import com.pmovil.soccer.R;
import com.pmovil.soccer.entities.Statistic;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ResumenFragment extends FragmentBase {

    private static final String TAG = "StaticsFragment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private View view = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol;
    private ListView lvResumen = null;
    private List<Item> itemList = null;
    private View loadingAway = null;
    private View loadingHome = null;
    private boolean isSecondTime = false;
    private JSONObject responseJSONStats = null;
    private int minutes = 0;
    private AdView bannerAdView;

    private ConnectionHandlerResponseBody statisticsSummaryHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, "Summary Successfull " + response);

            try {
                resourcesMetegol.setStatisticSummaryList(
                        JsonParsers.ParserStatisticSummary(
                                new JSONObject(response), responseJSONStats, getActivity()
                        )
                );
                responseJSONStats = null;

                loadListAdapterAndViewsData();
            } catch (Exception e) {
                e.printStackTrace();

            }
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "Error summary" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private ConnectionHandlerResponseBody statisticsHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, "Statistics Successfull " + response);

            try {

                responseJSONStats = new JSONObject(response);
                resourcesMetegol.setStatisticList(
                        JsonParsers.ParserStatistic(
                                responseJSONStats, getActivity()
                        )
                );

                connWsFutebol.getStatsSummaryGamePost(
                        statisticsSummaryHandler,
                        resourcesMetegol.getIdChampionshipSelected(),
                        resourcesMetegol.getGameSelectedId()
                );

            } catch (Exception e) {
                e.printStackTrace();
                hideLoading();
                setBlockSlidingMenu(false);
            }

        }

        public void onError(String response) {
            //Log.i(TAG, "error Statistics" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private TextView tvDateTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                    .getInstance(getActivity());
        if (connWsFutebol == null)
            connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.resumen_fragment, container, false);

        lvResumen = (ListView) view.findViewById(R.id.lv_resumen);
        //ADMOB
        bannerAdView = (AdView) view.findViewById(R.id.bannerView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("942EC2AA2A735DFBD71A9EEF70692CE7")
                .build();
        // Start loading the ad in the background.
        bannerAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkDataAndLoadAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshMenu();
        }*/

    }

    private void checkDataAndLoadAdapter() {

        // if (resourcesMetegol.getStatisticList() == null) {
        connWsFutebol.getStatsGamePost(statisticsHandler,
                resourcesMetegol.getIdChampionshipSelected(),
                resourcesMetegol.getGameSelectedId());
        showLoading();
        setBlockSlidingMenu(true);
        // } else {
        // loadListAdapterAndViewsData();
        // }
    }

    private void loadListAdapterAndViewsData() {
        if (lvResumen == null) {
            //Log.e(TAG, "lvStatistics == null");

            return;
        }
        lvResumen.setDividerHeight(0);
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();

        //loadStatus();

        List<Statistic> statisticList = new ArrayList<Statistic>();
        statisticList.addAll(resourcesMetegol.getStatisticSummaryList());
        //statisticList.addAll(resourcesMetegol.getStatisticList());

        if (statisticList == null) {
            //Log.e(TAG, "statisticList == null");
            return;
        }

        for (int indexStatitics = 0; indexStatitics < statisticList.size(); indexStatitics++) {
            Statistic statistic = statisticList.get(indexStatitics);
            statistic.setPos(indexStatitics);

                Item item = new ItemSummary();
                item.setData(statistic);
                itemList.add(item);
        }

        //To display banner
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(R.layout.listview_footer, null);
        lvResumen.addFooterView(footer);

        if (lvResumen.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) lvResumen.getAdapter();
            adapter.setItemsList(itemList);
            adapter.notifyDataSetChanged();
        } else

            lvResumen.setAdapter(new AdapterGeneric(getActivity(), itemList));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.i(TAG, "DestroyView");
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }
}
