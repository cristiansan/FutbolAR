package com.pmovil.soccer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.components.AnimateFirstDisplayListener;
import com.components.TextViewPlus;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pmovil.soccer.entities.Team;

public class TeamSelectedActivity extends Activity {

    private Button btnContinue;
    private TextViewPlus tvChangeTeam;
    private TextViewPlus tvFanTeam;
    private ImageView ivShield;
    private SharedPreferences sharedPreferences;
    private Team teamSelected;
    private static DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selected);

        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageForEmptyUri(R.drawable.logo_3)
                    .showImageOnFail(R.drawable.logo_3).cacheInMemory()
                    .cacheOnDisc().build();

        tvFanTeam = (TextViewPlus) findViewById(R.id.tv_fan_of);
        btnContinue = (Button) findViewById(R.id.btn_continue);
        tvChangeTeam = (TextViewPlus) findViewById(R.id.tv_change_team);
        ivShield = (ImageView) findViewById(R.id.iv_team_shield);

        teamSelected = (Team) getIntent().getExtras().get("Team");

        if(teamSelected !=null) {

            tvFanTeam.setText(teamSelected.getValue());

            int width = ivShield.getLayoutParams().width;
            int height = ivShield.getLayoutParams().height;

            String urlImage = teamSelected.getEmblem().replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);;

            imageLoader.displayImage(urlImage, ivShield,
                    options, animateFirstListener);

        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SAVE SELECTED TEAM IN SHAREDPREFERENCES
                sharedPreferences = getSharedPreferences("Team", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gsonTeam = new Gson();
                String jsonTeam = gsonTeam.toJson(teamSelected);
                prefsEditor.putString("JsonTeamSelected", jsonTeam);
                prefsEditor.commit();

                TeamSelectedActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        tvChangeTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TeamSelectedActivity.this.finish();
                Intent intent = new Intent(getApplicationContext(), SelectTeamActivity.class);
                startActivity(intent);
            }
        });
    }
}
