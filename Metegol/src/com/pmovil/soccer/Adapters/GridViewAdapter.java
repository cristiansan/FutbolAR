package com.pmovil.soccer.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.pmovil.soccer.Cache.BitmapLruCache;
import com.pmovil.soccer.GoogleCloudMessagingUtilities;
import com.pmovil.soccer.R;
import com.pmovil.soccer.TeamSelectedActivity;
import com.pmovil.soccer.entities.Team;
import com.pmovil.soccer.net.ManagerConnection;

import java.util.List;

public class GridViewAdapter extends BaseAdapter{

    private final List<Team> teamList;
    private Context mContext;
    private String urlImage;
    private RequestQueue mRequestQueue;
    private static LayoutInflater mInflater = null;
    private com.android.volley.toolbox.ImageLoader mImageLoader;
    public ManagerConnection.ConnectionResponse response = null;
    public Team data;

        public GridViewAdapter(Context c, List<Team> teamList) {
            mContext = c;
            this.teamList = teamList;
            mRequestQueue = Volley.newRequestQueue(this.mContext);
            mImageLoader = new com.android.volley.toolbox.ImageLoader(mRequestQueue, new BitmapLruCache());
        }

        public int getCount() {
            return teamList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                viewHolder = new ViewHolder();
                if (mInflater == null)
                    mInflater = LayoutInflater.from(mContext);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.networkimageview, null);

                viewHolder.ivShieldTeam = (NetworkImageView) convertView.findViewById(R.id.ivShield);
                viewHolder.viewDivider = (View) convertView.findViewById(R.id.strut);

                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();

            }


                int width = viewHolder.ivShieldTeam.getLayoutParams().width;
                int height = viewHolder.ivShieldTeam.getLayoutParams().height;

                urlImage = teamList.get(position).getEmblem().replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                        "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);
                ;

                viewHolder.ivShieldTeam.setImageUrl(urlImage, mImageLoader);

                viewHolder.ivShieldTeam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        data = teamList.get(position);

                        new UpdateNotification().execute();

                        Intent intent = new Intent(mContext, TeamSelectedActivity.class);
                        intent.putExtra("Team", data);
                        mContext.startActivity(intent);

                    }
                });

            return convertView;
        }

            private class UpdateNotification extends AsyncTask<Void, Void, Void> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {

                    com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                            .getInstance(GridViewAdapter.this.mContext);
                    com.pmovil.soccer.net.ConnectionsWSMetegol connectionsWSMetegol = resourcesMetegol
                            .getConnWsFutebol();
                    String deviceToken = GoogleCloudMessagingUtilities.getRegistrationId(GridViewAdapter.this.mContext);
                    if (deviceToken.trim().equalsIgnoreCase("")) {
                        return null;
                    }

                    //Remove all teams notification
                    if (teamList != null) {
                        for (Team team : teamList) {

                            int teamId = team.getId();
                            response = connectionsWSMetegol.setConfigPush(deviceToken,
                                    "0", "" + teamId);
                            try {
                                //Log.i(TAG, new String(response.getBodyResponse(), "UTF-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    int teamToAdd = data.getId();
                    response = connectionsWSMetegol.setConfigPush(deviceToken,
                            "1", "" + teamToAdd);
                    try {
                        //Log.i(TAG, new String(response.getBodyResponse(), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                }
            }

    private static class ViewHolder {
        private NetworkImageView ivShieldTeam;
        private View viewDivider;
    }
}
