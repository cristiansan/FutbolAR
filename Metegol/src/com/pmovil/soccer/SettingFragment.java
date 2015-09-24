package com.pmovil.soccer;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.components.AnimateFirstDisplayListener;
import com.components.ShareData;
import com.components.TextViewPlus;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.pmovil.soccer.entities.Championship;
import com.pmovil.soccer.entities.Team;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends FragmentBase  {

    private static final String TAG = "SettingFragment";
    private Context context;
    private com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = null;
    private ImageView ivUser;
    private TextViewPlus tvUser;
    private TextViewPlus tvLogout;
    private TextViewPlus tvChangeTeam;
    private TextViewPlus tvChangeLanguage;
    private TextViewPlus tvChangeTournament;
    private TextViewPlus tvLanguage;
    private ImageView ivTournament;
    private TextViewPlus tvTournament;
    private String username;
    private Switch swNotifications;
    private SharedPreferences sharedPreferences;
    private SparseArray<Fragment> fragmentsStore = null;
    private static DisplayImageOptions options;
    private String userImage;
    private ImageView ivTeam;
    private TextViewPlus tvTeam;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private View view;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        hideLoading();

        context = getActivity().getApplicationContext();
        resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context);

        view = inflater.inflate(R.layout.activity_settings, container, false);

        tvLogout = (TextViewPlus) view.findViewById(R.id.tv_logout);
        tvChangeTeam = (TextViewPlus) view.findViewById(R.id.tv_change_team);
        tvChangeLanguage = (TextViewPlus) view.findViewById(R.id.tv_change_language);
        tvChangeTournament = (TextViewPlus) view.findViewById(R.id.tv_change_tournamen);
        tvTournament = (TextViewPlus) view.findViewById(R.id.tv_tournament);
        ivTournament = (ImageView) view.findViewById(R.id.iv_change_tournament);
        swNotifications = (Switch) view.findViewById(R.id.notifications);
        ivUser = (ImageView) view.findViewById(R.id.iv_avatar);
        tvUser = (TextViewPlus) view.findViewById(R.id.tv_user_name);

        tvLanguage = (TextViewPlus) view.findViewById(R.id.tv_languaje);

        sharedPreferences = SettingFragment.this.getActivity().getSharedPreferences("Language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "");

        final SharedPreferences mSharedPreferences = getActivity().getApplicationContext()
                .getSharedPreferences(ShareData.SHARE_NAME, 0);

        if (mSharedPreferences.getString(ShareData.USER_ID_FACEBOOK, "").equalsIgnoreCase("")) {

            tvLogout.setText(getResources().getString(R.string.login));

        }else{
            tvLogout.setText(getResources().getString(R.string.logout));
        }
        //To show facebook data;

        if (mSharedPreferences.getString(ShareData.USER_NAME_FACEBOOK, "").equalsIgnoreCase("")) {

        }else{
            username = mSharedPreferences.getString(ShareData.USER_NAME_FACEBOOK, "");
            tvUser.setText(username);
        }

        if (mSharedPreferences.getString(ShareData.PROFILE_IMAGE_URL_FACEBOOK, "").equalsIgnoreCase("")) {

        }else{
            userImage = mSharedPreferences.getString(ShareData.PROFILE_IMAGE_URL_FACEBOOK, "");
            imageLoader.displayImage(userImage, ivUser,
                    options, animateFirstListener);
        }

        String localeLanguage = context.getResources().getConfiguration().locale.getDisplayLanguage();

        if(language.equalsIgnoreCase("")){
            tvLanguage.setText(localeLanguage.toUpperCase());

        }else{

            if(language.equalsIgnoreCase("es")){
                tvLanguage.setText("ESPAÑOL");
            } else if(language.equalsIgnoreCase("en")){
                tvLanguage.setText("ENGLISH");
            } else{
                tvLanguage.setText("PORTUGUES");
            }
        }


        if(GoogleCloudMessagingUtilities.isSoundNotification(context) &&
                GoogleCloudMessagingUtilities.isVibrationNotification(context))
            swNotifications.setChecked(true);

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

        ivTeam = (ImageView) view.findViewById(R.id.iv_team);
        tvTeam = (TextViewPlus) view.findViewById(R.id.tv_team);

        if(teamStored!=null){

            int width = ivTeam.getLayoutParams().width;
            int height = ivTeam.getLayoutParams().height;

            String urlImage = teamStored.getEmblem().replace(com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_REPLACE_IMG_URL,
                    "" + width + "x" + height + com.pmovil.soccer.entities.parser.ResourcesMetegol.VALUE_SCALE_INSIDE);;

            imageLoader.displayImage(urlImage, ivTeam,
                    options, animateFirstListener);

            tvTeam.setText(teamStored.getValue());

        }

        List<Championship> championship = resourcesMetegol
                .getChampionships();

        if (championship != null && championship.size() == 1) {
            tvTournament.setVisibility(View.INVISIBLE);
            ivTournament.setVisibility(View.INVISIBLE);
            tvChangeTournament.setVisibility(View.INVISIBLE);
        }

        /*
        Session session = Session.getActiveSession();
        if(session.isOpened()){
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        public void onCompleted(GraphUser user,
                                                Response response) {
                            // If the response is successful
                                if (user != null) {

                                    String profileName = user.getName();// user's
                                    String profileImage = ShareData.PROFILE_IMAGE_URL_FACEBOOK;

                                    tvUser.setText(profileName);
                                }
                        }
                    }
            );
        }*/

        swNotifications.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View view) {

                if (swNotifications.isChecked()) {

                    GoogleCloudMessagingUtilities.setSoundNotification(context, true);
                    GoogleCloudMessagingUtilities.setVibrationNotification(context, true);
                    swNotifications.setChecked(true);

                } else {

                    GoogleCloudMessagingUtilities.setSoundNotification(context, false);
                    GoogleCloudMessagingUtilities.setVibrationNotification(context, false);
                    swNotifications.setChecked(false);
                }
            }

        });

        tvChangeTournament.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                resourcesMetegol.clearGame();

                Fragment fgChangeTournament = new ChangeTournamentFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(android.R.id.content,fgChangeTournament);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

            }

        });

        tvChangeLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                CharSequence languages[] = new CharSequence[] {getResources().getString(R.string.language_spanish),
                        getResources().getString(R.string.language_english),
                        getResources().getString(R.string.language_portugues)};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.language));
                builder.setItems(languages, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selected) {

                        List<String> languages = new ArrayList<String>();
                        languages.add(0,"es");
                        languages.add(1,"en");
                        languages.add(2,"pt");

                        com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context).changeLenguageApp(languages.get(selected));

                        //SAVE SELECTED LANGUAGE IN SHAREDPREFERENCES
                        sharedPreferences = getActivity().getSharedPreferences("Language", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                        String language = languages.get(selected);
                        prefsEditor.putString("language", language);
                        prefsEditor.commit();

                        //To recreate app
                        Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);


                    }
                });
                builder.show();

            }
        });

        tvChangeTeam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectTeamActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mSharedPreferences = getActivity().getApplicationContext()
                        .getSharedPreferences(ShareData.SHARE_NAME, 0);
                if (mSharedPreferences.getString(ShareData.USER_NAME_FACEBOOK, "").equalsIgnoreCase("")) {

                    DialogFragment newFragment = CommentDialog.newInstance(1);
                    newFragment.setTargetFragment(SettingFragment.this, 1);
                    Bundle args = new Bundle();
                    args.putBoolean("showText", false);
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "CommentDialog");

                }else{

                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(
                            ShareData.PROFILE_IMAGE_URL_FACEBOOK, "");
                    e.putString(ShareData.USER_ID_FACEBOOK,
                            "");
                    e.putString(ShareData.USER_NAME_FACEBOOK,
                            "");
                    e.commit();

                    tvUser.setText("");
                    tvLogout.setText(R.string.login);
                    ivUser.setImageResource(R.drawable.avatar);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            SharedPreferences mSharedPreferences = getActivity().getApplicationContext()
                    .getSharedPreferences(ShareData.SHARE_NAME, 0);

            username = mSharedPreferences.getString(ShareData.USER_NAME_FACEBOOK, "");
            tvUser.setText(username);

            userImage = mSharedPreferences.getString(ShareData.PROFILE_IMAGE_URL_FACEBOOK, "");
            imageLoader.displayImage(userImage, ivUser,
                    options, animateFirstListener);

            tvLogout.setText(R.string.logout);


    }

}