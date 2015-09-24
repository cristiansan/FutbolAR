package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.components.Item;
import com.pmovil.soccer.entities.Championship;

public class ItemChampionship implements Item {

    private static LayoutInflater mInflater = null;
    private Championship championship;

    public Object getData() {
        return championship;
    }

    public void setData(Object data) {
        if (data instanceof Championship) {
            championship = (Championship) data;
        }
    }

    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            if (mInflater == null) {
                mInflater = LayoutInflater.from(context);
            }
            view = mInflater.inflate(R.layout.championship_row, null);
            viewHolder.championshipNameTextView = (TextView) view
                    .findViewById(R.id.tv_selection_championship_row);
            viewHolder.background = view
                    .findViewById(R.id.container_champion_rows);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.championshipNameTextView.setText(championship.getValue());

        if (championship == null)
            return view;

        if (championship.getPosition() % 2 == 0)
            viewHolder.background
                    .setBackgroundResource(R.color.gray_notification_light);
        else {
            viewHolder.background
                    .setBackgroundResource(R.color.gary_notification_strong);
        }

        return view;
    }

    private static class ViewHolder {
        private View background;
        private TextView championshipNameTextView;
    }
}
