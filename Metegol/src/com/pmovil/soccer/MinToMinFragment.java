package com.pmovil.soccer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.components.AdapterGeneric;
import com.components.Item;
import com.components.ShareData;
import com.components.TextViewPlus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pmovil.soccer.entities.Comment;
import com.pmovil.soccer.entities.Incidence;
import com.pmovil.soccer.entities.MinToMinData;
import com.pmovil.soccer.entities.Value;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MinToMinFragment extends FragmentBase {

    private static final String TAG = "MinToMinFragment";
    private static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private PullToRefreshListView lvMinToMin = null;
    private List<Item> itemList = null;
    private List<Item> itemCommentList = null;
    private ListView lvComments;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = null;
    private View view = null;
    private View no_comments = null;
    private Button btnComent;
    private TextViewPlus tvNoData;
    private EditText etComment;
    private RelativeLayout ltCommentsBar;
    private Context context;
    private int minToMinId = 0;
    private DialogShareComment dialog_comment;
    /*private TextView tvGoalHome = null;
    private TextView tvGoalAway = null;
    private ImageView ivEmblemHome = null;
    private ImageView ivEmblemAway = null;
    private TextView tvMinByMinStatusTeamAway = null;
    private TextView tvMinByMinStatusTeamHome = null;
    private View loadingAway = null;
    private View loadingHome = null;*/
    private Handler handler = null;
    private boolean inPullToRefresh = false;
    private RelativeLayout ltComment;
    private List<Comment> commentList = null;
    private ImageView ivComments;
    private AdView bannerAdView;
    private ConnectionHandlerResponseBody minToMinPullRefreshHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            processSatisfactoryProcess(response);
            inPullToRefresh = false;
        }

        public void onError(String response) {
            if (lvMinToMin != null)
                lvMinToMin.onRefreshComplete();
            //Log.i(TAG, "error MinToMin" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
            inPullToRefresh = false;

        }
    };
    private ConnectionHandlerResponseBody minToMinAutoRefreshHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {

//			try {
//				Toast.makeText(getActivity(), "REFRESH", Toast.LENGTH_LONG)
//						.show();
//				} catch (Exception e) {
//				}

            if (inPullToRefresh) {
                return;
            }
            processSatisfactoryProcess(response);
        }

        public void onError(String response) {
            if (inPullToRefresh) {
                return;
            }
            if (lvMinToMin != null)
                lvMinToMin.onRefreshComplete();
            //Log.i(TAG, "error MinToMin" + response);
            setBlockSlidingMenu(false);
            hideLoading();
            //loadStatus();
        }
    };
    ConnectionHandlerResponseBody responseGetCommentHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                commentList = JsonParsers.ParserComments(new JSONObject(
                        response));
                if (commentList == null) {
                    getActivity().finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                getActivity().finish();
            }

            loadCommentsListAdapter();
            ////Log.i(TAG, response);
        }

        public void onError(String response) {
            getActivity().finish();
            ////Log.i(TAG, response);

        }
    };
    ManagerConnection.ConnectionHandlerResponseBody responseAddCommentHandler = new ManagerConnection.ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            ////Log.i(TAG, response);
            // hideLoading();
            if (response.contains("OK")) {
                // Toast.makeText(getApplicationContext(),
                // "Comment Success. Update Comments..", Toast.LENGTH_LONG)
                // .show();
                com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                        .getConnWsFutebol();
                connectionsWSMetegol.getCommentsFromMatchAndMintoMinPost(
                        responseGetCommentHandler, matchId, minToMinId, 20);
            }

        }

        public void onError(String response) {
            ////Log.i(TAG, response);
            // Toast.makeText(getBaseContext(), "Update Error",
            // Toast.LENGTH_LONG)
            // .show();
        }
    };
    private boolean inFocus = false;
    private int matchId = 0;
    private Value status = null;
    private Runnable runnable = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        matchId = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity().getApplicationContext()).getGameSelectedId();
        context = getActivity().getApplicationContext();

        hideLoading();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());
        if (connWsFutebol == null)
            connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                    .cacheOnDisc().build();
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.min_to_min_fragment, container, false);
       /* tvGoalAway = (TextView) view.findViewById(R.id.tv_goals_away);
        tvGoalHome = (TextView) view.findViewById(R.id.tv_goals_home);
        ivEmblemAway = (ImageView) view.findViewById(R.id.iv_emblem_away);
        ivEmblemHome = (ImageView) view.findViewById(R.id.iv_emblem_home);
        tvMinByMinStatusTeamAway = (TextView) view
                .findViewById(R.id.tv_min_to_min_status_team_away_fragment);
        tvMinByMinStatusTeamHome = (TextView) view
                .findViewById(R.id.tv_min_to_min_status_team_home_fragment);
        loadingAway = view.findViewById(R.id.progressBarEmblemAway);
        loadingHome = view.findViewById(R.id.progressBarEmblemHome);*/
        lvMinToMin = (PullToRefreshListView) view
                .findViewById(R.id.lv_min_to_min);
        lvComments = (ListView) view.findViewById(R.id.lvComments);
        btnComent = (Button) view.findViewById(R.id.btnComment);
        etComment = (EditText) view.findViewById(R.id.et_comment);
        ltCommentsBar = (RelativeLayout) view.findViewById(R.id.comment_divider);
        ltComment = (RelativeLayout) view.findViewById(R.id.layout_comment);
        tvNoData = (TextViewPlus) view.findViewById(R.id.tv_no_data);
        ivComments = (ImageView) view.findViewById(R.id.iv_hide_comments);
        ivComments.setRotation(180);

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

        /*tvStatus = (TextView) view
                .findViewById(R.id.tv_status_min_to_min_fragment);*/

        ltCommentsBar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {

            if (ltComment.getVisibility()== View.GONE){

                ltComment.setVisibility(View.VISIBLE);
                ivComments.setRotation(0);
                connectAndLoadComment();

            }else{

                ltComment.setVisibility(View.GONE);
                ivComments.setRotation(180);
                loadMinByMinListAdapter();
            }

            }
        });

        btnComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = etComment.getText().toString();

                if(!comment.equals("") && comment.length()>0){

                    final SharedPreferences mSharedPreferences = getActivity().getApplicationContext()
                            .getSharedPreferences(ShareData.SHARE_NAME, 0);

                    if (mSharedPreferences.getString(ShareData.USER_ID_FACEBOOK, "").equalsIgnoreCase("")) {

                        DialogFragment newFragment = CommentDialog.newInstance(1);
                        newFragment.setTargetFragment(MinToMinFragment.this, 1);
                        Bundle args = new Bundle();
                        args.putBoolean("showText", true);
                        newFragment.setArguments(args);
                        newFragment.show(getFragmentManager(), "CommentDialog");

                    }else{
                        goComment(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);
                    }
                }
            }
        });

     view.setFocusableInTouchMode(true);
     view.requestFocus();
     view.setOnKeyListener( new View.OnKeyListener(){
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ){
                if( keyCode == KeyEvent.KEYCODE_BACK ){

                    if (ltComment.getVisibility()== View.VISIBLE) {

                        ltComment.setVisibility(View.GONE);
                        ivComments.setRotation(180);
                        loadMinByMinListAdapter();
                    }

                    return true;
                }
                return false;
            }
        } );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (runnable != null) {
            handler.sendEmptyMessageDelayed(0, 0000);
        }
        //Log.i(TAG, "DestroyView");
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkDataAndLoadAdapter();
        // lvMinToMin.

        // pos = lvMinToMin.getRefreshableView().getSelectedItemPosition();
        handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                if (inFocus) {
                    loadDataByAutoRefresh();
                    handler.postDelayed(this, 1000 * 60);
                }
            }
        }, 1000 * 60);
        //
        // lvMinToMin.setOnRefreshListener(new OnRefreshListener<ListView>() {
        //
        // public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        // connWsFutebol.getFootballGameMinToMinPost(minToMinHandler,
        // resourcesMetegol.getIdChampionshipSelected(),
        // resourcesMetegol.getGameSelectedId());
        // // showLoading();
        // setBlockSlidingMenu(true);
        // }
        // });

        lvMinToMin.setOnRefreshListener(new OnRefreshListener<ListView>() {

            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDataByPullToRefresh();
                showLoading();
                setBlockSlidingMenu(true);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        goComment(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK);

    }

    @Override
    public void onResume() {
        super.onResume();
        inFocus = true;
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity) {
            ((MainActivity) act).showIconTeam();
            //((MainActivity) getActivity()).refreshMenu();
        }
    }

    private void loadDataByAutoRefresh() {
        loadData(minToMinAutoRefreshHandler);
    }

    private void loadDataByPullToRefresh() {
        inPullToRefresh = true;
        loadData(minToMinPullRefreshHandler);

    }

    private void loadData(ConnectionHandlerResponseBody minToMinHandler) {
        if (connWsFutebol == null) {
            connWsFutebol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                    .getConnWsFutebol();
        }
        connWsFutebol.getFootballGameMinToMinPost(minToMinHandler,
                resourcesMetegol.getIdChampionshipSelected(),
                resourcesMetegol.getGameSelectedId());
    }

    private void checkDataAndLoadAdapter() {

        // connWsFutebol.getFootballGameMinToMinPost(minToMinHandler,
        // resourcesMetegol.getIdChampionshipSelected(),
        // resourcesMetegol.getGameSelectedId());

        loadDataByPullToRefresh();
        showLoading();
        setBlockSlidingMenu(true);
        // } else {
        // loadMinByMinListAdapter();
        // }
    }

    private void connectAndLoadComment() {

        com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                .getConnWsFutebol();
        connectionsWSMetegol.getCommentsFromMatchAndMintoMinPost(
                responseGetCommentHandler, matchId, minToMinId, 20);
    }

    private void loadCommentsListAdapter() {

        if (lvComments == null)
            return;
        lvComments.setDividerHeight(8);
        if (itemCommentList == null)
            itemCommentList = new ArrayList<Item>();
        itemCommentList.clear();

        if (commentList == null || commentList.size()==0) {

            tvNoData.setVisibility(View.VISIBLE);
            return;

        }else{

            tvNoData.setVisibility(View.GONE);

            for (int indexCommentList = 0; indexCommentList < commentList.size(); indexCommentList++) {
                Item item = new ItemComment();
                item.setData(commentList.get(indexCommentList));
                itemCommentList.add(item);

            }

            lvComments.setAdapter(new AdapterGeneric(getActivity().getApplicationContext(),
                    itemCommentList));

            /*
            if (lvComments.getAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) lvComments.getAdapter();
                adapter.setItemsList(itemList);
                adapter.notifyDataSetChanged();
            } else
                lvComments.setAdapter(new AdapterGeneric(getActivity().getApplicationContext(),
                        itemList));*/
        }
    }

    private void loadMinByMinListAdapter() {
        if (lvMinToMin == null)
            return;
        ListView listView = lvMinToMin.getRefreshableView();
        listView.setDividerHeight(0);
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();

        //loadStatus();

        MinToMinData minToMinData = resourcesMetegol.getMinToMinData();

        if (minToMinData == null) {
            //noDataAnimation();
            return;
        }
        List<Incidence> incidences = minToMinData.getIncidences();
        if (incidences == null) {
            //noData();
            return;
        }
        for (int indexIncidences = 0; indexIncidences < incidences.size(); indexIncidences++) {
            Incidence incidence = incidences.get(indexIncidences);
            Item item = new ItemMinByMin();
            item.setData(incidence);
            itemList.add(item);

        }

        if (itemList.isEmpty()) {
            //noData();
            return;
        }
        goneMesaggeData();
        if (listView.getAdapter() instanceof HeaderViewListAdapter) {
            if (((HeaderViewListAdapter) listView.getAdapter())
                    .getWrappedAdapter() instanceof AdapterGeneric) {
                AdapterGeneric adapter = (AdapterGeneric) ((HeaderViewListAdapter) listView
                        .getAdapter()).getWrappedAdapter();
                adapter.setItemsList(itemList);
                adapter.notifyDataSetChanged();
            } else {
                Context context = getActivity();
                if (context != null)
                    listView.setAdapter(new AdapterGeneric(context, itemList));
            }
        } else {
            Context context = getActivity();
            if (context != null)
                listView.setAdapter(new AdapterGeneric(context, itemList));
        }

    }

    private void goneMesaggeData() {
        Activity act = getActivity();
        if (act != null && act instanceof MainActivity)
            ((MainActivity) act).goneMessage();
    }

    private void processSatisfactoryProcess(String response) {
        try {

            MinToMinData minData = JsonParsers.ParserMinToMin(new JSONObject(
                    response));
            try {
                Collections.reverse(minData.getIncidences());
            } catch (Exception e) {

            }
            com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                    .setMinToMinData(minData);
            loadMinByMinListAdapter();
            //Log.i(TAG, "MintoMin Successfull");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.i(TAG, response);
        if (lvMinToMin != null)
            lvMinToMin.onRefreshComplete();
        hideLoading();
        setBlockSlidingMenu(false);
    }

    public void goComment(String socialNetwork) {

       resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(context);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);

        String msg = etComment.getText().toString();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                ShareData.SHARE_NAME, 0);
        String userId = "";
        String userName = "";
        String userPhoto = "";
        if (socialNetwork
                .equalsIgnoreCase(com.pmovil.soccer.net.ConnectionsWSMetegol.SOCIAL_NETWORK_FACEBOOK)) {
            userId = sharedPreferences.getString(
                    ShareData.USER_ID_FACEBOOK, "");
            userName = sharedPreferences.getString(
                    ShareData.USER_NAME_FACEBOOK, "");
            userPhoto = sharedPreferences.getString(
                    ShareData.PROFILE_IMAGE_URL_FACEBOOK, "");
        } else {
            userId = ""
                    + sharedPreferences.getLong(
                    ShareData.USER_ID_TWITTER, 0);
            userName = sharedPreferences.getString(
                    ShareData.USER_NAME_TWITTER, "");
            userPhoto = sharedPreferences.getString(
                    ShareData.PROFILE_IMAGE_URL_TWITTER, "");

        }
        com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                .getConnWsFutebol();
        connectionsWSMetegol.addCommentsFromMatchAndMinByMinPost(
                responseAddCommentHandler,matchId,minToMinId,
                userId, userName, userPhoto, socialNetwork, msg);

        etComment.setText("");
    }

}
