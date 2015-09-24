package com.pmovil.soccer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.components.AdapterGeneric;
import com.components.Item;
import com.components.TextViewPlus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.Player;
import com.pmovil.soccer.entities.PlayerTweet;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilePlayerActivity extends Activity {

    public static final String PLAYER = "player";
    public static final int BIOGRAPHY = 0;
    public static final int TWITTER = 1;
    private static final String TAG = "FilePlayerActivity";
    public RelativeLayout containerBanner;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Player playerData;
    private ImageView barBiogrphySelected;
    private ImageView barTwitterSelected;
    private TextViewPlus tvBiography;
    private TextViewPlus tvTwitter;
    private RelativeLayout containerTwitterPlayer;
    private TextView mentions;
    private int cantMentions = 0;
    private ListView lvTwitter = null;
    private List<Item> itemsList = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol ResourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsMetegol = null;
    private ViewFlow viewFlow;
    private int page = 0;
    private View containerPreloader = null;
    private AdView bannerAdView;
    private ConnectionHandlerResponseBody twitterHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                ResourcesMetegol.setPlayerTweet(JsonParsers
                        .ParserPlayerTweets(new JSONObject(response)));
                loadDataListAdapter();
                //Log.i(TAG, "Position Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            hideLoading();
            // setBlockSlidingMenu(false);

        }

        public void onError(String response) {
            //Log.i(TAG, "error Fixture" + response);
            // setBlockSlidingMenu(false);
            hideLoading();
            Activity act = FilePlayerActivity.this;
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_player);
        Serializable serializable = getIntent().getSerializableExtra(PLAYER);
        if (serializable instanceof Player) {
            playerData = (Player) serializable;
        }

        hideLoading();

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

        barBiogrphySelected = (ImageView) findViewById(R.id.bar_biography_selected);
        barTwitterSelected = (ImageView) findViewById(R.id.bar_twitter_selected);
        tvBiography = (TextViewPlus) findViewById(R.id.tv_biography);
        tvTwitter = (TextViewPlus) findViewById(R.id.tv_twitter);
       // TODO hidde
        tvTwitter.setVisibility(View.INVISIBLE);

        containerTwitterPlayer = (RelativeLayout) findViewById(R.id.container_subtitle_twitter_playyer);

        mentions = (TextView) findViewById(R.id.tv_cant_mentions);
        containerPreloader = findViewById(R.id.container_preloader);

        View view = findViewById(R.id.iv_menu_icon_actionbar);
        if (view != null)
            view.setVisibility(View.INVISIBLE);

        if (ResourcesMetegol == null) {
            ResourcesMetegol = ResourcesMetegol
                    .getInstance(FilePlayerActivity.this);
        }
        connWsMetegol = ResourcesMetegol.getConnWsFutebol();
        lvTwitter = new ListView(FilePlayerActivity.this);
        lvTwitter.setCacheColorHint(Color.TRANSPARENT);

        viewFlow = (ViewFlow) findViewById(R.id.viewflow);
        viewFlow.setAdapter(new Adapter());
        viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {

            public void onSwitched(View view, int pos) {

                if (page <= 1) {

                    page = pos;
                }
                initSubmenu();
                checkDataAndLoadAdapter();

            }
        });
        checkDataAndLoadAdapter();
        initSubmenu();
        //

    }

    private void initSubmenu() {
        switch (page) {
            default:
            case BIOGRAPHY:
                containerTwitterPlayer.setVisibility(View.GONE);
                barBiogrphySelected.setVisibility(View.VISIBLE);
                barTwitterSelected.setVisibility(View.INVISIBLE);
                tvBiography.setTextColor(getResources().getColor(
                        R.color.blue_selected));
                tvTwitter.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));

                tvBiography.setCustomFont(getBaseContext(), getString(R.string.font_name_bold));
                tvTwitter.setCustomFont(getBaseContext(), getString(R.string.font_name));

                break;
            /*
            case TWITTER:
                containerTwitterPlayer.setVisibility(View.VISIBLE);
                barBiogrphySelected.setVisibility(View.INVISIBLE);
                barTwitterSelected.setVisibility(View.VISIBLE);
                tvBiography.setTextColor(getResources().getColor(
                        R.color.blue_not_selected));
                tvTwitter.setTextColor(getResources().getColor(
                        R.color.blue_selected));

                tvBiography.setCustomFont(getBaseContext(), getString(R.string.font_name));
                tvTwitter.setCustomFont(getBaseContext(), getString(R.string.font_name_bold));

                break;
                */
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        if (viewFlow != null) {
            switch (page) {
                case BIOGRAPHY:
                    viewFlow.setSelection(BIOGRAPHY);
                    break;
            /*
                case TWITTER:
                    viewFlow.setSelection(TWITTER);
                    break;
            */
                default:
                    viewFlow.setSelection(BIOGRAPHY);
                    break;
            }
            // initSubmenu();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Activity act = FilePlayerActivity.this;
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);

        switch (page) {
            default:

            case BIOGRAPHY:
                viewFlow.setSelection(BIOGRAPHY);
                break;
            /*
            case TWITTER:
                viewFlow.setSelection(TWITTER);
                break;
            */
        }

    }

    private void checkDataAndLoadAdapter() {

        switch (page) {
            case TWITTER:
                if (ResourcesMetegol.getPlayerTweet() == null) {
                    connWsMetegol.getPlayerTwitterPost(twitterHandler, ""
                                    + playerData.getId(),
                            "" + ResourcesMetegol.getGameSelectedId()
                    );
                    showLoading();
                    // setBlockSlidingMenu(true);

                } else
                    loadDataListAdapter();
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        ResourcesMetegol.setPlayerTweet(null);
    }

    private void loadDataListAdapter() {
        generateItemTimeLine();
        ListView listView = null;
        listView = lvTwitter;
        if (itemsList.isEmpty()) {
            Activity act = FilePlayerActivity.this;
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noData();
        }

        if (listView.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) listView.getAdapter();
            adapter.setItemsList(itemsList);
            adapter.notifyDataSetChanged();
            listView.refreshDrawableState();
            //Log.i(TAG, "Update");
        } else
            listView.setAdapter(new AdapterGeneric(FilePlayerActivity.this,
                    itemsList));
    }

    private void generateItemTimeLine() {
        if (itemsList == null)
            itemsList = new ArrayList<Item>();
        itemsList.clear();
        List<PlayerTweet> playerTweets = ResourcesMetegol.getPlayerTweet();
        if (playerTweets == null)
            return;
        for (int indexTeams = 0; indexTeams < playerTweets.size(); indexTeams++) {
            PlayerTweet playerTweet = playerTweets.get(indexTeams);
            Item item = new ItemFilePlayerTwitter();
            item.setData(playerTweet);
            itemsList.add(item);
            if (playerTweet.getPlayerMentions() != null
                    && !playerTweet.getPlayerMentions().equalsIgnoreCase("")) {
                cantMentions = Integer
                        .parseInt(playerTweet.getPlayerMentions());
            }

        }
        mentions.setText(getString(R.string.Mentions_count) + ": "
                + cantMentions);
    }

    public void showLoading() {
        if (findViewById(R.id.container_viewflow) != null)
            findViewById(R.id.container_viewflow).setVisibility(View.GONE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        if (findViewById(R.id.container_viewflow) != null)
            findViewById(R.id.container_viewflow).setVisibility(View.VISIBLE);
        if (containerPreloader != null) {
            containerPreloader.setVisibility(View.GONE);
        }
    }

    private class Adapter extends BaseAdapter {

        View biographyView;

        public Adapter() {
            Biography bio = new Biography(FilePlayerActivity.this, playerData);
            biographyView = bio.getViewBiography();
        }

        public int getCount() {
            return 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                default:
                case BIOGRAPHY:
                    return biographyView;
               /*
                case TWITTER:
                    return lvTwitter;
                */
            }
        }

    }

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		EasyTracker.getInstance(this).activityStart(this);
//	}
//	
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		EasyTracker.getInstance(this).activityStop(this);
//	}

}
