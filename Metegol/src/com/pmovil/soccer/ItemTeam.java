package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.components.Item;
import com.pmovil.soccer.entities.Team;

import java.util.Set;

public class ItemTeam implements Item {

    private static LayoutInflater mInflater = null;
    private Team team = null;

    public Object getData() {
        return team;
    }

    public void setData(Object data) {
        if (data instanceof Team) {
            team = (Team) data;
        }
    }

    public View getView(View view, final Context context) {
        ViewHolder viewHolder = null;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            if (mInflater == null) {
                mInflater = LayoutInflater.from(context);
            }
            view = mInflater.inflate(R.layout.team_row, null);
            viewHolder.swithSelectorToggleButton = (ToggleButton) view
                    .findViewById(R.id.switch_team_row);
            viewHolder.teamNameTextView = (TextView) view
                    .findViewById(R.id.tv_selection_team_row);
            viewHolder.background = view
                    .findViewById(R.id.container_team_rows);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (team == null)
            return view;

        if (team.getPosition() % 2 == 0)
            viewHolder.background.setBackgroundResource(R.color.gray_notification_light);

        else {
            viewHolder.background
                    .setBackgroundResource(R.color.gary_notification_strong);

        }

        viewHolder.teamNameTextView.setText(team.getNc());
        viewHolder.swithSelectorToggleButton
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                                .getInstance(context);
                        if (isChecked) {
                            resourcesMetegol.addIdTeamByNotification(team
                                    .getId());
                        } else {
                            resourcesMetegol
                                    .removeIdTeamByNotification(team.getId());
                        }
                    }
                });
        com.pmovil.soccer.entities.parser.ResourcesMetegol resource = com.pmovil.soccer.entities.parser.ResourcesMetegol
                .getInstance(context);
        Set<Integer> idAddNotification = resource.getIdTeamsToAddNotification();
        Set<Integer> idRemoveNotification = resource
                .getIdTeamsToRemoveNotification();

        if (idAddNotification != null
                && idAddNotification.contains(team.getId())) {
            viewHolder.swithSelectorToggleButton.setChecked(true);
        } else if (idRemoveNotification != null
                && idRemoveNotification.contains(team.getId())) {
            viewHolder.swithSelectorToggleButton.setChecked(false);
        } else {
            viewHolder.swithSelectorToggleButton.setChecked(team
                    .isSubscribedForNotifications());
        }
        return view;
    }

    private static class ViewHolder {
        private View background;
        private TextView teamNameTextView;
        private ToggleButton swithSelectorToggleButton;
    }

}
