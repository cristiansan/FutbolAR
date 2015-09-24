package com.pmovil.soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.components.Item;
import com.components.TextViewPlus;
import com.pmovil.soccer.MenuFragment.Options;

public class ItemOption implements Item {

    private static LayoutInflater mInflater = null;
    private OptionData optionData = null;

    public ItemOption() {
    }

    public ItemOption(ItemOption itemOption) {
        optionData = itemOption.optionData;
    }

    public ItemOption copy() {
        return new ItemOption(this);
    }

    public Object getData() {
        return optionData;
    }

    public void setData(Object data) {
        if (data instanceof OptionData) {
            optionData = (OptionData) data;
        }
    }

    public View getView(View view, Context context) {
        ViewHolder viewHolder;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            if (mInflater == null)
                mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.option_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextViewPlus) view
                    .findViewById(R.id.option_title);
            //FBARBIERI viewHolder.dividerLine = view.findViewById(R.id.line_divider);
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.option_icon);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        if (optionData != null) {
            loadIcon(optionData, viewHolder.ivIcon);
            /* FBARBIERI
            if (optionData.last)
                viewHolder.dividerLine.setVisibility(View.INVISIBLE);
            else
                viewHolder.dividerLine.setVisibility(View.VISIBLE);

            */
            viewHolder.textView.setText(optionData.name);
        }
        return view;
    }

    private void loadIcon(OptionData data, ImageView image) {
        Options option = data.getOption();
        switch (option) {

            //FBARBIERI Oculte los iconos anteriores

            case TEAM:
                //image.setImageResource(R.drawable.icon_times);
                break;
            case MIN_TO_MIN:
                //image.setImageResource(R.drawable.icon_minuto);
                break;
            case GAME:
                //image.setImageResource(R.drawable.icon_estadisticas);
                break;
            /*FBARBIERI
            case VIDEOS:
                image.setImageResource(R.drawable.icon_video);
                break;
            */
            /*
            case UPCOMING:
                //image.setImageResource(R.drawable.);
                break;*/
            case FIXTURE:
                image.setImageResource(R.drawable.fixture_icon);
                break;
            case SETTING:
                image.setImageResource(R.drawable.setting_icon);
                break;

            /*FBARBIERI
            case BATTLES:
                image.setImageResource(R.drawable.batallas_metegol);
                break;*/

            case POSITIONS:
                image.setImageResource(R.drawable.posiciones_icon);
                break;
            case SCORERS:
                //image.setImageResource(R.drawable.icon_artilharia);
                break;
            case AVERAGE:
                //image.setImageResource(R.drawable.icon_estadisticas);
                break;
            case MEDIA:
                image.setImageResource(R.drawable.news_icon);
                break;
            /*FBARBIERI
            case NEWS:
                image.setImageResource(R.drawable.icon_noticias);
                break;
            */
            case BET:
                //image.setImageResource(R.drawable.icon_bolao);
                break;
            case CHANGE_TOURNAMENT:
                //image.setImageResource(R.drawable.icon_change_tournament);
                break;
            case MATCH_TWITTER:
                //image.setImageResource(R.drawable.icon_match_twitter);
                break;
            default:
                break;
        }
    }

    private static class ViewHolder {
        private TextViewPlus textView;
        //FBARBIERI private View dividerLine;
        private ImageView ivIcon;
    }

    public static class OptionData {

        private String name = new String();
        private Options option = Options.FIXTURE;
        private boolean last = false;

        public OptionData() {

        }

        public OptionData(OptionData optionData) {
            super();
            this.name = optionData.name;
            this.option = optionData.option;
            this.last = optionData.last;
        }

        public OptionData copy() {
            return new OptionData(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Options getOption() {
            return option;
        }

        public void setOption(Options option) {
            this.option = option;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

    }

}
