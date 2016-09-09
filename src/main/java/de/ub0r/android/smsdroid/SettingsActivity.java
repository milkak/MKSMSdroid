package de.ub0r.android.smsdroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.ub0r.android.lib.Utils;

/**
 * @author Aidan Follestad (afollestad)
 */
public class SettingsActivity extends ThemableActivity implements ColorChooserDialog.ColorCallback
{
    public static Toolbar mToolbar;
    public static String mTitle;
    SharedPreferences  Sp;
    public static String TAG = "Settings";


    @Override
    public void onColorSelection(int title, int color) {
        if (title == R.string.primary_color)
            getThemeUtils().primaryColor(color);
        else if (title == R.string.accent_color)
            getThemeUtils().accentColor(color);
        else
            getThemeUtils().thumbnailColor(color);
        recreate();
    }


    //public static class SettingsFragment extends PreferenceFragment  implements  MyAdapter.ColorCallback
    public static class SettingsFragment extends PreferenceFragment


    {
        SharedPreferences prefs;
        MyAdapter ColorCallback;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_userinterface);



            findPreference("base_theme").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //    ImageLoader.getInstance().clearMemoryCache();

                    final boolean dark = ThemeUtils.isDarkMode(getActivity());
                    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int preselect = 0;
                    if (prefs.getBoolean("true_black", false)) {
                        preselect = 2;
                    } else if (prefs.getBoolean("dark_mode", false)) {
                        preselect = 1;
                    }




                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.base_theme)
                            .items(R.array.base_themes)
                            .theme(dark ? Theme.DARK : Theme.LIGHT)
                            .itemsCallbackSingleChoice(preselect, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                    if (getActivity() == null) return false;
                                    SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                    switch (i) {
                                        default:
                                            prefs.remove("dark_mode").remove("true_black");
                                            break;
                                        case 1:
                                            prefs.remove("true_black")
                                                    .putBoolean("dark_mode", true);
                                            break;
                                        case 2:
                                            prefs.putBoolean("dark_mode", true)
                                                    .putBoolean("true_black", true);
                                            break;
                                    }
                                    prefs.commit();
                                    //    ImageLoader.getInstance().clearMemoryCache();
                                    getActivity().recreate();
                                return true;}
                            }).show();
                    return false;
                }
            });



           /* findPreference("skin").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    final int current = Integer.parseInt(prefs.getString("skin", "" + 6));

                    //    List<String> stations = new ArrayList<String>();
                    List<String> stations = new ArrayList<String>();


                    CharSequence[] sa1 = getActivity().getResources().getTextArray(R.array.skin);

                    for (int i = 0; i < sa1.length; i++) //clean previous selected
                    {
                        stations.add(sa1[i].toString());

                    }

                    final String[] colors = new String[]{
                            "#F44336",
                            "#e91e63",
                            "#9c27b0",
                            "#673ab7",
                            "#3f51b5",
                            "#2196F3",
                            "#03A9F4",
                            "#00BCD4",
                            "#009688",
                            "#4CAF50",
                            "#8bc34a",
                            "#FFC107",
                            "#FF9800",
                            "#FF5722",
                            "#795548",
                            "#212121",
                            "#607d8b",
                            "#004d40"
                    };




                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.skin)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })

                                    //.setAdapter()
                            .setSingleChoiceItems(R.array.skin, current, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    prefs.edit().putString("skin", "" + i).commit();
                                    dialogInterface.cancel();
                                    prefs.edit().putString("skin_color", colors[i]).apply();
                                    String skin = prefs.getString("skin_color", "#5677fc");
                                    mToolbar.setBackgroundColor(Color.parseColor(skin));

                                }
                            }).show();
//                    getActivity().recreate();
                    return false;
                }
            });*/

            ThemeUtils themeUtils = ((ThemableActivity) getActivity()).getThemeUtils();
            CabinetPreference primaryColor= (CabinetPreference) findPreference("primary_color");
            primaryColor.setColor(themeUtils.primaryColor(), resolveColor(getActivity(), R.attr.colorAccent));

            primaryColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ThemeUtils themeUtils = ((ThemableActivity) getActivity()).getThemeUtils();
                    new ColorChooserDialog().show(getActivity(), preference.getTitleRes(),
                            themeUtils.primaryColor());
                    return true;
                }
            });

         };



        public void onColorSelection(String color) {
            Toast.makeText(getActivity(), color, Toast.LENGTH_LONG).show();

            // getThemeUtils().thumbnailColor(color);
//        mCallback.ColorSelectionUpdate( color);
            //recreate();
        }
        public static int resolveColor(Context context, int color) {
            TypedArray a = context.obtainStyledAttributes(new int[]{color});
            int resId = a.getColor(0, context.getResources().getColor(R.color.cabinet_color));
            a.recycle();
            return resId;
        }



    };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity_custom);
            try {
        mToolbar = (Toolbar) findViewById(R.id.toolbarnew);
        mToolbar.setPopupTheme(getThemeUtils().getPopupTheme());

        //String skin = ConversationListActivity.Sp.getString("skin_color", "#5677fc");
            //    int skin = Sp.getInt("primary_color", 999999);


                mToolbar.setBackgroundColor(getThemeUtils().primaryColor());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ConversationListActivity.mmTitle);
        getFragmentManager().beginTransaction().replace(R.id.settings_content, new SettingsFragment()).commit();


            } catch (Exception e) {
                Log.e("Preferences", e.toString());
            }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            ConversationListActivity.mToolbar.setBackgroundColor(getThemeUtils().primaryColor());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}