package com.pmovil.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.components.AdapterGeneric;
import com.components.AnimateFirstDisplayListener;
import com.components.ImageUtil;
import com.components.Item;
import com.components.TextViewPlus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pmovil.soccer.entities.News;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends FragmentBase {

    private static final String TAG = "NewsFragment";
    private PullToRefreshListView pullRefreshNews = null;
    private List<Item> itemList = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = null;
    private View view = null;
    private int start = 10;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private static DisplayImageOptions options;
    private ConnectionHandlerResponseBody addMoreNewsHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                resourcesMetegol.getNewsList().addAll(
                        JsonParsers.ParserNews(new JSONObject(response)));

                loadListAdapter();
                //Log.i(TAG, "News Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            pullRefreshNews.onRefreshComplete();
            //Log.i(TAG, response);
        }

        public void onError(String response) {
            //Log.i(TAG, "error News" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            pullRefreshNews.onRefreshComplete();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private ConnectionHandlerResponseBody newsHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                resourcesMetegol.setNewsList(JsonParsers
                        .ParserNews(new JSONObject(response)));

                loadListAdapter();
                //Log.i(TAG, "News Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "error News" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                    .getInstance(getActivity());
        if (connWsFutebol == null)
            connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.news_fragment, container, false);
        pullRefreshNews = (PullToRefreshListView) view
                .findViewById(R.id.lv_news);
        pullRefreshNews.getRefreshableView().setDividerHeight(20);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkDataAndLoadAdapter();
    }

    private void checkDataAndLoadAdapter() {

        if (resourcesMetegol.getNewsList() == null) {
            connWsFutebol.getNewsPost(newsHandler,
                    resourcesMetegol.getIdChampionshipSelected());
            showLoading();
            setBlockSlidingMenu(true);
        } else {
            loadListAdapter();
        }
    }

    private void loadListAdapter() {

        //FBARBIERI ADD HEADER TO NEWS LIST.
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View header_news = (View) inflater.inflate(R.layout.news_header, pullRefreshNews, false);

        ImageView image_new = (ImageView) header_news.findViewById(R.id.iv_news);
        TextViewPlus title_new = (TextViewPlus) header_news.findViewById(R.id.tv_title_news);
        TextViewPlus desc_new = (TextViewPlus) header_news.findViewById(R.id.tv_desc_news);

        ListView lvNews = pullRefreshNews.getRefreshableView();
        header_news.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));

        if (pullRefreshNews == null)
            return;
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();
        List<News> newsList = resourcesMetegol.getNewsList();
        if (newsList == null)
            return;

        //FBARBIERI for (int indexNews = 0; indexNews < newsList.size(); indexNews++) {
        for (int indexNews = 1; indexNews < newsList.size(); indexNews++) {
            Item item = new ItemNews();
            item.setData(newsList.get(indexNews));
            itemList.add(item);

        }

        if (itemList.isEmpty()) {
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noNewsData();
        }

        //FBARBIERI SETTING DATA TO LISTVIEW HEADER
        int width = image_new.getLayoutParams().width;
        int height = (int) (image_new.getLayoutParams().height - ImageUtil
                .getPixel(getActivity().getApplicationContext(), 3));

        String urlEmblem = newsList.get(0).getImage().replace(
                com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);

        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageForEmptyUri(R.drawable.logo_3)
                .showImageOnFail(R.drawable.logo_3).cacheInMemory()
                .cacheOnDisc().build();
        imageLoader.displayImage(urlEmblem, image_new,
                options, animateFirstListener);

        title_new.setText(newsList.get(0).getTitle());
        desc_new.setText(newsList.get(0).getDescription());

        lvNews.addHeaderView(header_news);

        if (lvNews.getAdapter() instanceof HeaderViewListAdapter) {
            if (((HeaderViewListAdapter) lvNews.getAdapter())
                    .getWrappedAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) lvNews
                        .getAdapter()).getWrappedAdapter();
                adapter.setItemsList(itemList);
                adapter.notifyDataSetChanged();
            } else {
                Context context = getActivity();
                if (context != null)
                    lvNews.setAdapter(new AdapterGeneric(context, itemList));
            }
        } else {
            Context context = getActivity();
            if (context != null)
                lvNews.setAdapter(new AdapterGeneric(context, itemList));
        }
        pullRefreshNews.setOnRefreshListener(new OnRefreshListener<ListView>() {

            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                connWsFutebol
                        .getNewsPost(addMoreNewsHandler, resourcesMetegol
                                .getIdChampionshipSelected(), start);
                start += 10;
            }
        });
        pullRefreshNews.setMode(Mode.PULL_FROM_END);
        pullRefreshNews.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                List<News> newsList = resourcesMetegol.getNewsList();
                if (newsList == null)
                    return;
                pos--;
                if (newsList.size() > pos) {
                    String url = newsList.get(pos).getLink();
                    if (url != null && !url.equalsIgnoreCase("")
                            && !url.equalsIgnoreCase("null")) {
                        Intent intent = new Intent(getActivity(),
                                NewsWebActivity.class);
                        intent.putExtra(NewsWebActivity.URL, url);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

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
