package com.pmovil.soccer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.components.AdapterGeneric;
import com.components.AnimateFirstDisplayListener;
import com.components.Item;
import com.components.TextViewPlus;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pmovil.soccer.Fragments.GameFragment;
import com.pmovil.soccer.Fragments.MediaFragment;
import com.pmovil.soccer.ItemOption.OptionData;
import com.pmovil.soccer.SubscriptionDialog.SubscriptionsListener;
import com.pmovil.soccer.entities.FootballGame;
import com.pmovil.soccer.entities.Game;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;
import com.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends ListFragment {

    private static final String TAG = "MenuFragment";
    private static ProgressDialog progressDialog = null;
    private List<Item> items = null;
    private SparseArray<Fragment> fragmentsStore = null;
    private View view = null;
    private SharedPreferences sharedPreferences;
    private boolean byNews = false;
    private ImageView ivTeam;
    private TextViewPlus tvTeam;
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private ConnectionHandlerResponseBody responseCheckBuyingProcess = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            if (progressDialog != null)
                progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                // 'SUBSCRIBED' o 'ALREADY_SUBSCRIBED
                String status = jsonObject.optString("status", "");
                if (status.equalsIgnoreCase("SUBSCRIBED")
                        || status.equalsIgnoreCase("ALREADY_SUBSCRIBED")) {
                    String celco = jsonObject.optString("celco");
                    String msisdn = jsonObject.getString("msisdn");

                    Editor editor = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                            .getEditor();
                    editor.putString(com.pmovil.soccer.net.ConnectionsWSMetegol.CELCO_VALUE, celco);
                    editor.putString(com.pmovil.soccer.net.ConnectionsWSMetegol.MSISDN_VALUE, msisdn);
                    editor.putLong(com.pmovil.soccer.net.ConnectionsWSMetegol.DATE_VALUE,
                            System.currentTimeMillis());
                    editor.commit();
                    Fragment fragment;

                    fragment = new MediaFragment();
                    fragmentsStore.append(Options.MEDIA.ordinal(), fragment);

                    /*FBARBIERI
                    if (byNews) {
                        fragment = new NewsFragment();
                        fragmentsStore.append(Options.NEWS.ordinal(), fragment);
                    } else {
                        fragment = new VideosFragment();
                        fragmentsStore.append(Options.VIDEOS.ordinal(),
                                fragment);
                    }
                    */

                    switchFragment(fragment);
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
        }

        public void onError(String response) {
            //Log.i(TAG, response);
            if (progressDialog != null)

                progressDialog.dismiss();

        }
    };
    private ConnectionHandlerResponseBody responseCheckCobrandingSubscriptions = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            //Log.i(TAG, response);
            if (progressDialog != null)
                progressDialog.dismiss();
            Editor editor = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                    .getEditor();

            if (response.contains("\"status\":\"SUBSCRIBED\"")) {
                editor.putLong(com.pmovil.soccer.net.ConnectionsWSMetegol.DATE_VALUE,
                        System.currentTimeMillis());
                editor.commit();
                Fragment fragment;

                fragment = new MediaFragment();
                fragmentsStore.append(Options.MEDIA.ordinal(), fragment);

                    /*FBARBIERI
                    if (byNews) {
                        fragment = new NewsFragment();
                        fragmentsStore.append(Options.NEWS.ordinal(), fragment);
                    } else {
                        fragment = new VideosFragment();
                        fragmentsStore.append(Options.VIDEOS.ordinal(),
                                fragment);
                    }
                    */

                switchFragment(fragment);
            } else {
                editor.putString(com.pmovil.soccer.net.ConnectionsWSMetegol.CELCO_VALUE, null);
                editor.putString(com.pmovil.soccer.net.ConnectionsWSMetegol.MSISDN_VALUE, null);
                editor.putLong(com.pmovil.soccer.net.ConnectionsWSMetegol.DATE_VALUE, 0);
                editor.commit();
            }
        }

        public void onError(String response) {
            //Log.i(TAG, response);
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    };
    private MainActivity mainActivity;
    private SubscriptionsListener subscriptionsListener = new SubscriptionsListener() {

        public void finishLogin() {

            String sessionID = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                    .getSharedPreferences()
                    .getString(com.pmovil.soccer.net.ConnectionsWSMetegol.SESSION_ID_VALUE, null);
            if (sessionID == null)
                return;
            if (progressDialog == null) {
                if (mainActivity != null) {
                    progressDialog = ProgressDialog.show(mainActivity, "",
                            getString(R.string.LoadingKey), true);
                } else {
                    progressDialog = ProgressDialog.show(getActivity(), "",
                            getString(R.string.LoadingKey), true);
                }
            }
            progressDialog.setCancelable(false);
            progressDialog.show();
            com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                    getActivity()).getConnWsFutebol();
            connWsFutebol.postCheckBuyingProcess(responseCheckBuyingProcess,
                    sessionID);
        }

        public void errorLogin() {

        }
    };
    private ConnectionHandlerResponseBody responseSessionID = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            if (progressDialog != null)
                progressDialog.dismiss();
            try {
                JSONObject json = new JSONObject(response);
                String sessionId = json.optString("sessionId", null);
                Editor editor = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                        .getEditor();
                editor.putString(com.pmovil.soccer.net.ConnectionsWSMetegol.SESSION_ID_VALUE,
                        sessionId);
                editor.commit();
                String urlRedirect = json.optString("urlRedirect", null);
                SubscriptionDialog subscriptionDialog = new SubscriptionDialog(
                        getActivity(), Uri.parse(urlRedirect).toString());
                subscriptionDialog
                        .setSubscriptionListener(subscriptionsListener);

                subscriptionDialog.show();
                //Log.i(TAG, response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void onError(String response) {
            if (progressDialog != null)
                progressDialog.dismiss();
            //Log.i(TAG, response);
        }
    };

    ;

    public MenuFragment() {
        fragmentsStore = new SparseArray<Fragment>();

    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void appendFixtureFragment(Fragment fragment) {
        fragmentsStore.append(Options.FIXTURE.ordinal(), fragment);
    }

    public void appendFragment(int ordinal, Fragment fragment) {
        fragmentsStore.append(ordinal, fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null)
            return view;

        view = inflater.inflate(R.layout.list, null);

        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageForEmptyUri(R.drawable.logo_3)
                    .showImageOnFail(R.drawable.logo_3).cacheInMemory()
                    .cacheOnDisc().build();

        sharedPreferences = getActivity().getSharedPreferences("Team",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("JsonTeamSelected", "");
        Team teamStored = gson.fromJson(json, Team.class);

        ivTeam = (ImageView) view.findViewById(R.id.iv_selected_team);
        tvTeam = (TextViewPlus) view.findViewById(R.id.tv_equipo);

        if(teamStored!=null){

            int width = ivTeam.getLayoutParams().width;
            int height = ivTeam.getLayoutParams().height;

            String urlImage = teamStored.getEmblem().replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);;

            imageLoader.displayImage(urlImage, ivTeam,
                    options, animateFirstListener);

            tvTeam.setText(teamStored.getValue());

        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    public void loadData() {
        generateMenuListItems();
        if (!(getListAdapter() instanceof AdapterGeneric)) {
            AdapterGeneric adapGeneric = new AdapterGeneric(getActivity(),
                    items);
            setListAdapter(adapGeneric);
            getListView().setDividerHeight(0);
        } else {
            ((AdapterGeneric) getListAdapter()).setItemsList(items);
            ((AdapterGeneric) getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Fragment newContent = null;
        if (items != null && items.size() > position) {
            if (items.get(position) instanceof ItemOption) {
                Options option = ((OptionData) items.get(position).getData())
                        .getOption();
                newContent = getNewFragmentToChange(option);
            }
        }
        if (newContent != null) {
            // if (newContent instanceof NewsFragment
            // || newContent instanceof VideosFragment) {
            // loadData();
            // byNews = newContent instanceof NewsFragment;
            // if (checkSubscriptionState()) {
            //
            // if (checkCobrandingSubscriptions())
            // switchFragment(newContent);
            //
            // }
            //
            // } else {
            switchFragment(newContent);

            // }
        }
    }

    public void changeItemFromButton(Options option) {
        Fragment newContent = null;
        newContent = getNewFragmentToChange(option);
        if (newContent != null)
            switchFragmentNow(newContent);
    }

    private Fragment getNewFragmentToChange(Options option) {
        com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(getActivity());
        //mainActivity.containerBanner.setVisibility(View.VISIBLE);
        switch (option) {
            case TEAM:
                if (com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getGameSelectedId() <= 0) {
                    Toast.makeText(getActivity(), R.string.noSelectGame,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                /*
                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(), new TeamFragment());*/

                return new TeamFragment();
                //break;

            case MIN_TO_MIN:
                if (com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getGameSelectedId() <= 0) {
                    Toast.makeText(getActivity(), R.string.noSelectGame,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                /*
                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(), new MinToMinFragment());*/
                return new MinToMinFragment();
                //break;
            /*
            case MATCH_TWITTER:
                if (ResourcesMetegol.getInstance(getActivity()).getGameSelectedId() <= 0) {
                    Toast.makeText(getActivity(), R.string.noSelectGame,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(),
                            new MatchTwitterFragment());
                break;
                */
            case GAME:

                if (com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getGameSelectedId() <= 0) {
                    Toast.makeText(getActivity(), R.string.noSelectGame,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(),
                            //new StatisticsFragment());
                              new GameFragment());*/
                mainActivity.containerBanner.setVisibility(View.GONE);
                return new GameFragment();
                //break;

            /*FBARBIERI
            case VIDEOS:
                resourcesMetegol.clearGame();
                if (fragmentsStore.get(option.ordinal()) == null)

                    //fragmentsStore.append(option.ordinal(), new VideosFragment());
                    fragmentsStore.append(option.ordinal(), new MediaFragment());
                break;
            */
            case FIXTURE:
                resourcesMetegol.clearGame();

                /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(),

                            new FixtureFragment());*/
                FixtureFragment fragFixture = new FixtureFragment();
                Bundle args = new Bundle();
                args.putBoolean("showInterstitial",true);
                fragFixture.setArguments(args);

                mainActivity.containerBanner.setVisibility(View.VISIBLE);
                return fragFixture;

                //break;
            case POSITIONS:
                resourcesMetegol.clearGame();

                mainActivity.containerBanner.setVisibility(View.VISIBLE);
                return new PositionsScorersAverageFragment();

            /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(),
                            new PositionsScorersAverageFragment());
                ((PositionsScorersAverageFragment) fragmentsStore.get(option
                        .ordinal()))
                        .setPage(PositionsScorersAverageFragment.POSITION);*/

            /*
            case SCORERS:
                resourcesMetegol.clearGame();

                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(),
                            new PositionsScorersAverageFragment());
                ((PositionsScorersAverageFragment) fragmentsStore.get(option
                        .ordinal()))
                        .setPage(PositionsScorersAverageFragment.SCORERS);
                break;
            case AVERAGE:
                resourcesMetegol.clearGame();

                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(),
                            new PositionsScorersAverageFragment());
                ((PositionsScorersAverageFragment) fragmentsStore.get(option
                        .ordinal()))
                        .setPage(PositionsScorersAverageFragment.AVERAGE);
                break;*/

            /* FBARBIERI
            case NEWS:
                resourcesMetegol.clearGame();

                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(), new NewsFragment());
                break;
             */

            case SCORERS:
                break;
            case BET:
                break;
            case AVERAGE:
                break;
            case MEDIA:
                resourcesMetegol.clearGame();

                /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(), new MediaFragment());*/
                mainActivity.containerBanner.setVisibility(View.VISIBLE);
                return new MediaFragment();
                //break;

            /*
            case BET:
                resourcesMetegol.clearGame();

                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(), new BetFragment());
                break;*/

            /*FBARBIERI
            case BATTLES:
                resourcesMetegol.clearGame();
                if (fragmentsStore.get(option.ordinal()) == null)
                    fragmentsStore.append(option.ordinal(), new BattlesFragment());
                mainActivity.containerBanner.setVisibility(View.GONE);
                break;
            *//*
            case CHANGE_TOURNAMENT:

                resourcesMetegol.clearGame();

                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(),
                            new ChangeTournamentFragment());
                break;
            */

            /*case UPCOMING:

                resourcesMetegol.clearGame();*/

                /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(), new UpcomingFragment());*/
                //return new UpcomingFragment();
                //break;
            case SETTING:

                resourcesMetegol.clearGame();

                /*
                if (fragmentsStore.get(option.ordinal()) == null)

                    fragmentsStore.append(option.ordinal(), new SettingFragment());*/
                mainActivity.containerBanner.setVisibility(View.VISIBLE);
                return new SettingFragment();
                //break;
            default:
                return null;
        }
       return null;
    }

    private boolean checkSubscriptionState() {
        SharedPreferences sharedPreference = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                getActivity()).getSharedPreferences();
        if (sharedPreference.getString(com.pmovil.soccer.net.ConnectionsWSMetegol.MSISDN_VALUE, "")
                .equalsIgnoreCase("")) {

            if (progressDialog == null) {
                if (mainActivity != null) {
                    progressDialog = ProgressDialog.show(mainActivity, "",
                            getString(R.string.LoadingKey), true);
                } else {
                    progressDialog = ProgressDialog.show(getActivity(), "",
                            getString(R.string.LoadingKey), true);
                }
            }
            progressDialog.setCancelable(false);
            progressDialog.show();
            com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getConnWsFutebol()
                    .postStartBuying(responseSessionID);
            return false;
        }

        return true;
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof MainActivity) {
            MainActivity fca = (MainActivity) getActivity();
            fca.getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
            fca.switchContent(fragment);
            //fca.refreshMenu();
        }
    }

    private void switchFragmentNow(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof MainActivity) {
            MainActivity fca = (MainActivity) getActivity();
            fca.getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
            fca.switchContentNow(fragment);
            //fca.refreshMenu();
        }
    }

    private void generateMenuListItems() {
        items = new ArrayList<Item>();
        //ItemHeader header = new ItemHeader();

        ItemOption option = new ItemOption();
        OptionData data = new OptionData();
        if (com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getGameSelected() != null
                || com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                .getFootballGame() != null) {
            Game game = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                    .getGameSelected();
            String titleGame = "";
            if (game != null) {
                titleGame = game.getHomeTeam().getNameShort() + " X "
                        + game.getAwayTeam().getNameShort();
            }
            if (com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).getFootballGame() != null) {
                FootballGame footballGame = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                        getActivity()).getFootballGame();
                List<Team> teams = footballGame.getTeams();
                String home = "";
                String away = "";
                for (Team team : teams) {
                    if (team.getHomeOrAway().equalsIgnoreCase("visitante")) {
                        away = team.getNameShort();
                    } else {
                        home = team.getNameShort();

                    }
                }
                titleGame = home + " X " + away;
                //header.setData(titleGame);
            } else
                //header.setData(titleGame);

            if (titleGame.trim().equalsIgnoreCase(" X ")) {
                titleGame = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity())
                        .getTitleGame();
                //if (titleGame != null)
                    //header.setData(titleGame);

            } else {
                com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).setTitleGame(
                        titleGame);
            }
            //items.add(header);

            //
            /*
            data.setName(getString(R.string.MenuOptTeamsKey));
            data.setOption(Options.TEAM);
            option.setData(data.copy());
            items.add(option.copy());
            */
            //
            /*
            data.setOption(Options.GAME);
            data.setName(getString(R.string.MenuOptStatisticsKey));
            option.setData(data.copy());
            items.add(option.copy());
            //
            data.setOption(Options.MIN_TO_MIN);
            data.setName(getString(R.string.MenuOptMinByMinKey));*/


            /*
            if (game != null && game.getState() != null
                    && game.getState().getId() == 100) {
                data.setLast(true);
                option.setData(data.copy());
                items.add(option.copy());
            } else {*/

                /*
                option.setData(data.copy());
                items.add(option.copy());

                data.setOption(Options.MATCH_TWITTER);
                data.setLast(true);
                data.setName(getString(R.string.MenuOptTwitterMatchKey)
                        .toUpperCase());
                option.setData(data.copy());
                items.add(option.copy());
            */
            //}

        }
        //

        /*header = new ItemHeader();
        if (ResourcesMetegol.getInstance(getActivity())
                .getChampionshipByIdSelected() != null)
            header.setData(ResourcesMetegol.getInstance(getActivity())
                    .getChampionshipByIdSelected().getValue());

        else
            header.setData(getString(R.string.header_championship));
        items.add(header);*/
        data = new OptionData();
        //
        //

        // data.setOption(Options.BET);
        // data.setName(getString(R.string.MenuOptBetsKey));
        // option.setData(data.copy());
        // items.add(option.copy());
        //
        data.setOption(Options.FIXTURE);
        data.setName(getString(R.string.MenuOptFixtureKey));
        option.setData(data.copy());
        items.add(option.copy());
        //

        /*
        data.setOption(Options.UPCOMING);
        data.setName(getString(R.string.MenuOptUpcomingMatchesKey)
                .toUpperCase());
        option.setData(data.copy());
        items.add(option.copy());
        */
        //
        data.setOption(Options.POSITIONS);
        data.setName(getString(R.string.MenuOptPositionsKey));
        option.setData(data.copy());
        items.add(option.copy());
        //
        /*
        data.setOption(Options.SCORERS);
        data.setName(getString(R.string.MenuOptScorersKey));
        option.setData(data.copy());
        items.add(option.copy());
        //

        data.setOption(Options.AVERAGE);
        data.setName(getString(R.string.MenuOptAveragesKey));
        option.setData(data.copy());
        items.add(option.copy());
        //
        */
        /* FBARBIERI
        data.setOption(Options.NEWS);
        data.setName(getString(R.string.MenuOptNewsKey));
        option.setData(data.copy());
        items.add(option.copy());
        */
        data.setOption(Options.MEDIA);
        data.setName(getString(R.string.MenuOptMediaKey));
        option.setData(data.copy());
        items.add(option.copy());
        //

        /* FBARBIERI
        data.setOption(Options.VIDEOS);
        data.setName(getString(R.string.MenuOptVideosKey));
        option.setData(data.copy());
        */



        /*
        List<Championship> championship = ResourcesMetegol.getInstance(
                getActivity()).getChampionships();
        if (championship != null && championship.size() > 1) {
            option.setData(data.copy());
            items.add(option.copy());
            data.setOption(Options.CHANGE_TOURNAMENT);
            data.setName(getString(R.string.MenuOptSelectionKey));
            // data.setLast(true);
            option.setData(data.copy());
            items.add(option.copy());
        } else {
            // data.setLast(true);
            option.setData(data.copy());
            items.add(option.copy());
        }*/

        //
        data.setOption(Options.SETTING);
        data.setName(getString(R.string.MenuOptNotificationKey));
        option.setData(data.copy());
        items.add(option.copy());
        //

        /*FBARBIERI
        data.setOption(Options.BATTLES);
        data.setName(getString(R.string.MenuOptBattlesKey));
        data.setLast(true);
        option.setData(data.copy());
        items.add(option.copy());*/

    }

    private boolean checkCobrandingSubscriptions() {
        SharedPreferences preferences = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                getActivity()).getSharedPreferences();
        // Date lastUpdate = new Date(preferences.getLong(
        // ConnectionsWSMetegol.DATE_VALUE, 0));
        // Date currenteDate = new Date();
        // int days = Days.daysBetween(new DateTime(lastUpdate),
        // new DateTime(currenteDate)).getDays();
        // if (days >= 1) {
        if (progressDialog == null) {
            if (mainActivity != null) {
                progressDialog = ProgressDialog.show(mainActivity, "",
                        getString(R.string.LoadingKey), true);
            } else {
                progressDialog = ProgressDialog.show(getActivity(), "",
                        getString(R.string.LoadingKey), true);
            }
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
        SharedPreferences sharedPreferences = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(
                getActivity()).getSharedPreferences();

        String msisdn = sharedPreferences.getString(
                com.pmovil.soccer.net.ConnectionsWSMetegol.MSISDN_VALUE, "");
        String celco = sharedPreferences.getString(
                com.pmovil.soccer.net.ConnectionsWSMetegol.CELCO_VALUE, "");

        com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(getActivity())
                .getConnWsFutebol()
                .postCheckCobradingSubscription(
                        responseCheckCobrandingSubscriptions, msisdn, celco);
        return false;
        // }

        // return true;

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

    public enum Options {

        TEAM, MIN_TO_MIN, GAME, FIXTURE, UPCOMING, POSITIONS, SCORERS, BET, AVERAGE, MEDIA, SETTING, CHANGE_TOURNAMENT, MATCH_TWITTER
    }

}
