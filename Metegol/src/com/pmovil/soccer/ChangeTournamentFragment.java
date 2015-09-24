package com.pmovil.soccer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pmovil.soccer.MenuFragment.Options;
import com.pmovil.soccer.entities.Championship;

public class ChangeTournamentFragment extends FragmentBase implements
        OnClickListener {

    private static final String TAG = "ChangeTournamentFragment";
    private View view;
    private LinearLayout containerOption = null;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideLoading();
        if (resourcesMetegol == null)
            resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                    .getInstance(getActivity());
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.change_torunament_fragment, container,
                false);
        containerOption = (LinearLayout) view
                .findViewById(R.id.container_button_select_championship);
        for (int indexChampionship = 0; indexChampionship < resourcesMetegol
                .getChampionships().size(); indexChampionship++) {
            Championship championship = resourcesMetegol.getChampionships()
                    .get(indexChampionship);
            View view = inflater.inflate(R.layout.button, null);
            View viewButton = view.findViewById(R.id.iv_backgorud_button);
            viewButton.setTag(championship.getId());
            viewButton.setOnClickListener(this);
            TextView nameButton = (TextView) view.findViewById(R.id.tv_button);
            nameButton.setText(championship.getValue());
            containerOption.addView(view);

        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ////Log.i(TAG, "DestroyView");
        if (view != null) {

            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof Integer) {
            ////Log.i(TAG, "" + v.getTag());
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("appData",
                    Context.MODE_PRIVATE);
            Editor editor = sharedPreferences.edit();
            editor.putInt(com.pmovil.soccer.entities.parser.ResourcesMetegol.CHAMPIONCHIP_VALUE_ID,
                    (Integer) v.getTag());
            editor.commit();
            resourcesMetegol
                    .setIdChampionshipSelected((Integer) v.getTag());
            resourcesMetegol.clearResources();
            Activity act = getActivity();
            if (act != null && act instanceof MainActivity) {
                ((MainActivity) act).menuFragment
                        .changeItemFromButton(Options.FIXTURE);

            }

            this.getActivity().finish();
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
    }
}
