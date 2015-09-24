package com.pmovil.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.pmovil.soccer.entities.Video;
import com.pmovil.soccer.entities.parser.JsonParsers;
import com.pmovil.soccer.net.ManagerConnection.ConnectionHandlerResponseBody;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends FragmentBase {

    private static final String TAG = "VideosFragment";
    private PullToRefreshListView lvVideos = null;
    private List<Item> itemList = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private com.pmovil.soccer.net.ConnectionsWSMetegol connWsFutebol = null;
    private View view = null;
    private int count = 20;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private static DisplayImageOptions options;
    private ConnectionHandlerResponseBody addMoreVideosHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                resourcesMetegol.setVideoList(JsonParsers
                        .ParserVideos(new JSONArray(response)));
                loadListAdapter(false);
                //Log.i(TAG, "Video Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            lvVideos.onRefreshComplete();
        }

        public void onError(String response) {
            //Log.i(TAG, "error Video" + response);
            lvVideos.onRefreshComplete();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noConnection();
        }
    };
    private ConnectionHandlerResponseBody videosHandler = new ConnectionHandlerResponseBody() {

        public void onSuccess(String response) {
            try {
                com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity()).setVideoList(
                        JsonParsers.ParserVideos(new JSONArray(response)));
                loadListAdapter(true);
                //Log.i(TAG, "Video Successfull");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, response);
            hideLoading();
            setBlockSlidingMenu(false);
        }

        public void onError(String response) {
            //Log.i(TAG, "error Video" + response);
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
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(getActivity());
        if (connWsFutebol == null)
            connWsFutebol = resourcesMetegol.getConnWsFutebol();
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.videos_fragment, container, false);
        lvVideos = (PullToRefreshListView) view.findViewById(R.id.lv_videos);

        lvVideos.getRefreshableView().setDividerHeight(20);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkDataAndLoadAdapter();
    }

    private void checkDataAndLoadAdapter() {

        if (resourcesMetegol.getVideoList() == null) {
            connWsFutebol.getVideoPost(videosHandler,
                    resourcesMetegol.getIdChampionshipSelected());
            showLoading();
            setBlockSlidingMenu(true);
        } else {
            loadListAdapter(true);
        }
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

    private void loadListAdapter( boolean loadHeader) {

        ListView listview = lvVideos.getRefreshableView();

        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (lvVideos == null)
            return;
        if (itemList == null)
            itemList = new ArrayList<Item>();
        itemList.clear();
        List<Video> videoList = resourcesMetegol.getVideoList();
        if (videoList == null)
            return;
        for (int indexVideo = 1; indexVideo < videoList.size(); indexVideo++) {
            Item item = new ItemVideo();
            item.setData(videoList.get(indexVideo));
            itemList.add(item);

        }

        if(loadHeader){
            final View header_videos = (View) inflater.inflate(R.layout.videos_header, lvVideos, false);

            ImageView image_video = (ImageView) header_videos.findViewById(R.id.iv_thumbnail_video);
            TextViewPlus desc_video = (TextViewPlus) header_videos.findViewById(R.id.tv_desc_video);

            header_videos.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
            desc_video.setText(videoList.get(0).getDesc());

            int width = image_video.getLayoutParams().width;
            int height = (int) (image_video.getLayoutParams().height - ImageUtil
                    .getPixel(getActivity().getApplicationContext(), 3));

            String urlImageVideo = videoList.get(0).getThumbnail().replace(
                    com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);

            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageForEmptyUri(R.drawable.logo_3)
                    .showImageOnFail(R.drawable.logo_3).cacheInMemory()
                    .cacheOnDisc().build();
            imageLoader.displayImage(urlImageVideo, image_video,
                    options, animateFirstListener);

            listview.addHeaderView(header_videos);
        }


        if (itemList.isEmpty()) {
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity)
                ((MainActivity) act).noVideoData();
            ;
        }

        if (listview.getAdapter() instanceof AdapterGeneric) {
            AdapterGeneric adapter = (AdapterGeneric) listview.getAdapter();
            adapter.setItemsList(itemList);
            adapter.notifyDataSetChanged();
        } else
        listview.setAdapter(new AdapterGeneric(getActivity(), itemList));
        lvVideos.setMode(Mode.PULL_FROM_END);
        lvVideos.setOnRefreshListener(new OnRefreshListener<ListView>() {

            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                connWsFutebol
                        .getVideoPost(addMoreVideosHandler,
                                resourcesMetegol
                                        .getIdChampionshipSelected(), count
                        );
                count += 10;

            }
        });
        lvVideos.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {

                Intent intent = new Intent(getActivity(), VideoActivity.class);

                if (itemList.size() > pos) {
                    String url = ((Video) itemList.get(pos).getData()).getUrl();
                    if (url.contains("youtube")) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.putExtra("force_fullscreen", true);
                        i.setData(Uri.parse(url));
                        startActivity(i);

						/*
                         * int currentapiVersion =
						 * android.os.Build.VERSION.SDK_INT; if
						 * (currentapiVersion <
						 * android.os.Build.VERSION_CODES.HONEYCOMB) { try {
						 * intent = new Intent(Intent.ACTION_VIEW);
						 * intent.setPackage("com.google.android.youtube");
						 * intent.setData(Uri.parse(url));
						 * startActivity(intent); } catch
						 * (ActivityNotFoundException e) { intent = new
						 * Intent(Intent.ACTION_VIEW);
						 * intent.setData(Uri.parse(url));
						 * startActivity(intent); }
						 *
						 * } else { intent = new Intent(getActivity(),
						 * VideoWebActivity.class);
						 * intent.putExtra(VideoWebActivity.URL, url);
						 * startActivity(intent); }
						 */
                    } else {

                        intent.putExtra("video", url);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
